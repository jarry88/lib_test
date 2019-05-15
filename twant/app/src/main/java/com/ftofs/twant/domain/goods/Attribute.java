package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class Attribute implements Serializable {
    /**
     * 属性编号
     */
    private int attributeId;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 分类编号
     */
    private int categoryId;

    /**
     * 属性排序
     */
    private int attributeSort;

    /**
     * 是否显示
     */
    private int isShow;

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAttributeSort() {
        return attributeSort;
    }

    public void setAttributeSort(int attributeSort) {
        this.attributeSort = attributeSort;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attributeId=" + attributeId +
                ", attributeName='" + attributeName + '\'' +
                ", categoryId=" + categoryId +
                ", attributeSort=" + attributeSort +
                ", isShow=" + isShow +
                '}';
    }
}
