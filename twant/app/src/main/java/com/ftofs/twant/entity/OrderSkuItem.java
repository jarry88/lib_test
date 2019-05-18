package com.ftofs.twant.entity;

/**
 * 訂單Sku項目
 * @author zwm
 */
public class OrderSkuItem {
    public OrderSkuItem(String goodsName, String imageSrc, float goodsPrice, String goodsFullSpecs, int buyNum) {
        this.goodsName = goodsName;
        this.imageSrc = imageSrc;
        this.goodsPrice = goodsPrice;
        this.goodsFullSpecs = goodsFullSpecs;
        this.buyNum = buyNum;
    }

    public String goodsName;
    public String imageSrc;
    public float goodsPrice;
    public String goodsFullSpecs;
    public int buyNum;
}
