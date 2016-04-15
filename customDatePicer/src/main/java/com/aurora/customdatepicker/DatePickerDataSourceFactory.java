package com.aurora.customdatepicker;

import android.content.Context;

/**
 * Created by liuxiaohui on 4/15/16.
 */
public class DatePickerDataSourceFactory {
    public static final int MODE_LUNAR = 1;
    public static final int MODE_COMMON = 2;

    public static DatePicker.DatePickerDataSource getDataSourceByMode(int mode, Context context, String minDate, String maxDate) {
        switch (mode) {
            case MODE_COMMON:
                return new CommonDatePickerDataSource(context, minDate, maxDate);
            default:
                return new LunarDatePickerDataSource(context, minDate, maxDate);
        }
    }
}
