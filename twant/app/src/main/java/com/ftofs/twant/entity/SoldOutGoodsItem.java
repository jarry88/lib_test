package com.ftofs.twant.entity;

/**
 * 售罄商品项
 * @author zwm
 */
public class SoldOutGoodsItem {
    public static final int REASON_SOLD_OUT = 1; // 售罄
    public static final int REASON_NOT_AVAILABLE = 2; // 该地区不支持配送

    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public int buyNum;
    public int reason;
    public String reasonDesc;

    public SoldOutGoodsItem() {
    }

    public SoldOutGoodsItem(int goodsId, String goodsImage, String goodsName, int buyNum, int reason, String reasonDesc) {
        this.goodsId = goodsId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.buyNum = buyNum;
        this.reason = reason;
        this.reasonDesc = reasonDesc;
    }
}
