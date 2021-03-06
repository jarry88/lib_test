package com.ftofs.twant.constant;


import org.jetbrains.annotations.NotNull;

import kotlin.Pair;

/**
 * 常量定義
 * @author zwm
 */
public class Constant {
    public static final String CLIENT_TYPE_ANDROID = "android";

    /**
     * 【镇店之宝】无限循环的最大值
     */
    public static final int INFINITE_LOOP_VALUE = 2999999;
    public static final String TOS_URL = "https://www.twant.com/web/register/agreement";

    /**
     * 分類相關的常量定義
     */
    // 商店分類
    public static final int CATEGORY_TYPE_SHOP = 1;
    // 產品分類
    public static final int CATEGORY_TYPE_COMMODITY = 2;
    // 品牌分類
    public static final int CATEGORY_TYPE_BRAND = 3;
    /**
     * 活動專場狀態
     */
    // 商店分類
    public static final int ZONE_OPEN_TYPE = 1;
    // 產品分類
    public static final int ZONE_CLOSE_TYPE = 0;
    // 品牌分類
    public static final int ZONE_STOP_TYPE = 2;



    /**
     * 在線支付
     */
    public static final int PAY_WAY_ONLINE = 0;
    /**
     * 貨到付款
     */
    public static final int PAY_WAY_DELIVERY = 1;
    /**
     * 門店自提
     */
    public static final int PAY_WAY_FETCH = 2;



    /**
     * 訂單狀態
     */
    public static final int ORDER_STATUS_ALL = 0;  // 全部
    public static final int ORDER_STATUS_TO_BE_PAID = 1;  // 待付款
    public static final int ORDER_STATUS_TO_BE_SHIPPED = 2;  // 待發貨
    public static final int ORDER_STATUS_TO_BE_RECEIVED = 3; // 待收貨
    public static final int ORDER_STATUS_TO_BE_COMMENTED = 4; // 待評價
    public static final int ORDER_STATUS_CANCELLED = 5;  // 已取消


    /**
     * 服務器端的訂單狀態的定義
     */
    public static final int SERVER_ORDERS_STATE_UNINITIALIZED = -1; // 未初始化的，表示未取到服務器端的值
    public static final int SERVER_ORDERS_STATE_TO_BE_PAID = 10; // 待付款



    /**
     * 正常的Item
     */
    public static final int ITEM_TYPE_NORMAL = 1;
    /**
     * 數據全部加載完成的提示
     */
    public static final int ITEM_TYPE_LOAD_END_HINT = 2;
    /**
     * Banner
     */
    public static final int ITEM_TYPE_BANNER = 3;
    /**
     * 標題Item
     */
    public static final int ITEM_TYPE_TITLE =4;
    /**
     * 沒有數據的Item
     */
    public static final int ITEM_TYPE_NO_DATA = 5;
    /**
     * 數據全部加載完成的提示
     */
    public static final int ITEM_TYPE_NO_STORE_DATA = 6;

    /**
     * Header類型
     */
    public static final int ITEM_TYPE_HEADER = 7;

    /**
     * Footer類型
     */
    public static final int ITEM_TYPE_FOOTER = 8;

    /**
     * 樓層類型
     */
    public static final int ITEM_TYPE_FLOOR = 9;

    /**
     * 優選好店類型
     */
    public static final int ITEM_TYPE_BEST_STORE = 10;

    /**
     * 為你推薦標題欄
     */
    public static final int ITEM_TYPE_RECOMMEND_TITLE = 11;

    /**
     * 團購
     */
    public static final int ITEM_TYPE_GROUP = 12;

    /**
     * 砍價
     */
    public static final int ITEM_TYPE_BARGAIN = 13;

    /**
     * 秒殺
     */
    public static final int ITEM_TYPE_SEC_KILL = 14;

    /**
     * 文本類型的跑馬燈
     */
    public static final int ITEM_TYPE_MARQUEE_TEXT = 15;

    /**
     * SLOGAN類型的跑馬燈
     */
    public static final int ITEM_TYPE_MARQUEE_SLOGAN = 16;


    /**
     * 數字定義
     */
    public final static Integer ZERO = 0;
    public final static Integer ONE = 1;
    public final static Integer TWO = 2;

    /**
     * 增刪查改 -- 動作定義
     */
    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;

    /**
     * 直接購買還是添加到購物袋
     */
    public static final int ACTION_ADD_TO_CART = 1;
    public static final int ACTION_BUY = 2;
    public static final int ACTION_SELECT_SPEC = 3; // 選擇規格

    /**
     * 想要帖類型
     * 1 -- 通用 2 -- 求購
     */
    public static final int POST_TYPE_COMMON = 1;
    public static final int POST_TYPE_WANTED = 2;


    /**
     * 地區ID定義
     */
    public static final int AREA_ID_UNKNOWN = 0;
    public static final int AREA_ID_HONGKONG = 1;
    public static final int AREA_ID_MAINLAND = 2;
    public static final int AREA_ID_MACAO = 3;

    /**
     * 配送方式定義
     */
    public static final String PAYMENT_TYPE_CODE_ONLINE = "online";  // 在線支付
    public static final String PAYMENT_TYPE_CODE_OFFLINE = "offline";  // 貨到付款
    public static final String PAYMENT_TYPE_CODE_CHAIN = "chain"; // 門店自提


    /**
     * 消息分類定義
     */
    public static final int MESSAGE_CATEGORY_LOGISTICS = 1001;
    public static final int MESSAGE_CATEGORY_REFUND = 1002;


    /**
     * AreaCode定義
     */
    public static final String AREA_CODE_HONGKONG = "00852";
    public static final String AREA_CODE_MAINLAND = "0086";
    public static final String AREA_CODE_MACAO = "00853";

    /**
     * 商店距離閾值
     */
    public static final float STORE_DISTANCE_THRESHOLD = 0.0000001f;


    /**
     * 確認訂單常規項目
     */
    public static final int ITEM_VIEW_TYPE_COMMON = 1;
    /**
     * 確認訂單匯總項目
     */
    public static final int ITEM_VIEW_TYPE_SUMMARY = 2;

    /**
     * 查看模式
     */
    public static final int MODE_VIEW = 0;
    /**
     * 編輯模式
     */
    public static final int MODE_EDIT = 1;


    /**
     * Android 鍵盤狀態
     */
    public static final int KEYBOARD_HIDDEN = 0;
    public static final int KEYBOARD_SHOWN = 1;


    /**
     * 選中狀態
     */
    public static final int STATUS_SELECTED = 1;

    /**
     * 未選中狀態
     */
    public static final int STATUS_UNSELECTED = 2;


    public static final int COMMENT_CHANNEL_STORE = 2;
    public static final int COMMENT_CHANNEL_GOODS = 3;
    public static final int COMMENT_CHANNEL_POST = 4;

    public static final int COMMENT_TYPE_ALL = 1;
    public static final int COMMENT_TYPE_VIDEO = 2;
    public static final int COMMENT_TYPE_TEXT = 3;


    public static final int ACTION_REFUND = 1;
    public static final int ACTION_RETURN = 2;
    public static final int ACTION_COMPLAIN = 3;
    public static final int ACTION_REFUND_ALL = 4;  // 全部退款

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    /*
     * IM消息類型
     */
    public static final int CHAT_MESSAGE_TYPE_UNKNOWN = 0;
    public static final int CHAT_MESSAGE_TYPE_TXT = 1;
    public static final int CHAT_MESSAGE_TYPE_GOODS = 2;
    public static final int CHAT_MESSAGE_TYPE_ORDER = 3;
    public static final int CHAT_MESSAGE_TYPE_IMAGE = 4;
    public static final int CHAT_MESSAGE_TYPE_ENC = 5; // 電子名片

    // 摄像头动作类型
    public static final int CAMERA_ACTION_IMAGE = 1;  // 用Camera拍照片
    public static final int CAMERA_ACTION_VIDEO = 2;  // 用Camera拍视频


    /**
     * 用戶界面重用 -- 用戶注冊
     */
    public static final int USAGE_USER_REGISTER = 1;
    /**
     * 用戶界面重用 -- 重置密碼
     */
    public static final int USAGE_RESET_PASSWORD = 2;
    /**
     * 用戶界面重用 -- 設置支付密碼
     */
    public static final int USAGE_SET_PAYMENT_PASSWORD = 3;




    /**
     * 想付錢包狀態 -- 未知
     */
    public static final int WANT_PAY_WALLET_STATUS_UNKNOWN = 0;

    /**
     * 想付錢包狀態 -- 已激活
     */
    public static final int WANT_PAY_WALLET_STATUS_ACTIVATED = 1;

    /**
     * 想付錢包狀態 -- 未激活
     */
    public static final int WANT_PAY_WALLET_STATUS_NOT_ACTIVATED = 2;

    /**
     * 卡券類型 -- 商店券
     */
    public static final int COUPON_TYPE_STORE = 1;
    /**
     * 卡券類型 -- 平台券
     */
    public static final int COUPON_TYPE_PLATFORM = 2;


    /*
    消息模板分类
    1001-交易消息
    1002-資產消息
    1003-社交消息
    1004-促銷消息
    1005-通知消息
     */
    public static final int TPL_CLASS_TRANSACT = 1001;
    public static final int TPL_CLASS_ASSET = 1002;
    public static final int TPL_CLASS_SOCIAL = 1003;
    public static final int TPL_CLASS_BARGAIN = 1004;
    public static final int TPL_CLASS_NOTICE = 1005;


    public static final String UMENG_ALIAS_TYPE = "twant";


    // 動畫的幾種狀態
    public static final int ANIM_NOT_SHOWN = 0;
    public static final int ANIM_SHOWING = 1;
    public static final int ANIM_SHOWN = 2;

    // 各種渠道
    public static final String FLAVOR_GOOGLE = "google";
    public static final String FLAVOR_OFFICIAL = "official";
    public static final String FLAVOR_TENCENT = "tencent";
    public static final String FLAVOR_HUAWEI = "huawei";
    public static final String FLAVOR_XIAOMI = "xiaomi";


    /**
     * 微信授權的用途
     */
    public static final int WEIXIN_AUTH_USAGE_LOGIN = 1;  // 用于登錄
    public static final int WEIXIN_AUTH_USAGE_UNBIND = 2; // 用于解綁
    public static final int WEIXIN_AUTH_USAGE_BIND = 3; // 用于綁定


    /**
     * 優惠券狀態
     */
    public static final int COUPON_STATE_UNRECEIVED = 1;  // 未領取
    public static final int COUPON_STATE_RECEIVED = 2;   // 已領取
    public static final int COUPON_STATE_RUN_OUT_OF = 3; // 已搶完
    public static final int COUPON_STATE_USED = 4;       // 已使用
    public static final int COUPON_STATE_OUT_OF_DATE = 5; // 已過期
    public static final int COUPON_STATE_DISCARDED = 6;  // 已作廢


    /**
     * INT類型的true | false 定義
     */
    public static final int TRUE_INT = 1;
    public static final int FALSE_INT = 0;
    public static final String MSG_NOTIFY_HXID ="hId" ;

    // public static final int SKU_SELECTED_FONT_SIZE = 14;
    // public static final int SKU_UNSELECTED_FONT_SIZE = 14;

    public static final int DISTRICT_ID_MACAO = 45056;
    public static final int DISTRICT_ID_HONGKONG = 45064;

    /**
     * 商品狀態
     */
    public static final int GOODS_STATUS_OFF_SHELF = 0;  // 下架
    public static final int GOODS_STATUS_ON_SHELF = 1;   // 在售(即上架)
    public static final int RC_CROP_IMAGE = 102;
    public static final int GOODS_TYPE_CONSULT = 5; // 諮詢類商品
    public static final int GOODS_TYPE_CROSS_BORDER = 6; // 購城購商品
    public static final int CONSULT_STORE = 1;
    public static final int NORMAL_STORE = 2;
    public static int SELLER_REFUND=1;//退款
    public static int SELLER_RETURN=2;//退貨


    public static final String WORD_COUPON_TYPE_STORE = "voucher";   // 店鋪口令優惠券
    public static final String WORD_COUPON_TYPE_PLATFORM = "coupon";  // 平臺口令優惠券

    public static final int COLOR_SPEC_ID = 1;  // 【颜色】的规格组Id


    public static final double DOUBLE_ZERO_THRESHOLD = 0.000001d;


    /*
    "promotionType": 0,  #0無促銷活動 1限時折扣 2全款預售 3定金預售 4多人拼團 5優惠套裝 6秒殺活動 7砍價活動
     */
    public static final int PROMOTION_TYPE_NONE = 0;
    public static final int PROMOTION_TYPE_TIME_LIMITED_DISCOUNT = 1;
    public static final int PROMOTION_TYPE_GROUP = 4;
    public static final int PROMOTION_TYPE_SEC_KILL = 6;
    public static final int PROMOTION_TYPE_BARGAIN = 7;


    // 廣告彈窗彈出策略   0 -- 当前APP版本只彈一次 1 -- 每次訪問彈出 2 -- 每天首次訪問彈出
    public static final int ENABLE_EVERY_TIME_APP_POPUP_AD_VERSION_ONCE = 0;
    public static final int ENABLE_EVERY_TIME_APP_POPUP_AD_ALWAYS = 1;
    public static final int ENABLE_EVERY_TIME_APP_POPUP_AD_DAILY_ONCE = 2;

    // 無效的開團Id
    public static final int INVALID_GO_ID = -1;
    // 無效的砍價Id
    public static final int INVALID_BARGAIN_ID = -1;
    // 無效的砍價OpenId
    public static final int INVALID_BARGAIN_OPEN_ID = -1;

    public static final String BARGAIN_INSTRUCTION_URL = "/mobile/bargain/introduction";
    /*
    砍价状态（0未开始，1已开始，2已结束，3已关闭）
     */
    public static final int BARGAIN_STATE_NOT_STARTED = 0;
    public static final int BARGAIN_STATE_ONGOING = 1;
    public static final int BARGAIN_STATE_FINISHED = 2;
    public static final int BARGAIN_STATE_CLOSED = 3;

    /**
     * 分銷狀態
     * -1 未申請 0申請中 1審核通過 2審核不通過 3清退
     */
    public static final int MARKETING_STATE_NOT_APPLY = -1;
    public static final int MARKETING_STATE_APPLY_IN_PROGRESS = 0;
    public static final int MARKETING_STATE_APPLY_PASS = 1;
    public static final int MARKETING_STATE_APPLY_NOT_PASS = 2;
    public static final int MARKETING_STATE_APPLY_REMOVED = 3;

    /**
     * 分銷佣金類型
     */
    public static final int COMMISSION_TYPE_RATIO = 1; // 按比例
    public static final int COMMISSION_TYPE_FIXED = 2; // 固定金額

    /**
     * 海報類型
     */
    public static final int POSTER_TYPE_GOODS = 1; // 商品海報
    public static final int POSTER_TYPE_INVITATION = 2; // 邀請海報


    public static int[] WRITE_OFFS_ID=new int[]{
        1575, 959,1598
    };
    public static final String GO853_HOLD_PLACE ="go853_no_data";

    public static final int MOON_ZONE_ID=69;
    @NotNull
    public static final Pair<String, String> CLIENT_TYPE_PAIR=new Pair("clientType",CLIENT_TYPE_ANDROID);
}

