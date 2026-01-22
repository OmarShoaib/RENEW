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
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
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
        AppConstants.initToolbar(activity, getString(R.string.t3a1), getString(R.string.t3a1b), false);
        appDatabase = AppDatabase.getDBInstance();

        sF3a = Form3a.SF3a.getData();
        sF3a = sF3a == null ? new Form3a.SF3a() : sF3a;
        bi.setForm(sF3a);
        initUI();
    }

    private void initUI() {
        bi.f3a1201x.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f03a01.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f3a09.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f03a02.setThemeId(R.style.Theme_AppStructure_TimePickerStyle);
        sF3a.setF3a01(MainApp.vForm3a.getParticipantId());
        sF3a.setF3a02(MainApp.vForm3a.getParticipantName());
        sF3a.setF3a03(MainApp.vForm3a.getFatherName());
        sF3a.setF3a04(MainApp.vForm3a.getAge());
        sF3a.setF3a05(MainApp.vForm3a.getContactNumber());
        sF3a.setF3a06(MainApp.vForm3a.getHcfId());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(MainApp.vForm3a.getHcfId());
        bi.f3a06.setText(hcf == null ? "" : hcf.getHfName());
        sF3a.setF3a07(MainApp.vForm3a.getVillageAddress());
//        sF3a.setF3a08(MainApp.vForm3a.getEnteryUser());
        sF3a.setF3a08(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
//        sF3a.setF3a09(MainApp.vForm3a.getScreeningDate());
        bi.f03a01.setMinDate(MainApp.vForm3a.getScreeningDate());
        bi.f3a09.setMinDate(MainApp.vForm3a.getScreeningDate());
        bi.f3a09.addTextChangedListener(new AppTextWatcher(bi.f3a09.getId(), iAppTextWatcher));
    }

    AppTextWatcher.IAppTextWatcher iAppTextWatcher = (viewId, text) -> {
        sF3a.setF3a1201x("");
        if(text.isEmpty()) {
            bi.f3a1201x.setMinDate(DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT));
        } else {
            bi.f3a1201x.setMinDate(text);
            String maxDate = DateUtils.addSubMonths(text, 9);
            bi.f3a1201x.setMaxDate(maxDate);
        }
    };

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form3a.saveMainData(MainApp.form3a.getPregnantWomanId());
        MainApp.form3a.setIStatus("1");
        Form3a.SF3a.saveData(sF3a);
        AppConstants.gotoActivity(activity, Followup3aListAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, Followup3aListAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, Followup3aListAC.class);
    }

}
