package com.aurora.customdatepicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends Activity implements DatePickerDialogFragment.DatePickerListener {

    private TextView mTextView;
    private Calendar mTmpCalendar = Calendar.getInstance();

    private DatePickerDialogFragment mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text_view);
        updateTextView();
    }

    @Override
    public void onPickDate(Calendar calendar) {
        mTmpCalendar.setTimeInMillis(calendar.getTimeInMillis());
        updateTextView();
    }

    public void showDatePickerDialog(View view) {
        if (mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialogFragment();
            mDatePickerDialog.setDatePickerListener(this);
        }
        if (!mDatePickerDialog.isVisible()) {
            mDatePickerDialog.setTime(mTmpCalendar.getTimeInMillis());
            mDatePickerDialog.show(getFragmentManager(), "DatePickerDialogFragment");
        }
    }

    private void updateTextView() {
        mTextView.setText(String.format("Current selected day is:\n%d/%d/%d",
                mTmpCalendar.get(Calendar.YEAR),
                mTmpCalendar.get(Calendar.MONTH) + 1,
                mTmpCalendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Override
    public void onBackPressed() {
        if (mDatePickerDialog != null && mDatePickerDialog.isVisible()) {
            mDatePickerDialog.dismiss();
        }
        super.onBackPressed();
    }
}
