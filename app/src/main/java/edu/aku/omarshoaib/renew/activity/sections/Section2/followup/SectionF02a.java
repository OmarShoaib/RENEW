package edu.aku.omarshoaib.renew.activity.sections.Section2.followup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.sections.Section3.PregnantParticipantsAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF02aBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2a;

public class SectionF02a extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF02a.this;

    ActivitySectionF02aBinding bi;
    private AppDatabase appDatabase;
    private Form2a.SF2a sF2a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f02a);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f3t0), getString(R.string.f3t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF2a = Form2a.SF2a.getData();
        sF2a = sF2a == null ? new Form2a.SF2a() : sF2a;
        bi.setForm(sF2a);
        initUI();
    }

    private void initUI() {

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2a.saveMainData(MainApp.form2a.getParticipantId());
//        MainApp.form3.setIStatus("1");
//        MainApp.form3.setPregnantWomanId(sF3.getF315());
        Form2a.SF2a.saveData(sF2a);
        AppConstants.gotoActivity(activity, PregnantParticipantsAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, PregnantParticipantsAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, PregnantParticipantsAC.class);
    }
}
