package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.database.AppDatabase;
import edu.aku.omarshoaib.renew.database.dao.SummaryDao;
import edu.aku.omarshoaib.renew.global.views.AppBottomSheet;
import edu.aku.omarshoaib.renew.model.SyncModel;
import edu.aku.omarshoaib.renew.webcall.UploadData;

public class SummaryUtils {

    /**
     * SUMMARY BOTTOMSHEET
     */
    private static String selectedDate;
    private static String selectedTable;

    public static void showSummary(Activity activity) {
        BottomSheetDialog bottomSheetSummary = AppBottomSheet.getBottomSheetDialog(activity,
                R.style.Theme_AppStructure_BottomSheetStyle, R.layout.view_summary,
                true, null);

        // For displaying selected option
        TextView filterTitleTV = bottomSheetSummary.findViewById(R.id.filterTitleTV);
        assert filterTitleTV != null;

        // For filter chip group
        ChipGroup filterCG = bottomSheetSummary.findViewById(R.id.filterCG);
        assert filterCG != null;

        // We take out this particular chip from chip group because we need to click
        // search by date chip whether or not it is selected. The issue was if the by date chip
        // is already selected then the listener won't get called
        Chip byDateChip = bottomSheetSummary.findViewById(R.id.byDateChip);
        assert byDateChip != null;
        byDateChip.setOnClickListener(view -> {
            // ByDate chip clicked
            showDatePicker(activity, bottomSheetSummary);
        });

        filterCG.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // '0' is because we already set singleSelection to true
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.todaysChip) {
                // Todays chip clicked
                selectedDate = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
                setRecords(activity, bottomSheetSummary);
                filterTitleTV.setText(activity.getString(R.string.todays_forms));
            } else {
                // Total chip clicked
                selectedDate = AppConstants._EMPTY_;
                setRecords(activity, bottomSheetSummary);
                filterTitleTV.setText(activity.getString(R.string.total_forms));
            }
        });

        // For form1 type selection
        TextView formTypeTV = bottomSheetSummary.findViewById(R.id.formTypeTV);
        assert formTypeTV != null;

        // Add upload tables list to form1 type dropdown
        List<String> uploadTables = new ArrayList<>();
        // Remove all sub-forms/sub-sections and add main forms only
        for (Map.Entry<SyncModel, Boolean> entry : UploadData.UPLOAD_TABLES.entrySet()) {
            if (entry.getValue())
                uploadTables.add((entry.getKey()).getTable());
        }

        formTypeTV.setOnClickListener(view -> {
            // Show form1 type popup
            ListPopupWindow formTypeMenu = new ListPopupWindow(activity);
            formTypeMenu.setAnchorView(view);
            formTypeMenu.setDropDownGravity(Gravity.END);
            formTypeMenu.setHeight(ListPopupWindow.WRAP_CONTENT);
            formTypeMenu.setAdapter(new ArrayAdapter<>(activity,
                    android.R.layout.simple_list_item_1, uploadTables));
            formTypeMenu.setOnItemClickListener((adapterView, view1, i, l) -> {
                selectedTable = uploadTables.get(i);
                formTypeTV.setText(selectedTable);
                setRecords(activity, bottomSheetSummary);
                formTypeMenu.dismiss();
            });
            formTypeMenu.show();
        });

        // By default show todays Summary and select first table/form1 in dropdown
        selectedDate = DateUtils.getCurrentDateTime(AppConstants.APP_DATE_FORMAT);
        selectedTable = uploadTables.get(0);
        formTypeTV.setText(selectedTable);
        setRecords(activity, bottomSheetSummary);

        // Show bottomsheet
        bottomSheetSummary.show();
    }

    // For Filtering Records Count
    private static void setRecords(Activity activity, BottomSheetDialog bottomSheetSummary) {
        String username = MainApp.user.getUsername();

        TextView bsErrorTV = bottomSheetSummary.findViewById(R.id.bsErrorTV);

        // This list is used to record counts of each query to display those on chart
        List<Float> countList = new ArrayList<>();
        SummaryDao summaryDao = AppDatabase.getDBInstance().summaryDao();

        // Synced Forms
        SimpleSQLiteQuery syncedQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable + " WHERE (synced != '' OR synced != null) " +
                "AND (syncDate != '' OR syncDate != null) AND syncDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary syncedLayout = bottomSheetSummary.findViewById(R.id.syncedLayout);
        assert syncedLayout != null;
        String syncedCount = Integer.toString(summaryDao.getSyncedCount(syncedQuery));
        syncedLayout.setCount(syncedCount);
        countList.add(Float.parseFloat(syncedCount));

        // Unsynced Forms
        SimpleSQLiteQuery unsyncedQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable + " WHERE (synced IS '' OR synced IS null) " +
                "AND (syncDate IS '' OR syncDate IS null) AND (iStatus != '' OR iStatus != null) AND endingDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary unsyncedLayout = bottomSheetSummary.findViewById(R.id.unsyncedLayout);
        assert unsyncedLayout != null;
        String unsyncedCount = Integer.toString(summaryDao.getUnsyncedCount(unsyncedQuery));
        unsyncedLayout.setCount(unsyncedCount);
        countList.add(Float.parseFloat(unsyncedCount));

        // Completed Forms - Exclude synced forms
        SimpleSQLiteQuery completedQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable + " WHERE (iStatus != '' OR iStatus != null) AND iStatus = 1 " +
                "AND (synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null) AND endingDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary completedLayout = bottomSheetSummary.findViewById(R.id.completedLayout);
        assert completedLayout != null;
        String completedCount = Integer.toString(summaryDao.getCompletedCount(completedQuery));
        completedLayout.setCount(completedCount);
        countList.add(Float.parseFloat(completedCount));

        // Completed Forms but Status is other than complete like refused, house locked etc. i.e. Exclude Completed forms with iStatus=1(Complete)
        SimpleSQLiteQuery completedOtherQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable + " WHERE (iStatus != '' OR iStatus != null) AND iStatus != 1 " +
                "AND (synced IS '' OR synced IS null) AND (syncDate IS '' OR syncDate IS null) AND endingDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary completedOtherLayout = bottomSheetSummary.findViewById(R.id.completedOtherLayout);
        assert completedOtherLayout != null;
        String completedOtherCount = Integer.toString(summaryDao.getCompletedOtherCount(completedOtherQuery));
        completedOtherLayout.setCount(completedOtherCount);
        countList.add(Float.parseFloat(completedOtherCount));

        // Incomplete Forms
        SimpleSQLiteQuery incompleteQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable + " WHERE (iStatus IS '' OR iStatus IS null) " +
                "AND sysDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary incompleteLayout = bottomSheetSummary.findViewById(R.id.incompleteLayout);
        assert incompleteLayout != null;
        String incompleteCount = Integer.toString(summaryDao.getInCompleteCount(incompleteQuery));
        incompleteLayout.setCount(incompleteCount);
        countList.add(Float.parseFloat(incompleteCount));

        // Total Forms
        SimpleSQLiteQuery totalQuery = new SimpleSQLiteQuery("SELECT COUNT(*) FROM " + selectedTable +
                " WHERE sysDate LIKE '" + selectedDate + "%' AND username LIKE '%" + username + "%'");
        ItemSummary totalLayout = bottomSheetSummary.findViewById(R.id.totalLayout);
        assert totalLayout != null;
        String totalCount = Integer.toString(summaryDao.getTotalCount(totalQuery));
        totalLayout.setCount(totalCount);
        countList.add(Float.parseFloat(totalCount));

        // Draw chart with respect to data
        PieChart chart = bottomSheetSummary.findViewById(R.id.chart);
        assert chart != null;
        initChart(activity, chart, countList, bsErrorTV);
    }

    // Init chart
    private static void initChart(Activity activity, PieChart chart, List<Float> countList, TextView bsErrorTV) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // For checking if the all the entries have '0' value or not
        boolean isAllZero = true;

        // Input data and fit data into pie chart entry
        // -1 is because we need to avoid total count to add in the chart
        for (int i = 0; i < countList.size() - 1; i++) {
            Float count = countList.get(i);
            entries.add(new PieEntry(count));
            if (count > 0) isAllZero = false;
        }

        // If the entries list have any value then hide error textview
        bsErrorTV.setVisibility(isAllZero ? View.VISIBLE : View.GONE);

        // Prepare dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(activity.getResources().getDimension(com.intuit.sdp.R.dimen._1sdp));
        dataSet.setSelectionShift(activity.getResources().getDimension(com.intuit.sdp.R.dimen._4sdp));

        // Add color
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(activity, R.color.synced_color));
        colors.add(ContextCompat.getColor(activity, R.color.unsynced_color));
        colors.add(ContextCompat.getColor(activity, R.color.completed_color));
        colors.add(ContextCompat.getColor(activity, R.color.completed_other_color));
        colors.add(ContextCompat.getColor(activity, R.color.incomplete_color));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(activity.getResources().getDimension(R.dimen.text_size_small));
        data.setValueTextColor(ContextCompat.getColor(activity, R.color.white));
        chart.setData(data);
        data.setDrawValues(true);

        // undo all highlights
        chart.highlightValues(null);
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerTapEnabled(true);
        chart.setRotationEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setHoleColor(ContextCompat.getColor(activity, R.color.white));
        chart.setMaxAngle(180f);
//        chart.setRotation(270f);
        chart.animateY(1000, Easing.EaseInOutCubic);
        chart.invalidate();
    }

    // Show date picker
    private static void showDatePicker(Activity activity, BottomSheetDialog bottomSheetSummary) {
        Calendar c = Calendar.getInstance();

        // Current date
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.Theme_AppStructure_DatePickerStyle,
                (datePicker, year1, monthOfYear, dayOfMonth) -> {
                    selectedDate = String.format(Locale.ENGLISH, "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    setRecords(activity, bottomSheetSummary);

                    ((TextView) Objects.requireNonNull(bottomSheetSummary.findViewById(R.id.filterTitleTV))).setText(selectedDate);
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    /**
     * VIEW OF SINGLE ITEM OF SUMMARY CARDS
     */
    public static class ItemSummary extends CardView {

        private TextView countTV;

        public ItemSummary(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public void init(Context context, AttributeSet attrs) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_summary, this, true);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemSummary, 0, 0);

            try {
                int titleBackground = typedArray.getColor(R.styleable.ItemSummary_titleBGColor,
                        ContextCompat.getColor(context, R.color.disabled_text_color));
                String title = typedArray.getString(R.styleable.ItemSummary_title);
                String count = typedArray.getString(R.styleable.ItemSummary_count);

                TextView titleTV = view.findViewById(R.id.titleTV);
                titleTV.setBackgroundColor(titleBackground);
                titleTV.setText(title);

                countTV = view.findViewById(R.id.countTV);
                countTV.setText(count);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }

        public void setCount(String count) {
            countTV.setText(count);
        }
    }

}
