package edu.aku.omarshoaib.renew.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = VFormF06.TABLE_NAME)
public class VFormF06 {
    public final static String TABLE_NAME = "vw_form6";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private long id;

    @SerializedName("participant_id")
    private String participantId;

    @SerializedName("_uid")
    private String uid;

    @SerializedName("enrol_date")
    private String enrollmentDate;

    @SerializedName("child_name")
    private String childName;

    @SerializedName("visit_date")
    private String visitDate;

    @SerializedName("visit_number")
    private String visitNumber;

    @SerializedName("dob")
    private String dob;

    @SerializedName("father_caregiver")
    private String fatherName;

    @SerializedName("contact")
    private String contactNo;

    @SerializedName("sachet")
    private String noOfSachets;

    @SerializedName("last_visit")
    private String lastVisitF604a;

    @SerializedName("f515b")
    private String type;

    private String village;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getNoOfSachets() {
        return noOfSachets;
    }

    public void setNoOfSachets(String noOfSachets) {
        this.noOfSachets = noOfSachets;
    }

    public String getLastVisitF604a() {
        return lastVisitF604a;
    }

    public void setLastVisitF604a(String lastVisitF604a) {
        this.lastVisitF604a = lastVisitF604a;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}