package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class CrossBorderHomeItem implements MultiItemEntity {
    public int itemType;

    public List<CrossBorderBannerItem> bannerItemList;
    public List<CrossBorderNavItem> navItemList;
    public List<CrossBorderShoppingZoneItem> shoppingZoneList;

    @Override
    public int getItemType() {
        return itemType;
    }
}
