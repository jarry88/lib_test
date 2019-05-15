package com.ftofs.twant.domain.api;

public class ApiMenu {
    /**
     * 编号
     */
    private int menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单广告图(JSON)
     */
    private String menuAd;

    /**
     * 菜单排序
     */
    private int sort;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuAd() {
        return menuAd;
    }

    public void setMenuAd(String menuAd) {
        this.menuAd = menuAd;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ApiMenu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", menuAd='" + menuAd + '\'' +
                ", sort=" + sort +
                '}';
    }
}

