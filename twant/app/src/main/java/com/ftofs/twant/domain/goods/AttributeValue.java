package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class AttributeValue implements Serializable {
    /**
     * 属性值编号
     */
    private int attributeValueId;

    /**
     * 属性值名称
     */
    private String attributeValueName;

    /**
     * 属性编号
     */
    private int attributeId;

    public int getAttributeValueId() {
        return attributeValueId;
    }

    public void setAttributeValueId(int attributeValueId) {
        this.attributeValueId = attributeValueId;
    }

    public String getAttributeValueName() {
        return attributeValueName;
    }

    public void setAttributeValueName(String attributeValueName) {
        this.attributeValueName = attributeValueName;
    }

    public int getAttributeId(int attributeId) {
        return this.attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "attributeValueId=" + attributeValueId +
                ", attributeValueName='" + attributeValueName + '\'' +
                ", attributeId=" + attributeId +
                '}';
    }
}
