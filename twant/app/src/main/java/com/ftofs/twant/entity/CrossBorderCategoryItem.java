package com.ftofs.twant.entity;

public class CrossBorderCategoryItem {
    public int categoryId;
    public String catName;
    public String backgroundColor;

    public CrossBorderCategoryItem() {
    }

    public CrossBorderCategoryItem(int categoryId, String catName, String backgroundColor) {
        this.categoryId = categoryId;
        this.catName = catName;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "CrossBorderCategoryItem{" +
                "categoryId=" + categoryId +
                ", catName='" + catName + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                '}';
    }
}
