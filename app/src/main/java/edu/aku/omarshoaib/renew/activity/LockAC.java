package edu.aku.omarshoaib.renew.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.databinding.ActivityLockBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.UserAuth;

public class LockAC extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private Activity activity = LockAC.this;

    ActivityLockBinding bi;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_lock);
        bi.setCallback(this);

        if (MainApp.user != null)
            bi.usernameET.setText(MainApp.user.getUsername());
    }

    public void attemptUnlock(View view) {
        if (UserAuth.checkPassword(bi.passwordET.getText().toString(), MainApp.user.getPasswordEnc())) {
            finish();
        } else {
            bi.passwordET.setError(getString(R.string.password_not_matched));
        }
    }

    public void closeApp(View view) {
        finish();
        startActivity(new Intent(activity, LoginAC.class).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // Show/Hide password
    public void togglePassVisibility(View view) {
        if (bi.passwordET.getTransformationMethod() == null) {
            bi.passwordET.setTransformationMethod(new PasswordTransformationMethod());
            bi.passwordIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_locked));
        } else {
            bi.passwordET.setTransformationMethod(null);
            bi.passwordIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_unlocked));
        }
    }

    @Override
    public void onBackPressed() {
        AppConstants.showSimpleSnackBar(activity, getString(R.string.back_pressed_not_allowed),
                AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
    }
}