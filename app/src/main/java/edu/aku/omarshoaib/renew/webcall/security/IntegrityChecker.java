package edu.aku.omarshoaib.renew.webcall.security;

import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLPeerUnverifiedException;

import okhttp3.CertificatePinner;

// IntegrityChecker.java
public final class IntegrityChecker {

    /**
     * Detect if OkHttp's CertificatePinner.check() has been replaced
     * in memory by Frida/Xposed.
     *
     * How it works: we call check() with an INVALID pin against a known
     * hostname. If it does NOT throw, the method has been hooked to always
     * pass — we know we're under attack.
     */
    public static boolean isCertificatePinnerIntact() {
        try {
            CertificatePinner testPinner = new CertificatePinner.Builder()
                    .add("integrity.check.internal",
                         // Deliberately invalid SHA-256 pin
                         "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                    .build();

            // Build a fake certificate chain
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // We use a self-signed cert encoded inline for the probe
            // This MUST throw CertificatePinException if pinner is intact
            testPinner.check("integrity.check.internal"
                    /* empty list — should throw immediately */);

            // If we reach here, pinner was hooked (didn't throw)
            return false;

        } catch (SSLPeerUnverifiedException e) {
            // Expected — pinner is working correctly
            return true;
        } catch (Exception e) {
            // Unexpected exception type — treat as suspicious
            return false;
        }
    }
}