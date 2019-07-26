package com.example;

import java.util.Date;

public class Jarbon {
    /**
     * 毫秒
     */
    private long timestamp;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int milliSecond;

    public long getTimestamp() {
        return timestamp;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getMilliSecond() {
        return milliSecond;
    }

    public Jarbon(long timestamp) {
        this.timestamp = timestamp;

        Date date = new Date(timestamp);
        year = date.getYear();
        month =
    }

    /**
     * 解析字符串格式的日期时间
     * 支持如下格式:
     *      2019-07-26 08:15.20.083
     *      2019-07-26 08:15.20
     *      2019-07-26
     * @param datetime
     * @return
     */
    public static Jarbon parse(String datetime) {
        return null;
    }

    public Jarbon startOfDay() {
        return null;
    }

    public int diffInDays(Jarbon jarbon) {
        Jarbon startOfDay1 = startOfDay();
        Jarbon startOfDay2 = jarbon.startOfDay();

        long timestamp1 = startOfDay1.getTimestamp();
        long timestamp2 = startOfDay2.getTimestamp();

        return (int) ((timestamp1 - timestamp2) / 86400000);
    }

    @Override
    public String toString() {
        return String.format("datetime[%04d-%02d-%02d %02d:%02d:%02d.%03d],timestamp[%s]",
                year, month, day, hour, minute, second, milliSecond, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Jarbon)) {
            return false;
        }

        Jarbon jarbon = (Jarbon) obj;
        return timestamp == jarbon.timestamp;
    }
}
