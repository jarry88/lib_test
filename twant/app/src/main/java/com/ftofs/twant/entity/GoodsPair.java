package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.constant.Constant;

public class GoodsPair implements MultiItemEntity {
    public int itemType;
    public Goods leftGoods;
    public Goods rightGoods;
    private String itemTitle;

    public GoodsPair() {
        itemType = Constant.ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getItemTitle() {
        return itemTitle;
    }
    public void setItemTitle(String title) {
        this.itemTitle=title;
    }
}
