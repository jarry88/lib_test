package com.ftofs.twant.entity;

/**
 * 店鋪商品對
 * @author zwm
 */
public class StoreGoodsPair {
    /**
     * 最新產品
     */
    public static final int TYPE_NEW = 1;
    /**
     * 熱賣產品
     */
    public static final int TYPE_HOT = 2;

    /**
     * 類型
     * 是熱賣產品還是最新產品
     */
    public int type;
    public StoreGoodsItem leftItem;
    public StoreGoodsItem rightItem;

    public StoreGoodsPair(int type) {
        this.type = type;
    }
}
