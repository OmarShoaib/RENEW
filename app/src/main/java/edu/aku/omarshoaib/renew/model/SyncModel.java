package edu.aku.omarshoaib.renew.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.aku.omarshoaib.renew.database.AppDatabase;

public class SyncModel {

    /*For DOWNLOAD*/
    private String table;
    private String filter;
    private String select;
    private String check;

    // For Sync list adapter
    private int id;
    private int statusId;
    private String status;
    private String message;
    private String info;

    // For App Version
    // Path of output-meta json file of apk upload server
    private String folder;

    //For sections in upload data
    private String tableSections;

    // For Object Casting in SyncedRecs
    private Class<?> clazz;

    // Module Name i.e. Form1, FormFup etc.
    private int module;

    // For local use
    // For checking synced, duplicates and error status on call
    private boolean isSyncedPerfect = true;

    public SyncModel() {
    }

    public SyncModel(String table, String tableSections) {
        this.table = table;
        this.tableSections = tableSections;
    }

    public SyncModel(Class<?> clazz, int module, String table, String tableSections) {
        this.clazz = clazz;
        this.module = module;
        this.table = table;
    }


    public SyncModel(String table, String select, String filter, String check) {
        this.table = table;
        this.select = select;
        this.filter = filter;
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public String getFilter() {
        return filter;
    }

    public String getSelect() {
        return select;
    }

    public String getCheck() {
        return check;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getTableSections() {
        return tableSections;
    }

    public void setTableSections(String tableSections) {
        this.tableSections = tableSections;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public boolean isSyncedPerfect() {
        return isSyncedPerfect;
    }

    public void setSyncedPerfect(boolean syncedPerfect) {
        isSyncedPerfect = syncedPerfect;
    }

    /*For generating sync adapter item*/
    // This list is used to display ALL items in the list while downloading
    public static List<SyncModel> initSyncList(List<String> syncTableList) {
        List<SyncModel> syncModelList = new ArrayList<>();
        for (int i = 0; i < syncTableList.size(); i++) {
            syncModelList.add(new SyncModel(syncTableList.get(i), "", "", ""));
        }
        return syncModelList;
    }

    /*Used to parse UPLOAD response*/
    public static class WebResponse {
        private int id;
        private int status;
        private int error;
        private String message;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // For Date Time Parsing because JsonObject is returning from server
    public static class ResponseDate implements Serializable {
        private String date;

        @SerializedName("timezone_type")
        private int timezoneType;

        private String timezone;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(int timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        // This class is used to parse the object to save in room db
        public static class DataConverter extends AppDatabase.BaseConverter<ResponseDate> {
            public DataConverter() {
                super(new TypeToken<ResponseDate>() {
                }.getType());
            }
        }
    }

}
