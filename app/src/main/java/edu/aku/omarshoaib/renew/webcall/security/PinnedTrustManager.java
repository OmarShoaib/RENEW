package edu.aku.omarshoaib.renew.webcall.security;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import edu.aku.omarshoaib.renew.R;

// PinnedTrustManager.java
public final class PinnedTrustManager implements X509TrustManager {

    private final Set<String> validPins;
    private final X509TrustManager systemTrustManager;

    public PinnedTrustManager(Context context, Set<String> validPins) {
        if (validPins == null || validPins.isEmpty()) {
            throw new IllegalArgumentException("Pins must not be empty");
        }
        this.validPins = Collections.unmodifiableSet(new HashSet<>(validPins));
        this.systemTrustManager = buildTrustManager(context);
    }

    // -------------------------------------------------------
    // Build TrustManager from YOUR certificate, not system CA
    // This matches what network_security_config is doing and
    // ensures debug and release behave identically
    // -------------------------------------------------------
    private static X509TrustManager buildTrustManager(Context context) {
        try {
            // Load your PEM certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources()
                    .openRawResource(R.raw.vcoe1_aku_edu);
            Certificate ca = cf.generateCertificate(caInput);
            caInput.close();

            // Build a KeyStore containing your cert
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server_ca", ca);

            // Build TrustManager from that KeyStore
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    return (X509TrustManager) tm;
                }
            }
            throw new RuntimeException("No X509TrustManager found");

        } catch (Exception e) {
            throw new RuntimeException("Failed to build TrustManager", e);
        }
    }

    // -------------------------------------------------------
    // 2-arg version — required by X509TrustManager interface
    // -------------------------------------------------------
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        performFullValidation(chain, authType, null);
    }

    // -------------------------------------------------------
    // 3-arg version — called by Android framework via
    // reflection when domain-config is present.
    // Must be public and exactly this signature.
    // -------------------------------------------------------
    @SuppressWarnings("unused")
    public List<X509Certificate> checkServerTrusted(
            X509Certificate[] chain,
            String authType,
            String hostname) throws CertificateException {
        performFullValidation(chain, authType, hostname);
        return chain != null ? Arrays.asList(chain) : Collections.emptyList();
    }

    // -------------------------------------------------------
    // Core validation — shared by both overloads
    // -------------------------------------------------------
    private void performFullValidation(
            X509Certificate[] chain,
            String authType,
            @Nullable String hostname) throws CertificateException {

        if (chain == null || chain.length == 0) {
            throw new CertificateException("Empty certificate chain");
        }

        // Step A: Chain validation against YOUR certificate
        // (not system store — matches network_security_config)
        try {
            if (hostname != null) {
                // Try 3-arg first for domain-config compatibility
                try {
                    Method method = systemTrustManager.getClass().getMethod(
                            "checkServerTrusted",
                            X509Certificate[].class,
                            String.class,
                            String.class);
                    method.setAccessible(true);
                    method.invoke(systemTrustManager, chain, authType, hostname);
                } catch (NoSuchMethodException e) {
                    // 3-arg not available, fall back to 2-arg
                    systemTrustManager.checkServerTrusted(chain, authType);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof CertificateException) {
                        throw (CertificateException) e.getCause();
                    }
                    throw new CertificateException("Chain validation failed", e);
                }
            } else {
                systemTrustManager.checkServerTrusted(chain, authType);
            }
        } catch (CertificateException e) {
            throw e;
        } catch (Exception e) {
            throw new CertificateException("Chain validation error: " + e.getMessage());
        }

        // Step B: Public key pin check
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

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        throw new CertificateException("Client certificates not supported");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private static String computeSpkiPin(X509Certificate cert) throws Exception {
        byte[] spkiBytes = cert.getPublicKey().getEncoded();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(spkiBytes);
        return "sha256/" + Base64.encodeToString(hash, Base64.NO_WRAP);
    }
}