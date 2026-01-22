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
import edu.aku.omarshoaib.renew.database.dao.Form3aDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form3a.TABLE_NAME)
public class Form3a extends FormBaseModel {

    public static final String TABLE_NAME = "Form3a";
    public static final String SECTION_NAME = "Sections: F3a";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("village_name")
    private String villageName = _EMPTY_;

    @SerializedName("pregnant_woman_id")
    private String pregnantWomanId = _EMPTY_;

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
        MainApp.form3a = new Form3a();
        MainApp.form3a.setDistrictCode(MainApp.user.getDistId());
        MainApp.form3a.setVillageName(MainApp.vForm3a.getVillageAddress());
        MainApp.form3a.setPregnantWomanId(MainApp.vForm3a.getParticipantId());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String participantId) {
        Form3aDao forms3aDao = AppDatabase.getDBInstance().form3aDao();
        Form3a form3a = forms3aDao.getDataByParticipantId(participantId);
        if (form3a != null) {
            MainApp.form3a = form3a;
        } else {
            MainApp.form3a.setUid(AppConstants.generateUid());
            MainApp.form3a.setId(forms3aDao.add(MainApp.form3a));
        }
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getPregnantWomanId() {
        return pregnantWomanId;
    }

    public void setPregnantWomanId(String pregnantWomanId) {
        this.pregnantWomanId = pregnantWomanId;
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

    private SF3a sF3a;

    public SF3a getSF3a() {
        return sF3a;
    }

    public void setSF3a(SF3a sF3a){
        this.sF3a = sF3a;
    }

    /**
     * F3aorm03a: High Risk Pregnancy Assessment Form (Validation by Doctor)
     */

    public static class SF3a extends BaseObservable {
        private String f3a01 = _EMPTY_;
        private String f3a02 = _EMPTY_;
        private String f3a03 = _EMPTY_;
        private String f3a04 = _EMPTY_;
        private String f3a05 = _EMPTY_;
        private String f3a06 = _EMPTY_;
        private String f3a07 = _EMPTY_;
        private String f3a08 = _EMPTY_;
        private String f3a09 = _EMPTY_;
        private String f3a10 = _EMPTY_;
        private String f3a11 = _EMPTY_;
        private String f3a12 = _EMPTY_;
        private String f3a1201x = _EMPTY_;
        private String f3a13 = _EMPTY_;
        private String f3a1301x = _EMPTY_;
        private String f3a1396x = _EMPTY_;
        private String f3a14 = _EMPTY_;
        private String f3a1496x = _EMPTY_;
//        private String f3a15 = _EMPTY_;
//        private String f3a1596x = _EMPTY_;
        private String f3a1601 = _EMPTY_;
        private String f3a1602 = _EMPTY_;
        private String f3a1603 = _EMPTY_;
        private String f3a1604 = _EMPTY_;
        private String f3a1605 = _EMPTY_;
        private String f3a1606 = _EMPTY_;
        private String f3a1607 = _EMPTY_;
        private String f3a1608 = _EMPTY_;
        private String f3a1609 = _EMPTY_;
        private String f3a1610 = _EMPTY_;
        private String f3a1611 = _EMPTY_;
        private String f3a1698 = _EMPTY_;
        private String f03a01 = _EMPTY_;
        private String f03a02 = _EMPTY_;
        private String f03a03 = _EMPTY_;
        private String f03a0396x = _EMPTY_;
        private String f03a04 = _EMPTY_;
        private String f03a05 = _EMPTY_;
        private String f03a06 = _EMPTY_;
        private String f03a0696x = _EMPTY_;
        private String f03a07 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF3a> {
            public DataConverter() {
                super(new TypeToken<SF3a>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF3a data) {
            MainApp.form3a.setSF3a(data);
            return AppDatabase.getDBInstance().form3aDao().update(MainApp.form3a);
        }

        // Get section object by parsing json
        public static SF3a getData() {
            return MainApp.form3a.getSF3a();
        }

        @Bindable
        public String getF3a01() {
            return f3a01;
        }

        public void setF3a01(String f3a01) {
            this.f3a01 = f3a01;
            notifyPropertyChanged(BR.f3a01);
        }

        @Bindable
        public String getF3a02() {
            return f3a02;
        }

        public void setF3a02(String f3a02) {
            this.f3a02 = f3a02;
            notifyPropertyChanged(BR.f3a02);
        }

        @Bindable
        public String getF3a03() {
            return f3a03;
        }

        public void setF3a03(String f3a03) {
            this.f3a03 = f3a03;
            notifyPropertyChanged(BR.f3a03);
        }

        @Bindable
        public String getF3a04() {
            return f3a04;
        }

        public void setF3a04(String f3a04) {
            this.f3a04 = f3a04;
            notifyPropertyChanged(BR.f3a04);
        }

        @Bindable
        public String getF3a05() {
            return f3a05;
        }

        public void setF3a05(String f3a05) {
            this.f3a05 = f3a05;
            notifyPropertyChanged(BR.f3a05);
        }

        @Bindable
        public String getF3a06() {
            return f3a06;
        }

        public void setF3a06(String f3a06) {
            this.f3a06 = f3a06;
            notifyPropertyChanged(BR.f3a06);
        }

        @Bindable
        public String getF3a07() {
            return f3a07;
        }

        public void setF3a07(String f3a07) {
            this.f3a07 = f3a07;
            notifyPropertyChanged(BR.f3a07);
        }

        @Bindable
        public String getF3a08() {
            return f3a08;
        }

        public void setF3a08(String f3a08) {
            this.f3a08 = f3a08;
            notifyPropertyChanged(BR.f3a08);
        }

        @Bindable
        public String getF3a09() {
            return f3a09;
        }

        public void setF3a09(String f3a09) {
            this.f3a09 = f3a09;
            notifyPropertyChanged(BR.f3a09);
        }

        @Bindable
        public String getF3a10() {
            return f3a10;
        }

        public void setF3a10(String f3a10) {
            this.f3a10 = f3a10;
            if(!f3a10.equals("1")) {
                setF3a11(_EMPTY_);
                setF3a12(_EMPTY_);
                setF3a13(_EMPTY_);
                setF3a14(_EMPTY_);
                setF3a1698(_EMPTY_);
//                setF3a15(_EMPTY_);
//                setF03a01(_EMPTY_);
//                setF03a02(_EMPTY_);
//                setF03a03(_EMPTY_);
//                setF03a04(_EMPTY_);
//                setF03a05(_EMPTY_);
//                setF03a06(_EMPTY_);
//                setF03a07(_EMPTY_);
            }
            notifyPropertyChanged(BR.f3a10);
        }

        @Bindable
        public String getF3a11() {
            return f3a11;
        }

        public void setF3a11(String f3a11) {
            this.f3a11 = f3a11;
            notifyPropertyChanged(BR.f3a11);
        }

        @Bindable
        public String getF3a12() {
            return f3a12;
        }

        public void setF3a12(String f3a12) {
            this.f3a12 = f3a12;
            setF3a1201x(f3a12.equals("1") ? this.f3a1201x : _EMPTY_);
            notifyPropertyChanged(BR.f3a12);
        }

        @Bindable
        public String getF3a1201x() {
            return f3a1201x;
        }

        public void setF3a1201x(String f3a1201x) {
            this.f3a1201x = f3a1201x;
            notifyPropertyChanged(BR.f3a1201x);
        }

        @Bindable
        public String getF3a13() {
            return f3a13;
        }

        public void setF3a13(String f3a13) {
            this.f3a13 = f3a13;
            setF3a1396x(f3a13.equals("96") ? this.f3a1396x: _EMPTY_);
            setF3a1301x(f3a13.equals("1") ? this.f3a1301x: _EMPTY_);
            notifyPropertyChanged(BR.f3a13);
        }

        @Bindable
        public String getF3a1301x() {
            return f3a1301x;
        }

        public void setF3a1301x(String f3a1301x) {
            this.f3a1301x = f3a1301x;
            notifyPropertyChanged(BR.f3a1301x);
        }

        @Bindable
        public String getF3a1396x() {
            return f3a1396x;
        }

        public void setF3a1396x(String f3a1396x) {
            this.f3a1396x = f3a1396x;
            notifyPropertyChanged(BR.f3a1396x);
        }

        @Bindable
        public String getF3a14() {
            return f3a14;
        }

        public void setF3a14(String f3a14) {
            this.f3a14 = f3a14;
            setF3a1496x(f3a14.equals("96") ? this.f3a1496x: _EMPTY_);
            notifyPropertyChanged(BR.f3a14);
        }

        @Bindable
        public String getF3a1496x() {
            return f3a1496x;
        }

        public void setF3a1496x(String f3a1496x) {
            this.f3a1496x = f3a1496x;
            notifyPropertyChanged(BR.f3a1496x);
        }

        /*@Bindable
        public String getF3a15() {
            return f3a15;
        }

        public void setF3a15(String f3a15) {
            this.f3a15 = f3a15;
            setF3a1596x(f3a15.equals("96") ? this.f3a1596x: _EMPTY_);
            notifyPropertyChanged(BR.f3a15);
        }

        @Bindable
        public String getF3a1596x() {
            return f3a1596x;
        }

        public void setF3a1596x(String f3a1596x) {
            this.f3a1596x = f3a1596x;
            notifyPropertyChanged(BR.f3a1596x);
        }*/

        @Bindable
        public String getF3a1601() {
            return f3a1601;
        }

        public void setF3a1601(String f3a1601) {
            this.f3a1601 = f3a1601;
            notifyPropertyChanged(BR.f3a1601);
        }

        @Bindable
        public String getF3a1602() {
            return f3a1602;
        }

        public void setF3a1602(String f3a1602) {
            this.f3a1602 = f3a1602;
            notifyPropertyChanged(BR.f3a1602);
        }

        @Bindable
        public String getF3a1603() {
            return f3a1603;
        }

        public void setF3a1603(String f3a1603) {
            this.f3a1603 = f3a1603;
            notifyPropertyChanged(BR.f3a1603);
        }

        @Bindable
        public String getF3a1604() {
            return f3a1604;
        }

        public void setF3a1604(String f3a1604) {
            this.f3a1604 = f3a1604;
            notifyPropertyChanged(BR.f3a1604);
        }

        @Bindable
        public String getF3a1605() {
            return f3a1605;
        }

        public void setF3a1605(String f3a1605) {
            this.f3a1605 = f3a1605;
            notifyPropertyChanged(BR.f3a1605);
        }

        @Bindable
        public String getF3a1606() {
            return f3a1606;
        }

        public void setF3a1606(String f3a1606) {
            this.f3a1606 = f3a1606;
            notifyPropertyChanged(BR.f3a1606);
        }

        @Bindable
        public String getF3a1607() {
            return f3a1607;
        }

        public void setF3a1607(String f3a1607) {
            this.f3a1607 = f3a1607;
            notifyPropertyChanged(BR.f3a1607);
        }

        @Bindable
        public String getF3a1608() {
            return f3a1608;
        }

        public void setF3a1608(String f3a1608) {
            this.f3a1608 = f3a1608;
            notifyPropertyChanged(BR.f3a1608);
        }

        @Bindable
        public String getF3a1609() {
            return f3a1609;
        }

        public void setF3a1609(String f3a1609) {
            this.f3a1609 = f3a1609;
            notifyPropertyChanged(BR.f3a1609);
        }

        @Bindable
        public String getF3a1610() {
            return f3a1610;
        }

        public void setF3a1610(String f3a1610) {
            this.f3a1610 = f3a1610;
            notifyPropertyChanged(BR.f3a1610);
        }

        @Bindable
        public String getF3a1611() {
            return f3a1611;
        }

        public void setF3a1611(String f3a1611) {
            this.f3a1611 = f3a1611;
            notifyPropertyChanged(BR.f3a1611);
        }

        @Bindable
        public String getF3a1698() {
            return f3a1698;
        }

        public void setF3a1698(String f3a1698) {
            this.f3a1698 = f3a1698;
            setF3a1601(_EMPTY_);
            setF3a1602(_EMPTY_);
            setF3a1603(_EMPTY_);
            setF3a1604(_EMPTY_);
            setF3a1605(_EMPTY_);
            setF3a1606(_EMPTY_);
            setF3a1607(_EMPTY_);
            setF3a1608(_EMPTY_);
            setF3a1609(_EMPTY_);
            setF3a1610(_EMPTY_);
            setF3a1611(_EMPTY_);
            setF03a01(_EMPTY_);
            setF03a02(_EMPTY_);
            setF03a03(_EMPTY_);
            setF03a04(_EMPTY_);
            setF03a05(_EMPTY_);
            setF03a06(_EMPTY_);
            setF03a07(_EMPTY_);
            notifyPropertyChanged(BR.f3a1698);
        }

        @Bindable
        public String getF03a01() {
            return f03a01;
        }

        public void setF03a01(String f03a01) {
            this.f03a01 = f03a01;
            notifyPropertyChanged(BR.f03a01);
        }

        @Bindable
        public String getF03a02() {
            return f03a02;
        }

        public void setF03a02(String f03a02) {
            this.f03a02 = f03a02;
            notifyPropertyChanged(BR.f03a02);
        }

        @Bindable
        public String getF03a03() {
            return f03a03;
        }

        public void setF03a03(String f03a03) {
            this.f03a03 = f03a03;
            setF03a0396x(f03a03.equals("96") ? this.f03a0396x: _EMPTY_);
            notifyPropertyChanged(BR.f03a03);
        }

        @Bindable
        public String getF03a0396x() {
            return f03a0396x;
        }

        public void setF03a0396x(String f03a0396x) {
            this.f03a0396x = f03a0396x;
            notifyPropertyChanged(BR.f03a0396x);
        }

        @Bindable
        public String getF03a04() {
            return f03a04;
        }

        public void setF03a04(String f03a04) {
            this.f03a04 = f03a04;
            setF03a05(f03a04.equals("1") ? this.f03a05: _EMPTY_);
            setF03a06(f03a04.equals("2") ? this.f03a06: _EMPTY_);
            setF03a07(f03a04.equals("2") ? this.f03a07: _EMPTY_);
            notifyPropertyChanged(BR.f03a04);
        }

        @Bindable
        public String getF03a05() {
            return f03a05;
        }

        public void setF03a05(String f03a05) {
            this.f03a05 = f03a05;
            notifyPropertyChanged(BR.f03a05);
        }

        @Bindable
        public String getF03a06() {
            return f03a06;
        }

        public void setF03a06(String f03a06) {
            this.f03a06 = f03a06;
            setF03a0696x(f03a06.equals("96") ? this.f03a0696x: _EMPTY_);
            notifyPropertyChanged(BR.f03a06);
        }

        @Bindable
        public String getF03a0696x() {
            return f03a0696x;
        }

        public void setF03a0696x(String f03a0696x) {
            this.f03a0696x = f03a0696x;
            notifyPropertyChanged(BR.f03a0696x);
        }

        @Bindable
        public String getF03a07() {
            return f03a07;
        }

        public void setF03a07(String f03a07) {
            this.f03a07 = f03a07;
            notifyPropertyChanged(BR.f03a07);
        }

    }

}
