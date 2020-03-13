package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品与自定义属性关系Vo
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:06
 */
public class GoodsCustomVo {
    /**
     * 自定义属性编号
     */
    private int customId;
    /**
     * 自定义属性名称
     */
    private String customValue;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    @Override
    public String toString() {
        return "GoodsCustomVo{" +
                "customId=" + customId +
                ", customValue='" + customValue + '\'' +
                '}';
    }
}
