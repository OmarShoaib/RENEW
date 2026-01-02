package edu.aku.omarshoaib.renew.activity.sections.Section2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF02Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.model.Form2;

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
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form2.SF2.saveData(sF2);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}