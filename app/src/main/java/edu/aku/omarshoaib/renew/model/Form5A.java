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
import edu.aku.omarshoaib.renew.database.dao.Form05ADao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = Form5A.TABLE_NAME)
public class Form5A extends FormBaseModel{

    public static final String TABLE_NAME = "Form5a";
    public static final String SECTION_NAME = "Sections: F5a";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("_uuid")
    private String uuid = _EMPTY_;

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
    private SF5A sF5A;

    public Form5A() {
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.form5a = new Form5A();
        MainApp.form5a.setDistrictCode(MainApp.user.getDistId());
        MainApp.form5a.setParticipantId(MainApp.vFormF05a.getParticipantId());
        MainApp.form5a.setUuid(MainApp.vFormF05a.getUid());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String participantId) {
        Form05ADao dao = AppDatabase.getDBInstance().form05aDao();
        Form5A form = dao.getDataByParticipantId(MainApp.user.getDistId(), participantId);
        if (form != null) {
            MainApp.form5a = form;
        } else {
            MainApp.form5a.setUid(AppConstants.generateUid());
            MainApp.form5a.setId(dao.add(MainApp.form5a));
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

    public void setParticipantId(String scrId) {
        this.participantId = scrId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public SF5A getSF5A() {
        return sF5A;
    }

    public void setSF5A(SF5A sF5A) {
        this.sF5A = sF5A;
    }

    /**
     * Form 5a:
     */
    public static class SF5A extends BaseObservable {
        private String f5a = _EMPTY_;
        private String f5b = _EMPTY_;
        private String f5c = _EMPTY_;
        private String f5d = _EMPTY_;
        private String f5e = _EMPTY_;
        private String f5f = _EMPTY_;
        private String f5g = _EMPTY_;
        private String f5h = _EMPTY_;
        private String f5i = _EMPTY_;
        private String f501 = _EMPTY_;
        private String f502 = _EMPTY_;
//        private String f503 = _EMPTY_;
        private String f504 = _EMPTY_;
        private String f505 = _EMPTY_;
        private String f506 = _EMPTY_;
        private String f507dd = _EMPTY_;
        private String f507mm = _EMPTY_;
        private String f508 = _EMPTY_;
        private String f509 = _EMPTY_;
        private String f510 = _EMPTY_;
        private String f511 = _EMPTY_;
        private String f512 = _EMPTY_;
        private String f513 = _EMPTY_;
        private String f513a = _EMPTY_;
        private String fo514 = _EMPTY_;
        private String fo514a1 = _EMPTY_;
        private String fo514a = _EMPTY_;
        private String fo515 = _EMPTY_;
        private String f515a = _EMPTY_;
        private String f515b = _EMPTY_;
        private String f516 = _EMPTY_;
        private String f517a = _EMPTY_;
        private String f517b = _EMPTY_;
        private String f517c = _EMPTY_;
        private String f517d = _EMPTY_;
        private String f517e = _EMPTY_;
        private String f517f = _EMPTY_;
        private String f517g = _EMPTY_;
        private String f518 = _EMPTY_;
        private String f519 = _EMPTY_;
        private String f520 = _EMPTY_;
        private String f52001x = _EMPTY_;
        private String f521 = _EMPTY_;
        private String f52101x = _EMPTY_;
        private String f52102x = _EMPTY_;
        private String f52196x = _EMPTY_;
        private String f521a = _EMPTY_;
        private String f522 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<Form5A.SF5A> {
            public DataConverter() {
                super(new TypeToken<Form5A.SF5A>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(Form5A.SF5A data) {
            MainApp.form5a.setSF5A(data);
            return AppDatabase.getDBInstance().form05aDao().update(MainApp.form5a);
        }

        // Get section object by parsing JSON
        public static Form5A.SF5A getData() {
            return MainApp.form5a.getSF5A();
        }

        @Bindable
        public String getF5a() {
            return f5a;
        }

        public void setF5a(String f5a) {
            this.f5a = f5a;
            notifyPropertyChanged(BR.f5a);
        }

        @Bindable
        public String getF5b() {
            return f5b;
        }

        public void setF5b(String f5b) {
            this.f5b = f5b;
            notifyPropertyChanged(BR.f5b);
        }

        @Bindable
        public String getF5c() {
            return f5c;
        }

        public void setF5c(String f5c) {
            this.f5c = f5c;
            notifyPropertyChanged(BR.f5c);
        }

        @Bindable
        public String getF5d() {
            return f5d;
        }

        public void setF5d(String f5d) {
            this.f5d = f5d;
            notifyPropertyChanged(BR.f5d);
        }

        @Bindable
        public String getF5e() {
            return f5e;
        }

        public void setF5e(String f5e) {
            this.f5e = f5e;
            notifyPropertyChanged(BR.f5e);
        }

        @Bindable
        public String getF5f() {
            return f5f;
        }

        public void setF5f(String f5f) {
            this.f5f = f5f;
            notifyPropertyChanged(BR.f5f);
        }

        @Bindable
        public String getF5g() {
            return f5g;
        }

        public void setF5g(String f5g) {
            this.f5g = f5g;
            notifyPropertyChanged(BR.f5g);
        }

        @Bindable
        public String getF5h() {
            return f5h;
        }

        public void setF5h(String f5h) {
            this.f5h = f5h;
            notifyPropertyChanged(BR.f5h);
        }

        @Bindable
        public String getF5i() {
            return f5i;
        }

        public void setF5i(String f5i) {
            this.f5i = f5i;
            notifyPropertyChanged(BR.f5i);
        }

        @Bindable
        public String getF501() {
            return f501;
        }

        public void setF501(String f501) {
            this.f501 = f501;
            notifyPropertyChanged(BR.f501);
        }

        @Bindable
        public String getF502() {
            return f502;
        }

        public void setF502(String f502) {
            this.f502 = f502;
            notifyPropertyChanged(BR.f502);
        }

        /*@Bindable
        public String getF503() {
            return f503;
        }

        public void setF503(String f503) {
            this.f503 = f503;
            if(!f503.equals("1")) {
            }
            notifyPropertyChanged(BR.f503);
        }*/

        @Bindable
        public String getF504() {
            return f504;
        }

        public void setF504(String f504) {
            this.f504 = f504;
            notifyPropertyChanged(BR.f504);
        }

        @Bindable
        public String getF505() {
            return f505;
        }

        public void setF505(String f505) {
            this.f505 = f505;
            notifyPropertyChanged(BR.f505);
        }

        @Bindable
        public String getF506() {
            return f506;
        }

        public void setF506(String f506) {
            this.f506 = f506;
            notifyPropertyChanged(BR.f506);
        }

        @Bindable
        public String getF507dd() {
            return f507dd;
        }

        public void setF507dd(String f507dd) {
            this.f507dd = f507dd;
            notifyPropertyChanged(BR.f507dd);
        }

        @Bindable
        public String getF507mm() {
            return f507mm;
        }

        public void setF507mm(String f507mm) {
            this.f507mm = f507mm;
            notifyPropertyChanged(BR.f507mm);
        }

        @Bindable
        public String getF508() {
            return f508;
        }

        public void setF508(String f508) {
            this.f508 = f508;
            notifyPropertyChanged(BR.f508);
        }

        @Bindable
        public String getF509() {
            return f509;
        }

        public void setF509(String f509) {
            this.f509 = f509;
            notifyPropertyChanged(BR.f509);
        }

        @Bindable
        public String getF510() {
            return f510;
        }

        public void setF510(String f510) {
            this.f510 = f510;
            notifyPropertyChanged(BR.f510);
        }

        @Bindable
        public String getF511() {
            return f511;
        }

        public void setF511(String f511) {
            this.f511 = f511;
            notifyPropertyChanged(BR.f511);
        }

        @Bindable
        public String getF512() {
            return f512;
        }

        public void setF512(String f512) {
            this.f512 = f512;
            notifyPropertyChanged(BR.f512);
        }

        @Bindable
        public String getF513() {
            return f513;
        }

        public void setF513(String f513) {
            this.f513 = f513;
            calculateRUTF();
            notifyPropertyChanged(BR.f513);
        }

        @Bindable
        public String getF513a() {
            return f513a;
        }

        public void setF513a(String f513a) {
            this.f513a = f513a;
            notifyPropertyChanged(BR.f513a);
        }

        @Bindable
        public String getFo514() {
            return fo514;
        }

        public void setFo514(String fo514) {
            this.fo514 = fo514;
            notifyPropertyChanged(BR.fo514);
        }

        @Bindable
        public String getFo514a1() {
            return fo514a1;
        }

        public void setFo514a1(String fo514a1) {
            this.fo514a1 = fo514a1;
            notifyPropertyChanged(BR.fo514a1);
        }

        @Bindable
        public String getFo514a() {
            return fo514a;
        }

        public void setFo514a(String fo514a) {
            this.fo514a = fo514a;
            notifyPropertyChanged(BR.fo514a);
        }

        @Bindable
        public String getFo515() {
            return fo515;
        }

        public void setFo515(String fo515) {
            this.fo515 = fo515;
            notifyPropertyChanged(BR.fo515);
        }

        @Bindable
        public String getF515a() {
            return f515a;
        }

        public void setF515a(String f515a) {
            this.f515a = f515a;
            notifyPropertyChanged(BR.f515a);
        }

        @Bindable
        public String getF515b() {
            return f515b;
        }

        public void setF515b(String f515b) {
            this.f515b = f515b;
            setF516(f515b.equals("2") ? this.f516 : _EMPTY_);
            notifyPropertyChanged(BR.f515b);
        }

        public void clearUnEligible() {
            setF516(_EMPTY_);
            setF517a(_EMPTY_);
            setF517b(_EMPTY_);
            setF517c(_EMPTY_);
            setF517d(_EMPTY_);
            setF517e(_EMPTY_);
            setF517f(_EMPTY_);
            setF517g(_EMPTY_);
            setF518(_EMPTY_);
            setF519(_EMPTY_);
            setF520(_EMPTY_);
            setF521(_EMPTY_);
        }

        @Bindable
        public String getF516() {
            return f516;
        }

        public void setF516(String f516) {
            this.f516 = f516;
            notifyPropertyChanged(BR.f516);
        }

        @Bindable
        public String getF517a() {
            return f517a;
        }

        public void setF517a(String f517a) {
            this.f517a = f517a;
            notifyPropertyChanged(BR.f517a);
        }

        @Bindable
        public String getF517b() {
            return f517b;
        }

        public void setF517b(String f517b) {
            this.f517b = f517b;
            notifyPropertyChanged(BR.f517b);
        }

        @Bindable
        public String getF517c() {
            return f517c;
        }

        public void setF517c(String f517c) {
            this.f517c = f517c;
            notifyPropertyChanged(BR.f517c);
        }

        @Bindable
        public String getF517d() {
            return f517d;
        }

        public void setF517d(String f517d) {
            this.f517d = f517d;
            notifyPropertyChanged(BR.f517d);
        }

        @Bindable
        public String getF517e() {
            return f517e;
        }

        public void setF517e(String f517e) {
            this.f517e = f517e;
            notifyPropertyChanged(BR.f517e);
        }

        @Bindable
        public String getF517f() {
            return f517f;
        }

        public void setF517f(String f517f) {
            this.f517f = f517f;
            notifyPropertyChanged(BR.f517f);
        }

        @Bindable
        public String getF517g() {
            return f517g;
        }

        public void setF517g(String f517g) {
            this.f517g = f517g;
            notifyPropertyChanged(BR.f517g);
        }

        @Bindable
        public String getF518() {
            return f518;
        }

        public void setF518(String f518) {
            this.f518 = f518;
            notifyPropertyChanged(BR.f518);
        }

        @Bindable
        public String getF519() {
            return f519;
        }

        public void setF519(String f519) {
            this.f519 = f519;
            notifyPropertyChanged(BR.f519);
        }

        @Bindable
        public String getF520() {
            return f520;
        }

        public void setF520(String f520) {
            this.f520 = f520;
            setF52001x(f520.equals("1") ? this.f52001x: _EMPTY_);
            notifyPropertyChanged(BR.f520);
        }

        @Bindable
        public String getF52001x() {
            return f52001x;
        }

        public void setF52001x(String f52001x) {
            this.f52001x = f52001x;
            notifyPropertyChanged(BR.f52001x);
        }

        @Bindable
        public String getF521() {
            return f521;
        }

        public void setF521(String f521) {
            this.f521 = f521;
            setF521a(f521.equals("1") || f521.equals("2")? this.f521a : _EMPTY_);
            setF52101x(f521.equals("1") ? "30" : _EMPTY_);
            calculateRUTF();
            notifyPropertyChanged(BR.f521);
        }

        private void calculateRUTF() {
            if(this.f513.isEmpty()) return;
            float weight = Float.parseFloat(this.f513);
            float rutfValue = weight == 0 ? 150f : weight*((float) 150/500)*15;// 4.5f;
            setF52102x(f521.equals("2") ? String.valueOf(Math.round(rutfValue)) : _EMPTY_);
        }

        @Bindable
        public String getF52101x() {
            return f52101x;
        }

        public void setF52101x(String f52101x) {
            this.f52101x = f52101x;
            notifyPropertyChanged(BR.f52101x);
        }

        @Bindable
        public String getF52102x() {
            return f52102x;
        }

        public void setF52102x(String f52102x) {
            this.f52102x = f52102x;
            notifyPropertyChanged(BR.f52102x);
        }

        @Bindable
        public String getF52196x() {
            return f52196x;
        }

        public void setF52196x(String f52196x) {
            this.f52196x = f52196x;
            notifyPropertyChanged(BR.f52196x);
        }

        @Bindable
        public String getF521a() {
            return f521a;
        }

        public void setF521a(String f521a) {
            this.f521a = f521a;
            notifyPropertyChanged(BR.f521a);
        }

        @Bindable
        public String getF522() {
            return f522;
        }

        public void setF522(String f522) {
            this.f522 = f522;
            notifyPropertyChanged(BR.f522);
        }
    }
}