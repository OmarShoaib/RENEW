package edu.aku.omarshoaib.renew.activity.sections;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF03Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.Form3;

public class SectionF03 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF03.this;

    ActivitySectionF03Binding bi;
    private AppDatabase appDatabase;
    private Form3.SF3 sF3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f03);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f3t0), getString(R.string.f3t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF3 = Form3.SF3.getData();
        sF3 = sF3 == null ? new Form3.SF3() : sF3;
        bi.setForm(sF3);
        initUI();
    }

    private void initUI() {
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form3.SF3.saveData(sF3);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}