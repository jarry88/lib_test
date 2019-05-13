package com.ftofs.twant.util;

import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;

/**
 * 用戶相關工具類
 * @author zwm
 */
public class User {
    /**
     * 獲取當前已登錄的用戶Id
     * @return 如果已經登錄，返回用戶Id；否則，返回0
     */
    public static int getUserId() {
        int userId = Hawk.get(SPField.FIELD_USER_ID, 0);
        String token = Hawk.get(SPField.FIELD_TOKEN, "");
        String nickname = Hawk.get(SPField.FIELD_NICKNAME, "");
        int lastLoginTime = Hawk.get(SPField.FIELD_LAST_LOGIN_TIME, 0);

        SLog.info("userId[%d], token[%s], nickname[%s], lastLoginTime[%d]",
                userId, token, nickname, lastLoginTime);

        int now = Time.timestamp();

        if (userId > 0 && !StringUtil.isEmpty(token) && !StringUtil.isEmpty(nickname) &&
                now - lastLoginTime < Config.LOGIN_VALID_TIME) {
            SLog.info("用戶已登錄, token[%s]", token);
            return userId;
        }

        SLog.info("用戶【未】登錄");
        return 0;
    }

    /**
     * 退出登錄，清空數據
     */
    public static void logout() {
        Hawk.delete(SPField.FIELD_USER_ID);
        Hawk.delete(SPField.FIELD_TOKEN);
        Hawk.delete(SPField.FIELD_NICKNAME);
        Hawk.delete(SPField.FIELD_LAST_LOGIN_TIME);
    }

    /**
     * 獲取當前已登錄用戶的Token
     * @return
     */
    public static String getToken() {
        return Hawk.get(SPField.FIELD_TOKEN);
    }
}
