package edu.aku.omarshoaib.renew.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityModuleBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.HCF;

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

        AppConstants.gotoActivity(activity, MainActivity.class, false);
    }
}