package com.ftofs.twant.entity;

public class StoreAmount {
    /**
     * 商店優惠額
     */
    public double storeDiscountAmount;
    /**
     * 店铺购买金额(不含运费)
     */
    public double storeBuyAmount;

    public StoreAmount(double storeDiscountAmount, double storeBuyAmount) {
        this.storeDiscountAmount = storeDiscountAmount;
        this.storeBuyAmount = storeBuyAmount;
    }
}
