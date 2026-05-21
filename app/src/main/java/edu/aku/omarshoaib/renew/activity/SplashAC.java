package edu.aku.omarshoaib.renew.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.SecurityUtils;

public class SplashAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SplashAC.this;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        /* Update Code */
//        if (SecurityUtils.checkIfDeviceRootedAndExit(activity))
//            return;

        /* Old Code */
        // Check if device is rooted. If rooted then exit the app
//        if (AppConstants.checkIfDeviceRooted(activity))
//            return;

        /* Secondary Function */
        //Check Device Tampering
//       if (AppConstants.isDeviceTampered(activity))
//            return;

        /* For Logging */
//       Map<String, Boolean> diag = SecurityUtils.runRootChecks(activity);

        splash();
    }

    private void splash() {
        // in milliseconds
        int SPLASH_TIME = 1500;

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    AppConstants.gotoActivity(activity, LoginAC.class, true);
                }
            }
        };
        th.start();
    }


}