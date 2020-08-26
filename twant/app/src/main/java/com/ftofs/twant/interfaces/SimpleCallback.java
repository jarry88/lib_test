package com.ftofs.twant.interfaces;

/**
 * 簡單的回調接口
 * @author zwm
 */
public interface SimpleCallback {
    /**
     * 選擇圖片
     */
    int ACTION_SELECT_IMAGE = 1;

    /**
     * 選擇取消訂單的原因
     */
    int ACTION_SELECT_CANCEL_ORDER_REASON = 2;


    void onSimpleCall(Object data);
}
