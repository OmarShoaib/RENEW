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
import edu.aku.omarshoaib.renew.database.dao.Form2Dao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form2.TABLE_NAME)
public class Form2 extends FormBaseModel {

    public static final String TABLE_NAME = "Form2";
    public static final String SECTION_NAME = "Sections: F2";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("village_name")
    private String villageName = _EMPTY_;

    @SerializedName("_uuid")
    private String uuId = _EMPTY_;

    @SerializedName("scr_id")
    private String scrId = _EMPTY_;

    @SerializedName("participant_id")
    private String participantId = _EMPTY_;

    @SerializedName("ending_date")
    private String endingDate = _EMPTY_;

    // This variable is used to mark the form2 that its completed once.
    // To implement the logic of displaying 'Skip to End' button over
    // the sections if user open the form2 in edit mode, update any section/value,
    // save it and then directly skip to end without traversing the whole form2 again.
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
    private SF2 sF2;

    public Form2() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form2 = new Form2();
        MainApp.form2.setDistrictCode(MainApp.user.getDistId());
        MainApp.form2.setScrId(MainApp.form1.getScrId());
        MainApp.form2.setVillageName(MainApp.form1.getVillageName());
        MainApp.form2.setUuId(MainApp.participant.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        Form2Dao formsDao = AppDatabase.getDBInstance().form2Dao();
        Form2 form2 = formsDao.getDataByUuid(MainApp.participant.getUid(), scrId);
        if (form2 != null) {
            MainApp.form2 = form2;
        } else {
            MainApp.form2.setUid(AppConstants.generateUid());
            MainApp.form2.setId(formsDao.add(MainApp.form2));
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

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
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

    public SF2 getSF2() {
        return sF2;
    }

    public void setSF2(SF2 sF2){
        this.sF2 = sF2;
    }

    /**
     * Form 2: Mental Health assessment (PHQ-9) Questionnaire
     */

    public static class SF2 extends BaseObservable {
        private String f201 = _EMPTY_;
        private String f202 = _EMPTY_;
        private String f203 = _EMPTY_;
        private String f204 = _EMPTY_;
        private String f205 = _EMPTY_;
        private String f206 = _EMPTY_;
        private String f207 = _EMPTY_;
        private String f208a = _EMPTY_;
        private String f208b = _EMPTY_;
        private String f208c = _EMPTY_;
        private String f208d = _EMPTY_;
        private String f208e = _EMPTY_;
        private String f208f = _EMPTY_;
        private String f208g = _EMPTY_;
        private String f208h = _EMPTY_;
        private String f208i = _EMPTY_;
        private String f209 = _EMPTY_;
        private String f210 = _EMPTY_;
        private String f211 = _EMPTY_;
        private String f212 = _EMPTY_;
        private String f213 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF2> {
            public DataConverter() {
                super(new TypeToken<SF2>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF2 data) {
            MainApp.form2.setSF2(data);
            return AppDatabase.getDBInstance().form2Dao().update(MainApp.form2);
        }

        // Get section object by parsing json
        public static SF2 getData() {
            return MainApp.form2.getSF2();
        }

        @Bindable
        public String getF201() {
            return f201;
        }

        public void setF201(String f201) {
            this.f201 = f201;
            notifyPropertyChanged(BR.f201);
        }

        @Bindable
        public String getF202() {
            return f202;
        }

        public void setF202(String f202) {
            this.f202 = f202;
            notifyPropertyChanged(BR.f202);
        }

        @Bindable
        public String getF203() {
            return f203;
        }

        public void setF203(String f203) {
            this.f203 = f203;
            notifyPropertyChanged(BR.f203);
        }

        @Bindable
        public String getF204() {
            return f204;
        }

        public void setF204(String f204) {
            this.f204 = f204;
            notifyPropertyChanged(BR.f204);
        }

        @Bindable
        public String getF205() {
            return f205;
        }

        public void setF205(String f205) {
            this.f205 = f205;
            notifyPropertyChanged(BR.f205);
        }

        @Bindable
        public String getF206() {
            return f206;
        }

        public void setF206(String f206) {
            this.f206 = f206;
            notifyPropertyChanged(BR.f206);
        }

        @Bindable
        public String getF207() {
            return f207;
        }

        public void setF207(String f207) {
            this.f207 = f207;
            notifyPropertyChanged(BR.f207);
        }

        @Bindable
        public String getF208a() {
            return f208a;
        }

        public void setF208a(String f208a) {
            this.f208a = f208a;
            calculateF209();
            notifyPropertyChanged(BR.f208a);
        }

        @Bindable
        public String getF208b() {
            return f208b;
        }

        public void setF208b(String f208b) {
            this.f208b = f208b;
            calculateF209();
            notifyPropertyChanged(BR.f208b);
        }

        @Bindable
        public String getF208c() {
            return f208c;
        }

        public void setF208c(String f208c) {
            this.f208c = f208c;
            calculateF209();
            notifyPropertyChanged(BR.f208c);
        }

        @Bindable
        public String getF208d() {
            return f208d;
        }

        public void setF208d(String f208d) {
            this.f208d = f208d;
            calculateF209();
            notifyPropertyChanged(BR.f208d);
        }

        @Bindable
        public String getF208e() {
            return f208e;
        }

        public void setF208e(String f208e) {
            this.f208e = f208e;
            calculateF209();
            notifyPropertyChanged(BR.f208e);
        }

        @Bindable
        public String getF208f() {
            return f208f;
        }

        public void setF208f(String f208f) {
            this.f208f = f208f;
            calculateF209();
            notifyPropertyChanged(BR.f208f);
        }

        @Bindable
        public String getF208g() {
            return f208g;
        }

        public void setF208g(String f208g) {
            this.f208g = f208g;
            calculateF209();
            notifyPropertyChanged(BR.f208g);
        }

        @Bindable
        public String getF208h() {
            return f208h;
        }

        public void setF208h(String f208h) {
            this.f208h = f208h;
            calculateF209();
            notifyPropertyChanged(BR.f208h);
        }

        @Bindable
        public String getF208i() {
            return f208i;
        }

        public void setF208i(String f208i) {
            this.f208i = f208i;
            calculateF209();
            notifyPropertyChanged(BR.f208i);
        }

        @Bindable
        public String getF209() {
            return f209;
        }

        public void setF209(String f209) {
            this.f209 = f209;
            setF210(getSeverityLevel());
            notifyPropertyChanged(BR.f209);
        }

        private void calculateF209() {
            int sum = 0;
            boolean hasAnyValue = false;

            String[] fields = {f208a, f208b, f208c, f208d, f208e,
                    f208f, f208g, f208h, f208i};

            for (String field : fields) {
                if (!field.isEmpty()) {  // or !field.equals(_EMPTY_)
                    hasAnyValue = true;
                    try {
                        sum += Integer.parseInt(field.trim());
                    } catch (NumberFormatException e) {}
                }
            }
            setF209(hasAnyValue ? String.valueOf(sum) : _EMPTY_);
        }

        private String getSeverityLevel() {
            if (f209.equals(_EMPTY_) || f209.trim().isEmpty()) return _EMPTY_;

            try {
                int score = Integer.parseInt(f209.trim());

                if (score >= 0 && score <= 4)        return "1";
                else if (score >=  5 && score <=  9) return "2";
                else if (score >= 10 && score <= 14) return "3";
                else if (score >= 15 && score <= 19) return "4";
                else if (score >= 20 && score <= 27) return "5";
            } catch (NumberFormatException e) {
                return _EMPTY_; // or "Invalid"
            }
            return _EMPTY_;
        }

        @Bindable
        public String getF210() {
            return f210;
        }

        public void setF210(String f210) {
            this.f210 = f210;
            setF211(f210.equals("1") || f210.equals("2") ? _EMPTY_ : this.f211);
            setF212(f210.equals("1") || f210.equals("2") ? _EMPTY_ : this.f212);
            setF213(f210.equals("1") || f210.equals("2") ? _EMPTY_ : this.f213);
            notifyPropertyChanged(BR.f210);
        }

        @Bindable
        public String getF211() {
            return f211;
        }

        public void setF211(String f211) {
            this.f211 = f211;
            notifyPropertyChanged(BR.f211);
        }

        @Bindable
        public String getF212() {
            return f212;
        }

        public void setF212(String f212) {
            this.f212 = f212;
            notifyPropertyChanged(BR.f212);
        }

        @Bindable
        public String getF213() {
            return f213;
        }

        public void setF213(String f213) {
            this.f213 = f213;
            notifyPropertyChanged(BR.f213);
        }

    }
}
