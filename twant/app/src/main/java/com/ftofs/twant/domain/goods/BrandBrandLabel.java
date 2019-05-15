package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class BrandBrandLabel implements Serializable {
    /**
     * 品牌编号
     */
    private int brandId;

    /**
     * 品牌标签编号
     */
    private int brandLabelId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getBrandLabelId() {
        return brandLabelId;
    }

    public void setBrandLabelId(int brandLabelId) {
        this.brandLabelId = brandLabelId;
    }

    @Override
    public String toString() {
        return "BrandBrandLabel{" +
                "brandId=" + brandId +
                ", brandLabelId=" + brandLabelId +
                '}';
    }
}
