package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.List;

/**
 * 確認訂單店鋪項數據
 */
public class ConfirmOrderStoreItem implements MultiItemEntity {


    public ConfirmOrderStoreItem(int storeId, String storeName, float buyItemAmount, float freightAmount,
                                 int itemCount, int voucherCount, List<ConfirmOrderSkuItem> confirmOrderSkuItemList) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.buyItemAmount = buyItemAmount;
        this.freightAmount = freightAmount;
        this.itemCount = itemCount;
        this.voucherCount = voucherCount;
        this.confirmOrderSkuItemList = confirmOrderSkuItemList;
    }


    public int storeId;
    public String storeName;
    public float buyItemAmount;  // 金額
    public float freightAmount; // 運費
    public float discountAmount;  // 店鋪優惠
    public int itemCount;    // 店鋪訂單的商品件數: 如果sku1有2件，sku2有3件，那么件數就是5
    public String leaveMessage;  // 留言
    public int voucherCount;  // 店鋪券數量
    public int voucherId;  // 當前正在使用的店鋪券Id（0表示不使用）
    public String voucherName; // 當前正在使用的店鋪券名稱
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;  // 訂單的Sku列表
    // public Object payload; // 仿照RecyclerView的payload，用于局部刷新優化

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_COMMON;
    }
}
