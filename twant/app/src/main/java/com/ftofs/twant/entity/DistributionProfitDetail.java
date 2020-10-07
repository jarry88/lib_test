package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DistributionProfitDetail implements MultiItemEntity {
    public int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public DistributionProfitDetail(int itemType) {
        this.itemType = itemType;
    }
}
