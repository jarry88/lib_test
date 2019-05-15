package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialCategory implements Serializable {
    /**
     * 推文分类编号
     */
    private int categoryId;

    /**
     * 推文分类名称
     */
    private String categoryName;

    /**
     * 推文分类排序
     */
    private int categorySort;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(int categorySort) {
        this.categorySort = categorySort;
    }

    @Override
    public String toString() {
        return "AdvertorialcCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categorySort=" + categorySort +
                '}';
    }
}
