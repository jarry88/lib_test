package com.ftofs.twant.entity;

/**
 * 確認訂單Sku數據項
 * @author zwm
 */
public class ConfirmOrderSkuItem {
    public ConfirmOrderSkuItem(String goodsImage, int goodsId, String goodsName, String goodsFullSpecs, int buyNum) {
        this.goodsImage = goodsImage;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsFullSpecs = goodsFullSpecs;
        this.buyNum = buyNum;
    }

    public String goodsImage;
    public int goodsId;
    public String goodsName;
    public String goodsFullSpecs;
    public int buyNum;
}
