package edu.aku.omarshoaib.renew.activity.sections.child;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;
import com.validatorcrawler.aliazaz.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySectionF06Binding;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form6;

public class SectionF06 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SectionF06.this;

    ActivitySectionF06Binding bi;
    private AppDatabase appDatabase;
    private Form6.SF6 sF6;
    private List<RadioGroup> f610RadioGroups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_f06);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.f6t0), getString(R.string.f6t1), false);
        appDatabase = AppDatabase.getDBInstance();

        sF6 = Form6.SF6.getData();
        sF6 = sF6 == null ? new Form6.SF6() : sF6;
        bi.setForm(sF6);
        initUI();
    }

    private void initUI() {
        setChangeListeners();
        bi.f602.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        sF6.setF604(MainApp.vFormF06.getParticipantId());
        sF6.setF601(MainApp.form6.getUsername());
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form6.saveMainData(MainApp.vFormF06.getParticipantId());
        MainApp.form6.setIStatus("1");
        MainApp.form6.setEndingDate(DateUtils.getCurrentDateTime());
        Form6.SF6.saveData(sF6);
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
                f610RadioGroups.add((RadioGroup) child);

            if (child instanceof ViewGroup) {
                getAllRadioGroups((ViewGroup) child); // Recursive call
            }
        }
    }

    private void setChangeListeners() {
        getAllRadioGroups(bi.fldGrpCVf610);
        for(RadioGroup rg  : f610RadioGroups) rg.setOnCheckedChangeListener(listener);
    }

    private boolean areAnyJ517One() {
        return Stream.of(
                sF6.getF610a(), sF6.getF610b(), sF6.getF610c(),
                sF6.getF610d(), sF6.getF610e(), sF6.getF610f(),
                sF6.getF610g()
        ).anyMatch("1"::equals);
    }

    RadioGroup.OnCheckedChangeListener listener =
            ((group, checkedId) -> group.post(() ->
                    bi.f610Info.setVisibility(areAnyJ517One() ? View.VISIBLE : View.GONE)));
}