package edu.aku.omarshoaib.renew.activity.sections.child;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF05Binding;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form4;
import edu.aku.omarshoaib.renew.model.Form5;

public class SectionF05 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF05.this;

    ActivitySectionF05Binding bi;
    private AppDatabase appDatabase;
    private Form5.SF5 sF5;
    private List<RadioGroup> f0515RadioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f05);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f5t0), getString(R.string.f5t1), false);
        appDatabase = AppDatabase.getDBInstance();

        MainApp.form5 = appDatabase.form5Dao().getDataByScrId(MainApp.user.getDistId(), MainApp.form4.getScrId());
        if (MainApp.form5 == null) Form5.initMeta();
        sF5 = Form5.SF5.getData();
        sF5 = sF5 == null ? new Form5.SF5() : sF5;
        bi.setForm(sF5);
        initUI();
    }

    private void initUI() {
        setChangeListeners();
        sF5.setF501(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
        sF5.setF502(MainApp.form4.getSF4().getF402());
        bi.f502.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f506.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f502.addTextChangedListener(new AppTextWatcher(bi.f502.getId(), dateTextWatcher));
        bi.f506.addTextChangedListener(new AppTextWatcher(bi.f506.getId(), dateTextWatcher));
//        bi.f512.addTextChangedListener(new AppTextWatcher(bi.f512.getId(),
//                (viewId, text) -> eligible()));
//        bi.fo515.setOnCheckedChangeListener((rG, i) -> rG.post(this::eligible));
        bi.f503.setOnCheckedChangeListener(f503Listener);
    }

    RadioGroup.OnCheckedChangeListener f503Listener = (RadioGroup radioGroup, int i) -> radioGroup.post(() -> {
        if(i == bi.f50301.getId()) {
            sF5.setF504(MainApp.form4.getSF4().getF405());
            sF5.setF511(MainApp.form4.getSF4().getF404());
            sF5.setF512(MainApp.form4.getSF4().getF410());
            sF5.setFo515(MainApp.form4.getSF4().getF409());
            return;
        }
        sF5 = new Form5.SF5();
        sF5.setF501(MainApp.user.getFullName()+" - "+MainApp.user.getUserId());
        sF5.setF502(MainApp.form4.getSF4().getF402());
        bi.setForm(sF5);
    });

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
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
        /*if(!proceed()) {
            AppConstants.showSimpleSnackBar(activity,
                    "Child not eligible for enrollment", AppConstants.TYPE_ERROR);
            return false;
        }*/
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form5.saveMainData(MainApp.form4.getScrId());
        MainApp.form5.setIStatus("1");
        Form5.SF5.saveData(sF5);
        AppConstants.gotoActivity(activity, MainActivity.class, true);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }

    private void getAllRadioGroups(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RadioGroup)
                f0515RadioGroups.add((RadioGroup) child);

            if (child instanceof ViewGroup) {
                getAllRadioGroups((ViewGroup) child); // Recursive call
            }
        }
    }

    private void setChangeListeners() {
        getAllRadioGroups(bi.fldGrpCVf517);
        for(RadioGroup rg  : f0515RadioGroups) rg.setOnCheckedChangeListener(listener);
    }

    private boolean areAnyJ517One() {
        return Stream.of(
                sF5.getF517a(), sF5.getF517b(), sF5.getF517c(),
                sF5.getF517d(), sF5.getF517e(), sF5.getF517f(),
                sF5.getF517g()
        ).anyMatch("1"::equals);
    }

    RadioGroup.OnCheckedChangeListener listener =
            ((group, checkedId) -> group.post(() -> {
                /*if (areAnyJ517One()) {
                    bi.fldGrpCVj0401m.setVisibility(View.VISIBLE);
                } else {
                    bi.fldGrpCVj0401m.setVisibility(View.GONE);
                    sJ4.setJ0401ma(_EMPTY_);
                    sJ4.setJ0401mb(_EMPTY_);
                    sJ4.setJ0401mc(_EMPTY_);
                    sJ4.setJ0401md(_EMPTY_);
                    sJ4.setJ0401me(_EMPTY_);
                    sJ4.setJ0401mf(_EMPTY_);
                    sJ4.setJ0401mxx(_EMPTY_);
                }*/
            }));

    /*private boolean proceed() {
        boolean lowMuac = !sF5.getF512().isEmpty() && Float.parseFloat(sF5.getF512()) < 12.5f;
        boolean edema = sF5.getFo515().equals("1");
        return lowMuac || edema;
    }*/

    /*private void eligible() {
        if (proceed()) {
            bi.eligible.setVisibility(View.VISIBLE);
        } else {
            bi.eligible.setVisibility(View.GONE);
            sF5.clearUnEligible();
        }
    }*/
}