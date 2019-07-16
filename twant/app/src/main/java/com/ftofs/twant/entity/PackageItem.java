package com.ftofs.twant.entity;


import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PackageItem implements MultiItemEntity {
    public static final int ITEM_TYPE_SEARCH = 1;
    public static final int ITEM_TYPE_INFO = 2;

    @Override
    public int getItemType() {
        return itemType;
    }

    public int itemType;
    public String originalOrderNumber; //受理單號

    public String consigneeAddress; //收件人地址
    public String consigneeName; //收件人姓名
    public String consigneePhone; //收件人手機

    public String consignerAddress; //寄件人地址
    public String consignerName; //寄件人姓名
    public String consignerPhone; //寄件人手機
    public int state; //物流狀態
    public String customerOrderNumber; //快遞客戶單號
    public String createTime; //創建時間/發貨日期
    public String updateTime; //更新時間
}
