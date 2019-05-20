package com.ftofs.twant.entity;

import java.util.List;

public class ConfirmOrderStoreItem {
    public ConfirmOrderStoreItem(int storeId, String storeName, float buyItemAmount, float freightAmount,
                                 int itemCount, List<ConfirmOrderSkuItem> confirmOrderSkuItemList) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.buyItemAmount = buyItemAmount;
        this.freightAmount = freightAmount;
        this.itemCount = itemCount;
        this.confirmOrderSkuItemList = confirmOrderSkuItemList;
    }

    public int storeId;
    public String storeName;
    public float buyItemAmount;  // 金額
    public float freightAmount; // 運費
    public int itemCount;
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;
}
