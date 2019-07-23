package com.ftofs.twant.constant;

public enum RequestCode {
    /**
     * 添加收貨地址
     */
    ADD_ADDRESS,
    /**
     * 切換收貨地址
     */
    CHANGE_ADDRESS,
    /**
     * 確認訂單
     */
    CONFIRM_ORDER,

    /**
     * 選擇收貨地址
     */
    SELECT_ADDR,

    /**
     * 編輯發票
     */
    EDIT_RECEIPT,

    /**
     * 从相册选择文件
     */
    OPEN_ALBUM,

    /**
     * 選擇POST封面圖片
     */
    PICK_POST_COVER,

    /**
     * 編輯標題
     */
    EDIT_TITLE,

    /**
     * 編輯關鍵字
     */
    EDIT_KEYWORD,

    /**
     * 掃描二維碼
     */
    SCAN_QR_CODE,
}
