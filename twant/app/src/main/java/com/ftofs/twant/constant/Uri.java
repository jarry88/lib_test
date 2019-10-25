package com.ftofs.twant.constant;

import com.ftofs.twant.config.Config;

public class Uri {
    /**
     * API負載域名
     */
    public final static String API_DOMAIN = Config.API_BASE_URL;

    /**
     * WEB負載域名
     */
    public final static String WEB_DOMAIN = Config.WEB_BASE_URL;
    /**
     * 購物袋
     */
    public final static String API_SHOPPING_CART_LIST = API_DOMAIN.concat("/cart/list");

    /**
     * ICBC支付
     */
    public final static String API_ICBC_PAY = WEB_DOMAIN.concat("/buy/pay/icbc/ios");

    /**
     * ICBC支付查詢
     */
    public final static String API_ICBC_PAY_ENQUIRY = API_DOMAIN.concat("/member/icbc/pay/query");

    /**
     * 退貨列表
     */
    public final static String API_RETURN_LIST = API_DOMAIN.concat("/member/return/list");

    /**
     * 退款列表
     */
    public final static String API_REFUND_LIST = API_DOMAIN.concat("/member/refund/list");
}
