package com.ftofs.twant.constant;

public enum EBMessageType {
    /**
     * 添加購物袋完成的消息
     */
    MESSAGE_TYPE_ADD_CART,
    /**
     * ICBC支付完成消息
     */
    MESSAGE_TYPE_ICBC_PAY_FINISH,
    /**
     * 登錄成功
     */
    MESSAGE_TYPE_LOGIN_SUCCESS,
    /**
     * 登出成功
     */
    MESSAGE_TYPE_LOGOUT_SUCCESS,
    /**
     * 選擇規格消息
     */
    MESSAGE_TYPE_SELECT_SPECS,
    /**
     * 刷新數據
     */
    MESSAGE_TYPE_REFRESH_DATA,

    /**
     * 文件上傳成功
     */
    MESSAGE_TYPE_UPLOAD_FILE_SUCCESS,

    /**
     * 重新加載數據(訂單詳情)
     */
    MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL,

    /**
     * 重新加載數據(訂單列表)
     */
    MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST,

    /**
     * 收到聊天消息
     */
    MESSAGE_TYPE_NEW_CHAT_MESSAGE,

    /**
     * 更新好友的個人資料完成
     */
    MESSAGE_TYPE_UPDATE_FRIEND_INFO,

    /**
     * 顯示某個Fragment
     */
    MESSAGE_TYPE_SHOW_FRAGMENT,

    /**
     * (通知MainActivity)顯示Toast
     */
    MESSAGE_TYPE_SHOW_TOAST,

    /**
     * 鍵盤狀態變化
     */
    MESSAGE_TYPE_KEYBOARD_STATE_CHANGED,

    /**
     * 添加新想要帖
     */
    MESSAGE_TYPE_ADD_POST,

    /**
     * 打開應用時，顯示初始化未讀消息條數和購物袋項數等工具欄數據
     */
    MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE,

    /**
     * 重新加載產品詳情
     */
    MESSAGE_TYPE_RELOAD_GOODS_DETAIL,

    /**
     * 想付錢包支付成功
     */
    MESSAGE_TYPE_WALLET_PAY_SUCCESS,

    /**
     * 修改城友頭像
     */
    MESSAGE_TYPE_CHANGE_MEMBER_AVATAR,
	
	/**
     * 修改了個人專頁背景圖
     */
    MESSAGE_TYPE_CHANGE_PERSONAL_BACKGROUND,

    /**
     * 微信登錄
     */
    MESSAGE_TYPE_WEIXIN_LOGIN,

    /**
     * 微信解綁
     */
    MESSAGE_TYPE_WEIXIN_UNBIND,

    /**
     * 綁定微信
     */
    MESSAGE_TYPE_WEIXIN_BIND,

    /**
     * 選擇想要帖產品
     */
    MESSAGE_TYPE_SELECT_POST_GOODS,
    /**
     * 通知欄信息跳轉
     */
    MESSAGE_NOTIFICATION_INTENT,

    /**
     * 【賣家】刷新訂單列表
     */
    MESSAGE_SELLER_RELOAD_ORDER_LIST,
}



