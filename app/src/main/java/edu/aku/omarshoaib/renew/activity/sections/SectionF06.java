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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF06Binding;
import edu.aku.omarshoaib.renew.model.Form6;

public class SectionF06 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF06.this;

    ActivitySectionF06Binding bi;
    private AppDatabase appDatabase;
    private Form6.SF6 sF6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f06);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f6t1), getString(R.string.f6t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF6 = Form6.SF6.getData();
        sF6 = sF6 == null ? new Form6.SF6() : sF6;
        bi.setForm(sF6);
        initUI();
    }

    private void initUI() {
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form6.SF6.saveData(sF6);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}