package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 分类佣金
 * 
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class CategoryCommissionAdminListVo {
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
    private int parentId;
    /**
     * 父级分类名称
     */
    private String parentName;
    /**
     * 排序
     */
    private int categorySort;
    /**
     * 深度
     */
    private int deep;

    /**
     * 佣金比例id
     */
    private int ratesId;
    /**
     * 佣金比例
     */
    private int commisRate;

    public int getRatesId() {
        return ratesId;
    }

    public void setRatesId(int ratesId) {
        this.ratesId = ratesId;
    }

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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

    public int getCommisRate() {
        return commisRate;
    }

    public void setCommisRate(int commisRate) {
        this.commisRate = commisRate;
    }

    @Override
    public String toString() {
        return "CategoryCommisRatesAdminListVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", deep=" + deep +
                ", ratesId='" + ratesId + '\'' +
                ", commisRate='" + commisRate + '\'' +
                '}';
    }
}
