package edu.aku.omarshoaib.renew.global;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtils {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Check server reachability by just pinging it
    public static void checkServerReachability(String serverUrl, IServerReachableListener listener) {
        executor.submit(() -> {
            boolean isReachable = false;
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD"); // Use HEAD request to check server availability
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    isReachable = true; // Server is reachable
                }
            } catch (IOException e) {
                // Handle connection error
                e.printStackTrace();
            }
            listener.onServerReachable(isReachable);
        });
    }

    /*// Check server reachability by just pinging it
    public static void checkServerReachability(String serverUrl, IServerReachableListener listener) {
        executor.submit(() -> {
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    String decryptedResponse = CryptoUtil.decrypt(response.toString());
                    String errorMessage = WebCall.checkForError(decryptedResponse, response.toString());
                    if (AppConstants.isEmpty(errorMessage)) {
                        SyncModel.WebResponse webResponse = MainApp.gson.fromJson(decryptedResponse,
                                SyncModel.WebResponse.class);
                        listener.onServerReachable(webResponse.getStatus() == 1);
                    } else
                        listener.onServerReachable(false);
                } else
                    listener.onServerReachable(false);
            } catch (IOException e) {
                // Handle connection error
                e.printStackTrace();
                listener.onServerReachable(false);
            }
        });
    }*/

    public interface IServerReachableListener {
        void onServerReachable(boolean isReachable);
    }
}
