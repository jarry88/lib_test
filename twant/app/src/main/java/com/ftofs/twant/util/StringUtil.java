package com.ftofs.twant.util;

import android.content.Context;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.Mobile;

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
     * 格式化价格
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

    /**
     * 区号转为地区Id
     * @param areaCode
     * @return
     */
    public static int areaCodeToAreaId(String areaCode) {
        switch (areaCode) {
            case Constant.AREA_CODE_HONGKONG:
                return Constant.AREA_ID_HONGKONG;
            case Constant.AREA_CODE_MAINLAND:
                return Constant.AREA_ID_MAINLAND;
            case Constant.AREA_CODE_MACAO:
                return Constant.AREA_ID_MACAO;
            default:
                return Constant.AREA_ID_UNKNOWN;
        }
    }


    /**
     * 获取手机号信息
     * @param fullMobile  带区号的手机号码  008613417785707 这种格式 或 0086,13417785707 这种格式
     * @return
     */
    public static Mobile getMobileInfo(String fullMobile) {
        Mobile mobile = new Mobile();
        if (fullMobile.indexOf(',') != -1) {
            // 用逗号分隔这种格式
            String[] result = fullMobile.split(",");
            mobile.areaCode = result[0];
            mobile.mobile = result[1];
            mobile.areaId = areaCodeToAreaId(mobile.areaCode);
            return mobile;
        }

        if (fullMobile.startsWith(Constant.AREA_CODE_HONGKONG)) {
            mobile.areaId = Constant.AREA_ID_HONGKONG;
            mobile.areaCode = fullMobile.substring(0, 5);
            mobile.mobile = fullMobile.substring(5);
        } else if (fullMobile.startsWith(Constant.AREA_CODE_MACAO)) {
            mobile.areaId = Constant.AREA_ID_MACAO;
            mobile.areaCode = fullMobile.substring(0, 5);
            mobile.mobile = fullMobile.substring(5);
        } else if (fullMobile.startsWith(Constant.AREA_CODE_MAINLAND)) {
            mobile.areaId = Constant.AREA_ID_MAINLAND;
            mobile.areaCode = fullMobile.substring(0, 4);
            mobile.mobile = fullMobile.substring(4);
        } else {
            // 未知地区
            mobile.areaId = Constant.AREA_ID_UNKNOWN;
            mobile.areaCode = "";
            mobile.mobile = fullMobile;
        }

        return mobile;
    }
}
