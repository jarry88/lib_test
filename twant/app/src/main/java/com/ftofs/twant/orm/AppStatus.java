package com.ftofs.twant.orm;

import org.litepal.crud.LitePalSupport;

/**
 * App狀態數據
 * @author zwm
 */
public class AppStatus extends LitePalSupport {
    public enum Key {
        /**
         * 表情版本
         */
        KEY_EMOJI_VERSION
    }

    public int key;
    public String value;
}
