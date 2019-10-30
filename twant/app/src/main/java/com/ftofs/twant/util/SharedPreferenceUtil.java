package com.ftofs.twant.util;

import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
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
            Hawk.put(SPField.FIELD_LAST_LOGIN_TIME, Time.timestamp());  // 最近一次登錄時間

            String fullMobile = responseObj.getString("datas.memberVo.mobileAreaCode") + "," + responseObj.getString("datas.memberVo.mobile");
            Hawk.put(SPField.FIELD_MOBILE, fullMobile);  // 區號+手機號
            Hawk.put(SPField.FIELD_MOBILE_ENCRYPT, responseObj.getString("datas.memberVo.mobileEncrypt"));
            Hawk.put(SPField.FIELD_MEMBER_NAME, responseObj.getString("datas.memberName"));
            Hawk.put(SPField.FIELD_IM_TOKEN, responseObj.getString("datas.imToken"));
            Hawk.put(SPField.FIELD_AVATAR, responseObj.getString("datas.memberVo.avatar"));
            Hawk.put(SPField.FIELD_GENDER, responseObj.getInt("datas.memberVo.memberSex"));
            Hawk.put(SPField.FIELD_MEMBER_SIGNATURE, responseObj.getString("datas.memberVo.memberSignature"));
            Hawk.put(SPField.FIELD_MEMBER_BIO, responseObj.getString("datas.memberVo.memberBio"));
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }
    }
}
