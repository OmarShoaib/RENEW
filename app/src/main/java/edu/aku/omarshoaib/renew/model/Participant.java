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
import edu.aku.omarshoaib.renew.database.dao.ParticipantDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;
import retrofit2.http.Part;

@Entity(tableName = Participant.TABLE_NAME)
public class Participant extends FormBaseModel{
    public static final String TABLE_NAME = "Participant";
    public static final String SECTION_NAME = "Sections: F1";
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

    @SerializedName("_uuid")
    private String uuid = _EMPTY_;

    @SerializedName("line_no")
    private int lineNo;

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

    // Init default data
    public static void initMeta(int lineNo) {
        // This is used to add record for the first time
        MainApp.participant = new Participant();
        MainApp.participant.setDistrictCode(MainApp.user.getDistId());
        MainApp.participant.setLineNo(lineNo);
        MainApp.participant.setUuid(MainApp.form1.getUid());
        MainApp.participant.setScrId(MainApp.form1.getScrId());
        MainApp.participant.setVillageName(MainApp.form1.getVillageName());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        ParticipantDao formsDao = AppDatabase.getDBInstance().participantDao();
        Participant form1 = formsDao.getDataByLineNo(MainApp.form1.getUid(),
                MainApp.participant.getLineNo());
        if (form1 != null) {
            MainApp.participant = form1;
        } else {
            MainApp.participant.setUid(AppConstants.generateUid());
            MainApp.participant.setId(formsDao.add(MainApp.participant));
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
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
        private String f104 = _EMPTY_;
        private String f105 = _EMPTY_;
        private String f106 = _EMPTY_;
        private String f107 = _EMPTY_;
        private String f108 = _EMPTY_;
        private String f109 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<Participant.SF1> {
            public DataConverter() {
                super(new TypeToken<Participant.SF1>() {
                }.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF1 data) {
            MainApp.participant.setSF1(data);
            return AppDatabase.getDBInstance().participantDao().update(MainApp.participant);
        }

        // Get section object by parsing json
        public static SF1 getData() {
            return MainApp.participant.getSF1();
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
