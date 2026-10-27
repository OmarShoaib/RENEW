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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionC1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionC.this;

    ActivitySectionC1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SC sC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_c1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.c1t1), getString(R.string.c1t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sC = FormKAP.SC.getData();
        sC = sC == null ? new FormKAP.SC() : sC;
        bi.setForm(sC);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SC.saveData(sC);
        AppConstants.gotoActivity(activity, SectionD.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingACKAP.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, EndingACKAP.class);
    }
}
