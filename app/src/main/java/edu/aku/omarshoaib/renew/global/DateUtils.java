package edu.aku.omarshoaib.renew.global;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static edu.aku.omarshoaib.renew.global.AppConstants._EMPTY_;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.edittextpicker.aliazaz.EditTextPicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import edu.aku.omarshoaib.renew.R;

public class DateUtils {

    // Get formatted Current Date Time
    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.APP_DATE_TIME_FORMAT,
                Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Get Current Date Time in specified format
    public static String getCurrentDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                Locale.ENGLISH);
        Date date = new Date();
        return dateFormat.format(date);
    }

    // DateTime formatter with Date Object
    public static String getFormattedDateTime(Date dateTime, String outputFormat) {
        SimpleDateFormat cFormat = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        return cFormat.format(dateTime);
    }

    // DateTime formatter with String
    public static String getFormattedDateTime(String dateTime, String inputFormat, String outputFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
            Date newDate = format.parse(dateTime);

            format = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
            assert newDate != null;
            return format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _EMPTY_;
    }

    // Check and Get server and device date
    public static List<String> checkServerAndDeviceDate(String date, String format) {
        List<String> list = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            Date strDate = sdf.parse(date);
            SimpleDateFormat sdf1 = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            String serverDate = sdf1.format(Objects.requireNonNull(strDate));
            String systemDate = sdf1.format(new Date());
            if (!serverDate.equals(systemDate)) {
                // '0' index = Server Date
                // '1' index = System Date
                list.add(serverDate);
                list.add(systemDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Check valid date format
    public static boolean isDateFormatValid(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setLenient(false); // Ensures strict parsing
        try {
            // Attempt to parse the date
            sdf.parse(dateStr);
            return true; // Valid date
        } catch (ParseException e) {
            return false; // Invalid date format
        }
    }

    // For showing incorrect device date error popup
    public static void showDeviceDateErrorAlert(Activity activity, String serverDate, String systemDate) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_date_error, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        TextView txtDia = view.findViewById(R.id.dateTV);
        Button btnYes = view.findViewById(R.id.btnYes);
        txtDia.setText(String.format(activity.getString(R.string.incorrect_device_date_desc), serverDate, systemDate));

        // To prevent error on show dialog after activity is finishing or destroyed
        if (activity.isFinishing() || activity.isDestroyed()) return;

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        btnYes.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            activity.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
        });
    }

    // Convert millis to String date
    public static String convertMillisToDate(long millis, String outputFormat) {
        Date date = new Date(millis);
        return getFormattedDateTime(date, AppConstants.APP_DATE_FORMAT);
    }

    /**
     * START - For adding or subtracting days or months from the date
     */

    // For adding/subtract Days in specified DateTime in DateTimePicker
    public static String addSubDays(String specifiedDate, int addSubDays) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfIn = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            Date inputDate = sdfIn.parse(specifiedDate);
            cal.setTime(Objects.requireNonNull(inputDate));
            cal.add(Calendar.DAY_OF_MONTH, addSubDays);
            SimpleDateFormat sdfOut = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            return sdfOut.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return _EMPTY_;
        }
    }

    // For adding/subtract months in specified DateTime in DateTimePicker
    public static String addSubMonths(String specifiedDate, int addSubMonths) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfIn = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            Date inputDate = sdfIn.parse(specifiedDate);
            cal.setTime(Objects.requireNonNull(inputDate));
            cal.add(Calendar.MONTH, addSubMonths);
            SimpleDateFormat sdfOut = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            return sdfOut.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return _EMPTY_;
        }
    }

    // For adding/subtract Years in specified DateTime in DateTimePicker
    public static String addSubYears(String specifiedDate, int addSubYears) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfIn = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            Date inputDate = sdfIn.parse(specifiedDate);
            cal.setTime(Objects.requireNonNull(inputDate));
            cal.add(Calendar.YEAR, addSubYears);
            SimpleDateFormat sdfOut = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            return sdfOut.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return AppConstants._EMPTY_;
        }
    }

    // For adding/subtract both days and months in specified DateTime in DateTimePicker
    public static String addSubDaysMonths(String specifiedDate, int addSubDays, int addSubMonths) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfIn = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            Date inputDate = sdfIn.parse(specifiedDate);
            cal.setTime(Objects.requireNonNull(inputDate));
            cal.add(Calendar.DAY_OF_MONTH, addSubDays);
            /*if (addSubMonths > 0)*/ cal.add(Calendar.MONTH, addSubMonths);
            SimpleDateFormat sdfOut = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            return sdfOut.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return _EMPTY_;
        }
    }

    /**
     * END - For adding or subtracting days or months from the date
     */

    // Calculate difference between two dates
    public static String calculateDiffInDates(String d1, String d2, int diffType, boolean isNegativeAllowed) {
        try {
            String dateFormat;
            if (diffType == AppConstants.DATE_DIFF_IN_DAYS)
                dateFormat = AppConstants.APP_DATE_FORMAT;
            else dateFormat = AppConstants.APP_DATE_TIME_FORMAT;

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            assert date1 != null && date2 != null;

            if (!isNegativeAllowed) {
                // If date2 is bigger than date1 than swap values
                Date _temp; // Just for switching values
                if (date2.compareTo(date1) > 0) {
                    _temp = date2;
                    date2 = date1;
                    date1 = _temp;
                }
            }

            int diff;
            if (diffType == AppConstants.DATE_DIFF_IN_DAYS) {
                // Difference in days
                diff = Math.toIntExact((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
            } else if (diffType == AppConstants.DATE_DIFF_IN_HOURS) {
                // Difference in hours
                diff = Math.toIntExact((date1.getTime() - date2.getTime()) / (1000L * 60 * 60));
            } else {
                // Difference in minutes
                diff = Math.toIntExact((date1.getTime() - date2.getTime()) / (1000L * 60));
            }
            if (!isNegativeAllowed)
                return diff >= 0 ? Integer.toString(diff) : _EMPTY_;
            else return Integer.toString(diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _EMPTY_;
    }

    // Calculate age in years, months and days from days given as input
    // Index 0 = Years
    // Index 1 = Months
    // Index 2 = Days
    public static List<String> calculateAgeFromDays(String days) {
        List<String> age = new ArrayList<>();
        if (AppConstants.isEmpty(days)) return age;
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.add(Calendar.DAY_OF_YEAR, -Integer.parseInt(days)); // Subtract the given number of days from the current date

        int years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int daysLeft = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);

        if (daysLeft < 0) {
            months--;
            daysLeft += today.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (months < 0) {
            years--;
            months += 12;
        }

        age.add(Integer.toString(years));
        age.add(Integer.toString(months));
        age.add(Integer.toString(daysLeft));
        return age;
    }

    // Set max value by current month
    public static void setMaxDayByCurrent(Object obj) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        if (obj instanceof EditTextPicker)
            ((EditTextPicker) obj).setMaxvalue(cal.get(Calendar.DAY_OF_MONTH));
    }

    // Set max value by current month
    public static void setMaxMonthByCurrent(Object obj) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        if (obj instanceof EditTextPicker)
            ((EditTextPicker) obj).setMaxvalue(cal.get(Calendar.MONTH) + 1);
    }

    // Set min value by current year
    public static void setMinYearByCurrent(Object obj, int addSubYears) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        if (obj instanceof EditTextPicker)
            ((EditTextPicker) obj).setMinvalue(cal.get(Calendar.YEAR) + addSubYears);
    }

    // Set min value by (from whatever date)
    public static void setMinYearBy(String specifiedDate, Object obj, int addSubYears) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
            Date inputDate = sdf.parse(specifiedDate);
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTime(inputDate);
            if (obj instanceof EditTextPicker)
                ((EditTextPicker) obj).setMinvalue(cal.get(Calendar.YEAR) + addSubYears);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Set max value by current year
    public static void setMaxYearByCurrent(Object obj) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        if (obj instanceof EditTextPicker)
            ((EditTextPicker) obj).setMaxvalue(cal.get(Calendar.YEAR));
    }

    // Calculate AGE from DOB wrt specified date or Current date
    /* *
     * Age calculation from day, month and year.
     * dov = Date of Visit
     * Return list of string including age in years, months and days.
     * Example Calculated Age = 29 years 2 months and 20 days
     * index 0 = Years like 29 years
     * index 1 = Months like 2 months
     * index 2 = Days like 20 days
     * */

    // Calculate AGE from DOB from current date
    public static List<String> calculateAge(String year, String month, String day) {
        return calculateAge(_EMPTY_, year, month, day);
    }

    // Calculate AGE from DOB from current date wrt specified date
    public static List<String> calculateAge(String dov, String year, String month, String day) {
        List<String> ageList = new ArrayList<String>() {{
            add(_EMPTY_);
            add(_EMPTY_);
            add(_EMPTY_);
        }};
        if (!AppConstants.isEmpty(year) && !AppConstants.isEmpty(month) && !AppConstants.isEmpty(day)) {
            Calendar cal = Calendar.getInstance();

            int _year = Integer.parseInt(year);
            int _month = Integer.parseInt(month);
            int _day = Integer.parseInt(day) - 1;

            int defaultYear = Integer.parseInt(AppConstants.DEFAULT_MIN_YEAR);
            if (_year < defaultYear || (!month.equals("98") && _month > 12) || (!day.equals("98") && _day > 31))
                return ageList;

            // Default value
            _month = _month != 98 ? _month : 6;
            _day = _day != 98 ? _day : 15;

            try {
                SimpleDateFormat df = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
                cal.setTime(Objects.requireNonNull(df.parse(_year + "-" + _month + "-" + _day)));

                long millis;
                if (!AppConstants.isEmpty(dov)) {
                    // Calculate age with respect to Date of Screening (dos)
                    Calendar calSDate = Calendar.getInstance();
                    calSDate.setTime(Objects.requireNonNull(df.parse(dov)));
                    millis = calSDate.getTimeInMillis() - cal.getTimeInMillis();
                } else  // Calculate age with respect to Current date
                    millis = System.currentTimeMillis() - cal.getTimeInMillis();
                cal.setTimeInMillis(millis);

                long inDays = MILLISECONDS.toDays(millis);
                long inYears = (long) (inDays / 365.2425);
                long inMonths = (long) ((inDays - (inYears * 365.2425)) / 30.43);
                long tDay = (long) (inDays - ((inYears * 365.2425) + (inMonths * 30.43)));

                ageList.clear();
                ageList.add(inYears > 0 ? Long.toString(inYears) : "0");
                ageList.add(inMonths > 0 ? Long.toString(inMonths) : "0");
                ageList.add(inDays > 0 ? Long.toString(tDay) : "0");
                return ageList;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return ageList;
    }

    // Calculate DOB from AGE
    /* *
     * Calculate date of birth from age
     * Return list of strings including DOB year and month.
     * Example Calculated DOB = year 1947 and month 8.
     * index 0 = Year like 1947
     * index 1 = Month like 8
     * index 2 = Day like 2
     * */
    public static List<String> calculateDOBFromAge(String ageYear, String ageMonth) {
        return calculateDOBFromAge(ageYear, ageMonth, _EMPTY_);
    }

    public static List<String> calculateDOBFromAge(String ageYear, String ageMonth, String ageDay) {
        List<String> birthList = new ArrayList<String>() {{
            add(AppConstants._EMPTY_);
            add(AppConstants._EMPTY_);
        }};
        if (!AppConstants.isEmpty(ageYear) && !AppConstants.isEmpty(ageMonth)) {
            Calendar today = Calendar.getInstance(); // Current date

            int _year = Integer.parseInt(ageYear);
            int _month = Integer.parseInt(ageMonth);
            int _day = AppConstants.isEmpty(ageDay) ? 15 : Integer.parseInt(ageDay);

            // Subtract the age
            today.add(Calendar.YEAR, -_year); // Subtract years
            today.add(Calendar.MONTH, -_month); // Subtract months

            // Handle days carefully
            if (_day > today.get(Calendar.DAY_OF_MONTH)) {
                today.add(Calendar.MONTH, -1); // Borrow a month
                int daysInPreviousMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH); // Days in previous month
                today.set(Calendar.DAY_OF_MONTH, daysInPreviousMonth - (_day - today.get(Calendar.DAY_OF_MONTH)));
            } else {
                today.add(Calendar.DAY_OF_MONTH, -_day); // Subtract days
            }

            // Extract the adjusted date
            int birthYear = today.get(Calendar.YEAR);
            int birthMonth = today.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
            int birthDay = today.get(Calendar.DAY_OF_MONTH);

            // Populate the result list
            birthList.clear();
            birthList.add(Integer.toString(birthYear));
            birthList.add(Integer.toString(birthMonth));
            birthList.add(Integer.toString(birthDay));
        }
        return birthList;
    }

    // Calculate days, months and years (Cumulative) into days
    public static double getAgeInDays(String years, String months, String days) {
        int _years = 0, _months = 0, _days = 0;
        if (!AppConstants.isEmpty(years))
            _years = Integer.parseInt(years);

        if (!AppConstants.isEmpty(months))
            _months = Integer.parseInt(months);

        if (!AppConstants.isEmpty(days))
            _days = Integer.parseInt(days);

        double yearsInDays = _years * 365.2425;
        double monthsInDays = _months * 30.43;

        return Math.ceil(yearsInDays + monthsInDays + _days);
    }

    // Calculate months and years (Cumulative) into days
    public static double getAgeInDays(String years, String months) {
        int _years = 0, _months = 0;
        if (!AppConstants.isEmpty(years))
            _years = Integer.parseInt(years);

        if (!AppConstants.isEmpty(months))
            _months = Integer.parseInt(months);

        double yearsInDays = _years * 365.2425;
        double monthsInDays = _months * 30.43;

        return Math.ceil(yearsInDays + monthsInDays);
    }

    // Calculate days, months and years (Cumulative) into months
    public static int getAgeInMonths(String years, String months) {
        int _years = 0, _months = 0;
        if (!AppConstants.isEmpty(years))
            _years = Integer.parseInt(years);

        if (!AppConstants.isEmpty(months))
            _months = Integer.parseInt(months);

        return (_years * 12) + _months;
    }

    // Calculate no.of weeks by days
    public static int getWeeksByDays(int days) {
        if (days < 0) return 0;
        return days / 7; // Integer division
    }

    // Get dob from age in months
    public static List<String> getDobFromAgeInMonths(int totalMonths) {
        int years = totalMonths / 12;  // Get the number of years
        int months = totalMonths % 12; // Get the remaining months
        int days = months * 30; // Approximate days (assuming 30 days per month)

        return new ArrayList<String>() {{
            add(Integer.toString(years));
            add(Integer.toString(months));
            add(Integer.toString(days));
        }};
    }

    /**
     * For checking the date in 3 separated EditTexts like
     * dayET = day EditText, monthET = month EditText, yearET = year EditText
     * DMY = Day, Month, Year
     */

    // Criteria:
    // minDate = given date
    // maxDax = CurrentDate
    public static boolean isDMYDateValid(String minDate, EditTextPicker yearET,
                                         EditTextPicker monthET, EditTextPicker dayET) {
        // Day, Month, Year supplied from EditTexts
        int etYear = Integer.parseInt(Objects.requireNonNull(yearET.getText()).toString());
        int etMonth = Integer.parseInt(Objects.requireNonNull(monthET.getText()).toString());
        int etDay = Integer.parseInt(Objects.requireNonNull(dayET.getText()).toString());

        int defaultYear = Integer.parseInt(AppConstants.DEFAULT_MIN_YEAR);
        if (etYear < defaultYear || (etMonth != 98 && etMonth > 12) || (etDay != 98 && etDay > 31))
            return false;

        // Split min. date
        String[] minDateArr = minDate.split("-");
        int minYear = Integer.parseInt(minDateArr[0]);
        int minMonth = Integer.parseInt(minDateArr[1]);
        int minDay = Integer.parseInt(minDateArr[2]);

        // Get current date
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        // Setting default input month and day if passed day or month is 98
        etMonth = etMonth != 98 ? etMonth : Math.min(currentMonth, minMonth);
        etDay = etDay != 98 ? etDay : Math.min(currentDay, minDay);

        // Check the minimum
        // If inserted year < minYear then condition is false
        //if etYear does not lie within Range return false
        if (etYear < minYear || etYear > currentYear) return false;

        //if etYear lies within Range return true
        if (etYear > minYear && etYear < currentYear) return true;

        // if the min-max lies within the same year we need to check both min and max months and dates
        if (minYear == currentYear) {
            // check if month is in range
            if (etMonth < currentMonth && etMonth > minMonth) return true;

            // check if min-max is same year then we need to check weather etday is in range
            if (minMonth == currentMonth) return etDay >= minDay && etDay <= currentDay;

            // if min-month then compare with min day
            if (etMonth == minMonth && etDay >= minDay) return true;

            // if max-month then compare with max day
            return etMonth == currentMonth && etDay <= currentDay;

        } else if (etYear == minYear) {
            if (etMonth > minMonth) return true; // if month is within range

            return etMonth == minMonth && etDay >= minDay;// if month is last within range we compare day

        } else {  // if(etYear == currentYear)
            if (etMonth < currentMonth) return true; // if month is within range

            return etMonth == currentMonth && etDay <= currentDay;// if month is last within range we compare day
        }
    }

    // Criteria:
    // minDate = given minDate
    // maxDax = given maxDate
    public static boolean isDMYDateValid(String minDate, String maxDate, EditTextPicker yearET,
                                         EditTextPicker monthET, EditTextPicker dayET) {
        // Day, Month, Year supplied from EditTexts
        int etYear = Integer.parseInt(Objects.requireNonNull(yearET.getText()).toString());
        int etMonth = Integer.parseInt(Objects.requireNonNull(monthET.getText()).toString());
        int etDay = Integer.parseInt(Objects.requireNonNull(dayET.getText()).toString());

        int defaultYear = Integer.parseInt(AppConstants.DEFAULT_MIN_YEAR);
        if (etYear < defaultYear || (etMonth != 98 && etMonth > 12) || (etDay != 98 && etDay > 31))
            return false;

        // Split min. date
        String[] minDateArr = minDate.split("-");
        int minYear = Integer.parseInt(minDateArr[0]);
        int minMonth = Integer.parseInt(minDateArr[1]);
        int minDay = Integer.parseInt(minDateArr[2]);

        // Split max. date
        String[] maxDateArr = maxDate.split("-");
        int maxYear = Integer.parseInt(maxDateArr[0]);
        int maxMonth = Integer.parseInt(maxDateArr[1]);
        int maxDay = Integer.parseInt(maxDateArr[2]);

        // Set input month and day to minimum if they are (98)
        etMonth = etMonth != 98 ? etMonth : Math.min(maxMonth, minMonth);
        etDay = etDay != 98 ? etDay : Math.min(maxDay, minDay);

        //if etYear does not lie within Range return false
        if (etYear < minYear || etYear > maxYear) return false;

        //if etYear lies within Range return true
        if (etYear > minYear && etYear < maxYear) return true;

        // if the min-max lies within the same year we need to check both min and max months and dates
        if (minYear == maxYear) {
            // check if month is in range
            if (etMonth < maxMonth && etMonth > minMonth) return true;

            // check if min-max is same year then we need to check weather etday is in range
            if (minMonth == maxMonth) return etDay >= minDay && etDay <= maxDay;

            // if min-month then compare with min day
            if (etMonth == minMonth && etDay >= minDay) return true;

            // if max-month then compare with max day
            return etMonth == maxMonth && etDay <= maxDay;

        } else if (etYear == minYear) {
            if (etMonth > minMonth) return true; // if month is within range

            return etMonth == minMonth && etDay >= minDay;// if month is last within range we compare day

        } else {  // if(etYear == maxYear)
            if (etMonth < maxMonth) return true; // if month is within range

            return etMonth == maxMonth && etDay <= maxDay;// if month is last within range we compare day
        }
    }

    // Calculate Afghan Date to Gregorian/Default Date
    /*public static String convertAfghanDateToDefault(String... afghanDateArr) {
        if (afghanDateArr.length == 1) {
            // It means its a whole date (1402-08-01) otherwise its in day, month, year format
            afghanDateArr = afghanDateArr[0].split("-");
        }
        if (afghanDateArr[0].length() == 0 || afghanDateArr[1].length() == 0 || afghanDateArr[2].length() == 0)
            return _EMPTY_;
        // Create a PersianCalendar and set the Afghan date
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.set(Integer.parseInt(afghanDateArr[0]),
                Integer.parseInt(afghanDateArr[1]) - 1, Integer.parseInt(afghanDateArr[2]));

        // Convert the Persian date to a Gregorian date
        Date gregorianDate = persianCalendar.getTime();

        // Format and print the Afghan date and the corresponding Gregorian date
        SimpleDateFormat gregorianDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        // Format the Gregorian date in the default time zone
        String gregorianDateStr = gregorianDateFormat.format(gregorianDate);

        // Return Afghan date and the corresponding Gregorian date
        return gregorianDateStr;
    }

    // Calculate Default Date to Afghan Date
    public static String convertDefaultToAfghanDate(String... gregorianDateArr) {
        if (gregorianDateArr.length == 1) {
            // It means it's a whole date (2024-12-13) otherwise it's in day, month, year format
            gregorianDateArr = gregorianDateArr[0].split("-");
        }
        if (gregorianDateArr[0].length() == 0 || gregorianDateArr[1].length() == 0 || gregorianDateArr[2].length() == 0)
            return "Invalid Date"; // Custom placeholder for empty or invalid date inputs

        // Parse the Gregorian date
        int year = Integer.parseInt(gregorianDateArr[0]);
        int month = Integer.parseInt(gregorianDateArr[1]) - 1; // Subtract 1 because months are 0-based in Java
        int day = Integer.parseInt(gregorianDateArr[2]);

        // Create a Gregorian date
        Date gregorianDate = new Date(year - 1900, month, day); // Date constructor takes years since 1900

        // Create a PersianCalendar instance
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTime(gregorianDate);

        // Get the Afghan (Persian) date components (Persian year, month, day)
        int persianYear = persianCalendar.getYear();
        int persianMonth = persianCalendar.getMonth() + 1; // Persian months are 1-based
        int persianDay = persianCalendar.getDayOfMonth();

        // Return the Afghan date in the format "yyyy-MM-dd"
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d", persianYear, persianMonth, persianDay);
    }

    // Get max dari year by current gregorian year
    public static int getMaxDariYear() {
        // Get current Gregorian date
        Calendar calendar = new GregorianCalendar();
        int currentYear = calendar.get(Calendar.YEAR);

        // Convert to Dari (Persian) year using PersianCalendar (API 24+)
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        return persianCalendar.get(Calendar.YEAR); // Returns Dari Year
    }*/

}
