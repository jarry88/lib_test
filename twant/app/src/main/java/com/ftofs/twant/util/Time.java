package com.ftofs.twant.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期時間相關工具類
 * @author zwm
 */
public class Time {
    public static String date(String format) {
        return fromMillisUnixtime(System.currentTimeMillis(), format);
    }

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
}
