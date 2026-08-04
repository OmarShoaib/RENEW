package edu.aku.omarshoaib.renew.webcall.security;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

// FridaDetector.java
public final class FridaDetector {

    private static final String[] FRIDA_ARTIFACTS = {
        // Frida server process names
        "frida-server", "frida-agent", "frida", "gum-js-loop",
        // Frida native libraries loaded in memory
        "frida-gadget", "frida_agent_main",
        // Common Frida port (27042 default)
    };

    private static final String[] SUSPICIOUS_MAPS_STRINGS = {
        "frida", "xposed", "substrate", "magisk", "zygisk",
        "lsplant", "edxposed", "taichi"
    };

    /**
     * Master check — call this before building OkHttpClient.
     * Throws RuntimeException if instrumentation is detected.
     */
    public static void assertNoInstrumentation(Context context) {
        if (isFridaPortOpen()
                || isFridaInMaps()
                || isFridaLibraryLoaded()
                || isFridaProcessRunning()) {
            // Do NOT reveal details in the message — log vaguely
            throw new SecurityException("Integrity check failed [E01]");
        }
    }

    // -------------------------------------------------------
    // Check 1 : Frida default port 27042 open on localhost
    // -------------------------------------------------------
    public static boolean isFridaPortOpen() {
        int[] fridaPorts = {27042, 27043};
        for (int port : fridaPorts) {
            try {
                // Run socket check on a background thread with strict timeout
                final boolean[] result = {false};
                Thread thread = new Thread(() -> {
                    try (Socket s = new Socket()) {
                        s.connect(
                                new InetSocketAddress("127.0.0.1", port),
                                80  // 80ms timeout — localhost so this is plenty
                        );
                        result[0] = true; // Connected = Frida port is open
                    } catch (IOException ignored) {
                        // Expected — port not open
                    }
                });
                thread.start();
                thread.join(200); // Wait max 200ms per port
                if (result[0]) return true;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }
    // -------------------------------------------------------
    // Check 2 : Scan /proc/self/maps for Frida artifacts
    // -------------------------------------------------------
    private static boolean isFridaInMaps() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader("/proc/self/maps"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String lower = line.toLowerCase();
                for (String artifact : SUSPICIOUS_MAPS_STRINGS) {
                    if (lower.contains(artifact)) {
                        return true;
                    }
                }
            }
        } catch (IOException ignored) { }
        return false;
    }

    // -------------------------------------------------------
    // Check 3 : Check loaded native libraries
    // -------------------------------------------------------
    private static boolean isFridaLibraryLoaded() {
        // Reading /proc/self/maps covers this, but double-check
        // via class loader — Frida injects frida-agent*.so
        String mapsPath = "/proc/self/maps";
        try (BufferedReader br = new BufferedReader(new FileReader(mapsPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("frida-agent") || line.contains("frida-gadget")) {
                    return true;
                }
            }
        } catch (IOException ignored) { }
        return false;
    }

    // -------------------------------------------------------
    // Check 4 : Scan running processes for frida-server
    // -------------------------------------------------------
    private static boolean isFridaProcessRunning() {
        try {
            File procDir = new File("/proc");
            File[] processes = procDir.listFiles();
            if (processes == null) return false;

            for (File process : processes) {
                if (!process.isDirectory()) continue;
                File cmdlineFile = new File(process, "cmdline");
                if (!cmdlineFile.exists()) continue;

                try (BufferedReader reader = new BufferedReader(
                        new FileReader(cmdlineFile))) {
                    String cmdline = reader.readLine();
                    if (cmdline != null) {
                        String lower = cmdline.toLowerCase();
                        for (String artifact : FRIDA_ARTIFACTS) {
                            if (lower.contains(artifact)) {
                                return true;
                            }
                        }
                    }
                } catch (IOException ignored) { }
            }
        } catch (Exception ignored) { }
        return false;
    }
}