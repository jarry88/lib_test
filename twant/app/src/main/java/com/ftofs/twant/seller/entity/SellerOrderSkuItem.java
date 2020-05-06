package com.ftofs.twant.seller.entity;

/**
 * 賣家訂單列表Sku項
 * @author zwm
 */
public class SellerOrderSkuItem {
    public int goodsId;
    public String goodsImage;
    public String goodsName;
    public double goodsPrice;  // 單價
    public int buyNum;    // 購買數量
    public String goodsFullSpecs; // 規格描述

    public boolean isTimeLimitedDiscount;  // 是否限時折扣
    public boolean isFreightFree;  // 是否包郵
    public boolean hasGift;  // 是否有贈品
}
