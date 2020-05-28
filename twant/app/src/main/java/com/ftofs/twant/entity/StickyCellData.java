package com.ftofs.twant.entity;

import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class StickyCellData {
    public int goodsCommonCount; // 导航栏显示的商品数
    public int wantPostCount;    // 导航栏显示的贴文数
    public int storeCount;       // 导航栏显示的店铺数

    public boolean activityEnable;  // 是否启用导航栏上的活动按钮
    // 以下字段在activityEnable为true的时候才使用到
    public String appIndexNavigationImage;  // 活动按钮的图标URL
    public String appIndexNavigationLinkType;  // 活动按钮的链接类型，取值: activity -- 表示跳去H5活动页面  promotion -- 表示跳去购物专场页面
    public String appIndexNavigationLinkValue; // 跳去H5活动页面的URL地址（在appIndexNavigationLinkType取值为activity时才有用）

    public List<ShoppingZoneItem> zoneItemList;//主題活動列表

}
