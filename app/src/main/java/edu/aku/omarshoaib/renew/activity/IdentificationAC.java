package edu.aku.omarshoaib.renew.activity;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityIdentificationBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Cluster;
import edu.aku.omarshoaib.renew.model.Form1;

public class IdentificationAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = IdentificationAC.this;

    ActivityIdentificationBinding bi;
    private AppDatabase appDatabase;

    private Form1.SF1 sF1;
    private Button posBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_identification);

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.identification), "", false);

        // Init form1 for the first time
        Form1.initMeta();
        sF1 = new Form1.SF1();
        bi.setForm(sF1);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    private void initUI() {
        bi.a102.setEnabled(false);
        bi.a103.setEnabled(false);
        bi.a104.setEnabled(false);
        bi.a105.setEnabled(false);

        posBtn = bi.endButtonsLayout.findViewById(R.id.posBtn);

        bi.a101.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Objects.requireNonNull(bi.a101.getText()).toString().length() < 8) {
                    bi.a102.setText(_EMPTY_);
                    bi.a103.setText(_EMPTY_);
                    bi.a104.setText(_EMPTY_);
                    bi.a105.setText(_EMPTY_);
                    posBtn.setEnabled(false);
                    MainApp.form1.setDistrictCode(MainApp.user.getDistId());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Search Cluster
    public void search(View view) {
        if (AppConstants.isEmpty(bi.a101) || Objects.requireNonNull(bi.a101.getText()).toString().length() < 8)
            return;
        Cluster cluster = appDatabase.clusterDao().getDataByClusterNo(Objects.requireNonNull(bi.a101.getText()).toString());
        if (cluster != null) {
            bi.a102.setText(cluster.getDistrictName());
            bi.a103.setText(cluster.getTehsilName());
            bi.a104.setText(cluster.getUcName());
            bi.a105.setText(cluster.getVillageName());
            posBtn.setEnabled(true);
        } else {
            AppConstants.hideSoftKeyboard(activity);
            AppConstants.showSimpleSnackBar(activity, getString(R.string.incorrect_cluster_code),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        }
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;

        // Do not allow synced form1 to be edited
        if (appDatabase.form1Dao().isFormSynced(MainApp.user.getDistId(), Objects.requireNonNull(bi.a107.getText()).toString()))
            // Form1 has been Synced
            MainApp.isSynced = true;
        // New form1
        String clusterNo = Objects.requireNonNull(bi.a101.getText()).toString();
        String hhId = Objects.requireNonNull(bi.a107.getText()).toString();
        MainApp.form1.setDistrictCode(MainApp.user.getDistId());
        MainApp.form1.setScrId(hhId);
        Form1.saveMainData(hhId);
        Form1.SF1.saveData(sF1);
//            AppConstants.gotoActivity(activity, SectionA.class, true);
    }

    public boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppConstants.gotoActivity(activity, MainActivity.class, true);
    }
}