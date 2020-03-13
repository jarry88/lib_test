package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

public class GoodsPair implements MultiItemEntity {
    public int itemType;
    public Goods leftGoods;
    public Goods rightGoods;

    public GoodsPair() {
        itemType = Constant.ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
