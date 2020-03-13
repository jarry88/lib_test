package com.ftofs.twant.domain.promotion;

import java.io.Serializable;

public class Gift implements Serializable {
    /**
     * 赠品编号
     */
    private int giftId;

    /**
     * 產品SKU
     */
    private int goodsId;

    /**
     * 產品SPU
     */
    private int commonId;

    /**
     * 赠品数量
     */
    private int giftNum;

    /**
     * 赠品类型，1满优惠赠品、2產品赠品
     */
    private int giftType;

    /**
     * 项目编号，如满优惠编号、主產品编号
     */
    private int itemId;

    /**
     * 项目编号，记录產品赠品的主產品SPU
     */
    private Integer itemCommonId;

    /**
     * 店铺编号
     */
    private Integer storeId;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Integer getItemCommonId() {
        return itemCommonId;
    }

    public void setItemCommonId(Integer itemCommonId) {
        this.itemCommonId = itemCommonId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "giftId=" + giftId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", giftNum=" + giftNum +
                ", giftType=" + giftType +
                ", itemId=" + itemId +
                ", itemCommonId=" + itemCommonId +
                ", storeId=" + storeId +
                '}';
    }
}
