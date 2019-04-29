package com.ftofs.twant.util;

import com.ftofs.twant.constant.SPField;
import com.orhanobut.hawk.Hawk;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * Android SharedPreferenceUtil 相關的工具類
 * @author zwm
 */
public class SharedPreferenceUtil {
    /**
     * 保存注冊/登錄返回的用戶數據
     * @param responseObj
     */
    public static void saveUserInfo(EasyJSONObject responseObj) {
        try {
            Hawk.put(SPField.FIELD_USER_ID, responseObj.getInt("datas.memberId"));
            Hawk.put(SPField.FIELD_TOKEN, responseObj.getString("datas.token"));
            Hawk.put(SPField.FIELD_NICKNAME, responseObj.getString("datas.nickName"));

            // 最近一次登錄時間
            Hawk.put(SPField.FIELD_LAST_LOGIN_TIME, Time.timestamp());
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }
}
