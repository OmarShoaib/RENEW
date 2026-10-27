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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionB3Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionB3 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionB3.this;

    ActivitySectionB3Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SB3 sB3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_b3);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.b3t1), getString(R.string.b3t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sB3 = FormKAP.SB3.getData();
        sB3 = sB3 == null ? new FormKAP.SB3() : sB3;
        bi.setForm(sB3);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SB3.saveData(sB3);
        AppConstants.gotoActivity(activity, SectionB4.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingACKAP.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, EndingACKAP.class);
    }
}
