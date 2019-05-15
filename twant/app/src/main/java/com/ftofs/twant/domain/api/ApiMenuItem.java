package com.ftofs.twant.domain.api;

public class ApiMenuItem {
    /**
     * 菜单项目编号
     */
    private int itemId;

    /**
     * 菜单编号
     */
    private int menuId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目数据(JSON)
     */
    private String itemData;

    /**
     * 项目更多按钮
     */
    private String itemMore;

    /**
     * 三级项目数据(JSON)
     */
    private String subitemData;

    /**
     * 项目类型 0-图文 1-文字
     */
    private int itemType;

    /**
     * 排序
     */
    private int sort;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemData() {
        return itemData;
    }

    public void setItemData(String itemData) {
        this.itemData = itemData;
    }

    public String getItemMore() {
        return itemMore;
    }

    public void setItemMore(String itemMore) {
        this.itemMore = itemMore;
    }

    public String getSubitemData() {
        return subitemData;
    }

    public void setSubitemData(String subitemData) {
        this.subitemData = subitemData;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ApiMenuItem{" +
                "itemId=" + itemId +
                ", menuId=" + menuId +
                ", itemName='" + itemName + '\'' +
                ", itemData='" + itemData + '\'' +
                ", itemMore='" + itemMore + '\'' +
                ", subitemData='" + subitemData + '\'' +
                ", itemType=" + itemType +
                ", sort=" + sort +
                '}';
    }
}

