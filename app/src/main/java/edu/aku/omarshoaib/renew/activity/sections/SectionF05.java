package edu.aku.omarshoaib.renew.activity.sections;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.List;

import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF05Binding;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
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
        sF5.setF501(MainApp.form5.getUsername());
        bi.f502.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f506.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f502.addTextChangedListener(new AppTextWatcher(bi.f502.getId(), dateTextWatcher));
        bi.f506.addTextChangedListener(new AppTextWatcher(bi.f506.getId(), dateTextWatcher));
        bi.f512.addTextChangedListener(new AppTextWatcher(bi.f512.getId(),
                (viewId, text) -> eligible()));
        bi.fo515.setOnCheckedChangeListener((rG, i) -> rG.post(this::eligible));
    }

    AppTextWatcher.IAppTextWatcher dateTextWatcher = (viewId, text) -> {
        if (viewId == bi.f502.getId()) {
            sF5.setF506(_EMPTY_);
            if (text.isEmpty()) {
                String today = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
                String maxDate1 = DateUtils.addSubMonths(today, -6);
                String minDate1 = DateUtils.addSubMonths(today, -59);
                bi.f506.setMinDate(minDate1);
                bi.f506.setMaxDate(maxDate1);
                return;
            }
            String maxDate2 = DateUtils.addSubMonths(text, -6);
            String minDate2 = DateUtils.addSubMonths(text, -59);
            bi.f506.setMinDate(minDate2);
            bi.f506.setMaxDate(maxDate2);
        } else if (viewId == bi.f506.getId()) {
            if (text.isEmpty()) {
                sF5.setF507dd("");
                sF5.setF507mm("");
                return;
            }
            String[] dob = text.split("-");
            List<String> age = DateUtils.calculateAge(sF5.getF502(), dob[0], dob[1], dob[2]);
            sF5.setF507dd(age.get(2));
            sF5.setF507mm(String.
                    valueOf(DateUtils.
                            getAgeInMonths(age.get(0), age.get(1))
                    )
            );
        }
    };

    private boolean formValidation() {
        if(!Validator.emptyCheckingContainer(activity, bi.GrpName)) return false;

        if(!proceed()) {
            AppConstants.showSimpleSnackBar(activity,
                    "Child noe eligible for enrollment", AppConstants.TYPE_ERROR);
            return false;
        }

        return true;
    }

    private void eligible() {
        if (proceed()) {
            bi.eligible.setVisibility(View.VISIBLE);
        } else {
            bi.eligible.setVisibility(View.GONE);
            sF5.clearUnEligible();
        }
    }

    private boolean proceed() {
        boolean lowMuac = !sF5.getF512().isEmpty() && Float.parseFloat(sF5.getF512()) < 12.5f;
        boolean edema = sF5.getFo515().equals("1");
        return lowMuac || edema;
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