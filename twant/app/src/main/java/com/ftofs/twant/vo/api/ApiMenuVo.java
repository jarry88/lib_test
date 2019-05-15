package com.ftofs.twant.vo.api;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 移动端自定义菜单Vo
 *
 * @author dqw
 * Created 2017/4/17 11:54
 */
public class ApiMenuVo {
    //编号
    private int menuId;
    //菜单数据
    private String menuName;
    //菜单广告图(JSON)
    private List<ApiMenuItemDataVo> menuAd;
    //菜单项目列表
    private List<ApiMenuItemVo> menuItemList;

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

    public List<ApiMenuItemDataVo> getMenuAd() {
        return menuAd;
    }

    public void setMenuAd(List<ApiMenuItemDataVo> menuAd) {
        this.menuAd = menuAd;
    }

    public List<ApiMenuItemVo> getMenuItemList() {
        return menuItemList;
    }

    public void setMenuItemList(List<ApiMenuItemVo> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @Override
    public String
    toString() {
        return "ApiMenuVo{" +
                "menuId=" + menuId +
                ", menuName='" + menuName + '\'' +
                ", menuAd='" + menuAd + '\'' +
                ", menuItemList=" + menuItemList +
                '}';
    }
}

