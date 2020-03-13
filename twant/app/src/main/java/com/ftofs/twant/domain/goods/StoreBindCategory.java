package com.ftofs.twant.domain.goods;

public class StoreBindCategory {
    /**
     * 主键 自增
     */
    private int bindId;

    /**
     * 店铺Id
     */
    private int storeId;

    /**
     * 一级產品分类编号
     */
    private int categoryId1 = 0;

    /**
     * 一级產品分类名称
     */
    private String categoryName1 = "";

    /**
     * 二级產品分类
     */
    private int categoryId2 = 0;

    /**
     * 二级產品分类名称
     */
    private String categoryName2 = "";

    /**
     * 三级產品分类
     */
    private int categoryId3 = 0;

    /**
     * 三级產品分类名称
     */
    private String categoryName3 = "";

    /**
     * 佣金比例
     */
    private int commissionRate;

    /**
     * 状态 0-审核中，1-已审核
     * 商家入驻后，新增类目申请时为0，商家审核通过后为1
     * 商家第一次开店申请值为1
     */
    private int bindState = 1;

    /**
     * 最末级分类ID
     */
    private int categoryId = 0;

    public int getBindId() {
        return bindId;
    }

    public void setBindId(int bindId) {
        this.bindId = bindId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryName2() {
        return categoryName2;
    }

    public void setCategoryName2(String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryName3() {
        return categoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getBindState() {
        return bindState;
    }

    public void setBindState(int bindState) {
        this.bindState = bindState;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "StoreBindCategory{" +
                "bindId=" + bindId +
                ", storeId=" + storeId +
                ", categoryId1=" + categoryId1 +
                ", categoryName1='" + categoryName1 + '\'' +
                ", categoryId2=" + categoryId2 +
                ", categoryName2='" + categoryName2 + '\'' +
                ", categoryId3=" + categoryId3 +
                ", categoryName3='" + categoryName3 + '\'' +
                ", commissionRate=" + commissionRate +
                ", bindState=" + bindState +
                ", categoryId=" + categoryId +
                '}';
    }
}

