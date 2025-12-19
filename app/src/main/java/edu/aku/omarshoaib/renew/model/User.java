package edu.aku.omarshoaib.renew.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {

    public static String TABLE_NAME = "Users";

    public User() {
    }

    @PrimaryKey
    @SerializedName("id")
    private long userId;

    private String username;
    private String password;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("isNewUser")
    private String newUser;

    @SerializedName("auth_level")
    private int authLevel;

    private int enabled;
    private String designation;

    @SerializedName("dist_id")
    private String distId;

    @SerializedName("uccode")
    private String ucCode;

    private String passwordEnc;
    private SyncModel.ResponseDate pwdExpiry;   // Json Object

    private int attempt;
    private SyncModel.ResponseDate attemptDateTime;     // Json Object

    @SerializedName("lastPwd_dt")
    private String lastPwdDT;

    private String createdBy;
    private SyncModel.ResponseDate createdDateTime;   // Json Object

    private String updateBy;
    private SyncModel.ResponseDate updatedDateTime;   // Json Object

    private String deleteBy;
    private SyncModel.ResponseDate deletedDateTime;   // Json Object

    private String vType;

    // To enable/disable SendDB feature dynamically
    // 0 = Disable
    // 1 = Enable
    private int isSendDB;

    @SerializedName("colflag")
    private String colFlag;

    @SerializedName("hfcode")
    private String hfCode;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public int getIsSendDB() {
        return isSendDB;
    }

    public void setIsSendDB(int isSendDB) {
        this.isSendDB = isSendDB;
    }

    public int getAuthLevel() {
        return authLevel;
    }

    public void setAuthLevel(int authLevel) {
        this.authLevel = authLevel;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDistId() {
        return distId;
    }

    public void setDistId(String distId) {
        this.distId = distId;
    }

    public String getUcCode() {
        return ucCode;
    }

    public void setUcCode(String ucCode) {
        this.ucCode = ucCode;
    }

    public String getPasswordEnc() {
        return passwordEnc;
    }

    public void setPasswordEnc(String passwordEnc) {
        this.passwordEnc = passwordEnc;
    }

    public SyncModel.ResponseDate getPwdExpiry() {
        return pwdExpiry;
    }

    public void setPwdExpiry(SyncModel.ResponseDate pwdExpiry) {
        this.pwdExpiry = pwdExpiry;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public SyncModel.ResponseDate getAttemptDateTime() {
        return attemptDateTime;
    }

    public void setAttemptDateTime(SyncModel.ResponseDate attemptDateTime) {
        this.attemptDateTime = attemptDateTime;
    }

    public String getLastPwdDT() {
        return lastPwdDT;
    }

    public void setLastPwdDT(String lastPwdDT) {
        this.lastPwdDT = lastPwdDT;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public SyncModel.ResponseDate getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(SyncModel.ResponseDate createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public SyncModel.ResponseDate getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(SyncModel.ResponseDate updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public SyncModel.ResponseDate getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(SyncModel.ResponseDate deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    public String getVType() {
        return vType;
    }

    public void setVType(String vType) {
        this.vType = vType;
    }

    public String getColFlag() {
        return colFlag;
    }

    public void setColFlag(String colFlag) {
        this.colFlag = colFlag;
    }

    public String getHfCode() {
        return hfCode;
    }

    public void setHfCode(String hfCode) {
        this.hfCode = hfCode;
    }
}
