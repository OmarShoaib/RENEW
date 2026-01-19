package edu.aku.omarshoaib.renew.webcall;

import static edu.aku.omarshoaib.renew.global.AppConstants.IS_CALL_ENCRYPTED;
import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.b3nedikt.restring.Restring;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.SyncAC;
import edu.aku.omarshoaib.renew.adapter.SyncAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SharedPrefs;
import edu.aku.omarshoaib.renew.model.AppInfo;
import edu.aku.omarshoaib.renew.model.Cluster;
import edu.aku.omarshoaib.renew.model.DPortal;
import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.model.User;
import edu.aku.omarshoaib.renew.model.VForm2b;
import edu.aku.omarshoaib.renew.model.VForm3a;
import edu.aku.omarshoaib.renew.model.VPHQ9;
import edu.aku.omarshoaib.renew.webcall.web_client.CryptoUtil;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebCall;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class DownloadData {
    private final SyncAC activity;
    private final WebAPI webAPI;
    private final WebCall webCall;
    private final SyncAdapter syncAdapter;
    private final List<SyncModel> syncTablesList;
    private final AppDatabase appDatabase;
    private final Gson gson;
    // This boolean is used for date checking device date validation
    // and show popup only once
    private boolean isDateError;

    /**
     * TABLES TO DOWNLOAD
     */

    // Downloadable tables before login
    public static List<String> DT_BEFORE_LOGIN = new ArrayList<String>() {{
        // AppInfo - For version checking
        // App info is not a table. The info is stored in shared preferences
        // Just added here to show it on sync list as item
        add(AppInfo.NAME);
        add(User.TABLE_NAME);
    }};

    // Downloadable tables after login
    public static List<String> DT_AFTER_LOGIN = new ArrayList<String>() {{
        add("STRINGS");
        add("RANGES");
        /* APP CODE STARTS FROM HERE */
        add(Cluster.TABLE_NAME);
        add(HCF.TABLE_NAME);
        add(VPHQ9.TABLE_NAME);
        add(VForm2b.TABLE_NAME);
        add(VForm3a.TABLE_NAME);
    }};

    /**
     * INIT
     */

    public DownloadData(SyncAC activity, SyncAdapter syncAdapter, List<SyncModel> syncTablesList) {
        this.activity = activity;
        webAPI = WebClient.getInstance(activity).getWebAPI();
        webCall = new WebCall(activity, iWebCallback);
        this.syncAdapter = syncAdapter;
        this.syncTablesList = syncTablesList;
        appDatabase = AppDatabase.getDBInstance();
        gson = new Gson();
    }

    /**
     * DOWNLOAD DATA FROM SERVER
     */

    // Get Data
    // isLogin = This is used to differentiate between before and after login data download
    public void getData(int isLogin) {
        // Default filters
        String select = " * ";
        String filter = " (colflag is null or colflag = 0) ";
        String check = "";

        isDateError = false;
        int index = -1;

        if (isLogin == 1) {
            // Before Login tables download
            // AppInfo - For version checking
            // App info is not a table. The info is stored in shared preferences
            SyncModel appInfo = new SyncModel(DT_BEFORE_LOGIN.get(0), select, filter, check);
            appInfo.setFolder(WebAPI.VERSION_OUTPUT_JSON_FILE_PATH);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(appInfo))), AppConstants.DOWNLOAD_DATA, DT_BEFORE_LOGIN.get(0), ++index, 0, IS_CALL_ENCRYPTED);

            SyncModel s1 = new SyncModel(DT_BEFORE_LOGIN.get(1), select, "", check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s1))), AppConstants.DOWNLOAD_DATA, DT_BEFORE_LOGIN.get(1), ++index, 0, IS_CALL_ENCRYPTED);
        } else {
            // After Login tables download

            // For dynamic strings download
            Map<String, String> crfIdMap = new HashMap<>();
            crfIdMap.put("id_crf", AppConstants.CRF_ID);
            crfIdMap.put("prefix", AppConstants.CRF_PREFIX);
            String crfIdJson = CryptoUtil.encrypt(gson.toJson(crfIdMap), true);

            webCall.call(webAPI.downloadStringsAndRanges(WebClient.DICTIONARY_PORTAL_URL
                    + "getStrings.php", crfIdJson), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(0), ++index, 0, true);

            // For dynamic ranges download
            webCall.call(webAPI.downloadStringsAndRanges(WebClient.DICTIONARY_PORTAL_URL
                    + "getRanges.php", crfIdJson), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(1), ++index, 0, true);

            /* APP CODE STARTS FROM HERE */

            SyncModel s1 = new SyncModel(DT_AFTER_LOGIN.get(2), select, filter + /*"AND dist_id = " + MainApp.user.getDistId()*/ "", check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s1))), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(2), ++index, 0, IS_CALL_ENCRYPTED);

            SyncModel s2 = new SyncModel(DT_AFTER_LOGIN.get(3), select, "" + " dist_id = " + MainApp.user.getDistId(), check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s2))), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(3), ++index, 0, IS_CALL_ENCRYPTED);

            SyncModel s3 = new SyncModel(DT_AFTER_LOGIN.get(4), select, "" /*+ " dist_id = " + MainApp.user.getDistId()*/, check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s3))), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(4), ++index, 0, IS_CALL_ENCRYPTED);

            SyncModel s4 = new SyncModel(DT_AFTER_LOGIN.get(5), select, "" /*+ " dist_id = " + MainApp.user.getDistId()*/, check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s4))), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(5), ++index, 0, IS_CALL_ENCRYPTED);

            SyncModel s5 = new SyncModel(DT_AFTER_LOGIN.get(6), select, "" /*+ " dist_id = " + MainApp.user.getDistId()*/, check);
            webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(gson.toJson(s5))), AppConstants.DOWNLOAD_DATA, DT_AFTER_LOGIN.get(6), ++index, 0, IS_CALL_ENCRYPTED);
        }
    }

    /**
     * CALLBACK FOR DOWNLOAD DATA
     */

    WebCall.IWebCallback iWebCallback = new WebCall.IWebCallback() {
        @Override
        public void onSuccess(String tag, String jsonResponse, int index, int total, List<String> list) {

            // For enabling other sync buttons if all tasks are done
            activity.checkIfAllSynced(syncTablesList.size(), SyncAC.DOWNLOAD_DATA);

            // Call Success but error occurred while performing request
            if (!AppConstants.isJSONArrayValid(jsonResponse)) {
                // Update sync list view - Error
                if (!AppConstants.isJSONObjectValid(jsonResponse)) {
                    SyncModel.WebResponse response = gson.fromJson(jsonResponse, SyncModel.WebResponse.class);
                    if (!AppConstants.isEmpty(response.getError())) {
                        // Update sync list view - Error
                        String errorMessage = response.getMessage();
                        SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), 0, AppConstants.RESPONSE_ERROR, errorMessage);
                        syncTablesList.set(index, syncModel);
                        syncAdapter.notifyItemChanged(index);
                        return;
                    }
                }
            }

            // For Success
            if (tag.equals(AppInfo.NAME)) {
                // For App Version
                // App version call download the content of output-metadata.json file of apk placed on server
                try {
                    JSONObject appInfoJson = new JSONObject(jsonResponse);
                    JSONObject appVersionJson = appInfoJson.getJSONArray("elements").getJSONObject(0);
                    AppInfo appInfo = new AppInfo();
                    appInfo.setVersionName(appVersionJson.getString("versionName"));
                    appInfo.setVersionCode(appVersionJson.getInt("versionCode"));
                    appInfo.setOutputFile(appVersionJson.getString("outputFile"));

                    // Setting app version
                    AppInfo.setUpdatedAppInfo(appInfo);

                    // Update sync list view
                    String message;
                    int status;
                    if (AppInfo.isAppUpdated()) {
                        message = activity.getString(R.string.app_up_to_date);
                        status = AppConstants.RESPONSE_SUCCESS;
                    } else {
                        message = String.format(Locale.ENGLISH, "New Version Available %s.%d=%s", appInfo.getVersionName(), appInfo.getVersionCode(), appInfo.getOutputFile());
                        status = AppConstants.RESPONSE_ERROR;
                    }

                    SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), 1, status, null, false, message);
                    syncTablesList.set(index, syncModel);
                    syncAdapter.notifyItemChanged(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (tag.equals(User.TABLE_NAME)) {
                User[] users = gson.fromJson(jsonResponse, User[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), users.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.userDao().reinsert(users);
            } else if (tag.equals("STRINGS")) {
                Log.d("DynamicStrings", "Fetched strings: " + jsonResponse);
                List<DPortal.LabelData> labelDataList;
                try {
                    // Define the list type
                    Type listType = new TypeToken<List<DPortal.LabelData>>() {
                    }.getType();

                    // Parse JSON into List<LabelData>
                    labelDataList = gson.fromJson(jsonResponse, listType);
                    if (labelDataList != null && !labelDataList.isEmpty()) {
                        for (DPortal.LabelData data : labelDataList) {
                            String langCode = data.getLangCode();
                            Map<String, String> labels = data.getLabels();

                            if (langCode != null && labels != null && !labels.isEmpty()) {
                                // Register this language in ReString
                                Restring.putStrings(new Locale(langCode), labels);
                            } else
                                Log.w("DynamicStrings", "Skipping invalid or empty language entry: " + langCode);
                        }
                    } else Log.e("DynamicStrings", "labelDataList is null or empty.");
                    SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), labelDataList != null ?
                            labelDataList.size() : 0, AppConstants.RESPONSE_SUCCESS, null);
                    syncTablesList.set(index, syncModel);
                    syncAdapter.notifyItemChanged(index);
                } catch (JsonSyntaxException e) {
                    Log.e("DynamicStrings", "Failed to parse JSON: " + e.getMessage(), e);
                }
            } else if (tag.equals("RANGES")) {
                Log.d("DynamicRanges", "Fetched ranges: " + jsonResponse);
                List<DPortal.RangeData> rangeDataList;
                try {
                    // Define the list type
                    Type listType = new TypeToken<List<DPortal.RangeData>>() {
                    }.getType();

                    // Parse JSON into List<LabelData>
                    rangeDataList = gson.fromJson(jsonResponse, listType);
                    if (rangeDataList != null && !rangeDataList.isEmpty()) {
                        // For Logging
                        /*for (DPortalData.RangeData data : rangeDataList) {
                            String label = data.getLabel();
                            String minValue = data.getMinValue();
                            String maxValue = data.getMaxValue();
                            Log.d("DynamicRanges", "Field: " + label + ", min: " + minValue + ", max: " + maxValue);
                        }*/
                        // Converts List → Map
                        DPortal.getInstance().setRanges(rangeDataList);
                        // Save in sharedPrefs for later use i.e. in BaseActivity
                        SharedPrefs.write(SharedPrefs.FIELD_RANGES, gson.toJson(DPortal.getInstance().getRangesMap()));
                    } else Log.e("DynamicRanges", "rangeDataList is null or empty.");

                    SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), rangeDataList != null ?
                            rangeDataList.size() : 0, AppConstants.RESPONSE_SUCCESS, null);
                    syncTablesList.set(index, syncModel);
                    syncAdapter.notifyItemChanged(index);
                } catch (JsonSyntaxException e) {
                    Log.e("DynamicRanges", "Failed to parse JSON: " + e.getMessage(), e);
                }
            }
            /* APP CODE STARTS FROM HERE */
            else if (tag.equals(Cluster.TABLE_NAME)) {
                Cluster[] clusters = gson.fromJson(jsonResponse, Cluster[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), clusters.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.clusterDao().reinsert(clusters);
            }
            else if (tag.equals(HCF.TABLE_NAME)) {
                HCF[] hcfs = gson.fromJson(jsonResponse, HCF[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), hcfs.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.hcfDao().reinsert(hcfs);
            } else if (tag.equals(VPHQ9.TABLE_NAME)) {
                VPHQ9[] vhq9Views = gson.fromJson(jsonResponse, VPHQ9[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), vhq9Views.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.vphq9Dao().reinsert(vhq9Views);
            } else if (tag.equals(VForm2b.TABLE_NAME)) {
                VForm2b[] vhq9Views = gson.fromJson(jsonResponse, VForm2b[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), vhq9Views.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.vForm2bDao().reinsert(vhq9Views);
            } else if (tag.equals(VForm3a.TABLE_NAME)) {
                VForm3a[] vForm3as = gson.fromJson(jsonResponse, VForm3a[].class);
                // Update sync list view
                SyncModel syncModel = getUpdatedSyncDownloadItem(activity, syncTablesList.get(index), vForm3as.length, AppConstants.RESPONSE_SUCCESS, null);
                syncTablesList.set(index, syncModel);
                syncAdapter.notifyItemChanged(index);

                // Clear and Add data to db
                appDatabase.vForm3aDao().reinsert(vForm3as);
            }
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
            syncAdapter.notifyItemChanged(index);

            // For enabling other sync when failure occurs
            activity.checkIfAllSynced(syncTablesList.size(), SyncAC.DOWNLOAD_DATA);
        }
    };

    /**
     * UPDATE SYNC ITEM ON ADAPTER
     */

    /*This function is used to update list item after DOWNLOAD*/
    // The reason to make this function instance-wise is because if we make it static then there
    // will be a probability of data messing as we call this method after parallel calls
    public SyncModel getUpdatedSyncDownloadItem(Activity activity, SyncModel syncModel, int size, int callStatus, String error) {
        return getUpdatedSyncDownloadItem(activity, syncModel, size, callStatus, error, false, _EMPTY_);
    }

    public SyncModel getUpdatedSyncDownloadItem(Activity activity, SyncModel syncModel, int size, int callStatus, String error, boolean isSequential, String message) {
        String _message;
        if (error == null) {
            if (!isSequential) {
                // Success
                if (AppConstants.isEmpty(message)) {
                    _message = String.format(activity.getString(R.string.sync_download_success), size);
                    syncModel.setStatusId(callStatus);
                } else {
                    _message = message;
                    syncModel.setStatusId(callStatus);
                }
            } else {
                _message = String.format(activity.getString(R.string.sync_download_success_seq), message);
                syncModel.setStatusId(callStatus);
            }
        } else {
            // Error
            _message = String.format(activity.getString(R.string.sync_error), error);
            syncModel.setStatusId(callStatus);
        }

        syncModel.setMessage(_message);
        return syncModel;
    }

}