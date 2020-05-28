package com.ftofs.twant.constant;

public class UmengAnalyticsActionName {
    // 登錄界面的登錄按鈕login，微信登錄wechat_login，Facebook登錄Facebook_Login，註冊按鈕register，註冊成功按鈕register_success
    public static final String LOGIN = "login";
    public static final String WECHAT_LOGIN = "wechat_login";
    public static final String FACEBOOK_LOGIN = "Facebook_Login";
    public static final String REGISTER = "register";
    public static final String REGISTER_SUCCESS = "register_success";

    // 加入購物車goods_addcart（代入commonId），彈出框的想要購買goods_buy （代入commonId），產品詳情界面goods （代入commonId）
    public static final String GOODS_ADD_TO_CART = "goods_addcart";
    public static final String GOODS_BUY = "goods_buy";
    public static final String GOODS = "goods";

    // 店鋪界面store （代入storeId）
    public static final String STORE = "store";

    // 收到支付成功回調（pay_success）
    public static final String PAY_SUCCESS = "pay_success";

    // 跳轉到產品activity_goods（commonId），店鋪activity_store（storeId）， 購物專場activity_zone（代入zoneId）
    public static final String ACTIVITY_GOODS = "activity_goods";
    public static final String ACTIVITY_STORE = "activity_store";
    public static final String ACTIVITY_ZONE = "activity_zone";
}
