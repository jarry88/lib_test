package com.ftofs.twant.vo;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 想要帖分类Vo
 * 
 * @author hbj
 * Created 2017/4/13 16:14
 */
public class ArticleCategoryVo {
    /**
     * 分类主键、自增
     */
    private int categoryId;
    /**
     * 分类名称
     */
    private String title;
    /**
     * 分类排序
     */
    private int sort;

    /**
     * 出现位置
     */
    private int positionId;
    /**
     * 分类显示位置名称
     */
    private String positionTitle;
    /**
     * 想要帖类型
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

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ArticleCategoryVo{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", sort=" + sort +
                ", positionId=" + positionId +
                ", positionTitle='" + positionTitle + '\'' +
                ", type=" + type +
                '}';
    }
}
