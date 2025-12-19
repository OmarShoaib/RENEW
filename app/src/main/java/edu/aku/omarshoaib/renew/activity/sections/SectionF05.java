package edu.aku.omarshoaib.renew.activity.sections;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF05Binding;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.model.Form5;

public class SectionF05 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF05.this;

    ActivitySectionF05Binding bi;
    private AppDatabase appDatabase;
    private Form5.SF5 sF5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f05);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f5t0), getString(R.string.f5t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF5 = Form5.SF5.getData();
        sF5 = sF5 == null ? new Form5.SF5() : sF5;
        bi.setForm(sF5);
        initUI();
    }

    private void initUI() {
        bi.f502.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f506.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);

        //TODO: Need 502 Watcher
        bi.f506.setMaxDate(DateUtils.addSubMonths(sF5.getF502(), -6));
        bi.f506.setMinDate(DateUtils.addSubMonths(sF5.getF502(), -59));
       /* bi.f506.setMinDate(F502 Screen Date  - 6 Months)
        bi.f506.setMaxDate(F502 Screen Date  - 59 Months)*/
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form5.SF5.saveData(sF5);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}