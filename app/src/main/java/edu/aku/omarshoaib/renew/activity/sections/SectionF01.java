package edu.aku.omarshoaib.renew.activity.sections;

import android.app.Activity;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF01Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.Form1;

public class SectionF01 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF01.this;

    ActivitySectionF01Binding bi;
    private AppDatabase appDatabase;
    private Form1.SF1 sF1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f01);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f1t0), getString(R.string.f1t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF1 = Form1.SF1.getData();
        sF1 = sF1 == null ? new Form1.SF1() : sF1;
        bi.setForm(sF1);
        initUI();
    }

    private void initUI() {
        bi.f103.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form1.SF1.saveData(sF1);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}