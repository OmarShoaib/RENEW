package edu.aku.omarshoaib.renew.activity.sections.Section3.followup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.sections.Section2.followup.Followup2aListAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF03aBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2a;
import edu.aku.omarshoaib.renew.model.Form3a;
import edu.aku.omarshoaib.renew.model.HCF;

public class SectionF03a extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF03a.this;

    ActivitySectionF03aBinding bi;
    private AppDatabase appDatabase;
    private Form3a.SF3a sF3a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f03a);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f3t0), getString(R.string.f3t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF3a = Form3a.SF3a.getData();
        sF3a = sF3a == null ? new Form3a.SF3a() : sF3a;
        bi.setForm(sF3a);
        initUI();
    }

    private void initUI() {
//        bi.f2a11.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        bi.f2a12.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        bi.f2c01.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        bi.f2c0401x.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        sF3a.setF2a01(MainApp.vPHQ9.getParticipantId());
//        sF3a.setF2a02(MainApp.vPHQ9.getParticipantName());
//        sF3a.setF2a03(MainApp.vPHQ9.getFatherName());
//        sF3a.setF2a04(MainApp.vPHQ9.getAge());
//        sF3a.setF2a05(MainApp.vPHQ9.getContactNumber());
//        sF3a.setF2a06(MainApp.vPHQ9.getHcfId());
//        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.vPHQ9.getHcfId());
//        bi.f2a06.setText(hcf == null ? "" : hcf.getHfName());
//        sF3a.setF2a07(MainApp.vPHQ9.getVillageAddress());
//        sF3a.setF2a08(MainApp.vPHQ9.getEnteryUser());
//        sF3a.setF2a09(MainApp.vPHQ9.getScreeningDate());
//        sF3a.setF2a10(MainApp.vPHQ9.getPhq9Score());

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form3a.saveMainData(MainApp.form3a.getParticipantId());
        MainApp.form3a.setIStatus("1");
        Form3a.SF3a.saveData(sF3a);
//        AppConstants.gotoActivity(activity, Followup3aListAC.class, true);
    }

    public void btnEnd(View view) {
//        AppConstants.checkDoubleCancelPress(activity, Followup2aListAC.class);
    }

    @Override
    public void onBackPressed() {
//        AppConstants.checkDoubleBackPress(activity, Followup2aListAC.class);
    }

}
