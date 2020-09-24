package com.gzp.lib_common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.gzp.lib_common.R;
import com.gzp.lib_common.constant.ResponseCode;
import com.gzp.lib_common.model.User;
import com.gzp.lib_common.service.AppService;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;

import static com.gzp.lib_common.utils.StringUtilKt.isEmpty;

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
        StyleableToast.makeText(context, text, Toast.LENGTH_SHORT, R.style.tw_toast).show();
    }

    public static void success(Context context, String text) {
        StyleableToast.makeText(context, text, Toast.LENGTH_SHORT, R.style.tw_toast).show();
    }

    public static void error(Context context, String text) {
        SLog.bt();
        if (text == null || text.length() == 0) {
            SLog.info("空text ，現在設置為不彈窗，只輸出日志");
            return;
        }
        StyleableToast.makeText(context, text, Toast.LENGTH_SHORT, R.style.tw_toast).show();
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
            if (isEmpty(errorMessage)) {
                if (!ToastUtil.isNull(responseObj) && responseObj.exists("datas.error")) {
                    try {
                        errorMessage = responseObj.getSafeString("datas.error");
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                    error(context, errorMessage);

                    if (isEmpty(errorMessage)) {
                        return true;
                    }
                } else {
                    error(context, "返回内容為空");
                }
            }

            if (!ToastUtil.isNull(responseObj) && responseObj.exists("code")) {
                try {
                    int code = responseObj.getInt("code");
                    if (code == ResponseCode.NOT_LOGIN) {
                        // 如果是未登錄，則刪除Token
                        User.logout();
//                        AppService appService = AppJoint.service(AppService.class);
//                        appService.errorPopToMainFragment(context);

                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

        // SLog.info("responseObj[%s]", responseObj.getClass());

        // SLog.info("responseObj[%s][%s]", responseObj, responseObj.toString());
        try {
            int code = responseObj.getInt("code");
            if (code == ResponseCode.NOT_LOGIN) {
                // 如果是未登錄，則刪除Token
                User.logout();
            }
            if (code != ResponseCode.SUCCESS) {
                return true;
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        //e.getMessage()
        error(context,"當前網絡信號弱");
    }
}
