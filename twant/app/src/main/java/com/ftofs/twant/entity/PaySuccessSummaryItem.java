package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PaySuccessSummaryItem implements MultiItemEntity {
    public int itemViewType;

    public int couponPrice;
    public boolean isMPayActivity;
    public String mpayActivityDesc;
    public double totalAmount;
    public int storeCouponCount;  // 收到的店铺券数量
    public boolean isCashOnDelivery; // 是否是貨到付款
    public boolean isGroupBuy;

    @Override
    public int getItemType() {
        return itemViewType;
    }
}
