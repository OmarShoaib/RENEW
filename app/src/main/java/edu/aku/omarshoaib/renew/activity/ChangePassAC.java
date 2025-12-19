package edu.aku.omarshoaib.renew.activity;

import static edu.aku.omarshoaib.renew.global.AppConstants.IS_CALL_ENCRYPTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Pattern;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityChangePassBinding;
import edu.aku.omarshoaib.renew.global.AlertPopup;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.Callbacks;
import edu.aku.omarshoaib.renew.global.Loading;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.UserAuth;
import edu.aku.omarshoaib.renew.model.ResetPass;
import edu.aku.omarshoaib.renew.webcall.web_client.CryptoUtil;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebCall;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class ChangePassAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = ChangePassAC.this;

    private ActivityChangePassBinding bi;

    private WebAPI webAPI;
    private WebCall webCall;
    private Loading loading;
    private Gson gson;
    private AppDatabase appDatabase;

    private static final String TAG_RESET_PASS = "RESET_PASS";
    private static final int POP_RESET_PASS = 101;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_change_pass);
        bi.setCallback(this);

        webAPI = WebClient.getInstance(activity).getWebAPI();
        webCall = new WebCall(activity, iWebCallback);
        loading = new Loading(activity, false);
        gson = new Gson();
        appDatabase = AppDatabase.getDBInstance();
    }

    // Password reset web service call
    public void attemptReset(View view) {
        if (!isPasswordValid()) return;

        // Generate hashed password
        String hashedPassword = UserAuth.generatePassword(bi.newPassET.getText().toString(), null);
        if (AppConstants.isEmpty(hashedPassword)) {
            AppConstants.showSimpleSnackBar(activity, getString(R.string.secured_pass_not_generated),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
            return;
        }

        ResetPass resetPass = new ResetPass();
        resetPass.setUserName(MainApp.user.getUsername());
        resetPass.setOldPassword(MainApp.user.getPasswordEnc());
        resetPass.setNewPassword(hashedPassword);

        loading.showLoading();
        String postJson = CryptoUtil.encrypt(gson.toJson(resetPass));
        webCall.call(webAPI.resetPassword(postJson), AppConstants.UPLOAD_DATA, TAG_RESET_PASS, 0, 0, IS_CALL_ENCRYPTED);
    }

    // Change password callback
    WebCall.IWebCallback iWebCallback = new WebCall.IWebCallback() {
        @Override
        public void onSuccess(String tag, String responseBody, int index, int total, List<String> list) {
            // Update user new password (after encryption) in the local db to prevent downloading
            // the user data again from server for login with new password
            MainApp.user.setPasswordEnc(UserAuth.generatePassword(bi.newPassET.getText().toString(), null));
            MainApp.user.setNewUser("0");
            appDatabase.userDao().update(MainApp.user);
            AlertPopup.alert(POP_RESET_PASS, activity, getString(R.string.success),
                    getString(R.string.password_reset_successful), AppConstants.TYPE_SUCCESS,
                    getString(R.string.ok), iAlertCallback);
            loading.hideLoading();
        }

        @Override
        public void onFailure(String tag, String errorMessage, int index, int total, List<String> list) {
            AlertPopup.alert(activity, getString(R.string.error), errorMessage, AppConstants.TYPE_ERROR);
            loading.hideLoading();
        }
    };

    // Password validation
    private boolean isPasswordValid() {
        String oldPass = bi.oldPassET.getText().toString();
        String newPass = bi.newPassET.getText().toString();
        String confirmPass = bi.confirmPassET.getText().toString();

        // Username and Password cannot be same
        if (newPass.equals(MainApp.user.getUsername())) {
            bi.newPassET.setError(getString(R.string.username_pass_same));
            return false;
        }

        // Old password cannot be empty
        if (AppConstants.isEmpty(oldPass)) {
            bi.oldPassET.setError(getString(R.string.field_empty_error));
            return false;
        }

        // Check for a valid password, if the user entered one.
        if (newPass.length() < AppConstants.MIN_PASS_LENGTH) {
            bi.newPassET.setError(String.format(getString(R.string.invalid_password),
                    AppConstants.MIN_PASS_LENGTH));
            return false;
        }

        // Password creation criteria
        String passRegex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@$!%*?&._-]).{" + AppConstants.MIN_PASS_LENGTH + ",}$";
        Pattern pattern = Pattern.compile(passRegex);
        if (!pattern.matcher(newPass).matches()) {
            bi.newPassET.setError(getString(R.string.password_creation_criteria));
            return false;
        }

        // Old password field must be equal to current password
        if (!UserAuth.checkPassword(oldPass, MainApp.user.getPasswordEnc())) {
            bi.oldPassET.setError(getString(R.string.incorrect_old_password));
            return false;
        }

        // Old and New Passwords cannot be same
        if (UserAuth.checkPassword(newPass, MainApp.user.getPasswordEnc())) {
            bi.newPassET.setError(getString(R.string.current_previous_password_cannot_same));
            return false;
        }

        // New and Confirm Passwords field must be same
        if (!newPass.equals(confirmPass)) {
            bi.confirmPassET.setError(getString(R.string.password_not_matched));
            return false;
        }

        return true;
    }

    // Show/Hide password
    public void togglePassVisibility(View view) {
        int viewId = view.getId();
        EditText editText;
        ImageView imageView;
        if (viewId == R.id.oldPassIV) {
            editText = bi.oldPassET;
            imageView = bi.oldPassIV;
        } else if (viewId == R.id.newPassIV) {
            editText = bi.newPassET;
            imageView = bi.newPassIV;
        } else {
            editText = bi.confirmPassET;
            imageView = bi.confirmPassIV;
        }
        if (editText.getTransformationMethod() == null) {
            editText.setTransformationMethod(new PasswordTransformationMethod());
            imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_locked));
        } else {
            editText.setTransformationMethod(null);
            imageView.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_unlocked));
        }
    }

    Callbacks.IAlertCallback iAlertCallback = (popupId, isOkClick, text) -> {
        if (popupId == POP_RESET_PASS) {
            finish();
        }
    };
}