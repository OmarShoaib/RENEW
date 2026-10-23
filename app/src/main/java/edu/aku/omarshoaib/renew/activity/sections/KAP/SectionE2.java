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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionE2Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionE2 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionE2.this;

    ActivitySectionE2Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SE2 sE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_e2);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.e2t1), getString(R.string.e2t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sE = FormKAP.SE2.getData();
        sE = sE == null ? new FormKAP.SE2() : sE;
        bi.setForm(sE);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SE2.saveData(sE);
        AppConstants.gotoActivity(activity, SectionE1.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}
