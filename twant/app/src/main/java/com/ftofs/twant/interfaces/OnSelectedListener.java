package com.ftofs.twant.interfaces;


/**
 * 選中監聽接口，一般用于選中事件的回調
 * @author zwm
 */
public interface OnSelectedListener {
    public static final int TYPE_DEFAULT = 0;

    /**
     * 選中后的通知回調
     * @param type 類型
     * @param id 選中哪一個
     * @param extra 附加數據
     */
    public void onSelected(int type, int id, Object extra);
}
