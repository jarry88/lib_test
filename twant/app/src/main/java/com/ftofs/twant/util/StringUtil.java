package com.ftofs.twant.util;

import android.content.Context;

import com.ftofs.twant.R;

/**
 * 字符串工具類
 * @author zwm
 */
public class StringUtil {
    private static String currencyTypeSign;

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

    /**
     * 格式化格式
     * @param context
     * @param price
     * @param spaceCount 美元符$与价格的空格个数
     * @return
     */
    public static String formatPrice(Context context, float price, int spaceCount) {
        if (isEmpty(currencyTypeSign)) {
            currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        }

        StringBuilder sb = new StringBuilder(currencyTypeSign);
        for (int i = 0; i < spaceCount; i++) {
            sb.append(" ");
        }
        sb.append(String.format("%.2f", price));
        return sb.toString();
    }
}
