package edu.aku.omarshoaib.renew.global;

/*
 * ============================================================================
 *  LOCAL-ONLY VERSION (no Google Cloud, no Play Integrity, no backend, no new
 *  Gradle dependencies).
 *
 *  Every change vs. your ORIGINAL file is wrapped like this:
 *      // >>> CHANGE n: description
 *      ...
 *      // <<< END CHANGE n
 *  Search for ">>> CHANGE" to jump between them.
 *
 *  Call from a background thread if you can (the key-attestation check
 *  generates a Keystore key, which can take a few hundred ms), e.g. on your
 *  splash screen, then show the popup on the UI thread.
 * ============================================================================
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
// >>> CHANGE 1: extra imports (all part of the Android/Java SDK)
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
// <<< END CHANGE 1

import edu.aku.omarshoaib.renew.R;

public class SecurityUtils {
    private static final String TAG = "SecurityUtils";

    // >>> CHANGE 2: scoring. Reliable ("hard") signals block immediately.
    // Noisy ("soft") signals only block when 2 or more fire together, because
    // each of them can false-positive on some legit devices/OEM ROMs.
    private static final Set<String> SOFT_SIGNALS = new HashSet<>(Arrays.asList(
            "bootloader-unlocked",
            "verified-boot-not-green",
            "mount-namespace-anomaly",
            "mount-id-gap",
            "maps-anomaly",
            "sbin-symlink-anomaly",
            "prop-source-mismatch",
            "root-check-exception"
    ));
    private static final int SOFT_SIGNAL_LIMIT = 2;

    // Mount-id gap heuristic (see checkMountIdGaps). Log the value on a few of
    // your known-clean devices and set this slightly above what they report.
    private static final int MOUNT_GAP_THRESHOLD = 10;

    // SHA-256 (lowercase hex) of the PUBLIC KEY of Google's hardware-attestation
    // root certificate(s). Get them from the Android Developers page
    // "Verify hardware-backed key pairs with Key Attestation" (Google lists
    // more than one root and rotates them, so pin every current one).
    // If left empty the chain is still checked for consistency but the root is
    // NOT pinned (weaker).
    private static final Set<String> PINNED_ROOT_KEY_SHA256 = new HashSet<>(Arrays.asList(
            // "paste_sha256_of_root_public_key_here"
    ));

    // >>> CHANGE 12: generic constant alias (no app-specific name, since this is a
    // shared/global utility class). Same alias is reused on every run and the key
    // is deleted right after the check.
    private static final String ATTEST_KEY_ALIAS = "global_attest_check";
    // <<< END CHANGE 12
    private static volatile String attestationCache = "UNSET";

    public static class Result {
        public final boolean blocked;
        public final List<String> hard = new ArrayList<>();
        public final List<String> soft = new ArrayList<>();

        Result(Map<String, Boolean> checks) {
            for (Map.Entry<String, Boolean> e : checks.entrySet()) {
                if (e.getValue() != null && e.getValue()) {
                    if (SOFT_SIGNALS.contains(e.getKey())) soft.add(e.getKey());
                    else hard.add(e.getKey());
                }
            }
            blocked = !hard.isEmpty() || soft.size() >= SOFT_SIGNAL_LIMIT;
        }

        public String reason() {
            if (!blocked) return "no-root-detected";
            List<String> all = new ArrayList<>(hard);
            all.addAll(soft);
            return all.toString();
        }
    }

    public static Result evaluate(Context context) {
        return new Result(runRootChecks(context));
    }
    // <<< END CHANGE 2

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

            result.put("prop-source-mismatch", checkPropSourceMismatch());
            result.put("mount-namespace-anomaly", checkMountAnomalies());
            result.put("magisk-alt-package", checkMagiskAltPackages(context));
            result.put("sbin-symlink-anomaly", checkSbinAnomaly());
            // >>> CHANGE 3: removed cross-process-magisk (Android hides other
            // processes' /proc entries from apps, so it could never fire)
            // result.put("cross-process-magisk", checkCrossProcessMagisk());
            // <<< END CHANGE 3
            result.put("zygote-injected-lib", checkZygiskInjection());
            result.put("bootloader-unlocked", checkBootloaderUnlocked());
            result.put("verified-boot-not-green", checkVerifiedBootState());

            // >>> CHANGE 4: NEW checks that still work when Magisk DenyList /
            // Shamiko hides the usual files, packages and mounts
            result.put("mount-id-gap", checkMountIdGaps());
            result.put("maps-anomaly", checkMapsAnomalies());
            result.put("debugger-tracer-attached", checkTracerPid());
            result.put("key-attestation-failed", checkKeyAttestationFailed());
            // <<< END CHANGE 4
        } catch (Throwable t) {
            Log.e(TAG, "runRootChecks exception", t);
            result.put("root-check-exception", true);
        }

        for (Map.Entry<String, Boolean> e : result.entrySet()) {
            Log.i(TAG, e.getKey() + " = " + e.getValue());
        }
        return result;
    }

    // >>> CHANGE 5: decisions now use the hard/soft scoring from CHANGE 2
    public static boolean isRootedOrTampered(Context context) {
        return evaluate(context).blocked;
    }

    public static String getRootReason(Context context) {
        return evaluate(context).reason();
    }
    // <<< END CHANGE 5

    public static boolean checkIfDeviceRootedAndExit(Activity activity) {
        if (isRootedOrTampered(activity)) {
            //String reason = getRootReason(activity);
            AlertPopup.alert(0, activity,
                    activity.getString(R.string.rooted_device_title),
                    activity.getString(R.string.rooted_device_desc) /*+ "\nReason: " + reason*/,
                    AppConstants.TYPE_ERROR,
                    activity.getString(R.string.ok),
                    // >>> CHANGE 6: killProcess was after System.exit (never ran)
                    (popupId, isOkClick, obj) -> exitApp(activity));
            // <<< END CHANGE 6
            return true;
        }
        return false;
    }

    // >>> CHANGE 6 (helper)
    private static void exitApp(Activity activity) {
        activity.finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    // <<< END CHANGE 6 (helper)

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

    // >>> CHANGE 7: also catches LSPosed/EdXposed/Substrate hook frames and
    // looks at a fresh stack trace as well
    private static boolean isMethodHooked() {
        String[] needles = {
                "de.robv.android.xposed", "org.lsposed", "lsphooker",
                "edhooker", "com.saurik.substrate", "frida"
        };
        List<StackTraceElement> frames = new ArrayList<>();
        frames.addAll(Arrays.asList(Thread.currentThread().getStackTrace()));
        frames.addAll(Arrays.asList(new Throwable().getStackTrace()));
        for (StackTraceElement element : frames) {
            String cn = element.getClassName().toLowerCase();
            for (String n : needles) {
                if (cn.contains(n)) return true;
            }
        }
        return false;
    }
    // <<< END CHANGE 7

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

    // >>> CHANGE 8: /system/xbin/which does not exist on most devices, so the
    // old version always threw and returned false. Scan PATH directly.
    private static boolean checkWhichSu() {
        try {
            String path = System.getenv("PATH");
            if (path == null) return false;
            for (String dir : path.split(":")) {
                if (new File(dir, "su").exists()) return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
    // <<< END CHANGE 8

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
                "/cache/.magisk",
                // >>> CHANGE 9: KernelSU / APatch
                "/data/adb/ksu",
                "/data/adb/ap"
                // <<< END CHANGE 9
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

    private static boolean checkPropSourceMismatch() {
        try {
            String viaExec = getProp("ro.secure");
            String viaReflection = getPropViaReflection("ro.secure");
            if (viaReflection != null && !viaReflection.isEmpty()
                    && viaExec != null && !viaExec.isEmpty()
                    && !viaReflection.equals(viaExec)) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static String getPropViaReflection(String propName) {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);
            Object value = get.invoke(null, propName);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean checkMountAnomalies() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/mountinfo"));
            String line;
            int suspiciousCount = 0;
            while ((line = reader.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("magisk")
                        || lower.contains("/data/adb/modules")
                        || (lower.contains("overlay") && lower.contains("/system"))) {
                    suspiciousCount++;
                }
            }
            if (suspiciousCount > 0) return true;
        } catch (Exception ignored) {
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    // >>> CHANGE 10: more root-manager packages (KernelSU, APatch, old SU apps)
    private static boolean checkMagiskAltPackages(Context context) {
        String[] knownPackages = {
                "com.topjohnwu.magisk",
                "io.github.vvb2060.magisk",
                "io.github.huskydg.magisk",
                "com.topjohnwu.magisk.debug",
                "me.weishu.kernelsu",
                "me.bmax.apatch",
                "eu.chainfire.supersu",
                "com.noshufou.android.su",
                "com.koushikdutta.superuser",
                "com.thirdparty.superuser",
                "com.yellowes.su"
        };
        for (String pkg : knownPackages) {
            if (isPackageInstalled(context, pkg)) return true;
        }
        return false;
    }
    // <<< END CHANGE 10

    private static boolean checkSbinAnomaly() {
        try {
            File sbin = new File("/sbin");
            // >>> CHANGE 13: java.nio.file.Files needs API 26. This works on API 1+:
            // a symlink's canonical path differs from its absolute path.
            if (sbin.exists() && !sbin.getCanonicalPath().equals(sbin.getAbsolutePath())) {
                return true;
            }
            // <<< END CHANGE 13
        } catch (Exception ignored) {
        }
        return false;
    }

    private static boolean checkZygiskInjection() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = reader.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("zygisk") || lower.contains("magisk64") || lower.contains("magisk32")) {
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

    private static boolean checkBootloaderUnlocked() {
        try {
            String flashLocked = getProp("ro.boot.flash.locked");
            if ("0".equals(flashLocked)) return true;
            // >>> CHANGE 11: second source for the same fact
            String deviceState = getProp("ro.boot.vbmeta.device_state");
            if ("unlocked".equalsIgnoreCase(deviceState)) return true;
            // <<< END CHANGE 11
        } catch (Exception ignored) {
        }
        return false;
    }

    private static boolean checkVerifiedBootState() {
        try {
            String state = getProp("ro.boot.verifiedbootstate");
            if (state != null && !state.isEmpty() && !state.equalsIgnoreCase("green")) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static boolean isDeviceTampered(Activity activity) {
        if (isFridaDetected() || isXposedDetected() || isMethodHooked()) {
            AlertPopup.alert(0, activity, activity.getString(R.string.tamper_title),
                    activity.getString(R.string.tamper_desc), AppConstants.TYPE_ERROR,
                    activity.getString(R.string.ok),
                    // >>> CHANGE 6 (used here too)
                    (popupId, isOkClick, obj) -> exitApp(activity));
            // <<< END CHANGE 6
            return true;
        }
        return false;
    }

    // ========================================================================
    // >>> CHANGE 4 (implementations): new detection methods
    // ========================================================================

    /**
     * Mount-id gaps. Magisk DenyList hides itself by UNMOUNTING its mounts from
     * your app's mount namespace, so the entries disappear from mountinfo, but
     * the numeric mount IDs of the removed entries stay missing. Many missing
     * IDs inside the used range means something was unmounted.
     * Soft signal: calibrate MOUNT_GAP_THRESHOLD on clean devices.
     */
    private static boolean checkMountIdGaps() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/mountinfo"));
            Set<Integer> ids = new HashSet<>();
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 2) continue;
                int id = Integer.parseInt(parts[0]);
                ids.add(id);
                if (id < min) min = id;
                if (id > max) max = id;
            }
            if (ids.isEmpty()) return false;
            int missing = (max - min + 1) - ids.size();
            Log.i(TAG, "mount-id gaps = " + missing);
            return missing > MOUNT_GAP_THRESHOLD;
        } catch (Exception ignored) {
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /**
     * Leftovers in this process's memory map: /data/adb paths, LSPosed/Riru
     * libs, or shared libraries whose backing file was deleted after injection.
     */
    private static boolean checkMapsAnomalies() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/maps"));
            String line;
            while ((line = reader.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("/data/adb")
                        || lower.contains("lsposed")
                        || lower.contains("lspd")
                        || lower.contains("riru")
                        || lower.contains("edxp")
                        || lower.contains("xposed")
                        || (lower.contains(".so") && lower.contains("(deleted)"))) {
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

    /** A tracer attached to our process (Frida ptrace mode, gdb, strace...). */
    private static boolean checkTracerPid() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/status"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    return !line.substring(10).trim().equals("0");
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

    /**
     * Local hardware key attestation (strongest check here, and Magisk cannot
     * hide from it by unmounting anything). We ask the Keystore for a key with
     * an attestation certificate chain, then verify ON DEVICE:
     *   - the chain is internally valid and ends in a (pinned) Google root
     *   - the attestation is hardware-backed (TEE / StrongBox)
     *   - the challenge matches the one we just generated
     *   - the bootloader is LOCKED and verified boot state is VERIFIED
     * Returns true only when attestation clearly FAILS. If the device simply
     * doesn't support it (API < 24, no attestation) it returns false.
     */
    private static boolean checkKeyAttestationFailed() {
        // >>> CHANGE 14: setAttestationChallenge() is API 24+. On API 23 the
        // Keystore cannot produce an attestation chain, so this check gives
        // no signal there (the other checks still run). No crash, no false block.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return false;
        // <<< END CHANGE 14
        if (!"UNSET".equals(attestationCache)) return attestationCache != null;
        String failure = null;
        try {
            failure = runKeyAttestation();
        } catch (Throwable t) {
            Log.w(TAG, "attestation unavailable: " + t);
            failure = null; // unsupported device: no signal either way
        }
        attestationCache = failure;
        if (failure != null) Log.w(TAG, "attestation failure: " + failure);
        return failure != null;
    }

    // >>> CHANGE 14 (annotation): keeps lint/compiler happy with minSdk 23
    @TargetApi(Build.VERSION_CODES.N)
    private static String runKeyAttestation() throws Exception {
        // <<< END CHANGE 14 (annotation)

        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);

        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        if (ks.containsAlias(ATTEST_KEY_ALIAS)) ks.deleteEntry(ATTEST_KEY_ALIAS);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
        kpg.initialize(new KeyGenParameterSpec.Builder(
                ATTEST_KEY_ALIAS, KeyProperties.PURPOSE_SIGN)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setAttestationChallenge(challenge)
                .build());
        kpg.generateKeyPair();

        Certificate[] raw = ks.getCertificateChain(ATTEST_KEY_ALIAS);
        ks.deleteEntry(ATTEST_KEY_ALIAS);
        if (raw == null || raw.length < 2) return "chain-too-short";

        X509Certificate[] chain = new X509Certificate[raw.length];
        for (int i = 0; i < raw.length; i++) chain[i] = (X509Certificate) raw[i];

        // 1) chain signatures
        for (int i = 0; i < chain.length - 1; i++) {
            try {
                chain[i].verify(chain[i + 1].getPublicKey());
            } catch (Exception e) {
                return "chain-signature-invalid";
            }
        }
        X509Certificate root = chain[chain.length - 1];
        try {
            root.verify(root.getPublicKey());
        } catch (Exception e) {
            return "root-not-self-signed";
        }

        // 2) pin the root to Google's attestation root(s)
        if (!PINNED_ROOT_KEY_SHA256.isEmpty()) {
            String hash = toHex(MessageDigest.getInstance("SHA-256")
                    .digest(root.getPublicKey().getEncoded()));
            if (!PINNED_ROOT_KEY_SHA256.contains(hash)) return "root-not-google";
        } else {
            Log.w(TAG, "PINNED_ROOT_KEY_SHA256 is empty: root not pinned");
        }

        // 3) read the attestation extension from the leaf certificate
        byte[] ext = chain[0].getExtensionValue("1.3.6.1.4.1.11129.2.1.17");
        if (ext == null) return "no-attestation-extension";

        Der.Tlv wrapper = Der.read(ext, 0);              // OCTET STRING wrapper
        byte[] kd = Arrays.copyOfRange(ext, wrapper.valStart, wrapper.valEnd);
        Der.Tlv seq = Der.read(kd, 0);                   // KeyDescription SEQUENCE
        List<Der.Tlv> f = Der.children(kd, seq);
        if (f.size() < 8) return "bad-key-description";

        int attestationSecurityLevel = kd[f.get(1).valStart] & 0xFF; // 0 sw,1 TEE,2 StrongBox
        if (attestationSecurityLevel == 0) return "software-attestation";

        Der.Tlv challengeTlv = f.get(4);
        byte[] gotChallenge = Arrays.copyOfRange(kd, challengeTlv.valStart, challengeTlv.valEnd);
        if (!Arrays.equals(gotChallenge, challenge)) return "challenge-mismatch";

        // RootOfTrust is tag [704] in teeEnforced (index 7), sometimes softwareEnforced (6)
        Boolean locked = null;
        Integer bootState = null;
        for (int idx : new int[]{7, 6}) {
            for (Der.Tlv item : Der.children(kd, f.get(idx))) {
                if (item.tagNumber == 704) {
                    Der.Tlv rot = Der.read(kd, item.valStart);
                    List<Der.Tlv> r = Der.children(kd, rot);
                    if (r.size() >= 3) {
                        locked = kd[r.get(1).valStart] != 0;
                        bootState = kd[r.get(2).valStart] & 0xFF; // 0 Verified,1 SelfSigned,2 Unverified,3 Failed
                    }
                }
            }
            if (locked != null) break;
        }
        if (locked == null) return "no-root-of-trust";
        if (!locked) return "bootloader-unlocked-attested";
        if (bootState != 0) return "verified-boot-state-" + bootState;
        return null; // all good
    }

    private static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }

    /** Tiny DER reader, just enough for the attestation extension (no libraries). */
    private static final class Der {
        static final class Tlv {
            int tagNumber;
            int valStart;
            int valEnd;
        }

        static Tlv read(byte[] d, int pos) {
            int p = pos;
            int first = d[p++] & 0xFF;
            int tagNum = first & 0x1F;
            if (tagNum == 0x1F) {
                tagNum = 0;
                int b;
                do {
                    b = d[p++] & 0xFF;
                    tagNum = (tagNum << 7) | (b & 0x7F);
                } while ((b & 0x80) != 0);
            }
            int len = d[p++] & 0xFF;
            if (len >= 0x80) {
                int n = len & 0x7F;
                len = 0;
                for (int i = 0; i < n; i++) len = (len << 8) | (d[p++] & 0xFF);
            }
            Tlv t = new Tlv();
            t.tagNumber = tagNum;
            t.valStart = p;
            t.valEnd = p + len;
            return t;
        }

        static List<Tlv> children(byte[] d, Tlv parent) {
            List<Tlv> out = new ArrayList<>();
            int p = parent.valStart;
            while (p < parent.valEnd) {
                Tlv t = read(d, p);
                out.add(t);
                p = t.valEnd;
            }
            return out;
        }
    }
    // <<< END CHANGE 4 (implementations)
}