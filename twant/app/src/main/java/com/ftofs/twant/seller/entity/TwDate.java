package com.ftofs.twant.seller.entity;

import androidx.annotation.NonNull;

import com.ftofs.twant.util.Util;

/**
 * 日期結構體
 * @author zwm
 */
public class TwDate {
    public int year;
    public int month;
    public int day;

    public TwDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * 日期比較
     * @param date1
     * @param date2
     * @return 如果date1早於date2，返回-1
     *          如果date1晚於date2，返回1
     *          如果是同一天，返回0
     */
    public static int compare(TwDate date1, TwDate date2) {
        if (date1.year != date2.year) {
            return Util.sign(date1.year - date2.year);
        }
        if (date1.month != date2.month) {
            return Util.sign(date1.month - date2.month);
        }
        return Util.sign(date1.day - date2.day);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}
