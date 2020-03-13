package com.ftofs.twant.entity;

import java.util.List;

/**
 * 店铺满优惠
 * @author zwm
 */
public class StoreConform {
    public String contentCartRule;
    private List<GiftItem> giftItemList;

    public StoreConform(int templateId, int storeId, int conformId, int limitAmount, int conformPrice,
                        String startTime, String endTime, int giftCount, int isFreeFreight) {
        this.templateId = templateId;
        this.storeId = storeId;
        this.conformId = conformId;
        this.limitAmount = limitAmount;
        this.conformPrice = conformPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.giftCount = giftCount;
        this.isFreeFreight = isFreeFreight;
    }
    public StoreConform(int templateId, int storeId, int conformId, int limitAmount, int conformPrice,
                        String startTime, String endTime,String contentCartRule, int giftCount, int isFreeFreight) {
        this.templateId = templateId;
        this.storeId = storeId;
        this.conformId = conformId;
        this.limitAmount = limitAmount;
        this.conformPrice = conformPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.giftCount = giftCount;
        this.isFreeFreight = isFreeFreight;
        this.contentCartRule = contentCartRule;
    }

    public int templateId;
    public int storeId;
    public int conformId;
    public int limitAmount;
    public int conformPrice;
    public String startTime;
    public String endTime;
    public int giftCount; // 贈品數量
    public int isFreeFreight;

    public void setGiftList(List<GiftItem> giftItemList) {
        this.giftItemList = giftItemList;
    }
    public List<GiftItem> getGiftList() {
        return giftItemList;
    }
}
