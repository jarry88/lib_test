package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.List;

public class ConfirmOrderStoreItem implements MultiItemEntity {


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
    public String leaveMessage;  // 留言
    public Receipt receipt; // 發票信息
    public int shipTimeType; // 配送時間
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_COMMON;
    }
}
