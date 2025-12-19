package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.io.File;
import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;

public class SendDB {

    // Email database to specified email address as attachment
    public static void email(Activity activity) {
        AppDatabase.getDBInstance().generalDao().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, AppConstants.SEND_DB_TO_EMAIL);
        emailIntent.putExtra(Intent.EXTRA_CC, AppConstants.SEND_DB_CC_EMAIL);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, AppConstants.PROJECT_NAME + " Database - For Issue Monitoring");
        emailIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.PROJECT_NAME +
                " database upload from the device which has issues while uploading the data." +
                "This is just for testing/checking purpose.");
//        File file = activity.getDatabasePath(AppConstants.DATABASE_NAME);
        try {
            boolean isDirCreated = true, isFileCreated = true, isFileDeleted = true;
            String fileName = AppConstants.PROJECT_NAME + "_" + MainApp.user.getUsername() + "_" +
                    DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT) + ".sqlite3";
            File outputDir = new File(activity.getExternalFilesDir("backup").getPath());
            File outputFile = new File(outputDir, fileName);
            if (!outputDir.exists())
                // Create directory if not exists
                isDirCreated = outputDir.mkdirs();

            if (isDirCreated) {
                if (outputFile.exists())
                    // Delete file if already exists
                    isFileDeleted = outputFile.delete();

                // Create file if not exists
                isFileCreated = isFileDeleted && outputFile.createNewFile();
            }

            if (isFileCreated) {
                AppConstants.copyFile(activity.getDatabasePath(AppConstants.DATABASE_NAME), outputFile);

                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".file_provider", outputFile);
                } else {
                    uri = Uri.fromFile(outputFile);
                }
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                Intent chooser = Intent.createChooser(emailIntent, activity.getString(R.string.pick_email_provider));
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                activity.startActivity(chooser);
//            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.pick_email_provider)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
