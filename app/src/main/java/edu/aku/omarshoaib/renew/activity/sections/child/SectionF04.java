package edu.aku.omarshoaib.renew.activity.sections.child;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

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
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form4;
import edu.aku.omarshoaib.renew.model.HCF;

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
        sF4.setF401(MainApp.form4.getUsername());
        HCF hcf = appDatabase.hcfDao().getHcfbyCode(sF4.getF403().trim());
        bi.f403.setText(hcf == null ? "" : hcf.getHfName());
        bi.f409.setOnCheckedChangeListener((group, checkedId) -> group.post(this::viewF411));
        bi.f410.addTextChangedListener(new AppTextWatcher(bi.f410.getId(), (viewId, text) -> viewF411()));
    }

    private void viewF411() {
        if (proceedToF05()) bi.fldGrpCVf411.setVisibility(View.VISIBLE);
        else {
            bi.fldGrpCVf411.setVisibility(View.GONE);
            sF4.setF411(_EMPTY_);
        }
    }

    private boolean proceedToF05() {
        boolean lowMuac = !sF4.getF410().isEmpty() && Float.parseFloat(sF4.getF410()) < 12.5f;
        boolean edema = sF4.getF409().equals("1");
        return lowMuac || edema;
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form4.saveMainData(MainApp.form4.getScrId());
        MainApp.form4.setIStatus("1");
        Form4.SF4.saveData(sF4);
        AppConstants.gotoActivity(activity, proceedToF05() ?
                SectionF05.class : MainActivity.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}