package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class GoodsCustom implements Serializable {
    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 自定义属性编号
     */
    private int customId;

    /**
     * 自定义属性名称
     */
    private String customValue;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

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
        return "GoodsCustom{" +
                "commonId=" + commonId +
                ", customId=" + customId +
                ", customValue='" + customValue + '\'' +
                '}';
    }
}
