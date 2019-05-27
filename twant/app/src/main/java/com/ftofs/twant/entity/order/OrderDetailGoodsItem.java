package com.ftofs.twant.entity.order;

/**
 * 訂單詳情的商品列表Item
 * @author zwm
 */
public class OrderDetailGoodsItem {
    public OrderDetailGoodsItem(int goodsId, String imageSrc, String goodsName, float goodsPrice, int buyNum, String goodsFullSpecs) {
        this.goodsId = goodsId;
        this.imageSrc = imageSrc;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.buyNum = buyNum;
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public int goodsId;
    public String imageSrc;
    public String goodsName;
    public float goodsPrice;
    public int buyNum;
    public String goodsFullSpecs;
}
