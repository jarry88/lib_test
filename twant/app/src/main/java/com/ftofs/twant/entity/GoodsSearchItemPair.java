package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class GoodsSearchItemPair implements MultiItemEntity {
    public int itemViewType;

    public GoodsSearchItem left;
    public GoodsSearchItem right;

    // 最后一項的動畫的顯示狀態
    public int animShowStatus;

    public GoodsSearchItemPair(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    @Override
    public int getItemType() {
        return itemViewType;
    }
}
