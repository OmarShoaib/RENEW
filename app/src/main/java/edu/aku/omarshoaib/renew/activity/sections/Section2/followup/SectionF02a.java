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
import edu.aku.omarshoaib.renew.model.HCF;

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
        AppConstants.initToolbar(activity, getString(R.string.f2at0), getString(R.string.t2at1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF2a = Form2a.SF2a.getData();
        sF2a = sF2a == null ? new Form2a.SF2a() : sF2a;
        bi.setForm(sF2a);
        initUI();
    }

    private void initUI() {
        bi.f2a11.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f2a12.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f2c01.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f2c0401x.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        sF2a.setF2a01(MainApp.vPHQ9.getParticipantId());
        sF2a.setF2a02(MainApp.vPHQ9.getParticipantName());
        sF2a.setF2a03(MainApp.vPHQ9.getFatherName());
        sF2a.setF2a04(MainApp.vPHQ9.getAge());
        sF2a.setF2a05(MainApp.vPHQ9.getContactNumber());
        sF2a.setF2a06(MainApp.vPHQ9.getHcfId());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.vPHQ9.getHcfId());
        bi.f2a06.setText(hcf == null ? "" : hcf.getHfName());
        sF2a.setF2a07(MainApp.vPHQ9.getVillageAddress());
        sF2a.setF2a08(MainApp.vPHQ9.getEnteryUser());
        sF2a.setF2a09(MainApp.vPHQ9.getScreeningDate());
        bi.f2a12.setMinDate(MainApp.vPHQ9.getScreeningDate());
        sF2a.setF2a10(MainApp.vPHQ9.getPhq9Score());
        sF2a.setF2a11(MainApp.vPHQ9.getRefrenceDate());

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2a.saveMainData(MainApp.form2a.getParticipantId());
        MainApp.form2a.setIStatus("1");
        Form2a.SF2a.saveData(sF2a);
        AppConstants.gotoActivity(activity, Followup2aListAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, Followup2aListAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, Followup2aListAC.class);
    }
}
