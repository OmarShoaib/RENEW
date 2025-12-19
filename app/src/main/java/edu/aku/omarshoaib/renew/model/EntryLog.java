package edu.aku.omarshoaib.renew.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity
public class EntryLog {

    public static String TABLE_NAME = "EntryLog";

    public EntryLog() {
    }

    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private int id;

    @SerializedName("_uid")
    private String uid;

    private String projectName;

    @SerializedName("_uuid")
    private String uuid;

    private String username;

    @SerializedName("sysdate")
    private String sysDate;

    private String entryDate;

    @SerializedName("appversion")
    private String appVersion;

    @SerializedName("entry_type")
    private String entryType;

    @SerializedName("deviceid")
    private String deviceId;

    private String synced;

    @SerializedName("sync_date")
    private String syncDate;

    // For local use
    // This is used for resolving data while posting
    @ColumnInfo(defaultValue = "0")
    private transient boolean isError;

    public static void initEntryLog(AppDatabase appDatabase, String entryType, String username) {
        EntryLog entryLog = new EntryLog();
        entryLog.setUid(AppConstants.generateUid());
        entryLog.setProjectName(AppConstants.PROJECT_NAME);
        entryLog.setSysDate(DateUtils.getCurrentDateTime());
        entryLog.setEntryDate(DateUtils.getCurrentDateTime());
        entryLog.setEntryType(entryType);
        entryLog.setAppVersion(MainApp.appInfo.getAppVersion());
        entryLog.setDeviceId(AppConstants.DEVICE_ID);
        entryLog.setUsername(username);
        // Add entry log to db
        appDatabase.entryLogDao().add(entryLog);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSysDate() {
        return sysDate;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
