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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionA3Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionA3 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionA3.this;

    ActivitySectionA3Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SA3 sA3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_a3);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.a3t1), getString(R.string.a3t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sA3 = FormKAP.SA3.getData();
        sA3 = sA3 == null ? new FormKAP.SA3() : sA3;
        bi.setForm(sA3);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SA3.saveData(sA3);
        AppConstants.gotoActivity(activity, IdentificationAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}