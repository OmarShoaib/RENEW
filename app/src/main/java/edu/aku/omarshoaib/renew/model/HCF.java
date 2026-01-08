package edu.aku.omarshoaib.renew.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class HCF {

    public static String TABLE_NAME = "HCF";

    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("dist_id")
    private String distCode;

    @SerializedName("district")
    private String distName;

    @SerializedName("tehsil_code")
    private String tehsilCode;

    @SerializedName("tehsil")
    private String tehsilName;

    @SerializedName("uc_code")
    private String ucCode;

    @SerializedName("uc")
    private String ucName;

    @SerializedName("hf_code")
    private String hfCode;

    @SerializedName("hf_name")
    private String hfName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getDistName() {
        return distName;
    }

    public void setDistName(String distName) {
        this.distName = distName;
    }

    public String getTehsilCode() {
        return tehsilCode;
    }

    public void setTehsilCode(String tehsilCode) {
        this.tehsilCode = tehsilCode;
    }

    public String getTehsilName() {
        return tehsilName;
    }

    public void setTehsilName(String tehsilName) {
        this.tehsilName = tehsilName;
    }

    public String getUcCode() {
        return ucCode;
    }

    public void setUcCode(String ucCode) {
        this.ucCode = ucCode;
    }

    public String getUcName() {
        return ucName;
    }

    public void setUcName(String ucName) {
        this.ucName = ucName;
    }

    public String getHfCode() {
        return hfCode;
    }

    public void setHfCode(String hfCode) {
        this.hfCode = hfCode;
    }

    public String getHfName() {
        return hfName;
    }

    public void setHfName(String hfName) {
        this.hfName = hfName;
    }

    @NonNull
    @Override
    public String toString() {
        return hfCode.trim()+" - "+hfName.trim();
    }
}
