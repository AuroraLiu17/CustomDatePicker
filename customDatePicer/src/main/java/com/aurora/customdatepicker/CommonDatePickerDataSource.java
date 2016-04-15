package com.aurora.customdatepicker;

import android.content.Context;
import com.aurora.customdatepicker.simonvt.NumberPicker;

/**
 * Common data source of DatePicker
 * Created by Aurora Liu on 4/15/16.
 */

public class CommonDatePickerDataSource extends DatePicker.AbstractDatePickerDataSource {
    private String mDayFormatString;
    private String mMonthFormatString;
    public CommonDatePickerDataSource(Context context, String minDate, String maxDate) {
        super(context, minDate, maxDate);
        mDayFormatString = context.getString(R.string.day_format_string);
        mMonthFormatString = context.getString(R.string.month_format_string);
        mDayFormatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(mDayFormatString, value);
            }
        };

        mMonthFormatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(mMonthFormatString, value + 1);
            }
        };
    }

    public String[] monthStringsForYear(int year) {
        int monthOfYear = monthNumForYear(year) + 1;
        String[] strings = new String[monthOfYear];
        for (int i = 0; i < monthOfYear; i++) {
            strings[i] = String.format(mMonthFormatString, i + 1);
        }
        return strings;
    }

    @Override
    public String[] dayStringsForYearAndMonth(int year, int month) {
        int days = dayNumForYearAndMonth(year, month);
        String[] strings = new String[days];
        for (int i = 0; i < days; i++) {
            strings[i] = String.format(mDayFormatString, i + 1);
        }
        return strings;
    }
}


