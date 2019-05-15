package com.ftofs.twant.domain.store;

public class StoreMobileSpecialItem {
    /**
     * 专题项目编号
     */
    private int itemId;

    /**
     * 专题编号
     */
    private int specialId;

    /**
     * 店铺编号
     */
    private int storeId;

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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    @Override
    public String toString() {
        return "StoreMobileSpecialItem{" +
                "itemId=" + itemId +
                ", specialId=" + specialId +
                ", storeId=" + storeId +
                ", itemType='" + itemType + '\'' +
                ", itemData='" + itemData + '\'' +
                ", itemSort=" + itemSort +
                '}';
    }
}

