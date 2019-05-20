package com.ftofs.twant.entity;

import java.util.List;

public class ConfirmOrderStoreItem {
    public ConfirmOrderStoreItem(int storeId, String storeName, List<ConfirmOrderSkuItem> confirmOrderSkuItemList) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.confirmOrderSkuItemList = confirmOrderSkuItemList;
    }

    public int storeId;
    public String storeName;
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;
}
