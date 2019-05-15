package com.ftofs.twant.vo.menu;

import com.ftofs.twant.domain.menu.MenuItem;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 菜单Vo
 *
 * @author dqw
 * Created 2017/4/17 11:54
 */
public class MenuVo {
    //编号
    private int menuId;
    //菜单数据(JSON)
    private String menuData;
    //菜单图标
    private String menuIcon;
    //菜单导航(JSON)
    private String menuNav;
    //菜单广告图(JSON)
    private String menuAd;
    //菜单项目列表
    private List<MenuItem> menuItemList;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
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

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @Override
    public String toString() {
        return "MenuVo{" +
                "menuId=" + menuId +
                ", menuData='" + menuData + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuNav='" + menuNav + '\'' +
                ", menuAd='" + menuAd + '\'' +
                ", menuItemList=" + menuItemList +
                '}';
    }
}


