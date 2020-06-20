package com.ftofs.twant.seller.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class SellerSpecValueListItem implements MultiItemEntity {
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_FOOTER = 2;

    int itemType;

    public int specValueId;
    public String specValueName;

    public SellerSpecValueListItem(int itemType, int specValueId, String specValueName) {
        this.itemType = itemType;
        this.specValueId = specValueId;
        this.specValueName = specValueName;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
