package edu.aku.omarshoaib.renew.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = VFormF06.TABLE_NAME)
public class VFormF06 {
    public final static String TABLE_NAME = "vw_Form6";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private long id;

    @SerializedName("participant_id")
    private String participantId;

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
}
