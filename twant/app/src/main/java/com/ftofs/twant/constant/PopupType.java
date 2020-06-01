package com.ftofs.twant.constant;

/**
 * Popup彈窗類型
 * @author zwm
 */
public enum PopupType {
    /**
     * 默認類型
     */
    DEFAULT,
    /**
     * 婚姻
     */
    MARRY,
    /**
     * 選擇手機區號
     */
    MOBILE_ZONE,

    /**
     * 選擇地區
     */
    AREA,

    /**
     * 選擇城友生日
     */
    BIRTH_DAY,
    
    /**
     * 選擇城友地址
     */
    MEMBER_ADDRESS,

    /**
     * 發貨時間
     */
    SHIPPING_TIME,

    /**
     * 選擇支付方式
     */
    PAY_WAY,
    
    /**
     * IM聊天選擇發送產品
     */
    IM_CHAT_SEND_GOODS,

    /**
     * IM聊天選擇發送訂單
     */
    IM_CHAT_SEND_ORDER,

    /**
     * IM聊天常用語
     */
    IM_CHAT_COMMON_USED_SPEECH,

    /**
     * 篩選想要帖
     */
    POST_FILTER,

    /**
     * 選擇優惠券
     */
    SELECT_VOUCHER,

    /**
     * 選擇平台券
     */
    SELECT_PLATFORM_COUPON,

    /**
     * 搜索商店排序類型: 綜合、關注量、開店時長
     */
    STORE_SORT_TYPE,

    /**
     * 搜索商店所在地過濾
     */
    STORE_FILTER_LOCATION,

    /**
     * 搜索商店商圈過濾
     */
    STORE_FILTER_BIZ_CIRCLE,

    /**
     * 選擇個人專頁背景
     */
    SELECT_PERSONAL_BACKGROUND,

    /**
     * 選擇想要帖類型
     */
    SELECT_POST_CATEGORY,
    /**
     * 職位類型選擇：全職、兼職...
     */
    POSITION_CATEGORY,
    /**
     * 選擇期望薪資
     */
    SELECT_SALARY,
    /**
     * 選擇職位關鍵詞
     */
    SELECT_POST_KEYWORD,
    /**
     * 選擇學歷
     */
    ACEDEMIC_TYPE,
    /**
     * 選擇工作時長
     */
    WORK_TIME_TYPE,
    /**
     *選擇當前工作狀態
     */
    MY_STATUS_TYPE,
    /**
    * 選擇月份
    * */
    SELECT_END_MONTH,
/*
切換環境
 */
    Env_TYPE,
    SELECT_START_MONTH,

    /**
     * 選擇退款方式
     */
    SELECT_REFUND_WAY,

    /**
     * 選擇物流公司
     */
    SELECT_LOGISTICS_COMPANY,

    /**
     * 選擇跨境購的購物車拆單
     */
    SELECT_SPLIT_CROSS_BORDER,

    /**
     * 瀏覽SKU圖片
     */
    SELECT_SKU_IMAGE,

    /**
     * 【賣家】選擇物流方式
     */
    SELECT_SELLER_LOGISTICS_WAY,


    /**
     * 【賣家】選擇物流公司
     */
    SELECT_SELLER_LOGISTICS_COMPANY,

    /**
     * 【賣家】選擇訂單類型
     */
    SELECT_ORDER_TYPE,

    /**
     * 【賣家】選擇訂單類型
     */
    SELECT_ORDER_SOURCE,

    /**
     * 選擇開始日期
     */
    SELECT_BEGIN_DATE,

    /**
     * 選擇結束日期
     */
    SELECT_END_DATE,
    /**
     * 選擇店内分類
     */
    STORE_LABEL ,

    /**
     * 【賣家】選擇規格
     */
    SELLER_SELECT_SPEC,
}
