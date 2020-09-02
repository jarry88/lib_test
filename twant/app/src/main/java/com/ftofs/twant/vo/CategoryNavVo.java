package com.ftofs.twant.vo;

import com.ftofs.twant.domain.IndexCategoryNav;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 分类导航
 * 
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class CategoryNavVo {
    /**
     * 產品分类编号
     */
    private int categoryId;
    /**
     * 產品分类名称
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
    private String appImage;
    /**
     * 移动端分类图片Url
     */
    private String appImageUrl;
    /**
     * 分类列表
     */
    private List<CategoryNavVo> categoryList = new ArrayList<>();

    /**
     * bycj
     * 分类导航信息
     */
    private IndexCategoryNav IndexCategoryNav;

    /**
     * bycj
     * 频道id
     */
    private int channelId = 0 ;

    /**
     * 分类
     */
    private Object category;

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

    public void setAppImageUrl(String appImageUrl) {
        this.appImageUrl = appImageUrl;
    }

    public List<CategoryNavVo> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryNavVo> categoryList) {
        this.categoryList = categoryList;
    }

    public IndexCategoryNav getIndexCategoryNav() {
        return IndexCategoryNav;
    }

    public void setIndexCategoryNav(IndexCategoryNav indexCategoryNav) {
        IndexCategoryNav = indexCategoryNav;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public int isFold = 1;

    @Override
    public String toString() {
        return "CategoryNavVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", categorySort=" + categorySort +
                ", deep=" + deep +
                ", appImage='" + appImage + '\'' +
                ", appImageUrl='" + appImageUrl + '\'' +
                ", categoryList=" + categoryList +
                ", IndexCategoryNav=" + IndexCategoryNav +
                ", channelId=" + channelId +
                ", category=" + category +
                '}';
    }
}
