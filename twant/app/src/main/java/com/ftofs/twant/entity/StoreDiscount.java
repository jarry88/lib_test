package com.ftofs.twant.entity;

public class StoreDiscount {
    public StoreDiscount(int discountId, float discountRate, int goodsCount) {
        this.discountId = discountId;
        this.discountRate = discountRate;
        this.goodsCount = goodsCount;
    }

    public int discountId;
    // 如果discountRate為9.5，表示9.5折
    public float discountRate;
    public int goodsCount;
}
