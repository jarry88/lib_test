package com.ftofs.twant.domain;

public class ArticleCategory {
    /**
     * 分类主键、自增
     */
    private int categoryId;

    /**
     * 分类名称
     */
    private String title;

    /**
     * 出现位置
     */
    private int positionId = 1;

    /**
     * 分类排序
     */
    private int sort = 0;

    /**
     * 想要帖类型
     * 1-系统内置不可以删除，不可以发布想要帖
     * 2-系统内置不可以删除，可以发布想要帖
     * 3-系统内置可以删除，可以发布想要帖
     */
    private int type = 3;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ArticleCategory{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", positionId=" + positionId +
                ", sort=" + sort +
                ", type=" + type +
                '}';
    }
}
