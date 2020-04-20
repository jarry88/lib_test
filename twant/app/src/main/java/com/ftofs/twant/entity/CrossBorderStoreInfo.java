package com.ftofs.twant.entity;

import cn.snailpad.easyjson.EasyJSONArray;

public class CrossBorderStoreInfo {
    public CrossBorderStoreInfo(int storeId, String storeName, boolean isCrossBorder) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.isCrossBorder = isCrossBorder;
        buyData = EasyJSONArray.generate();
    }

    public int storeId;
    public String storeName;
    public boolean isCrossBorder;
    public int productCount;
    public EasyJSONArray buyData;
}
