package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

public class CrossBorderHomeItem implements MultiItemEntity {
    // 樓層類型常量定義
    public static final String FLOOR_TYPE_BANNER = "banner";
    public static final String FLOOR_TYPE_RECOMMEND = "recommend";

    public int itemType;

    // Banner用到以下數據
    public List<CrossBorderBannerItem> bannerItemList;

    // HEADER用到以下數據
    public int navItemCount;
    public List<CrossBorderNavPane> navPaneList;
    public List<CrossBorderShoppingZoneItem> shoppingZoneList; // 購物專場
    public List<CrossBorderActivityGoods> bargainGoodsList; // 砍價
    public List<CrossBorderActivityGoods> groupGoodsList; // 拼團

    // 樓層用到以下數據
    public int floorId;
    public String floorHeadline;
    public String floorSubhead;
    public String floorType;
    public List<FloorItem> floorItemList;

    // 優選好店用到以下數據
    public List<Store> storeList;

    // NORMAL用到以下數據
    public GoodsSearchItemPair goodsPair;

    @Override
    public int getItemType() {
        return itemType;
    }
}
