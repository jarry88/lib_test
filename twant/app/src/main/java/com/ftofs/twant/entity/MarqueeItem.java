package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MarqueeItem implements MultiItemEntity {
    public int itemType;

    public String text;

    public MarqueeItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}

