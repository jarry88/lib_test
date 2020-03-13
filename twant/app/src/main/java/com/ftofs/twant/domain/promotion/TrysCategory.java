package com.ftofs.twant.domain.promotion;

import java.io.Serializable;

public class TrysCategory implements Serializable {
    /**
     * 產品分类编号
     */
    private int categoryId;

    /**
     * 產品分类名称
     */
    private String categoryName;

    /**
     * 排序
     */
    private int categorySort;

    /**
     * PC分类图片
     */
    private String image1 = "";

    /**
     * PC分类图片Url
     */
    private String imageUrl1;

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

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    @Override
    public String toString() {
        return "TrysCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categorySort=" + categorySort +
                ", image1='" + image1 + '\'' +
                ", imageUrl1='" + imageUrl1 + '\'' +
                '}';
    }
}
