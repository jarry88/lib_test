package com.ftofs.twant.vo.api;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 接口商城活动项目Vo
 *
 * @author szq
 * Created 2017/8/28 11:48
 */
public class ApiThemeItemVo {
    /**
     * 项目类型
     */
    private String itemType;
    /**
     * 项目数据
     */
    private Object itemData;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Object getItemData() {
        return itemData;
    }

    public void setItemData(Object itemData) {
        this.itemData = itemData;
    }

    @Override
    public String toString() {
        return "ApiThemeItemVo{" +
                "itemType='" + itemType + '\'' +
                ", itemData=" + itemData +
                '}';
    }
}
