package edu.aku.omarshoaib.renew.activity;

/* Base activity for sections */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Locale;

import dev.b3nedikt.restring.Restring;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.Helper;
import edu.aku.omarshoaib.renew.global.SharedPrefs;

public class BaseActivity extends AppCompatActivity {

    // Reference of child activity
    public Activity activity;
    // To implement anything after loading child and parent activity i.e. onFullLoad
    public IBaseAC iBaseAC;
    // SkipEnd button visibility after sync form1. By default its visible.
    public boolean isSkipEndVisible = true;

    private final Handler idleHandler = new Handler();
    // This boolean is used to prevent calling startHandler when switching activity.
    // We intentionally made condition in onPause because we need to continue the runnable
    // when the activity is in background state but stop the handle when switching activity
//    private boolean isACSwitching = false;

    private Helper.IHelperCB iHelperCB;

    // For Server strings update on UI
    private Resources restringResources;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Restring.setLocale(new Locale(SharedPrefs.read(SharedPrefs.LOCALE, AppConstants.LOCALE_ENGLISH)));
        setTheme(R.style.Theme_AppStructure_FullScreen_StatusBar_Sections);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Helper.resetRadioGroupList();
    }

    // This timeout is just for the beeping sound
    @SuppressWarnings("PointlessArithmeticExpression")
    CountDownTimer countDownTimer = new CountDownTimer(1 * 60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            if ((millisUntilFinished / 1000) < 14) {
                MainApp.toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            }
        }

        public void onFinish() {
//            isACSwitching = true;
            startActivity(new Intent(getApplicationContext(), LockAC.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            stopHandler();
        }
    };

    private void startHandler() {
        countDownTimer.cancel();
        idleHandler.postDelayed(idleRunnable, AppConstants.IDLE_TIMEOUT);
    }

    private void stopHandler() {
        idleHandler.removeCallbacks(idleRunnable);
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    private final Runnable idleRunnable = () -> {
        // Handle IDLE state
        countDownTimer.start();
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
//        if (!isACSwitching) {
        stopHandler();
        startHandler();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isACSwitching = false;
        startHandler();
        // For checking radio groups clear checks
        Helper.radioGroupsClearChecks(this, activity);

        // Apply ranges from server
        Helper.applyRanges(activity);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Restring.wrapContext(newBase));
    }

    @Override
    public Resources getResources() {
        if (restringResources == null) {
            restringResources = Restring.wrapContext(super.getBaseContext()).getResources();
        }
        return restringResources;
    }

    @Override
    protected void onPause() {
//        if (isACSwitching)
        stopHandler();
        if (iHelperCB != null)
            iHelperCB.clearCheckRunnableStatus(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopHandler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (activity == null)
            return false;
        if ((MainApp.formType == 1 && MainApp.form1 != null && !AppConstants.isEmpty(MainApp.form1.getSynced())) ||
                (MainApp.formType == 2 && MainApp.form2 != null && !AppConstants.isEmpty(MainApp.form2.getSynced())) ||
                (MainApp.formType == 3 && MainApp.form2a != null && !AppConstants.isEmpty(MainApp.form2a.getSynced())) ||
                (MainApp.formType == 4 && MainApp.form2b != null && !AppConstants.isEmpty(MainApp.form2b.getSynced())) ||
                (MainApp.formType == 5 && MainApp.form3a != null && !AppConstants.isEmpty(MainApp.form3a.getSynced())) ||
                (MainApp.formType == 6 && MainApp.form3 != null && !AppConstants.isEmpty(MainApp.form3.getSynced())) ||
                (MainApp.formType == 7 && MainApp.form4 != null && !AppConstants.isEmpty(MainApp.form4.getSynced())) ||
                (MainApp.formType == 8 && MainApp.form6 != null && !AppConstants.isEmpty(MainApp.form6.getSynced()))) {

            View endingButtonsLayout = activity.findViewById(R.id.endButtonsLayout);
            // This check is just for safe side in case
            if (endingButtonsLayout != null) {
                if (endingButtonsLayout.findViewById(R.id.posBtn) != null)
                    // If form1 is in view mode after sync then change posBtn text from save to next
                    ((Button) endingButtonsLayout.findViewById(R.id.posBtn)).setText(getString(R.string.next));
                if (endingButtonsLayout.findViewById(R.id.negBtn) != null)
                    // If form1 is in view mode after sync then hide negBtn
                    endingButtonsLayout.findViewById(R.id.negBtn).setVisibility(View.INVISIBLE);
            }

            try {
                AppConstants.isAnyViewFilled(activity.findViewById(R.id.GrpName));
                // Next layout/section is empty
                finish();
                startActivity(new Intent(activity, EndingAC.class).putExtra("complete", true));
            } catch (Exception e) {
                // Next layout/section is filled.
                // Arrays.asList can be app specific. It is used to prevent disabling views
                // in view mode
                AppConstants.disableViews(activity, Collections.singletonList(activity.findViewById(R.id.GrpName)),
                        Collections.singletonList("posBtn"));
                if (isSkipEndVisible) showSkipToEndBtn(menu);
                /*if (iBaseAC != null)
                    iBaseAC.onFullLoad();*/
            }
        } else if (MainApp.form1 != null && MainApp.form1.isFormCompleteOnce()) {
            // This logic is used to mark the form1 that its completed once.
            // This logic is used to display 'Skip to End' button over
            // the sections if user open the form1 in edit mode and the form1 has been completed once,
            // update any section/value, save it and then directly skip to end without traversing
            // the whole form1 again.
            // Note: User must save the updated value by clicking on usual save btn first then
            // can click on skip to end on the next activity if don't want to edit any other value.
            // Requirement:
            // setFormCompleteOnce() must be set to 'true' on EndingAC for showing 'Skip to End' btn
            if (isSkipEndVisible) showSkipToEndBtn(menu);
        }

        if (iBaseAC != null)
            iBaseAC.onFullLoad();

        // For testing purpose - For developers
        if (AppConstants.IS_ADMIN) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_save, menu);
            // Save button is for the development purpose to help developers save the
            // section without scrolling to the bottom.
            // Requirement:
            // Add 'super.activity = activity;' in onCreate()
            // where 'activity' is the context of the current activity
            MenuItem saveBtn = menu.findItem(R.id.saveBtn);
            saveBtn.setVisible(true);
            saveBtn.setOnMenuItemClickListener(item -> {
                AppConstants.callMethod(activity, findViewById(item.getItemId()), "btnContinue");
                return true;
            });
            return true;
        }
        return false;
    }

    // To display skipToEnd button.
    private void showSkipToEndBtn(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);

        MenuItem skipToEndBtn = menu.findItem(R.id.skipEndBtn);
        skipToEndBtn.setVisible(true);
        skipToEndBtn.setOnMenuItemClickListener(item -> {
            // Check if current form1 is filled before proceeding to end.
            // null for calling no argument function
            Object returnObj = AppConstants.callMethod(activity, "formValidation");
            // In this case, returned object is a Boolean as we are calling
            // 'formValidation' function of the activity
            if (returnObj != null && !((boolean) returnObj))
                return false;

            finish();
            startActivity(new Intent(activity, EndingAC.class).putExtra("complete", true));
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        AppConstants.showSimpleSnackBar(activity,
                getString(R.string.back_pressed_not_allowed), AppConstants.TYPE_ERROR);
    }

    // Set IHelperCB to update the clearCheck handler running status on Helper class
    public void setIHelperCB(Helper.IHelperCB iHelperCB) {
        this.iHelperCB = iHelperCB;
    }

    // Callback of parent activity in child activity
    public interface IBaseAC {
        // To call a child method after loading parent activity.
        // Here it is explicitly used to call a method after onCreateOptionsMenu
        void onFullLoad();
    }
}
