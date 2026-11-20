package edu.aku.omarshoaib.renew.model;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = BLRandomized.TABLE_NAME)
public class BLRandomized {

    public static final String TABLE_NAME = "bl_randomised";

    @PrimaryKey
    @SerializedName("_id")
    private long id;

    @SerializedName("sno")
    private String sNo = _EMPTY_;

    @SerializedName("randDT")
    private SyncModel.ResponseDate randomizedDT;

    // Cluster No
    @SerializedName("clustercode")
    private String clusterNo = _EMPTY_;

    // Listing Uid
    @SerializedName("luid")
    private String luid = _EMPTY_;

    // Tab No.
    @SerializedName("hltab")
    private String tabNo = _EMPTY_;

    // HHId
    @SerializedName("hhid")
    private String hhId = _EMPTY_;

    // Comp. Id
    @SerializedName("compid")
    private String compId = _EMPTY_;

    // HH Head Name
    @SerializedName("head")
    private String hhHead = _EMPTY_;

    // District Id
    @SerializedName("dist_id")
    private String districtId = _EMPTY_;

    // Area
    @SerializedName("area")
    private String geoArea = _EMPTY_;

    // Username
    @SerializedName("user_id")
    private String username = _EMPTY_;

    public BLRandomized() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSNo() {
        return sNo;
    }

    public void setSNo(String sNo) {
        this.sNo = sNo;
    }

    public SyncModel.ResponseDate getRandomizedDT() {
        return randomizedDT;
    }

    public void setRandomizedDT(SyncModel.ResponseDate randomizedDT) {
        this.randomizedDT = randomizedDT;
    }

    public String getClusterNo() {
        return clusterNo;
    }

    public void setClusterNo(String clusterNo) {
        this.clusterNo = clusterNo;
    }

    public String getLuid() {
        return luid;
    }

    public void setLuid(String luid) {
        this.luid = luid;
    }

    public String getTabNo() {
        return tabNo;
    }

    public void setTabNo(String tabNo) {
        this.tabNo = tabNo;
    }

    public String getHhId() {
        return hhId;
    }

    public void setHhId(String hhId) {
        this.hhId = hhId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getHhHead() {
        return hhHead;
    }

    public void setHhHead(String hhHead) {
        this.hhHead = hhHead;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getGeoArea() {
        return geoArea;
    }

    public void setGeoArea(String geoArea) {
        this.geoArea = geoArea;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
