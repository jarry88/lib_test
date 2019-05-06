package com.ftofs.twant.entity;

/**
 * 分類店鋪數據結構
 * @author zwm
 */
public class CategoryShop {
    public CategoryShop(int shopId, String coverUrl, String shopParentName, int shopCount, int commodityCount) {
        this.shopId = shopId;
        this.coverUrl = coverUrl;
        this.shopParentName = shopParentName;
        this.shopCount = shopCount;
        this.commodityCount = commodityCount;
    }

    public int shopId;
    public String coverUrl;
    public String shopParentName;
    public int shopCount;
    public int commodityCount;
}
