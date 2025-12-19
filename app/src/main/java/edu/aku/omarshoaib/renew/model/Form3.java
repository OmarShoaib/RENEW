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
import edu.aku.omarshoaib.renew.database.dao.Form3Dao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form3.TABLE_NAME)
public class Form3 extends FormBaseModel {

    public static final String TABLE_NAME = "Form3";
    public static final String SECTION_NAME = "Sections: F3";
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

    // This variable is used to mark the form3 that its completed once.
    // To implement the logic of displaying 'Skip to End' button over
    // the sections if user open the form3 in edit mode, update any section/value,
    // save it and then directly skip to end without traversing the whole form3 again.
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
    private SF3 sF3;

    public Form3() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form3 = new Form3();
        MainApp.form3.setDistrictCode(MainApp.user.getDistId());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        Form3Dao formsDao = AppDatabase.getDBInstance().form3Dao();
        Form3 form3 = formsDao.getDataByScrId(MainApp.user.getDistId(), scrId);
        if (form3 != null) {
            MainApp.form3 = form3;
        } else {
            MainApp.form3.setUid(AppConstants.generateUid());
            MainApp.form3.setId(formsDao.add(MainApp.form3));
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

    public SF3 getSF3() {
        return sF3;
    }

    public void setSF3(SF3 sF3){
        this.sF3 = sF3;
    }

    /**
     * Form 2: Mental Health assessment (PHQ-9) Questionnaire
     */

    public static class SF3 extends BaseObservable {
        private String f301 = _EMPTY_;
        private String f302 = _EMPTY_;
        private String f303 = _EMPTY_;
        private String f304 = _EMPTY_;
        private String f305 = _EMPTY_;
        private String f306 = _EMPTY_;
        private String f3dd = _EMPTY_;
        private String f307 = _EMPTY_;
        private String f308 = _EMPTY_;
        private String f309 = _EMPTY_;
        private String f310 = _EMPTY_;
        private String f31001x = _EMPTY_;
        private String f31096x = _EMPTY_;
        private String f311 = _EMPTY_;
        private String f31196x = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF3> {
            public DataConverter() {
                super(new TypeToken<SF3>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF3 data) {
            MainApp.form3.setSF3(data);
            return AppDatabase.getDBInstance().form3Dao().update(MainApp.form3);
        }

        // Get section object by parsing json
        public static SF3 getData() {
            return MainApp.form3.getSF3();
        }

        /*Getter Setter*/
        @Bindable
        public String getF301() {
            return f301;
        }

        public void setF301(String f301) {
            this.f301 = f301;
            notifyPropertyChanged(BR.f301);
        }

        @Bindable
        public String getF302() {
            return f302;
        }

        public void setF302(String f302) {
            this.f302 = f302;
            notifyPropertyChanged(BR.f302);
        }

        @Bindable
        public String getF303() {
            return f303;
        }

        public void setF303(String f303) {
            this.f303 = f303;
            notifyPropertyChanged(BR.f303);
        }

        @Bindable
        public String getF304() {
            return f304;
        }

        public void setF304(String f304) {
            this.f304 = f304;
            notifyPropertyChanged(BR.f304);
        }

        @Bindable
        public String getF305() {
            return f305;
        }

        public void setF305(String f305) {
            this.f305 = f305;
            notifyPropertyChanged(BR.f305);
        }

        @Bindable
        public String getF306() {
            return f306;
        }

        public void setF306(String f306) {
            this.f306 = f306;
            notifyPropertyChanged(BR.f306);
        }

        @Bindable
        public String getF3dd() {
            return f3dd;
        }

        public void setF3dd(String f3dd) {
            this.f3dd = f3dd;
            notifyPropertyChanged(BR.f3dd);
        }

        @Bindable
        public String getF307() {
            return f307;
        }

        public void setF307(String f307) {
            this.f307 = f307;
            notifyPropertyChanged(BR.f307);
        }

        @Bindable
        public String getF308() {
            return f308;
        }

        public void setF308(String f308) {
            this.f308 = f308;
            notifyPropertyChanged(BR.f308);
        }

        @Bindable
        public String getF309() {
            return f309;
        }

        public void setF309(String f309) {
            this.f309 = f309;
            notifyPropertyChanged(BR.f309);
        }

        @Bindable
        public String getF310() {
            return f310;
        }

        public void setF310(String f310) {
            this.f310 = f310;
            setF31096x(f310.equals("96") ? this.f31096x: _EMPTY_);
            notifyPropertyChanged(BR.f310);
        }

        @Bindable
        public String getF31001x() {
            return f31001x;
        }

        public void setF31001x(String f31001x) {
            this.f31001x = f31001x;
            notifyPropertyChanged(BR.f31001x);
        }

        @Bindable
        public String getF31096x() {
            return f31096x;
        }

        public void setF31096x(String f31096x) {
            this.f31096x = f31096x;
            notifyPropertyChanged(BR.f31096x);
        }

        @Bindable
        public String getF311() {
            return f311;
        }

        public void setF311(String f311) {
            this.f311 = f311;
            setF31196x(f311.equals("96") ? this.f31196x: _EMPTY_);
            notifyPropertyChanged(BR.f311);
        }

        @Bindable
        public String getF31196x() {
            return f31196x;
        }

        public void setF31196x(String f31196x) {
            this.f31196x = f31196x;
            notifyPropertyChanged(BR.f31196x);
        }

    }


}
