package edu.aku.omarshoaib.renew.global;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class GPSLocation {

    public static String GPS_PERMISSION = "0";
    public static String GPS_AVAILABLE = "0";

    private final Activity activity;
    private final FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ICurrentLocationCB iCurrentLocationCB;

    private final AppPermissions appPermissions;

    public static boolean IS_LOCATION_INITIALIZED = false;

    private static final int MY_LOCATION_PERMISSION_CODE = 101;

    private static final int LOCATION_UPDATE_INTERVAL = 5000;
    /*private static final int LOCATION_MIN_UPDATE_INTERVAL = 5000;
    private static final int LOCATION_MAX_UPDATE_INTERVAL = 10000;
    private static final int LOCATION_MIN_DISTANCE = 1;*/

    // For location retrying if not fetched
    private static final int LOCATION_RETRY_INTERVAL = 200;
    private static final int LOCATION_RETRY_COUNT = 5;
    private int retryCounter;

    // For GPS data got stuck i.e. same location everytime
    private String prevLat = _EMPTY_, prevLon = _EMPTY_;
    private static final int GPS_DUPLICATE_DATA_RESET_LIMIT = 5;
    private int gpsResetIndex = 0;

    private final String[] locPermissionsArr = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};

    public GPSLocation(Activity activity) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        appPermissions = new AppPermissions(activity, iAppPermissions);
        // Init Location Request Builder
        initLocationRequestBuilder();
    }

    public GPSLocation(Activity activity, ICurrentLocationCB iCurrentLocationCB) {
        this.activity = activity;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        appPermissions = new AppPermissions(activity, iAppPermissions);
        this.iCurrentLocationCB = iCurrentLocationCB;
        // Init Location Request Builder
        initLocationRequestBuilder();
    }

    //Check GPS Availability
    public boolean isGPSAvailable() {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /* =========================
     * GET CURRENT LOCATION
     * ========================= */

    // Init Location Request Builder
    private void initLocationRequestBuilder() {
        LocationRequest.Builder locationRequestBuilder = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                .setWaitForAccurateLocation(false)
                .setIntervalMillis(LOCATION_UPDATE_INTERVAL);
                /*.setMinUpdateIntervalMillis(LOCATION_MIN_UPDATE_INTERVAL)
                .setMaxUpdateDelayMillis(LOCATION_MAX_UPDATE_INTERVAL)
                .setMinUpdateDistanceMeters(LOCATION_MIN_DISTANCE);*/
        locationRequest = locationRequestBuilder.build();
    }

    // Check GPS permission
    private boolean checkGPSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android sdk level >= Marshmallow(6.0)
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // If permission is not granted then we do not need to ask for permission
                // therefore, GPS coordinates will not be collected
                appPermissions.requestPermissions(locPermissionsArr, MY_LOCATION_PERMISSION_CODE, false);
                return false;    // Permission not granted
            }
        }
        return true;   // Permission granted
    }

    // Get Current Location
    public void getCurrentLocation() {
        if (checkGPSPermission()) {
            GPS_PERMISSION = "1";
            // Permission is available
            if (isGPSAvailable()) {
                GPS_AVAILABLE = "1";
                // GPS are available
                buildLocationCallback();
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                fusedLocationClient.getLastLocation().addOnSuccessListener(activity, location -> {
                    if (location != null) {
                        if (iCurrentLocationCB != null) iCurrentLocationCB.currentLocation(location);
                        setValues(location);
                    }
                });
            } else GPS_AVAILABLE = "0";
        } else GPS_PERMISSION = "0";
    }

    // Location Updates Callback
    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult.getLocations().isEmpty() && retryCounter < LOCATION_RETRY_COUNT) {
                    retryCounter++;
                    getLocationTimer();
                } else {
                    for (Location location : locationResult.getLocations())
                        if (location != null) setValues(location);
                }
            }
        };
    }

    // Setting values
    private void setValues(Location location) {
        GPSLocation.IS_LOCATION_INITIALIZED = true;

        // User Location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
        long date = location.getTime();

        SharedPrefs.write(SharedPrefs.GPS_LAT, Double.toString(latitude));
        SharedPrefs.write(SharedPrefs.GPS_LON, Double.toString(longitude));
        SharedPrefs.write(SharedPrefs.GPS_ACC, Double.toString(accuracy));
        SharedPrefs.write(SharedPrefs.GPS_DATE, Double.toString(date));

        // To check if the GPS data got stuck i.e. always provide same location
        // then we will reset the location updates
        checkGPSDataStatus(Double.toString(latitude), Double.toString(longitude));

       /* Log.e("LAT: ", Double.toString(latitude));
        Log.e("LON: ", Double.toString(longitude));
        Log.e("ACC: ", Double.toString(accuracy));
        Log.e("DATE: ", Double.toString(date));
        Log.e("ANOTHER_GPS_LOCATION", "-------------------------------");

        String gpsLoc = String.format(Locale.ENGLISH, "LAT: %.7f\nLON: %.7f",
                latitude, longitude);
        Toast.makeText(activity, gpsLoc, Toast.LENGTH_SHORT).show();*/

//        Log.e("GPS_LOCATION", latitude + ", " + longitude);

        // Get Address for lat lon
        /*try {
            Geocoder myLocation = new Geocoder(activity, Locale.getDefault());
            List<Address> myList = myLocation.getFromLocation(latitude, longitude, 1);
            Address returnedAddress = myList.get(0);

            StringBuilder strReturnedAddress = new StringBuilder();
            String address = AppConstants._EMPTY_;

            if (returnedAddress.getAddressLine(0) != null)
                address += returnedAddress.getAddressLine(0);
            if (returnedAddress.getAddressLine(1) != null)
                address += ", " + returnedAddress.getAddressLine(1);
            if (returnedAddress.getAddressLine(2) != null)
                address += ", " + returnedAddress.getAddressLine(2);

            for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                if (returnedAddress.getAddressLine(i) != null) {
                    if (i > 0)
                        strReturnedAddress.append(", ");
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
            }

            address = strReturnedAddress.toString();

            String city = myList.get(0).getLocality();
            String state = myList.get(0).getAdminArea();
            String country = myList.get(0).getCountryName();
            String postalCode = myList.get(0).getPostalCode();
            String knownName = myList.get(0).getFeatureName();

            if (city != null)
                address += ", " + city;

            if (state != null)
                address += ", " + state;

            if (country != null)
                address += ", " + country;

            if (postalCode != null)
                address += ", " + postalCode;

            if (knownName != null)
                address += ", " + knownName;

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    // To check if the GPS data got stuck i.e. always provide same location
    // then we will reset the location updates
    private void checkGPSDataStatus(String currLat, String currLon) {
        if (prevLat.equals(currLat) && prevLon.equals(currLon)) {
            if (gpsResetIndex == GPS_DUPLICATE_DATA_RESET_LIMIT) {
                Log.e("LOC_RESET: ", "Location Reset");
                prevLat = _EMPTY_;
                prevLon = _EMPTY_;
                gpsResetIndex = 0;

                // Stop current location updates
                fusedLocationClient.removeLocationUpdates(locationCallback);

                // Start fresh location updates
                if (checkGPSPermission())
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else gpsResetIndex++;
        } else {
            prevLat = currLat;
            prevLon = currLon;
            gpsResetIndex = 0;
        }
    }

    // Call getCurrentLocation() again after some time interval
    // if the location is null
    private void getLocationTimer() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            retryCounter++;
            getCurrentLocation();
        }, LOCATION_RETRY_INTERVAL);
    }

    public interface ICurrentLocationCB {
        void currentLocation(Location location);
    }

    /* =========================
     * PERMISSIONS CALLBACK
     * ========================= */

    Callbacks.IAppPermissions iAppPermissions = new Callbacks.IAppPermissions() {
        @Override
        public void onPermissionsSuccess(int requestCode) {
            if (requestCode == MY_LOCATION_PERMISSION_CODE) {
                getCurrentLocation();
            }
        }

        @Override
        public void onPermissionFailure(int requestCode) {
            Log.e("GPS", "Permission not granted");
        }
    };



    Callbacks.IAlertCallback callback = (popupId, isOkClick, obj) -> {
        if (isOkClick) getCurrentLocation();
        else System.exit(0);
    };
}
