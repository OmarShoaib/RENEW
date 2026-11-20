package edu.aku.omarshoaib.renew.activity;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.sections.KAP.SectionA1;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityIdentificationBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.BLRandomized;
import edu.aku.omarshoaib.renew.model.Cluster;
import edu.aku.omarshoaib.renew.model.FormKAP;

public class IdentificationAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = IdentificationAC.this;

    ActivityIdentificationBinding bi;
    private AppDatabase appDatabase;

    private FormKAP.SA1 sF1;
    private Button posBtn;
    private String clusterNo, hhId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_identification);

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.identification), "", false);

        // Init form1 for the first time
        FormKAP.initMeta();
        sF1 = new FormKAP.SA1();
        bi.setForm(sF1);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    private void initUI() {
        posBtn = bi.endButtonsLayout.findViewById(R.id.posBtn);
        bi.a110.addTextChangedListener(new AppTextWatcher(bi.a110.getId(), iAppTextWatcher));
        bi.a109.addTextChangedListener(new AppTextWatcher(bi.a109.getId(), iAppTextWatcher));
    }

    private final AppTextWatcher.IAppTextWatcher iAppTextWatcher = new AppTextWatcher.IAppTextWatcher() {
        @Override
        public void afterTextChanged(int viewId, String text) {
            if (viewId == bi.a110.getId()) {
                clusterNo = _EMPTY_;
                bi.district.setText(_EMPTY_);
                bi.tehsil.setText(_EMPTY_);
                bi.uc.setText(_EMPTY_);
                bi.village.setText(_EMPTY_);
                bi.a109.setText(_EMPTY_);
                bi.a109.setEnabled(false);
            } else if (viewId == bi.a109.getId()) {
                hhId = _EMPTY_;
                bi.head.setVisibility(View.GONE);
                bi.head.setText(_EMPTY_);
                posBtn.setVisibility(View.GONE);
            }
        }
    };

    // Search Cluster
    public void search(View view) {
        AppConstants.hideSoftKeyboard(activity);
        if (!Validator.emptyEditTextPicker(activity, bi.a110)) return;
        clusterNo = sF1.getA110();
        Cluster cluster = appDatabase.clusterDao().getDataByClusterNo(clusterNo);
        if (cluster != null) {
            bi.district.setText(cluster.getDistrictName());
            bi.tehsil.setText(cluster.getTehsilName());
            bi.uc.setText(cluster.getUcName());
            bi.village.setText(cluster.getVillageName());
            bi.a109.setEnabled(true);
        } else AppConstants.showSimpleSnackBar(activity, getString(R.string.incorrect_cluster_code),
                AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
    }

    public void searchHH(View view) {
        AppConstants.hideSoftKeyboard(activity);
        if (!Validator.emptyEditTextPicker(activity, bi.a109)) return;
        hhId = sF1.getA109();
        String tabNo = hhId.split("-")[0];

        BLRandomized bl = appDatabase.blRandomizedDao().getDataByHHId(clusterNo, tabNo, hhId);
        if (bl != null) {
            bi.head.setVisibility(View.VISIBLE);
            bi.head.setText(AppConstants.toTitleCase(bl.getHhHead()));
            posBtn.setVisibility(View.VISIBLE);
        } else AppConstants.showSimpleSnackBar(activity, getString(R.string.incorrect_hhId),
                AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;

        // Do not allow synced form1 to be edited
        // Form1 has been Synced
        MainApp.isSynced = appDatabase.formKAPDao().isFormSynced(clusterNo, hhId);

        // New formKAP
        MainApp.formKAP.setHhid(hhId);
        MainApp.formKAP.setHcfCode(clusterNo);
        FormKAP.saveMainData(hhId, clusterNo);

        AppConstants.gotoActivity(activity, SectionA1.class, true);
    }

    public boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    @Override
    public void onBackPressed() {
        AppConstants.gotoActivity(activity, ModuleAC.class, true);
    }
}