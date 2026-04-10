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
import edu.aku.omarshoaib.renew.database.dao.Form6Dao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form6.TABLE_NAME)
public class Form6 extends FormBaseModel {

    public static final String TABLE_NAME = "Form6";
    public static final String SECTION_NAME = "Sections: F6";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("_uuid")
    private String uuid = _EMPTY_;

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("participant_id")
    private String participantId = _EMPTY_;

    @SerializedName("ending_date")
    private String endingDate = _EMPTY_;

    // This variable is used to mark the form5 that its completed once.
    // To implement the logic of displaying 'Skip to End' button over
    // the sections if user open the form5 in edit mode, update any section/value,
    // save it and then directly skip to end without traversing the whole form5 again.
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
    private SF6 sF6;

    public Form6() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form6 = new Form6();
        MainApp.form6.setDistrictCode(MainApp.user.getDistId());
        MainApp.form6.setParticipantId(MainApp.vFormF06.getParticipantId());
        MainApp.form6.setUuid(MainApp.vFormF06.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String participantId) {
        Form6Dao dao = AppDatabase.getDBInstance().form6Dao();
        Form6 form = dao.getDataByParticipantId(MainApp.user.getDistId(), participantId);
        if (form != null) {
            MainApp.form6 = form;
        } else {
            MainApp.form6.setUid(AppConstants.generateUid());
            MainApp.form6.setId(dao.add(MainApp.form6));
        }
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String scrId) {
        this.participantId = scrId;
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

    public SF6 getSF6() {
        return sF6;
    }

    public void setSF6(SF6 sF6){
        this.sF6 = sF6;
    }

    /** Form 2: Mental Health assessment (PHQ-9) Questionnaire */
    public static class SF6 extends BaseObservable {
        private String f601 = _EMPTY_;
        private String f602 = _EMPTY_;
        private String f603 = _EMPTY_;
        private String f604 = _EMPTY_;
        private String f604a = _EMPTY_;
        private String f604b = _EMPTY_;
        private String f605 = _EMPTY_;
        private String f606 = _EMPTY_;
        private String f607 = _EMPTY_;
        private String f607a = _EMPTY_;
        private String f608 = _EMPTY_;
        private String f608a = _EMPTY_;
        private String f609 = _EMPTY_;
        private String f610a = _EMPTY_;
        private String f610b = _EMPTY_;
        private String f610c = _EMPTY_;
        private String f610d = _EMPTY_;
        private String f610e = _EMPTY_;
        private String f610f = _EMPTY_;
        private String f610g = _EMPTY_;
        private String f611 = _EMPTY_;
        private String f612 = _EMPTY_;
        private String f61296x = _EMPTY_;
        private String f613 = _EMPTY_;
        private String f61396x = _EMPTY_;
        private String f613a = _EMPTY_;
        private String f613a02x = _EMPTY_;
        private String f61401 = _EMPTY_;
        private String f61402 = _EMPTY_;
        private String f61403 = _EMPTY_;
        private String f61404 = _EMPTY_;
        private String f61405 = _EMPTY_;
        private String f61496 = _EMPTY_;
        private String f61496x = _EMPTY_;
        private String f61497 = _EMPTY_;
        private String f615 = _EMPTY_;
        private String f616 = _EMPTY_;
        private String f617 = _EMPTY_;
        private String f618 = _EMPTY_;
        private String f61801x = _EMPTY_;
        private String f618a = _EMPTY_;
        private String f618b = _EMPTY_;
        private String f618b96x = _EMPTY_;
        private String f619 = _EMPTY_;
        private String f61901 = _EMPTY_;
        private String f61905 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SF6> {
            public DataConverter() {
                super(new TypeToken<SF6>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SF6 data) {
            MainApp.form6.setSF6(data);
            return AppDatabase.getDBInstance().form6Dao().update(MainApp.form6);
        }

        // Get section object by parsing json
        public static SF6 getData() {
            return MainApp.form6.getSF6();
        }

        @Bindable
        public String getF601() {
            return f601;
        }

        public void setF601(String f601) {
            this.f601 = f601;
            notifyPropertyChanged(BR.f601);
        }

        @Bindable
        public String getF602() {
            return f602;
        }

        public void setF602(String f602) {
            this.f602 = f602;
            notifyPropertyChanged(BR.f602);
        }

        @Bindable
        public String getF603() {
            return f603;
        }

        public void setF603(String f603) {
            this.f603 = f603;
            notifyPropertyChanged(BR.f603);
        }

        @Bindable
        public String getF604() {
            return f604;
        }

        public void setF604(String f604) {
            this.f604 = f604;
            notifyPropertyChanged(BR.f604);
        }

        @Bindable
        public String getF604a() {
            return f604a;
        }

        public void setF604a(String f604a) {
            this.f604a = f604a;
            setF604b(f604a.equals("2") ? this.f604b : _EMPTY_);
            if(!f604a.equals("1")) {
                setF605(_EMPTY_);
                setF606(_EMPTY_);
                setF607(_EMPTY_);
                setF607a(_EMPTY_);
                setF608(_EMPTY_);
                setF609(_EMPTY_);
                setF610a(_EMPTY_);
                setF610b(_EMPTY_);
                setF610c(_EMPTY_);
                setF610d(_EMPTY_);
                setF610e(_EMPTY_);
                setF610f(_EMPTY_);
                setF610g(_EMPTY_);
                setF611(_EMPTY_);
                setF612(_EMPTY_);
                setF613(_EMPTY_);
                setF613a(_EMPTY_);
                setF61497("97");
                setF61497(_EMPTY_);
                setF615(_EMPTY_);
                setF616(_EMPTY_);
                setF617(_EMPTY_);
                setF618(_EMPTY_);
                setF618a(_EMPTY_);
                setF618b(_EMPTY_);
            }
            notifyPropertyChanged(BR.f604a);
        }

        @Bindable
        public String getF604b() {
            return f604b;
        }

        public void setF604b(String f604b) {
            this.f604b = f604b;
            notifyPropertyChanged(BR.f604b);
        }

        @Bindable
        public String getF605() {
            return f605;
        }

        public void setF605(String f605) {
            this.f605 = f605;
            notifyPropertyChanged(BR.f605);
        }

        @Bindable
        public String getF606() {
            return f606;
        }

        public void setF606(String f606) {
            this.f606 = f606;
            notifyPropertyChanged(BR.f606);
        }

        @Bindable
        public String getF607() {
            return f607;
        }

        public void setF607(String f607) {
            this.f607 = f607;
            notifyPropertyChanged(BR.f607);
        }

        @Bindable
        public String getF607a() {
            return f607a;
        }

        public void setF607a(String f607a) {
            this.f607a = f607a;
            notifyPropertyChanged(BR.f607a);
        }

        @Bindable
        public String getF608() {
            return f608;
        }

        public void setF608(String f608) {
            this.f608 = f608;
            setF608a(f608.equals("1") ? this.f608a : _EMPTY_);
            notifyPropertyChanged(BR.f608);
        }

        @Bindable
        public String getF608a() {
            return f608a;
        }

        public void setF608a(String f608a) {
            this.f608a = f608a;
            notifyPropertyChanged(BR.f608a);
        }

        @Bindable
        public String getF609() {
            return f609;
        }

        public void setF609(String f609) {
            this.f609 = f609;
            notifyPropertyChanged(BR.f609);
        }

        @Bindable
        public String getF610a() {
            return f610a;
        }

        public void setF610a(String f610a) {
            this.f610a = f610a;
            notifyPropertyChanged(BR.f610a);
        }

        @Bindable
        public String getF610b() {
            return f610b;
        }

        public void setF610b(String f610b) {
            this.f610b = f610b;
            notifyPropertyChanged(BR.f610b);
        }

        @Bindable
        public String getF610c() {
            return f610c;
        }

        public void setF610c(String f610c) {
            this.f610c = f610c;
            notifyPropertyChanged(BR.f610c);
        }

        @Bindable
        public String getF610d() {
            return f610d;
        }

        public void setF610d(String f610d) {
            this.f610d = f610d;
            notifyPropertyChanged(BR.f610d);
        }

        @Bindable
        public String getF610e() {
            return f610e;
        }

        public void setF610e(String f610e) {
            this.f610e = f610e;
            notifyPropertyChanged(BR.f610e);
        }

        @Bindable
        public String getF610f() {
            return f610f;
        }

        public void setF610f(String f610f) {
            this.f610f = f610f;
            notifyPropertyChanged(BR.f610f);
        }

        @Bindable
        public String getF610g() {
            return f610g;
        }

        public void setF610g(String f610g) {
            this.f610g = f610g;
            notifyPropertyChanged(BR.f610g);
        }

        @Bindable
        public String getF611() {
            return f611;
        }

        public void setF611(String f611) {
            this.f611 = f611;
            notifyPropertyChanged(BR.f611);
        }

        @Bindable
        public String getF612() {
            return f612;
        }

        public void setF612(String f612) {
            this.f612 = f612;
            setF61296x(f612.equals("96") ? this.f61296x: _EMPTY_);
            notifyPropertyChanged(BR.f612);
        }

        @Bindable
        public String getF61296x() {
            return f61296x;
        }

        public void setF61296x(String f61296x) {
            this.f61296x = f61296x;
            notifyPropertyChanged(BR.f61296x);
        }

        @Bindable
        public String getF613() {
            return f613;
        }

        public void setF613(String f613) {
            this.f613 = f613;
            setF61396x(f613.equals("96") ? this.f61396x: _EMPTY_);
            notifyPropertyChanged(BR.f613);
        }

        @Bindable
        public String getF61396x() {
            return f61396x;
        }

        public void setF61396x(String f61396x) {
            this.f61396x = f61396x;
            notifyPropertyChanged(BR.f61396x);
        }

        @Bindable
        public String getF613a() {
            return f613a;
        }

        public void setF613a(String f613a) {
            this.f613a = f613a;
            setF613a02x(f613a.equals("2") ? this.f613a02x: _EMPTY_);
            notifyPropertyChanged(BR.f613a);
        }

        @Bindable
        public String getF613a02x() {
            return f613a02x;
        }

        public void setF613a02x(String f613a02x) {
            this.f613a02x = f613a02x;
            notifyPropertyChanged(BR.f613a02x);
        }

        @Bindable
        public String getF61401() {
            return f61401;
        }

        public void setF61401(String f61401) {
            this.f61401 = f61401;
            notifyPropertyChanged(BR.f61401);
        }

        @Bindable
        public String getF61402() {
            return f61402;
        }

        public void setF61402(String f61402) {
            this.f61402 = f61402;
            notifyPropertyChanged(BR.f61402);
        }

        @Bindable
        public String getF61403() {
            return f61403;
        }

        public void setF61403(String f61403) {
            this.f61403 = f61403;
            notifyPropertyChanged(BR.f61403);
        }

        @Bindable
        public String getF61404() {
            return f61404;
        }

        public void setF61404(String f61404) {
            this.f61404 = f61404;
            notifyPropertyChanged(BR.f61404);
        }

        @Bindable
        public String getF61405() {
            return f61405;
        }

        public void setF61405(String f61405) {
            this.f61405 = f61405;
            notifyPropertyChanged(BR.f61405);
        }

        @Bindable
        public String getF61496() {
            return f61496;
        }

        public void setF61496(String f61496) {
            this.f61496 = f61496;
            setF61496x(f61496.equals("96") ? this.f61496x: _EMPTY_);
            notifyPropertyChanged(BR.f61496);
        }

        @Bindable
        public String getF61496x() {
            return f61496x;
        }

        public void setF61496x(String f61496x) {
            this.f61496x = f61496x;
            notifyPropertyChanged(BR.f61496x);
        }

        @Bindable
        public String getF61497() {
            return f61497;
        }

        public void setF61497(String f61497) {
            this.f61497 = f61497;
            if(f61497.equals("97")) {
                setF61401(_EMPTY_);
                setF61402(_EMPTY_);
                setF61403(_EMPTY_);
                setF61404(_EMPTY_);
                setF61405(_EMPTY_);
                setF61496(_EMPTY_);
            }
            notifyPropertyChanged(BR.f61497);
        }

        @Bindable
        public String getF615() {
            return f615;
        }

        public void setF615(String f615) {
            this.f615 = f615;
            notifyPropertyChanged(BR.f615);
        }

        @Bindable
        public String getF616() {
            return f616;
        }

        public void setF616(String f616) {
            this.f616 = f616;
            setF617(f616.equals("1") ? this.f617 : _EMPTY_);
            setF618(f616.equals("1") ? this.f618 : _EMPTY_);
//            setF618a(f616.equals("1") ? this.f618a : _EMPTY_);
//            setF618b(f616.equals("1") ? this.f618b : _EMPTY_);
            notifyPropertyChanged(BR.f616);
        }

        @Bindable
        public String getF617() {
            return f617;
        }

        public void setF617(String f617) {
            this.f617 = f617;
            notifyPropertyChanged(BR.f617);
        }

        @Bindable
        public String getF618() {
            return f618;
        }

        public void setF618(String f618) {
            this.f618 = f618;
            setF61801x(f618.equals("1") ? this.f61801x : _EMPTY_);
            notifyPropertyChanged(BR.f618);
        }

        @Bindable
        public String getF61801x() {
            return f61801x;
        }

        public void setF61801x(String f61801x) {
            this.f61801x = f61801x;
            notifyPropertyChanged(BR.f61801x);
        }

        @Bindable
        public String getF618a() {
            return f618a;
        }

        public void setF618a(String f618a) {
            this.f618a = f618a;
            setF618b(f618a.equals("2") ? this.f618b : _EMPTY_);
            notifyPropertyChanged(BR.f618a);
        }

        @Bindable
        public String getF618b() {
            return f618b;
        }

        public void setF618b(String f618b) {
            this.f618b = f618b;
            setF618b96x(f618b.equals("96") ? this.f618b96x : _EMPTY_);
            notifyPropertyChanged(BR.f618b);
        }

        @Bindable
        public String getF618b96x() {
            return f618b96x;
        }

        public void setF618b96x(String f618b96x) {
            this.f618b96x = f618b96x;
            notifyPropertyChanged(BR.f618b96x);
        }

        @Bindable
        public String getF619() {
            return f619;
        }

        public void setF619(String f619) {
            this.f619 = f619;
            setF61901(f619.equals("1") ? this.f61901: _EMPTY_);
            setF61905(f619.equals("5") ? this.f61905: _EMPTY_);
            notifyPropertyChanged(BR.f619);
        }

        @Bindable
        public String getF61901() {
            return f61901;
        }

        public void setF61901(String f61901) {
            this.f61901 = f61901;
            notifyPropertyChanged(BR.f61901);
        }

        @Bindable
        public String getF61905() {
            return f61905;
        }

        public void setF61905(String f61905) {
            this.f61905 = f61905;
            notifyPropertyChanged(BR.f61905);
        }
    }
}
