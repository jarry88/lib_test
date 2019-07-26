package com.ftofs.twant.constant;

public enum EBMessageType {
    /**
     * 添加購物車完成的消息
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
     * 頭像上傳成功
     */
    MESSAGE_TYPE_UPLOAD_AVATAR_SUCCESS,

    /**
     * 支付成功
     */
    MESSAGE_TYPE_PAY_SUCCESS,

    /**
     * 收到聊天消息
     */
    MESSAGE_TYPE_NEW_CHAT_MESSAGE,

    /**
     * 更新好友的個人資料完成
     */
    MESSAGE_TYPE_UPDATE_FRIEND_INFO,

    /**
     * 重建MainFragment
     */
    MESSAGE_TYPE_RECREATE_MAIN_FRAGMENT,

    /**
     * 刷新訂單列表數據
     */
    MESSAGE_TYPE_REFRESH_ORDER_LIST,
}
