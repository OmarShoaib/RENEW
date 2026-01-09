package edu.aku.omarshoaib.renew.activity.sections.Section3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.activity.sections.Section2.PHQ9ParticipantsAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF03Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.Form3;

public class SectionF03 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF03.this;

    ActivitySectionF03Binding bi;
    private AppDatabase appDatabase;
    private Form3.SF3 sF3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f03);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f3t0), getString(R.string.f3t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF3 = Form3.SF3.getData();
        sF3 = sF3 == null ? new Form3.SF3() : sF3;
        bi.setForm(sF3);
        initUI();
    }

    private void initUI() {
        bi.f3dd.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f309.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f3dd.addTextChangedListener(new AppTextWatcher(bi.f3dd.getId(), dateTextWatcher));
        sF3.setF301(MainApp.participant.getSF1().getF104());
        sF3.setF303(MainApp.participant.getSF1().getF105());
        sF3.setF304(MainApp.form1.getSF1().getF102());
//        sF3.setF305(MainApp.participant.getVillageName());
        sF3.setF306(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
        bi.f3dd.setMinDate(MainApp.form1.getSF1().getF103());
    }

    AppTextWatcher.IAppTextWatcher dateTextWatcher = (viewId, text) -> {
        if(text.isEmpty()) {
            String today = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
            String maxDate1 = DateUtils.addSubMonths(today, 9);
            bi.f309.setMinDate(today);
            bi.f309.setMaxDate(maxDate1);
            return;
        }
        String maxDate2 = DateUtils.addSubMonths(text, 9);
        bi.f309.setMinDate(text);
        bi.f309.setMaxDate(maxDate2);
    };

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form3.saveMainData(MainApp.form1.getScrId());
        MainApp.form3.setIStatus("1");
        Form3.SF3.saveData(sF3);
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