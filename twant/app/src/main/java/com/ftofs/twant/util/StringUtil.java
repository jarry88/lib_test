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

    public static String implode(String glue, String[] strArray)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<strArray.length;i++) {
            sb.append((i == strArray.length - 1) ? strArray[i] : strArray[i] + glue);
        }
        return sb.toString();
    }
}
