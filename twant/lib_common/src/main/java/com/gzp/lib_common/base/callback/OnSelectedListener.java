package com.gzp.lib_common.base.callback;


import com.gzp.lib_common.constant.PopupType;

/**
 * 選中監聽接口，一般用于選中事件的回調
 * @author zwm
 */
public interface OnSelectedListener {
    /**
     * 選中后的通知回調
     * @param type 類型
     * @param id 選中哪一個
     * @param extra 附加數據
     */
    void onSelected(PopupType type, int id, Object extra);
}
