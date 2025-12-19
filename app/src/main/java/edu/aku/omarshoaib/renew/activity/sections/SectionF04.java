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
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF04Binding;
import edu.aku.omarshoaib.renew.model.Form4;

public class SectionF04 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF04.this;

    ActivitySectionF04Binding bi;
    private AppDatabase appDatabase;
    private Form4.SF4 sF4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f04);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f4t0), getString(R.string.f4t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF4 = Form4.SF4.getData();
        sF4 = sF4 == null ? new Form4.SF4() : sF4;
        bi.setForm(sF4);
        initUI();
    }

    private void initUI() {
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form4.SF4.saveData(sF4);
        AppConstants.gotoActivity(activity, EndingAC.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}