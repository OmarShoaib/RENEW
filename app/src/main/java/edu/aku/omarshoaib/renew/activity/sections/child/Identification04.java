package edu.aku.omarshoaib.renew.activity.sections.child;

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
import edu.aku.omarshoaib.renew.databinding.ActivityIdentification02Binding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form4;
import edu.aku.omarshoaib.renew.model.HCF;

public class Identification04 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = Identification04.this;

    ActivityIdentification02Binding bi;
    private AppDatabase appDatabase;

    private Form4.SF4 sF4;
    private Button posBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_identification_02);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.identification), "", false);

        // Init form1 for the first time
        Form4.initMeta();
        sF4 = new Form4.SF4();
        bi.setForm(sF4);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    private void initUI() {
        bi.f103.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f103.addTextChangedListener(new AppTextWatcher(bi.f103.getId(), textWatcher));
        bi.f103.setTextLocale(Locale.ENGLISH);
        setupHCFSpinner();
//        setupTeamSpinner();
    }

    /*private void setupTeamSpinner() {
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
                    sF4.setF101("");
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                sF4.setF101(item.split("-")[0].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if (!sF4.getF101().isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTeamId().trim().equals(sF4.getF101())) {
                    bi.f101.setSelection(i);
                    break;
                }
            }
        }

    }*/

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
                    sF4.setF403("");
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                sF4.setF403(item.split("-")[0].trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        if (!sF4.getF403().isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getHfCode().trim().equals(sF4.getF403())) {
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
        if (appDatabase.form4Dao().isFormSynced(MainApp.user.getDistId(), scrId))
            MainApp.isSynced = true;

        // New form1
//        String clusterNo = Objects.requireNonNull(bi.a101.getText()).toString();
        MainApp.form4.setScrId(scrId);
        MainApp.form4.setSF4(sF4);
        Form4.saveMainData(scrId);
//        MainApp.form4.setTeamId(sF4.getF101());
//        Form4.SF4.saveData(sF4);
        AppConstants.gotoActivity(activity, SectionF04.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}