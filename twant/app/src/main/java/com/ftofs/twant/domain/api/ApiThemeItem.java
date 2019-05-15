package com.ftofs.twant.domain.api;

public class ApiThemeItem {
    /**
     * 专题项目编号
     */
    private int itemId;

    /**
     * 专题编号
     */
    private int themeId;

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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
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
        return "ApiThemeItem{" +
                "itemId=" + itemId +
                ", themeId=" + themeId +
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

