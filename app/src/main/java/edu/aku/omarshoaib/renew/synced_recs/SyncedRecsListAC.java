package edu.aku.omarshoaib.renew.synced_recs;

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;
import static edu.aku.omarshoaib.renew.global.MainApp.entryType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ActivitySyncedRecsListBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.Callbacks;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.model.Form1;

public class SyncedRecsListAC extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private final Activity activity = SyncedRecsListAC.this;

    ActivitySyncedRecsListBinding bi;

    private AppDatabase appDatabase;
    private Gson gsonCustom;

    // main table i.e. form1 table that will be used to generate
    // display list and getting linked tables
    private String mainTable;

    // Display list records
    private List<JSONObject> syncedRecsList;

    private SyncedRecsAdapter syncedRecsAdapter;
    private final int DATA_FETCH_LIMIT_DB = 10;

    // To disable loadMore functionality while searching/filtering records
    private boolean isSearch;
    // This will be empty for when isSearch=false
    private String appFilter = _EMPTY_;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(activity, R.layout.activity_synced_recs_list);
        bi.setCallback(this);

        // Init toolbar
        AppConstants.initToolbar(activity, getString(R.string.synced_recs), "", false);

        appDatabase = AppDatabase.getDBInstance();

        initUI();
    }

    private void initUI() {
        // Get MAIN table by entryType
        mainTable = DownloadSyncedRecsData.getMainTable(entryType);
        // This check is just for safe side in case of
        // entryType not set i.e. entryType=0
        if (entryType == 0) {
            bi.filterLayout.setVisibility(View.GONE);
            bi.emptyTV.setVisibility(View.GONE);
            bi.emptyTV.setText(getString(R.string.module_not_defined));
            return;
        }

        // If network is not available or the filter is same as last then check if any
        // downloaded synced recs exists in the db i.e. previously downloaded
        syncedRecsList = getSyncedRecsDBQuery(0, appFilter);
        if (syncedRecsList.size() > 0) {
            initList();
        } else {
            // Network is not available and local DB synced recs do not exists either
            bi.filterLayout.setVisibility(View.GONE);
            bi.emptyTV.setVisibility(View.GONE);
            bi.emptyTV.setText(getString(R.string.no_records_found));
        }

        // APP_SPECIFIC__SYNCED_RECS
        /*FILTER*/
        //This is used to show/hide filter radioButton w.r.t. module selected
        /*if (entryType == MODULE_HOSPITAL || entryType == MODULE_LAB)
            bi.enrIdRB.setVisibility(View.GONE);
        else if (entryType == MODULE_HCUS)
            bi.scrIdRB.setVisibility(View.GONE);

        // For preparing initial format for filter - For UX
        bi.searchRG.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            String initialFormat;
            if (checkedId == bi.scrIdRB.getId())
                if (entryType == MODULE_HOSPITAL)
                    initialFormat = String.format(Locale.ENGLISH, "SC-%02d-%s-", MainApp.selectedSite.getId(),
                            MainApp.user.getUserCode());
                else
                    initialFormat = String.format(Locale.ENGLISH, "LPF-%02d-%s-", MainApp.selectedSite.getId(),
                            MainApp.user.getUserCode());
            else if (checkedId == bi.enrIdRB.getId())
                // For HCUS
                initialFormat = String.format(Locale.ENGLISH, "EN%d-%02d-%s-", entryType, MainApp.selectedSite.getId(),
                        MainApp.user.getUserCode());
            else
                initialFormat = _EMPTY_;

            bi.searchET.setText(initialFormat);
            bi.searchET.setSelection(bi.searchET.getText().length());
        });*/
    }

    /**
     * DISPLAY SYNCED RECS
     */

    // whereClauses are fetched from SyncedRecsFilter class
    private void initList() {
        if (syncedRecsList == null)
            syncedRecsList = getSyncedRecsDBQuery(0, appFilter);
        if (syncedRecsList.size() > 0) {
            syncedRecsAdapter = new SyncedRecsAdapter(activity, syncedRecsList, mainTable,
                    iRVOnItemClickListener);
            bi.syncedRecsRV.setAdapter(syncedRecsAdapter);
            bi.syncedRecsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(1))
                        loadMore();
                }
            });
        } else {
            bi.filterLayout.setVisibility(View.GONE);
            bi.emptyTV.setVisibility(View.GONE);
            bi.emptyTV.setText(getString(R.string.network_error));
        }
    }

    // Query for selecting synced records from db's main table to prepare display list
    private List<JSONObject> getSyncedRecsDBQuery(int offset, String filter) {
        // Select only those records with synced=2.
        // synced=2 means synced forms that are data is downloaded from server.
        // This query is used to prepare list of records from main table
        // i.e. selected module or entryType
       /* SimpleSQLiteQuery getSyncedRecsQuery = new SimpleSQLiteQuery("SELECT * FROM " + mainTable
                + " WHERE synced=2 " + filter + " ORDER BY id DESC LIMIT " + DATA_FETCH_LIMIT_DB + " OFFSET " + offset);
        return appDatabase.generalDao().getSyncedRecs(getSyncedRecsQuery);*/

        String selectQuery;/* = "SELECT * FROM " + mainTable
                + " WHERE synced=2 " + filter + " ORDER BY id DESC LIMIT " + DATA_FETCH_LIMIT_DB + " OFFSET " + offset;*/
        if (!isSearch)
            // Offset and limit apply if isSearch=false
            selectQuery = "SELECT * FROM " + mainTable + " WHERE synced = 2 "
                    + filter + " ORDER BY id DESC LIMIT " + DATA_FETCH_LIMIT_DB + " OFFSET " + offset;
        else
            // Offset and limit do not apply if isSearch=true i.e. get all searched records at once
            selectQuery = "SELECT * FROM " + mainTable + " WHERE synced = 2 " + filter + " ORDER BY id DESC";

        SupportSQLiteDatabase db = appDatabase.getOpenHelper().getWritableDatabase();
        Cursor cursor = db.query(selectQuery);
        return AppConstants.cursorToJsonObjects(cursor);
    }

    //FILTER
    @SuppressLint("NotifyDataSetChanged")
    public void filterForms(View view) {
        isSearch = true;
        String searchText = bi.searchET.getText().toString().toUpperCase().trim();
        if (AppConstants.isEmpty(searchText))
            return;
        syncedRecsAdapter.reset();

        // APP_SPECIFIC__SYNCED_RECS
        if (entryType == MainApp.MODULE_FORM) {
            if (bi.nameRB.isChecked())
                appFilter = " AND hhid like '%" + searchText + "%' COLLATE NOCASE";
        }
        List<JSONObject> _syncedRecsList = getSyncedRecsDBQuery(0, appFilter);
        if (_syncedRecsList.size() > 0) {
            bi.syncedRecsRV.setVisibility(View.VISIBLE);
            bi.emptyTV.setVisibility(View.GONE);
            syncedRecsAdapter.addAll(_syncedRecsList);
        } else {
            bi.syncedRecsRV.setVisibility(View.GONE);
            bi.emptyTV.setVisibility(View.VISIBLE);
            bi.emptyTV.setText(getString(R.string.no_records_found));
        }
    }

    // Load more records from db and append in the list
    private void loadMore() {
        if (isSearch) return;
        int initialSize = syncedRecsList.size();
        if (initialSize < DATA_FETCH_LIMIT_DB) return;
        syncedRecsList.addAll(getSyncedRecsDBQuery(initialSize, appFilter));
        // If all records fetched from db then prevent adapter to notify again and again
        if (initialSize < syncedRecsList.size()) {
            int updatedSize = syncedRecsList.size();
            bi.syncedRecsRV.post(() -> {
                // For crash handling because of item inserted in scroll listener
                syncedRecsAdapter.updateRangeInserted(syncedRecsList, initialSize, updatedSize);
            });
        }
    }

    // Repopulate list
    public void clearSearch(View view) {
        isSearch = false;
        bi.searchET.setText(null);
        syncedRecsList = null;
        appFilter = _EMPTY_;
        initList();
    }

    // APP_SPECIFIC__SYNCED_RECS
    //This queries in this itemClickListener will be customized based on the module
    Callbacks.IRVOnItemClickListener iRVOnItemClickListener = new Callbacks.IRVOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView recyclerView, Object obj, int index) {
            MainApp.isSyncedRecs = true;
            if (entryType == MainApp.MODULE_FORM) {
                MainApp.form1 = (Form1) obj;
//                AppConstants.gotoActivity(activity, SectionA.class, false);
            }
        }
    };

}