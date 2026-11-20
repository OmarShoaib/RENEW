package edu.aku.omarshoaib.renew.model;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Cluster {

    public static String TABLE_NAME = "Clusters";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    private long id;

    @SerializedName("col_id")
    private long colId;

    @SerializedName("col_dt")
    private SyncModel.ResponseDate colDT;     // Json Object

    @SerializedName("cluster")
    private String clusterNo = _EMPTY_;

    @SerializedName("geoarea")
    private String geoArea = _EMPTY_;

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;
    @SerializedName("district")
    private String districtName = _EMPTY_;

    @SerializedName("teh_code")
    private String tehsilCode = _EMPTY_;
    @SerializedName("tehsil")
    private String tehsilName = _EMPTY_;

    @SerializedName("hf_code")
    private String hfCode = _EMPTY_;
    @SerializedName("hf_name")
    private String hfName = _EMPTY_;

    private String lhs = _EMPTY_;

    private String lhw = _EMPTY_;

    private String group = _EMPTY_;

    @SerializedName("uc_id")
    private String ucCode = _EMPTY_;
    @SerializedName("uc")
    private String ucName = _EMPTY_;

    @SerializedName("village")
    private String villageName = _EMPTY_;

    public Cluster() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getColId() {
        return colId;
    }

    public void setColId(long colId) {
        this.colId = colId;
    }

    public SyncModel.ResponseDate getColDT() {
        return colDT;
    }

    public void setColDT(SyncModel.ResponseDate colDT) {
        this.colDT = colDT;
    }

    public String getClusterNo() {
        return clusterNo;
    }

    public void setClusterNo(String clusterNo) {
        this.clusterNo = clusterNo;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
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

    public String getLhs() {
        return lhs;
    }

    public void setLhs(String lhs) {
        this.lhs = lhs;
    }

    public String getLhw() {
        return lhw;
    }

    public void setLhw(String lhw) {
        this.lhw = lhw;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

}
