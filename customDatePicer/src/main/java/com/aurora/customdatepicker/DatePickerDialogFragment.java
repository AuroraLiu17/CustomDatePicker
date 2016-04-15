package com.aurora.customdatepicker;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Aurora Liu on 3/11/16.
 */
public class DatePickerDialogFragment extends DialogFragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private DatePicker mDatePicker;
    private TextView mBackToToday;
    private TextView mSaveButton;
    private RadioGroup mSwitcher;
    private DatePickerListener mDatePickerListener;

    private int mMode = DatePickerDataSourceFactory.MODE_COMMON;
    private Calendar mCalendar = Calendar.getInstance();

    public interface DatePickerListener
    {
        void onPickDate(Calendar calendar);
    }

    public void setDatePickerListener(DatePickerListener mListener) {
        this.mDatePickerListener = mListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        window.setLayout(getScreenWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Press back button to save & dismiss
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && getDialog().isShowing()) {
                    save();
                    return true;
                }
                return false;
            }
        });
        View view = inflater.inflate(R.layout.dialog_date_picker, container);
        mBackToToday = (TextView) view.findViewById(R.id.back_to_today);
        mSaveButton = (TextView) view.findViewById(R.id.save);
        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, long timeInMills) {
                mCalendar.setTimeInMillis(timeInMills);
            }
        });

        mSwitcher = (RadioGroup) view.findViewById(R.id.calendar_swicher);
        mSwitcher.setOnCheckedChangeListener(this);

        mBackToToday.setOnClickListener(this);
        setBtnTouchColorFade(mBackToToday);
        mSaveButton.setOnClickListener(this);
        setBtnTouchColorFade(mSaveButton);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == mSwitcher) {
            switch (checkedId) {
                case R.id.common_calendar_switcher:
                    setMode(DatePickerDataSourceFactory.MODE_COMMON);
                    break;
                case R.id.lunar_calendar_switcher:
                    setMode(DatePickerDataSourceFactory.MODE_LUNAR);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                save();
                break;
            case R.id.back_to_today:
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                updateDatePickerFromCalendar();
                break;
        }
    }

    public void setMode(int mode) {
        mMode = mode;
        if (mDatePicker != null) {
            mDatePicker.setMode(getActivity(), mode);
        }
    }

    public void setTime(long time) {
        mCalendar.setTimeInMillis(time);
    }

    public void save() {
        if (mDatePickerListener != null) {
            saveCalendarFromDatePicker();
            mDatePickerListener.onPickDate(mCalendar);
        }
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDatePickerFromCalendar();
        ((RadioButton) mSwitcher.findViewById(mMode == DatePickerDataSourceFactory.MODE_LUNAR ?
                R.id.lunar_calendar_switcher : R.id.common_calendar_switcher))
                .setChecked(true);
     }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        if (getActivity() != null) {
            mMode = SharedPreference.getInstance(getActivity()).getDatePickerMode();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        SharedPreference.getInstance(getActivity()).setDatePickerMode(mMode);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveCalendarFromDatePicker();
    }

    private void updateDatePickerFromCalendar() {
        mDatePicker.updateDate(mCalendar.getTimeInMillis());
    }

    private void saveCalendarFromDatePicker() {
        mCalendar.setTimeInMillis(mDatePicker.getTimeInMills());
    }

    public static int getScreenWidth(Context context) {
        Point size = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static void setBtnTouchColorFade(final View view) {
        if (view == null)
            return;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    view.setAlpha((float) 0.3);
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    view.setAlpha(1);
                }
                return false;
            }
        });
    }
}
