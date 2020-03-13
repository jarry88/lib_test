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
     * 選擇发貨地址
     */
    SELECT_SENDER_ADDR,

    /**
     * 填寫發貨信息
     */
    INPUT_SENDER_INFO,

    /**
     * 選擇收貨地址
     */
    SELECT_RECEIVER_ADDR,

    /**
     * 填寫發貨信息
     */
    INPUT_RECEIVER_INFO,

    /**
     * 編輯單據
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

    /**
     * 拍照片
     */
    CAMERA_IMAGE,

    /**
     * 拍視頻
     */
    CAMERA_VIDEO,

    /**
     * 請求權限
     */
    REQUEST_PERMISSION,

    /**
     * 快遞查詢
     */
    LOGISTICS_QUERY,

    /**
     * 添加、編輯常用語、常用版式
     */
    UPDATE_COMMON_USED_SPEECH,

    /**
     * 支付寶支付
     */
    ALI_PAY,

    /**
     * 添加說說
     */
    ADD_COMMENT,


    /**
     * 請求安裝APP的權限
     */
    REQUEST_INSTALL_APP_PERMISSION,

    /**
     * 選擇想要帖產品
     */
    SELECT_POST_GOODS,
    /**
     * 返回簡歷我的狀態值
     */
    MY_STATUS ,
    /**
     * 返回簡歷期望職位值
     */
    EXPECT_POSITION,
    /**
     * 添加邮箱
     */
    ADD_EMAIL,
    /**
     * 添加編輯工作
     */
    WORK_EXP,
    EDUCATE_EXP ,
    EDIT_SKILL,
    CERTIFICATION,
    WINNINGEXPERIENCE;

}
