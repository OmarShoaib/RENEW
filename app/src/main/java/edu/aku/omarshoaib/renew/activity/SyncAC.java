package edu.aku.omarshoaib.renew.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.adapter.SyncAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySyncBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.ConnectionDetector;
import edu.aku.omarshoaib.renew.global.ImageUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.synced_recs.DownloadSyncedRecsData;
import edu.aku.omarshoaib.renew.synced_recs.SyncedRecsListAC;
import edu.aku.omarshoaib.renew.webcall.DownloadData;
import edu.aku.omarshoaib.renew.webcall.UploadData;
import edu.aku.omarshoaib.renew.webcall.UploadPhotos;

public class SyncAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SyncAC.this;

    private ActivitySyncBinding bi;

    private ConnectionDetector connectionDetector;
    private AppDatabase appDatabase;

    // Table names list to download/upload
    private List<SyncModel> syncTablesList;
    private SyncAdapter syncAdapter;

    // For disabling other sync buttons if one task is performing
    private int syncRecordsCount;

    // Sync Types
    public static int DOWNLOAD_DATA = 111;
    public static int UPLOAD_DATA = 112;
    public static int UPLOAD_PHOTOS = 113;
    public static int SYNCED_RECS = 114;

    // For Synced Recs
    // For Synced Recs, we need all the tables to download successfully,
    // if any of the table fails to download then we do not proceed
    private boolean isFailure;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_sync);
        bi.setCallback(this);

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.data_sync_activity), null, true);

        connectionDetector = new ConnectionDetector(activity);
        appDatabase = AppDatabase.getDBInstance();

        // Init sync list
        syncTablesList = new ArrayList<>();
        initSyncList();
    }

    // Init sync list adapter
    private void initSyncList() {
        syncAdapter = new SyncAdapter(activity, syncTablesList, null);
        bi.syncRV.setAdapter(syncAdapter);

        // For Synced Recs
        // If network is not available then show proceed btn for checking
        // (on the next activity) if the data already exists
        if (AppConstants.IS_LOGIN == 3) {
            // Hide UploadData and UploadPhotos button - For UX
            bi.uploadBtn.setVisibility(View.GONE);
            bi.uploadPhotosBtn.setVisibility(View.GONE);
            // If network is not available hide the download data btn
            // and show proceed btn
            if (!connectionDetector.hasInternetConnection()) {
                bi.proceedBtn.setVisibility(View.VISIBLE);
                bi.downloadBtn.setVisibility(View.GONE);
                bi.emptyTV.setText(getString(R.string.no_network_synced_recs_filter));
                bi.emptyTV.setVisibility(View.VISIBLE);
            }
        }
    }

    // Download Data
    @SuppressLint("NotifyDataSetChanged")
    public void downloadData(View view) {
        DisableOtherViews(DOWNLOAD_DATA);
        syncTablesList.clear();
        if (AppConstants.IS_LOGIN == 1) { // Before Login Download
            syncTablesList.addAll(SyncModel.initSyncList(DownloadData.DT_BEFORE_LOGIN));
            syncAdapter.notifyDataSetChanged();
            DownloadData downloadData = new DownloadData(this, syncAdapter, syncTablesList);
            downloadData.getData(AppConstants.IS_LOGIN);
        } else if (AppConstants.IS_LOGIN == 2) { // After Login Download
            syncTablesList.addAll(SyncModel.initSyncList(DownloadData.DT_AFTER_LOGIN));
            syncAdapter.notifyDataSetChanged();
            DownloadData downloadData = new DownloadData(this, syncAdapter, syncTablesList);
            downloadData.getData(AppConstants.IS_LOGIN);
        } else { // Synced Recs Download
            // Get MAIN table by entryType
            // For Synced Recs
            // main table i.e. form1 table that will be used to generate
            // display list and getting linked tables
//            String mainTable = SyncedRecsData.getMainTable(entryType);
            // Tables that are linked with main table.
            // Getting from UploadData HashMap
            List<String> linkedTables = DownloadSyncedRecsData.getLinkedTables(MainApp.entryType);
            // Get whereClauses
            HashMap<String, String> whereClauses = (HashMap<String, String>) getIntent().getSerializableExtra("where");
            isFailure = false;
            syncTablesList.addAll(SyncModel.initSyncList(linkedTables));
            syncAdapter.notifyDataSetChanged();
            DownloadSyncedRecsData downloadData = new DownloadSyncedRecsData(this, syncAdapter, syncTablesList, whereClauses);
            downloadData.getData();
        }
    }

    // Upload Data
    @SuppressLint("NotifyDataSetChanged")
    public void uploadData(View view) {
        DisableOtherViews(UPLOAD_DATA);
        syncTablesList.clear();
        syncTablesList.addAll(UploadData.UPLOAD_TABLES.keySet());
        syncAdapter.notifyDataSetChanged();
        UploadData uploadData = new UploadData(this, syncAdapter, syncTablesList);
        uploadData.postData();
    }

    // Upload Photos
    @SuppressLint("NotifyDataSetChanged")
    public void uploadPhotos(View view) {
        syncTablesList.clear();
        List<String> imagesNamesList = ImageUtils.getAllImagesNames(activity);
        if (imagesNamesList == null || imagesNamesList.size() == 0) {
            AppConstants.showSimpleSnackBar(activity, activity.getString(R.string.no_photos_to_upload),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            syncAdapter.notifyDataSetChanged();
            return;
        }
        DisableOtherViews(UPLOAD_PHOTOS);
        syncTablesList.addAll(SyncModel.initSyncList(imagesNamesList));
        syncAdapter.notifyDataSetChanged();
        UploadPhotos uploadPhotos = new UploadPhotos(this, syncAdapter, syncTablesList);
        uploadPhotos.postPhotos();
    }

    // Proceed to Synced Recs Activity List
    @SuppressLint("NotifyDataSetChanged")
    public void proceedSyncedRecs(View view) {
        AppConstants.gotoActivity(activity, SyncedRecsListAC.class, true);
    }

    // For disabling other sync buttons if one task is performing
    private void DisableOtherViews(int syncType) {
        int disabledColor = ContextCompat.getColor(activity, R.color.disabled_text_color);
        if (syncType == DOWNLOAD_DATA || syncType == SYNCED_RECS) {
            // Download Data - Disable Upload and Upload Photos buttons
            bi.uploadBtn.setEnabled(false);
            bi.uploadBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            bi.uploadPhotosBtn.setEnabled(false);
            bi.uploadPhotosBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
        } else if (syncType == UPLOAD_DATA) {
            // Upload Data - Disable Download and Upload Photos buttons
            bi.downloadBtn.setEnabled(false);
            bi.downloadBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            bi.uploadPhotosBtn.setEnabled(false);
            bi.uploadPhotosBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
        } else {
            // Upload Photos - Disable Download and Upload buttons
            bi.downloadBtn.setEnabled(false);
            bi.downloadBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            bi.uploadBtn.setEnabled(false);
            bi.uploadBtn.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
        }
    }

    // For enabling other sync buttons if all tasks are done
    private void EnableOtherViews(int syncType) {
        // Reset sync records count
        syncRecordsCount = 0;
        int enabledColor = ContextCompat.getColor(activity, R.color.primary_color);
        int enabledPhotosColor = ContextCompat.getColor(activity, R.color.secondary_color_dark);
        if (syncType == DOWNLOAD_DATA || syncType == SYNCED_RECS) {
            // Download Data - Disable Upload and Upload Photos buttons
            bi.uploadBtn.setEnabled(true);
            bi.uploadBtn.setBackgroundTintList(ColorStateList.valueOf(enabledPhotosColor));
            bi.uploadPhotosBtn.setEnabled(true);
            bi.uploadPhotosBtn.setBackgroundTintList(ColorStateList.valueOf(enabledPhotosColor));
        } else if (syncType == UPLOAD_DATA) {
            // Upload Data - Disable Download and Upload Photos buttons
            bi.downloadBtn.setEnabled(true);
            bi.downloadBtn.setBackgroundTintList(ColorStateList.valueOf(enabledColor));
            bi.uploadPhotosBtn.setEnabled(true);
            bi.uploadPhotosBtn.setBackgroundTintList(ColorStateList.valueOf(enabledPhotosColor));
        } else {
            // Upload Photos - Disable Download and Upload buttons
            bi.downloadBtn.setEnabled(true);
            bi.downloadBtn.setBackgroundTintList(ColorStateList.valueOf(enabledColor));
            bi.uploadBtn.setEnabled(true);
            bi.uploadBtn.setBackgroundTintList(ColorStateList.valueOf(enabledPhotosColor));
        }
    }

    // For enabling other sync buttons if all tasks are done
    public void checkIfAllSynced(int total, int syncType) {
        if (syncRecordsCount < total - 1) {
            syncRecordsCount++;
            return;
        }
        EnableOtherViews(syncType);
    }

    // This override function is only used for Synced Recs
    // For enabling other sync buttons if all tasks are done
    public boolean checkIfAllSynced(int total, int syncType, boolean isFailure) {
        if (!isFailure) this.isFailure = false;
        if (syncRecordsCount < total - 1) {
            syncRecordsCount++;
            return false;
        }
        EnableOtherViews(syncType);
        return true;
    }

    // For Synced Recs - Show proceed button after all successful calls
    public void showProceedBtn() {
        if (!isFailure)
            bi.proceedBtn.setVisibility(View.VISIBLE);
    }

    // For toolbar back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}