package edu.aku.omarshoaib.renew.activity.sections.KAP;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.Arrays;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.IdentificationAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionA1Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class SectionA1 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionA1.this;

    ActivitySectionA1Binding bi;
    private AppDatabase appDatabase;
    private FormKAP.SA1 sA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_a1);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.a1t2), getString(R.string.a1t22), false);
        appDatabase = AppDatabase.getDBInstance();

        sA1 = FormKAP.SA1.getData();
        sA1 = sA1 == null ? new FormKAP.SA1() : sA1;
        bi.setForm(sA1);
        initUI();
    }

    private void initUI() {
        if(!sA1.getA115().isEmpty()) {
            bi.a115.setEnabled(false);
            bi.a115.setTextColor(ContextCompat.getColor(this, R.color.red_color));
        } else {
            String minDate = DateUtils
                    .addSubDays(DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT),
                            -25);
            bi.a115.setMinDate(minDate);
        }
        if(MainApp.formKAP.getSA2() != null) {
            AppConstants.disableViews(activity, Arrays.asList(bi.a102, bi.a104, bi.a105, bi.a106));
        }
    }

    private boolean formValidation() {
        if(!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        /*if(!sA1.getA103m().isEmpty() && !sA1.getA103d().isEmpty() &&
                Integer.parseInt(sA1.getA103m()) == 0 && Integer.parseInt(sA1.getA103d()) == 0  ) {
            return Validator.emptyCustomTextBox(activity, bi.a103m, "Both cannot be zero");
        }*/

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        FormKAP.SA1.saveData(sA1);

        AppConstants.gotoActivity(activity, sA1.getA106().equals("1") ? SectionA2.class :
                EndingACKAP.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, IdentificationAC.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, IdentificationAC.class);
    }
}