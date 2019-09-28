package com.ftofs.twant.entity;

/**
 * 訂單Sku項目
 * @author zwm
 */
public class OrderSkuItem {
    public OrderSkuItem(int commonId, int goodsId, String goodsName, String imageSrc, float goodsPrice, String goodsFullSpecs, int buyNum) {
        this.commonId = commonId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.imageSrc = imageSrc;
        this.goodsPrice = goodsPrice;
        this.goodsFullSpecs = goodsFullSpecs;
        this.buyNum = buyNum;
    }

    public int commonId;
    public int goodsId;
    public String goodsName;
    public String imageSrc;
    public float goodsPrice;
    public String goodsFullSpecs;
    public int buyNum;
}
