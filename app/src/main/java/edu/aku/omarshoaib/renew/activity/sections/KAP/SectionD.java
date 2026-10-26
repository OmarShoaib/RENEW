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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionD1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionD extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionD.this;

    ActivitySectionD1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SD sD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_d1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.d1t1), getString(R.string.d1t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sD = FormKAP.SD.getData();
        sD = sD == null ? new FormKAP.SD() : sD;
        bi.setForm(sD);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SD.saveData(sD);
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
