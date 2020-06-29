package com.ftofs.twant.util;

import com.ftofs.twant.entity.TimeInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期時間相關工具類
 * @author zwm
 */
public class Time {
    /**
     * 与fromUnixtime()函数类似，只是第1个参数为毫秒数
     * @param millisUnixtime  毫秒的unix 时间戳
     * @param format
     * @return
     */
    public static String fromMillisUnixtime(long millisUnixtime, String format) {
        String pattern = format2pattern(format);
        // 乘以1000，从秒转换为毫秒
        Date date = new Date(millisUnixtime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 从php的date()函数的格式转为java日期格式化的pattern
     * @param format  php的date()函数的日期格式字符串
     *                Y - 年份，例如 2019
     *                m - 月份，固定為2位數，例如 04
     *                d - 天， 固定為2位數，例如 27
     *                H - 時，固定為2位數，例如 14
     *                i - 分，固定為2位數，例如 41
     *                s - 秒，固定為2位數，例如 07
     *                u - 毫秒，固定為3位數，例如 003
     *         例如, 時間 2019-04-27 14:41:07.003 對應的format為  Y-m-d H:i:s.u
     * @return
     */
    private static String format2pattern(String format) {
        int len = format.length();
        // pattern的格式为  yyyy-MM-dd HH:mm:ss.SSS
        String pattern = "";
        for (int i = 0; i < len; ++i) {
            char c = format.charAt(i);
            if (c == 'Y') {
                pattern += "yyyy";
            }
            else if (c == 'm') {
                pattern += "MM";
            }
            else if (c == 'd') {
                pattern += "dd";
            }
            else if (c == 'H') {
                pattern += "HH";
            }
            else if (c == 'i') {
                pattern += "mm";
            }
            else if (c == 's') {
                pattern += "ss";
            }
            else if (c == 'u') {
                pattern += "SSS";
            }
            else {
                pattern += c;
            }
        }
        return pattern;
    }


    /**
     * 获取当前的unix时间戳(秒)
     * @return
     */
    public static int timestamp() {
        // 从毫秒转为秒
        return (int)(timestampMillis() / 1000);
    }

    /**
     * 获取当前的unix时间戳(毫秒)
     * @return
     */
    public static long timestampMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 計算兩個時間戳之差(開始時間不能晚于結束時間)
     * @param from 開始時間
     * @param to 結束時間
     * @return 如果開始時間晚於結束時間，返回null
     */
    public static TimeInfo diff(int from, int to) {
        if (from > to) {
            return null;
        }

        int d = to - from;
        TimeInfo timeInfo = new TimeInfo();
        timeInfo.day = d / 86400;
        d = d % 86400;
        timeInfo.hour = d / 3600;
        d = d % 3600;
        timeInfo.minute = d / 60;
        timeInfo.second = d % 60;

        return timeInfo;
    }

    /**
     * 計算離團購結束還有多長時間
     * @param currTime 當前時間
     * @param endTime 結束時間
     * @return
     */
    public static TimeInfo groupTimeDiff(long currTime, long endTime) {
        if (currTime > endTime) {  // 如果當前時間大於結束時間，返回全為零的時間信息
            return new TimeInfo();
        }

        long d = endTime - currTime;
        int diffSecond = (int) (d / 1000);
        TimeInfo timeInfo = new TimeInfo();

        timeInfo.hour = diffSecond / 3600;
        diffSecond = diffSecond % 3600;
        timeInfo.minute = diffSecond / 60;
        timeInfo.second = diffSecond % 60;
        timeInfo.milliSecond = (int) (d % 1000);

        return timeInfo;
    }
}
