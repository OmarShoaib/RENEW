package edu.aku.omarshoaib.renew.activity.sections.Section2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import java.util.Locale;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.EndingAC;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.activity.sections.Section1.ParticipantListAC;
import edu.aku.omarshoaib.renew.activity.sections.Section1.SectionF01;
import edu.aku.omarshoaib.renew.adapter.GenericAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityParticipantListBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.Participant;

public class EligibleParticipantsAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = EligibleParticipantsAC.this;

    ActivityParticipantListBinding bi;
    private AppDatabase appDatabase;
    private GenericAdapter<Participant> genericAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppStructure_FullScreen_StatusBar);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_participant_list);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.eligible_participant_list),
                "", false);
        bi.titleTV.setText(getString(R.string.eligible_participant_list));
        bi.addMoreBtn.setVisibility(View.INVISIBLE);

        appDatabase = AppDatabase.getDBInstance();
    }

    private boolean isCompleted(Participant participant) {
        for (Form2 form2 : MainApp.listForm2)
            if(form2.getUuId().equals(participant.getUid()))
                return true;

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bi.totalTV.setText(String.valueOf(MainApp.participantList.size()));
        MainApp.listForm2 = appDatabase.form2Dao().getDataByScrId(MainApp.form1.getScrId());

        genericAdapter = new GenericAdapter<Participant>(activity, MainApp.participantList,
                bi.rv, null, false) {
            @Override
            protected View createView(Activity activity, ViewGroup viewGroup, int viewType) {
                return LayoutInflater.from(activity).inflate(R.layout.item_member,
                        viewGroup, false);
            }

            @Override
            protected void bindView(Participant item, GenericAdapter<Participant>.ViewHolder viewHolder, int position, boolean isMultiSelect) {
                View view = viewHolder.itemView;

                view.setTag(position);
                TextView lineNoTV = view.findViewById(R.id.lineNoTV);
                TextView nameTV = view.findViewById(R.id.nameTV);
                ImageView imageView = view.findViewById(R.id.iv);
                ImageView completeFlagIV = view.findViewById(R.id.completeFlagIV);
                completeFlagIV.setVisibility(isCompleted(item) ? View.VISIBLE : View.INVISIBLE);

                lineNoTV.setText(String.format(Locale.ENGLISH, "Participant# %02d", item.getLineNo()));
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.fetus));
                nameTV.setText(item.getSF1().getF104());

                view.setOnClickListener(view1 -> {
                    MainApp.participant = MainApp.participantList.get((int) view1.getTag());
                    MainApp.form2 = appDatabase.form2Dao().getDataByUuid(MainApp.participant.getUid(), MainApp.form1.getScrId());
                    if (MainApp.form2 == null) Form2.initMeta();
                    AppConstants.gotoActivity(activity, SectionF02.class, true);
                });
            }
        };
        bi.rv.setAdapter(genericAdapter);
        boolean isCountMismatch = MainApp.participantList.size() == MainApp.listForm2.size();
        bi.endButtonsLayout.findViewById(R.id.posBtn).setVisibility(isCountMismatch ? View.VISIBLE : View.INVISIBLE);
    }



    public void btnAddMore(View view) {
        Participant.initMeta(MainApp.participantList.size() + 1);
        AppConstants.gotoActivity(activity, SectionF01.class, true);
    }

    public void btnContinue(View view) {
        AppConstants.gotoActivity(activity, MainActivity.class, true);
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}