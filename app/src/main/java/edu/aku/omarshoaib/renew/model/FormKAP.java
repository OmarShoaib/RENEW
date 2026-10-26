package edu.aku.omarshoaib.renew.model;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.aku.omarshoaib.renew.BR;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.database.dao.Form1Dao;
import edu.aku.omarshoaib.renew.database.dao.FormKAPDao;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;

@Entity(tableName = FormKAP.TABLE_NAME)
public class FormKAP extends FormBaseModel {

    public static final String TABLE_NAME = "FormKAP";
    public static final String SECTION_NAME = "Sections: F1, F1A";
    // Only for Main Table i.e. Module Table like Form1.
    // These fields are used to display on Synced Recs list.
    // Dynamic approach + Sequence matters
    public static final String SYNCED_RECS_ITEMS = "scrId, districtCode, sysDate";

    @SerializedName("dist_id")
    private String districtCode = _EMPTY_;

    @SerializedName("scr_id")
    private String scrId = _EMPTY_;

    @SerializedName("ending_date")
    private String endingDate = _EMPTY_;

    @SerializedName("team_id")
    private String teamId = _EMPTY_;

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
    private SA1 sA1;
    private SA2 sA2;
    private SA3 sA3;

    private SB1 sB1;
    private SB2 sB2;
    private SB3 sB3;
    private SB4 sB4;

    private SC sC;

    private SD sD;

    private SE1 sE1;
    private SE2 sE2;

    public SA1 getSA1() {
        return sA1;
    }

    public void setSA1(SA1 sA) {
        this.sA1 = sA;
    }

    public SA2 getSA2() {
        return sA2;
    }

    public void setSA2(SA2 sA) {
        this.sA2 = sA;
    }

    public SA3 getSA3() {
        return sA3;
    }

    public void setSA3(SA3 sA) {
        this.sA3 = sA;
    }

    public SB1 getSB1() {
        return sB1;
    }

    public void setSB1(SB1 sB1){
        this.sB1 = sB1;
    }

    public SB2 getSB2() {
        return sB2;
    }

    public void setSB2(SB2 sB2){
        this.sB2 = sB2;
    }

    public SB3 getSB3() {
        return sB3;
    }

    public void setSB3(SB3 sB3){
        this.sB3 = sB3;
    }

    public SB4 getSB4() {
        return sB4;
    }

    public void setSB4(SB4 sB4){
        this.sB4 = sB4;
    }

    public SC getSC() {
        return sC;
    }

    public void setSC(SC sC){
        this.sC = sC;
    }

    public SD getSD() {
        return sD;
    }

    public void setSD(SD sD){
        this.sD = sD;
    }

    public SE1 getSE1() {
        return sE1;
    }

    public void setSE1(SE1 sE1){
        this.sE1 = sE1;
    }

    public SE2 getSE2() {
        return sE2;
    }

    public void setSE2(SE2 sE2){
        this.sE2 = sE2;
    }

    // Init default data
    public static void initMeta() {
        // This is used to add record for the first time
        MainApp.formKAP = new FormKAP();
        MainApp.formKAP.setDistrictCode(MainApp.user.getDistId());
    }

    /*FOR IDENTIFICATION INFORMATION - CLUSTER-WISE*/
    // Save data in db
    public static void saveMainData(String scrId) {
        FormKAPDao formsKAPDao = AppDatabase.getDBInstance().formKAPDao();
        FormKAP formKAP = formsKAPDao.getDataByScrId(MainApp.user.getDistId(), scrId);
        if (formKAP != null) {
            MainApp.formKAP = formKAP;
        } else {
            MainApp.formKAP.setUid(AppConstants.generateUid());
            MainApp.formKAP.setId(formsKAPDao.add(MainApp.formKAP));
        }
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getScrId() {
        return scrId;
    }

    public void setScrId(String scrId) {
        this.scrId = scrId;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean isFormCompleteOnce() {
        return isFormCompleteOnce;
    }

    public void setFormCompleteOnce(boolean formCompleteOnce) {
        isFormCompleteOnce = formCompleteOnce;
    }

    /**
     * Part 1: Identification and eligibility screening
     */
    public static class SA1 extends BaseObservable {
        private String a101 = _EMPTY_;
        private String a102 = _EMPTY_;
        private String a103m = _EMPTY_;
        private String a103d = _EMPTY_;
        private String a104 = _EMPTY_;
        private String a105 = _EMPTY_;
        private String a106 = _EMPTY_;
        private String a107 = _EMPTY_;
        private String a108 = _EMPTY_;
        private String a109 = _EMPTY_;
        private String a110 = _EMPTY_;
        private String a111 = _EMPTY_;
        private String a112 = _EMPTY_;
        private String a113 = _EMPTY_;
        private String a115 = _EMPTY_;
        private String a116 = _EMPTY_;
        private String a117 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SA1> {
            public DataConverter() {
                super(new TypeToken<SA1>() {
                }.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SA1 data) {
            MainApp.formKAP.setSA1(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SA1 getData() {
            return MainApp.formKAP.getSA1();
        }

        @Bindable
        public String getA101() {
            return a101;
        }

        public void setA101(String a101) {
            this.a101 = a101;
            notifyPropertyChanged(BR.a101);
        }

        @Bindable
        public String getA102() {
            return a102;
        }

        public void setA102(String a102) {
            this.a102 = a102;
            notifyPropertyChanged(BR.a102);
        }

        @Bindable
        public String getA103m() {
            return a103m;
        }

        public void setA103m(String a103m) {
            this.a103m = a103m;
            notifyPropertyChanged(BR.a103m);
        }

        @Bindable
        public String getA103d() {
            return a103d;
        }

        public void setA103d(String a103d) {
            this.a103d = a103d;
            notifyPropertyChanged(BR.a103d);
        }

        @Bindable
        public String getA104() {
            return a104;
        }

        public void setA104(String a104) {
            this.a104 = a104;
            notifyPropertyChanged(BR.a104);
        }

        @Bindable
        public String getA105() {
            return a105;
        }

        public void setA105(String a105) {
            this.a105 = a105;
            notifyPropertyChanged(BR.a105);
        }

        @Bindable
        public String getA106() {
            return a106;
        }

        public void setA106(String a106) {
            this.a106 = a106;
            notifyPropertyChanged(BR.a106);
        }

        @Bindable
        public String getA107() {
            return a107;
        }

        public void setA107(String a107) {
            this.a107 = a107;
            notifyPropertyChanged(BR.a107);
        }

        @Bindable
        public String getA108() {
            return a108;
        }

        public void setA108(String a108) {
            this.a108 = a108;
            notifyPropertyChanged(BR.a108);
        }

        @Bindable
        public String getA109() {
            return a109;
        }

        public void setA109(String a109) {
            this.a109 = a109;
            notifyPropertyChanged(BR.a109);
        }

        @Bindable
        public String getA110() {
            return a110;
        }

        public void setA110(String a110) {
            this.a110 = a110;
            notifyPropertyChanged(BR.a110);
        }

        @Bindable
        public String getA111() {
            return a111;
        }

        public void setA111(String a111) {
            this.a111 = a111;
            notifyPropertyChanged(BR.a111);
        }

        @Bindable
        public String getA112() {
            return a112;
        }

        public void setA112(String a112) {
            this.a112 = a112;
            notifyPropertyChanged(BR.a112);
        }

        @Bindable
        public String getA113() {
            return a113;
        }

        public void setA113(String a113) {
            this.a113 = a113;
            notifyPropertyChanged(BR.a113);
        }

        @Bindable
        public String getA115() {
            return a115;
        }

        public void setA115(String a115) {
            this.a115 = a115;
            notifyPropertyChanged(BR.a115);
        }

        @Bindable
        public String getA116() {
            return a116;
        }

        public void setA116(String a116) {
            this.a116 = a116;
            notifyPropertyChanged(BR.a116);
        }

        @Bindable
        public String getA117() {
            return a117;
        }

        public void setA117(String a117) {
            this.a117 = a117;
            notifyPropertyChanged(BR.a117);
        }
    }

    /**
     * Part 2: Identification and eligibility screening
     */
    public static class SA2 extends BaseObservable {
        private String a201 = _EMPTY_;
        private String a202 = _EMPTY_;
        private String a20296x = _EMPTY_;
        private String a203 = _EMPTY_;
        private String a20396x = _EMPTY_;
        private String a204 = _EMPTY_;
        private String a20496x = _EMPTY_;
        private String a205 = _EMPTY_;
        private String a20596x = _EMPTY_;
        private String a206 = _EMPTY_;
        private String a207 = _EMPTY_;
        private String a20796x = _EMPTY_;
        private String a208 = _EMPTY_;
        private String a20896x = _EMPTY_;
        private String a209 = _EMPTY_;
        private String a20996x = _EMPTY_;
        private String a210 = _EMPTY_;
        private String a21096x = _EMPTY_;
        private String a211 = _EMPTY_;
        private String a212 = _EMPTY_;
        private String a21301 = _EMPTY_;
        private String a21302 = _EMPTY_;
        private String a21303 = _EMPTY_;
        private String a21304 = _EMPTY_;
        private String a21396 = _EMPTY_;
        private String a21396x = _EMPTY_;
        private String a214a = _EMPTY_;
        private String a214b = _EMPTY_;
        private String a214c = _EMPTY_;
        private String a214d = _EMPTY_;
        private String a214e = _EMPTY_;
        private String a214f = _EMPTY_;
        private String a214g = _EMPTY_;
        private String a214h = _EMPTY_;
        private String a214i = _EMPTY_;
        private String a214j = _EMPTY_;
        private String a214k = _EMPTY_;
        private String a214l = _EMPTY_;
        private String a214m = _EMPTY_;
        private String a214n = _EMPTY_;
        private String a214o = _EMPTY_;
        private String a214p = _EMPTY_;
        private String a214q = _EMPTY_;
        private String a214r = _EMPTY_;
        private String a215 = _EMPTY_;
        private String a21596x = _EMPTY_;
        private String a216 = _EMPTY_;
        private String a21696x = _EMPTY_;
        private String a217 = _EMPTY_;
        private String a217a = _EMPTY_;
        private String a217b = _EMPTY_;
        private String a217c = _EMPTY_;
        private String a217d = _EMPTY_;
        private String a217e = _EMPTY_;
        private String a217f = _EMPTY_;
        private String a217g = _EMPTY_;
        private String a217h = _EMPTY_;
        private String a217i = _EMPTY_;
        private String a218 = _EMPTY_;
        private String a219 = _EMPTY_;
        private String a220a = _EMPTY_;
        private String a220b = _EMPTY_;
        private String a220c = _EMPTY_;
        private String a220d = _EMPTY_;
        private String a220e = _EMPTY_;
        private String a220f = _EMPTY_;
        private String a220g = _EMPTY_;
        private String a221 = _EMPTY_;
        private String a22196x = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SA2> {
            public DataConverter() {
                super(new TypeToken<SA2>() {
                }.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SA2 data) {
            MainApp.formKAP.setSA2(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SA2 getData() {
            return MainApp.formKAP.getSA2();
        }

        @Bindable
        public String getA201() {
            return a201;
        }

        public void setA201(String a201) {
            this.a201 = a201;
            notifyPropertyChanged(BR.a201);
        }

        @Bindable
        public String getA202() {
            return a202;
        }

        public void setA202(String a202) {
            this.a202 = a202;
            setA20296x(a202.equals("96") ? this.a20296x : _EMPTY_);
            notifyPropertyChanged(BR.a202);
        }

        @Bindable
        public String getA20296x() {
            return a20296x;
        }

        public void setA20296x(String a20296x) {
            this.a20296x = a20296x;
            notifyPropertyChanged(BR.a20296x);
        }

        @Bindable
        public String getA203() {
            return a203;
        }

        public void setA203(String a203) {
            this.a203 = a203;
            setA20396x(a203.equals("96") ? this.a20396x : _EMPTY_);
            notifyPropertyChanged(BR.a203);
        }

        @Bindable
        public String getA20396x() {
            return a20396x;
        }

        public void setA20396x(String a20396x) {
            this.a20396x = a20396x;
            notifyPropertyChanged(BR.a20396x);
        }

        @Bindable
        public String getA204() {
            return a204;
        }

        public void setA204(String a204) {
            this.a204 = a204;
            setA20496x(a204.equals("96") ? this.a20496x : _EMPTY_);
            notifyPropertyChanged(BR.a204);
        }

        @Bindable
        public String getA20496x() {
            return a20496x;
        }

        public void setA20496x(String a20496x) {
            this.a20496x = a20496x;
            notifyPropertyChanged(BR.a20496x);
        }

        @Bindable
        public String getA205() {
            return a205;
        }

        public void setA205(String a205) {
            this.a205 = a205;
            setA20596x(a205.equals("96") ? this.a20596x : _EMPTY_);
            notifyPropertyChanged(BR.a205);
        }

        @Bindable
        public String getA20596x() {
            return a20596x;
        }

        public void setA20596x(String a20596x) {
            this.a20596x = a20596x;
            notifyPropertyChanged(BR.a20596x);
        }

        @Bindable
        public String getA206() {
            return a206;
        }

        public void setA206(String a206) {
            this.a206 = a206;
            notifyPropertyChanged(BR.a206);
        }

        @Bindable
        public String getA207() {
            return a207;
        }

        public void setA207(String a207) {
            this.a207 = a207;
            setA20796x(a207.equals("96") ? this.a20796x : _EMPTY_);
            notifyPropertyChanged(BR.a207);
        }

        @Bindable
        public String getA20796x() {
            return a20796x;
        }

        public void setA20796x(String a20796x) {
            this.a20796x = a20796x;
            notifyPropertyChanged(BR.a20796x);
        }

        @Bindable
        public String getA208() {
            return a208;
        }

        public void setA208(String a208) {
            this.a208 = a208;
            setA20896x(a208.equals("96") ? this.a20896x : _EMPTY_);
            notifyPropertyChanged(BR.a208);
        }

        @Bindable
        public String getA20896x() {
            return a20896x;
        }

        public void setA20896x(String a20896x) {
            this.a20896x = a20896x;
            notifyPropertyChanged(BR.a20896x);
        }

        @Bindable
        public String getA209() {
            return a209;
        }

        public void setA209(String a209) {
            this.a209 = a209;
            setA20996x(a209.equals("96") ? this.a20996x : _EMPTY_);
            notifyPropertyChanged(BR.a209);
        }

        @Bindable
        public String getA20996x() {
            return a20996x;
        }

        public void setA20996x(String a20996x) {
            this.a20996x = a20996x;
            notifyPropertyChanged(BR.a20996x);
        }

        @Bindable
        public String getA210() {
            return a210;
        }

        public void setA210(String a210) {
            this.a210 = a210;
            setA21096x(a210.equals("96") ? this.a21096x : _EMPTY_);
            notifyPropertyChanged(BR.a210);
        }

        @Bindable
        public String getA21096x() {
            return a21096x;
        }

        public void setA21096x(String a21096x) {
            this.a21096x = a21096x;
            notifyPropertyChanged(BR.a21096x);
        }

        @Bindable
        public String getA211() {
            return a211;
        }

        public void setA211(String a211) {
            this.a211 = a211;
            setA212(a211.equals("1") ? this.a212 : _EMPTY_);
            notifyPropertyChanged(BR.a211);
        }

        @Bindable
        public String getA212() {
            return a212;
        }

        public void setA212(String a212) {
            this.a212 = a212;
            notifyPropertyChanged(BR.a212);
        }

        @Bindable
        public String getA21301() {
            return a21301;
        }

        public void setA21301(String a21301) {
            this.a21301 = a21301;
            notifyPropertyChanged(BR.a21301);
        }

        @Bindable
        public String getA21302() {
            return a21302;
        }

        public void setA21302(String a21302) {
            this.a21302 = a21302;
            notifyPropertyChanged(BR.a21302);
        }

        @Bindable
        public String getA21303() {
            return a21303;
        }

        public void setA21303(String a21303) {
            this.a21303 = a21303;
            notifyPropertyChanged(BR.a21303);
        }

        @Bindable
        public String getA21304() {
            return a21304;
        }

        public void setA21304(String a21304) {
            this.a21304 = a21304;
            notifyPropertyChanged(BR.a21304);
        }

        @Bindable
        public String getA21396() {
            return a21396;
        }

        public void setA21396(String a21396) {
            this.a21396 = a21396;
            setA21396x(a21396.equals("96") ? this.a21396x : _EMPTY_);
            notifyPropertyChanged(BR.a21396);
        }

        @Bindable
        public String getA21396x() {
            return a21396x;
        }

        public void setA21396x(String a21396x) {
            this.a21396x = a21396x;
            notifyPropertyChanged(BR.a21396x);
        }

        @Bindable
        public String getA214a() {
            return a214a;
        }

        public void setA214a(String a214a) {
            this.a214a = a214a;
            notifyPropertyChanged(BR.a214a);
        }

        @Bindable
        public String getA214b() {
            return a214b;
        }

        public void setA214b(String a214b) {
            this.a214b = a214b;
            notifyPropertyChanged(BR.a214b);
        }

        @Bindable
        public String getA214c() {
            return a214c;
        }

        public void setA214c(String a214c) {
            this.a214c = a214c;
            notifyPropertyChanged(BR.a214c);
        }

        @Bindable
        public String getA214d() {
            return a214d;
        }

        public void setA214d(String a214d) {
            this.a214d = a214d;
            notifyPropertyChanged(BR.a214d);
        }

        @Bindable
        public String getA214e() {
            return a214e;
        }

        public void setA214e(String a214e) {
            this.a214e = a214e;
            notifyPropertyChanged(BR.a214e);
        }

        @Bindable
        public String getA214f() {
            return a214f;
        }

        public void setA214f(String a214f) {
            this.a214f = a214f;
            notifyPropertyChanged(BR.a214f);
        }

        @Bindable
        public String getA214g() {
            return a214g;
        }

        public void setA214g(String a214g) {
            this.a214g = a214g;
            notifyPropertyChanged(BR.a214g);
        }

        @Bindable
        public String getA214h() {
            return a214h;
        }

        public void setA214h(String a214h) {
            this.a214h = a214h;
            notifyPropertyChanged(BR.a214h);
        }

        @Bindable
        public String getA214i() {
            return a214i;
        }

        public void setA214i(String a214i) {
            this.a214i = a214i;
            notifyPropertyChanged(BR.a214i);
        }

        @Bindable
        public String getA214j() {
            return a214j;
        }

        public void setA214j(String a214j) {
            this.a214j = a214j;
            notifyPropertyChanged(BR.a214j);
        }

        @Bindable
        public String getA214k() {
            return a214k;
        }

        public void setA214k(String a214k) {
            this.a214k = a214k;
            notifyPropertyChanged(BR.a214k);
        }

        @Bindable
        public String getA214l() {
            return a214l;
        }

        public void setA214l(String a214l) {
            this.a214l = a214l;
            notifyPropertyChanged(BR.a214l);
        }

        @Bindable
        public String getA214m() {
            return a214m;
        }

        public void setA214m(String a214m) {
            this.a214m = a214m;
            notifyPropertyChanged(BR.a214m);
        }

        @Bindable
        public String getA214n() {
            return a214n;
        }

        public void setA214n(String a214n) {
            this.a214n = a214n;
            notifyPropertyChanged(BR.a214n);
        }

        @Bindable
        public String getA214o() {
            return a214o;
        }

        public void setA214o(String a214o) {
            this.a214o = a214o;
            notifyPropertyChanged(BR.a214o);
        }

        @Bindable
        public String getA214p() {
            return a214p;
        }

        public void setA214p(String a214p) {
            this.a214p = a214p;
            notifyPropertyChanged(BR.a214p);
        }

        @Bindable
        public String getA214q() {
            return a214q;
        }

        public void setA214q(String a214q) {
            this.a214q = a214q;
            notifyPropertyChanged(BR.a214q);
        }

        @Bindable
        public String getA214r() {
            return a214r;
        }

        public void setA214r(String a214r) {
            this.a214r = a214r;
            notifyPropertyChanged(BR.a214r);
        }

        @Bindable
        public String getA215() {
            return a215;
        }

        public void setA215(String a215) {
            this.a215 = a215;
            setA21596x(a215.equals("96") ? this.a21596x : _EMPTY_);
            notifyPropertyChanged(BR.a215);
        }

        @Bindable
        public String getA21596x() {
            return a21596x;
        }

        public void setA21596x(String a21596x) {
            this.a21596x = a21596x;
            notifyPropertyChanged(BR.a21596x);
        }

        @Bindable
        public String getA216() {
            return a216;
        }

        public void setA216(String a216) {
            this.a216 = a216;
            setA21696x(a216.equals("96") ? this.a21696x : _EMPTY_);
            notifyPropertyChanged(BR.a216);
        }

        @Bindable
        public String getA21696x() {
            return a21696x;
        }

        public void setA21696x(String a21696x) {
            this.a21696x = a21696x;
            notifyPropertyChanged(BR.a21696x);
        }

        @Bindable
        public String getA217() {
            return a217;
        }

        public void setA217(String a217) {
            this.a217 = a217;
            notifyPropertyChanged(BR.a217);
        }

        @Bindable
        public String getA217a() {
            return a217a;
        }

        public void setA217a(String a217a) {
            this.a217a = a217a;
            notifyPropertyChanged(BR.a217a);
        }

        @Bindable
        public String getA217b() {
            return a217b;
        }

        public void setA217b(String a217b) {
            this.a217b = a217b;
            notifyPropertyChanged(BR.a217b);
        }

        @Bindable
        public String getA217c() {
            return a217c;
        }

        public void setA217c(String a217c) {
            this.a217c = a217c;
            notifyPropertyChanged(BR.a217c);
        }

        @Bindable
        public String getA217d() {
            return a217d;
        }

        public void setA217d(String a217d) {
            this.a217d = a217d;
            notifyPropertyChanged(BR.a217d);
        }

        @Bindable
        public String getA217e() {
            return a217e;
        }

        public void setA217e(String a217e) {
            this.a217e = a217e;
            notifyPropertyChanged(BR.a217e);
        }

        @Bindable
        public String getA217f() {
            return a217f;
        }

        public void setA217f(String a217f) {
            this.a217f = a217f;
            notifyPropertyChanged(BR.a217f);
        }

        @Bindable
        public String getA217g() {
            return a217g;
        }

        public void setA217g(String a217g) {
            this.a217g = a217g;
            notifyPropertyChanged(BR.a217g);
        }

        @Bindable
        public String getA217h() {
            return a217h;
        }

        public void setA217h(String a217h) {
            this.a217h = a217h;
            notifyPropertyChanged(BR.a217h);
        }

        @Bindable
        public String getA217i() {
            return a217i;
        }

        public void setA217i(String a217i) {
            this.a217i = a217i;
            notifyPropertyChanged(BR.a217i);
        }

        @Bindable
        public String getA218() {
            return a218;
        }

        public void setA218(String a218) {
            this.a218 = a218;
            notifyPropertyChanged(BR.a218);
        }

        @Bindable
        public String getA219() {
            return a219;
        }

        public void setA219(String a219) {
            this.a219 = a219;
            setA220a(a219.equals("1") ? this.a220a : _EMPTY_);
            setA220b(a219.equals("1") ? this.a220b : _EMPTY_);
            setA220c(a219.equals("1") ? this.a220c : _EMPTY_);
            setA220d(a219.equals("1") ? this.a220d : _EMPTY_);
            setA220e(a219.equals("1") ? this.a220e : _EMPTY_);
            setA220f(a219.equals("1") ? this.a220f : _EMPTY_);
            setA220g(a219.equals("1") ? this.a220g : _EMPTY_);
            notifyPropertyChanged(BR.a219);
        }

        @Bindable
        public String getA220a() {
            return a220a;
        }

        public void setA220a(String a220a) {
            this.a220a = a220a;
            notifyPropertyChanged(BR.a220a);
        }

        @Bindable
        public String getA220b() {
            return a220b;
        }

        public void setA220b(String a220b) {
            this.a220b = a220b;
            notifyPropertyChanged(BR.a220b);
        }

        @Bindable
        public String getA220c() {
            return a220c;
        }

        public void setA220c(String a220c) {
            this.a220c = a220c;
            notifyPropertyChanged(BR.a220c);
        }

        @Bindable
        public String getA220d() {
            return a220d;
        }

        public void setA220d(String a220d) {
            this.a220d = a220d;
            notifyPropertyChanged(BR.a220d);
        }

        @Bindable
        public String getA220e() {
            return a220e;
        }

        public void setA220e(String a220e) {
            this.a220e = a220e;
            notifyPropertyChanged(BR.a220e);
        }

        @Bindable
        public String getA220f() {
            return a220f;
        }

        public void setA220f(String a220f) {
            this.a220f = a220f;
            notifyPropertyChanged(BR.a220f);
        }

        @Bindable
        public String getA220g() {
            return a220g;
        }

        public void setA220g(String a220g) {
            this.a220g = a220g;
            notifyPropertyChanged(BR.a220g);
        }

        @Bindable
        public String getA221() {
            return a221;
        }

        public void setA221(String a221) {
            this.a221 = a221;
            setA22196x(a221.equals("96") ? this.a22196x : _EMPTY_);
            notifyPropertyChanged(BR.a221);
        }

        @Bindable
        public String getA22196x() {
            return a22196x;
        }

        public void setA22196x(String a22196x) {
            this.a22196x = a22196x;
            notifyPropertyChanged(BR.a22196x);
        }
    }

    /**
     * Part 3: Identification and eligibility screening
     */
    public static class SA3 extends BaseObservable {
        private String a301d = _EMPTY_;
        private String a301m = _EMPTY_;
        private String a301y = _EMPTY_;
        private String a302 = _EMPTY_;
        private String a303 = _EMPTY_;
        private String a30396x = _EMPTY_;
        private String a304 = _EMPTY_;
        private String a30496x = _EMPTY_;
        private String a305 = _EMPTY_;
        private String a306 = _EMPTY_;
        private String a307 = _EMPTY_;
        private String a308 = _EMPTY_;
        private String a30896x = _EMPTY_;
        private String a309 = _EMPTY_;
        private String a310a = _EMPTY_;
        private String a310b = _EMPTY_;
        private String a310c = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SA3> {
            public DataConverter() {
                super(new TypeToken<SA3>() {
                }.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SA3 data) {
            MainApp.formKAP.setSA3(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SA3 getData() {
            return MainApp.formKAP.getSA3();
        }

        @Bindable
        public String getA301d() {
            return a301d;
        }

        public void setA301d(String a301d) {
            this.a301d = a301d;
            setAgeA302();
            notifyPropertyChanged(BR.a301d);
        }

        @Bindable
        public String getA301m() {
            return a301m;
        }

        public void setA301m(String a301m) {
            this.a301m = a301m;
            if (a301m.equals("98")) {
                setA301d("98");
                setA302(_EMPTY_);
            }
            else setAgeA302();
            notifyPropertyChanged(BR.a301m);
        }

        @Bindable
        public String getA301y() {
            return a301y;
        }

        public void setA301y(String a301y) {
            this.a301y = a301y;
            if (a301y.equals("9998")) {
                setA301m("98");
                setA302(_EMPTY_);
            }
            else setAgeA302();
            notifyPropertyChanged(BR.a301y);
        }

        private void setAgeA302() {
            List<String> age = DateUtils.calculateAge(this.a301y, this.a301m, this.a301d);
            setA302(age.get(0));
//            setA206m(age.get(1));
//            setA206d(age.get(2));
        }

        @Bindable
        public String getA302() {
            return a302;
        }

        public void setA302(String a302) {
            this.a302 = a302;
            notifyPropertyChanged(BR.a302);
        }

        @Bindable
        public String getA303() {
            return a303;
        }

        public void setA303(String a303) {
            this.a303 = a303;
            setA30396x(a303.equals("96") ? this.a30396x : _EMPTY_);
            notifyPropertyChanged(BR.a303);
        }

        @Bindable
        public String getA30396x() {
            return a30396x;
        }

        public void setA30396x(String a30396x) {
            this.a30396x = a30396x;
            notifyPropertyChanged(BR.a30396x);
        }

        @Bindable
        public String getA304() {
            return a304;
        }

        public void setA304(String a304) {
            this.a304 = a304;
            setA30496x(a304.equals("96") ? this.a30496x : _EMPTY_);
            notifyPropertyChanged(BR.a304);
        }

        @Bindable
        public String getA30496x() {
            return a30496x;
        }

        public void setA30496x(String a30496x) {
            this.a30496x = a30496x;
            notifyPropertyChanged(BR.a30496x);
        }

        @Bindable
        public String getA305() {
            return a305;
        }

        public void setA305(String a305) {
            this.a305 = a305;
            if(!a305.equals("1")) {
                setA306(_EMPTY_);
                setA307(_EMPTY_);
            }
            notifyPropertyChanged(BR.a305);
        }

        @Bindable
        public String getA306() {
            return a306;
        }

        public void setA306(String a306) {
            this.a306 = a306;
            notifyPropertyChanged(BR.a306);
        }

        @Bindable
        public String getA307() {
            return a307;
        }

        public void setA307(String a307) {
            this.a307 = a307;
            notifyPropertyChanged(BR.a307);
        }

        @Bindable
        public String getA308() {
            return a308;
        }

        public void setA308(String a308) {
            this.a308 = a308;
            setA30896x(a308.equals("96") ? this.a30896x : _EMPTY_);
            notifyPropertyChanged(BR.a308);
        }

        @Bindable
        public String getA30896x() {
            return a30896x;
        }

        public void setA30896x(String a30896x) {
            this.a30896x = a30896x;
            notifyPropertyChanged(BR.a30896x);
        }

        @Bindable
        public String getA309() {
            return a309;
        }

        public void setA309(String a309) {
            this.a309 = a309;
            notifyPropertyChanged(BR.a309);
        }

        @Bindable
        public String getA310a() {
            return a310a;
        }

        public void setA310a(String a310a) {
            this.a310a = a310a;
            notifyPropertyChanged(BR.a310a);
        }

        @Bindable
        public String getA310b() {
            return a310b;
        }

        public void setA310b(String a310b) {
            this.a310b = a310b;
            notifyPropertyChanged(BR.a310b);
        }

        @Bindable
        public String getA310c() {
            return a310c;
        }

        public void setA310c(String a310c) {
            this.a310c = a310c;
            notifyPropertyChanged(BR.a310c);
        }
    }

    /**
     * B1. PREPAREDNESS ACTIONS AND PLANNING (PAP)
     */
    public static class SB1 extends BaseObservable {
        private String b101 = _EMPTY_;
        private String b102 = _EMPTY_;
        private String b102a01 = _EMPTY_;
        private String b102a02 = _EMPTY_;
        private String b102a03 = _EMPTY_;
        private String b102a96 = _EMPTY_;
        private String b102a96x = _EMPTY_;
        private String b103 = _EMPTY_;
        private String b104 = _EMPTY_;
        private String b105 = _EMPTY_;
        private String b106 = _EMPTY_;
        private String b107 = _EMPTY_;
        private String b108 = _EMPTY_;
        private String b109 = _EMPTY_;
        private String b110 = _EMPTY_;
        private String b111 = _EMPTY_;
        private String b112 = _EMPTY_;
        private String b113 = _EMPTY_;
        private String b114 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SB1> {
            public DataConverter() {
                super(new TypeToken<SB1>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SB1 data) {
            MainApp.formKAP.setSB1(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SB1 getData() {
            return MainApp.formKAP.getSB1();
        }

        @Bindable
        public String getB101() {
            return b101;
        }

        public void setB101(String b101) {
            this.b101 = b101;
            notifyPropertyChanged(BR.b101);
        }

        @Bindable
        public String getB102() {
            return b102;
        }

        public void setB102(String b102) {
            this.b102 = b102;
            if(!b102.equals("1")) {
                setB102a01(_EMPTY_);
                setB102a02(_EMPTY_);
                setB102a03(_EMPTY_);
                setB102a96(_EMPTY_);
            }
            notifyPropertyChanged(BR.b102);
        }

        @Bindable
        public String getB102a01() {
            return b102a01;
        }

        public void setB102a01(String b102a01) {
            this.b102a01 = b102a01;
            notifyPropertyChanged(BR.b102a01);
        }

        @Bindable
        public String getB102a02() {
            return b102a02;
        }

        public void setB102a02(String b102a02) {
            this.b102a02 = b102a02;
            notifyPropertyChanged(BR.b102a02);
        }

        @Bindable
        public String getB102a03() {
            return b102a03;
        }

        public void setB102a03(String b102a03) {
            this.b102a03 = b102a03;
            notifyPropertyChanged(BR.b102a03);
        }

        @Bindable
        public String getB102a96() {
            return b102a96;
        }

        public void setB102a96(String b102a96) {
            this.b102a96 = b102a96;
            setB102a96x(b102a96.equals("96") ? this.b102a96x : _EMPTY_);
            notifyPropertyChanged(BR.b102a96);
        }

        @Bindable
        public String getB102a96x() {
            return b102a96x;
        }

        public void setB102a96x(String b102a96x) {
            this.b102a96x = b102a96x;
            notifyPropertyChanged(BR.b102a96x);
        }

        @Bindable
        public String getB103() {
            return b103;
        }

        public void setB103(String b103) {
            this.b103 = b103;
            notifyPropertyChanged(BR.b103);
        }

        @Bindable
        public String getB104() {
            return b104;
        }

        public void setB104(String b104) {
            this.b104 = b104;
            notifyPropertyChanged(BR.b104);
        }

        @Bindable
        public String getB105() {
            return b105;
        }

        public void setB105(String b105) {
            this.b105 = b105;
            notifyPropertyChanged(BR.b105);
        }

        @Bindable
        public String getB106() {
            return b106;
        }

        public void setB106(String b106) {
            this.b106 = b106;
            notifyPropertyChanged(BR.b106);
        }

        @Bindable
        public String getB107() {
            return b107;
        }

        public void setB107(String b107) {
            this.b107 = b107;
            notifyPropertyChanged(BR.b107);
        }

        @Bindable
        public String getB108() {
            return b108;
        }

        public void setB108(String b108) {
            this.b108 = b108;
            notifyPropertyChanged(BR.b108);
        }

        @Bindable
        public String getB109() {
            return b109;
        }

        public void setB109(String b109) {
            this.b109 = b109;
            notifyPropertyChanged(BR.b109);
        }

        @Bindable
        public String getB110() {
            return b110;
        }

        public void setB110(String b110) {
            this.b110 = b110;
            notifyPropertyChanged(BR.b110);
        }

        @Bindable
        public String getB111() {
            return b111;
        }

        public void setB111(String b111) {
            this.b111 = b111;
            notifyPropertyChanged(BR.b111);
        }

        @Bindable
        public String getB112() {
            return b112;
        }

        public void setB112(String b112) {
            this.b112 = b112;
            notifyPropertyChanged(BR.b112);
        }

        @Bindable
        public String getB113() {
            return b113;
        }

        public void setB113(String b113) {
            this.b113 = b113;
            notifyPropertyChanged(BR.b113);
        }

        @Bindable
        public String getB114() {
            return b114;
        }

        public void setB114(String b114) {
            this.b114 = b114;
            notifyPropertyChanged(BR.b114);
        }

    }

    /**
     * B2. EIGHT-ITEM DISASTER PREPAREDNESS QUESTIONNAIRE
     */
    public static class SB2 extends BaseObservable {
        private String b201 = _EMPTY_;
        private String b202 = _EMPTY_;
        private String b203 = _EMPTY_;
        private String b204 = _EMPTY_;
        private String b205 = _EMPTY_;
        private String b206 = _EMPTY_;
        private String b206a01 = _EMPTY_;
        private String b206a02 = _EMPTY_;
        private String b206a96 = _EMPTY_;
        private String b206a96x = _EMPTY_;
        private String b206b = _EMPTY_;
        private String b207 = _EMPTY_;
        private String b208 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SB2> {
            public DataConverter() {
                super(new TypeToken<SB2>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SB2 data) {
            MainApp.formKAP.setSB2(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SB2 getData() {
            return MainApp.formKAP.getSB2();
        }

        @Bindable
        public String getB201() {
            return b201;
        }

        public void setB201(String b201) {
            this.b201 = b201;
            notifyPropertyChanged(BR.b201);
        }

        @Bindable
        public String getB202() {
            return b202;
        }

        public void setB202(String b202) {
            this.b202 = b202;
            notifyPropertyChanged(BR.b202);
        }

        @Bindable
        public String getB203() {
            return b203;
        }

        public void setB203(String b203) {
            this.b203 = b203;
            notifyPropertyChanged(BR.b203);
        }

        @Bindable
        public String getB204() {
            return b204;
        }

        public void setB204(String b204) {
            this.b204 = b204;
            notifyPropertyChanged(BR.b204);
        }

        @Bindable
        public String getB205() {
            return b205;
        }

        public void setB205(String b205) {
            this.b205 = b205;
            notifyPropertyChanged(BR.b205);
        }

        @Bindable
        public String getB206() {
            return b206;
        }

        public void setB206(String b206) {
            this.b206 = b206;
            if(!b206.equals("1")) {
                setB206a01(_EMPTY_);
                setB206a02(_EMPTY_);
                setB206a96(_EMPTY_);
                setB206b(_EMPTY_);
            }
            notifyPropertyChanged(BR.b206);
        }

        @Bindable
        public String getB206a01() {
            return b206a01;
        }

        public void setB206a01(String b206a01) {
            this.b206a01 = b206a01;
            notifyPropertyChanged(BR.b206a01);
        }

        @Bindable
        public String getB206a02() {
            return b206a02;
        }

        public void setB206a02(String b206a02) {
            this.b206a02 = b206a02;
            notifyPropertyChanged(BR.b206a02);
        }

        @Bindable
        public String getB206a96() {
            return b206a96;
        }

        public void setB206a96(String b206a96) {
            this.b206a96 = b206a96;
            setB206a96x(b206a96.equals("96") ? this.b206a96x : _EMPTY_);
            notifyPropertyChanged(BR.b206a96);
        }

        @Bindable
        public String getB206a96x() {
            return b206a96x;
        }

        public void setB206a96x(String b206a96x) {
            this.b206a96x = b206a96x;
            notifyPropertyChanged(BR.b206a96x);
        }

        @Bindable
        public String getB206b() {
            return b206b;
        }

        public void setB206b(String b206b) {
            this.b206b = b206b;
            notifyPropertyChanged(BR.b206b);
        }

        @Bindable
        public String getB207() {
            return b207;
        }

        public void setB207(String b207) {
            this.b207 = b207;
            notifyPropertyChanged(BR.b207);
        }

        @Bindable
        public String getB208() {
            return b208;
        }

        public void setB208(String b208) {
            this.b208 = b208;
            notifyPropertyChanged(BR.b208);
        }

    }

    /**
     * B3. HEAT EMERGENCY AWARENESS AND TREATMENT (HEAT)

     */
    public static class SB3 extends BaseObservable {
        private String b301 = _EMPTY_;
        private String b302 = _EMPTY_;
        private String b303 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SB3> {
            public DataConverter() {
                super(new TypeToken<SB3>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SB3 data) {
            MainApp.formKAP.setSB3(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SB3 getData() {
            return MainApp.formKAP.getSB3();
        }

        @Bindable
        public String getB301() {
            return b301;
        }

        public void setB301(String b301) {
            this.b301 = b301;
            notifyPropertyChanged(BR.b301);
        }

        @Bindable
        public String getB302() {
            return b302;
        }

        public void setB302(String b302) {
            this.b302 = b302;
            notifyPropertyChanged(BR.b302);
        }

        @Bindable
        public String getB303() {
            return b303;
        }

        public void setB303(String b303) {
            this.b303 = b303;
            notifyPropertyChanged(BR.b303);
        }

    }

    /**
     * B4. CLIMATE-SENSITIVE DISEASES

     */
    public static class SB4 extends BaseObservable {
        private String b40101 = _EMPTY_;
        private String b40102 = _EMPTY_;
        private String b40103 = _EMPTY_;
        private String b40104 = _EMPTY_;
        private String b40105 = _EMPTY_;
        private String b40106 = _EMPTY_;
        private String b40107 = _EMPTY_;
        private String b40108 = _EMPTY_;
        private String b40198 = _EMPTY_;
        private String b40196 = _EMPTY_;
        private String b40196x = _EMPTY_;
        private String b40201 = _EMPTY_;
        private String b40202 = _EMPTY_;
        private String b40203 = _EMPTY_;
        private String b40204 = _EMPTY_;
        private String b40205 = _EMPTY_;
        private String b40206 = _EMPTY_;
        private String b40207 = _EMPTY_;
        private String b40208 = _EMPTY_;
        private String b40209 = _EMPTY_;
        private String b40298 = _EMPTY_;
        private String b40296 = _EMPTY_;
        private String b40296x = _EMPTY_;
        private String b40301 = _EMPTY_;
        private String b40302 = _EMPTY_;
        private String b40303 = _EMPTY_;
        private String b40304 = _EMPTY_;
        private String b40305 = _EMPTY_;
        private String b40306 = _EMPTY_;
        private String b40307 = _EMPTY_;
        private String b40398 = _EMPTY_;
        private String b40396 = _EMPTY_;
        private String b40396x = _EMPTY_;
        private String b40401 = _EMPTY_;
        private String b40402 = _EMPTY_;
        private String b40403 = _EMPTY_;
        private String b40404 = _EMPTY_;
        private String b40405 = _EMPTY_;
        private String b40498 = _EMPTY_;
        private String b40496 = _EMPTY_;
        private String b40496x = _EMPTY_;
        private String b40501 = _EMPTY_;
        private String b40502 = _EMPTY_;
        private String b40503 = _EMPTY_;
        private String b40504 = _EMPTY_;
        private String b40505 = _EMPTY_;
        private String b40506 = _EMPTY_;
        private String b40507 = _EMPTY_;
        private String b40508 = _EMPTY_;
        private String b40509 = _EMPTY_;
        private String b40510 = _EMPTY_;
        private String b40511 = _EMPTY_;
        private String b40598 = _EMPTY_;
        private String b40596 = _EMPTY_;
        private String b40596x = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SB4> {
            public DataConverter() {
                super(new TypeToken<SB4>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SB4 data) {
            MainApp.formKAP.setSB4(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SB4 getData() {
            return MainApp.formKAP.getSB4();
        }

        @Bindable
        public String getB40101() {
            return b40101;
        }

        public void setB40101(String b40101) {
            this.b40101 = b40101;
            notifyPropertyChanged(BR.b40101);
        }

        @Bindable
        public String getB40102() {
            return b40102;
        }

        public void setB40102(String b40102) {
            this.b40102 = b40102;
            notifyPropertyChanged(BR.b40102);
        }

        @Bindable
        public String getB40103() {
            return b40103;
        }

        public void setB40103(String b40103) {
            this.b40103 = b40103;
            notifyPropertyChanged(BR.b40103);
        }

        @Bindable
        public String getB40104() {
            return b40104;
        }

        public void setB40104(String b40104) {
            this.b40104 = b40104;
            notifyPropertyChanged(BR.b40104);
        }

        @Bindable
        public String getB40105() {
            return b40105;
        }

        public void setB40105(String b40105) {
            this.b40105 = b40105;
            notifyPropertyChanged(BR.b40105);
        }

        @Bindable
        public String getB40106() {
            return b40106;
        }

        public void setB40106(String b40106) {
            this.b40106 = b40106;
            notifyPropertyChanged(BR.b40106);
        }

        @Bindable
        public String getB40107() {
            return b40107;
        }

        public void setB40107(String b40107) {
            this.b40107 = b40107;
            notifyPropertyChanged(BR.b40107);
        }

        @Bindable
        public String getB40108() {
            return b40108;
        }

        public void setB40108(String b40108) {
            this.b40108 = b40108;
            notifyPropertyChanged(BR.b40108);
        }

        @Bindable
        public String getB40198() {
            return b40198;
        }

        public void setB40198(String b40198) {
            this.b40198 = b40198;
            if(b40198.equals("98")) {
                setB40101(_EMPTY_);
                setB40102(_EMPTY_);
                setB40103(_EMPTY_);
                setB40104(_EMPTY_);
                setB40105(_EMPTY_);
                setB40106(_EMPTY_);
                setB40107(_EMPTY_);
                setB40108(_EMPTY_);
                setB40196(_EMPTY_);
            }
            notifyPropertyChanged(BR.b40198);
        }

        @Bindable
        public String getB40196() {
            return b40196;
        }

        public void setB40196(String b40196) {
            this.b40196 = b40196;
            setB40196x(b40196.equals("96") ? this.b40196x : _EMPTY_);
            notifyPropertyChanged(BR.b40196);
        }

        @Bindable
        public String getB40196x() {
            return b40196x;
        }

        public void setB40196x(String b40196x) {
            this.b40196x = b40196x;
            notifyPropertyChanged(BR.b40196x);
        }

        @Bindable
        public String getB40201() {
            return b40201;
        }

        public void setB40201(String b40201) {
            this.b40201 = b40201;
            notifyPropertyChanged(BR.b40201);
        }

        @Bindable
        public String getB40202() {
            return b40202;
        }

        public void setB40202(String b40202) {
            this.b40202 = b40202;
            notifyPropertyChanged(BR.b40202);
        }

        @Bindable
        public String getB40203() {
            return b40203;
        }

        public void setB40203(String b40203) {
            this.b40203 = b40203;
            notifyPropertyChanged(BR.b40203);
        }

        @Bindable
        public String getB40204() {
            return b40204;
        }

        public void setB40204(String b40204) {
            this.b40204 = b40204;
            notifyPropertyChanged(BR.b40204);
        }

        @Bindable
        public String getB40205() {
            return b40205;
        }

        public void setB40205(String b40205) {
            this.b40205 = b40205;
            notifyPropertyChanged(BR.b40205);
        }

        @Bindable
        public String getB40206() {
            return b40206;
        }

        public void setB40206(String b40206) {
            this.b40206 = b40206;
            notifyPropertyChanged(BR.b40206);
        }

        @Bindable
        public String getB40207() {
            return b40207;
        }

        public void setB40207(String b40207) {
            this.b40207 = b40207;
            notifyPropertyChanged(BR.b40207);
        }

        @Bindable
        public String getB40208() {
            return b40208;
        }

        public void setB40208(String b40208) {
            this.b40208 = b40208;
            notifyPropertyChanged(BR.b40208);
        }

        @Bindable
        public String getB40209() {
            return b40209;
        }

        public void setB40209(String b40209) {
            this.b40209 = b40209;
            notifyPropertyChanged(BR.b40209);
        }

        @Bindable
        public String getB40298() {
            return b40298;
        }

        public void setB40298(String b40298) {
            this.b40298 = b40298;
            if(b40298.equals("98")) {
                setB40201(_EMPTY_);
                setB40202(_EMPTY_);
                setB40203(_EMPTY_);
                setB40204(_EMPTY_);
                setB40205(_EMPTY_);
                setB40206(_EMPTY_);
                setB40207(_EMPTY_);
                setB40208(_EMPTY_);
                setB40209(_EMPTY_);
                setB40296(_EMPTY_);
            }
            notifyPropertyChanged(BR.b40298);
        }

        @Bindable
        public String getB40296() {
            return b40296;
        }

        public void setB40296(String b40296) {
            this.b40296 = b40296;
            setB40296x(b40296.equals("96") ? this.b40296x : _EMPTY_);
            notifyPropertyChanged(BR.b40296);
        }

        @Bindable
        public String getB40296x() {
            return b40296x;
        }

        public void setB40296x(String b40296x) {
            this.b40296x = b40296x;
            notifyPropertyChanged(BR.b40296x);
        }

        @Bindable
        public String getB40301() {
            return b40301;
        }

        public void setB40301(String b40301) {
            this.b40301 = b40301;
            notifyPropertyChanged(BR.b40301);
        }

        @Bindable
        public String getB40302() {
            return b40302;
        }

        public void setB40302(String b40302) {
            this.b40302 = b40302;
            notifyPropertyChanged(BR.b40302);
        }

        @Bindable
        public String getB40303() {
            return b40303;
        }

        public void setB40303(String b40303) {
            this.b40303 = b40303;
            notifyPropertyChanged(BR.b40303);
        }

        @Bindable
        public String getB40304() {
            return b40304;
        }

        public void setB40304(String b40304) {
            this.b40304 = b40304;
            notifyPropertyChanged(BR.b40304);
        }

        @Bindable
        public String getB40305() {
            return b40305;
        }

        public void setB40305(String b40305) {
            this.b40305 = b40305;
            notifyPropertyChanged(BR.b40305);
        }

        @Bindable
        public String getB40306() {
            return b40306;
        }

        public void setB40306(String b40306) {
            this.b40306 = b40306;
            notifyPropertyChanged(BR.b40306);
        }

        @Bindable
        public String getB40307() {
            return b40307;
        }

        public void setB40307(String b40307) {
            this.b40307 = b40307;
            notifyPropertyChanged(BR.b40307);
        }

        @Bindable
        public String getB40398() {
            return b40398;
        }

        public void setB40398(String b40398) {
            this.b40398 = b40398;
            if(b40398.equals("98")) {
                setB40301(_EMPTY_);
                setB40302(_EMPTY_);
                setB40303(_EMPTY_);
                setB40304(_EMPTY_);
                setB40305(_EMPTY_);
                setB40306(_EMPTY_);
                setB40307(_EMPTY_);
                setB40396(_EMPTY_);
            }
            notifyPropertyChanged(BR.b40398);
        }

        @Bindable
        public String getB40396() {
            return b40396;
        }

        public void setB40396(String b40396) {
            this.b40396 = b40396;
            setB40396x(b40396.equals("96") ? this.b40396x : _EMPTY_);
            notifyPropertyChanged(BR.b40396);
        }

        @Bindable
        public String getB40396x() {
            return b40396x;
        }

        public void setB40396x(String b40396x) {
            this.b40396x = b40396x;
            notifyPropertyChanged(BR.b40396x);
        }

        @Bindable
        public String getB40401() {
            return b40401;
        }

        public void setB40401(String b40401) {
            this.b40401 = b40401;
            notifyPropertyChanged(BR.b40401);
        }

        @Bindable
        public String getB40402() {
            return b40402;
        }

        public void setB40402(String b40402) {
            this.b40402 = b40402;
            notifyPropertyChanged(BR.b40402);
        }

        @Bindable
        public String getB40403() {
            return b40403;
        }

        public void setB40403(String b40403) {
            this.b40403 = b40403;
            notifyPropertyChanged(BR.b40403);
        }

        @Bindable
        public String getB40404() {
            return b40404;
        }

        public void setB40404(String b40404) {
            this.b40404 = b40404;
            notifyPropertyChanged(BR.b40404);
        }

        @Bindable
        public String getB40405() {
            return b40405;
        }

        public void setB40405(String b40405) {
            this.b40405 = b40405;
            notifyPropertyChanged(BR.b40405);
        }

        @Bindable
        public String getB40498() {
            return b40498;
        }

        public void setB40498(String b40498) {
            this.b40498 = b40498;
            if(b40498.equals("98")) {
                setB40401(_EMPTY_);
                setB40402(_EMPTY_);
                setB40403(_EMPTY_);
                setB40404(_EMPTY_);
                setB40405(_EMPTY_);
                setB40496(_EMPTY_);
            }
            notifyPropertyChanged(BR.b40498);
        }

        @Bindable
        public String getB40496() {
            return b40496;
        }

        public void setB40496(String b40496) {
            this.b40496 = b40496;
            setB40496x(b40496.equals("96") ? this.b40496x : _EMPTY_);
            notifyPropertyChanged(BR.b40496);
        }

        @Bindable
        public String getB40496x() {
            return b40496x;
        }

        public void setB40496x(String b40496x) {
            this.b40496x = b40496x;
            notifyPropertyChanged(BR.b40496x);
        }

        @Bindable
        public String getB40501() {
            return b40501;
        }

        public void setB40501(String b40501) {
            this.b40501 = b40501;
            notifyPropertyChanged(BR.b40501);
        }

        @Bindable
        public String getB40502() {
            return b40502;
        }

        public void setB40502(String b40502) {
            this.b40502 = b40502;
            notifyPropertyChanged(BR.b40502);
        }

        @Bindable
        public String getB40503() {
            return b40503;
        }

        public void setB40503(String b40503) {
            this.b40503 = b40503;
            notifyPropertyChanged(BR.b40503);
        }

        @Bindable
        public String getB40504() {
            return b40504;
        }

        public void setB40504(String b40504) {
            this.b40504 = b40504;
            notifyPropertyChanged(BR.b40504);
        }

        @Bindable
        public String getB40505() {
            return b40505;
        }

        public void setB40505(String b40505) {
            this.b40505 = b40505;
            notifyPropertyChanged(BR.b40505);
        }

        @Bindable
        public String getB40506() {
            return b40506;
        }

        public void setB40506(String b40506) {
            this.b40506 = b40506;
            notifyPropertyChanged(BR.b40506);
        }

        @Bindable
        public String getB40507() {
            return b40507;
        }

        public void setB40507(String b40507) {
            this.b40507 = b40507;
            notifyPropertyChanged(BR.b40507);
        }

        @Bindable
        public String getB40508() {
            return b40508;
        }

        public void setB40508(String b40508) {
            this.b40508 = b40508;
            notifyPropertyChanged(BR.b40508);
        }

        @Bindable
        public String getB40509() {
            return b40509;
        }

        public void setB40509(String b40509) {
            this.b40509 = b40509;
            notifyPropertyChanged(BR.b40509);
        }

        @Bindable
        public String getB40510() {
            return b40510;
        }

        public void setB40510(String b40510) {
            this.b40510 = b40510;
            notifyPropertyChanged(BR.b40510);
        }

        @Bindable
        public String getB40511() {
            return b40511;
        }

        public void setB40511(String b40511) {
            this.b40511 = b40511;
            notifyPropertyChanged(BR.b40511);
        }

        @Bindable
        public String getB40598() {
            return b40598;
        }

        public void setB40598(String b40598) {
            this.b40598 = b40598;
            if(b40598.equals("98")) {
                setB40501(_EMPTY_);
                setB40502(_EMPTY_);
                setB40503(_EMPTY_);
                setB40504(_EMPTY_);
                setB40505(_EMPTY_);
                setB40506(_EMPTY_);
                setB40507(_EMPTY_);
                setB40508(_EMPTY_);
                setB40509(_EMPTY_);
                setB40510(_EMPTY_);
                setB40511(_EMPTY_);
                setB40596(_EMPTY_);
            }
            notifyPropertyChanged(BR.b40598);
        }

        @Bindable
        public String getB40596() {
            return b40596;
        }

        public void setB40596(String b40596) {
            this.b40596 = b40596;
            setB40596x(b40596.equals("96") ? this.b40596x : _EMPTY_);
            notifyPropertyChanged(BR.b40596);
        }

        @Bindable
        public String getB40596x() {
            return b40596x;
        }

        public void setB40596x(String b40596x) {
            this.b40596x = b40596x;
            notifyPropertyChanged(BR.b40596x);
        }

    }

    /**
     * C. NUTRITION IN EMERGENCIES - FOOD AND AGRICULTURE ORGANIZATION (FAO) - ADAPTED FORM

     */
    public static class SC extends BaseObservable {
        private String c101 = _EMPTY_;
        private String c10196x = _EMPTY_;
        private String c102 = _EMPTY_;
        private String c10296x = _EMPTY_;
        private String c103 = _EMPTY_;
        private String c10396x = _EMPTY_;
        private String c10401 = _EMPTY_;
        private String c10402 = _EMPTY_;
        private String c10403 = _EMPTY_;
        private String c10404 = _EMPTY_;
        private String c10405 = _EMPTY_;
        private String c10406 = _EMPTY_;
        private String c10498 = _EMPTY_;
        private String c105 = _EMPTY_;
        private String c106 = _EMPTY_;
        private String c10696x = _EMPTY_;
        private String c107 = _EMPTY_;
        private String c10796x = _EMPTY_;
        private String c108 = _EMPTY_;
        private String c10901 = _EMPTY_;
        private String c10902 = _EMPTY_;
        private String c10903 = _EMPTY_;
        private String c10904 = _EMPTY_;
        private String c10905 = _EMPTY_;
        private String c10998 = _EMPTY_;
        private String c10996 = _EMPTY_;
        private String c10996x = _EMPTY_;
        private String c110 = _EMPTY_;
        private String c110nx = _EMPTY_;
        private String c11101 = _EMPTY_;
        private String c11102 = _EMPTY_;
        private String c11103 = _EMPTY_;
        private String c11104 = _EMPTY_;
        private String c11105 = _EMPTY_;
        private String c11198 = _EMPTY_;
        private String c11201 = _EMPTY_;
        private String c11202 = _EMPTY_;
        private String c11203 = _EMPTY_;
        private String c11204 = _EMPTY_;
        private String c11298 = _EMPTY_;
        private String c11296 = _EMPTY_;
        private String c11296x = _EMPTY_;
        private String c113 = _EMPTY_;
        private String c11401 = _EMPTY_;
        private String c11402 = _EMPTY_;
        private String c11403 = _EMPTY_;
        private String c11404 = _EMPTY_;
        private String c11405 = _EMPTY_;
        private String c11406 = _EMPTY_;
        private String c11498 = _EMPTY_;
        private String c11496 = _EMPTY_;
        private String c11496x = _EMPTY_;
        private String c115 = _EMPTY_;
        private String c11596x = _EMPTY_;
        private String c116 = _EMPTY_;
        private String c11696x = _EMPTY_;
        private String c117 = _EMPTY_;
        private String c11796x = _EMPTY_;
        private String c118 = _EMPTY_;
        private String c11896x = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SC> {
            public DataConverter() {
                super(new TypeToken<SC>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SC data) {
            MainApp.formKAP.setSC(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SC getData() {
            return MainApp.formKAP.getSC();
        }

        @Bindable
        public String getC10196x() {
            return c10196x;
        }

        public void setC10196x(String c10196x) {
            this.c10196x = c10196x;
            notifyPropertyChanged(BR.c10196x);
        }

        @Bindable
        public String getC101() {
            return c101;
        }

        public void setC101(String c101) {
            this.c101 = c101;
            setC10196x(c101.equals("96") ? this.c10196x: _EMPTY_);
            notifyPropertyChanged(BR.c101);
        }

        @Bindable
        public String getC102() {
            return c102;
        }

        public void setC102(String c102) {
            this.c102 = c102;
            setC10296x(c102.equals("96") ? this.c10296x: _EMPTY_);
            notifyPropertyChanged(BR.c102);
        }

        @Bindable
        public String getC10296x() {
            return c10296x;
        }

        public void setC10296x(String c10296x) {
            this.c10296x = c10296x;
            notifyPropertyChanged(BR.c10296x);
        }

        @Bindable
        public String getC103() {
            return c103;
        }

        public void setC103(String c103) {
            this.c103 = c103;
            setC10396x(c103.equals("96") ? this.c10396x: _EMPTY_);
            notifyPropertyChanged(BR.c103);
        }

        @Bindable
        public String getC10396x() {
            return c10396x;
        }

        public void setC10396x(String c10396x) {
            this.c10396x = c10396x;
            notifyPropertyChanged(BR.c10396x);
        }

        @Bindable
        public String getC10401() {
            return c10401;
        }

        public void setC10401(String c10401) {
            this.c10401 = c10401;
            notifyPropertyChanged(BR.c10401);
        }

        @Bindable
        public String getC10402() {
            return c10402;
        }

        public void setC10402(String c10402) {
            this.c10402 = c10402;
            notifyPropertyChanged(BR.c10402);
        }

        @Bindable
        public String getC10403() {
            return c10403;
        }

        public void setC10403(String c10403) {
            this.c10403 = c10403;
            notifyPropertyChanged(BR.c10403);
        }

        @Bindable
        public String getC10404() {
            return c10404;
        }

        public void setC10404(String c10404) {
            this.c10404 = c10404;
            notifyPropertyChanged(BR.c10404);
        }

        @Bindable
        public String getC10405() {
            return c10405;
        }

        public void setC10405(String c10405) {
            this.c10405 = c10405;
            notifyPropertyChanged(BR.c10405);
        }

        @Bindable
        public String getC10406() {
            return c10406;
        }

        public void setC10406(String c10406) {
            this.c10406 = c10406;
            notifyPropertyChanged(BR.c10406);
        }

        @Bindable
        public String getC10498() {
            return c10498;
        }

        public void setC10498(String c10498) {
            this.c10498 = c10498;
            if(c10498.equals("98")) {
                setC10401(_EMPTY_);
                setC10402(_EMPTY_);
                setC10403(_EMPTY_);
                setC10404(_EMPTY_);
                setC10405(_EMPTY_);
                setC10406(_EMPTY_);
            }
            notifyPropertyChanged(BR.c10498);
        }

        @Bindable
        public String getC105() {
            return c105;
        }

        public void setC105(String c105) {
            this.c105 = c105;
            notifyPropertyChanged(BR.c105);
        }

        @Bindable
        public String getC106() {
            return c106;
        }

        public void setC106(String c106) {
            this.c106 = c106;
            setC10696x(c106.equals("96") ? this.c10696x: _EMPTY_);
            notifyPropertyChanged(BR.c106);
        }

        @Bindable
        public String getC10696x() {
            return c10696x;
        }

        public void setC10696x(String c10696x) {
            this.c10696x = c10696x;
            notifyPropertyChanged(BR.c10696x);
        }

        @Bindable
        public String getC107() {
            return c107;
        }

        public void setC107(String c107) {
            this.c107 = c107;
            setC10796x(c107.equals("96") ? this.c10796x: _EMPTY_);
            notifyPropertyChanged(BR.c107);
        }

        @Bindable
        public String getC10796x() {
            return c10796x;
        }

        public void setC10796x(String c10796x) {
            this.c10796x = c10796x;
            notifyPropertyChanged(BR.c10796x);
        }

        @Bindable
        public String getC108() {
            return c108;
        }

        public void setC108(String c108) {
            this.c108 = c108;
            notifyPropertyChanged(BR.c108);
        }

        @Bindable
        public String getC10901() {
            return c10901;
        }

        public void setC10901(String c10901) {
            this.c10901 = c10901;
            notifyPropertyChanged(BR.c10901);
        }

        @Bindable
        public String getC10902() {
            return c10902;
        }

        public void setC10902(String c10902) {
            this.c10902 = c10902;
            notifyPropertyChanged(BR.c10902);
        }

        @Bindable
        public String getC10903() {
            return c10903;
        }

        public void setC10903(String c10903) {
            this.c10903 = c10903;
            notifyPropertyChanged(BR.c10903);
        }

        @Bindable
        public String getC10904() {
            return c10904;
        }

        public void setC10904(String c10904) {
            this.c10904 = c10904;
            notifyPropertyChanged(BR.c10904);
        }

        @Bindable
        public String getC10905() {
            return c10905;
        }

        public void setC10905(String c10905) {
            this.c10905 = c10905;
            notifyPropertyChanged(BR.c10905);
        }

        @Bindable
        public String getC10998() {
            return c10998;
        }

        public void setC10998(String c10998) {
            this.c10998 = c10998;
            if(c10998.equals("98")) {
                setC10901(_EMPTY_);
                setC10902(_EMPTY_);
                setC10903(_EMPTY_);
                setC10904(_EMPTY_);
                setC10905(_EMPTY_);
                setC10996(_EMPTY_);
            }
            notifyPropertyChanged(BR.c10998);
        }

        @Bindable
        public String getC10996() {
            return c10996;
        }

        public void setC10996(String c10996) {
            this.c10996 = c10996;
            setC10996x(c10996.equals("96") ? this.c10996x : _EMPTY_);
            notifyPropertyChanged(BR.c10996);
        }

        @Bindable
        public String getC10996x() {
            return c10996x;
        }

        public void setC10996x(String c10996x) {
            this.c10996x = c10996x;
            notifyPropertyChanged(BR.c10996x);
        }

        @Bindable
        public String getC110() {
            return c110;
        }

        public void setC110(String c110) {
            this.c110 = c110;
            notifyPropertyChanged(BR.c110);
        }

        @Bindable
        public String getC110nx() {
            return c110nx;
        }

        public void setC110nx(String c110nx) {
            this.c110nx = c110nx;
            notifyPropertyChanged(BR.c110nx);
        }

        @Bindable
        public String getC11101() {
            return c11101;
        }

        public void setC11101(String c11101) {
            this.c11101 = c11101;
            notifyPropertyChanged(BR.c11101);
        }

        @Bindable
        public String getC11102() {
            return c11102;
        }

        public void setC11102(String c11102) {
            this.c11102 = c11102;
            notifyPropertyChanged(BR.c11102);
        }

        @Bindable
        public String getC11103() {
            return c11103;
        }

        public void setC11103(String c11103) {
            this.c11103 = c11103;
            notifyPropertyChanged(BR.c11103);
        }

        @Bindable
        public String getC11104() {
            return c11104;
        }

        public void setC11104(String c11104) {
            this.c11104 = c11104;
            notifyPropertyChanged(BR.c11104);
        }

        @Bindable
        public String getC11105() {
            return c11105;
        }

        public void setC11105(String c11105) {
            this.c11105 = c11105;
            notifyPropertyChanged(BR.c11105);
        }

        @Bindable
        public String getC11198() {
            return c11198;
        }

        public void setC11198(String c11198) {
            this.c11198 = c11198;
            if(c11198.equals("98")) {
                setC11101(_EMPTY_);
                setC11102(_EMPTY_);
                setC11103(_EMPTY_);
                setC11104(_EMPTY_);
                setC11105(_EMPTY_);
            }
            notifyPropertyChanged(BR.c11198);
        }

        @Bindable
        public String getC11201() {
            return c11201;
        }

        public void setC11201(String c11201) {
            this.c11201 = c11201;
            notifyPropertyChanged(BR.c11201);
        }

        @Bindable
        public String getC11202() {
            return c11202;
        }

        public void setC11202(String c11202) {
            this.c11202 = c11202;
            notifyPropertyChanged(BR.c11202);
        }

        @Bindable
        public String getC11203() {
            return c11203;
        }

        public void setC11203(String c11203) {
            this.c11203 = c11203;
            notifyPropertyChanged(BR.c11203);
        }

        @Bindable
        public String getC11204() {
            return c11204;
        }

        public void setC11204(String c11204) {
            this.c11204 = c11204;
            notifyPropertyChanged(BR.c11204);
        }

        @Bindable
        public String getC11298() {
            return c11298;
        }

        public void setC11298(String c11298) {
            this.c11298 = c11298;
            if(c11298.equals("98")) {
                setC11201(_EMPTY_);
                setC11202(_EMPTY_);
                setC11203(_EMPTY_);
                setC11204(_EMPTY_);
                setC11296(_EMPTY_);
            }
            notifyPropertyChanged(BR.c11298);
        }

        @Bindable
        public String getC11296() {
            return c11296;
        }

        public void setC11296(String c11296) {
            this.c11296 = c11296;
            setC11296x(c11296.equals("96") ? this.c11296x : _EMPTY_);
            notifyPropertyChanged(BR.c11296);
        }

        @Bindable
        public String getC11296x() {
            return c11296x;
        }

        public void setC11296x(String c11296x) {
            this.c11296x = c11296x;
            notifyPropertyChanged(BR.c11296x);
        }

        @Bindable
        public String getC113() {
            return c113;
        }

        public void setC113(String c113) {
            this.c113 = c113;
            notifyPropertyChanged(BR.c113);
        }

        @Bindable
        public String getC11401() {
            return c11401;
        }

        public void setC11401(String c11401) {
            this.c11401 = c11401;
            notifyPropertyChanged(BR.c11401);
        }

        @Bindable
        public String getC11402() {
            return c11402;
        }

        public void setC11402(String c11402) {
            this.c11402 = c11402;
            notifyPropertyChanged(BR.c11402);
        }

        @Bindable
        public String getC11403() {
            return c11403;
        }

        public void setC11403(String c11403) {
            this.c11403 = c11403;
            notifyPropertyChanged(BR.c11403);
        }

        @Bindable
        public String getC11404() {
            return c11404;
        }

        public void setC11404(String c11404) {
            this.c11404 = c11404;
            notifyPropertyChanged(BR.c11404);
        }

        @Bindable
        public String getC11405() {
            return c11405;
        }

        public void setC11405(String c11405) {
            this.c11405 = c11405;
            notifyPropertyChanged(BR.c11405);
        }

        @Bindable
        public String getC11406() {
            return c11406;
        }

        public void setC11406(String c11406) {
            this.c11406 = c11406;
            notifyPropertyChanged(BR.c11406);
        }

        @Bindable
        public String getC11498() {
            return c11498;
        }

        public void setC11498(String c11498) {
            this.c11498 = c11498;
            if(c11498.equals("98")) {
                setC11401(_EMPTY_);
                setC11402(_EMPTY_);
                setC11403(_EMPTY_);
                setC11404(_EMPTY_);
                setC11405(_EMPTY_);
                setC11406(_EMPTY_);
                setC11496(_EMPTY_);
            }
            notifyPropertyChanged(BR.c11498);
        }

        @Bindable
        public String getC11496() {
            return c11496;
        }

        public void setC11496(String c11496) {
            this.c11496 = c11496;
            setC11496x(c11496.equals("96") ? this.c11496x : _EMPTY_);
            notifyPropertyChanged(BR.c11496);
        }

        @Bindable
        public String getC11496x() {
            return c11496x;
        }

        public void setC11496x(String c11496x) {
            this.c11496x = c11496x;
            notifyPropertyChanged(BR.c11496x);
        }

        @Bindable
        public String getC115() {
            return c115;
        }

        public void setC115(String c115) {
            this.c115 = c115;
            setC11596x(c115.equals("96") ? this.c11596x: _EMPTY_);
            notifyPropertyChanged(BR.c115);
        }

        @Bindable
        public String getC11596x() {
            return c11596x;
        }

        public void setC11596x(String c11596x) {
            this.c11596x = c11596x;
            notifyPropertyChanged(BR.c11596x);
        }

        @Bindable
        public String getC116() {
            return c116;
        }

        public void setC116(String c116) {
            this.c116 = c116;
            setC11696x(c116.equals("96") ? this.c11696x: _EMPTY_);
            notifyPropertyChanged(BR.c116);
        }

        @Bindable
        public String getC11696x() {
            return c11696x;
        }

        public void setC11696x(String c11696x) {
            this.c11696x = c11696x;
            notifyPropertyChanged(BR.c11696x);
        }

        @Bindable
        public String getC117() {
            return c117;
        }

        public void setC117(String c117) {
            this.c117 = c117;
            setC11796x(c117.equals("96") ? this.c11796x: _EMPTY_);
            notifyPropertyChanged(BR.c117);
        }

        @Bindable
        public String getC11796x() {
            return c11796x;
        }

        public void setC11796x(String c11796x) {
            this.c11796x = c11796x;
            notifyPropertyChanged(BR.c11796x);
        }

        @Bindable
        public String getC118() {
            return c118;
        }

        public void setC118(String c118) {
            this.c118 = c118;
            setC11896x(c118.equals("96") ? this.c11896x: _EMPTY_);
            notifyPropertyChanged(BR.c118);
        }

        @Bindable
        public String getC11896x() {
            return c11896x;
        }

        public void setC11896x(String c11896x) {
            this.c11896x = c11896x;
            notifyPropertyChanged(BR.c11896x);
        }

    }

    /**
     * D. RMNCH - JHPIEGO Birth Preparedness and Complication Readiness (BP/CR)
     */
    public static class SD extends BaseObservable {
        private String d101 = _EMPTY_;
        private String d10201 = _EMPTY_;
        private String d10202 = _EMPTY_;
        private String d10203 = _EMPTY_;
        private String d10204 = _EMPTY_;
        private String d10205 = _EMPTY_;
        private String d10206 = _EMPTY_;
        private String d10207 = _EMPTY_;
        private String d10208 = _EMPTY_;
        private String d10209 = _EMPTY_;
        private String d10210 = _EMPTY_;
        private String d10211 = _EMPTY_;
        private String d10212 = _EMPTY_;
        private String d10297 = _EMPTY_;
        private String d10298 = _EMPTY_;
        private String d10296 = _EMPTY_;
        private String d10296x = _EMPTY_;
        private String d103 = _EMPTY_;
        private String d10401 = _EMPTY_;
        private String d10402 = _EMPTY_;
        private String d10403 = _EMPTY_;
        private String d10404 = _EMPTY_;
        private String d10405 = _EMPTY_;
        private String d10406 = _EMPTY_;
        private String d10407 = _EMPTY_;
        private String d10497 = _EMPTY_;
        private String d10498 = _EMPTY_;
        private String d10496 = _EMPTY_;
        private String d10496x = _EMPTY_;
        private String d105 = _EMPTY_;
        private String d10601 = _EMPTY_;
        private String d10602 = _EMPTY_;
        private String d10603 = _EMPTY_;
        private String d10604 = _EMPTY_;
        private String d10605 = _EMPTY_;
        private String d10606 = _EMPTY_;
        private String d10607 = _EMPTY_;
        private String d10608 = _EMPTY_;
        private String d10609 = _EMPTY_;
        private String d10610 = _EMPTY_;
        private String d10697 = _EMPTY_;
        private String d10698 = _EMPTY_;
        private String d10696 = _EMPTY_;
        private String d10696x = _EMPTY_;
        private String d107 = _EMPTY_;
        private String d10801 = _EMPTY_;
        private String d10802 = _EMPTY_;
        private String d10803 = _EMPTY_;
        private String d10804 = _EMPTY_;
        private String d10805 = _EMPTY_;
        private String d10806 = _EMPTY_;
        private String d10807 = _EMPTY_;
        private String d10808 = _EMPTY_;
        private String d10809 = _EMPTY_;
        private String d10810 = _EMPTY_;
        private String d10811 = _EMPTY_;
        private String d10897 = _EMPTY_;
        private String d10898 = _EMPTY_;
        private String d10896 = _EMPTY_;
        private String d10896x = _EMPTY_;
        private String d109 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SD> {
            public DataConverter() {
                super(new TypeToken<SD>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SD data) {
            MainApp.formKAP.setSD(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SD getData() {
            return MainApp.formKAP.getSD();
        }

        @Bindable
        public String getD101() {
            return d101;
        }

        public void setD101(String d101) {
            this.d101 = d101;
            notifyPropertyChanged(BR.d101);
        }

        @Bindable
        public String getD10201() {
            return d10201;
        }

        public void setD10201(String d10201) {
            this.d10201 = d10201;
            notifyPropertyChanged(BR.d10201);
        }

        @Bindable
        public String getD10202() {
            return d10202;
        }

        public void setD10202(String d10202) {
            this.d10202 = d10202;
            notifyPropertyChanged(BR.d10202);
        }

        @Bindable
        public String getD10203() {
            return d10203;
        }

        public void setD10203(String d10203) {
            this.d10203 = d10203;
            notifyPropertyChanged(BR.d10203);
        }

        @Bindable
        public String getD10204() {
            return d10204;
        }

        public void setD10204(String d10204) {
            this.d10204 = d10204;
            notifyPropertyChanged(BR.d10204);
        }

        @Bindable
        public String getD10205() {
            return d10205;
        }

        public void setD10205(String d10205) {
            this.d10205 = d10205;
            notifyPropertyChanged(BR.d10205);
        }

        @Bindable
        public String getD10206() {
            return d10206;
        }

        public void setD10206(String d10206) {
            this.d10206 = d10206;
            notifyPropertyChanged(BR.d10206);
        }

        @Bindable
        public String getD10207() {
            return d10207;
        }

        public void setD10207(String d10207) {
            this.d10207 = d10207;
            notifyPropertyChanged(BR.d10207);
        }

        @Bindable
        public String getD10208() {
            return d10208;
        }

        public void setD10208(String d10208) {
            this.d10208 = d10208;
            notifyPropertyChanged(BR.d10208);
        }

        @Bindable
        public String getD10209() {
            return d10209;
        }

        public void setD10209(String d10209) {
            this.d10209 = d10209;
            notifyPropertyChanged(BR.d10209);
        }

        @Bindable
        public String getD10210() {
            return d10210;
        }

        public void setD10210(String d10210) {
            this.d10210 = d10210;
            notifyPropertyChanged(BR.d10210);
        }

        @Bindable
        public String getD10211() {
            return d10211;
        }

        public void setD10211(String d10211) {
            this.d10211 = d10211;
            notifyPropertyChanged(BR.d10211);
        }

        @Bindable
        public String getD10212() {
            return d10212;
        }

        public void setD10212(String d10212) {
            this.d10212 = d10212;
            notifyPropertyChanged(BR.d10212);
        }

        @Bindable
        public String getD10297() {
            return d10297;
        }

        public void setD10297(String d10297) {
            this.d10297 = d10297;
            if(d10297.equals("97")) {
                setD10201(_EMPTY_);
                setD10202(_EMPTY_);
                setD10203(_EMPTY_);
                setD10204(_EMPTY_);
                setD10205(_EMPTY_);
                setD10206(_EMPTY_);
                setD10207(_EMPTY_);
                setD10208(_EMPTY_);
                setD10209(_EMPTY_);
                setD10210(_EMPTY_);
                setD10211(_EMPTY_);
                setD10212(_EMPTY_);
                setD10298(_EMPTY_);
                setD10296(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10297);
        }

        @Bindable
        public String getD10298() {
            return d10298;
        }

        public void setD10298(String d10298) {
            this.d10298 = d10298;
            if(d10298.equals("98")) {
                setD10201(_EMPTY_);
                setD10202(_EMPTY_);
                setD10203(_EMPTY_);
                setD10204(_EMPTY_);
                setD10205(_EMPTY_);
                setD10206(_EMPTY_);
                setD10207(_EMPTY_);
                setD10208(_EMPTY_);
                setD10209(_EMPTY_);
                setD10210(_EMPTY_);
                setD10211(_EMPTY_);
                setD10212(_EMPTY_);
                setD10297(_EMPTY_);
                setD10296(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10298);
        }

        @Bindable
        public String getD10296() {
            return d10296;
        }

        public void setD10296(String d10296) {
            this.d10296 = d10296;
            setD10296x(d10296.equals("96") ? this.d10296x : _EMPTY_);
            notifyPropertyChanged(BR.d10296);
        }

        @Bindable
        public String getD10296x() {
            return d10296x;
        }

        public void setD10296x(String d10296x) {
            this.d10296x = d10296x;
            notifyPropertyChanged(BR.d10296x);
        }

        @Bindable
        public String getD103() {
            return d103;
        }

        public void setD103(String d103) {
            this.d103 = d103;
            notifyPropertyChanged(BR.d103);
        }

        @Bindable
        public String getD10401() {
            return d10401;
        }

        public void setD10401(String d10401) {
            this.d10401 = d10401;
            notifyPropertyChanged(BR.d10401);
        }

        @Bindable
        public String getD10402() {
            return d10402;
        }

        public void setD10402(String d10402) {
            this.d10402 = d10402;
            notifyPropertyChanged(BR.d10402);
        }

        @Bindable
        public String getD10403() {
            return d10403;
        }

        public void setD10403(String d10403) {
            this.d10403 = d10403;
            notifyPropertyChanged(BR.d10403);
        }

        @Bindable
        public String getD10404() {
            return d10404;
        }

        public void setD10404(String d10404) {
            this.d10404 = d10404;
            notifyPropertyChanged(BR.d10404);
        }

        @Bindable
        public String getD10405() {
            return d10405;
        }

        public void setD10405(String d10405) {
            this.d10405 = d10405;
            notifyPropertyChanged(BR.d10405);
        }

        @Bindable
        public String getD10406() {
            return d10406;
        }

        public void setD10406(String d10406) {
            this.d10406 = d10406;
            notifyPropertyChanged(BR.d10406);
        }

        @Bindable
        public String getD10407() {
            return d10407;
        }

        public void setD10407(String d10407) {
            this.d10407 = d10407;
            notifyPropertyChanged(BR.d10407);
        }

        @Bindable
        public String getD10497() {
            return d10497;
        }

        public void setD10497(String d10497) {
            this.d10497 = d10497;
            if(d10497.equals("97")) {
                setD10401(_EMPTY_);
                setD10402(_EMPTY_);
                setD10403(_EMPTY_);
                setD10404(_EMPTY_);
                setD10405(_EMPTY_);
                setD10406(_EMPTY_);
                setD10407(_EMPTY_);
                setD10498(_EMPTY_);
                setD10496(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10497);
        }

        @Bindable
        public String getD10498() {
            return d10498;
        }

        public void setD10498(String d10498) {
            this.d10498 = d10498;
            if(d10498.equals("98")) {
                setD10401(_EMPTY_);
                setD10402(_EMPTY_);
                setD10403(_EMPTY_);
                setD10404(_EMPTY_);
                setD10405(_EMPTY_);
                setD10406(_EMPTY_);
                setD10407(_EMPTY_);
                setD10497(_EMPTY_);
                setD10496(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10498);
        }

        @Bindable
        public String getD10496() {
            return d10496;
        }

        public void setD10496(String d10496) {
            this.d10496 = d10496;
            setD10496x(d10496.equals("96") ? this.d10496x : _EMPTY_);
            notifyPropertyChanged(BR.d10496);
        }

        @Bindable
        public String getD10496x() {
            return d10496x;
        }

        public void setD10496x(String d10496x) {
            this.d10496x = d10496x;
            notifyPropertyChanged(BR.d10496x);
        }

        @Bindable
        public String getD105() {
            return d105;
        }

        public void setD105(String d105) {
            this.d105 = d105;
            notifyPropertyChanged(BR.d105);
        }

        @Bindable
        public String getD10601() {
            return d10601;
        }

        public void setD10601(String d10601) {
            this.d10601 = d10601;
            notifyPropertyChanged(BR.d10601);
        }

        @Bindable
        public String getD10602() {
            return d10602;
        }

        public void setD10602(String d10602) {
            this.d10602 = d10602;
            notifyPropertyChanged(BR.d10602);
        }

        @Bindable
        public String getD10603() {
            return d10603;
        }

        public void setD10603(String d10603) {
            this.d10603 = d10603;
            notifyPropertyChanged(BR.d10603);
        }

        @Bindable
        public String getD10604() {
            return d10604;
        }

        public void setD10604(String d10604) {
            this.d10604 = d10604;
            notifyPropertyChanged(BR.d10604);
        }

        @Bindable
        public String getD10605() {
            return d10605;
        }

        public void setD10605(String d10605) {
            this.d10605 = d10605;
            notifyPropertyChanged(BR.d10605);
        }

        @Bindable
        public String getD10606() {
            return d10606;
        }

        public void setD10606(String d10606) {
            this.d10606 = d10606;
            notifyPropertyChanged(BR.d10606);
        }

        @Bindable
        public String getD10607() {
            return d10607;
        }

        public void setD10607(String d10607) {
            this.d10607 = d10607;
            notifyPropertyChanged(BR.d10607);
        }

        @Bindable
        public String getD10608() {
            return d10608;
        }

        public void setD10608(String d10608) {
            this.d10608 = d10608;
            notifyPropertyChanged(BR.d10608);
        }

        @Bindable
        public String getD10609() {
            return d10609;
        }

        public void setD10609(String d10609) {
            this.d10609 = d10609;
            notifyPropertyChanged(BR.d10609);
        }

        @Bindable
        public String getD10610() {
            return d10610;
        }

        public void setD10610(String d10610) {
            this.d10610 = d10610;
            notifyPropertyChanged(BR.d10610);
        }

        @Bindable
        public String getD10697() {
            return d10697;
        }

        public void setD10697(String d10697) {
            this.d10697 = d10697;
            if(d10697.equals("97")) {
                setD10601(_EMPTY_);
                setD10602(_EMPTY_);
                setD10603(_EMPTY_);
                setD10604(_EMPTY_);
                setD10605(_EMPTY_);
                setD10606(_EMPTY_);
                setD10607(_EMPTY_);
                setD10608(_EMPTY_);
                setD10609(_EMPTY_);
                setD10610(_EMPTY_);
                setD10698(_EMPTY_);
                setD10696(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10697);
        }

        @Bindable
        public String getD10698() {
            return d10698;
        }

        public void setD10698(String d10698) {
            this.d10698 = d10698;
            if(d10698.equals("98")) {
                setD10601(_EMPTY_);
                setD10602(_EMPTY_);
                setD10603(_EMPTY_);
                setD10604(_EMPTY_);
                setD10605(_EMPTY_);
                setD10606(_EMPTY_);
                setD10607(_EMPTY_);
                setD10608(_EMPTY_);
                setD10609(_EMPTY_);
                setD10610(_EMPTY_);
                setD10697(_EMPTY_);
                setD10696(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10698);
        }

        @Bindable
        public String getD10696() {
            return d10696;
        }

        public void setD10696(String d10696) {
            this.d10696 = d10696;
            setD10696x(d10696.equals("96") ? this.d10696x : _EMPTY_);
            notifyPropertyChanged(BR.d10696);
        }

        @Bindable
        public String getD10696x() {
            return d10696x;
        }

        public void setD10696x(String d10696x) {
            this.d10696x = d10696x;
            notifyPropertyChanged(BR.d10696x);
        }

        @Bindable
        public String getD107() {
            return d107;
        }

        public void setD107(String d107) {
            this.d107 = d107;
            notifyPropertyChanged(BR.d107);
        }

        @Bindable
        public String getD10801() {
            return d10801;
        }

        public void setD10801(String d10801) {
            this.d10801 = d10801;
            notifyPropertyChanged(BR.d10801);
        }

        @Bindable
        public String getD10802() {
            return d10802;
        }

        public void setD10802(String d10802) {
            this.d10802 = d10802;
            notifyPropertyChanged(BR.d10802);
        }

        @Bindable
        public String getD10803() {
            return d10803;
        }

        public void setD10803(String d10803) {
            this.d10803 = d10803;
            notifyPropertyChanged(BR.d10803);
        }

        @Bindable
        public String getD10804() {
            return d10804;
        }

        public void setD10804(String d10804) {
            this.d10804 = d10804;
            notifyPropertyChanged(BR.d10804);
        }

        @Bindable
        public String getD10805() {
            return d10805;
        }

        public void setD10805(String d10805) {
            this.d10805 = d10805;
            notifyPropertyChanged(BR.d10805);
        }

        @Bindable
        public String getD10806() {
            return d10806;
        }

        public void setD10806(String d10806) {
            this.d10806 = d10806;
            notifyPropertyChanged(BR.d10806);
        }

        @Bindable
        public String getD10807() {
            return d10807;
        }

        public void setD10807(String d10807) {
            this.d10807 = d10807;
            notifyPropertyChanged(BR.d10807);
        }

        @Bindable
        public String getD10808() {
            return d10808;
        }

        public void setD10808(String d10808) {
            this.d10808 = d10808;
            notifyPropertyChanged(BR.d10808);
        }

        @Bindable
        public String getD10809() {
            return d10809;
        }

        public void setD10809(String d10809) {
            this.d10809 = d10809;
            notifyPropertyChanged(BR.d10809);
        }

        @Bindable
        public String getD10810() {
            return d10810;
        }

        public void setD10810(String d10810) {
            this.d10810 = d10810;
            notifyPropertyChanged(BR.d10810);
        }

        @Bindable
        public String getD10811() {
            return d10811;
        }

        public void setD10811(String d10811) {
            this.d10811 = d10811;
            notifyPropertyChanged(BR.d10811);
        }

        @Bindable
        public String getD10897() {
            return d10897;
        }

        public void setD10897(String d10897) {
            this.d10897 = d10897;
            if(d10897.equals("97")) {
                setD10801(_EMPTY_);
                setD10802(_EMPTY_);
                setD10803(_EMPTY_);
                setD10804(_EMPTY_);
                setD10805(_EMPTY_);
                setD10806(_EMPTY_);
                setD10807(_EMPTY_);
                setD10808(_EMPTY_);
                setD10809(_EMPTY_);
                setD10810(_EMPTY_);
                setD10811(_EMPTY_);
                setD10898(_EMPTY_);
                setD10896(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10897);
        }

        @Bindable
        public String getD10898() {
            return d10898;
        }

        public void setD10898(String d10898) {
            this.d10898 = d10898;
            if(d10898.equals("98")) {
                setD10801(_EMPTY_);
                setD10802(_EMPTY_);
                setD10803(_EMPTY_);
                setD10804(_EMPTY_);
                setD10805(_EMPTY_);
                setD10806(_EMPTY_);
                setD10807(_EMPTY_);
                setD10808(_EMPTY_);
                setD10809(_EMPTY_);
                setD10810(_EMPTY_);
                setD10811(_EMPTY_);
                setD10897(_EMPTY_);
                setD10896(_EMPTY_);
            }
            notifyPropertyChanged(BR.d10898);
        }

        @Bindable
        public String getD10896() {
            return d10896;
        }

        public void setD10896(String d10896) {
            this.d10896 = d10896;
            setD10896x(d10896.equals("96") ? this.d10896x : _EMPTY_);
            notifyPropertyChanged(BR.d10896);
        }

        @Bindable
        public String getD10896x() {
            return d10896x;
        }

        public void setD10896x(String d10896x) {
            this.d10896x = d10896x;
            notifyPropertyChanged(BR.d10896x);
        }

        @Bindable
        public String getD109() {
            return d109;
        }

        public void setD109(String d109) {
            this.d109 = d109;
            notifyPropertyChanged(BR.d109);
        }
    }

    /**
     * E1. MENTAL HEALTH AND GENDER-BASED VIOLENCE

     */
    public static class SE1 extends BaseObservable {
        private String e101 = _EMPTY_;
        private String e102 = _EMPTY_;
        private String e103 = _EMPTY_;
        private String e104 = _EMPTY_;
        private String e105 = _EMPTY_;
        private String e106 = _EMPTY_;
        private String e107 = _EMPTY_;
        private String e108 = _EMPTY_;
        private String e109 = _EMPTY_;
        private String e110 = _EMPTY_;
        private String e111 = _EMPTY_;
        private String e112 = _EMPTY_;
        private String e113 = _EMPTY_;
        private String e114 = _EMPTY_;
        private String e115 = _EMPTY_;
        private String e116 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SE1> {
            public DataConverter() {
                super(new TypeToken<SE1>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SE1 data) {
            MainApp.formKAP.setSE1(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SE1 getData() {
            return MainApp.formKAP.getSE1();
        }

        @Bindable
        public String getE101() {
            return e101;
        }

        public void setE101(String e101) {
            this.e101 = e101;
            notifyPropertyChanged(BR.e101);
        }

        @Bindable
        public String getE102() {
            return e102;
        }

        public void setE102(String e102) {
            this.e102 = e102;
            notifyPropertyChanged(BR.e102);
        }

        @Bindable
        public String getE103() {
            return e103;
        }

        public void setE103(String e103) {
            this.e103 = e103;
            notifyPropertyChanged(BR.e103);
        }

        @Bindable
        public String getE104() {
            return e104;
        }

        public void setE104(String e104) {
            this.e104 = e104;
            notifyPropertyChanged(BR.e104);
        }

        @Bindable
        public String getE105() {
            return e105;
        }

        public void setE105(String e105) {
            this.e105 = e105;
            notifyPropertyChanged(BR.e105);
        }

        @Bindable
        public String getE106() {
            return e106;
        }

        public void setE106(String e106) {
            this.e106 = e106;
            notifyPropertyChanged(BR.e106);
        }

        @Bindable
        public String getE107() {
            return e107;
        }

        public void setE107(String e107) {
            this.e107 = e107;
            notifyPropertyChanged(BR.e107);
        }

        @Bindable
        public String getE108() {
            return e108;
        }

        public void setE108(String e108) {
            this.e108 = e108;
            notifyPropertyChanged(BR.e108);
        }

        @Bindable
        public String getE109() {
            return e109;
        }

        public void setE109(String e109) {
            this.e109 = e109;
            notifyPropertyChanged(BR.e109);
        }

        @Bindable
        public String getE110() {
            return e110;
        }

        public void setE110(String e110) {
            this.e110 = e110;
            notifyPropertyChanged(BR.e110);
        }

        @Bindable
        public String getE111() {
            return e111;
        }

        public void setE111(String e111) {
            this.e111 = e111;
            notifyPropertyChanged(BR.e111);
        }

        @Bindable
        public String getE112() {
            return e112;
        }

        public void setE112(String e112) {
            this.e112 = e112;
            notifyPropertyChanged(BR.e112);
        }

        @Bindable
        public String getE113() {
            return e113;
        }

        public void setE113(String e113) {
            this.e113 = e113;
            notifyPropertyChanged(BR.e113);
        }

        @Bindable
        public String getE114() {
            return e114;
        }

        public void setE114(String e114) {
            this.e114 = e114;
            notifyPropertyChanged(BR.e114);
        }

        @Bindable
        public String getE115() {
            return e115;
        }

        public void setE115(String e115) {
            this.e115 = e115;
            notifyPropertyChanged(BR.e115);
        }

        @Bindable
        public String getE116() {
            return e116;
        }

        public void setE116(String e116) {
            this.e116 = e116;
            notifyPropertyChanged(BR.e116);
        }

    }

    /**
     * E2. PDHS ATTITUDES TOWARD GBV

     */
    public static class SE2 extends BaseObservable {
        private String e201 = _EMPTY_;
        private String e202 = _EMPTY_;
        private String e203 = _EMPTY_;
        private String e204 = _EMPTY_;
        private String e205 = _EMPTY_;
        private String e206 = _EMPTY_;

        public static class DataConverter extends AppDatabase.BaseConverter<SE2> {
            public DataConverter() {
                super(new TypeToken<SE2>() {}.getType());
            }
        }

        // Save section object as json object in db
        public static int saveData(SE2 data) {
            MainApp.formKAP.setSE2(data);
            return AppDatabase.getDBInstance().formKAPDao().update(MainApp.formKAP);
        }

        // Get section object by parsing json
        public static SE2 getData() {
            return MainApp.formKAP.getSE2();
        }

        @Bindable
        public String getE201() {
            return e201;
        }

        public void setE201(String e201) {
            this.e201 = e201;
            notifyPropertyChanged(BR.e201);
        }

        @Bindable
        public String getE202() {
            return e202;
        }

        public void setE202(String e202) {
            this.e202 = e202;
            notifyPropertyChanged(BR.e202);
        }

        @Bindable
        public String getE203() {
            return e203;
        }

        public void setE203(String e203) {
            this.e203 = e203;
            notifyPropertyChanged(BR.e203);
        }

        @Bindable
        public String getE204() {
            return e204;
        }

        public void setE204(String e204) {
            this.e204 = e204;
            notifyPropertyChanged(BR.e204);
        }

        @Bindable
        public String getE205() {
            return e205;
        }

        public void setE205(String e205) {
            this.e205 = e205;
            notifyPropertyChanged(BR.e205);
        }

        @Bindable
        public String getE206() {
            return e206;
        }

        public void setE206(String e206) {
            this.e206 = e206;
            notifyPropertyChanged(BR.e206);
        }

    }
}