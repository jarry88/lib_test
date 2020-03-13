package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PaySuccessSummaryItem implements MultiItemEntity {
    public int itemViewType;

    public int couponPrice;
    public boolean isMPayActivity;
    public String mpayActivityDesc;
    public double totalAmount;
    public int storeCouponCount;  // 收到的店铺券数量

    @Override
    public int getItemType() {
        return itemViewType;
    }
}
