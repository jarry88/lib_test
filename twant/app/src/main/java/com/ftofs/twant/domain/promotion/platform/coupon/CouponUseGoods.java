package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;

public class CouponUseGoods implements Serializable,Cloneable {
    /**
     * 自增ID
     */
    private int id;

    /**
     * 平台券活动ID
     */
    private int activityId = 0;

    /**
     * 產品分类编号
     */
    private int categoryId = 0;

    /**
     * 一级分类
     */
    private int categoryId1 = 0;

    /**
     * 二级分类
     */
    private int categoryId2 = 0;

    /**
     * 三级分类
     */
    private int categoryId3 = 0;

    /**
     * 一级分类名称
     */
    private String categoryName1 = "";

    /**
     * 二级分类名称
     */
    private String categoryName2 = "";

    /**
     * 三级分类名称
     */
    private String categoryName3 = "";

    /**
     * 產品SPU编号
     */
    private int commonId = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public String getCategoryName2() {
        return categoryName2;
    }

    public void setCategoryName2(String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    public String getCategoryName3() {
        return categoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CouponUseGoods{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", categoryName1='" + categoryName1 + '\'' +
                ", categoryName2='" + categoryName2 + '\'' +
                ", categoryName3='" + categoryName3 + '\'' +
                ", commonId=" + commonId +
                '}';
    }
}
