package com.ftofs.twant.constant;

import com.ftofs.twant.config.Config;

public class Uri {
    /**
     * API負載域名
     */
    public final static String API_DOMAIN = Config.API_BASE_URL.concat("/api");

    /**
     * WEB負載域名
     */
    public final static String WEB_DOMAIN = Config.API_BASE_URL.concat("/web");
    /**
     * 購物車
     */
    public final static String API_SHOPPING_CART_LIST = API_DOMAIN.concat("/cart/list");

    /**
     * ICBC支付
     */
    public final static String API_ICBC_PAY = WEB_DOMAIN.concat("/buy/pay/icbc/ios");
}
