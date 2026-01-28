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
import edu.aku.omarshoaib.renew.database.dao.Form2aDao;
import edu.aku.omarshoaib.renew.database.dao.Form2bDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form2b.TABLE_NAME)
public class Form2b extends FormBaseModel{

    public static final String TABLE_NAME = "Form2b";
    public static final String SECTION_NAME = "Sections: F2b";
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

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form2b = new Form2b();
        MainApp.form2b.setDistrictCode(MainApp.user.getDistId());
        MainApp.form2b.setParticipantId(MainApp.vForm2b.getParticipantId());

//        MainApp.form2a.setScrId(MainApp.form1.getScrId());
//        MainApp.form2a.setVillageName(MainApp.form1.getVillageName());
//        MainApp.form2a.setUuId(MainApp.participant.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String participantId) {
        Form2bDao forms2bDao = AppDatabase.getDBInstance().form2bDao();
        Form2b form2b = forms2bDao.getDataByParticipantId(participantId);
        if (form2b != null) {
            MainApp.form2b = form2b;
        } else {
            MainApp.form2b.setUid(AppConstants.generateUid());
            MainApp.form2b.setId(forms2bDao.add(MainApp.form2b));
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

    /*JSON OBJECTS*/
    private SF2b sF2b;

    public SF2b getSF2b() {
        return sF2b;
    }

    public void setSF2b(SF2b sF2b){
        this.sF2b = sF2b;
    }

    /**
     * Form 02b: Follow-up of Referral
     */

    public static class SF2b extends BaseObservable {
        private String f02b01 = _EMPTY_;
        private String f02b02 = _EMPTY_;
        private String f02b03 = _EMPTY_;
        private String f02b04 = _EMPTY_;
        private String f02b05 = _EMPTY_;
        private String f02b06 = _EMPTY_;
        private String f02b07 = _EMPTY_;
        private String f02b08 = _EMPTY_;
        private String f02b09 = _EMPTY_;
        private String f02b10 = _EMPTY_;
        private String f02b11 = _EMPTY_;
        private String f02b12 = _EMPTY_;
        private String f02b13 = _EMPTY_;
        private String f02b1401 = _EMPTY_;
        private String f02b1402 = _EMPTY_;
        private String f02b1403 = _EMPTY_;
        private String f02b1404 = _EMPTY_;
        private String f02b1496 = _EMPTY_;
        private String f02b1496x = _EMPTY_;
        private String f02b15 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF2b> {
            public DataConverter() {
                super(new TypeToken<SF2b>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF2b data) {
            MainApp.form2b.setSF2b(data);
            return AppDatabase.getDBInstance().form2bDao().update(MainApp.form2b);
        }

        // Get section object by parsing json
        public static SF2b getData() {
            return MainApp.form2b.getSF2b();
        }

        @Bindable
        public String getF02b01() {
            return f02b01;
        }

        public void setF02b01(String f02b01) {
            this.f02b01 = f02b01;
            notifyPropertyChanged(BR.f02b01);
        }

        @Bindable
        public String getF02b02() {
            return f02b02;
        }

        public void setF02b02(String f02b02) {
            this.f02b02 = f02b02;
            notifyPropertyChanged(BR.f02b02);
        }

        @Bindable
        public String getF02b03() {
            return f02b03;
        }

        public void setF02b03(String f02b03) {
            this.f02b03 = f02b03;
            notifyPropertyChanged(BR.f02b03);
        }

        @Bindable
        public String getF02b04() {
            return f02b04;
        }

        public void setF02b04(String f02b04) {
            this.f02b04 = f02b04;
            notifyPropertyChanged(BR.f02b04);
        }

        @Bindable
        public String getF02b05() {
            return f02b05;
        }

        public void setF02b05(String f02b05) {
            this.f02b05 = f02b05;
            notifyPropertyChanged(BR.f02b05);
        }

        @Bindable
        public String getF02b06() {
            return f02b06;
        }

        public void setF02b06(String f02b06) {
            this.f02b06 = f02b06;
            notifyPropertyChanged(BR.f02b06);
        }

        @Bindable
        public String getF02b07() {
            return f02b07;
        }

        public void setF02b07(String f02b07) {
            this.f02b07 = f02b07;
            notifyPropertyChanged(BR.f02b07);
        }

        @Bindable
        public String getF02b08() {
            return f02b08;
        }

        public void setF02b08(String f02b08) {
            this.f02b08 = f02b08;
            notifyPropertyChanged(BR.f02b08);
        }

        @Bindable
        public String getF02b09() {
            return f02b09;
        }

        public void setF02b09(String f02b09) {
            this.f02b09 = f02b09;
            notifyPropertyChanged(BR.f02b09);
        }

        @Bindable
        public String getF02b10() {
            return f02b10;
        }

        public void setF02b10(String f02b10) {
            this.f02b10 = f02b10;
            notifyPropertyChanged(BR.f02b10);
        }

        @Bindable
        public String getF02b11() {
            return f02b11;
        }

        public void setF02b11(String f02b11) {
            this.f02b11 = f02b11;
            notifyPropertyChanged(BR.f02b11);
        }

        @Bindable
        public String getF02b12() {
            return f02b12;
        }

        public void setF02b12(String f02b12) {
            this.f02b12 = f02b12;
            notifyPropertyChanged(BR.f02b12);
        }

        @Bindable
        public String getF02b13() {
            return f02b13;
        }

        public void setF02b13(String f02b13) {
            this.f02b13 = f02b13;
            if(!f02b13.equals("2")) {
                setF02b1401(_EMPTY_);
                setF02b1402(_EMPTY_);
                setF02b1403(_EMPTY_);
                setF02b1404(_EMPTY_);
                setF02b1496(_EMPTY_);
                setF02b15(_EMPTY_);
            }
            notifyPropertyChanged(BR.f02b13);
        }

        @Bindable
        public String getF02b1401() {
            return f02b1401;
        }

        public void setF02b1401(String f02b1401) {
            this.f02b1401 = f02b1401;
            notifyPropertyChanged(BR.f02b1401);
        }

        @Bindable
        public String getF02b1402() {
            return f02b1402;
        }

        public void setF02b1402(String f02b1402) {
            this.f02b1402 = f02b1402;
            notifyPropertyChanged(BR.f02b1402);
        }

        @Bindable
        public String getF02b1403() {
            return f02b1403;
        }

        public void setF02b1403(String f02b1403) {
            this.f02b1403 = f02b1403;
            notifyPropertyChanged(BR.f02b1403);
        }

        @Bindable
        public String getF02b1404() {
            return f02b1404;
        }

        public void setF02b1404(String f02b1404) {
            this.f02b1404 = f02b1404;
            notifyPropertyChanged(BR.f02b1404);
        }

        @Bindable
        public String getF02b1496() {
            return f02b1496;
        }

        public void setF02b1496(String f02b1496) {
            this.f02b1496 = f02b1496;
            setF02b1496x(f02b1496.equals("96") ? this.f02b1496x : _EMPTY_);
            notifyPropertyChanged(BR.f02b1496);
        }

        @Bindable
        public String getF02b1496x() {
            return f02b1496x;
        }

        public void setF02b1496x(String f02b1496x) {
            this.f02b1496x = f02b1496x;
            notifyPropertyChanged(BR.f02b1496x);
        }

        @Bindable
        public String getF02b15() {
            return f02b15;
        }

        public void setF02b15(String f02b15) {
            this.f02b15 = f02b15;
            notifyPropertyChanged(BR.f02b15);
        }

    }

}
