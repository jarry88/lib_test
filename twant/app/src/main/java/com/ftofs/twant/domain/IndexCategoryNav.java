package com.ftofs.twant.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class IndexCategoryNav implements Serializable {
    /**
     * 分类id
     */
    private int categoryId;

    /**
     * 首页分类广告图json数据
     */
    private String categoryNavAdvJson;

    /**
     * 首页分类广告图json数据
     */
    private String categoryNavImage;

    /**
     * 更新时间
     */
    private Timestamp categoryNavUpdateTime;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryNavAdvJson() {
        return categoryNavAdvJson;
    }

    public void setCategoryNavAdvJson(String categoryNavAdvJson) {
        this.categoryNavAdvJson = categoryNavAdvJson;
    }

    public Timestamp getCategoryNavUpdateTime() {
        return categoryNavUpdateTime;
    }

    public void setCategoryNavUpdateTime(Timestamp categoryNavUpdateTime) {
        this.categoryNavUpdateTime = categoryNavUpdateTime;
    }

    public String getCategoryNavImage() {
        return categoryNavImage;
    }

    public void setCategoryNavImage(String categoryNavImage) {
        this.categoryNavImage = categoryNavImage;
    }

    @Override
    public String toString() {
        return "IndexCategoryNav{" +
                "categoryId=" + categoryId +
                ", categoryNavAdvJson='" + categoryNavAdvJson + '\'' +
                ", categoryNavImage='" + categoryNavImage + '\'' +
                ", categoryNavUpdateTime=" + categoryNavUpdateTime +
                '}';
    }
}
