package edu.aku.omarshoaib.renew.activity;

import static edu.aku.omarshoaib.renew.global.AppConstants.IS_CALL_ENCRYPTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivityLoginBinding;
import edu.aku.omarshoaib.renew.global.AlertPopup;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.ConnectionDetector;
import edu.aku.omarshoaib.renew.global.LocaleManager;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SharedPrefs;
import edu.aku.omarshoaib.renew.model.AppInfo;
import edu.aku.omarshoaib.renew.model.EntryLog;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.model.User;
import edu.aku.omarshoaib.renew.webcall.UploadData;
import edu.aku.omarshoaib.renew.webcall.web_client.CryptoUtil;
import edu.aku.omarshoaib.renew.webcall.web_client.WebAPI;
import edu.aku.omarshoaib.renew.webcall.web_client.WebCall;
import edu.aku.omarshoaib.renew.webcall.web_client.WebClient;

public class LoginAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = LoginAC.this;
    private String language;

    private EditText username, password;

    private ActivityLoginBinding bi;
    private AppDatabase appDatabase;

    private int attemptCounter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // By default language selection
        language = SharedPrefs.read(SharedPrefs.LOCALE, AppConstants.LOCALE_ENGLISH);
        LocaleManager.setLocale(activity, language);

        bi = DataBindingUtil.setContentView(activity, R.layout.activity_login);
        bi.setCallback(this);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    // For setting initial UI
    private void initUI() {
        UploadData.initUploadList();
        // Current app version
        bi.installDateTV.setText(AppInfo.getAppInfo());

        // Set language text
        bi.languageTV.setText(LocaleManager.getValue(activity, language));

        username = bi.usernameTextInput.getDefaultET();
        password = bi.passwordTextInput.getDefaultET();

        // For Admin
        if (AppConstants.IS_ADMIN) {
            username.setText(AppConstants.TEST_USERNAME);
            password.setText(AppConstants.TEST_PASSWORD);
            // Testing app banner
//            bi.testingAppTV.setVisibility(View.VISIBLE);
        }

        // Testing app banner
        bi.testingAppTV.setVisibility(MainApp.appInfo.getAppVersion().contains("D") ? View.VISIBLE : View.GONE);

        // Show/Hide password
        bi.passwordTextInput.getDefaultTI().setStartIconOnClickListener(view -> {
            if (password.getTransformationMethod() == null) {
                password.setTransformationMethod(new PasswordTransformationMethod());
                bi.passwordTextInput.getDefaultTI().setStartIconDrawable(ContextCompat.getDrawable(LoginAC.this, R.drawable.ic_locked));
            } else {
                password.setTransformationMethod(null);
                bi.passwordTextInput.getDefaultTI().setStartIconDrawable(ContextCompat.getDrawable(LoginAC.this, R.drawable.ic_unlocked));
            }
        });

        // Current app version
        bi.installDateTV.setText(AppInfo.getAppInfo());

        // Checking the updated app version
        if (!new ConnectionDetector(activity).hasInternetConnection()) {
            // If version is not updated then show a message
            if (!AppInfo.isAppUpdated()) {
                bi.updateVersionTV.setText(AppInfo.getUpdatedAppInfo(activity));
                bi.updateVersionTV.setVisibility(View.VISIBLE);
            }
        } else // Get app version from server automatically if network is available
            getAppVersion();
    }

    // User Login
    public void login(View view) {
        if (attemptCounter < AppConstants.MAX_LOGIN_ATTEMPT_COUNT) {
            String _username = username.getText().toString().trim();
            String _password = password.getText().toString();

            // Username cannot be empty
            if (AppConstants.isEmpty(_username)) {
                username.setError(getString(R.string.username_required));
                return;
            }

            // Username and Password cannot be same
            if (_username.equals(_password)) {
                password.setError(getString(R.string.username_pass_same));
                return;
            }

            // Check for a valid password, if the user entered one.
            if (_password.length() < AppConstants.MIN_PASS_LENGTH) {
                password.setError(String.format(getString(R.string.invalid_password),
                        AppConstants.MIN_PASS_LENGTH));
                return;
            }

            // Check login credentials
            if (appDatabase.userDao().doLogin(_username, _password)) {
                // Login Success
                if (AppConstants.IS_ADMIN) {
                    // Login as admin
                    EntryLog.initEntryLog(appDatabase, AppConstants.ADMIN_LOGIN_SUCCESS, _username);
                    AppConstants.gotoActivity(activity, MainActivity.class, false);
                } else {
                    if (MainApp.user.getNewUser().equals("1")) {
                        // Login as user for the first time
                        EntryLog.initEntryLog(appDatabase, AppConstants.USER_LOGIN_FIRST, _username);
                        AppConstants.gotoActivity(activity, ChangePassAC.class, false);
                    } else {
                        // Login as user - normal
                        EntryLog.initEntryLog(appDatabase, AppConstants.USER_LOGIN_SUCCESS, _username);
                        AppConstants.gotoActivity(activity, MainActivity.class, false);
                    }
                }
                attemptCounter = 0;
            } else {
                // Login Failed
                EntryLog.initEntryLog(appDatabase, AppConstants.LOGIN_FAILED, _username);
                AlertPopup.alert(activity, getString(R.string.login_failed),
                        String.format(getString(R.string.attempt_no),
                                ++attemptCounter, getString(R.string.incorrect_username_or_password)),
                        AppConstants.TYPE_ERROR);
            }
        } else {
            // Login attempts exceed
            AlertPopup.alert(activity, getString(R.string.account_locked),
                    getString(R.string.login_attempts_exceeded), AppConstants.TYPE_ERROR);
        }
    }

    // Change Language
    public void changeLanguage(View view) {
        PopupMenu langMenu = new PopupMenu(activity, view);
        HashMap<String, String> localeMap = LocaleManager.getLocaleList(activity);
        List<String> localeList = new ArrayList<>(localeMap.values());
        for (int i = 0; i < localeList.size(); i++) {
            langMenu.getMenu().add(localeList.get(i));
        }
        langMenu.setOnMenuItemClickListener(menuItem -> {
            LocaleManager.changeLocale(activity, Objects.requireNonNull(menuItem.getTitle()).toString());
            return true;
        });
        langMenu.show();
    }

    // Goto sync activity
    public void gotoSyncActivity(View view) {
        // IS_LOGIN = To differentiate before and after login download
        // For before login sync data download
        AppConstants.IS_LOGIN = 1;
        AppConstants.gotoActivity(activity, SyncAC.class, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // For enabling locked layout when data is not downloaded
        List<User> userList = appDatabase.userDao().getAllData();
        if (userList != null && !userList.isEmpty())
            bi.lockedLayout.setVisibility(View.GONE);
    }

    /* FOR GETTING APP VERSION AUTOMATICALLY IF NETWORK IS AVAILABLE */

    private void getAppVersion() {
        WebAPI webAPI = WebClient.getInstance(activity).getWebAPI();
        WebCall webCall = new WebCall(activity, iWebCallback);
        SyncModel appInfo = new SyncModel(AppInfo.NAME, " * ", " (colflag is null or colflag = 0) ", "");
        appInfo.setFolder(WebAPI.VERSION_OUTPUT_JSON_FILE_PATH);
        webCall.call(webAPI.downloadEncData(CryptoUtil.encrypt(MainApp.gson.toJson(appInfo))), AppConstants.DOWNLOAD_DATA, AppInfo.NAME, 0, 0, IS_CALL_ENCRYPTED);
    }

    private final WebCall.IWebCallback iWebCallback = new WebCall.IWebCallback() {
        @Override
        public void onSuccess(String tag, String jsonResponse, int index, int total, List<String> list) {
            // App version call download the content of output-metadata.json file of apk placed on server
            try {
                JSONObject appInfoJson = new JSONObject(jsonResponse);
                JSONObject appVersionJson = appInfoJson.getJSONArray("elements").getJSONObject(0);
                AppInfo appInfo = new AppInfo();
                appInfo.setVersionName(appVersionJson.getString("versionName"));
                appInfo.setVersionCode(appVersionJson.getInt("versionCode"));

                // Setting app version
                AppInfo.setUpdatedAppInfo(appInfo);

                // Checking the updated app version
                // If version is not updated then show a message
                if (!AppInfo.isAppUpdated()) {
                    bi.updateVersionTV.setText(AppInfo.getUpdatedAppInfo(activity));
                    bi.updateVersionTV.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(String tag, String errorMessage, int index, int total, List<String> list) {

        }
    };
}