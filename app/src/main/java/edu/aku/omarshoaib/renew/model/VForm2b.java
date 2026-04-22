package edu.aku.omarshoaib.renew.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class VForm2b {

    public static String TABLE_NAME = "vw_Form2a";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private long id;

    @SerializedName("participant_id")
    private String participantId;

    @SerializedName("_uid")
    private String uid;

    @SerializedName("participant_name")
    private String participantName;

    @SerializedName("father_name")
    private String fatherName;

    private String age;

    @SerializedName("contact_number")
    private String contactNumber;

    @SerializedName("hcf_id")
    private String hcfId;

    @SerializedName("village_address")
    private String villageAddress;

    @SerializedName("staff")
    private String enteryUser;

    @SerializedName("screening_date")
    private String screeningDate;

    @SerializedName("FS_PHQ9")
    private String fieldStaffPhq9Score;

    @SerializedName("ref_date")
    private String refrenceDate;

    @SerializedName("Ref_PHQ9")
    private String psycologistPhq9Score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getHcfId() {
        return hcfId;
    }

    public void setHcfId(String hcfId) {
        this.hcfId = hcfId;
    }

    public String getVillageAddress() {
        return villageAddress;
    }

    public void setVillageAddress(String villageAddress) {
        this.villageAddress = villageAddress;
    }

    public String getEnteryUser() {
        return enteryUser;
    }

    public void setEnteryUser(String enteryUser) {
        this.enteryUser = enteryUser;
    }

    public String getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(String screeningDate) {
        this.screeningDate = screeningDate;
    }

    public String getFieldStaffPhq9Score() {
        return fieldStaffPhq9Score;
    }

    public void setFieldStaffPhq9Score(String fieldStaffPhq9Score) {
        this.fieldStaffPhq9Score = fieldStaffPhq9Score;
    }

    public String getRefrenceDate() {
        return refrenceDate;
    }

    public void setRefrenceDate(String refrenceDate) {
        this.refrenceDate = refrenceDate;
    }

    public String getPsycologistPhq9Score() {
        return psycologistPhq9Score;
    }

    public void setPsycologistPhq9Score(String psycologistPhq9Score) {
        this.psycologistPhq9Score = psycologistPhq9Score;
    }
}
