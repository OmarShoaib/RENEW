package edu.aku.omarshoaib.renew.global;

import android.app.Activity;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RadioGroup;

import com.edittextpicker.aliazaz.EditTextPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.aku.omarshoaib.renew.R;
import edu.aku.omarshoaib.renew.activity.BaseActivity;
import edu.aku.omarshoaib.renew.model.DPortal;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

public class Helper {

    /**
     * FOR RADIO GROUPS CLEAR CHECKS WHEN
     * HIDDEN OR DISABLED
     */

    // For radio group clear checks
    private static final Handler handler = new Handler();
    private static final List<RadioGroup> radioGroupList = new ArrayList<>();
    private static boolean isClearChecksRunnableRunning = false;

    public static void radioGroupsClearChecks(BaseActivity baseActivity, Activity activity) {
        // Get All radioGroups in child activity
        if (activity != null && !isClearChecksRunnableRunning) {
            View view = activity.findViewById(R.id.GrpName);
            if (view instanceof ViewGroup) {
                if (radioGroupList.isEmpty()) {
                    getAllRadioGroupsAndSetDatePickerFormat((ViewGroup) view);
                    if (!radioGroupList.isEmpty())
                        handler.post(clearChecksRunnable);
                } else handler.post(clearChecksRunnable);
            }
            // To call helperCB here in order to update the clearCheck
            // handler running status
            baseActivity.setIHelperCB(iHelperCB);
        }
    }

    // Get all radio groups
    private static void getAllRadioGroupsAndSetDatePickerFormat(ViewGroup parent) {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RadioGroup)
                radioGroupList.add((RadioGroup) child);

            if (child instanceof ViewGroup)
                getAllRadioGroupsAndSetDatePickerFormat((ViewGroup) child); // Recursive call
            else if (child instanceof DatePickerEditText)
                ((DatePickerEditText) child).setDateFormat(sdf);
        }
    }

    private static final Runnable clearChecksRunnable = new Runnable() {
        @Override
        public void run() {
            isClearChecksRunnableRunning = true;
            clearChecks();
            handler.postDelayed(this, 500); // Repeat every 500ms
        }
    };

    // Clear checks on radio groups based on their visibility and its enable/disable state
    private static void clearChecks() {
        for (int i = 0; i < radioGroupList.size(); i++) {
            RadioGroup rg = radioGroupList.get(i);
            if (!isViewVisible(rg) && rg.getCheckedRadioButtonId() != -1) {
                rg.clearCheck();
            }
        }
    }

    // Check if radio group is visible.
    // This extra check is because the radio group is visible
    // but its parent is gone so ultimately the radio group is also
    // not visible in the UI but programmatically its visible
    private static boolean isViewVisible(View view) {
        if (view == null) return false; // Null check

        // Check if this view itself is hidden
        if (view.getVisibility() != View.VISIBLE /*|| !view.isEnabled()*/)
            return false;

        // Recursively check all parent views
        ViewParent parent = view.getParent();
        if (parent instanceof View)
            return isViewVisible((View) parent);

        return true; // If no parent is hidden, it's visible
    }

    /**
     * FOR DYNAMIC RANGE UPDATE
     */

    public static void applyRanges(Activity activity) {
        Map<String, DPortal.RangeData> ranges = DPortal.getInstance().loadFromSharedPrefs();

        for (Map.Entry<String, DPortal.RangeData> entry : ranges.entrySet()) {
            String label = entry.getKey();
            DPortal.RangeData rules = entry.getValue();

            View view = AppConstants.getViewByName(activity, label);
            if (view == null) continue;

            // EditTextPicker - for numeric validation
            if (view instanceof EditTextPicker) {
                // For Ranged EditText
                EditTextPicker picker = (EditTextPicker) view;
                // type 1 = range
                picker.setType(1);

                String minStr = rules.getMinValue();
                String maxStr = rules.getMaxValue();

                if (isValidNumber(minStr) && isValidNumber(maxStr)) {
                    Float min = parseToFloat(minStr);
                    Float max = parseToFloat(maxStr);

                    if (min != null && max != null) {
                        picker.setMinvalue(min);
                        picker.setMaxvalue(max);

                        // Check if the min or max is a decimal value
                        boolean isDecimal = isDecimalValue(minStr) || isDecimalValue(maxStr);

                        if (isDecimal) {
                            // For decimal range (20.0 - 50.0)
                            picker.setInputType(InputType.TYPE_CLASS_NUMBER/* | InputType.TYPE_NUMBER_FLAG_DECIMAL*/);

                            // Detect decimal places
                            int digitsBeforeDecimal = String.valueOf(max.intValue()).length(); // max length = digit count of max value
                            int digitsAfterDecimal = getDecimalPlaces(max);

                            String mask = repeatChar('#', digitsBeforeDecimal) + "." + repeatChar('#', digitsAfterDecimal);
                            String pattern = "^\\d{" + digitsBeforeDecimal + "," + digitsBeforeDecimal + "}\\.\\d{" + digitsAfterDecimal + "," + digitsAfterDecimal + "}$";
                            int totalLength = digitsBeforeDecimal + 1 + digitsAfterDecimal;

                            picker.setMask(mask);
                            picker.setPattern(pattern);
                            picker.setHint(mask);
                            picker.setFilters(new InputFilter[]{new InputFilter.LengthFilter(totalLength)});

                        } else {
                            // For whole number range (20 - 50)
                            picker.setInputType(InputType.TYPE_CLASS_NUMBER);

                            int digits = String.valueOf(max.intValue()).length(); // max length = digit count of max value
                            picker.setFilters(new InputFilter[]{new InputFilter.LengthFilter(digits)});
                            picker.setHint(repeatChar('#', digits));
                        }
                    }
                } else
                    Log.d("DynamicRanges", "Invalid Range: " + label + " - min: " + minStr + ", max: " + maxStr);
            } /*else if (view instanceof DatePickerEditText) {
                // For date range
                DatePickerEditText datePicker = (DatePickerEditText) view;
                datePicker.setMinDate(AppConstants.isEmpty(rules.getMinValue())
                        || rules.getMinValue().contains("today")
                        || (!rules.getMinValue().contains("-") && !rules.getMinValue().contains("/")) ? "CR_DATE" : rules.getMinValue());
                datePicker.setMaxDate(AppConstants.isEmpty(rules.getMaxValue())
                        || rules.getMinValue().contains("today")
                        || (!rules.getMinValue().contains("-") && !rules.getMinValue().contains("/")) ? "CR_DATE" : rules.getMaxValue());
            }*/
        }
    }

    private static boolean isValidNumber(String value) {
        if (value == null) return false;
        return value.matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean isDecimalValue(String value) {
        return value != null && value.contains(".");
    }

    private static Float parseToFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static int getDecimalPlaces(Number number) {
        String str = number.toString();
        if (str.contains(".")) {
            return str.length() - str.indexOf('.') - 1;
        }
        return 0;
    }

    private static String repeatChar(char c, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(c);
        }
        return builder.toString();
    }

    private static IHelperCB iHelperCB = status -> {
        isClearChecksRunnableRunning = status;
        if (!status)
            handler.removeCallbacks(clearChecksRunnable);
    };

    public static void resetRadioGroupList() {
        radioGroupList.clear();
    }


    public interface IHelperCB {
        void clearCheckRunnableStatus(boolean status);
    }

}