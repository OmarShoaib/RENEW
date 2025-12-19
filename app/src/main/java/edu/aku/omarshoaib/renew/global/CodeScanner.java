package edu.aku.omarshoaib.renew.global;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import edu.aku.omarshoaib.renew.R;

public class CodeScanner {

    private final Activity activity;
    private final String scanCodeType;
    private final String promptText;
    private final int timeout;
    private final Callbacks.ICodeScannerCallback iCodeScannerCallback;

    private final AppPermissions appPermissions;

    public static final int CAMERA_PERMISSION_CODE = 200;
    public static String[] camPermissionsArr = new String[]{
            Manifest.permission.CAMERA
            /*, Manifest.permission.WRITE_EXTERNAL_STORAGE*/};

    public CodeScanner(Activity activity, String scanCodeType, String promptText,
                       int timeout, Callbacks.ICodeScannerCallback iCodeScannerCallback) {
        this.activity = activity;
        this.scanCodeType = scanCodeType;
        this.promptText = promptText;
        this.timeout = timeout;
        this.iCodeScannerCallback = iCodeScannerCallback;

        appPermissions = new AppPermissions(activity, iAppPermissions);
    }

    public void checkPermissionAndScanCode() {
        appPermissions.requestPermissions(camPermissionsArr, CAMERA_PERMISSION_CODE, true);
    }

    private void scanCode() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setDesiredBarcodeFormats(scanCodeType);
        scanOptions.setPrompt(promptText);
        scanOptions.setOrientationLocked(false);
        scanOptions.setBeepEnabled(true);
        scanOptions.setTimeout(timeout);

        iCodeScannerCallback.scanCode(scanOptions);
    }

    // Permission callback
    Callbacks.IAppPermissions iAppPermissions = new Callbacks.IAppPermissions() {
        @Override
        public void onPermissionsSuccess(int requestCode) {
            scanCode();
        }

        @Override
        public void onPermissionFailure(int requestCode) {
            AlertPopup.alert(activity, activity.getString(R.string.permission_denied),
                    activity.getString(R.string.camera_scan_permission_not_enabled),
                    AppConstants.TYPE_ERROR);
        }
    };

    // When code scan is not correct due to some error
    public static void codeScanError(Activity activity, ScanIntentResult result) {
        Intent originalIntent = result.getOriginalIntent();
        if (originalIntent == null) {
            AppConstants.showSimpleSnackBar(activity,
                    activity.getString(R.string.scan_cancelled),
                    Snackbar.LENGTH_LONG, AppConstants.TYPE_ERROR);
        } else if (originalIntent.hasExtra(Intents.Scan.TIMEOUT)) {
            AppConstants.showSimpleSnackBar(activity,
                    activity.getString(R.string.scan_timeout),
                    Snackbar.LENGTH_LONG, AppConstants.TYPE_ERROR);
        } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
            AlertPopup.alert(activity, activity.getString(R.string.permission_denied),
                    activity.getString(R.string.camera_scan_permission_not_enabled),
                    AppConstants.TYPE_ERROR);
        }
    }

}