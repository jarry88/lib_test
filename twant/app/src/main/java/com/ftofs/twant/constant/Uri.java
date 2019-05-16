package com.ftofs.twant.constant;

import com.ftofs.twant.config.Config;

public class Uri {
    /**
     * API負載域名
     */
    public final static String API_DOMAIN = Config.API_BASE_URL.concat("/api");
    /**
     * 購物車
     */
    public final static String API_SHOPPING_CART_LIST = API_DOMAIN.concat("/cart/list");
}
