package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PaySuccessStoreInfoItem implements MultiItemEntity {
    public int itemViewType;

    public String storeAvatar;
    public String storeName;
    public int storeBizStatus; // 店鋪營業狀態

    public String goodsImageUrl;
    public String goodsName;
    public String goodsSpec;  // 商品規格
    public String selfTakeCode;  // 自提碼

    public String storePhone;
    public String storeAddress;
    public double storeLatitude;
    public double storeLongitude;
    public double storeDistance;

    public String businessTimeWorkingDay;
    public String businessTimeWeekend;
    public String transportInstruction;
    public String storeBusInfo;
    public double templatePrice;  // 店鋪優惠券,大於0才顯示

    @Override
    public int getItemType() {
        return itemViewType;
    }
}
