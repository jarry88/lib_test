package com.ftofs.twant.constant;

/**
 * 常量定義
 * @author zwm
 */
public class Constant {
    public static final String CLIENT_TYPE_ANDROID = "android";

    /**
     * 分類相關的常量定義
     */
    // 店鋪分類
    public static final int CATEGORY_TYPE_SHOP = 1;
    // 商品分類
    public static final int CATEGORY_TYPE_COMMODITY = 2;
    // 品牌分類
    public static final int CATEGORY_TYPE_BRAND = 3;



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


    public static final int POPUP_TYPE_DEFAULT = 0;
    public static final int POPUP_TYPE_MOBILE_ZONE = 1;
    public static final int POPUP_TYPE_AREA = 2;


    /**
     * 訂單狀態
     */
    public static final int ORDER_STATUS_ALL = 0;
    public static final int ORDER_STATUS_TO_BE_PAID = 1;
    public static final int ORDER_STATUS_TO_BE_SHIPPED = 2;
    public static final int ORDER_STATUS_TO_BE_RECEIVED = 3;
    public static final int ORDER_STATUS_TO_BE_COMMENTED = 4;


    /**
     * 添加收貨地址
     */
    public static final int REQUEST_CODE_ADD_ADDRESS = 1;
    /**
     * 切換收貨地址
     */

    public static final int REQUEST_CODE_CHANGE_ADDRESS = 2;
    /**
     * 確認訂單
     */
    public static final int REQUEST_CODE_CONFIRM_ORDER = 3;

    /**
     * 選擇收貨地址
     */
    public static final int REQUEST_CODE_SELECT_ADDR = 4;


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
     * 地區ID定義
     */
    public static final int AREA_ID_UNKNOWN = 0;
    public static final int AREA_ID_HONGKONG = 1;
    public static final int AREA_ID_MAINLAND = 2;
    public static final int AREA_ID_MACAO = 3;


    /**
     * AreaCode定義
     */
    public static final String AREA_CODE_HONGKONG = "00852";
    public static final String AREA_CODE_MAINLAND = "0086";
    public static final String AREA_CODE_MACAO = "00853";

    /**
     * 店鋪距離閾值
     */
    public static final float STORE_DISTANCE_THRESHOLD = 0.0000001f;

}
