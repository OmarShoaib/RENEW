package edu.aku.omarshoaib.renew.global;

import android.app.Activity;

import androidx.sqlite.db.SimpleSQLiteQuery;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;

public class BackupDB {

    /**
     * Backup DB
     * - Single backup per day
     * - It will replace previous file if backup on the same day
     * - Returns output file
     */

    public static File backup(Activity activity) {
        AppDatabase.getDBInstance().generalDao().checkpoint(new SimpleSQLiteQuery("pragma wal_checkpoint(full)"));

        try {
            boolean isDirCreated = true, isFileCreated = true, isFileDeleted = true;
            String fileName = AppConstants.PROJECT_NAME + "_" +
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
                // Check and delete oldest file (if any)
                deleteOldestBackup(outputDir);

                return outputFile;
            } else
                // Path/Directory not created
                AppConstants.showSimpleSnackBar(activity, activity.getString(R.string.dir_not_created),
                        AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        } catch (IOException e) {
            // Error
            e.printStackTrace();
            AppConstants.showSimpleSnackBar(activity, activity.getString(R.string.somethings_not_right),
                    AppConstants.MSG_DURATION, AppConstants.TYPE_ERROR);
        }
        return null;
    }

    // Check and delete oldest backup file (if any) after specified count
    private static void deleteOldestBackup(File outputDir) {
        File[] files = outputDir.listFiles();
        if (Objects.requireNonNull(files).length > AppConstants.DB_BACKUP_COUNT) {
            // Sort files array with respect to last modified date in ASC order
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            // Delete 1st file because after ordering, the 1st file is oldest
            files[0].delete();
        }
    }

}
