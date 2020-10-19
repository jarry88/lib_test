package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class CrossBorderHomeItem implements MultiItemEntity {
    public int itemType;

    // HEADER用到以下數據
    public List<CrossBorderBannerItem> bannerItemList;
    public int navItemCount;
    public List<CrossBorderNavPane> navPaneList;
    public List<CrossBorderShoppingZoneItem> shoppingZoneList;
    public List<CrossBorderActivityGoods> bargainGoodsList;
    public List<CrossBorderActivityGoods> groupGoodsList;
    public List<CrossBorderFloorItem> floorItemList;  // 樓層結構
    public List<Store> storeList;

    // NORMAL用到以下數據
    public GoodsSearchItemPair goodsPair;

    @Override
    public int getItemType() {
        return itemType;
    }
}
