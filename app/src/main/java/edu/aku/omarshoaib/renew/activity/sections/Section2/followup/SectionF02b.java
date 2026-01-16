package edu.aku.omarshoaib.renew.activity.sections.Section2.followup;

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
import edu.aku.omarshoaib.renew.model.Form2a;
import edu.aku.omarshoaib.renew.model.Form2b;

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
//        bi.f2b11.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        bi.f2b12.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
//        sF2b.setf2b01(MainApp.vPHQ9.getParticipantId());
//        sF2b.setf2b02(MainApp.vPHQ9.getParticipantName());
//        sF2b.setf2b03(MainApp.vPHQ9.getFatherName());
//        sF2b.setf2b04(MainApp.vPHQ9.getAge());
//        sF2b.setf2b05(MainApp.vPHQ9.getContactNumber());
//        sF2b.setf2b06(MainApp.vPHQ9.getHcfId());
//        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.vPHQ9.getHcfId());
//        bi.f2b06.setText(hcf == null ? "" : hcf.getHfName());
//        sF2b.setf2b07(MainApp.vPHQ9.getVillageAddress());
//        sF2b.setf2b08(MainApp.vPHQ9.getEnteryUser());
//        sF2b.setf2b09(MainApp.vPHQ9.getScreeningDate());
//        sF2b.setf2b10(MainApp.vPHQ9.getPhq9Score());

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2a.saveMainData(MainApp.form2b.getParticipantId());
        MainApp.form2b.setIStatus("1");
        Form2b.SF2b.saveData(sF2b);
//        AppConstants.gotoActivity(activity, Followup2bListAC.class, true);
    }

    public void btnEnd(View view) {
//        AppConstants.checkDoubleCancelPress(activity, Followup2bListAC.class);
    }

    @Override
    public void onBackPressed() {
//        AppConstants.checkDoubleBackPress(activity, Followup2bListAC.class);
    }
}
