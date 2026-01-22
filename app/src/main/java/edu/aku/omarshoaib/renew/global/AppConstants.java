package edu.aku.omarshoaib.renew.global;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.mateware.snacky.Snacky;
import edu.aku.omarshoaib.renew.R;

public class AppConstants {

    public static final String PROJECT_NAME = "RENEW";
    public static final String DATABASE_NAME = PROJECT_NAME + "_DB";

    // For service api
    public static final String API_NAME = "renew";
    // CRF Id
    public static final String CRF_ID = "1234";
    // For adding 'm2', 'm3' etc for multiple crf strings
    public static final String CRF_PREFIX = "false";

    // For email db
    public static final String SEND_DB_TO_EMAIL = "omar.shoaib@aku.edu";
    public static final String[] SEND_DB_CC_EMAIL = new String[]{"omar.shoaib@aku.edu", "hussain.siddiqui@aku.edu"};

    /**
     * ==============================
     * SWITCHES OF THE APP - START
     * ==============================
     */

    // For toggling between Production and Testing server redirection
    // true = Production Server
    // false = Testing Server
    public static boolean IS_PRODUCTION_SERVER = false;

    // To check as an admin
    // true = Show admin level features
    // false = Hide admin level features
    public static boolean IS_ADMIN = true;
    public static String TEST_USERNAME = "test0002";
    public static String TEST_PASSWORD = "Test0002";

    // For development purposes & for DEVELOPER ONLY
    // Do not share it with anyone
    public static String DEV_USERNAME = "devUser";
    public static String DEV_PASSWORD = "dev@user"; // X5aGsLMUdNsdtBqFm4QTDMG8

    // For visibility of send db option
    // This will always be visible if IS_ADMIN is true
    // true = Show Send DB feature
    // false = Hide Send DB feature
    public static boolean IS_SEND_DB = false;

    // For toggling gps
    // true = GPS ON
    // false = GPS OFF
    public static boolean IS_GPS_ON = false;

    // To enable or disable encryption on web calls
    public static final boolean IS_CALL_ENCRYPTED = true;

    // Max database backup count
    public static int DB_BACKUP_COUNT = 5;

    /**
     * =============================
     * SWITCHES OF THE APP - END
     * ==============================
     */

    // Minimum password length
    public static int MIN_PASS_LENGTH = 8;

    // Maximum number of login attempts
    public static int MAX_LOGIN_ATTEMPT_COUNT = 5;

    public static final String _EMPTY_ = "";

    // Connection timeout in seconds - 60 seconds
    public static int CONNECTION_TIMEOUT = 60;

    // Connection timeout in seconds - 5 minutes
    public static int READ_TIMEOUT = 5 * 60;

    // Connection timeout in seconds - 5 minutes
    public static int WRITE_TIMEOUT = 5 * 60;

    // Screen idle timeout - 15 minutes
    public static long IDLE_TIMEOUT = 15 * 60 * 1000;

    // KishGrid
    public static int[][] KISH_GRID;
    public static int KG_HOUSEHOLD_COUNT = 0;
    public static int KG_MAX_ELIGIBLE_COUNT = 0;

    // Exact length of uid generation
    public static int UID_LENGTH = 19;

    // Entry Logs type
    public static String ADMIN_LOGIN_SUCCESS = "Admin-Login-Success";
    public static String USER_LOGIN_SUCCESS = "User-Login-Success";
    public static String USER_LOGIN_FIRST = "User-Login-First";
    public static String USER_INACTIVE = "User-Inactive";
    public static String LOGIN_FAILED = "Login-Failed-Incorrect-User-Pass";

    // User Roles
    public static String USER_ROLE_DATA_COLLECTOR = "Data Collector";
    public static String USER_ROLE_TEAM_LEADER = "Team Leader";

    // Device Id & Name
    public static String DEVICE_ID;
    public static String DEVICE_NAME;

    // Languages and Country Code
    public static String LOCALE_ENGLISH = "en";
    public static String LOCALE_SINDHI = "sd";

    public static String COUNTRY_CODE_DEFAULT = "PK";

    // Date formats
    public static String APP_DATE_FORMAT = "yyyy-MM-dd";
    public static String APP_TIME_FORMAT = "hh:mm a";
    public static String APP_TIME_24_FORMAT = "HH:mm";
    //    public static String APP_DATE_PICKER_FORMAT = "dd/MM/yyyy";
    public static String APP_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static String SERVER_DATE_TIME_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";
    public static String CUSTOM_SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss.000000";
    public static String DEFAULT_MIN_DATE = "1925-01-01";
    public static String DEFAULT_MIN_YEAR = "1925";

    // Date difference (calculation) types
    public static int DATE_DIFF_IN_DAYS = 1;
    public static int DATE_DIFF_IN_HOURS = 2;
    public static int DATE_DIFF_IN_MINUTES = 3;

    // Response Types - Custom
    public static int RESPONSE_SUCCESS = 1;
    public static int RESPONSE_ERROR = 2;
    // This is used when there's is no record to to upload
    public static int RESPONSE_NOT_PROCESSED = 3;

    // Sync Types
    public static int DOWNLOAD_DATA = 111;
    public static int UPLOAD_DATA = 112;
    public static int UPLOAD_PHOTOS = 113;

    // Status Types
    public static int TYPE_ERROR = 0;
    public static int TYPE_SUCCESS = 1;
    public static int TYPE_WARNING = 2;
    public static int TYPE_INFO = 3;
    public static int TYPE_SB_DEFAULT = 4;

    // Drawable Position values
    public static int POSITION_LEFT = 1;
    public static int POSITION_TOP = 2;
    public static int POSITION_RIGHT = 3;
    public static int POSITION_BOTTOM = 4;

    // Default SnackBar Duration
    public static int MSG_DURATION = 3000;

    // Screen timeout when any code is not scanned
    public static int SCAN_CODE_TIMEOUT = 15 * 1000;

    // Response codes
    public static int NO_CONTENT = 204;
    public static int BAD_REQUEST = 400;
    public static int UNAUTHORIZED = 401;

    // Encryption/Decryption Key
    public static String IBAHC = _EMPTY_;
    public static String DP_IBAHC = _EMPTY_; // For dictionary portal
    // Encryption/Decryption Value
    public static int TRATS = 0;
    public static int DP_TRATS = 0; // For dictionary portal
    //  To get the Encryption/Decryption Key dynamically on server - ENCID Key
    public static String YEK_DATA_KEY = _EMPTY_;
    // Index to put the Data Key inside the encrypted data string.
    // We will put this value at a constant index in encrypted data string.
    // For now it sets to 8th index - Client/Server trust
    public static int YEK_DATA_INDEX = 0;

    public static File GALLERY_DIR;

    // Passing Intent Data Tags
    // To differentiate before, after login download and synced recs
    // 1 = Before Login
    // 2 = After Login
    // 3 = Synced Recs
    public static int IS_LOGIN = 1;

    // Reset All Constants that was set conditionally
    public static void ResetConstants() {
    }

    // Uid generation scheme
    public static String generateUid() {
        // Uid Scheme = 6 characters of device id + current date time in millis
        String deviceIdSS = DEVICE_ID.substring(0, 6);
        long timeInMillis = System.currentTimeMillis();
        return String.format(Locale.ENGLISH, "%s%d", deviceIdSS, timeInMillis);
    }

    // Goto Activity
    public static void gotoActivity(Activity currentActivity, Class<? extends Activity> nextActivity, boolean isFinish) {
        gotoActivity(currentActivity, nextActivity, isFinish, null);
    }

    // Goto Activity
    public static void gotoActivity(Activity currentActivity, Class<? extends Activity> nextActivity, boolean isFinish, Bundle bundle) {
        if (bundle != null)
            currentActivity.startActivity(new Intent(currentActivity, nextActivity).putExtras(bundle));
        else currentActivity.startActivity(new Intent(currentActivity, nextActivity));
        if (isFinish) currentActivity.finish();
    }

    // Init Toolbar
    public static void initToolbar(Activity activity, String title, String subTitle, boolean isBackEnabled) {
        AppCompatActivity appCompatActivity = ((AppCompatActivity) activity);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        appCompatActivity.setSupportActionBar(toolbar);
        assert appCompatActivity.getSupportActionBar() != null;
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleTV = toolbar.findViewById(R.id.titleTV);
        titleTV.setText(title);
        if (!AppConstants.isEmpty(subTitle)) {
            TextView subTitleTV = toolbar.findViewById(R.id.subTitleTV);
            subTitleTV.setText(subTitle);
            subTitleTV.setVisibility(View.VISIBLE);
        }
        // Add back arrow to toolbar
        if (isBackEnabled && appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    // Check Edittext, TextView and String is not empty
    public static boolean isEmpty(Object... objectArr) {
        for (Object object : objectArr) {
            if (object == null)
                return true;
            if (object instanceof EditText) {
                return (((EditText) object).isEnabled() && (((EditText) object).getText() == null
                        || ((EditText) object).getText().toString().trim().isEmpty()));
            } else if (object instanceof CheckBox) {
                return (!((CheckBox) object).isChecked());
            } else if (object instanceof ChipGroup) {
                return (((ChipGroup) object).getChildCount() == 0);
            } else if (object instanceof TextView) {
                return (((TextView) object).getText() == null
                        || ((TextView) object).getText().toString().isEmpty());
            } else if (object instanceof RadioGroup) {
                return (((RadioGroup) object).getCheckedRadioButtonId() == -1);
            } else if (object instanceof Spinner) {
                return (((Spinner) object).getSelectedItemPosition() == -1
                        || ((Spinner) object).getSelectedItemPosition() == 0);
            } else if (object instanceof List)
                return ((List<?>) object).isEmpty();
            else if (object.getClass().isArray())
                return Array.getLength(object) == 0;
            else if (object instanceof Map)
                return ((Map<?, ?>) object).isEmpty();
            else if (object instanceof Collection)
                return ((Collection<?>) object).isEmpty();
            else if (object instanceof String) {
                return (object.equals(_EMPTY_));
            }
        }
        return false;
    }

    // Parse string to int and return 0(Zero) if string is empty
    public static int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        else return Integer.parseInt(value);
    }

    // Check if sum of all numeric fields are 0(Zero)
    public static boolean validateSumNotZero(List<String> values) {
        boolean anyNotEmpty = false;
        int sum = 0;

        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                anyNotEmpty = true;
                sum += Integer.parseInt(value.trim());
            }
        }
        return anyNotEmpty && sum == 0;
    }

    // Convert simple text to rich text i.e. parse <b><i> tags etc. inside string
    @SuppressWarnings("deprecation")
    public static Spanned getRichText(String simpleText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(simpleText, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(simpleText);
        }
    }

    // Scroll to focused view
    public static void focusOnView(ScrollView sv, View view) {
        sv.post(() -> sv.smoothScrollTo(0, view.getBottom()));
    }

    // Check if method is defined on activity then invoke/call method
    public static Object callMethod(Activity activity, String methodName) {
        return callMethod(activity, null, null, methodName);
    }

    public static Object callMethod(Activity activity, View view, String methodName) {
        return callMethod(activity, view, null, methodName);
    }

    public static Object callMethod(Activity activity, String param, String methodName) {
        return callMethod(activity, null, param, methodName);
    }

    public static Object callMethod(Activity activity, View view, String param, String methodName) {
        Method method = null;
        try {
            for (Method method1 : activity.getClass().getDeclaredMethods()) {
                if (method1.getName().equals(methodName)) {
                    method = method1;
                    break;
                }
            }
            if (method != null) {
                return view != null ? method.invoke(activity, view) : !AppConstants.isEmpty(param) ?
                        method.invoke(activity, param) : method.invoke(activity);
            } else
                AppConstants.showSimpleSnackBar(activity, activity.getString(R.string.function_not_defined),
                        AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Calling function in object class from activity
    public static Object callMethodObj(Activity activity, Object instance, String methodName) {
        return callMethodObj(activity, instance, null, methodName);
    }

    public static Object callMethodObj(Activity activity, Object instance, String param, String methodName) {
        Method method = null;
        try {
            Class<?> targetClass = instance.getClass();

            for (Method method1 : targetClass.getDeclaredMethods()) {
                if (method1.getName().equals(methodName)) {
                    method = method1;
                    break;
                }
            }
            if (method != null) {
                return !AppConstants.isEmpty(param) ? method.invoke(instance, param) : method.invoke(instance);
            } else
                AppConstants.showSimpleSnackBar(activity, activity.getString(R.string.function_not_defined),
                        AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    // For loading json from assets folder
    public static String loadJsonFromAssets(Activity activity, String fileName) {
        try (InputStream inputStream = activity.getAssets().open(fileName)) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*// Disable single radio group
    public static void disableViews(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++)
            radioGroup.getChildAt(i).setEnabled(false);
    }*/

    /*// Enable single radio group
    public static void enableViews(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++)
            radioGroup.getChildAt(i).setEnabled(true);
    }*/

    /*// Disable list of radio groups
    public static void disableViews(List<View> views) {
        for (View view : views)
            if (view instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) view;
                for (int i = 0; i < radioGroup.getChildCount(); i++)
                    radioGroup.getChildAt(i).setEnabled(false);
            } else if (view instanceof Spinner) {
                view.setEnabled(false);
                ((Spinner) view).setSelection(0);
            } else {
                view.setEnabled(false);
            }
    }

    // Enable list of radio groups
    public static void enableViews(List<View> views) {
        for (View view : views)
            if (view instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) view;
                for (int i = 0; i < radioGroup.getChildCount(); i++)
                    radioGroup.getChildAt(i).setEnabled(true);
            } else if (view instanceof Spinner) {
                view.setEnabled(true);
                ((Spinner) view).setSelection(0);
            } else {
                view.setEnabled(true);
            }
    }

    // Disable all views in a specified linear layout
    public static void disableViews(LinearLayout parentLayout) {
        for (int i = 0; i < parentLayout.getChildCount(); i++)
            parentLayout.getChildAt(i).setEnabled(false);
    }

    // Enable all views in a specified linear layout
    public static void enableViews(LinearLayout parentLayout) {
        for (int i = 0; i < parentLayout.getChildCount(); i++)
            parentLayout.getChildAt(i).setEnabled(true);
    }*/

    // Disable all views
    public static void disableViews(Activity activity, List<View> views, List<String> preventDisableViews) {
        viewsEnabled(activity, views, preventDisableViews, false);
    }

    // Disable all views
    public static void disableViews(Activity activity, List<View> views) {
        viewsEnabled(activity, views, null, false);
    }

    // Disable all views
    public static void disableViews(Activity activity, View view, List<String> preventDisableViews) {
        viewsEnabled(activity, Collections.singletonList(view), preventDisableViews, false);
    }

    // Disable all views
    public static void disableViews(Activity activity, View view) {
        viewsEnabled(activity, Collections.singletonList(view), null, false);
    }

    // Disable all views
    public static void disableViews(Activity activity, View view, String preventDisableViews) {
        viewsEnabled(activity, Collections.singletonList(view), Collections.singletonList(preventDisableViews), false);
    }

    // Enable all views
    public static void enableViews(Activity activity, List<View> views, List<String> preventEnableViews) {
        viewsEnabled(activity, views, preventEnableViews, true);
    }

    // Enable all views
    public static void enableViews(Activity activity, List<View> views) {
        viewsEnabled(activity, views, null, true);
    }

    // Enable all views
    public static void enableViews(Activity activity, View view, List<String> preventEnableViews) {
        viewsEnabled(activity, Collections.singletonList(view), preventEnableViews, true);
    }

    // Enable all views
    public static void enableViews(Activity activity, View view) {
        viewsEnabled(activity, Collections.singletonList(view), null, true);
    }

    // Disable all views
    public static void enableViews(Activity activity, View view, String preventEnableViews) {
        viewsEnabled(activity, Collections.singletonList(view), Collections.singletonList(preventEnableViews), true);
    }

    // To enable/disable views in any view group
    // preventiveViews = Views that have no effect on the condition i.e. true/false even in view mode
    private static void viewsEnabled(Activity activity, List<View> views, List<String> preventiveViews, boolean isEnabled) {
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            // Getting view name
            // If view name is btnContinue then we won't disable it
            String viewName = getViewNameByView(activity, view);

            if (isEmpty(viewName))  // If view id is empty
                view.setEnabled(isEnabled);
            else if (!isEnabled) {    // For Disable
                if (preventiveViews == null || !preventiveViews.contains(viewName))
                    if (!(view instanceof ViewGroup))
                        view.setEnabled(false);
            } else    // For Enable
                view.setEnabled(preventiveViews == null || !preventiveViews.contains(viewName));

            // For spinners
            if (view instanceof Spinner) {
                view.setClickable(isEnabled);
                view.setEnabled(isEnabled);
            } else if (view instanceof EditText) {
                EditText et = ((EditText) view);
                if (isEnabled)
                    et.setTextColor(ContextCompat.getColor(activity, R.color.text_color));
                else et.setTextColor(ContextCompat.getColor(activity, R.color.red_color));
            } else if (view instanceof RadioButton) {
                RadioButton rb = ((RadioButton) view);
                boolean isDontKnowView = rb.getCurrentTextColor() == ContextCompat.getColor(activity, R.color.red_color);
                int textColor;
                if (rb.isEnabled())
                    textColor = ContextCompat.getColor(activity, R.color.text_color);
                else
                    textColor = (rb.isChecked() && !rb.isEnabled()) || (isDontKnowView && rb.isEnabled()) ?
                            ContextCompat.getColor(activity, R.color.red_color) :
                            !rb.isEnabled() ? ContextCompat.getColor(activity, R.color.disabled_text_color) :
                                    ContextCompat.getColor(activity, R.color.text_color);
                ((RadioButton) view).setTextColor(textColor);
            } else if (view instanceof CheckBox) {
                CheckBox cb = ((CheckBox) view);
                int textColor;
                boolean isDontKnowView = cb.getCurrentTextColor() == ContextCompat.getColor(activity, R.color.red_color);
                if (isEnabled) textColor = ContextCompat.getColor(activity, R.color.text_color);
                else
                    textColor = (cb.isChecked() && !cb.isEnabled()) || (isDontKnowView && cb.isEnabled()) ?
                            ContextCompat.getColor(activity, R.color.red_color) :
                            !cb.isEnabled() ? ContextCompat.getColor(activity, R.color.disabled_text_color) :
                                    ContextCompat.getColor(activity, R.color.text_color);
                ((CheckBox) view).setTextColor(textColor);
            }

            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;

                for (int j = 0; j < group.getChildCount(); j++) {
                    if (isEnabled)
                        enableViews(activity, Collections.singletonList(group.getChildAt(j)), preventiveViews);
                    else
                        disableViews(activity, Collections.singletonList(group.getChildAt(j)), preventiveViews);
                }
            }
        }
    }

    // For RadioGroup Clear Checks
    public static void rgClearChecks(RadioGroup radioGroup) {
        rgClearChecks(Collections.singletonList(radioGroup));
    }

    public static void rgClearChecks(List<RadioGroup> radioGroups) {
        for (int i = 0; i < radioGroups.size(); i++) {
            radioGroups.get(i).clearCheck();
        }
    }

    // Get view name by view id
    public static String getViewNameByView(Activity activity, View view) {
//        String fullName = activity.getResources().getResourceName(viewId);
        try {
            return (view.getId() == View.NO_ID) ? _EMPTY_ :
                    activity.getResources().getResourceName(view.getId()).split(":id/")[1];
        } catch (Exception e) {
            return !AppConstants.isEmpty(view.getTag()) ? (String) view.getTag() : _EMPTY_;
        }
    }

    // Get view name by view id
    public static String getViewNameById(Activity activity, int viewId) {
        try {
            return (viewId == View.NO_ID) ? _EMPTY_ :
                    activity.getResources().getResourceName(viewId).split(":id/")[1];
        } catch (Exception e) {
            return _EMPTY_;
        }
    }

    // Get view by view id as String
    public static View getViewByName(Activity activity, String viewName) {
        int id = activity.getResources().getIdentifier(viewName, "id", activity.getPackageName());
        return activity.findViewById(id);
    }

    // Get view by view id
    public static View getViewById(Activity activity, int viewId) {
        return activity.findViewById(viewId);
    }

    // Get string by id i.e. id will be passed in string
    public static String getStringByName(Activity activity, String id) {
        int resourceId = activity.getResources().getIdentifier(id, "string", activity.getPackageName());
        try {
            return activity.getString(resourceId);
        } catch (Exception e) {
            return _EMPTY_;
        }
    }

    // Check if ANY VALUE of views inside view group is FILLED.
    // If any one value of any view is filled then it will be considered as filled
    public static void isAnyViewFilled(View view) {
        if (view.getVisibility() == View.VISIBLE
                && ((view instanceof EditText || view instanceof CheckBox
                || view instanceof RadioGroup || view instanceof Spinner || view instanceof ChipGroup)
                && !AppConstants.isEmpty(view)) || (view instanceof RecyclerView
                || view instanceof ListView)) {
            throw new RuntimeException("true");
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View innerView = group.getChildAt(i);
                isAnyViewFilled(innerView);
            }
        }
//        return false;
    }

    // Check if ALL VALUES of views inside view group are FILLED.
    // If all values of view group are filled then it will be considered as filled
    public static void isAllLayoutFilled(View view) {
        if (view.getVisibility() == View.VISIBLE
                && (view instanceof EditText || view instanceof CheckBox
                || view instanceof RadioGroup || view instanceof Spinner)
                && AppConstants.isEmpty(view)) {
            throw new RuntimeException("false");
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View innerView = group.getChildAt(i);
                isAllLayoutFilled(innerView);
            }
        }
//        return false;
    }

    // Show customized simple SnackBar
    public static void showSimpleSnackBar(Activity activity, String message, int type) {
        showSimpleSnackBar(activity, message, MSG_DURATION, type);
    }

    public static void showSimpleSnackBar(Activity activity, String message, int duration, int type) {
        Snacky.Builder snackBar = Snacky.builder();
        snackBar.setActivity(activity);
        snackBar.setText(message);
        snackBar.setDuration(duration);
        snackBar.setView(activity.findViewById(android.R.id.content));
        snackBar.setTextTypeface(ResourcesCompat.getFont(activity, R.font.roboto_regular));
        snackBar.setTextTypefaceStyle(Typeface.BOLD);

        if (type == TYPE_ERROR) {
            snackBar.error().show();
        } else if (type == TYPE_SUCCESS) {
            snackBar.success().show();
        } else if (type == TYPE_INFO) {
            snackBar.info().show();
        } else if (type == TYPE_WARNING) {
            snackBar.warning().show();
        } else {
            // Default
            snackBar.build().show();
        }
    }

    // Check if json is valid JSONArray
    public static boolean isJSONArrayValid(String json) {
        try {
            new Gson().fromJson(json, Object[].class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    // Check if json is valid JSONObject
    public static boolean isJSONObjectValid(String json) {
        try {
            new Gson().fromJson(json, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    // Change Drawable Color
    public static void setDrawableColor(Activity activity, Drawable drawable, int color) {
        assert drawable != null;
        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.MULTIPLY));
    }

    // Show Keyboard
    public static void showSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    // Hide Keyboard
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Change Drawable Color
    public static void setDrawable(Activity activity, Object object, Drawable drawable, int color, int position) {
        assert drawable != null;
        drawable.setColorFilter(new
                PorterDuffColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.MULTIPLY));
        if (object instanceof EditText) {
            if (position == POSITION_LEFT) {
                // Left
                ((EditText) object).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            } else if (position == POSITION_TOP) {
                // Top
                ((EditText) object).setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            } else if (position == POSITION_RIGHT) {
                // Right
                ((EditText) object).setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                // Bottom
                ((EditText) object).setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
            }
        } else if (object instanceof TextView) {
            if (position == POSITION_LEFT) {
                // Left
                ((TextView) object).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            } else if (position == POSITION_TOP) {
                // Top
                ((TextView) object).setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            } else if (position == POSITION_RIGHT) {
                // Right
                ((TextView) object).setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } else {
                // Bottom
                ((TextView) object).setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
            }
        }
    }

    /*For Activity Screen Lock*/
   /* public static void lockScreen(Activity activity) {
        if (MainApp.timer != null) MainApp.timer.cancel();
        MainApp.timer = new CountDownTimer(15 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if ((millisUntilFinished / 1000) < 14) {
                    MainApp.toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                }
            }

            public void onFinish() {
                gotoActivity(activity, LockAC.class, false);
                MainApp.timer.cancel();
            }
        };
        MainApp.timer.start();
    }*/

    /*For Double Back Pressed*/
    private static boolean isDoubleBackPressed = false;

    public static void checkDoubleBackPress(Activity activity, Class<? extends Activity> nextActivity) {
        if (isDoubleBackPressed) {
            if (nextActivity != null)
                gotoActivity(activity, nextActivity, true);
            activity.finish();
            return;
        }
        isDoubleBackPressed = true;
        showSimpleSnackBar(activity, activity.getString(R.string.double_back_press), MSG_DURATION, TYPE_INFO);
        new Handler().postDelayed(() -> isDoubleBackPressed = false, 2000);
    }

    /*For Double Cancel Pressed*/
    public static void checkDoubleCancelPress(Activity activity, Class<? extends Activity> nextActivity) {
        checkDoubleCancelPress(activity, nextActivity, null);
    }

    public static void checkDoubleCancelPress(Activity activity, Class<? extends Activity> nextActivity, String message) {
        if (isDoubleBackPressed) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("complete", false);
            if (nextActivity != null)
                gotoActivity(activity, nextActivity, true, bundle);
            activity.finish();
            return;
        }
        isDoubleBackPressed = true;
        showSimpleSnackBar(activity, AppConstants.isEmpty(message) ? activity.getString(R.string.double_cancel_press) : message, MSG_DURATION, TYPE_INFO);
        new Handler().postDelayed(() -> isDoubleBackPressed = false, 2000);
    }

    // Check if device is rooted
    public static boolean checkIfDeviceRooted(Activity activity) {
        RootBeer rootBeer = new RootBeer(activity);
        if (rootBeer.isRooted()) {
            AlertPopup.alert(0, activity, activity.getString(R.string.rooted_device_title),
                    activity.getString(R.string.rooted_device_desc), AppConstants.TYPE_ERROR,
                    activity.getString(R.string.ok), (popupId, isOkClick, obj) -> {
                        activity.finish();
                        System.exit(0);
                    });
            return true;
        }
        return false;
    }

    // Toggle view visibility with time
    public static void toggleViewVisibilityTimer(View view, int duration, int visibilityAfter) {
        view.setVisibility(visibilityAfter == View.VISIBLE ? View.GONE : View.VISIBLE);
        view.postDelayed(() -> view.setVisibility(visibilityAfter == View.VISIBLE ? View.VISIBLE : View.GONE), duration);
    }

    // Clear app cache by deleting cache folder
    public static void deleteAppCache(Activity activity) {
        try {
            File dir = activity.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete all folders inside the directory
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success)
                        return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    // Delete files with specified extension from a folder
    public static void deleteFiles(String dirPath, String ext) {
        File dir = new File(dirPath);
        //Checking the directory exists
        if (!dir.exists())
            return;
        //Getting the list of all the files in the specific directory
        File[] fList = dir.listFiles();
        if (fList != null) {
            for (File f : fList) {
                //checking the extension of the file with endsWith method.
                if (f.getName().endsWith(ext))
                    f.delete();
            }
        }
    }

    // Restart Application
    public static void restartApp(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(activity.getPackageName());
        assert intent != null;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        activity.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    // Copy File
    public static void copyFile(File sourceFile, File destFile) {
        try (FileChannel source = new FileInputStream(sourceFile).getChannel();
             FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to insert string at specific index
    public static String insertStringAtIndex(String originalString, String stringToBeInserted, int index) {
        // Create a new string
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < originalString.length(); i++) {
            // Insert the original string character
            // into the new string
            newString.append(originalString.charAt(i));
            if (i == index - 1) {
                // Insert the string to be inserted
                // into the new string
                newString.append(stringToBeInserted);
            }
        }
        // return the modified String
        return newString.toString();
    }

    // Capitalize first word of the field to match for setters name
    // like syncDate(field name) to setSyncDate (Sync's 'S' is capitalized)
    public static String capitalizeFirstChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // Get Json as dynamic object type
    public static <T> T getObjectFromJson(String json, Class<T> clazz) {
        return MainApp.gson.fromJson(json, clazz);
    }

    // Convert cursor to list of json objects
    public static List<JSONObject> cursorToJsonObjects(Cursor cursor) {
        List<JSONObject> jObjects = new ArrayList<>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();

                    // Iterate through cursor columns
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String columnName = cursor.getColumnName(i);
                        int columnType = cursor.getType(i);

                        try {
                            // Handle different column types accordingly
                            switch (columnType) {
                                case Cursor.FIELD_TYPE_NULL:
                                    jsonObject.put(columnName, null);
                                    break;
                                case Cursor.FIELD_TYPE_INTEGER:
                                    jsonObject.put(columnName, cursor.getLong(i));
                                    break;
                                case Cursor.FIELD_TYPE_FLOAT:
                                    jsonObject.put(columnName, cursor.getDouble(i));
                                    break;
                                case Cursor.FIELD_TYPE_STRING:
                                    jsonObject.put(columnName, cursor.getString(i));
                                    break;
                                case Cursor.FIELD_TYPE_BLOB:
                                    // Handle BLOB data accordingly
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    jObjects.add(jsonObject);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return jObjects;
    }

    // Get device name
    public static String getDeviceName(Context context) {
        String deviceName = Settings.Global.getString(context.getContentResolver(), "device_name");
        return deviceName != null ? deviceName : android.os.Build.MODEL; // Fallback to model name
    }

    // Check if the Service is already started/running
    public static boolean isServiceRunning(Activity activity, String serviceClassName) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClassName.equals(service.service.getClassName())) {
                    return true; // Service is running
                }
            }
        }
        return false; // Service is not running
    }

    // Open web link
    public static void openWebLink(Activity activity, String link) {
        // Create an Intent with ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));

        // Verify that there is an app to handle this intent
        activity.startActivity(intent);
    }

    // Slide Up Animation
    public static void slideUp(ViewGroup layout) {
        // Initially position the layout below the screen
        layout.setVisibility(View.VISIBLE);
        new Handler().post(() -> {
            layout.setTranslationY(layout.getHeight());
            // Slide the layout into view
            ObjectAnimator animator = ObjectAnimator.ofFloat(layout, "translationY", layout.getHeight(), 0);
            animator.setDuration(300);  // Duration of the slide-in
            animator.start();
        });
    }

    // Slide Down Animation
    public static void slideDown(ViewGroup layout) {
        // Slide the layout out of view
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout, "translationY", 0, layout.getHeight());
        animator.setDuration(300);  // Duration of the slide-out
        animator.start();
        toggleViewVisibilityTimer(layout, 300, View.GONE);
    }

    // Function to get the key by value from a HashMap
    public static <K, V> K getHMKeyByValue(HashMap<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; // Return null if no matching key is found
    }

}