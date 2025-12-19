package edu.aku.omarshoaib.renew.model;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;

public class FormBaseModel {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private long id;

    @SerializedName("_uid")
    private String uid = _EMPTY_;

    private String projectName = _EMPTY_;

    @SerializedName("username")
    private String username = _EMPTY_;

    @SerializedName("sysdate")
    private String sysDate = _EMPTY_;

    @SerializedName("deviceid")
    private String deviceId = _EMPTY_;

    @SerializedName("deviceName")
    private String deviceName = _EMPTY_;

    @SerializedName("appversion")
    private String appVersion = _EMPTY_;

    @SerializedName("istatus")
    private String iStatus = _EMPTY_;

    @SerializedName("istatus96x")
    private String iStatus96x = _EMPTY_;

    @SerializedName("synced")
    private String synced = _EMPTY_;

    @SerializedName("sync_date")
    private String syncDate = _EMPTY_;

    @ColumnInfo(defaultValue = "0")
    private transient boolean isError;

    public FormBaseModel() {
        setSysDate(DateUtils.getCurrentDateTime());
        setDeviceId(AppConstants.DEVICE_ID);
        setDeviceName(AppConstants.DEVICE_NAME);
        setAppVersion(MainApp.appInfo.getAppVersion());
        setProjectName(AppConstants.PROJECT_NAME);
        if (MainApp.user != null)
            setUsername(MainApp.user.getUsername());
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIStatus() {
        return iStatus;
    }

    public void setIStatus(String iStatus) {
        this.iStatus = iStatus;
    }

    public String getIStatus96x() {
        return iStatus96x;
    }

    public void setIStatus96x(String iStatus96x) {
        this.iStatus96x = iStatus96x;
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
