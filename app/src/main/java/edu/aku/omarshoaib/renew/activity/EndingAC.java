package edu.aku.omarshoaib.renew.activity;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityEndingBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;

public class EndingAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = EndingAC.this;

    ActivityEndingBinding bi;
    private AppDatabase appDatabase;

    private RadioGroup radioGroup;
    private RadioButton completedRB, refusedRB;
    private EditText otherET;

    private boolean check;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.activity = activity;
        super.iBaseAC = iBaseAC;
        super.isSkipEndVisible = false;

        bi = DataBindingUtil.setContentView(this, R.layout.activity_ending);
        bi.setCallback(this);

        appDatabase = AppDatabase.getDBInstance();
        check = getIntent().getBooleanExtra("complete", false);

        initUI();
    }

    private void initUI() {
        // 1- Views enable/disable conditions based on form1
        initConditions();
        // 3- Preset values in exists
        presetValues();
    }

    private void initConditions() {
        radioGroup = bi.istatus;
        otherET = bi.istatus96x;
        completedRB = bi.istatusa;
        refusedRB = bi.istatusb;

        // Put all the specific views here which depends on any
        // form1 field condition. It means that all only one of the specified
        // view is enabled and all others are disabled only if form1 is incomplete
        // i.e. check=false
        List<View> conditionViews = new ArrayList<View>() {{
            // Insert views that needs to be disabled if form1 cancels in between
            /* Reference Code
            add(bi.a11510);*/
        }};

        // viewEnabled = The view that should only be enabled and all other views
        // should be disabled
        View viewEnabled = null;

        // Enter conditions based on teh form1 to enable the only
        // radio button if condition verifies
        /* Reference code
        if (MainApp.form1.getSA1().getA114().equals("2"))
            viewEnabled = conditionViews.get(0);   // Consent given?*/

        // 2- Prepare views - RadioGroup views enable/disable based on above conditions
        prepareViews(viewEnabled, conditionViews);
    }

    // To enable/disable radio buttons based on the condition
    private void prepareViews(View viewEnabled, List<View> conditionViews) {
        if (!AppConstants.isEmpty(MainApp.form1.getSynced())) {
            String status = MainApp.form1.getIStatus();
            int radioGroupIndex = status.equals("96") ?
                    radioGroup.getChildCount() - 1 : Integer.parseInt(status);
            RadioButton checkedRB = (RadioButton) radioGroup.getChildAt(radioGroupIndex - 1);
            AppConstants.disableViews(activity, radioGroup,
                    Collections.singletonList(AppConstants.getViewNameByView(activity, checkedRB)));
            return;
        }
        List<String> viewNames = new ArrayList<>();
        for (View view : conditionViews) {
            // This is a special check only for refused condition
            if (!check && view.getId() == refusedRB.getId()) continue;
            viewNames.add(AppConstants.getViewNameByView(activity, view));
        }
        if (viewEnabled != null)   // If form1 is NOT successfully completed - Based on tool conditions
            AppConstants.disableViews(activity, radioGroup,
                    Collections.singletonList(AppConstants.getViewNameByView(activity, viewEnabled)));
        else if (MainApp.form1.getIStatus().equals("1") || check) // If form1 is successfully completed
            AppConstants.disableViews(activity, radioGroup,
                    Collections.singletonList(AppConstants.getViewNameByView(activity, completedRB)));
        else {
            // If form1 is cancelled in between
            viewNames.add(AppConstants.getViewNameByView(activity, completedRB));
            AppConstants.enableViews(activity, radioGroup, viewNames);
        }
    }

    // If form1 is is once reach on EndingAC in any case whether cancel or
    // form1 complete we check previous iStatus if exists and preset it to UI
    private void presetValues() {
        if (MainApp.form1 != null && !AppConstants.isEmpty(MainApp.form1.getIStatus())) {
            String status = MainApp.form1.getIStatus();
            int radioGroupIndex = status.equals("96") ?
                    radioGroup.getChildCount() - 1 : Integer.parseInt(status);
            RadioButton checkedRB = (RadioButton) radioGroup.getChildAt(radioGroupIndex - 1);
            if (checkedRB.isEnabled()) {
                // The view must be enabled in order to preset
                radioGroup.check(checkedRB.getId());
                if (status.equals("96"))
                    otherET.setText(MainApp.form1.getIStatus96x());
            }
        }
    }

    public boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void onIStatusCheckChanged(RadioGroup radioGroup, int checkedId) {
        otherET.setText(_EMPTY_);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        String status = (String) findViewById(radioGroup.getCheckedRadioButtonId()).getTag();

        /*MainApp.form1.setIStatus(status);
        MainApp.form1.setIStatus96x(otherET.getText().toString());*/

        // If the Last Section has been filled once then it indicates that the form1
        // has been fully filled
        boolean isFormCompleteOnce = false;
        /*if(MainApp.form1.<getLastSection()> !=null)
            isFormCompleteOnce = true
            MainApp.form1.setFormCompleteOnce(true);*/

        String endingDate = _EMPTY_;
        if(AppConstants.isEmpty(MainApp.form1.getSyncDate()))
            endingDate = DateUtils.getCurrentDateTime();

        appDatabase.form1Dao().updateIStatus(MainApp.form1.getId(), status, otherET.getText().toString(), isFormCompleteOnce, endingDate);
        //appDatabase.formDao().update(MainApp.form1);
        AppConstants.gotoActivity(activity, MainActivity.class, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // If form1 is synced then we locked the UI to show in view mode
    IBaseAC iBaseAC = new IBaseAC() {
        @Override
        public void onFullLoad() {
            ((Button) bi.endButtonsLayout.findViewById(R.id.posBtn)).setText(getString(R.string.done));
        }
    };

    @Override
    public void onBackPressed() {
        AppConstants.showSimpleSnackBar(activity, getString(R.string.back_pressed_not_allowed),
                AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
    }
}