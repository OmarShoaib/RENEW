package edu.aku.omarshoaib.renew.global;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.provider.Settings;

import com.google.gson.Gson;

import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.RewordInterceptor;
import dev.b3nedikt.viewpump.ViewPump;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.model.AppInfo;
import edu.aku.omarshoaib.renew.model.Cluster;
import edu.aku.omarshoaib.renew.model.Form1;
import edu.aku.omarshoaib.renew.model.Form2;
import edu.aku.omarshoaib.renew.model.User;

public class MainApp extends Application {

    // APP_SPECIFIC__SYNCED_RECS
    // App Modules
    public static int MODULE_FORM = 1;

    // Static object of models
    public static AppInfo appInfo;
    public static User user;
    public static Cluster selectedCluster;
    public static Form1 form1;
    public static Form2 form2;

    // LockScreen timer and tone
    public static ToneGenerator toneGen;

    // For open form1 onClick
    public static int entryType = 1;

    public static Gson gson;

    // For Local Synced form1 - true when viewing locally saved sync form1
    public static boolean isSynced;

    // For Synced Recs - true when viewing synced recs
    public static boolean isSyncedRecs;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();

        // Prepare Encryption/Decryption Keys
        try {
            ApplicationInfo ai = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            AppConstants.TRATS = bundle.getInt("YEK_TRATS");
            AppConstants.IBAHC = bundle.getString("YEK_REVRES");

            // For dictionary portal
            AppConstants.DP_TRATS = bundle.getInt("DP_YEK_TRATS");
            AppConstants.DP_IBAHC = bundle.getString("DP_YEK_REVRES");

            // For dynamic server key - NIU
            AppConstants.YEK_DATA_KEY = bundle.getString("YEK_DATA_KEY");
            AppConstants.YEK_DATA_INDEX = bundle.getInt("YEK_DATA_INDEX");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Init and Secure DB
        AppDatabase.initAndSecureDB(this);

        // Init Shared Preferences
        SharedPrefs.init(this);

        // Set gallery path in static variable on app start for later use
        AppConstants.GALLERY_DIR = ImageUtils.getGalleryDir(this);

        // Get device Id
        AppConstants.DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get Device Name
        AppConstants.DEVICE_NAME = AppConstants.getDeviceName(getApplicationContext());

        // Init app info
        MainApp.appInfo = AppInfo.initAppInfo(this);

        // Lock screen tone
        toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        // Init Gson
        gson = new Gson();

        // For dynamic string update
        Restring.init(this);
        ViewPump.init(RewordInterceptor.INSTANCE);

        // For Kish Grid Generation dynamically
//      KishGrid.genKishGrid(AppConstants.KG_HOUSEHOLD_COUNT, AppConstants.KG_MAX_ELIGIBLE_COUNT);

    }

}
