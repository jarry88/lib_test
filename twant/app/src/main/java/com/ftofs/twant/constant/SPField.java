package com.ftofs.twant.constant;

/**
 * Shared Preference Field
 * Shared Preference存儲的字段名稱定義
 * @author zwm
 */
public class SPField {
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_TOKEN = "token";
    public static final String FIELD_NICKNAME = "nickname";
    public static final String FIELD_LAST_LOGIN_TIME = "last_login_time";
    public static final String FIELD_MOBILE_ENCRYPT = "mobile_encrypt";
    public static final String FIELD_SEARCH_TYPE = "search_type_";
    public static final String FIELD_MEMBER_NAME = "member_name";
    public static final String FIELD_IM_TOKEN = "im_token";
    public static final String FIELD_AVATAR = "avatar";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_MEMBER_SIGNATURE = "member_signature";
    public static final String FIELD_MEMBER_BIO = "member_bio";
    public static final String FIELD_AMAP_LOCATION = "amap_location";
    /**
     * 最近一次顯示App引導頁的日期
     * App引導頁每天顯示一次
     */
    public static final String FIELD_SHOW_APP_GUIDE_DATE = "show_app_guide_date";


    /**
     * 升級對話框是否在顯示，即最近一次顯示的時間戳
     */
    public static final String FIELD_APP_UPDATE_POPUP_SHOWN_TIMESTAMP = "app_update_popup_shown_timestamp";

    /**
     * 升級對話框最近一次顯示的日期
     */
    public static final String FIELD_APP_UPDATE_POPUP_SHOWN_DATE = "app_update_popup_shown_date";

    /**
     * 雙十一活動對話框最近一次顯示的時間戳
     */
    public static final String FIELD_DOUBLE_ELEVEN_POPUP_SHOWN_TIMESTAMP = "double_eleven_popup_shown_timestamp";


    /**
     * MPay 訂單的PayId
     */
    public static final String FIELD_MPAY_PAY_ID = "mpay_pay_id_%d";

    /**
     * 微信支付訂單的PayId
     */
    public static final String FIELD_WX_PAY_ID = "wx_pay_id_%d";

    /**
     * 支付寶支付訂單的PayId
     */
    public static final String FIELD_ALI_PAY_ID = "ali_pay_id_%d";
}
