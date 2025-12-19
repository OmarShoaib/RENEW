package edu.aku.omarshoaib.renew.model;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Date;
import java.util.Locale;

import edu.aku.omarshoaib.renew.BuildConfig;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SharedPrefs;

public class AppInfo {
    public static String NAME = "versionApp";

    private long installedOn;
    private String versionName;
    private int versionCode;
    private String deviceId;
    private String appVersion;
    private String sysDate;
    private String outputFile;

    public static AppInfo initAppInfo(Context context) {
        try {
            AppInfo appInfo = new AppInfo();
            String packageName = context.getPackageName();
            PackageInfo pm = context.getPackageManager().getPackageInfo(packageName, 0);

            appInfo.installedOn = pm.lastUpdateTime;
            appInfo.versionName = pm.versionName;
            appInfo.versionCode = pm.versionCode;
            appInfo.deviceId = AppConstants.DEVICE_ID;
            appInfo.appVersion = String.format(Locale.ENGLISH, "%s.%d", appInfo.versionName, appInfo.versionCode);
            appInfo.sysDate = DateUtils.getCurrentDateTime();

            return appInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getAppVersion() {
        return appVersion;
    }

    // For getting formatted version name and code
    public static String getAppInfo() {
        return String.format(Locale.ENGLISH, "Ver. %s (Last Updated: %s)",
                MainApp.appInfo.appVersion, DateUtils.getFormattedDateTime(
                        new Date(MainApp.appInfo.installedOn), AppConstants.APP_DATE_FORMAT));
    }

    // Setting updated app version
    public static void setUpdatedAppInfo(AppInfo appInfo) {
        SharedPrefs.write(SharedPrefs.APP_VERSION_NAME, appInfo.getVersionName());
        SharedPrefs.write(SharedPrefs.APP_VERSION_CODE, appInfo.getVersionCode());
    }

    // Getting updated app version from shared preferences
    public static String getUpdatedAppInfo(Activity activity) {
        String updatedVersionName = SharedPrefs.read(SharedPrefs.APP_VERSION_NAME, BuildConfig.VERSION_NAME);
        int updatedVersionCode = SharedPrefs.read(SharedPrefs.APP_VERSION_CODE, BuildConfig.VERSION_CODE);

        return String.format(Locale.ENGLISH, activity.getString(R.string.updated_version),
                updatedVersionName, updatedVersionCode);
    }

    // Check if app version is up to date
    public static boolean isAppUpdated() {
        String updatedVersionName = SharedPrefs.read(SharedPrefs.APP_VERSION_NAME, BuildConfig.VERSION_NAME);
        int updatedVersionCode = SharedPrefs.read(SharedPrefs.APP_VERSION_CODE, BuildConfig.VERSION_CODE);

        String currentVersionName = BuildConfig.VERSION_NAME;
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // If false then the app is NOT up to date
        // If true then the app is up to date
        return currentVersionCode >= updatedVersionCode;
    }

}
