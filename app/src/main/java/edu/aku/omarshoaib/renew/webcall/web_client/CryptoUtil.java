package edu.aku.omarshoaib.renew.webcall.web_client;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import edu.aku.omarshoaib.renew.BuildConfig;
import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.webcall.security.FridaDetector;
import edu.aku.omarshoaib.renew.webcall.security.IntegrityChecker;
import edu.aku.omarshoaib.renew.webcall.security.PinnedTrustManager;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class CryptoUtil {

    private static final String cypherInstance = "AES/GCM/NoPadding";

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 16;

    private static X509TrustManager x509;

    /* WEB CALL CIPHERING - START */

    // Web call encryption

    public static String encrypt(String data) {
        return encrypt(data, false);
    }

    // isDPortal is used for dictionary portal strings nd ranges enc and dec.
    public static String encrypt(String data, boolean isDPortal) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            (new SecureRandom()).nextBytes(iv);
            Cipher cipher = Cipher.getInstance(cypherInstance);
            GCMParameterSpec ivSpec = new GCMParameterSpec(TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(hashSHA256(isDPortal).getBytes(StandardCharsets.UTF_8), "AES"), ivSpec);
            byte[] ciphertext = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            byte[] encrypted = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Web call decryption

    public static String decrypt(String data) {
        return decrypt(data, false);
    }
    // isDPortal is used for dictionary portal strings nd ranges enc and dec.

    public static String decrypt(String data, boolean isDPortal) {
        try {
            byte[] decoded = Base64.decode(data, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LENGTH);
            Cipher cipher = Cipher.getInstance(cypherInstance);
            GCMParameterSpec ivSpec = new GCMParameterSpec(TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(hashSHA256(isDPortal).getBytes(StandardCharsets.UTF_8), "AES"), ivSpec);
            byte[] ciphertext = cipher.doFinal(decoded, IV_LENGTH, decoded.length - IV_LENGTH);
            return new String(ciphertext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String hashSHA256(boolean isDPortal)
            throws NoSuchAlgorithmException {
        String input = !isDPortal ? AppConstants.IBAHC : AppConstants.DP_IBAHC;
        MessageDigest mDigest = MessageDigest.getInstance("SHA-384");

        int value = !isDPortal ? AppConstants.TRATS : AppConstants.DP_TRATS;
        byte[] shaByteArr = mDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(shaByteArr, Base64.NO_WRAP).substring(value, value + 32);
    }

    /* WEB CALL CIPHERING - END */

    /* WEB CALL SSL/TLS VERIFICATION - START */

   /* public static SSLContext getSSLContext(Activity activity) {
        SSLContext sslContext = null;
        try {
            sslContext = createCertificate(activity);
        } catch (CertificateException | IOException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private static SSLContext createCertificate(Activity activity)
            throws CertificateException, IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {

        Certificate ca = getValidCertificate(activity);

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, tmf.getTrustManagers(), null);
        x509 = (X509TrustManager) tmf.getTrustManagers()[0];

        return sslContext;
    }

    public static X509TrustManager getX509TrustManager() {
        return x509;
    }*/

    /*// Get valid certificate from assets folder
    public static Certificate getValidCertificate(Activity activity) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = activity.getResources().openRawResource(R.raw.vcoe1_aku_edu);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }
            return ca;
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check server certificate validity
    public static boolean checkCertValidity(Activity activity, SSLSession session) {
        Certificate[] certs;
        try {
            certs = session.getPeerCertificates();
        } catch (SSLException e) {
            e.printStackTrace();
            return false;
        }

        for (Certificate cert : certs) {
            if (cert instanceof X509Certificate) {
                try {
                    ((X509Certificate) cert).checkValidity();
                    if (cert.equals(getValidCertificate(activity))) return true;
                } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    /*//* WEB CALL SSL/TLS VERIFICATION - END *//*
            */

    // CryptoUtil.java  — drop-in replacement for your current method
    public static OkHttpClient generateSecureOkHttpClient(
            Context context, String hostname) {
        try {

            // =================================================
            // GATE 1: Anti-instrumentation check
            // Must be FIRST — before any SSL setup
            // =================================================
            FridaDetector.assertNoInstrumentation(context);

            // =================================================
            // GATE 2: Verify OkHttp's own pinner hasn't been
            // hooked at the JVM level
            // =================================================
            if (!IntegrityChecker.isCertificatePinnerIntact()) {
                throw new SecurityException("Runtime integrity check failed [E03]");
            }

            // =================================================
            // LOGGING — debug only, NEVER in release
            // =================================================
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(
                    BuildConfig.DEBUG
                            ? HttpLoggingInterceptor.Level.BODY
                            : HttpLoggingInterceptor.Level.NONE);

            // =================================================
            // BUILD THE PINNED TRUST MANAGER
            // (custom impl — not hookable by generic Frida scripts)
            // =================================================
            // These pin values come from YOUR certificate.
            // See "How to generate your pins" section below.
            Set<String> pins = new HashSet<>();
            pins.add(BuildConfig.CERT_KEY);           // leaf cert pin
            pins.add(BuildConfig.CERT_KEY_BACKUP);    // backup / intermediate pin

            PinnedTrustManager pinnedTrustManager = new PinnedTrustManager(pins);

            // =================================================
            // SSL CONTEXT using our custom trust manager
            // =================================================
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{pinnedTrustManager}, null);

            // =================================================
            // ALSO keep OkHttp's CertificatePinner as a second
            // independent layer — attacker must bypass BOTH
            // =================================================
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(hostname, BuildConfig.CERT_KEY)
                    .add(hostname, BuildConfig.CERT_KEY_BACKUP)
                    .build();

            // =================================================
            // NETWORK SECURITY INTERCEPTOR
            // Validates security state on every single request
            // =================================================
            Interceptor securityInterceptor = chain -> {
                // Re-check on every request (Frida might be injected mid-session)
                if (FridaDetector.isFridaPortOpen()) {
                    throw new IOException("Security policy violation [E04]");
                }
                return chain.proceed(chain.request());
            };

            // =================================================
            // ASSEMBLE FINAL CLIENT
            // =================================================
            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), pinnedTrustManager)
                    .certificatePinner(certificatePinner)          // Layer A
                    .hostnameVerifier(new StrictHostnameVerifier(hostname)) // Layer B
                    .addInterceptor(securityInterceptor)           // Layer C
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(AppConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(AppConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(AppConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    // Disable connection reuse to prevent session hijacking
                    .retryOnConnectionFailure(false)
                    .build();

        } catch (SecurityException e) {
            // Re-throw security exceptions as-is
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create secure OkHttpClient [E00]", e);
        }
    }

    // -------------------------------------------------------
    // Strict hostname verifier — reject wildcard mismatches
    // -------------------------------------------------------
    private static class StrictHostnameVerifier implements HostnameVerifier {
        private final String expectedHost;

        StrictHostnameVerifier(String expectedHost) {
            this.expectedHost = expectedHost.toLowerCase(Locale.US);
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (hostname == null || !hostname.toLowerCase(Locale.US).equals(expectedHost)) {
                return false;
            }
            // Also run the default verifier
            return HttpsURLConnection.getDefaultHostnameVerifier()
                    .verify(hostname, session);
        }
    }
}
