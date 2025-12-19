package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import edu.aku.omarshoaib.renew.database.AppDatabase;

public class ImportDB {
    public static final int REQUEST_CODE_IMPORT_DB = 2001;
    private static final String DB_NAME = AppConstants.DATABASE_NAME;
    private static final String PASSWORD = new String(AppConstants.IBAHC.toCharArray());

    public static void startFilePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                "application/x-sqlite3",
                "application/octet-stream",
                "application/vnd.sqlite3"
        });
        activity.startActivityForResult(intent, REQUEST_CODE_IMPORT_DB);
    }

    public static void importDatabaseFromUri(Context context, Uri uri) {
        try {
            // Close Room instance
            AppDatabase.closeInstance();

            File dbFile = context.getDatabasePath(DB_NAME);
            // Remove WAL and SHM files if exist
            new File(dbFile.getPath() + "-wal").delete();
            new File(dbFile.getPath() + "-shm").delete();

            // Copy imported file to a temp file in DB folder
            File tempFile = new File(dbFile.getParent(), "_temp_import.db");
            try (InputStream in = context.getContentResolver().openInputStream(uri);
                 OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

            // Validate tempFile size (non-empty)
            if (tempFile.length() == 0) {
                tempFile.delete();
                Toast.makeText(context, "Selected file is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // to check the project name
            String projectName = getProjectNameFromImportedDB(context, tempFile, PASSWORD);
            if (projectName == null || !projectName.equals(AppConstants.PROJECT_NAME)) {
                Toast.makeText(context, "Imported DB does not match current project!", Toast.LENGTH_LONG).show();
                return;
            }

            if (dbFile.exists()) dbFile.delete();
            if (!tempFile.renameTo(dbFile)) {
                // fallback manual copy
                try (InputStream in = new FileInputStream(tempFile);
                     OutputStream out = new FileOutputStream(dbFile)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                tempFile.delete();
            }
            Toast.makeText(context, "Database imported successfully!", Toast.LENGTH_SHORT).show();
            restartApp(context);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to import database", Toast.LENGTH_LONG).show();
        }
    }

    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if (context instanceof Activity) ((Activity) context).finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    public static String getProjectNameFromImportedDB(Context context, File dbFile, String password) throws Exception {
        SQLiteDatabase.loadLibs(context);

        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbFile.getAbsolutePath(),
                password.toCharArray(),
                null,
                SQLiteDatabase.OPEN_READONLY
        );

        String projectName = null;
        try (Cursor cursor = db.rawQuery("SELECT projectName FROM EntryLog LIMIT 1", null)) {
            if (cursor.moveToFirst()) {
                projectName = cursor.getString(0);
            }
        }
        db.close();
        return projectName;
    }
}
