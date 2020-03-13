package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class GoodsAttribute implements Serializable {
    /**
     * 產品SPU编号
     */
    private int commonId;

    /**
     * 属性值编号
     */
    private int attributeValueId;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getAttributeValueId() {
        return attributeValueId;
    }

    public void setAttributeValueId(int attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    @Override
    public String toString() {
        return "GoodsAttribute{" +
                "commonId=" + commonId +
                ", attributeValueId=" + attributeValueId +
                '}';
    }
}
