package com.ftofs.twant.domain.store;

public class StoreSpecialItem {
    /**
     * 编号
     */
    private int itemId;

    /**
     * 专题编号
     */
    private int specialId;

    /**
     * 内容类型
     */
    private String itemType;

    /**
     * 内容数据
     */
    private String itemData;

    /**
     * 排序
     */
    private int itemSort;

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

    @Override
    public String toString() {
        return "StoreSpecialItem{" +
                "itemId=" + itemId +
                ", specialId=" + specialId +
                ", itemType='" + itemType + '\'' +
                ", itemData='" + itemData + '\'' +
                ", itemSort=" + itemSort +
                '}';
    }
}
