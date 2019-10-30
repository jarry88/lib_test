package com.ftofs.twant.util;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.hawk.Hawk;

/**
 * 用戶相關工具類
 * @author zwm
 */
public class User {
    public static boolean isLogin() {
        return getUserId() > 0;
    }

    /**
     * 獲取當前已登錄的用戶Id
     * @return 如果已經登錄，返回用戶Id；否則，返回0
     */
    public static int getUserId() {
        int userId = Hawk.get(SPField.FIELD_USER_ID, 0);
        String token = Hawk.get(SPField.FIELD_TOKEN, "");
        String nickname = Hawk.get(SPField.FIELD_NICKNAME, "");
        int lastLoginTime = Hawk.get(SPField.FIELD_LAST_LOGIN_TIME, 0);
        String memberName = Hawk.get(SPField.FIELD_MEMBER_NAME, "");

        // SLog.info("userId[%d], token[%s], nickname[%s], lastLoginTime[%d]", userId, token, nickname, lastLoginTime);

        int now = Time.timestamp();

        if (userId > 0 && !StringUtil.isEmpty(token) && !StringUtil.isEmpty(nickname) &&
                now - lastLoginTime < Config.LOGIN_VALID_TIME && !StringUtil.isEmpty(memberName)) {
            // SLog.info("用戶已登錄, token[%s]", token);
            return userId;
        }

        // SLog.info("用戶【未】登錄");
        return 0;
    }

    /**
     * 退出登錄，清空數據
     */
    public static void logout() {
        EMClient emClient = EMClient.getInstance();
        if (emClient != null) {
            emClient.logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgress(int progress, String status) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int code, String message) {
                    // TODO Auto-generated method stub

                }
            });
        }

        // 解除友盟別名綁定
        TwantApplication.getInstance().delUmengAlias();

        Hawk.delete(SPField.FIELD_USER_ID);
        Hawk.delete(SPField.FIELD_TOKEN);
        Hawk.delete(SPField.FIELD_NICKNAME);
        Hawk.delete(SPField.FIELD_LAST_LOGIN_TIME);
        Hawk.delete(SPField.FIELD_MOBILE);
        Hawk.delete(SPField.FIELD_MOBILE_ENCRYPT);
        Hawk.delete(SPField.FIELD_MEMBER_NAME);
        Hawk.delete(SPField.FIELD_IM_TOKEN);
        Hawk.delete(SPField.FIELD_AVATAR);
        Hawk.delete(SPField.FIELD_GENDER);
        Hawk.delete(SPField.FIELD_MEMBER_SIGNATURE);
        Hawk.delete(SPField.FIELD_MEMBER_BIO);
    }

    public static <T> T getUserInfo(String field, T defaultValue) {
        if (getUserId() == 0) {
            return defaultValue;
        }

        return Hawk.get(field, defaultValue);
    }

    /**
     * 獲取當前已登錄用戶的Token
     * @return 如果用戶未登錄，返回null
     */
    public static String getToken() {
        if (getUserId() == 0) {
            return null;
        }
        return Hawk.get(SPField.FIELD_TOKEN);
    }
}
