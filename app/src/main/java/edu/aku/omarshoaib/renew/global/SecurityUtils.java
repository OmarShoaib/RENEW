package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.aku.omarshoaib.renew.R;

public class SecurityUtils {
    private static final String TAG = "SecurityUtils";

    public static Map<String, Boolean> runRootChecks(Context context) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        try {
            RootBeer rootBeer = new RootBeer(context.getApplicationContext());
            boolean rb = rootBeer.isRooted();
            result.put("RootBeer.isRooted", rb);
            result.put("Build.test-keys", checkTestKeys());
            result.put("su-binary-found", checkForSuBinary());
            result.put("which-su", checkWhichSu());
            result.put("busybox-found", checkForBusybox());
            result.put("magisk-files-found", checkForMagisk());
            result.put("magisk-package-installed", isPackageInstalled(context, "com.topjohnwu.magisk"));
            result.put("dangerous-props", checkDangerousProps());
            result.put("selinux-permissive", isSelinuxPermissive());
            result.put("system-writable", canWriteToSystem());
            result.put("frida-detected", isFridaDetected());
            result.put("xposed-detected", isXposedDetected());
            result.put("method-hooked", isMethodHooked());
        } catch (Throwable t) {
            Log.e(TAG, "runRootChecks exception", t);
            result.put("root-check-exception", true);
        }
        // log for QA (can be removed or gated by build type)
        for (Map.Entry<String, Boolean> e : result.entrySet()) {
            Log.i(TAG, e.getKey() + " = " + e.getValue());

            // Your app should log detection attempts
            /*Log.i("Security", "Root detection check started");
            Log.w("Security", "Root binary detected: /system/xbin/su");
            Log.e("Security", "ROOT DETECTED - blocking application");*/
        }
        return result;
    }

    public static boolean isRootedOrTampered(Context context) {
        for (Boolean v : runRootChecks(context).values()) {
            if (v != null && v) return true;
        }
        return false;
    }

    public static String getRootReason(Context context) {
        Map<String, Boolean> map = runRootChecks(context);
        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            if (e.getValue() != null && e.getValue()) return e.getKey();
        }
        return "no-root-detected";
    }

    public static boolean checkIfDeviceRootedAndExit(Activity activity) {
        if (isRootedOrTampered(activity)) {
            String reason = getRootReason(activity);
            // Reuse your AlertPopup.alert(...) pattern. Replace strings as needed.
            AlertPopup.alert(0, activity,
                    activity.getString(R.string.rooted_device_title),
                    activity.getString(R.string.rooted_device_desc) + "\nReason: " + reason,
                    AppConstants.TYPE_ERROR,
                    activity.getString(R.string.ok),
                    (popupId, isOkClick, obj) -> {
                        activity.finishAffinity();
                        System.exit(0);
                    });
            return true;
        }
        return false;
    }

    public static boolean shouldBlockApp(Activity activity) {
        return isDeviceTampered(activity) || (new RootBeer(activity.getApplicationContext()).isRooted());
    }

    private static boolean isFridaDetected() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("frida") || line.contains("gum-js-loop") || line.contains("re.frida")) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private static boolean isXposedDetected() {
        String[] xposedClasses = {
                "de.robv.android.xposed.XposedBridge",
                "de.robv.android.xposed.XposedHelpers"
        };
        for (String className : xposedClasses) {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException ignored) {
            }
        }
        return false;
    }

    private static boolean isMethodHooked() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            if (element.getClassName().contains("de.robv.android.xposed") ||
                    element.getClassName().toLowerCase().contains("frida")) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkTestKeys() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkForSuBinary() {
        String[] paths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/sbin/su",
                "/system/su",
                "/system/bin/.ext/.su",
                "/system/usr/we-need-root/su-backdoor",
                "/su/bin/su",
                "/vendor/bin/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkWhichSu() {
        Process process = null;
        BufferedReader in = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = in.readLine();
            if (line != null && !line.trim().isEmpty()) return true;
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ignored) {
            }
            if (process != null) process.destroy();
        }
        return false;
    }

    // ---- busybox ----
    private static boolean checkForBusybox() {
        String[] busyPaths = {
                "/system/xbin/busybox",
                "/system/bin/busybox",
                "/sbin/busybox"
        };
        for (String p : busyPaths) {
            if (new File(p).exists()) return true;
        }
        return false;
    }

    private static boolean checkForMagisk() {
        String[] magiskPaths = {
                "/sbin/.magisk",
                "/sbin/.core",
                "/system/bin/magisk",
                "/system/xbin/magisk",
                "/data/adb/magisk",
                "/cache/.magisk"
        };
        for (String p : magiskPaths) {
            if (new File(p).exists()) return true;
        }
        String[] indicators = {
                "/system/xbin/.replace",
                "/data/adb/modules",
                "/data/adb/magisk.db"
        };
        for (String p : indicators) {
            if (new File(p).exists()) return true;
        }
        return false;
    }

    private static boolean isPackageInstalled(Context context, String pkgName) {
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo(pkgName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    private static boolean checkDangerousProps() {
        try {
            String roDebuggable = getProp("ro.debuggable");
            String roSecure = getProp("ro.secure");
            if ("1".equals(roDebuggable)) return true;
            if ("0".equals(roSecure)) return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    private static String getProp(String propName) {
        Process p = null;
        BufferedReader in = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{"getprop", propName});
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = in.readLine();
            return (line != null) ? line : "";
        } catch (Exception e) {
            return "";
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ignored) {
            }
            if (p != null) p.destroy();
        }
    }

    private static boolean isSelinuxPermissive() {
        Process p = null;
        BufferedReader in = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{"getenforce"});
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String enforcement = in.readLine();
            if (enforcement != null && enforcement.equalsIgnoreCase("Permissive")) return true;
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception ignored) {
            }
            if (p != null) p.destroy();
        }
        return false;
    }

    private static boolean canWriteToSystem() {
        try {
            File system = new File("/system");
            if (!system.exists()) return false;
            return system.canWrite();
        } catch (Exception ignored) {
        }
        return false;
    }

    private static boolean isDeviceTampered(Activity activity) {
        if (isFridaDetected() || isXposedDetected() || isMethodHooked()) {
            AlertPopup.alert(0, activity, activity.getString(R.string.tamper_title),
                    activity.getString(R.string.tamper_desc), AppConstants.TYPE_ERROR,
                    activity.getString(R.string.ok), (popupId, isOkClick, obj) -> {
                        activity.finishAffinity();
                        System.exit(0);
                    });
            return true;
        }
        return false;
    }
}