package edu.aku.omarshoaib.renew.webcall;

import static edu.aku.omarshoaib.renew.global.AppConstants.IS_CALL_ENCRYPTED;

import android.app.Activity;
import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.SyncAC;
import edu.aku.omarshoaib.renew.adapter.SyncAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.EntryLog;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.webcall.web_client.CryptoUtil;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebCall;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class UploadData {
    private final SyncAC activity;
    private final WebAPI webAPI;
    private final WebCall webCall;
    private final SyncAdapter syncAdapter;
    private final List<SyncModel> syncTablesList;
    private final AppDatabase appDatabase;
    private final Gson gson;

    /**
     * TABLES TO POST
     */

    // The boolean is used to identify that this table is a main form1 or sub-section
    // in order to generalize the upload data logic.
    /*Only the MAIN FORM/TABLE will be true*/
    // We are using class object type in SyncModel for parsing json to respective object
    // in Synced Recs
    //
    // We used/changed SyncModel instead of Table string as Key because of showing section
    // names in the upload item.
    public static LinkedHashMap<SyncModel, Boolean> UPLOAD_TABLES;

    public static void initUploadList() {
        UPLOAD_TABLES = new LinkedHashMap<SyncModel, Boolean>() {{
            put(new SyncModel(EntryLog.TABLE_NAME, AppConstants._EMPTY_), false);
            put(new SyncModel(Form1.class, MainApp.MODULE_FORM, Form1.TABLE_NAME, Form1.SECTION_NAME), true);
        }};
    }

    // Tables to exclude to display on Synced Recs filter module dropdown
    public static List<String> SYNCED_RECS_EXCLUDE_TABLES = new ArrayList<String>() {{
        // Put tables here for exclusion
    }};

    /**
     * INIT
     */

    public UploadData(SyncAC activity, SyncAdapter syncAdapter, List<SyncModel> syncTablesList) {
        this.activity = activity;
        webAPI = WebClient.getInstance(activity).getWebAPI();
        webCall = new WebCall(activity, iWebCallback);
        this.syncAdapter = syncAdapter;
        this.syncTablesList = syncTablesList;
        appDatabase = AppDatabase.getDBInstance();
        gson = new Gson();
    }

    /**
     * PREPARE DATA TO POST
     */

    /*Prepare UPLOAD data to post*/
    public static String prepareUploadData(String tableName, String uploadData) {
        try {
            // Table
            JSONObject table = new JSONObject();
            table.put("table", tableName);
            JSONArray value = new JSONArray();
            value.put(table);
            value.put(new JSONArray(uploadData));
            Log.e("POST_JSON", value.toString());
            /*String encData = CryptoUtil.encrypt(value.toString());
            String postData = AppConstants.insertStringAtIndex(encData,
                    AppConstants.YEK_DATA_KEY, AppConstants.YEK_DATA_INDEX);*/
            return CryptoUtil.encrypt(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POST DATA TO SERVER
     */

    // Post Data
    public void postData() {
        // index is used to update sync data list adapter item
        int index = -1;

        String tableName = ((SyncModel) UPLOAD_TABLES.keySet().toArray()[0]).getTable();
        List<EntryLog> entryLogs = appDatabase.entryLogDao().getAllUnSyncedData();
        if (entryLogs != null && entryLogs.size() > 0) {
            String postData = prepareUploadData(tableName, gson.toJson(entryLogs));
            webCall.call(webAPI.uploadEncData(postData), AppConstants.UPLOAD_DATA, tableName, ++index, entryLogs.size(), IS_CALL_ENCRYPTED);
        } else {
            iWebCallback.onFailure(tableName, activity.getString(R.string.no_new_records_to_upload), ++index, 0, null);
        }

        // Ths hashmap is used to store all the form1 completed uIds with respect to table name
        HashMap<String, List<String>> uIdsHM = new HashMap<>();
        for (int i = 0; i < UPLOAD_TABLES.size(); i++) {
            // This looping is used to get those un-synced forms
            // whose iStatus != null i.e. Interview Completed and also get
            // other models of the related form1.
            // This is used to reduce data invalidation on server
            SyncModel syncModel = (SyncModel) UPLOAD_TABLES.keySet().toArray()[i];
            boolean isMainTable = Boolean.TRUE.equals(UPLOAD_TABLES.get(syncModel));
            if (isMainTable) {
                SimpleSQLiteQuery syncedQuery = new SimpleSQLiteQuery("SELECT uid FROM " + syncModel.getTable() +
                        " WHERE ((synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null)" +
                        " AND (iStatus != '' OR iStatus != null)) OR isError IS 1");
                List<String> uIdsList = new ArrayList<>();
                uIdsHM.put(syncModel.getTable(), uIdsList);
                uIdsList = appDatabase.generalDao().getUnsyncedDataUIds(syncedQuery);
                if (uIdsList != null && !uIdsList.isEmpty()) {
                    Objects.requireNonNull(uIdsHM.get(syncModel.getTable())).addAll(uIdsList);
                }
            }
        }

        // Now access only those forms whose uIds are in iFormCompletedUIDs list
        String postData;
        List<String> iFormCompletedUIds;

        iFormCompletedUIds = uIdsHM.get(Form1.TABLE_NAME);
        tableName = ((SyncModel) UPLOAD_TABLES.keySet().toArray()[1]).getTable();
        List<Form1> list1 = appDatabase.form1Dao().getAllUnSyncedDataByUIds(iFormCompletedUIds);
        if (list1 != null && !list1.isEmpty()) {
            postData = prepareUploadData(tableName, gson.toJson(list1));
//            postData = prepareUploadData(tableName, testData());
            webCall.call(webAPI.uploadEncData(postData), AppConstants.UPLOAD_DATA, tableName, ++index, list1.size(), iFormCompletedUIds, IS_CALL_ENCRYPTED);
        } else
            iWebCallback.onFailure(tableName, activity.getString(R.string.no_new_records_to_upload), ++index, 0, null);

        /* ADD MORE TABLE HERE TO UPLOAD IF NECESSARY */

    }

    /*private String testData() {
        return "[{\"appversion\":\"1.13D.1353\",\"clusterCode\":\"700002\",\"deviceid\":\"d3de9be9ac201399\",\"dist_id\":\"700\",\"hhid\":\"0001-001\",\"istatus\":\"1\",\"istatus96x\":\"\",\"_id\":1,\"projectName\":\"CASI_MIDLINE_SURVEY\",\"sH1\":{\"h101\":\"Afghanistan\",\"h102\":\"700002\",\"h103\":\"\",\"h104\":\"\",\"h105\":\"\",\"h106\":\"\",\"h107\":\"0001-001\",\"h108\":\"Testing\",\"h109\":\"2023-10-18\",\"h109a\":\"17:35\",\"h110\":\"mdjdkdmd\",\"h111\":\"\",\"h112\":\"25\",\"h112a\":\"2\",\"h113a\":\"1\",\"h113b\":\"1\",\"h113c\":\"79944494449\"},\"sH3\":{\"h301\":\"9\",\"h30196x\":\"\",\"h302\":\"1\",\"h30296x\":\"\",\"h303\":\"10\",\"h30396x\":\"\",\"h303a\":\"1\",\"h304\":\"2\",\"h30496x\":\"\",\"h305\":\"15.5\",\"h306\":\"528282\",\"h306a\":\"2\",\"h306a01x\":\"\",\"h307\":\"1\",\"h308\":\"1\",\"h30803\":\"\",\"h30804\":\"\",\"h30805\":\"\",\"h30806\":\"\",\"h30896x\":\"\",\"h309\":\"1\",\"h310\":\"1\",\"h31096x\":\"\",\"h311\":\"12\",\"h312\":\"1\",\"h31296x\":\"\",\"h313\":\"1\",\"h314\":\"12\",\"h315\":\"1\",\"h31596x\":\"\",\"h316a\":\"2\",\"h316b\":\"2\",\"h316c\":\"2\",\"h316d\":\"2\",\"h316e\":\"2\",\"h316f\":\"2\",\"h316g\":\"2\",\"h316h\":\"2\",\"h316i\":\"2\",\"h316j\":\"2\",\"h316k\":\"2\",\"h316l\":\"2\",\"h316m\":\"2\",\"h316n\":\"2\",\"h316o\":\"2\",\"h316p\":\"2\",\"h316q\":\"2\",\"h316r\":\"\",\"h316s\":\"2\",\"h317a\":\"1\",\"h317b\":\"1\",\"h317c\":\"1\",\"h317d\":\"1\",\"h317e\":\"1\",\"h317f\":\"1\",\"h317g\":\"1\",\"h317h\":\"1\",\"h317i\":\"1\",\"h317j\":\"1\",\"h317k\":\"1\",\"h318\":\"1\",\"h320\":\"1\",\"h32096x\":\"\",\"h321\":\"1\",\"h32196x\":\"\",\"h321a\":\"1\",\"h322\":\"11\",\"h32296x\":\"\",\"h323\":\"11\",\"h32396x\":\"\",\"h324\":\"11\",\"h32496x\":\"\",\"h325\":\"12\",\"h326\":\"1\",\"h327\":\"\",\"h32701\":\"\",\"h32701x\":\"\",\"h32702\":\"\",\"h32702x\":\"\",\"h32703\":\"\",\"h32703x\":\"\",\"h32796\":\"\",\"h32796x\":\"\",\"h32798\":\"98\",\"h328\":\"1\",\"h329\":\"\",\"h32901\":\"\",\"h32901x\":\"\",\"h32902\":\"\",\"h32902x\":\"\",\"h32903\":\"\",\"h32903x\":\"\",\"h32904\":\"\",\"h32904x\":\"\",\"h32905\":\"\",\"h32905x\":\"\",\"h32906\":\"\",\"h32906x\":\"\",\"h32907\":\"\",\"h32907x\":\"\",\"h32998\":\"98\"},\"sH4\":{\"h401\":\"1\",\"h40196x\":\"\",\"h402\":\"1\",\"h403\":\"\",\"h40301\":\"1\",\"h40302\":\"\",\"h40303\":\"\",\"h40304\":\"\",\"h40305\":\"\",\"h404\":\"\",\"h405\":\"\",\"h40501\":\"\",\"h40502\":\"\",\"h40503\":\"\",\"h40504\":\"\",\"h40505\":\"\",\"h406\":\"\",\"h40601\":\"1\",\"h40602\":\"\",\"h40603\":\"\",\"h40604\":\"\",\"h40605\":\"\",\"h40606\":\"\",\"h40607\":\"7\",\"h40608\":\"\",\"h40609\":\"\",\"h40696\":\"\",\"h40696x\":\"\"},\"sH5\":{\"h501\":\"1\",\"h50196x\":\"\",\"h502\":\"1\",\"h503\":\"1\",\"h504\":\"1\",\"h50496x\":\"\"},\"sH6\":{\"h601\":\"2\",\"h601a\":\"\",\"h602\":\"2\",\"h602a\":\"\",\"h603\":\"2\",\"h603a\":\"\",\"h604\":\"2\",\"h604a\":\"\",\"h605\":\"2\",\"h605a\":\"\",\"h606\":\"2\",\"h606a\":\"\",\"h607\":\"2\",\"h607a\":\"\",\"h608\":\"2\",\"h608a\":\"\",\"h609\":\"2\",\"h609a\":\"\"},\"sH8\":{\"h801a\":\"97\",\"h801b\":\"1\",\"h801c\":\"2\",\"h801d\":\"1\",\"h801e\":\"97\",\"h801f\":\"97\",\"h801f08\":\"\",\"h801g\":\"97\",\"h801h\":\"97\",\"h801i\":\"3\",\"h801j\":\"6\",\"h801k\":\"1\",\"h801l\":\"3\",\"h8a0196\":\"2\",\"h8a0196x\":\"\",\"h8a01a\":\"2\",\"h8a01b\":\"3\",\"h8a01c\":\"4\",\"h8a01d\":\"4\",\"h8a01e\":\"5\",\"h8a01f\":\"\",\"h8a0296\":\"2\",\"h8a0296x\":\"\",\"h8a02a\":\"4\",\"h8a02b\":\"5\",\"h8a02c\":\"1\",\"h8a02d\":\"3\",\"h8a02e\":\"5\",\"h8a02f\":\"4\",\"h8a02g\":\"1\",\"h8a02h\":\"4\",\"h8a02i\":\"2\",\"h8a02j\":\"5\",\"h8a02k\":\"3\",\"h8a02l\":\"4\",\"h8a02m\":\"\"},\"sH9\":{\"h901\":\"2\",\"h901a\":\"\",\"h901b\":\"\",\"h902\":\"2\",\"h902a\":\"\",\"h902b\":\"\",\"h903\":\"2\",\"h903a\":\"\",\"h903b\":\"\",\"h904\":\"2\",\"h904a\":\"\",\"h904b\":\"\",\"h905\":\"2\",\"h905a\":\"\",\"h905b\":\"\",\"h906\":\"2\",\"h906a\":\"\",\"h906b\":\"\",\"h907\":\"2\",\"h907a\":\"\",\"h907b\":\"\",\"h908\":\"2\",\"h908a\":\"\",\"h908b\":\"\",\"h909\":\"2\",\"h909a\":\"\",\"h909b\":\"\",\"h910\":\"2\",\"h910a\":\"\",\"h910b\":\"\",\"h911\":\"2\",\"h911a\":\"\",\"h911b\":\"\",\"h912\":\"2\",\"h912a\":\"\",\"h912b\":\"\",\"h913\":\"2\",\"h913a\":\"\",\"h913b\":\"\",\"h914\":\"2\",\"h914a\":\"\",\"h914b\":\"\",\"h915\":\"2\",\"h915a\":\"\",\"h915b\":\"\",\"h916\":\"2\",\"h916a\":\"\",\"h916b\":\"\",\"h916x\":\"\"},\"sync_date\":\"\",\"synced\":\"\",\"sysdate\":\"2023-10-18 17:34:37\",\"_uid\":\"d3de9b1697632536199\",\"username\":\"test0002\",\"village_name\":\"\",\"wra_id\":\"\"},{\"appversion\":\"1.13D.1360\",\"clusterCode\":\"700001\",\"deviceid\":\"d3de9be9ac201399\",\"dist_id\":\"700\",\"hhid\":\"0001-001\",\"istatus\":\"5\",\"istatus96x\":\"\",\"_id\":2,\"projectName\":\"CASI_MIDLINE_SURVEY\",\"sH1\":{\"h101\":\"Pakistan\",\"h102\":\"700001\",\"h103\":\"Test Country \",\"h104\":\" Test District \",\"h105\":\" Test Location \",\"h106\":\" Test Village\",\"h107\":\"0001-001\",\"h108\":\"Testing\",\"h109\":\"2023-10-19\",\"h109a\":\"10:48\",\"h110\":\"mdmdmd\",\"h111\":\"\",\"h112\":\"25\",\"h112a\":\"1\",\"h113a\":\"1\",\"h113b\":\"1\",\"h113c\":\"54646494949\"},\"sync_date\":\"\",\"synced\":\"\",\"sysdate\":\"2023-10-19 10:47:44\",\"_uid\":\"d3de9b1697694501116\",\"username\":\"test0002\",\"village_name\":\" Test Village\",\"wra_id\":\"\"}]";
    }*/

    /**
     * CALLBACK FOR UPLOAD DATA
     */

    WebCall.IWebCallback iWebCallback = new WebCall.IWebCallback() {
        @Override
        public void onSuccess(String tag, String jsonResponse, int index, int total, List<String> list) {

            // For enabling other sync buttons if all tasks are done
            activity.checkIfAllSynced(syncTablesList.size(), SyncAC.UPLOAD_DATA);

            // Update sync list view
            List<SyncModel.WebResponse> responses = Arrays.asList(gson.fromJson(jsonResponse, SyncModel.WebResponse[].class));
            SyncModel syncModel = getUpdatedSyncUploadItem(activity, responses,
                    syncTablesList.get(index), AppConstants.RESPONSE_SUCCESS, null, total);
            syncTablesList.set(index, syncModel);
            syncAdapter.notifyItemChanged(index);

            // Update sync status to Success
            updateSyncStatus(tag, responses, list, true);
        }

        // list = list of uIds that has been selected for post
        @Override
        public void onFailure(String tag, String errorMessage, int index, int total, List<String> list) {
            // Update sync list view
            SyncModel syncModel = getUpdatedSyncUploadItem(activity, null,
                    syncTablesList.get(index), AppConstants.RESPONSE_ERROR, errorMessage, total);
            syncTablesList.set(index, syncModel);
            syncAdapter.notifyItemChanged(index);

            // For enabling other sync when failure occurs
            activity.checkIfAllSynced(syncTablesList.size(), SyncAC.UPLOAD_DATA);

            if (total == 0) return;
            // Update sync status to Error
            updateSyncStatus(tag, null, list, false);
        }
    };

    /**
     * UPDATE SYNC STATUS ON DB
     */

    // Update sync status to db i.e.
    // - In case of success, update synced and synced status value and isError to false
    // - In case of error, update isError to true
    // list = list of uIds of selected forms to post - This parameter is used to update isError
    // to true on selected records that are failed to upload
    private void updateSyncStatus(String tag, List<SyncModel.WebResponse> responses, List<String> list, boolean isSuccess) {
        if (isSuccess) {
            // Update sync success status to db
            if (tag.equals(EntryLog.TABLE_NAME)) {
                appDatabase.entryLogDao().updateSyncSuccess(responses);
            } else if (tag.equals(Form1.TABLE_NAME)) {
                appDatabase.form1Dao().updateSyncSuccess(responses);
            }
        } else {
            // Update sync error status to db
            if (tag.equals(EntryLog.TABLE_NAME)) {
                appDatabase.entryLogDao().updateSyncError(appDatabase.entryLogDao().getAllUnSyncedData());
            } else if (tag.equals(Form1.TABLE_NAME)) {
                appDatabase.form1Dao().updateSyncError(appDatabase.form1Dao().getAllUnSyncedDataByUIds(list));
            }
        }
    }

    /**
     * UPDATE SYNC ITEM ON ADAPTER
     */

    /*This function is used to update list item after UPLOAD*/
    // The reason to make this function instance-wise is because if we make it static then there
    // will be a probability of data messing as we call this method after parallel calls
    public SyncModel getUpdatedSyncUploadItem(Activity activity, List<SyncModel.WebResponse> response, SyncModel syncModel, int callStatus, String error, int total) {
        StringBuilder statusMessage = new StringBuilder();
        String message;
        int synced = 0, duplicates = 0, errors = 0;
        if (response != null) {
            // Success
            syncModel.setStatusId(callStatus);
            for (int i = 0; i < response.size(); i++) {
                if (response.get(i).getError() == 0) {
                    if (response.get(i).getStatus() == 1) {
                        // Synced
                        synced++;
                    } else if (response.get(i).getStatus() == 2) {
                        // Duplicate
                        duplicates++;
                        statusMessage.append(String.format(Locale.ENGLISH, "{%d-%s} | ", response.get(i).getId(), response.get(i).getMessage()));
                    }
                } else {
                    syncModel.setStatusId(AppConstants.RESPONSE_ERROR);
                    errors++;
                    statusMessage.append(String.format(Locale.ENGLISH, "{%d-%s} | ", response.get(i).getId(), response.get(i).getMessage()));
                }
            }
            message = String.format(activity.getString(R.string.sync_upload_success), total, synced, duplicates,
                    (duplicates == 0 && errors == 0) ? activity.getString(R.string.record_saved) : statusMessage);
        } else {
            // Error
            errors++;
            message = String.format(activity.getString(R.string.status), error);
            syncModel.setStatusId(AppConstants.RESPONSE_ERROR);
        }
        syncModel.setSyncedPerfect(duplicates == 0 && errors == 0);
        syncModel.setMessage(message);
        return syncModel;
    }

}