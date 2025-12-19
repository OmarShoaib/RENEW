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
import edu.aku.omarshoaib.renew.database.dao.Form4Dao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form4.TABLE_NAME)
public class Form4 extends FormBaseModel {

    public static final String TABLE_NAME = "Form4";
    public static final String SECTION_NAME = "Sections: F4";
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

    // This variable is used to mark the form4 that its completed once.
    // To implement the logic of displaying 'Skip to End' button over
    // the sections if user open the form4 in edit mode, update any section/value,
    // save it and then directly skip to end without traversing the whole form4 again.
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
    private SF4 sF4;

    public Form4() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form4 = new Form4();
        MainApp.form4.setDistrictCode(MainApp.user.getDistId());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        Form4Dao formsDao = AppDatabase.getDBInstance().form4Dao();
        Form4 form4 = formsDao.getDataByScrId(MainApp.user.getDistId(), scrId);
        if (form4 != null) {
            MainApp.form4 = form4;
        } else {
            MainApp.form4.setUid(AppConstants.generateUid());
            MainApp.form4.setId(formsDao.add(MainApp.form4));
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

    public SF4 getSF4() {
        return sF4;
    }

    public void setSF4(SF4 sF4){
        this.sF4 = sF4;
    }

    /**
     * Form 2: Mental Health assessment (PHQ-9) Questionnaire
     */

    public static class SF4 extends BaseObservable {
        private String f401 = _EMPTY_;
        private String f402 = _EMPTY_;
        private String f403 = _EMPTY_;
        private String f404 = _EMPTY_;
        private String f405 = _EMPTY_;
        private String f406 = _EMPTY_;
        private String f407dd = _EMPTY_;
        private String f407mm = _EMPTY_;
        private String f408 = _EMPTY_;
        private String f409 = _EMPTY_;
        private String f410 = _EMPTY_;
        private String f411 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF4> {
            public DataConverter() {
                super(new TypeToken<SF4>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF4 data) {
            MainApp.form4.setSF4(data);
            return AppDatabase.getDBInstance().form4Dao().update(MainApp.form4);
        }

        // Get section object by parsing json
        public static SF4 getData() {
            return MainApp.form4.getSF4();
        }

        @Bindable
        public String getF401() {
            return f401;
        }

        public void setF401(String f401) {
            this.f401 = f401;
            notifyPropertyChanged(BR.f401);
        }

        @Bindable
        public String getF402() {
            return f402;
        }

        public void setF402(String f402) {
            this.f402 = f402;
            notifyPropertyChanged(BR.f402);
        }

        @Bindable
        public String getF403() {
            return f403;
        }

        public void setF403(String f403) {
            this.f403 = f403;
            notifyPropertyChanged(BR.f403);
        }

        @Bindable
        public String getF404() {
            return f404;
        }

        public void setF404(String f404) {
            this.f404 = f404;
            notifyPropertyChanged(BR.f404);
        }

        @Bindable
        public String getF405() {
            return f405;
        }

        public void setF405(String f405) {
            this.f405 = f405;
            notifyPropertyChanged(BR.f405);
        }

        @Bindable
        public String getF406() {
            return f406;
        }

        public void setF406(String f406) {
            this.f406 = f406;
            notifyPropertyChanged(BR.f406);
        }

        @Bindable
        public String getF407dd() {
            return f407dd;
        }

        public void setF407dd(String f407dd) {
            this.f407dd = f407dd;
            notifyPropertyChanged(BR.f407dd);
        }

        @Bindable
        public String getF407mm() {
            return f407mm;
        }

        public void setF407mm(String f407mm) {
            this.f407mm = f407mm;
            notifyPropertyChanged(BR.f407mm);
        }

        @Bindable
        public String getF408() {
            return f408;
        }

        public void setF408(String f408) {
            this.f408 = f408;
            notifyPropertyChanged(BR.f408);
        }

        @Bindable
        public String getF409() {
            return f409;
        }

        public void setF409(String f409) {
            this.f409 = f409;
            notifyPropertyChanged(BR.f409);
        }

        @Bindable
        public String getF410() {
            return f410;
        }

        public void setF410(String f410) {
            this.f410 = f410;
            notifyPropertyChanged(BR.f410);
        }

        @Bindable
        public String getF411() {
            return f411;
        }

        public void setF411(String f411) {
            this.f411 = f411;
            notifyPropertyChanged(BR.f411);
        }

    }


}
