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
import edu.aku.omarshoaib.renew.database.dao.Form2aDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form2a.TABLE_NAME)
public class Form2a extends FormBaseModel{

    public static final String TABLE_NAME = "Form2a";
    public static final String SECTION_NAME = "Sections: F2a";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("participant_id")
    private String participantId = _EMPTY_;

    @SerializedName("ending_date")
    private String endingDate = _EMPTY_;

    @SerializedName("_uuid")
    private String uuid = _EMPTY_;

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
    private SF2a sF2a;

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form2a = new Form2a();
        MainApp.form2a.setDistrictCode(MainApp.user.getDistId());
        MainApp.form2a.setParticipantId(MainApp.vPHQ9.getParticipantId());
        MainApp.form2a.setUuid(MainApp.vPHQ9.getUid());

//        MainApp.form2a.setScrId(MainApp.form1.getScrId());
//        MainApp.form2a.setVillageName(MainApp.form1.getVillageName());
//        MainApp.form2a.setUuId(MainApp.participant.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String participantId) {
        Form2aDao forms2aDao = AppDatabase.getDBInstance().form2aDao();
        Form2a form2a = forms2aDao.getDataByParticipantId(participantId);
        if (form2a != null) {
            MainApp.form2a = form2a;
        } else {
            MainApp.form2a.setUid(AppConstants.generateUid());
            MainApp.form2a.setId(forms2aDao.add(MainApp.form2a));
        }
    }


    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
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

    public SF2a getSF2a() {
        return sF2a;
    }

    public void setSF2a(SF2a sF2a){
        this.sF2a = sF2a;
    }

    /**
     * Form 02a: Mental Health Assessment (PHQ-9) Questionnaire
     Screening of WRA, adolescents, and pregnant women for depression symptoms by Psychologist.
     */
    public static class SF2a extends BaseObservable {
        private String f2a01 = _EMPTY_;
        private String f2a02 = _EMPTY_;
        private String f2a03 = _EMPTY_;
        private String f2a04 = _EMPTY_;
        private String f2a05 = _EMPTY_;
        private String f2a06 = _EMPTY_;
        private String f2a07 = _EMPTY_;
        private String f2a08 = _EMPTY_;
        private String f2a09 = _EMPTY_;
        private String f2a10 = _EMPTY_;
        private String f2a11 = _EMPTY_;
        private String f2a12 = _EMPTY_;
        private String f2a13 = _EMPTY_;
        private String f2a14 = _EMPTY_;
        private String f2b01 = _EMPTY_;
        private String f2b02 = _EMPTY_;
        private String f2b03 = _EMPTY_;
        private String f2b04 = _EMPTY_;
        private String f2b05 = _EMPTY_;
        private String f2b06 = _EMPTY_;
        private String f2b07 = _EMPTY_;
        private String f2b08 = _EMPTY_;
        private String f2b09 = _EMPTY_;
        private String f2b10 = _EMPTY_;
        private String f2b11 = _EMPTY_;
        private String f2b12 = _EMPTY_;
        private String f2c01 = _EMPTY_;
        private String f2c02 = _EMPTY_;
        private String f2c03 = _EMPTY_;
        private String f2c04 = _EMPTY_;
        private String f2c0401x = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF2a> {
            public DataConverter() {
                super(new TypeToken<SF2a>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF2a data) {
            MainApp.form2a.setSF2a(data);
            return AppDatabase.getDBInstance().form2aDao().update(MainApp.form2a);
        }

        // Get section object by parsing json
        public static SF2a getData() {
            return MainApp.form2a.getSF2a();
        }

        @Bindable
        public String getF2a01() {
            return f2a01;
        }

        public void setF2a01(String f2a01) {
            this.f2a01 = f2a01;
            notifyPropertyChanged(BR.f2a01);
        }

        @Bindable
        public String getF2a02() {
            return f2a02;
        }

        public void setF2a02(String f2a02) {
            this.f2a02 = f2a02;
            notifyPropertyChanged(BR.f2a02);
        }

        @Bindable
        public String getF2a03() {
            return f2a03;
        }

        public void setF2a03(String f2a03) {
            this.f2a03 = f2a03;
            notifyPropertyChanged(BR.f2a03);
        }

        @Bindable
        public String getF2a04() {
            return f2a04;
        }

        public void setF2a04(String f2a04) {
            this.f2a04 = f2a04;
            notifyPropertyChanged(BR.f2a04);
        }

        @Bindable
        public String getF2a05() {
            return f2a05;
        }

        public void setF2a05(String f2a05) {
            this.f2a05 = f2a05;
            notifyPropertyChanged(BR.f2a05);
        }

        @Bindable
        public String getF2a06() {
            return f2a06;
        }

        public void setF2a06(String f2a06) {
            this.f2a06 = f2a06;
            notifyPropertyChanged(BR.f2a06);
        }

        @Bindable
        public String getF2a07() {
            return f2a07;
        }

        public void setF2a07(String f2a07) {
            this.f2a07 = f2a07;
            notifyPropertyChanged(BR.f2a07);
        }

        @Bindable
        public String getF2a08() {
            return f2a08;
        }

        public void setF2a08(String f2a08) {
            this.f2a08 = f2a08;
            notifyPropertyChanged(BR.f2a08);
        }

        @Bindable
        public String getF2a09() {
            return f2a09;
        }

        public void setF2a09(String f2a09) {
            this.f2a09 = f2a09;
            notifyPropertyChanged(BR.f2a09);
        }

        @Bindable
        public String getF2a10() {
            return f2a10;
        }

        public void setF2a10(String f2a10) {
            this.f2a10 = f2a10;
            notifyPropertyChanged(BR.f2a10);
        }

        @Bindable
        public String getF2a11() {
            return f2a11;
        }

        public void setF2a11(String f2a11) {
            this.f2a11 = f2a11;
            notifyPropertyChanged(BR.f2a11);
        }

        @Bindable
        public String getF2a12() {
            return f2a12;
        }

        public void setF2a12(String f2a12) {
            this.f2a12 = f2a12;
            notifyPropertyChanged(BR.f2a12);
        }

        @Bindable
        public String getF2a13() {
            return f2a13;
        }

        public void setF2a13(String f2a13) {
            this.f2a13 = f2a13;
            notifyPropertyChanged(BR.f2a13);
        }

        @Bindable
        public String getF2a14() {
            return f2a14;
        }

        public void setF2a14(String f2a14) {
            this.f2a14 = f2a14;
            notifyPropertyChanged(BR.f2a14);
        }

        @Bindable
        public String getF2b01() {
            return f2b01;
        }

        public void setF2b01(String f2b01) {
            this.f2b01 = f2b01;
            calculateF209();
            notifyPropertyChanged(BR.f2b01);
        }

        @Bindable
        public String getF2b02() {
            return f2b02;
        }

        public void setF2b02(String f2b02) {
            this.f2b02 = f2b02;
            calculateF209();
            notifyPropertyChanged(BR.f2b02);
        }

        @Bindable
        public String getF2b03() {
            return f2b03;
        }

        public void setF2b03(String f2b03) {
            this.f2b03 = f2b03;
            calculateF209();
            notifyPropertyChanged(BR.f2b03);
        }

        @Bindable
        public String getF2b04() {
            return f2b04;
        }

        public void setF2b04(String f2b04) {
            this.f2b04 = f2b04;
            calculateF209();
            notifyPropertyChanged(BR.f2b04);
        }

        @Bindable
        public String getF2b05() {
            return f2b05;
        }

        public void setF2b05(String f2b05) {
            this.f2b05 = f2b05;
            calculateF209();
            notifyPropertyChanged(BR.f2b05);
        }

        @Bindable
        public String getF2b06() {
            return f2b06;
        }

        public void setF2b06(String f2b06) {
            this.f2b06 = f2b06;
            calculateF209();
            notifyPropertyChanged(BR.f2b06);
        }

        @Bindable
        public String getF2b07() {
            return f2b07;
        }

        public void setF2b07(String f2b07) {
            this.f2b07 = f2b07;
            calculateF209();
            notifyPropertyChanged(BR.f2b07);
        }

        @Bindable
        public String getF2b08() {
            return f2b08;
        }

        public void setF2b08(String f2b08) {
            this.f2b08 = f2b08;
            calculateF209();
            notifyPropertyChanged(BR.f2b08);
        }

        @Bindable
        public String getF2b09() {
            return f2b09;
        }

        public void setF2b09(String f2b09) {
            this.f2b09 = f2b09;
            calculateF209();
            notifyPropertyChanged(BR.f2b09);
        }

        private void calculateF209() {
            int sum = 0;
            boolean hasAnyValue = false;

            String[] fields = {f2b01, f2b02, f2b03, f2b04, f2b05,
                    f2b06, f2b07, f2b08, f2b09};

            for (String field : fields) {
                if (!field.isEmpty()) {  // or !field.equals(_EMPTY_)
                    hasAnyValue = true;
                    try {
                        sum += (Integer.parseInt(field.trim()) - 1);
                    } catch (NumberFormatException e) {}
                }
            }
            setF2b10(hasAnyValue ? String.valueOf(sum) : _EMPTY_);
        }

        private String getSeverityLevel() {
            if (f2b10.equals(_EMPTY_) || f2b10.trim().isEmpty()) return _EMPTY_;

            try {
                int score = Integer.parseInt(f2b10);

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
        public String getF2b10() {
            return f2b10;
        }

        public void setF2b10(String f2b10) {
            this.f2b10 = f2b10;
            setF2b11(getSeverityLevel());
            notifyPropertyChanged(BR.f2b10);
        }

        @Bindable
        public String getF2b11() {
            return f2b11;
        }

        public void setF2b11(String f2b11) {
            this.f2b11 = f2b11;
            notifyPropertyChanged(BR.f2b11);
        }

        @Bindable
        public String getF2b12() {
            return f2b12;
        }

        public void setF2b12(String f2b12) {
            this.f2b12 = f2b12;
            setF2c01(f2b12.equals("1") ? this.f2c01 : _EMPTY_);
            setF2c02(f2b12.equals("1") ? this.f2c02 : _EMPTY_);
            setF2c03(f2b12.equals("1") ? this.f2c03 : _EMPTY_);
            setF2c04(f2b12.equals("1") ? this.f2c04 : _EMPTY_);
            notifyPropertyChanged(BR.f2b12);
        }

        @Bindable
        public String getF2c01() {
            return f2c01;
        }

        public void setF2c01(String f2c01) {
            this.f2c01 = f2c01;
            notifyPropertyChanged(BR.f2c01);
        }

        @Bindable
        public String getF2c02() {
            return f2c02;
        }

        public void setF2c02(String f2c02) {
            this.f2c02 = f2c02;
            notifyPropertyChanged(BR.f2c02);
        }

        @Bindable
        public String getF2c03() {
            return f2c03;
        }

        public void setF2c03(String f2c03) {
            this.f2c03 = f2c03;
            notifyPropertyChanged(BR.f2c03);
        }

        @Bindable
        public String getF2c04() {
            return f2c04;
        }

        public void setF2c04(String f2c04) {
            this.f2c04 = f2c04;
            setF2c0401x(f2c04.equals("1") ? this.f2c0401x : _EMPTY_);
            notifyPropertyChanged(BR.f2c04);
        }

        @Bindable
        public String getF2c0401x() {
            return f2c0401x;
        }

        public void setF2c0401x(String f2c0401x) {
            this.f2c0401x = f2c0401x;
            notifyPropertyChanged(BR.f2c0401x);
        }
    }
}
