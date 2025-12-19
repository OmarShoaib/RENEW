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

    @SerializedName("cluster_no")
    private String clusterNo = _EMPTY_;

    @SerializedName("geoarea")
    private String geoArea = _EMPTY_;

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;
    @SerializedName("district")
    private String districtName = _EMPTY_;

    @SerializedName("tehsil_id")
    private String tehsilCode = _EMPTY_;
    @SerializedName("tehsil_name")
    private String tehsilName = _EMPTY_;

    @SerializedName("uc_id")
    private String ucCode = _EMPTY_;
    @SerializedName("uc_name")
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
