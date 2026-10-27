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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionE1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionE1 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionE1.this;

    ActivitySectionE1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SE1 sE1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_e1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.e1t1), getString(R.string.e1t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sE1 = FormKAP.SE1.getData();
        sE1 = sE1 == null ? new FormKAP.SE1() : sE1;
        bi.setForm(sE1);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SE1.saveData(sE1);
        AppConstants.gotoActivity(activity, SectionE2.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingACKAP.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, EndingACKAP.class);
    }
}
