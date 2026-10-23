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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionB2Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionB2 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionB2.this;

    ActivitySectionB2Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SB2 sB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_b2);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.b2t1), getString(R.string.b2t2), false);
        appDatabase = AppDatabase.getDBInstance();

        sB2 = FormKAP.SB2.getData();
        sB2 = sB2 == null ? new FormKAP.SB2() : sB2;
        bi.setForm(sB2);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SB2.saveData(sB2);
        AppConstants.gotoActivity(activity, SectionB3.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}
