package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CrossBorderHomeItem implements MultiItemEntity {
    public int itemType;



    @Override
    public int getItemType() {
        return itemType;
    }
}
