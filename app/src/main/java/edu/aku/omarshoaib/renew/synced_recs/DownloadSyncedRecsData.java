package edu.aku.omarshoaib.renew.synced_recs;

/**
 * DOWNLOAD SYNCED RECS DATA
 */

import static edu.aku.omarshoaib.renew.global.AppConstants.IS_CALL_ENCRYPTED;
import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.SyncAC;
import edu.aku.omarshoaib.renew.adapter.SyncAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.database.DBTransactionQueue;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.EntryLog;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.webcall.UploadData;
import edu.aku.omarshoaib.renew.webcall.web_client.CryptoUtil;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebCall;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class DownloadSyncedRecsData {
    private final SyncAC activity;
    private final WebAPI webAPI;
    private final WebCall webCall;
    private final SyncAdapter syncRVAdapter;
    private final List<SyncModel> syncTablesList;
    private final AppDatabase appDatabase;
    // This boolean is used for date checking device date validation
    // and show popup only once
    private boolean isDateError;

    // main table i.e. form1 table that will be used to generate
    // display list and getting linked tables
    private final HashMap<String, String> whereClauses;

    // If delete data query added in transaction queue then
    // set this variable to true.
    boolean isDeleteQueryAddedInTransaction;

    private final DBTransactionQueue addTransactionQueue, deleteTransactionQueue;

    public DownloadSyncedRecsData(SyncAC activity, SyncAdapter syncRVAdapter,
                                  List<SyncModel> syncTablesList,
                                  HashMap<String, String> whereClauses) {
        this.activity = activity;
        webAPI = WebClient.getInstance(activity).getWebAPI();
        webCall = new WebCall(activity, iWebCallback);
        this.syncRVAdapter = syncRVAdapter;
        this.syncTablesList = syncTablesList;
        appDatabase = AppDatabase.getDBInstance();
        this.whereClauses = whereClauses;

        SupportSQLiteDatabase db = appDatabase.getOpenHelper().getWritableDatabase();
        // For deleting previously stored records
        deleteTransactionQueue = new DBTransactionQueue(db, null);
        // For adding newly downloaded records
        addTransactionQueue = new DBTransactionQueue(db, callback);
        isDeleteQueryAddedInTransaction = false;
    }

    /**
     * DOWNLOAD DATA FROM SERVER
     */

    // Get Data
    public void getData() {
        // Default filters
        String select = " * ";
        String filter = getWhereClauses()/* + " (colflag is null or colflag = 0) "*/;
        String check = "";

        for (int i = 0; i < syncTablesList.size(); i++) {
            // Now get all uids (only synced!=2) from the table and add it in filter for excluding
            // the give uid to download. This is for the case when user already has
            // the record which will be downloaded in Synced Recs so we exclude those
            // uids from download to avoid discrepancies/conflicts between the downloaded
            // one and the saved one
            // synced!=2 is because we only need uids from the records that were inserted
            // from this device, not all uids i.e. exclude synced==2 records
            String getAllUidQueryStr = "SELECT uid FROM " + syncTablesList.get(i).getTable() + " WHERE synced != 2";
            SupportSQLiteQuery getAllUidQuery = new SimpleSQLiteQuery(getAllUidQueryStr);
            List<String> uIds = appDatabase.generalDao().getAllUIds(getAllUidQuery);
            if (uIds != null && uIds.size() > 0) {
                // Convert uid list to , separated string for filter query
                StringBuilder uIdsBuilder = new StringBuilder();
                for (String s : uIds) {
                    if (uIdsBuilder.length() > 0)
                        uIdsBuilder.append(", ");
                    uIdsBuilder.append("'").append(s).append("'");
                }
                filter += " AND _uid NOT IN (" + uIdsBuilder + ")";
            }

            SyncModel syncModel = new SyncModel(syncTablesList.get(i).getTable(), select, filter, check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(MainApp.gson.toJson(syncModel))),
                    AppConstants.DOWNLOAD_DATA, syncTablesList.get(i).getTable(), i, 0, IS_CALL_ENCRYPTED);
        }
    }

    // Concat available where clauses to download synced recs from server
    private String getWhereClauses() {
        StringBuilder where = new StringBuilder(_EMPTY_);
        // For first iteration only
        int iter = 0;
        // This extra check is used to check if end date is given, if yes then query
        // will be appended as follows otherwise normal query
        boolean isEndDateGiven = whereClauses.containsKey("sysdate_e");
        if (isEndDateGiven)
            where = new StringBuilder(String.format(" CONVERT(DATE, sysdate, 102) BETWEEN CONVERT(DATETIME, '%s', 102) AND CONVERT(DATETIME, '%s', 102) ", whereClauses.get("sysdate_s"), whereClauses.get("sysdate_e")));
//        where = new StringBuilder(String.format(" sysdate BETWEEN '%s' AND '%s' AND ", whereClauses.get("sysdate_s"), whereClauses.get("sysdate_e")));
        for (Map.Entry<String, String> entry : whereClauses.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // To exclude sysdate_s and sysdate_e filters if already set above
            if ((isEndDateGiven && (key.equals("sysdate_s")) || key.equals("sysdate_e"))
                    || key.equals("mainTable") || key.equals("entryType"))
                continue;
            if (key.equals("sysdate_s")) {
                if (iter > 0) where.append(" AND ");
                where.append(String.format(" CONVERT(DATE, sysdate, 102) = CONVERT(DATETIME, '%s', 102) ", whereClauses.get("sysdate_s")));
//                key = "sysdate";
                continue;
            }
            if (iter == 0) {
                iter++;
                where.append(String.format(" %s = '%s' ", key, value));
                continue;
            }
            where.append(" AND ").append(String.format(" %s = '%s' ", key, value));
        }
        return where.toString();
    }

    /**
     * CALLBACK FOR DOWNLOAD DATA
     */

    WebCall.IWebCallback iWebCallback = new WebCall.IWebCallback() {
        @Override
        public void onSuccess(String tag, String jsonResponse, int index, int total, List<String> list) {
            // For enabling other sync buttons if all tasks are done
            boolean isAllSynced = activity.checkIfAllSynced(syncTablesList.size(), SyncAC.SYNCED_RECS, false);

            // Call Success but error occurred while performing request
            if (!AppConstants.isJSONArrayValid(jsonResponse)) {
                // Update sync list view - Error
                SyncModel.WebResponse response = MainApp.gson.fromJson(jsonResponse, SyncModel.WebResponse.class);
                if (!AppConstants.isEmpty(response.getError())) {
                    // Update sync list view - Error
                    String errorMessage = response.getMessage();
                    SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), 0, AppConstants.RESPONSE_ERROR, errorMessage);
                    syncTablesList.set(index, syncModel);
                    syncRVAdapter.notifyItemChanged(index);
                    return;
                }
            }

            // First delete all previously synced recs from respective tables if exists
            if (!isDeleteQueryAddedInTransaction) {
                // Added delete queries in transaction for only one time
                isDeleteQueryAddedInTransaction = true;
                deleteAllSyncedRecs();
            }
            // Add downloaded records into respective tables
            // tag = tableName
            JSONArray jArray = addAllSyncedRecs(tag, jsonResponse, index);
            // For initiating queued transactions when all records are downloaded
            // for saving this data
            if (isAllSynced)
                addTransactionQueue.executeTransactions();

            /*// Update sync list view - This is moved to ITransactionQueueCallback
            // // It will run after every transaction end to update item on sync list on UI
            SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), jArray.length(), AppConstants.RESPONSE_SUCCESS, null);
            syncTablesList.set(index, syncModel);
            syncRVAdapter.notifyItemChanged(index);*/
        }

        @Override
        public void onFailure(String tag, String errorMessage, int index, int total, List<String> list) {
            // Check if error message is a incorrect date error then split it to show formatted message on alert
            String[] dateErr;
            if (!AppConstants.isEmpty(errorMessage) && errorMessage.contains("APP DATE ERROR")) {
                dateErr = errorMessage.split("_");
                errorMessage = dateErr[1];

                // To prevent showing date error dialog again
                if (!isDateError) {
                    isDateError = true;
                    DateUtils.showDeviceDateErrorAlert(activity, dateErr[2], dateErr[3]);
                }
            }

            SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), 0, AppConstants.RESPONSE_ERROR, errorMessage);
            syncTablesList.set(index, syncModel);
            syncRVAdapter.notifyItemChanged(index);

            // For enabling other sync when failure occurs
            boolean isAllSynced = activity.checkIfAllSynced(syncTablesList.size(), SyncAC.SYNCED_RECS, true);
            // For initiating queued transactions when all records are downloaded
            // for saving this data
            if (isAllSynced)
                addTransactionQueue.executeTransactions();
        }
    };

    /**
     * ADD DOWNLOADED RECORDS TO DB
     */

    // Query for adding all synced recs dynamically in respective table
    // index, length are passed just for sync list UI updation
    private JSONArray addAllSyncedRecs(String tableName, String responseBody, int index) {
        JSONArray jArray = new JSONArray();
        try {
            // These three steps are used to convert Server Object to Client Object
            // because of variables naming conventions and extra variables
            // 1- Load the class object type dynamically
            Class<?> clazz = getObjectClazz(tableName);
            assert clazz != null;
            /*// 2- Use Gson to convert JSON to the specified class object
            Object clazzObj = gson.fromJson(responseBody, clazz);*/
            // 2- Init list of class Obj i.e. List<ClazzObj>
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            // We use gsonCustom here because we need to parse list of
            // class object to json by using its fields names instead of
            // its serialized name for saving data in local db

            Gson gsonCustom = new GsonBuilder()
                    .registerTypeAdapter(clazz, new GSONTypedAdapter())
                    .create();
            // 3- Use Gson to convert JSON to the specified class object
            List<?> clazzList = gsonCustom.fromJson(responseBody, listType);
            // 4- Convert list of class Object to JSON.
            String clazzObjJSON = gsonCustom.toJson(clazzList);

            // Get table rows count to prevent Unique constraint failed
            // i.e. id column needs not to be repeated
            String rowsCountQueryStr = "SELECT id FROM " + tableName + " ORDER BY id desc";
            SupportSQLiteQuery rowsCountQuery = new SimpleSQLiteQuery(rowsCountQueryStr);
            int rowsCount = appDatabase.generalDao().rawQuery(rowsCountQuery);
            // For first Iteration only
            int iter = 0;

            jArray = new JSONArray(clazzObjJSON);

//            List<String> columnNames = new ArrayList<>();
            // Start Query
            StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " (");

            // For concatenating columns we get the very first record
            JSONObject _jObject = jArray.getJSONObject(0);
            // Concat columns in insertQuery
            Iterator<String> keysIterator = _jObject.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                if (iter == 0) {
                    iter++;
                    insertQuery.append(key);
                    continue;
                }
                insertQuery.append(", ").append(key);
//                columnNames.add(key);
            }

            // For concatenating values
            insertQuery.append(") VALUES ");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);

                /*// First check if the uid is already exists local db,
                // if yes, then we skip the record otherwise insert it.
                // This extra check is for the case when the user already has
                // the same(downloaded) record in the db to avoid discrepancies/conflict
                // between the downloaded one and the saved one.
                // This long uid getting condition is because of column name
                // might differ i.e. _uid or uid
                String uid = jObject.has("_uid") ? jObject.getString("_uid")
                        : jObject.has("uid") ? jObject.getString("uid") : _EMPTY_;
                String checkRecordExistsQueryStr = "SELECT Count(*) FROM " + tableName + " WHERE uid = " + uid;
                SupportSQLiteQuery checkRecordExistsQuery = new SimpleSQLiteQuery(checkRecordExistsQueryStr);
                int recordExists = appDatabase.generalDao().rawQuery(checkRecordExistsQuery);
                if (recordExists == 0) continue;*/

                iter = 0;
                insertQuery.append("(");
                keysIterator = jObject.keys();
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next();
                    String value = jObject.getString(key);
                    // We are overriding synced value intentionally just to be
                    // used for viewing purpose
                    if (key.equals("synced")) value = "2";
                    if (key.equals("id") || key.equals("_id")) value = String.valueOf(++rowsCount);

                    if (iter == 0) {
                        iter++;
                        insertQuery.append("'").append(value).append("'");
                        continue;
                    }
                    insertQuery.append(", '").append(value).append("'");
                }
                insertQuery.append(")");
                if (i < jArray.length() - 1) {
                    insertQuery.append(", ");
                }
            }

            addTransactionQueue.addTransaction(insertQuery.toString(), index, jArray.length());

            /*db = appDatabase.getOpenHelper().getWritableDatabase();
            // Begin the transaction
            db.beginTransaction();
            db.execSQL(insertQuery.toString());
            // Set the transaction as successful
            db.setTransactionSuccessful();
            *//*ContentValues values = new ContentValues();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String key = columnNames.get(i);
                String value = jObject.getString(key);
                // We are overriding synced value intentionally just to be
                // used for viewing purpose
                if (key.equals("synced")) value = "2";
                if (key.equals("id") || key.equals("_id")) value = String.valueOf(++rowsCount);
                values.put(columnNames.get(i), value);
            }
            db.insert(tableName, SQLiteDatabase.CONFLICT_IGNORE, values);*//*

             *//*SupportSQLiteQuery addQuery = new SimpleSQLiteQuery(insertQuery.toString());
            appDatabase.generalDao().insertQuery(addQuery);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }/* finally {
            // End the transaction
            if (db != null) db.endTransaction();
        }*/
        return jArray;
    }

    /**
     * DELETE DOWNLOADED RECORDS FROM DB
     */

    // Query for deleting all synced recs dynamically from respective table
    private void deleteAllSyncedRecs() {
//        SupportSQLiteDatabase db = appDatabase.getOpenHelper().getWritableDatabase();
//        DBTransactionQueue deleteQueue = new DBTransactionQueue(db, null);
        try {
            // Delete all Synced Recs i.e. synced=2
            for (int i = 0; i < syncTablesList.size(); i++) {
                String deleteQuery = String.format("DELETE FROM %s WHERE synced=2",
                        syncTablesList.get(i).getTable());
               /* SimpleSQLiteQuery deleteQuery = new SimpleSQLiteQuery("DELETE FROM "
                        + syncTablesList.get(i).getTable() + " WHERE synced=2");
                appDatabase.generalDao().rawQuery(deleteQuery);*/
                deleteTransactionQueue.addTransaction(deleteQuery, 0, 0);
                /*// Begin the transaction
                db.beginTransaction();
                db.execSQL(deleteQuery);
                // Set the transaction as successful
                db.setTransactionSuccessful();*/
            }
            deleteTransactionQueue.executeTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            // End the transaction
            if (db != null) db.endTransaction();
        }*/
    }

    /**
     * UPDATE SYNC ITEM ON ADAPTER
     */

    private final DBTransactionQueue.ITransactionQueueCallback callback = new DBTransactionQueue.ITransactionQueueCallback() {
        @Override
        public void onTransactionEnd(int index, int length) {
            SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index),
                    length, AppConstants.RESPONSE_SUCCESS, null);
            syncTablesList.set(index, syncModel);
            syncRVAdapter.notifyItemChanged(index);
            // Show proceed button after all data inserted
            // i.e. Transaction queue is empty
            if (addTransactionQueue.isDBTransactionQueueEmpty())
                activity.showProceedBtn();
        }
    };

    /*This function is used to update list item after DOWNLOAD*/
    // The reason to make this function instance-wise is because if we make it static then there
    // will be a probability of data messing as we call this method after parallel calls
    public SyncModel getUpdatedSyncDownloadItem(Activity activity, SyncModel syncModel, int size, int callStatus, String error) {
        String message;
        if (error == null) {
            // Success
            message = String.format(activity.getString(R.string.sync_download_success), size);
        } else
            // Error
            message = String.format(activity.getString(R.string.sync_error), error);

        syncModel.setMessage(message);
        syncModel.setStatusId(callStatus);
        return syncModel;
    }

    /**
     * UTIL FUNCTIONS
     */

    // Get main form1 table by entryType
    public static String getMainTable(int entryType) {
        for (int i = 0; i < UploadData.UPLOAD_TABLES.size(); i++) {
            SyncModel syncModel = (SyncModel) UploadData.UPLOAD_TABLES.keySet().toArray()[i];
            if (!syncModel.getTable().equals(EntryLog.TABLE_NAME)
                    && !UploadData.SYNCED_RECS_EXCLUDE_TABLES.contains(syncModel.getTable())
                    && syncModel.getModule() == entryType
                    && Boolean.TRUE.equals(UploadData.UPLOAD_TABLES.get(syncModel))) {
                return syncModel.getTable();
            }
        }
        return _EMPTY_;
    }

    // Get linked tables by entryType
    public static List<String> getLinkedTables(int entryType) {
        List<String> tableNames = new ArrayList<>();
        for (int i = 0; i < UploadData.UPLOAD_TABLES.size(); i++) {
            SyncModel syncModel = (SyncModel) UploadData.UPLOAD_TABLES.keySet().toArray()[i];
            if (!syncModel.getTable().equals(EntryLog.TABLE_NAME)
                    && syncModel.getModule() == entryType)
                tableNames.add(syncModel.getTable());
        }
        return tableNames;
    }

    // Get all main form1 table by entryType
    public static List<String> getAllMainTables() {
        List<String> tableNames = new ArrayList<>();
        for (int i = 0; i < UploadData.UPLOAD_TABLES.size(); i++) {
            SyncModel syncModel = (SyncModel) UploadData.UPLOAD_TABLES.keySet().toArray()[i];
            if (!syncModel.getTable().equals(EntryLog.TABLE_NAME)
                    && !UploadData.SYNCED_RECS_EXCLUDE_TABLES.contains(syncModel.getTable())
                    && Boolean.TRUE.equals(UploadData.UPLOAD_TABLES.get(syncModel))
                    && syncModel.getModule() != 0) {
                tableNames.add(syncModel.getTable());
            }
        }
        return tableNames;
    }

    // Get object/model class by tableName
    public static Class<?> getObjectClazz(String tableName) {
        for (int i = 0; i < UploadData.UPLOAD_TABLES.size(); i++) {
            SyncModel syncModel = (SyncModel) UploadData.UPLOAD_TABLES.keySet().toArray()[i];
            if (!syncModel.getTable().equals(EntryLog.TABLE_NAME)
                    && syncModel.getTable().equals(tableName)) {
                return syncModel.getClazz();
            }
        }
        return null;
    }

    // Get SYNCED_RECS_ITEMS fields to display on SyncedRecsList as array
    // from object/model by entryType
    public static String[] getSyncedRecsDisplayFields(String tableName) {
        try {
            for (int i = 0; i < UploadData.UPLOAD_TABLES.size(); i++) {
                SyncModel syncModel = (SyncModel) UploadData.UPLOAD_TABLES.keySet().toArray()[i];
                if (!syncModel.getTable().equals(EntryLog.TABLE_NAME)
                        && syncModel.getTable().equals(tableName)) {
                    Class<?> clazz = DownloadSyncedRecsData.getObjectClazz(tableName);
                    assert clazz != null;
                    Field field = clazz.getDeclaredField("SYNCED_RECS_ITEMS");
                    String syncedRecsDisplayFields = ((String) field.get(null)); // null is because its static variable
                    assert syncedRecsDisplayFields != null;
                    return syncedRecsDisplayFields.split(",");
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
