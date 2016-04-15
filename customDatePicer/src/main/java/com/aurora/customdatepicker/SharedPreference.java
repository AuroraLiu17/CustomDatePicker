package com.aurora.customdatepicker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aurora Liu on 4/15/16.
 */
public class SharedPreference {

    private static final String SETTING = "Setting";
    public static final String DEFAULT_DATE_PICKER_MODE = "default_date_picker_mode";

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    private static SharedPreference instance = null;

    private SharedPreference(Context context) {
        initial(context);
    }

    public static SharedPreference getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreference(context);
        }
        return instance;
    }

    public void initial(Context context) {
        mSharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public int getDatePickerMode(){
        return mSharedPreferences.getInt(DEFAULT_DATE_PICKER_MODE, DatePickerDataSourceFactory.MODE_COMMON);
    }

    public void setDatePickerMode(int mode){
        mEditor.putInt(DEFAULT_DATE_PICKER_MODE,
                mode).apply();
    }
}
