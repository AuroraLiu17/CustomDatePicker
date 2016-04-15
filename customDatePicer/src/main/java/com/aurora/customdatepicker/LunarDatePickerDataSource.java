package com.aurora.customdatepicker;

import android.content.Context;
import com.aurora.customdatepicker.simonvt.NumberPicker.*;
import com.aurora.customdatepicker.utils.Lunar;

/**
 * A Chinese lunar calendar data source
 * Created by Aurora Liu on 3/25/16.
 */
public class LunarDatePickerDataSource extends DatePicker.AbstractDatePickerDataSource {
    private Lunar mCurrentLunar;

    public LunarDatePickerDataSource(Context context, String minDate, String maxDate) {
        super(context, minDate, maxDate);
        mContext = context;
        mCurrentLunar = new Lunar(mCurrentDate.getTimeInMillis());

        mDayFormatter = new Formatter() {
            @Override
            public String format(int value) {
                return Lunar.getLunarDayString(value);
            }
        };

        mMonthFormatter = new Formatter() {
            @Override
            public String format(int value) {
                value += 1;
                int leapMonth = Lunar.getLunarLeapMonth(mCurrentLunar.getLunarYear());
                String string = "";
                if (mCurrentLunar.isLeapYear() && value == leapMonth + 1) {
                    string = "闰";
                }
                return string + Lunar.getLunarMonthString(mCurrentLunar.isLeapYear() && value > leapMonth ? value - 1 : value);
            }
        };
    }

    @Override
    public int monthNum() {
        return monthNumForYear(mCurrentLunar.getLunarYear());
    }

    @Override
    public int monthNumForYear(int year) {
        return getLunarMonthNum(year);
    }

    @Override
    public int dayNum() {
        return dayNumForYearAndMonth(mCurrentLunar.getLunarYear(), getLunarMonthIndexWithLeap(mCurrentLunar));
    }

    @Override
    public int dayNumForYearAndMonth(int year, int month) {
        return getLunarDaysOfMonth(year, month);
    }

    @Override
    protected void updateCurrentTime(long timeInMills) {
        super.updateCurrentTime(timeInMills);
        mCurrentLunar = new Lunar(timeInMills);
    }

    @Override
    public int getYear() {
        return mCurrentLunar.getLunarYear();
    }

    @Override
    public int getMonth() {
        return getLunarMonthIndexWithLeap(mCurrentLunar) - 1;
    }

    @Override
    public int getDayOfMonth() {
        return mCurrentLunar.getLunarDay();
    }

    @Override
    public void updateDay(int newDay) {
        long timeInMills = Lunar.getTimeInMillsWithLunar(mCurrentLunar.getLunarYear(), mCurrentLunar.getLunarMonth(),
                mCurrentLunar.isLeap(), newDay);
        updateDate(timeInMills);
    }

    @Override
    public void updateMonth(int newMonth) {
        newMonth += 1;
        int leapMonth = Lunar.getLunarLeapMonth(mCurrentLunar.getLunarYear());
        long timeInMills = Lunar.getTimeInMillsWithLunar(mCurrentLunar.getLunarYear(), leapMonth > 0 && newMonth > leapMonth ? newMonth - 1 : newMonth,
                newMonth == leapMonth + 1, mCurrentLunar.getLunarDay());
        updateDate(timeInMills);
    }

    @Override
    public void updateYear(int newYear) {
        long timeInMills = Lunar.getTimeInMillsWithLunar(newYear, mCurrentLunar.getLunarMonth(),
                Lunar.getLunarLeapMonth(newYear) == mCurrentLunar.getLunarMonth(), mCurrentLunar.getLunarDay());
        updateDate(timeInMills);
    }

    // ------------------- Lunar Helpers ---------------------

    /**
     * Get the month counts of a lunar year
     * @param lunarYear
     * @return
     */
    private static int getLunarMonthNum(int lunarYear) {
        return Lunar.getLunarLeapMonth(lunarYear) > 0 ? 13 : 12;
    }

    /**
     * Start from 1 - 13
     * @param lunar
     * @return
     */
    private static int getLunarMonthIndexWithLeap(Lunar lunar) {
        if (lunar.isLeapYear()) {
            int leapMonth = Lunar.getLunarLeapMonth(lunar.getLunarYear());
            if (lunar.isLeap() || lunar.getLunarMonth() > leapMonth) {
                return lunar.getLunarMonth() + 1;
            }
        }
        return lunar.getLunarMonth();
    }

    /**
     * Get the days count of lunarYear, lunarMonthWithLeap
     * @param lunarYear
     * @param lunarMonthWithLeap The month index with leap month,
     *                           so if the leap month of the year is 9,
     *                           then the leap month index is 10
     * @return
     */
    private static int getLunarDaysOfMonth(int lunarYear, int lunarMonthWithLeap) {
        int leapMonth = Lunar.getLunarLeapMonth(lunarYear);
        if (leapMonth != 0 && leapMonth == lunarMonthWithLeap - 1) {
            return Lunar.getLunarLeapDays(lunarYear);
        }
        return Lunar.getLunarMonthDays(lunarYear, lunarMonthWithLeap > leapMonth ? lunarMonthWithLeap - 1 : lunarMonthWithLeap);
    }
}