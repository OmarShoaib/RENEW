package edu.aku.omarshoaib.renew.activity.sections.Section2;

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
import edu.aku.omarshoaib.renew.model.Participant;

public class Identification02 extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = Identification02.this;

    ActivityIdentification01Binding bi;
    private AppDatabase appDatabase;

    private Button posBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_identification_01);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.identification), "", false);
        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    private void initUI() {
        bi.f103.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.f103.addTextChangedListener(new AppTextWatcher(bi.f103.getId(), textWatcher));
        bi.fldGrpCVf101.setVisibility(View.GONE);
        bi.fldGrpCVf102.setVisibility(View.GONE);
    }

    AppTextWatcher.IAppTextWatcher textWatcher = (viewId, text) -> {
        if(text.isEmpty()) {
            bi.scrId.setText(String.format(Locale.getDefault(),"%02d-######-",
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
        Form1 form1 = appDatabase.form1Dao().getDataByScrId(MainApp.user.getDistId(), scrId);
        if (form1 == null) {
            AppConstants.showSimpleSnackBar(activity, getString(R.string.scr_id_not_found),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }
        MainApp.form1 = form1;
        List<Participant> participantList = appDatabase.participantDao().getAllDataByUuid(form1.getUid());
        participantList = eligibleParticipants(participantList);
        if(participantList.isEmpty()) {
            AppConstants.showSimpleSnackBar(activity, getString(R.string.no_eligible_participants),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }
        MainApp.participantList = participantList;
        AppConstants.gotoActivity(activity, PHQ9ParticipantsAC.class, true);

        // New form1
//        String clusterNo = Objects.requireNonNull(bi.a101.getText()).toString();
//        MainApp.form1.setScrId(scrId);
//        Form1.saveMainData(scrId);
//        AppConstants.gotoActivity(activity, ParticipantListAC.class, true);
    }

    private List<Participant> eligibleParticipants(List<Participant> list) {
        List<Participant> eligibleList = new ArrayList<>();
        for (Participant participant : list) {
            if(participant.getSF1().getF108().equals("1"))
                eligibleList.add(participant);
        }
        return eligibleList;
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}