package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class CategoryBrand implements Serializable {
    /**
     * 商品分类编号
     */
    private int categoryId;

    /**
     * 品牌编号
     */
    private int brandId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "CategoryBrand{" +
                "categoryId=" + categoryId +
                ", brandId=" + brandId +
                '}';
    }
}
