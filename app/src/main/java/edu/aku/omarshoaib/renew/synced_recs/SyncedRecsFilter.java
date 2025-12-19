package edu.aku.omarshoaib.renew.synced_recs;

/* For  */

import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.databinding.DataBindingUtil;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.SyncAC;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.databinding.ViewSyncedRecsFilterBinding;
import edu.aku.omarshoaib.renew.global.AppConstants;
import edu.aku.omarshoaib.renew.global.AppTextWatcher;
import edu.aku.omarshoaib.renew.global.ConnectionDetector;
import edu.aku.omarshoaib.renew.global.DateUtils;
import edu.aku.omarshoaib.renew.global.MainApp;
import edu.aku.omarshoaib.renew.global.SharedPrefs;
import edu.aku.omarshoaib.renew.model.Cluster;
import edu.aku.omarshoaib.renew.model.User;

public class SyncedRecsFilter {

    private Activity activity;
    ViewSyncedRecsFilterBinding bi;

    private ConnectionDetector connectionDetector;
    private AppDatabase appDatabase;

    private boolean isDataCollector;

    // Max Days range of From and To Date picker
    private final int MAX_DATE_RANGE = 7;

    // 1st String is column name on sever
    // 2nd String is column value on server
    private HashMap<String, String> whereClauses = new HashMap<>();

    // Selected module
    private int selectedEntryType;
    private Cluster selectedCluster;

    public SyncedRecsFilter(Activity activity) {
        this.activity = activity;
        connectionDetector = new ConnectionDetector(activity);
        appDatabase = AppDatabase.getDBInstance();

        String designation = MainApp.user.getDesignation();
        isDataCollector = AppConstants.isEmpty(designation) || designation.equalsIgnoreCase(AppConstants.USER_ROLE_DATA_COLLECTOR);
    }

    // View Synced Records Filter Popup
    // mainTable = Main Form1 Table/Parent Table - This will be set as tag in the XML
    public void showViewRecsFilterPopup() {
        bi = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.view_synced_recs_filter, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(bi.getRoot());

        // Modules Dropdown
        List<String> allMainTables = DownloadSyncedRecsData.getAllMainTables();
        bi.moduleTV.setOnClickListener(view -> initDropdown(bi.moduleTV, allMainTables));

        // Start Date Picker
        bi.startDatePicker.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);
        bi.startDatePicker.addTextChangedListener(new AppTextWatcher(bi.startDatePicker.getId(), iAppTextWatcher));
        bi.startDatePicker.setMinDate(DateUtils.addSubMonths(DateUtils.getCurrentDateTime(), -6));

        // End Date Picker
        bi.endDatePicker.setThemeId(R.style.Theme_AppStructure_DatePickerStyle);

        // Username Autocomplete
        List<User> userList = appDatabase.userDao().getAllData();
        ArrayAdapter<User> userAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, userList);
        bi.usernameAC.setAdapter(userAdapter);

        // To prevent error on show dialog after activity is finishing or destroyed
        if (activity.isFinishing() || activity.isDestroyed()) return;

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Proceed Button
        bi.posBtn.setOnClickListener(view -> {
            // Submit filter
            if (!formValidation(activity)) return;
            // APP_SPECIFIC__SYNCED_RECS
            MainApp.form1 = null;

            // Check if the last filter and current filter are same.
            // This check is used to prevent re-downloading of same data - For UX.
            String prevFilterJSON = SharedPrefs.read(SharedPrefs.SYNCED_RECS_FILTER, _EMPTY_);
            String filterJSON = MainApp.gson.toJson(whereClauses);
            // Save filter values to autofill next time when popup opens
            SharedPrefs.write(SharedPrefs.SYNCED_RECS_FILTER, filterJSON);
            AppConstants.IS_LOGIN = 3;
            activity.startActivity(new Intent(activity, SyncAC.class)
                    .putExtra("where", whereClauses)
                    .putExtra("filter_same_as_last", prevFilterJSON.equals(filterJSON)));
            dialog.dismiss();
        });

        // Title TextView
        bi.titleTV.setOnClickListener(view -> dialog.dismiss());

        // Clear Filter Button
        bi.clearTV.setOnClickListener(view -> {
            // For clearing filter values
            bi.moduleTV.setText(null);
            bi.siteFilterTV.setText(null);
            bi.siteFilterTV.setEnabled(false);
            bi.startDatePicker.setText(null);
            bi.endDatePicker.setText(null);
            bi.endDatePicker.setEnabled(false);
            if (!isDataCollector)
                bi.usernameAC.setText(null);
            selectedEntryType = 0;
            selectedCluster = null;
            whereClauses.clear();
        });

        // Preset last filter values if exists
        presetValues();

        dialog.show();
    }

    // Show custom dropdown attached to fields
    private void initDropdown(TextView anchorView, List<?> list) {
        ListPopupWindow popupWindow = new ListPopupWindow(activity);
        popupWindow.setAnchorView(anchorView);
        popupWindow.setDropDownGravity(Gravity.END);
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAdapter(new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, list));
        popupWindow.setOnItemClickListener((adapterView, view, position, id) -> {
            onDropdownItemClick(bi, anchorView, list.get(position), position);
            popupWindow.dismiss();
        });
        popupWindow.show();
    }

    // ListPopupWindow ItemClickListener
    private void onDropdownItemClick(ViewSyncedRecsFilterBinding bi,
                                     TextView anchorView, Object selectedObj, int pos) {
        if (anchorView.getId() == bi.moduleTV.getId()) {
            String text = (String) selectedObj;
            anchorView.setText(text);
            selectedEntryType = pos + 1;
            bi.siteFilterTV.setEnabled(true);
            bi.siteFilterTV.setText(_EMPTY_);
        }
    }

    // Preset last filter values
    private void presetValues() {
        bi.messageTV.setVisibility(View.GONE);
        String filterJSON = SharedPrefs.read(SharedPrefs.SYNCED_RECS_FILTER, _EMPTY_);
        if (!AppConstants.isEmpty(filterJSON)) {
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            whereClauses = MainApp.gson.fromJson(filterJSON, type);

            // Set Module
            bi.moduleTV.setText(whereClauses.get("mainTable"));
            selectedEntryType = Integer.parseInt(Objects.requireNonNull(whereClauses.get("entryType")));

            // Set Cluster/Site
            selectedCluster = appDatabase.clusterDao().getDataByClusterNo(whereClauses.get("site_code"));

            // Set Start Date if exists
            if (whereClauses.containsKey("sysdate_s"))
                bi.startDatePicker.setText(whereClauses.get("sysdate_s"));

            // Set End Date if exists
            if (whereClauses.containsKey("sysdate_e")) {
                bi.endDatePicker.setText(whereClauses.get("sysdate_e"));
                bi.endDatePicker.setEnabled(true);
            }

            // Set End Date if exists
            if (whereClauses.containsKey("username"))
                bi.usernameAC.setText(whereClauses.get("username"));

            // If no network the lock the last inserted filter if exists
            if (!connectionDetector.hasInternetConnection()) {
                // Disable all views except proceed button
                AppConstants.disableViews(activity, bi.mainLayout,
                        AppConstants.getViewNameByView(activity, bi.posBtn));
                bi.messageTV.setText(activity.getString(R.string.no_network_synced_recs_filter));
                bi.messageTV.setVisibility(View.VISIBLE);
            }
        }
        // If user designation is Empty or Data Collector then we set its username
        // by default in filter in order to restrict access of synced view records.
        // Team Leader has the rights to view other users records as well.
        if (isDataCollector) {
            bi.usernameAC.setText(MainApp.user.getUsername());
            bi.usernameAC.setEnabled(false);
        }
    }

    // Validate filter
    private boolean formValidation(Activity activity) {
        bi.messageTV.setVisibility(View.GONE);
        // Entry type must be selected
        if (selectedEntryType == 0) {
            bi.messageTV.setVisibility(View.VISIBLE);
            bi.messageTV.setText(activity.getString(R.string.select_module));
            return false;
        }
        MainApp.entryType = selectedEntryType;
        // These where clauses are for local use
        whereClauses.put("entryType", Integer.toString(selectedEntryType));
        whereClauses.put("mainTable", bi.moduleTV.getText().toString());

        // Site must be selected
        if (AppConstants.isEmpty(bi.siteFilterTV)) {
            bi.messageTV.setVisibility(View.VISIBLE);
            bi.messageTV.setText(activity.getString(R.string.insert_cluster_desc));
            return false;
        }
        MainApp.selectedCluster = selectedCluster;
        // This where clause is for local use
        whereClauses.put("site_code", selectedCluster.getClusterNo());

        // If Start Date Picker not empty
        if (!AppConstants.isEmpty(bi.startDatePicker))
            whereClauses.put("sysdate_s", Objects.requireNonNull(bi.startDatePicker.getText()).toString());

        // If End Date Picker not empty
        if (!AppConstants.isEmpty(bi.endDatePicker))
            whereClauses.put("sysdate_e", Objects.requireNonNull(bi.endDatePicker.getText()).toString());

        // If Username Autocomplete is not empty
        if (!AppConstants.isEmpty(bi.usernameAC)) {
            // Check and get if username exists
            String username = bi.usernameAC.getText().toString().trim();
            User user = appDatabase.userDao().getUserByUsername(username);
            if (user == null) {
                bi.messageTV.setVisibility(View.VISIBLE);
                bi.messageTV.setText(activity.getString(R.string.invalid_username));
                return false;
            }
            whereClauses.put("username", Objects.requireNonNull(username));
        }
        return true;
    }

    AppTextWatcher.IAppTextWatcher iAppTextWatcher = new AppTextWatcher.IAppTextWatcher() {
        @Override
        public void afterTextChanged(int viewId, String text) {
            if (text.length() == 0) return;
            if (viewId == bi.startDatePicker.getId()) {
                // For setting end date range w.r.t. start date
                // End Date will be +MAX_DATE_RANGE days by Start Date
                bi.endDatePicker.setEnabled(true);
                bi.endDatePicker.setText(_EMPTY_);
                bi.endDatePicker.setMinDate(Objects.requireNonNull(bi.startDatePicker.getText()).toString());
                bi.endDatePicker.setMaxDate(DateUtils.addSubDays(
                        Objects.requireNonNull(bi.startDatePicker.getText()).toString(), MAX_DATE_RANGE));
            }
        }
    };

}
