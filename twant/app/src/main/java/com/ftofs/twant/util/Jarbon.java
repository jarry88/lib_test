package com.ftofs.twant.util;

import java.util.Calendar;

import java.util.Calendar;

public class Jarbon {
    /**
     * 時間戳(秒)
     */
    private int timestamp;

    /**
     * 時間戳(毫秒)
     */
    private long timestampMillis;


    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int milliSecond;

    /**
     * 星期几
     * 星期一  1
     * 星期二  2
     * 星期三  3
     * 星期四  4
     * 星期五  5
     * 星期六  6
     * 星期日  7
     */
    private int dayOfWeek;


    public Jarbon() {
        this(System.currentTimeMillis());
    }

    public Jarbon(long timestampMillis) {
        this.timestamp = (int) (timestampMillis / 1000);
        this.timestampMillis = timestampMillis;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampMillis);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        milliSecond = calendar.get(Calendar.MILLISECOND);


        /*
        Calender星期几的表示
         * 星期日  1
         * 星期一  2
         * 星期二  3
         * 星期三  4
         * 星期四  5
         * 星期五  6
         * 星期六  7
         */
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            dayOfWeek = 7;
        } else {
            dayOfWeek = dayOfWeek - 1;
        }
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
        if (datetime == null) {
            return null;
        }

        int len = datetime.length();

        int Y = 0;
        int m = 0;
        int d = 0;
        int H = 0;
        int i = 0;
        int s = 0;
        int u = 0;
        if (len >= 10) { // 至少為這種格式  2019-07-26
            Y = Integer.valueOf(datetime.substring(0, 4));
            m = Integer.valueOf(datetime.substring(5, 7));
            d = Integer.valueOf(datetime.substring(8, 10));

            if (len >= 19) { // 至少為這種格式  2019-07-26 08:15.20
                H = Integer.valueOf(datetime.substring(11, 13));
                i = Integer.valueOf(datetime.substring(14, 16));
                s = Integer.valueOf(datetime.substring(17, 19));

                if (len >= 23) { // 這種格式 2019-07-26 08:15.20.083
                    u = Integer.valueOf(datetime.substring(20, 23));
                }
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Y, m, d, H, i, s);
        calendar.set(Calendar.MILLISECOND, u);

        return new Jarbon(calendar.getTimeInMillis());
    }

    public Jarbon startOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Jarbon(calendar.getTimeInMillis());
    }


    public int diffInDays(Jarbon jarbon) {
        Jarbon startOfDay1 = startOfDay();
        Jarbon startOfDay2 = jarbon.startOfDay();

        int timestamp1 = startOfDay1.getTimestamp();
        int timestamp2 = startOfDay2.getTimestamp();

        return (timestamp1 - timestamp2) / 86400;
    }

    @Override
    public String toString() {
        return String.format("datetime[%04d-%02d-%02d %02d:%02d:%02d.%03d],timestamp[%d],timestampMillis[%s],dayOfWeek[%d]",
                year, month, day, hour, minute, second, milliSecond, timestamp, timestampMillis, dayOfWeek);
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
        return timestampMillis == jarbon.timestampMillis;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public long getTimestampMillis() {
        return timestampMillis;
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

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}
