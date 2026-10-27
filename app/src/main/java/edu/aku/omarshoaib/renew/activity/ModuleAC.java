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
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.sections.KAP.SectionA1;
import edu.aku.omarshoaib.renew.activity.sections.child.Identification04;
import edu.aku.omarshoaib.renew.activity.sections.child.SectionF05AListAC;
import edu.aku.omarshoaib.renew.activity.sections.child.SectionF06ListAC;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section1.Identification01;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section2.Identification02;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section2.followup.FollowUp2bListAC;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section2.followup.Followup2aListAC;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section3.Identification03;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section3.followup.Followup3aListAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityModuleBinding;
import edu.aku.omarshoaib.renew.global.AlertPopup;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.BackupDB;
import edu.aku.omarshoaib.renew.global.ImportDB;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SendDB;
import edu.aku.omarshoaib.renew.global.SummaryUtils;
import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.synced_recs.SyncedRecsFilter;

public class ModuleAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = ModuleAC.this;

    ActivityModuleBinding bi;

    private AppDatabase appDatabase;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_module);
        bi.setCallback(this);

        // Init toolbar
        AppConstants.initToolbar(activity, String.format(getString(R.string.welcome_user),
                MainApp.user != null ? MainApp.user.getFullName() : ""), null, false);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    // For setting initial UI
    private void initUI() {}

    public void openForm(View view) {
        int viewId = view.getId();
        MainApp.entryType = 0;
        MainApp.isSynced = false;
        MainApp.isSyncedRecs = false;

        if (viewId == R.id.option1) MainApp.entryType = MainApp.MODULE_WOMAN;
        else if (viewId == R.id.option2) MainApp.entryType = MainApp.MODULE_CHILD;
        else if (viewId == R.id.option3) MainApp.entryType = MainApp.MODULE_KAP;

        AppConstants.gotoActivity(activity, MainApp.entryType == MainApp.MODULE_KAP ?
                IdentificationAC.class : MainActivity.class, false);
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
        List<HCF> hcfList = appDatabase.hcfDao().getAllData();
        if (hcfList != null && !hcfList.isEmpty())
            bi.lockedLayout.setVisibility(View.GONE);

        // For Villages
        // To show/hide intro layout
        /*List<Villages> villagesList = appDatabase.villagesDao().getAllData();
        if (villagesList != null && !villagesList.isEmpty())
            bi.lockedLayout.setVisibility(View.GONE);*/
    }
}