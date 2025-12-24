package edu.aku.omarshoaib.renew.model;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import edu.aku.omarshoaib.renew.BR;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.database.dao.Form1Dao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form1.TABLE_NAME)
public class Form1 extends FormBaseModel {

    public static final String TABLE_NAME = "Form1";
    public static final String SECTION_NAME = "Sections: F1, F1A";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("village_name")
    private String villageName = _EMPTY_;

    @SerializedName("scr_id")
    private String scrId = _EMPTY_;

    @SerializedName("ending_date")
    private String endingDate = _EMPTY_;

    // This variable is used to mark the form1 that its completed once.
    // To implement the logic of displaying 'Skip to End' button over
    // the sections if user open the form1 in edit mode, update any section/value,
    // save it and then directly skip to end without traversing the whole form1 again.
    // Note: User must save the updated value by clicking on usual save btn first then
    // can click on skip to end on the next activity if don't want to edit any other value.
    @ColumnInfo(defaultValue = "0")
    private transient boolean isFormCompleteOnce;

    // For enabling GPS
   /* public String gLat = SharedPrefs.read(SharedPrefs.GPS_LAT, _EMPTY_);
    public String gLon = SharedPrefs.read(SharedPrefs.GPS_LON, _EMPTY_);
    public String gAcc = SharedPrefs.read(SharedPrefs.GPS_ACC, _EMPTY_);
    public String gDate = SharedPrefs.read(SharedPrefs.GPS_DATE, _EMPTY_);
    public String gPerm = GPSLocation.GPS_PERMISSION;
    public String gAvail = GPSLocation.GPS_AVAILABLE;*/

    /*JSON OBJECTS*/
    private SF1 sF1;

    public Form1() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form1 = new Form1();
        MainApp.form1.setDistrictCode(MainApp.user.getDistId());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        Form1Dao formsDao = AppDatabase.getDBInstance().form1Dao();
        Form1 form1 = formsDao.getDataByScrId(MainApp.user.getDistId(), scrId);
        if (form1 != null) {
            MainApp.form1 = form1;
        } else {
            MainApp.form1.setUid(AppConstants.generateUid());
            MainApp.form1.setId(formsDao.add(MainApp.form1));
        }
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getScrId() {
        return scrId;
    }

    public void setScrId(String scrId) {
        this.scrId = scrId;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public boolean isFormCompleteOnce() {
        return isFormCompleteOnce;
    }

    public void setFormCompleteOnce(boolean formCompleteOnce) {
        isFormCompleteOnce = formCompleteOnce;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public SF1 getSF1() {
        return sF1;
    }

    public void setSF1(SF1 sF) {
        this.sF1 = sF;
    }

    /**
     * Form 1: Attendance Sheet For Health Education Session's Participants
     */

    public static class SF1 extends BaseObservable {
        private String dist = _EMPTY_;
        private String scrid = _EMPTY_;
        private String f101 = _EMPTY_;
        private String f102 = _EMPTY_;
        private String f103 = _EMPTY_;
        private String f104 = _EMPTY_;
        private String f105 = _EMPTY_;
        private String f106 = _EMPTY_;
        private String f107 = _EMPTY_;
        private String f108 = _EMPTY_;
        private String f109 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF1> {
            public DataConverter() {
                super(new TypeToken<SF1>() {
                }.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF1 data) {
            MainApp.form1.setSF1(data);
            return AppDatabase.getDBInstance().form1Dao().update(MainApp.form1);
        }

        // Get section object by parsing json
        public static SF1 getData() {
            return MainApp.form1.getSF1();
        }

        @Bindable
        public String getDist() {
            return dist;
        }

        public void setDist(String dist) {
            this.dist = dist;
            notifyPropertyChanged(BR.dist);
        }

        @Bindable
        public String getScrid() {
            return scrid;
        }

        public void setScrid(String scrid) {
            this.scrid = scrid;
            notifyPropertyChanged(BR.scrid);
        }

        @Bindable
        public String getF101() {
            return f101;
        }

        public void setF101(String f101) {
            this.f101 = f101;
            notifyPropertyChanged(BR.f101);
        }

        @Bindable
        public String getF102() {
            return f102;
        }

        public void setF102(String f102) {
            this.f102 = f102;
            notifyPropertyChanged(BR.f102);
        }

        @Bindable
        public String getF103() {
            return f103;
        }

        public void setF103(String f103) {
            this.f103 = f103;
            notifyPropertyChanged(BR.f103);
        }

        @Bindable
        public String getF104() {
            return f104;
        }

        public void setF104(String f104) {
            this.f104 = f104;
            notifyPropertyChanged(BR.f104);
        }

        @Bindable
        public String getF105() {
            return f105;
        }

        public void setF105(String f105) {
            this.f105 = f105;
            notifyPropertyChanged(BR.f105);
        }

        @Bindable
        public String getF106() {
            return f106;
        }

        public void setF106(String f106) {
            this.f106 = f106;
            setF107(f106.equals("1") ? this.f107 : _EMPTY_);
            setF108(f106.equals("1") ? this.f108 : _EMPTY_);
            notifyPropertyChanged(BR.f106);
        }

        @Bindable
        public String getF107() {
            return f107;
        }

        public void setF107(String f107) {
            this.f107 = f107;
            notifyPropertyChanged(BR.f107);
        }

        @Bindable
        public String getF108() {
            return f108;
        }

        public void setF108(String f108) {
            this.f108 = f108;
            setF109(f108.equals("1") ? this.f109 : _EMPTY_);
            notifyPropertyChanged(BR.f108);
        }

        @Bindable
        public String getF109() {
            return f109;
        }

        public void setF109(String f109) {
            this.f109 = f109;
            notifyPropertyChanged(BR.f109);
        }

    }

}
