package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.List;

/**
 * 確認訂單商店項數據
 */
public class ConfirmOrderStoreItem implements MultiItemEntity {


    public int tariffEnable =Constant.FALSE_INT;

    @NonNull
    @Override
    public String toString() {
        return String.format("storeName[%s],buyItemAmount[%s],taxAmount[%s]",storeName,buyItemAmount,taxAmount);
    }

    public ConfirmOrderStoreItem(int storeId, String storeName, float buyItemAmount, double freightAmount,
                                 int itemCount, int voucherCount, List<ConfirmOrderSkuItem> confirmOrderSkuItemList,
                                 float conformTemplatePrice) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.buyItemAmount = buyItemAmount;
        this.freightAmount = freightAmount;
        this.itemCount = itemCount;
        this.voucherCount = voucherCount;
        this.confirmOrderSkuItemList = confirmOrderSkuItemList;
        this.conformTemplatePrice = conformTemplatePrice;
    }

    public ConfirmOrderStoreItem(int storeId, String storeName, float buyItemAmount, float freightAmount,
                                 int itemCount, int voucherCount, List<ConfirmOrderSkuItem> confirmOrderSkuItemList,
                                 float conformTemplatePrice,double taxAmount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.buyItemAmount = buyItemAmount;
        this.freightAmount = freightAmount;
        this.itemCount = itemCount;
        this.voucherCount = voucherCount;
        this.confirmOrderSkuItemList = confirmOrderSkuItemList;
        this.conformTemplatePrice = conformTemplatePrice;
        this.tariffEnable = Constant.TRUE_INT;
        this.taxAmount = taxAmount;
    }


    public int storeId;
    public String storeName;
    public double taxAmount=0;//稅費
    public float buyItemAmount;  // 金額
    public double freightAmount; // 運費
    public float discountAmount;  // 商店優惠
    public int itemCount;    // 商店訂單的產品件數: 如果sku1有2件，sku2有3件，那么件數就是5
    public String leaveMessage;  // 留言
    public int voucherCount;  // 商店券數量
    public int voucherId;  // 當前正在使用的商店券Id（0表示不使用）
    public String voucherName; // 當前正在使用的商店券名稱
    public List<ConfirmOrderSkuItem> confirmOrderSkuItemList;  // 訂單的Sku列表
    // public Object payload; // 仿照RecyclerView的payload，用于局部刷新優化
    public float conformTemplatePrice;  // 店鋪滿減優惠券,大於0才顯示

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_COMMON;
    }
}
