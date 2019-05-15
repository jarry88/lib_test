package com.ftofs.twant.domain.api;

public class ApiSpecialItem {
    /**
     * 专题项目编号
     */
    private int itemId;

    /**
     * 专题编号
     */
    private int specialId;

    /**
     * 项目类型
     */
    private String itemType;

    /**
     * 项目数据
     */
    private String itemData = "";

    /**
     * 项目排序
     */
    private int itemSort = 999;

    /**
     * wap环境下是否显示，1-显示 0-不显示
     */
    private int wap = 1;

    /**
     * 微信环境下是否显示，1-显示 0-不显示
     */
    private int wechat= 1;

    /**
     * android环境下是否显示，1-显示 0-不显示
     */
    private int android = 1;

    /**
     * ios环境下是否显示，1-显示 0-不显示
     */
    private int ios = 1;

    public ApiSpecialItem() {
    }

    public ApiSpecialItem(ApiSpecialItem apiSpecialItem) {
        this.itemId = apiSpecialItem.itemId;
        this.specialId = apiSpecialItem.specialId;
        this.itemType = apiSpecialItem.itemType;
        this.itemData = apiSpecialItem.itemData;
        this.itemSort = apiSpecialItem.itemSort;
        this.wap = apiSpecialItem.wap;
        this.wechat = apiSpecialItem.wechat;
        this.android = apiSpecialItem.android;
        this.ios = apiSpecialItem.ios;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public int getItemSort() {
        return itemSort;
    }

    public void setItemSort(int itemSort) {
        this.itemSort = itemSort;
    }

    public String getItemTypeText() {
        return itemType;
    }

    public int getWap() {
        return wap;
    }

    public void setWap(int wap) {
        this.wap = wap;
    }

    public int getWechat() {
        return wechat;
    }

    public void setWechat(int wechat) {
        this.wechat = wechat;
    }

    public int getAndroid() {
        return android;
    }

    public void setAndroid(int android) {
        this.android = android;
    }

    public int getIos() {
        return ios;
    }

    public void setIos(int ios) {
        this.ios = ios;
    }

    @Override
    public String toString() {
        return "ApiSpecialItem{" +
                "itemId=" + itemId +
                ", specialId=" + specialId +
                ", itemType='" + itemType + '\'' +
                ", itemData='" + itemData + '\'' +
                ", itemSort=" + itemSort +
                ", wap=" + wap +
                ", wechat=" + wechat +
                ", android=" + android +
                ", ios=" + ios +
                '}';
    }
}

