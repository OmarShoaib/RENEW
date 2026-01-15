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

    @SerializedName("pregnant_woman_id")
    private String pregnantWomanId = _EMPTY_;

    @SerializedName("_uuid")
    private String uuid = _EMPTY_;

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
        MainApp.form3.setScrId(MainApp.form1.getScrId());
        MainApp.form3.setVillageName(MainApp.form1.getVillageName());
        MainApp.form3.setUuid(MainApp.participant.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        Form3Dao formsDao = AppDatabase.getDBInstance().form3Dao();
        Form3 form3 = formsDao.getDataByUuid(MainApp.participant.getUid(), scrId);
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

    public String getPregnantWomanId() {
        return pregnantWomanId;
    }

    public void setPregnantWomanId(String pregnantWomanId) {
        this.pregnantWomanId = pregnantWomanId;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        private String f309dk = _EMPTY_;
        private String f310 = _EMPTY_;
        private String f31001x = _EMPTY_;
        private String f31096x = _EMPTY_;
        private String f311 = _EMPTY_;
        private String f31196x = _EMPTY_;
        private String f31301 = _EMPTY_;
        private String f31302 = _EMPTY_;
        private String f31303 = _EMPTY_;
        private String f31304 = _EMPTY_;
        private String f31305 = _EMPTY_;
        private String f31306 = _EMPTY_;
        private String f31307 = _EMPTY_;
        private String f31308 = _EMPTY_;
        private String f31309 = _EMPTY_;
        private String f31310 = _EMPTY_;
        private String f31311 = _EMPTY_;
        private String f31398 = _EMPTY_;
        private String f315 = _EMPTY_;
        private String f316 = _EMPTY_;

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
            if(!f307.equals("1")) {
                setF308(_EMPTY_);
                setF309(_EMPTY_);
                setF309dk(_EMPTY_);
                setF310(_EMPTY_);
                setF311(_EMPTY_);
                setF31301(_EMPTY_);
                setF31302(_EMPTY_);
                setF31303(_EMPTY_);
                setF31304(_EMPTY_);
                setF31305(_EMPTY_);
                setF31306(_EMPTY_);
                setF31307(_EMPTY_);
                setF31308(_EMPTY_);
                setF31309(_EMPTY_);
                setF31310(_EMPTY_);
                setF31311(_EMPTY_);
            }
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
        public String getF309dk() {
            return f309dk;
        }

        public void setF309dk(String f309dk) {
            this.f309dk = f309dk;
            setF309(f309dk.equals("98") ? _EMPTY_ : this.f309);
            notifyPropertyChanged(BR.f309dk);
        }

        @Bindable
        public String getF310() {
            return f310;
        }

        public void setF310(String f310) {
            this.f310 = f310;
            setF31096x(f310.equals("96") ? this.f31096x: _EMPTY_);
            setF31001x(f310.equals("1") ? this.f31001x: _EMPTY_);
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

        @Bindable
        public String getF31301() {
            return f31301;
        }

        public void setF31301(String f31301) {
            this.f31301 = f31301;
            notifyPropertyChanged(BR.f31301);
        }

        @Bindable
        public String getF31302() {
            return f31302;
        }

        public void setF31302(String f31302) {
            this.f31302 = f31302;
            notifyPropertyChanged(BR.f31302);
        }

        @Bindable
        public String getF31303() {
            return f31303;
        }

        public void setF31303(String f31303) {
            this.f31303 = f31303;
            notifyPropertyChanged(BR.f31303);
        }

        @Bindable
        public String getF31304() {
            return f31304;
        }

        public void setF31304(String f31304) {
            this.f31304 = f31304;
            notifyPropertyChanged(BR.f31304);
        }

        @Bindable
        public String getF31305() {
            return f31305;
        }

        public void setF31305(String f31305) {
            this.f31305 = f31305;
            notifyPropertyChanged(BR.f31305);
        }

        @Bindable
        public String getF31306() {
            return f31306;
        }

        public void setF31306(String f31306) {
            this.f31306 = f31306;
            notifyPropertyChanged(BR.f31306);
        }

        @Bindable
        public String getF31307() {
            return f31307;
        }

        public void setF31307(String f31307) {
            this.f31307 = f31307;
            notifyPropertyChanged(BR.f31307);
        }

        @Bindable
        public String getF31308() {
            return f31308;
        }

        public void setF31308(String f31308) {
            this.f31308 = f31308;
            notifyPropertyChanged(BR.f31308);
        }

        @Bindable
        public String getF31309() {
            return f31309;
        }

        public void setF31309(String f31309) {
            this.f31309 = f31309;
            notifyPropertyChanged(BR.f31309);
        }

        @Bindable
        public String getF31310() {
            return f31310;
        }

        public void setF31310(String f31310) {
            this.f31310 = f31310;
            notifyPropertyChanged(BR.f31310);
        }

        @Bindable
        public String getF31311() {
            return f31311;
        }

        public void setF31311(String f31311) {
            this.f31311 = f31311;
            notifyPropertyChanged(BR.f31311);
        }

        @Bindable
        public String getF31398() {
            return f31398;
        }

        public void setF31398(String f31398) {
            this.f31398 = f31398;
            setF31301(_EMPTY_);
            setF31302(_EMPTY_);
            setF31303(_EMPTY_);
            setF31304(_EMPTY_);
            setF31305(_EMPTY_);
            setF31306(_EMPTY_);
            setF31307(_EMPTY_);
            setF31308(_EMPTY_);
            setF31309(_EMPTY_);
            setF31310(_EMPTY_);
            setF31311(_EMPTY_);
            setF315(_EMPTY_);
            setF316(_EMPTY_);
            notifyPropertyChanged(BR.f31398);
        }

        @Bindable
        public String getF315() {
            return f315;
        }

        public void setF315(String f315) {
            this.f315 = f315;
            notifyPropertyChanged(BR.f315);
        }

        @Bindable
        public String getF316() {
            return f316;
        }

        public void setF316(String f316) {
            this.f316 = f316;
            notifyPropertyChanged(BR.f316);
        }
    }
}
