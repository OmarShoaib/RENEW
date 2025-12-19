package edu.aku.omarshoaib.renew.global;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class GPSLocationService extends Service {

    // Initialization code - will used on other activity if needed
    // Pasting the code here for reference
    // Init GPS location request
       /* if (AppConstants.IS_GPS_ON && !AppConstants.isServiceRunning(activity, GPSLocationService.class.getSimpleName())) {
            // Set the GPSLocation instance in the service
            GPSLocationService.setGpsLocation(new GPSLocation(activity));
            startService(new Intent(this, GPSLocationService.class));  // Start the service
        }*/

    private Handler handler;
    private Runnable runnable;
    @SuppressLint("StaticFieldLeak")
    private static GPSLocation gpsLocation;

    private static final int PERIODIC_INTERVAL = 10000; // 10 seconds

    public static void setGpsLocation(GPSLocation location) {
        gpsLocation = location;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();

        // Define the task that runs periodically
        runnable = new Runnable() {
            @Override
            public void run() {
                // This is the function that you want to run periodically
                if (gpsLocation != null)
                    gpsLocation.getCurrentLocation(); // Use the same GPSLocation instance

                // Re-run this task every 10 seconds
                handler.postDelayed(this, PERIODIC_INTERVAL);
            }
        };

        // Start the periodic task immediately when service is created
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service will continue running until explicitly stopped
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Not binding, so we return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the periodic task when the service is destroyed
        handler.removeCallbacks(runnable);
    }
}
