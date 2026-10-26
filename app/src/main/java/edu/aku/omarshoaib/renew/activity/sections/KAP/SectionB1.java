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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionB1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionB1 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionB1.this;

    ActivitySectionB1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SB1 sB1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_b1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.b1t2), getString(R.string.b1t22), false);
        appDatabase = AppDatabase.getDBInstance();

        sB1 = FormKAP.SB1.getData();
        sB1 = sB1 == null ? new FormKAP.SB1() : sB1;
        bi.setForm(sB1);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SB1.saveData(sB1);
        AppConstants.gotoActivity(activity, SectionB2.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}