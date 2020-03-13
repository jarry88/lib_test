package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * Hawk工具類
 * @author zwm
 */
public class HawkUtil {
    /**
     * 從Hawk中獲取用戶數據
     * @return
     */
    public static EasyJSONObject getUserDataObject() {
        // 從Hawk中獲取用戶數據
        String userDataStr = Hawk.get(SPField.FIELD_USER_DATA, null);
        if (StringUtil.isEmpty(userDataStr)) {
            userDataStr = "{}";
        }

        EasyJSONObject userDataObj = EasyJSONObject.parse(userDataStr);
        return userDataObj;
    }

    /**
     * 根據key獲取用戶數據
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getUserData(String key, T defaultValue) {
        try {
            EasyJSONObject userDataObj = getUserDataObject();
            if (userDataObj.exists(key)) {
                return (T) userDataObj.get(key);
            }
            return defaultValue;
        } catch (Exception e) {

        }

        return null;
    }

    public static <T> void setUserData(String key, T value) {
        try {
            EasyJSONObject userDataObj = getUserDataObject();
            userDataObj.set(key, value);

            Hawk.put(SPField.FIELD_USER_DATA, userDataObj.toString());
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
