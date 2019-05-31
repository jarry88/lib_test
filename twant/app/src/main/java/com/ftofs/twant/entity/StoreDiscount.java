package com.ftofs.twant.entity;

public class StoreDiscount {
    public StoreDiscount(int storeId, int discountId, float discountRate, int goodsCount) {
        this.storeId = storeId;
        this.discountId = discountId;
        this.discountRate = discountRate;
        this.goodsCount = goodsCount;
    }

    public int storeId;
    public int discountId;
    // 如果discountRate為9.5，表示9.5折
    public float discountRate;
    public int goodsCount;
}
