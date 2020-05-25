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
    /**
     * 區號+手機，用逗號隔開，例如：0086,13425038750
     */
    public static final String FIELD_MOBILE = "mobile";
    public static final String FIELD_MOBILE_ENCRYPT = "mobile_encrypt";
    public static final String FIELD_SEARCH_TYPE = "search_type_new_";
    public static final String FIELD_MEMBER_NAME = "member_name";
    public static final String FIELD_IM_TOKEN = "im_token";
    public static final String FIELD_IM_TOKEN_EXPIRE = "im_token_expire";  // imToken的過期時間
    public static final String FIELD_AVATAR = "avatar";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_MEMBER_SIGNATURE = "member_signature";
    public static final String FIELD_MEMBER_BIO = "member_bio";
    public static final String FIELD_AMAP_LOCATION = "amap_location";

    /**
     * 未讀消息數
     * 內容結構如下:
     *{
     * 	"transact": 1,
     * 	"asset": 2,
     * 	"social": 3,
     * 	"bargain": 4,
     * 	"notice": 5
     * }
     */
    public static final String FIELD_UNREAD_MESSAGE_COUNT = "unread_message_count";

    /**
     * 最近一次顯示App引導頁的日期
     * App引導頁每天顯示一次
     */
    public static final String FIELD_SHOW_APP_GUIDE_DATE = "show_app_guide_date";

    /**
     * 最近一次顯示App引導頁的版本號
     * App引導頁每次更新顯示一次
     */
    public static final String FIELD_SHOW_APP_GUIDE_VERSION = "show_app_guide_version";


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

    /**
     * 支付寶香港支付訂單的PayId
     */
    public static final String FIELD_ALI_PAY_HK_ID = "ali_pay_hk_id_%d";

    /**
     * 下載App安裝包
     */
    public static final String FIELD_DOWNLOAD_APP = "download_app";

    /**
     * 個人專頁背景圖的路徑
     */
    public static final String FIELD_PERSONAL_BACKGROUND = "personal_background";

    /**
     * 登錄類型
     */
    public static final String FIELD_LOGIN_TYPE = "login_type";

    /**
     * 微信賬戶綁定狀態(第三方登錄時是否有綁定手機號)
     */
    public static final String FIELD_WX_BINDING_STATUS = "binding_status";

    /**
     * Facebook賬戶綁定狀態(第三方登錄時是否有綁定手機號)
     */
    public static final String FIELD_FB_BINDING_STATUS = "fb_binding_status";

    /**
     * 軟鍵盤的高度
     */
    public static final String FIELD_SOFT_INPUT_HEIGHT = "soft_input_height";

    /**
     * 網頁拉起APP的參數
     */
    public static final String FIELD_LAUNCH_APP_PARAMS = "launch_app_params";
    /**
     * 當前環境
     */
    public static final String FIELD_CURRENT_ENV="config_current_env";
    public static final String FIELD_MEMBER_TOKEN="member_token";

    public static final String FIELD_TOTAL_ORDER_AMOUNT = "total_order_amount";

    // 服务器配置的版本号，主要用于应用商店审核
    // public static final String FIELD_CURR_VERSION_SHOW_ACTIVITY_INDEX = "curr_version_show_activity_index";

    /**
     * 活动弹窗状态(当前版本的APP才弹出）
     */
    public static final String FIELD_POPUP_AD_STATUS_APP_VER = "popup_ad_status_app_ver_%s";

    /**
     * 退款方式記錄，每用戶一條，用於記錄用戶最近一次的退款方式(%d用於打印用戶Id)
     */
    public static final String FIELD_USER_REFUND_WAY = "user_refund_way_%d";

    /**
     * 用戶數據，在退出登錄前需要清空
     */
    public static final String FIELD_USER_DATA = "user_data";

    public static final String USER_DATA_KEY_WECHAT_ID = "wechat_id"; // 微信帳號
    public static final String USER_DATA_KEY_FACEBOOK_ID = "facebook_id"; // Facebook帳號
    public static final String USER_DATA_KEY_EMAIL = "email"; // 郵箱
    public static final String USER_DATA_KEY_BIO = "bio"; // 個人簡介
    public static final String USER_DATA_KEY_SIGNATURE = "signature";  // 個性簽名


    public static final String USER_RECEIVE_NEWS = "receive_news";
    public static final String USER_RECEIVE_NEWS_DETAIL = "receive_news_details";
    public static final String MAINACTIVITY_RESUME ="activity_on_front" ;
}
