package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

public class StatCategoryGeneral implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 分类ID
     */
    private int categoryId = 0;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 深度
     */
    private int deep = 0;

    /**
     * 一级分类
     */
    private int categoryId1;

    /**
     * 二级分类
     */
    private int categoryId2;

    /**
     * 三级分类
     */
    private int categoryId3;

    /**
     * 商品销量
     */
    private long ordersNum = 0L;

    /**
     * 商品销售额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 有销量商品数
     */
    private long haveOrdersGoodsCount = 0L;

    /**
     * 商品总数
     */
    private long goodsCount = 0L;

    /**
     * 无销量商品数
     */
    private long noOrdersGoodsCount = 0L;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
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

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
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

    public long getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(long ordersNum) {
        this.ordersNum = ordersNum;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public long getHaveOrdersGoodsCount() {
        return haveOrdersGoodsCount;
    }

    public void setHaveOrdersGoodsCount(long haveOrdersGoodsCount) {
        this.haveOrdersGoodsCount = haveOrdersGoodsCount;
    }

    public long getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(long goodsCount) {
        this.goodsCount = goodsCount;
    }

    public long getNoOrdersGoodsCount() {
        return noOrdersGoodsCount;
    }

    public void setNoOrdersGoodsCount(long noOrdersGoodsCount) {
        this.noOrdersGoodsCount = noOrdersGoodsCount;
    }

    @Override
    public String toString() {
        return "StatCategoryGeneral{" +
                "statId=" + statId +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", deep=" + deep +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", ordersNum=" + ordersNum +
                ", ordersAmount=" + ordersAmount +
                ", haveOrdersGoodsCount=" + haveOrdersGoodsCount +
                ", goodsCount=" + goodsCount +
                ", noOrdersGoodsCount=" + noOrdersGoodsCount +
                '}';
    }
}