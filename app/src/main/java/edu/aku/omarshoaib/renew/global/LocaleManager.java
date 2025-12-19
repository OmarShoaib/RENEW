package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.HashMap;
import java.util.Locale;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.LoginAC;

public class LocaleManager {

    public static void changeLocale(Activity activity, String language) {
        String localeKey = getKey(getLocaleList(activity), language);
        assert localeKey != null;
        if (localeKey.equals(SharedPrefs.read(SharedPrefs.LOCALE, "")))
            return;
        setLocale(activity, localeKey);
        activity.startActivity(new Intent(activity, LoginAC.class).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // Change Locale/Language
    public static void setLocale(Context context, String localeKey) {
        if (localeKey.equals(getCurrentLocale(context)))
            return;
        SharedPrefs.write(SharedPrefs.LOCALE, localeKey);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale = new Locale(localeKey, AppConstants.COUNTRY_CODE_DEFAULT);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(localeKey + "-" + locale.getCountry());
            // Call this on the main thread as it may require Activity.restart()
            AppCompatDelegate.setApplicationLocales(appLocale);
//            activity.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//            activity.createConfigurationContext(configuration);
//        } else {
        resources.updateConfiguration(configuration, displayMetrics);
//        }
    }

    // Get Current Locale
    private static String getCurrentLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getLanguage();
    }

    // Get Locale/Language List
    public static HashMap<String, String> getLocaleList(Activity activity) {
        HashMap<String, String> localeMap = new HashMap<>();
        localeMap.put(AppConstants.LOCALE_ENGLISH, activity.getString(R.string.lang_english));
        localeMap.put(AppConstants.LOCALE_SINDHI, activity.getString(R.string.lang_sindhi));
        return localeMap;
    }

    // Get HashMap key from Hashmap specific value - String
    private static String getKey(HashMap<String, String> map, String value) {
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Check the direction of language
    private static boolean isLocaleLTR(Activity activity) {
        Configuration config = activity.getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;
    }

    // Get HashMap Value from Hashmap specific key - String
    public static String getValue(Activity activity, String code) {
        for (HashMap.Entry<String, String> entry : getLocaleList(activity).entrySet()) {
            if (code.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

}
