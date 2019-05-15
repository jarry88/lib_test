package com.ftofs.twant.vo.api;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 移动端自定义菜单项目实体
 *
 * @author dqw
 * Created 2017/6/21 9:52
 */
public class ApiMenuItemVo {
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
     * 项目数据
     */
    private ApiMenuItemDataVo itemData;
    /**
     * 项目更多按钮
     */
    private ApiMenuItemDataVo itemMore;
    /**
     * 三级项目数据(JSON)
     */
    private List<ApiMenuItemDataVo> subitemData;
    /**
     * 项目类型 0-图文 1-文字
     */
    private int itemType;

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

    public ApiMenuItemDataVo getItemData() {
        return itemData;
    }

    public void setItemData(ApiMenuItemDataVo itemData) {
        this.itemData = itemData;
    }

    public ApiMenuItemDataVo getItemMore() {
        return itemMore;
    }

    public void setItemMore(ApiMenuItemDataVo itemMore) {
        this.itemMore = itemMore;
    }

    public List<ApiMenuItemDataVo> getSubitemData() {
        return subitemData;
    }

    public void setSubitemData(List<ApiMenuItemDataVo> subitemData) {
        this.subitemData = subitemData;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "ApiMenuItemVo{" +
                "itemId=" + itemId +
                ", menuId=" + menuId +
                ", itemName='" + itemName + '\'' +
                ", itemData=" + itemData +
                ", itemMore=" + itemMore +
                ", subitemData='" + subitemData + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}

