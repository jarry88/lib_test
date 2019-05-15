package com.ftofs.twant.constant;

import com.ftofs.twant.config.Config;

public class Uri {
    //購物車
    public final static String API_SHOPPING_CART_LIST = Config.API_BASE_URL.concat("/cart/list");
    public final static String API_SHOPPING_CART_ADD = Config.API_BASE_URL.concat("/cart/add");
    public final static String API_SHOPPING_CART_ADD_FROM_APP = Config.API_BASE_URL.concat("/cart/add/from/app");
    public final static String API_SHOPPING_CART_EDIT = Config.API_BASE_URL.concat("/cart/edit");
    public final static String API_SHOPPING_CART_DEL_BATCH_SKU = Config.API_BASE_URL.concat("/cart/del/batch/sku");
    public final static String API_SHOPPING_CART_COUNT = Config.API_BASE_URL.concat("/cart/count");
}
