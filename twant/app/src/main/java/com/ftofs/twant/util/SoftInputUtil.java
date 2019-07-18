package com.ftofs.twant.util;


import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.constant.SPField;
import com.orhanobut.hawk.Hawk;

public class SoftInputUtil {
    /**
     * 獲取軟鍵盤高度
     * @return  單位px
     */
    public static int getSoftInputHeight() {
        return Hawk.get(SPField.FIELD_KEYBOARD_HEIGHT, Util.dip2px(TwantApplication.getContext(), 223)); // 默認223dp
    }

    /**
     * 設置軟鍵盤高度
     * @param keyboardHeight  單位px
     */
    public static void setSoftInputHeight(int keyboardHeight) {
        Hawk.put(SPField.FIELD_KEYBOARD_HEIGHT, keyboardHeight);
    }
}
