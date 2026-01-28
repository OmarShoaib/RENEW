package edu.aku.omarshoaib.renew.activity.sections.woman.Section2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF02Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.HCF;

public class SectionF02 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF02.this;

    ActivitySectionF02Binding bi;
    private AppDatabase appDatabase;
    private Form2.SF2 sF2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f02);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f2t0), getString(R.string.f2t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF2 = Form2.SF2.getData();
        sF2 = sF2 == null ? new Form2.SF2() : sF2;
        bi.setForm(sF2);
        initUI();
    }

    private void initUI() {
        bi.f207.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        sF2.setF201(MainApp.participant.getSF1().getF104());
        sF2.setF203(MainApp.participant.getSF1().getF105());
        sF2.setF204(MainApp.form1.getSF1().getF102());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.form1.getSF1().getF102().trim());
        bi.f204.setText(hcf == null ? "" : hcf.getHfName());
        sF2.setF206(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
        bi.f207.setMinDate(MainApp.form1.getSF1().getF103());
        bi.scrid.setText(MainApp.form1.getScrId());
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2.saveMainData(MainApp.form1.getScrId());
        MainApp.form2.setParticipantId(sF2.getF212());
        Form2.SF2.saveData(sF2);
        AppConstants.gotoActivity(activity, PHQ9ParticipantsAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, PHQ9ParticipantsAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, PHQ9ParticipantsAC.class);
    }
}