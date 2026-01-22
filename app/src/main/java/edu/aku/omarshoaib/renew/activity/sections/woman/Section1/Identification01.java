package edu.aku.omarshoaib.renew.activity.sections.woman.Section1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityIdentification01Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.HCF;
import edu.aku.omarshoaib.renew.model.Teams;

public class Identification01 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = Identification01.this;

    ActivityIdentification01Binding bi;
    private AppDatabase appDatabase;

    private Form1.SF1 sF1;
    private Button posBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_identification_01);
        super.activity = activity;

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
        bi.f103.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f103.addTextChangedListener(new AppTextWatcher(bi.f103.getId(), textWatcher));
        setupHCFSpinner();
        setupTeamSpinner();
    }

    private void setupTeamSpinner() {
        List<Teams> list = new ArrayList<>();
        Teams team = new Teams();
        team.setTeamId("");
        team.setTeamName("Please Select");
        list.add(team);

        list.addAll(
                appDatabase.teamsDao().getAllData());
        ArrayAdapter<Teams> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        bi.f101.setAdapter(adapter);
        bi.f101.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sF1.setF101("");
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                sF1.setF101(item.split("-")[0].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if (!sF1.getF101().isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTeamId().trim().equals(sF1.getF101())) {
                    bi.f101.setSelection(i);
                    break;
                }
            }
        }

    }

    private void setupHCFSpinner() {
        List<HCF> list = new ArrayList<>();
        HCF ps = new HCF();
        ps.setHfCode("");
        ps.setHfName("Please Select");
        list.add(ps);

        list.addAll(
                appDatabase.hcfDao().getAllData());
        ArrayAdapter<HCF> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        bi.f102.setAdapter(adapter);
        bi.f102.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sF1.setF102("");
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                sF1.setF102(item.split("-")[0].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if (!sF1.getF102().isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getHfCode().trim().equals(sF1.getF102())) {
                    bi.f102.setSelection(i);
                    break;
                }
            }
        }

    }

    AppTextWatcher.IAppTextWatcher textWatcher = (viewId, text) -> {
        if (text.isEmpty()) {
            bi.scrId.setText(String.format(Locale.getDefault(), "%02d-######-",
                    MainApp.user.getUserId()));
            bi.f103a.setText("");
        } else {
            String[] date = text.split("-");
            String result = MainApp.user.getUserId() + "-" + date[0].substring(2)
                    + date[1] + date[2] + "-";
            bi.scrId.setText(result);
        }
    };

    public boolean formValidation() {
        return Validator.emptyCheckingContainer(activity, bi.GrpName);
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;

        String scrId = bi.scrId.getText().toString() + Objects.requireNonNull(bi.f103a.getText());

        // Do not allow synced form1 to be edited
        if (appDatabase.form1Dao().isFormSynced(MainApp.user.getDistId(), scrId))
            MainApp.isSynced = true;

        // New form1
//        String clusterNo = Objects.requireNonNull(bi.a101.getText()).toString();
        MainApp.form1.setScrId(scrId);
        MainApp.form1.setTeamId(sF1.getF101());
        Form1.saveMainData(scrId);
        Form1.SF1.saveData(sF1);
        AppConstants.gotoActivity(activity, ParticipantListAC.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}