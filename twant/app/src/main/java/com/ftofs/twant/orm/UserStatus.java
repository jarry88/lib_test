package com.ftofs.twant.orm;

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
}
