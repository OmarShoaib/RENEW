package edu.aku.omarshoaib.renew.activity.sections.KAP;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.IdentificationAC;
import edu.aku.omarshoaib.renew.activity.sections.woman.Section1.ParticipantListAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionA2Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionA2 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionA2.this;

    ActivitySectionA2Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SA2 sA2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_a2);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.a2t1), getString(R.string.a2t2), false);
        appDatabase = AppDatabase.getDBInstance();

        sA2 = FormKAP.SA2.getData();
        sA2 = sA2 == null ? new FormKAP.SA2() : sA2;
        bi.setForm(sA2);
        initUI();
    }

    private void initUI() {}

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SA2.saveData(sA2);
        AppConstants.gotoActivity(activity, SectionA3.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingACKAP.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, EndingACKAP.class);
    }
}