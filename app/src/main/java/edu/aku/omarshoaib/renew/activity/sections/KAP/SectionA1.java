package edu.aku.omarshoaib.renew.activity.sections.KAP;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.IdentificationAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionA1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionA1 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionA1.this;

    ActivitySectionA1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SA1 sA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_a1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.a1t2), getString(R.string.a1t22), false);
        appDatabase = AppDatabase.getDBInstance();

        sA1 = FormKAP.SA1.getData();
        sA1 = sA1 == null ? new FormKAP.SA1() : sA1;
        bi.setForm(sA1);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SA1.saveData(sA1);
        AppConstants.gotoActivity(activity, SectionA2.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}