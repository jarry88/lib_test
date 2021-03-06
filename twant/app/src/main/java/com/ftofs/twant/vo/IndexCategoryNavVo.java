package com.ftofs.twant.vo;

import java.io.Serializable;

public class IndexCategoryNavVo implements Serializable {
    /**
     * 分类id
     */
    private int categoryId;
    /**
     * 排序
     */
    private int categorySort;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 首页分类广告图json数据
     */
    private String categoryNavAdvJson;

    /**
     * 首页分类广告图片
     */
    private String categoryNavImage;

    /**
     * 更新时间
     */
    private String categoryNavUpdateTime;

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

    public String getCategoryNavAdvJson() {
        return categoryNavAdvJson;
    }

    public void setCategoryNavAdvJson(String categoryNavAdvJson) {
        this.categoryNavAdvJson = categoryNavAdvJson;
    }

    public String getCategoryNavUpdateTime() {
        return categoryNavUpdateTime;
    }

    public void setCategoryNavUpdateTime(String categoryNavUpdateTime) {
        this.categoryNavUpdateTime = categoryNavUpdateTime;
    }

    public int getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(int categorySort) {
        this.categorySort = categorySort;
    }


    public String getCategoryNavImage() {
        return categoryNavImage;
    }

    public void setCategoryNavImage(String categoryNavImage) {
        this.categoryNavImage = categoryNavImage;
    }

    @Override
    public String toString() {
        return "IndexCategoryNavVo{" +
                "categoryId=" + categoryId +
                ", categorySort=" + categorySort +
                ", categoryName='" + categoryName + '\'' +
                ", categoryNavAdvJson='" + categoryNavAdvJson + '\'' +
                ", categoryNavImage='" + categoryNavImage + '\'' +
                ", categoryNavUpdateTime=" + categoryNavUpdateTime +
                '}';
    }
}
