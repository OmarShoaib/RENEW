package edu.aku.omarshoaib.renew.webcall.security;

import android.util.Base64;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

// PinnedTrustManager.java
public final class PinnedTrustManager implements X509TrustManager {

    private final Set<String> validPins;
    private final X509TrustManager systemTrustManager;

    public PinnedTrustManager(Set<String> validPins) {
        if (validPins == null || validPins.isEmpty()) {
            throw new IllegalArgumentException("Pins must not be empty");
        }
        this.validPins = Collections.unmodifiableSet(new HashSet<>(validPins));
        this.systemTrustManager = getSystemTrustManager();
    }

    // -------------------------------------------------------
    // Get the real system TrustManager once at construction
    // time — reuse it for all chain validations
    // -------------------------------------------------------
    private static X509TrustManager getSystemTrustManager() {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null); // null = use system trust store
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not obtain system TrustManager", e);
        }
        throw new RuntimeException("No X509TrustManager found");
    }

    // -------------------------------------------------------
    // Standard 2-arg version (required by interface)
    // -------------------------------------------------------
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // Delegate to the 3-arg version with no hostname
        // (will still do chain + pin validation)
        performFullValidation(chain, authType, null);
    }

    // -------------------------------------------------------
    // Android-specific 3-arg version — THIS is what fixes
    // the "hostname aware checkServerTrusted" error.
    // Android's TrustManagerImpl calls this when a
    // domain-config is present in network_security_config.xml
    // -------------------------------------------------------
    @SuppressWarnings("unused") // Called by Android framework via reflection
    public List<X509Certificate> checkServerTrusted(
            X509Certificate[] chain,
            String authType,
            String hostname) throws CertificateException {

        performFullValidation(chain, authType, hostname);

        // Return the validated chain (required by Android's internal API)
        return chain != null ? Arrays.asList(chain) : Collections.emptyList();
    }

    // -------------------------------------------------------
    // Core validation logic shared by both overloads
    // -------------------------------------------------------
    private void performFullValidation(
            X509Certificate[] chain,
            String authType,
            @Nullable String hostname) throws CertificateException {

        if (chain == null || chain.length == 0) {
            throw new CertificateException("Empty certificate chain");
        }

        // ---------------------------------------------------
        // Step A: System chain validation
        // Use the 3-arg version on the system TrustManager
        // if hostname is available — this satisfies Android's
        // domain-config requirement
        // ---------------------------------------------------
        try {
            if (hostname != null) {
                // Attempt to call the Android-specific 3-arg method
                // on the system trust manager via reflection
                callSystemCheckServerTrustedWithHostname(chain, authType, hostname);
            } else {
                systemTrustManager.checkServerTrusted(chain, authType);
            }
        } catch (CertificateException e) {
            throw e; // Re-throw — chain is genuinely invalid
        } catch (Exception e) {
            // Reflection failed — fall back to 2-arg
            try {
                systemTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                throw ce;
            }
        }

        // ---------------------------------------------------
        // Step B: Our custom public-key pin check
        // System chain passed — now enforce our pins
        // ---------------------------------------------------
        boolean pinMatched = false;
        for (X509Certificate cert : chain) {
            try {
                String computedPin = computeSpkiPin(cert);
                if (validPins.contains(computedPin)) {
                    pinMatched = true;
                    break;
                }
            } catch (Exception e) {
                throw new CertificateException("Pin computation error", e);
            }
        }

        if (!pinMatched) {
            throw new CertificateException("Certificate pin verification failed [E02]");
        }
    }

    // -------------------------------------------------------
    // Call system TrustManager's 3-arg checkServerTrusted
    // via reflection — Android hides this in TrustManagerImpl
    // -------------------------------------------------------
    private void callSystemCheckServerTrustedWithHostname(
            X509Certificate[] chain,
            String authType,
            String hostname) throws Exception {

        Method method = systemTrustManager.getClass().getMethod(
                "checkServerTrusted",
                X509Certificate[].class,
                String.class,
                String.class);
        method.setAccessible(true);
        method.invoke(systemTrustManager, chain, authType, hostname);
    }

    // -------------------------------------------------------
    // Not used for server validation
    // -------------------------------------------------------
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        throw new CertificateException("Client certificates not supported");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    // -------------------------------------------------------
    // Compute SHA-256 of SubjectPublicKeyInfo (SPKI)
    // -------------------------------------------------------
    private static String computeSpkiPin(X509Certificate cert) throws Exception {
        byte[] spkiBytes = cert.getPublicKey().getEncoded();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(spkiBytes);
        return "sha256/" + Base64.encodeToString(hash, Base64.NO_WRAP);
    }
}