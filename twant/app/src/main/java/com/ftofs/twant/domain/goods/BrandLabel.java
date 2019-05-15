package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class BrandLabel implements Serializable {
    /**
     * 品牌标签编号
     */
    private int brandLabelId;

    /**
     * 品牌标签名称
     */
    private String brandLabelName;

    /**
     * 品牌标签排序
     */
    private int brandLabelSort = 0;

    public int getBrandLabelId() {
        return brandLabelId;
    }

    public void setBrandLabelId(int brandLabelId) {
        this.brandLabelId = brandLabelId;
    }

    public String getBrandLabelName() {
        return brandLabelName;
    }

    public void setBrandLabelName(String brandLabelName) {
        this.brandLabelName = brandLabelName;
    }

    public int getBrandLabelSort() {
        return brandLabelSort;
    }

    public void setBrandLabelSort(int brandLabelSort) {
        this.brandLabelSort = brandLabelSort;
    }

    @Override
    public String toString() {
        return "BrandLabel{" +
                "brandLabelId=" + brandLabelId +
                ", brandLabelName='" + brandLabelName + '\'' +
                ", brandLabelSort=" + brandLabelSort +
                '}';
    }
}
