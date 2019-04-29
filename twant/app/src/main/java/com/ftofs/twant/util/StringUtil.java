package com.ftofs.twant.util;

/**
 * 字符串工具類
 * @author zwm
 */
public class StringUtil {
    /**
     * 判斷字符串是否為空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
