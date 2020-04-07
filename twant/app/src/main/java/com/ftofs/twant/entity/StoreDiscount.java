package com.ftofs.twant.entity;

public class StoreDiscount {
    public StoreDiscount(int storeId, int discountId, String discountName, double discountRate, int goodsCount, int countDownTime) {
        this.storeId = storeId;
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountRate = discountRate;
        this.goodsCount = goodsCount;
        this.countDownTime = countDownTime;
    }

    public int storeId;
    public int discountId;
    public String discountName;
    // 如果discountRate為9.5，表示9.5折
    public double discountRate;
    public int goodsCount;
    public int countDownTime;  // 折扣活動剩余的秒數
}
