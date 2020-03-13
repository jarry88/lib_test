package com.ftofs.twant.entity;

public class StoreAmount {
    /**
     * 商店優惠額
     */
    public float storeDiscountAmount;
    /**
     * 店铺购买金额(不含运费)
     */
    public float storeBuyAmount;

    public StoreAmount(float storeDiscountAmount, float storeBuyAmount) {
        this.storeDiscountAmount = storeDiscountAmount;
        this.storeBuyAmount = storeBuyAmount;
    }
}
