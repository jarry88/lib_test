package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class Category implements Serializable {
    /**
     * 商品分类编号
     */
    private int categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 父级分类编号
     */
    private int parentId = 0;

    /**
     * 排序
     */
    private int categorySort;

    /**
     * 深度
     */
    private int deep = 0;

    /**
     * 移动端分类图片
     */
    private String appImage = "";

    /**
     * 移动端分类图片Url
     */
    private String appImageUrl;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(int categorySort) {
        this.categorySort = categorySort;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    public String getAppImageUrl() {
        return appImageUrl;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                ", appImage='" + appImage + '\'' +
                ", appImageUrl='" + appImageUrl + '\'' +
                '}';
    }
}
