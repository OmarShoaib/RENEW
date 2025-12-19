package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import edu.aku.omarshoaib.renew.R;

public class AppPermissions {

    final private Activity activity;
    final private Callbacks.IAppPermissions callback;

    public static final int SETTINGS_REQUEST_CODE = 100;
    public static final int PERMISSION_POPUP_ID = 200;

    public AppPermissions(Activity activity, Callbacks.IAppPermissions callback) {
        this.activity = activity;
        this.callback = callback;
    }

    // Requesting permissions
    public void requestPermissions(String[] permissionsArr, int requestCode, boolean isFailureCallbackNeeded) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(activity)
                    .withPermissions(permissionsArr)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                callback.onPermissionsSuccess(requestCode);
                            }

                            // check for permanent denial of any permission
                            if (!report.isAnyPermissionPermanentlyDenied() && !report.areAllPermissionsGranted()) {
                                // show alert dialog navigating to Settings
                                if (isFailureCallbackNeeded) {
                                    callback.onPermissionFailure(requestCode);
                                } /*else {
                                    showSettingsDialog();
                                }*/
                            } else if (report.isAnyPermissionPermanentlyDenied()) {
//                                callback.onPermissionsSuccess(requestCode);
//                                AppConstants.showSimpleSnackBar(activity,
//                                        activity.getResources().getString(R.string.need_permission_desc),
//                                        Snacky.LENGTH_LONG, AppConstants.TYPE_ERROR);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            callback.onPermissionsSuccess(requestCode);
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    public void showSettingsDialog() {
        AlertPopup.alert(PERMISSION_POPUP_ID, activity, activity.getString(R.string.need_permission),
                activity.getString(R.string.need_permission_desc),
                AppConstants.TYPE_ERROR, activity.getString(R.string.ok), iAlertCallback);
    }

    // Navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    // Alert Callback
    Callbacks.IAlertCallback iAlertCallback = (popupId, isOkClick, text) -> openSettings();

}
