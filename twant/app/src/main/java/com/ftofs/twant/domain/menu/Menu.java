package com.ftofs.twant.domain.menu;

public class Menu {
    /**
     * 编号
     */
    private int menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单数据(JSON)
     */
    private String menuData;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单导航(JSON)
     */
    private String menuNav;

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

    public String getMenuData() {
        return menuData;
    }

    public void setMenuData(String menuData) {
        this.menuData = menuData;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuNav() {
        return menuNav;
    }

    public void setMenuNav(String menuNav) {
        this.menuNav = menuNav;
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
        return "Menu{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", menuData='" + menuData + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuNav='" + menuNav + '\'' +
                ", menuAd='" + menuAd + '\'' +
                ", sort=" + sort +
                '}';
    }
}


