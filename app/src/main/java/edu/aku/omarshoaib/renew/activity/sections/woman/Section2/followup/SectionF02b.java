package edu.aku.omarshoaib.renew.activity.sections.woman.Section2.followup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF02bBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2b;
import edu.aku.omarshoaib.renew.model.HCF;

public class SectionF02b extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF02b.this;

    ActivitySectionF02bBinding bi;
    private AppDatabase appDatabase;
    private Form2b.SF2b sF2b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f02b);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.t02b), getString(R.string.f3t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF2b = Form2b.SF2b.getData();
        sF2b = sF2b == null ? new Form2b.SF2b() : sF2b;
        bi.setForm(sF2b);
        initUI();
    }

    private void initUI() {
        bi.f02b10.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f02b11.setThemeId(R.style.Theme_AppStructure_TimePickerStyle);
        sF2b.setF02b01(MainApp.vForm2b.getParticipantId());
        sF2b.setF02b02(MainApp.vForm2b.getParticipantName());
        sF2b.setF02b03(MainApp.vForm2b.getFatherName());
        sF2b.setF02b04(MainApp.vForm2b.getAge());
        sF2b.setF02b05(MainApp.vForm2b.getContactNumber());
        sF2b.setF02b06(MainApp.vForm2b.getHcfId());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.vForm2b.getHcfId());
        bi.f02b06.setText(hcf == null ? "" : hcf.getHfName());
        sF2b.setF02b07(MainApp.vForm2b.getVillageAddress());
        sF2b.setF02b08(MainApp.vForm2b.getRefrenceDate());
        bi.f02b10.setMinDate(MainApp.vForm2b.getRefrenceDate());
        sF2b.setF02b09(MainApp.vForm2b.getPsycologistPhq9Score());
        sF2b.setF02b12(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
//        sF2b.setF02b10(MainApp.vForm2b.getPhq9Score());
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2b.saveMainData(MainApp.form2b.getParticipantId());
        MainApp.form2b.setIStatus("1");
        Form2b.SF2b.saveData(sF2b);
        AppConstants.gotoActivity(activity, FollowUp2bListAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, FollowUp2bListAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, FollowUp2bListAC.class);
    }
}