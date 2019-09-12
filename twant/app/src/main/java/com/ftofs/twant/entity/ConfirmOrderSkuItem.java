package com.ftofs.twant.entity;

import java.util.List;

/**
 * 確認訂單Sku數據項
 * @author zwm
 */
public class ConfirmOrderSkuItem {
    public ConfirmOrderSkuItem(String goodsImage, int goodsId, String goodsName, String goodsFullSpecs, int buyNum, float skuPrice, List<GiftItem> giftItemList) {
        this.goodsImage = goodsImage;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsFullSpecs = goodsFullSpecs;
        this.buyNum = buyNum;
        this.skuPrice = skuPrice;
        this.giftItemList = giftItemList;
    }

    public String goodsImage;
    public int goodsId;
    public String goodsName;
    public String goodsFullSpecs;
    public int buyNum;
    public float skuPrice;
    public List<GiftItem> giftItemList;
}
