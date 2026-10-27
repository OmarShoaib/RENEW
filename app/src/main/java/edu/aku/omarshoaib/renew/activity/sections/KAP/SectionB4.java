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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionB4Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionB4 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionB4.this;

    ActivitySectionB4Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SB4 sB4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_b4);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.b4t1), getString(R.string.b4t12), false);
        appDatabase = AppDatabase.getDBInstance();

        sB4 = FormKAP.SB4.getData();
        sB4 = sB4 == null ? new FormKAP.SB4() : sB4;
        bi.setForm(sB4);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SB4.saveData(sB4);
        AppConstants.gotoActivity(activity, SectionC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingACKAP.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, EndingACKAP.class);
    }
}
