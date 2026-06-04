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
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
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
        bi.lastVisitSachets.setText(String.format("Last Visit Sachets: %s", MainApp.vFormF06.getNoOfSachets()));
        bi.f602.setMinDate(MainApp.vFormF06.getEnrollmentDate());
        bi.f604b.setMinDate(MainApp.vFormF06.getEnrollmentDate());
        bi.f602.addTextChangedListener(new AppTextWatcher(bi.f602.getId(), iAppTextWatcher));
        bi.f611.setMaxvalue(Float.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f61296x.setMaxvalue(Float.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f61396x.setMaxvalue(Float.parseFloat(MainApp.vFormF06.getNoOfSachets()));
        bi.f611.addTextChangedListener(new AppTextWatcher(bi.f611.getId(), iAppTextWatcher));
        bi.f61296x.addTextChangedListener(new AppTextWatcher(bi.f61296x.getId(), iAppTextWatcher));
        bi.f61396x.addTextChangedListener(new AppTextWatcher(bi.f61396x.getId(), iAppTextWatcher));
        bi.f605.addTextChangedListener(new AppTextWatcher(bi.f605.getId(), iAppTextWatcher));
        sF6.setF601(MainApp.form6.getUsername());
        sF6.setF603(MainApp.vFormF06.getVisitNumber());
        MainApp.form6.setFollowupNo(MainApp.vFormF06.getVisitNumber());
        sF6.setF604(MainApp.vFormF06.getParticipantId());
        bi.f61905x.setMinDate(MainApp.vFormF06.getEnrollmentDate());
//        bi.f604a.setOnCheckedChangeListener((group, checkedId) -> {
//            group.post(() -> {
//                if(sF6.getF604a().equals("3")) {
//                    if(MainApp.vFormF06.getLastVisitF604a().equals("3")) {
//                        bi.fldGrpCVf619.setVisibility(View.VISIBLE);
//                        AppConstants.disableViews(activity, bi.f619);
//                        sF6.setF619("3");
//                    } else {
//                        bi.fldGrpCVf619.setVisibility(View.GONE);
//                        AppConstants.enableViews(activity, bi.f619);
//                        sF6.setF619("");
//                    }
//                } else if (sF6.getF604a().equals("1")) {
//                    bi.fldGrpCVf619.setVisibility(View.VISIBLE);
//                } else if (sF6.getF604a().equals("2")) {
//                    bi.fldGrpCVf619.setVisibility(View.GONE);
//                    sF6.setF619("");
//                }
//            });
//        });
    }

    AppTextWatcher.IAppTextWatcher iAppTextWatcher = (viewId, text) -> {
        if(viewId == bi.f602.getId()) {
            if(text.isEmpty())
                bi.f604b.setMaxDate(DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT));
            else bi.f604b.setMaxDate(text);
        } else if(viewId == bi.f611.getId() || viewId == bi.f61296x.getId() ||
                viewId == bi.f61396x.getId()) {
            int f611 = sF6.getF611().isEmpty() ? 0 : Integer.parseInt(sF6.getF611()),
                    f612 = sF6.getF61296x().isEmpty() ? 0 : Integer.parseInt(sF6.getF61296x()),
                    f613 = sF6.getF61396x().isEmpty() ? 0 : Integer.parseInt(sF6.getF61396x());
            int previousSachets = Integer.parseInt(MainApp.vFormF06.getNoOfSachets());

            if(f611+f612+f613 < previousSachets) bi.fldGrpCVf613a.setVisibility(View.VISIBLE);
            else {
                bi.fldGrpCVf613a.setVisibility(View.GONE);
                sF6.setF613a("");
            }
        } else if(viewId == bi.f605.getId()) {
            if(text.isEmpty()) return;
            Float muac = Float.parseFloat(text);
            if(muac >= 11.5f && muac < 12.5f) {
                bi.fldGrpCVf609.setVisibility(View.GONE);
                sF6.setF609("");
            } else bi.fldGrpCVf609.setVisibility(View.VISIBLE);
        }
    };

    private boolean formValidation() {
        if(!Validator.emptyCheckingContainer(activity,bi.GrpName)) return false;

        if(sF6.getF604a().equals("1")) {
            int f611 = sF6.getF611().isEmpty() ? 0 : Integer.parseInt(sF6.getF611()),
                    f612 = sF6.getF61296x().isEmpty() ? 0 : Integer.parseInt(sF6.getF61296x()),
                    f613 = sF6.getF61396x().isEmpty() ? 0 : Integer.parseInt(sF6.getF61396x());
            int previousSachets = Integer.parseInt(MainApp.vFormF06.getNoOfSachets());
            if (f611 + f612 + f613 > previousSachets) {
                Validator.emptyCustomTextBox(activity, bi.f611, "Incorrect count");
                return false;
            }
        }

        return true;
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        Form6.saveMainData(MainApp.vFormF06.getParticipantId(), MainApp.vFormF06.getVisitNumber());
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
            if (child instanceof RadioGroup) f610RadioGroups.add((RadioGroup) child);
            else if (child instanceof ViewGroup) getAllRadioGroups((ViewGroup) child); // Recursive call

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