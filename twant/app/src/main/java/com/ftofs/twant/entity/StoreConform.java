package com.ftofs.twant.entity;

/**
 * 店铺满优惠
 * @author zwm
 */
public class StoreConform {
    public StoreConform(int conformId, int limitAmount, int conformPrice, String startTime, String endTime) {
        this.conformId = conformId;
        this.limitAmount = limitAmount;
        this.conformPrice = conformPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int conformId;
    public int limitAmount;
    public int conformPrice;
    public String startTime;
    public String endTime;
}
