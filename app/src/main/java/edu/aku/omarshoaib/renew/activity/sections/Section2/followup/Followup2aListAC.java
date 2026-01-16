package edu.aku.omarshoaib.renew.activity.sections.Section2.followup;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.activity.MainActivity;
import edu.aku.omarshoaib.renew.adapter.Followup2aAdapter;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySection2aListBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.MainApp;

public class Followup2aListAC extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = Followup2aListAC.this;

    ActivitySection2aListBinding bi;
    private AppDatabase appDatabase;
//    private Loading loading;

    private Followup2aAdapter followup2aAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_section_2a_list);
        super.activity = activity;

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.list_of_mwras),
                _EMPTY_, false);

        appDatabase = AppDatabase.getDBInstance();
//        loading = new Loading(activity, false);

        initUI();
    }

    private void initUI() {
        MainApp.vPHQ9List = appDatabase.vphq9Dao().getAllData();
        bi.searchET.addTextChangedListener(new AppTextWatcher(bi.searchET.getId(), iAppTextWatcher));

//        markSyncedForms();

        bi.searchRG.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            String hint, mask;
            int inputType, maxLength;
            bi.searchET.setText(_EMPTY_);
            if (checkedId == bi.pidRB.getId()) {
                hint = getString(R.string.search_by_pid);
                inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
                mask = "#-####-###-##";
                maxLength = 13;
            } else {
                hint = getString(R.string.search_by_name);
                inputType = InputType.TYPE_CLASS_TEXT;
                mask = _EMPTY_;
                maxLength = 100;
            }
            bi.searchET.setHint(hint);
            bi.searchET.setInputType(inputType);
            bi.searchET.setMask(mask);
            bi.searchET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        });
    }

    private final AppTextWatcher.IAppTextWatcher iAppTextWatcher = (viewId, text) -> {
        if (bi.searchRG.getCheckedRadioButtonId() == -1) {
            AppConstants.showSimpleSnackBar(activity, getString(R.string.select_search_type),
                    AppConstants.TYPE_WARNING);
            return;
        }
        String tagStr = (String) bi.searchRG.findViewById(bi.searchRG.getCheckedRadioButtonId()).getTag();
        followup2aAdapter.setSearchType(Integer.parseInt(tagStr));
        followup2aAdapter.getFilter().filter(text);
    };

    @Override
    protected void onResume() {
        super.onResume();
//        loading.showLoading();

        new Handler().post(() -> {
            if (MainApp.vPHQ9List != null) {
                followup2aAdapter = new Followup2aAdapter(activity, MainApp.vPHQ9List);
                bi.mwraRV.setAdapter(followup2aAdapter);
                bi.mwraRV.setVisibility(View.VISIBLE);
                bi.emptyTV.setVisibility(View.GONE);
                bi.filterLayout.setVisibility(View.VISIBLE);
            } else {
                bi.mwraRV.setVisibility(View.GONE);
                bi.emptyTV.setVisibility(View.VISIBLE);
                bi.filterLayout.setVisibility(View.GONE);
            }
//            loading.hideLoading();
        });
    }

    @Override
    public void onBackPressed() {
        AppConstants.checkDoubleBackPress(activity, MainActivity.class);
    }
}
