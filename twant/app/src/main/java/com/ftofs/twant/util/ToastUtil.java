package com.ftofs.twant.util;

import android.content.Context;
import android.widget.Toast;

import com.ftofs.twant.constant.ResponseCode;

import java.io.IOException;

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
        return checkError(context, responseObj, null);
    }


    /**
     * 檢查是否有錯并顯示
     * @param context
     * @param responseObj
     * @param defaultErrorMessage 如果有指定錯誤消息，則顯示錯誤消息；否則，顯示服務器應答的錯誤消息
     * @return
     */
    public static boolean checkError(Context context, EasyJSONObject responseObj, String defaultErrorMessage) {
        if (isError(responseObj)) {
            String errorMessage = defaultErrorMessage;
            if (StringUtil.isEmpty(errorMessage)) {
                if (responseObj.exists("datas.error")) {
                    try {
                        errorMessage = responseObj.getString("datas.error");
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }

                    if (StringUtil.isEmpty(errorMessage)) {
                        return true;
                    }
                }
            }

            show(context, errorMessage);
            return true;
        }
        return false;
    }

    /**
     * 檢查服務器API返回是否有錯
     * @param responseObj
     * @return
     */
    public static boolean isError(EasyJSONObject responseObj) {
        if (responseObj == null) {
            return true;
        }

        try {
            int code = responseObj.getInt("code");
            if (code != ResponseCode.SUCCESS) {
                return true;
            }
        } catch (EasyJSONException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static void showNetworkError(Context context, IOException e) {
        ToastUtil.show(context, e.getMessage());
    }
}
