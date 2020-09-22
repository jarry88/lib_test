package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class CrossBorderHomeItem implements MultiItemEntity {
    public int itemType;

    // HEADER用到以下數據
    public List<CrossBorderBannerItem> bannerItemList;
    public List<CrossBorderNavItem> navItemList;
    public List<CrossBorderShoppingZoneItem> shoppingZoneList;
    public List<CrossBorderActivityGoods> bargainGoodsList;
    public List<CrossBorderActivityGoods> groupGoodsList;
    public List<Store> storeList;

    // NORMAL用到以下數據
    public GoodsSearchItemPair goodsPair;

    @Override
    public int getItemType() {
        return itemType;
    }
}
