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
    public Receipt receipt; // 單據信息
    public int shipTimeType; // 配送時間
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;
    // public Object payload; // 仿照RecyclerView的payload，用于局部刷新優化

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_COMMON;
    }
}
