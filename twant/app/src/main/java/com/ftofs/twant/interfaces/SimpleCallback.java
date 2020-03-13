package com.ftofs.twant.interfaces;

/**
 * 簡單的回調接口
 * @author zwm
 */
public interface SimpleCallback {
    /**
     * 選擇圖片
     */
    public static final int ACTION_SELECT_IMAGE = 1;


    void onSimpleCall(Object data);
}
