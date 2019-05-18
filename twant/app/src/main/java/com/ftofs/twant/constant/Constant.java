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
     * 添加收貨地址
     */
    public static final int REQUEST_CODE_ADD_ADDRESS = 1;
    /**
     * 切換收貨地址
     */
    public static final int REQUEST_CODE_CHANGE_ADDRESS = 1;
}
