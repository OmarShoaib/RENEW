/*
package aku.edu.omarshoaib.appstructure.global;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;
import java.util.Locale;

import aku.edu.omarshoaib.appstructure.R;
import aku.edu.omarshoaib.appstructure.global.views.AppBottomSheet;

public class DateTimePicker implements View.OnClickListener {

    private final Activity activity;
    private final IDateTimePickerEventCallback iDateTimePickerEventCallback;

    private SingleDateAndTimePicker singleDateAndTimePickerDialog;

    private final int maxDateInDays = 7;

    private Date minDateRange, maxDateRange;

    private Date selectedDate;

    private BottomSheetDialog bottomSheetDateTimePicker;

    public DateTimePicker(Activity activity, IDateTimePickerEventCallback iDateTimePickerEventCallback) {
        this.activity = activity;
        this.iDateTimePickerEventCallback = iDateTimePickerEventCallback;
    }

    public void initDateTimePicker(String title, boolean isShowHours, boolean isShowMinutes, boolean isShowDays,
                                   boolean isShowDaysOfMonth, boolean isShowMonths, boolean isShowYears) {
        bottomSheetDateTimePicker = AppBottomSheet.getBottomSheetDialog(activity, R.style.Theme_AppStructure_BottomSheetStyle,
                R.layout.view_datetime_picker, true, null);

        TextView headerTV = bottomSheetDateTimePicker.findViewById(R.id.headerTV);

        Button bsSubmitBtn = bottomSheetDateTimePicker.findViewById(R.id.bsSubmitBtn);
        assert bsSubmitBtn != null;
        bsSubmitBtn.setOnClickListener(this);

        Button bsCloseBtn = bottomSheetDateTimePicker.findViewById(R.id.bsCloseBtn);
        assert bsCloseBtn != null;
        bsCloseBtn.setOnClickListener(this);

        assert headerTV != null;
        headerTV.setText(title);
        singleDateAndTimePickerDialog = bottomSheetDateTimePicker.findViewById(R.id.dateTimePicker);

        minDateRange = AppConstants.addSimpleCurrentDateTime(0, 0);
        maxDateRange = AppConstants.addSimpleCurrentDateTime(maxDateInDays, 0);

        singleDateAndTimePickerDialog.setMinDate(minDateRange);
        singleDateAndTimePickerDialog.setMaxDate(maxDateRange);

//        singleDateAndTimePickerDialog.setDefaultDate(minDateRange);
//            singleDateAndTimePickerDialog.setStepSizeMinutes(steppingMinutes);
//            singleDateAndTimePickerDialog.setDisplayHours(isShowHours);
//            singleDateAndTimePickerDialog.setDisplayMinutes(isShowMinutes);
        singleDateAndTimePickerDialog.setDisplayDays(isShowDays);
        singleDateAndTimePickerDialog.setDisplayMonths(isShowMonths);
        singleDateAndTimePickerDialog.setDisplayDaysOfMonth(isShowDaysOfMonth);
        singleDateAndTimePickerDialog.setDisplayYears(isShowYears);
        singleDateAndTimePickerDialog.setCustomLocale(Locale.US);
//                singleDateAndTimePickerDialog.title(activity.getString(R.string.observation_end_time));
//                singleDateAndTimePickerDialog.mainColor(ContextCompat.getColor(activity, R.color.red_shade_dark))
//                singleDateAndTimePickerDialog.titleTextColor(ContextCompat.getColor(activity, R.color.white))
        singleDateAndTimePickerDialog.setBackgroundColor(ContextCompat.getColor(activity, R.color.background_color));
        singleDateAndTimePickerDialog.setMustBeOnFuture(true);
    }

    public void showDateTimePicker() {
        bottomSheetDateTimePicker.show();
    }

    public void hideDateTimePicker() {
        bottomSheetDateTimePicker.dismiss();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bsSubmitBtn) {

            selectedDate = singleDateAndTimePickerDialog.getDate();

            // Double check min date range
            if (selectedDate == null) {
                AlertPopup.alert(activity, activity.getString(R.string.invalid_time_error),
                        activity.getString(R.string.time_not_selected_error_desc),
                        AppConstants.TYPE_ERROR);
                return;
            }

            // Double check min date range
            if (selectedDate.compareTo(AppConstants.getCurrentDateTime()) < 0) {
                AlertPopup.alert(activity, activity.getString(R.string.invalid_time_error),
                        activity.getString(R.string.invalid_time_error_desc),
                        AppConstants.TYPE_ERROR);
                return;
            }

            // Double check max date range
            if (selectedDate.compareTo(maxDateRange) > 0) {
                AlertPopup.alert(activity, activity.getString(R.string.invalid_time_error),
                        String.format(activity.getString(R.string.invalid_time_max_error_desc), maxDateInDays),
                        AppConstants.TYPE_ERROR);
                return;
            }
            iDateTimePickerEventCallback.onDateTimeSelected(selectedDate);
            bottomSheetDateTimePicker.dismiss();
        } else if (id == R.id.bsCloseBtn) {
            bottomSheetDateTimePicker.dismiss();
        }
    }

    public interface IDateTimePickerEventCallback {
        void onDateTimeSelected(Date date);
    }

}
*/
