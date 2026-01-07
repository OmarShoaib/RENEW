package edu.aku.omarshoaib.renew.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wajahatkarim3.roomexplorer.RoomExplorer;

import java.io.File;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.sections.Section1.Identification01;
import edu.aku.omarshoaib.renew.activity.sections.Section2.Identification02;
import edu.aku.omarshoaib.renew.activity.sections.Section3.SectionF03;
import edu.aku.omarshoaib.renew.activity.sections.SectionF04;
import edu.aku.omarshoaib.renew.activity.sections.SectionF05;
import edu.aku.omarshoaib.renew.activity.sections.SectionF06;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityMainBinding;
import edu.aku.omarshoaib.renew.global.AlertPopup;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.BackupDB;
import edu.aku.omarshoaib.renew.global.ImportDB;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SendDB;
import edu.aku.omarshoaib.renew.global.SummaryUtils;
import edu.aku.omarshoaib.renew.synced_recs.SyncedRecsFilter;
import edu.aku.omarshoaib.renew.webcall.UploadData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = MainActivity.this;

    ActivityMainBinding bi;

    private AppDatabase appDatabase;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        bi.setCallback(this);

        // Init toolbar
        AppConstants.initToolbar(activity, String.format(getString(R.string.welcome_user),
                MainApp.user != null ? MainApp.user.getFullName() : ""), null, false);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    // For setting initial UI
    private void initUI() {
        UploadData.initUploadList();
        // For Admin
//        bi.adminView.setVisibility(AppConstants.IS_ADMIN ? View.VISIBLE : View.GONE);

        /*// Init GPS location request
        if (AppConstants.IS_GPS_ON && !AppConstants.isServiceRunning(activity, GPSLocationService.class.getSimpleName())) {
            // Set the GPSLocation instance in the service
            GPSLocationService.setGpsLocation(new GPSLocation(activity));
            startService(new Intent(this, GPSLocationService.class));  // Start the service
        }*/
    }

    public void openForm(View view) {
        int viewId = view.getId();
        MainApp.entryType = 0;
        MainApp.isSynced = false;   // For locally saved synced form1
        MainApp.isSyncedRecs = false;   // For Synced Recs

        if (viewId == R.id.option1) {
            MainApp.entryType = 1;
            AppConstants.gotoActivity(activity, Identification01.class, true);
        } else if (viewId == R.id.option2) {
            MainApp.entryType = 2;
            AppConstants.gotoActivity(activity, Identification02.class, true);
        } else if (viewId == R.id.option3) {
            MainApp.entryType = 3;
            AppConstants.gotoActivity(activity, SectionF03.class, true);
        } else if (viewId == R.id.option4) {
            MainApp.entryType = 4;
            AppConstants.gotoActivity(activity, SectionF04.class, true);
        } else if (viewId == R.id.option5) {
            MainApp.entryType = 5;
            AppConstants.gotoActivity(activity, SectionF05.class, true);
        } else if (viewId == R.id.option6) {
            MainApp.entryType = 6;
            AppConstants.gotoActivity(activity, SectionF06.class, true);
        } else if (viewId == R.id.summaryLayout) {
            // Show summary bottomsheet
            SummaryUtils.showSummary(activity);
        } else if (viewId == R.id.syncedRecsLayout) {
            new SyncedRecsFilter(activity).showViewRecsFilterPopup();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // For Admin
        if (AppConstants.IS_ADMIN)
            menu.findItem(R.id.dbBrowser).setVisible(true);
        menu.findItem(R.id.sendDB).setVisible(MainApp.user != null && MainApp.user.getIsSendDB() == 1);
        menu.findItem(R.id.importDB).setVisible(MainApp.user != null && MainApp.user.getUsername().equals(AppConstants.DEV_USERNAME));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.onSync) {
            // IS_LOGIN = To differentiate before and after login dwnload
            // For after login sync data download
            AppConstants.IS_LOGIN = 2;
            AppConstants.gotoActivity(activity, SyncAC.class, false);
        } else if (menuItemId == R.id.changePassword) {
            AppConstants.gotoActivity(activity, ChangePassAC.class, false);
        } else if (menuItemId == R.id.dbBrowser) {
            RoomExplorer.show(this, AppDatabase.class, AppConstants.DATABASE_NAME);
        } else if (menuItemId == R.id.backupDB) {
            File outputFile = BackupDB.backup(activity);
            if (outputFile != null)
                AlertPopup.alert(activity, getString(R.string.backup_successful),
                        String.format(getString(R.string.backup_successful_desc), activity.getPackageName(),
                                outputFile.getName()), AppConstants.TYPE_SUCCESS);
        } else if (menuItemId == R.id.sendDB) {
            SendDB.email(activity);
        } else if (menuItemId == R.id.importDB) {
            ImportDB.startFilePicker(activity);
        } else if (menuItemId == R.id.logout) {
            AppConstants.gotoActivity(activity, LoginAC.class, true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImportDB.REQUEST_CODE_IMPORT_DB && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                ImportDB.importDatabaseFromUri(this, uri);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // For Clusters
        // To show/hide intro layout
//        List<Cluster> clusterList = appDatabase.clusterDao().getAllData();
//        if (clusterList != null && !clusterList.isEmpty())
            bi.lockedLayout.setVisibility(View.GONE);

        // For Villages
        // To show/hide intro layout
        /*List<Villages> villagesList = appDatabase.villagesDao().getAllData();
        if (villagesList != null && !villagesList.isEmpty())
            bi.lockedLayout.setVisibility(View.GONE);*/
    }
}