package edu.aku.omarshoaib.renew.activity.sections.woman.Section1;

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
import edu.aku.omarshoaib.renew.adapter.GenericAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityParticipantListBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Participant;

public class ParticipantListAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = ParticipantListAC.this;

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
        AppConstants.initToolbar(activity, getString(R.string.participant_list),
                getString(R.string.participant_list), false);
        bi.titleTV.setText("Participant List");

        appDatabase = AppDatabase.getDBInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApp.participantList = appDatabase.participantDao().getAllDataByUuid(MainApp.form1.getUid());

        if (MainApp.participantList == null || MainApp.participantList.isEmpty()) {
            bi.rv.setVisibility(View.GONE);
            bi.emptyTV.setVisibility(View.GONE);
            bi.endButtonsLayout.findViewById(R.id.posBtn).setVisibility(View.INVISIBLE);
            return;
        }

        bi.totalTV.setText(String.valueOf(MainApp.participantList.size()));

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

                lineNoTV.setText(String.format(Locale.ENGLISH, "Participant# %02d", item.getLineNo()));
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.fetus));
                nameTV.setText(item.getSF1().getF104());

                view.setOnClickListener(view1 -> {
                    MainApp.participant = MainApp.participantList.get((int) view1.getTag());
                    AppConstants.gotoActivity(activity, SectionF01.class, true);
                });
            }
        };
        bi.rv.setAdapter(genericAdapter);
        boolean isCountMismatch = MainApp.participantList.size() >= 3;

//        bi.addMoreBtn.setVisibility(isCountMismatch ? View.GONE : View.VISIBLE);
        bi.endButtonsLayout.findViewById(R.id.posBtn).setVisibility(isCountMismatch ? View.VISIBLE : View.INVISIBLE);
    }

    public void btnAddMore(View view) {
        Participant.initMeta(MainApp.participantList.size() + 1);
        AppConstants.gotoActivity(activity, SectionF01.class, true);
    }

    public void btnContinue(View view) {
        finish();
        startActivity(new Intent(activity, EndingAC.class).putExtra("complete", true));
    }

    public void btnEnd(View view) {
        AppConstants.checkDoubleCancelPress(activity, EndingAC.class);
    }
}