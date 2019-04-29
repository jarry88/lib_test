package com.ftofs.twant.util;

import android.content.Context;
import android.widget.Toast;

import com.ftofs.twant.constant.ResponseCode;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * Toast工具類
 * @author zwm
 */
public class ToastUtil {
    /**
     * 通用錯誤提示
     */
    public static String COMMON_ERROR_MESSAGE = "系統錯誤";

    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 檢查是否有錯并顯示
     * @param context
     * @param responseObj
     * @return 如果有錯，返回true; 否則，返回false
     */
    public static boolean checkError(Context context, EasyJSONObject responseObj) {
        if (responseObj == null) {
            show(context, COMMON_ERROR_MESSAGE);
            return true;
        }

        try {
            int code = responseObj.getInt("code");
            if (code != ResponseCode.SUCCESS) {
                show(context, COMMON_ERROR_MESSAGE);
                return true;
            }
        } catch (EasyJSONException e) {
            e.printStackTrace();
            show(context, COMMON_ERROR_MESSAGE);
            return true;
        }
        return false;
    }
}
