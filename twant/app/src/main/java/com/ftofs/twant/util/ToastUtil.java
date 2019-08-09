package com.ftofs.twant.util;

import android.content.Context;

import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.log.SLog;

import org.json.JSONObject;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import es.dmoral.toasty.Toasty;

/**
 * Toast工具類
 * @author zwm
 */
public class ToastUtil {
    /**
     * 通用錯誤提示
     */
    public static String COMMON_ERROR_MESSAGE = "系統錯誤";

    public static void info(Context context, String text) {
        Toasty.info(context, text, Toasty.LENGTH_SHORT, true).show();
    }

    public static void success(Context context, String text) {
        Toasty.success(context, text, Toasty.LENGTH_SHORT, true).show();
    }

    public static void error(Context context, String text) {
        Toasty.error(context, text, Toasty.LENGTH_SHORT, true).show();
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
                if (!ToastUtil.isNull(responseObj) && responseObj.exists("datas.error")) {
                    try {
                        errorMessage = responseObj.getString("datas.error");
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }

                    if (StringUtil.isEmpty(errorMessage)) {
                        return true;
                    }
                } else {
                    error(context, "返回內容為空");
                }
            }

            error(context, errorMessage);
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
        if (isNull(responseObj)) {
            return true;
        }

        SLog.info("responseObj[%s]", responseObj.getClass());

        SLog.info("responseObj[%s][%s]", responseObj, responseObj.toString());
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


    public static boolean isNull(EasyJSONObject easyJSONObject) {
        if (easyJSONObject == null) {
            return true;
        }

        if ("null".equals(easyJSONObject.toString())) {
            return true;
        }

        return false;
    }

    public static void showNetworkError(Context context, IOException e) {
        error(context, e.getMessage());
    }
}
