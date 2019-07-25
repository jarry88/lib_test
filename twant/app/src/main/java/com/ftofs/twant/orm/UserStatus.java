package com.ftofs.twant.orm;

import com.ftofs.twant.log.SLog;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * User狀態數據
 * @author zwm
 */
public class UserStatus extends LitePalSupport {
    public enum Key {
        /**
         * 表情版本(因為預防以數有自定義表情，所以將表情作為用戶級的數據)
         */
        KEY_EMOJI_VERSION
    }

    public int key;
    public String value;


    /**
     * 獲取當前用戶的表情版本號
     * @return
     */
    public static String getEmojiVersion() {
        UserStatus userStatus = LitePal.where("key = ?", String.valueOf(UserStatus.Key.KEY_EMOJI_VERSION.ordinal()))
                .findFirst(UserStatus.class);

        String emojiVersion = "";
        if (userStatus != null) {
            emojiVersion = userStatus.value;
        }
        SLog.info("emojiVersion[%s]", emojiVersion);

        return emojiVersion;
    }
}
