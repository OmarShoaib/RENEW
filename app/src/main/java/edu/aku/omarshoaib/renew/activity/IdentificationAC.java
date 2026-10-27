package edu.aku.omarshoaib.renew.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.List;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.sections.KAP.SectionA1;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityIdentificationBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.views.SpinnerBuilder;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.FormKAP;
import edu.aku.omarshoaib.renew.model.HCF;

public class IdentificationAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = IdentificationAC.this;

    ActivityIdentificationBinding bi;
    private AppDatabase appDatabase;

    private FormKAP.SA1 sF1;
    private Button posBtn;

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
        List<HCF> hcfList = appDatabase.hcfDao().getAllData();

        new SpinnerBuilder<>(activity, bi.a110, hcfList, value -> sF1.setA110(value))
                .setValueAs(hcf -> String.valueOf(hcf.getHfCode()))
                .displayValueAs(HCF::getHfName)
                .hintText("Select an option")
                .build();
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;

        String hhId = sF1.getA109();
        String hcfCode = sF1.getA110();

        // Do not allow synced form1 to be edited
        if (appDatabase.formKAPDao().isFormSynced(hcfCode, hhId))
            // Form1 has been Synced
            MainApp.isSynced = true;

        // New formKAP
        MainApp.formKAP.setHhid(hhId);
        MainApp.formKAP.setHcfCode(hcfCode);
        FormKAP.saveMainData(hhId, hcfCode);

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