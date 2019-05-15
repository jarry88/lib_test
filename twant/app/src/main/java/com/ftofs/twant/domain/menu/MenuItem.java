package com.ftofs.twant.domain.menu;

public class MenuItem {
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
     * 子项目数据(JSON)
     */
    private String subitemData;

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

    public String getSubitemData() {
        return subitemData;
    }

    public void setSubitemData(String subitemData) {
        this.subitemData = subitemData;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemId=" + itemId +
                ", menuId=" + menuId +
                ", itemName='" + itemName + '\'' +
                ", itemData='" + itemData + '\'' +
                ", subitemData='" + subitemData + '\'' +
                ", sort=" + sort +
                '}';
    }
}

