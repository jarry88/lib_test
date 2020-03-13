package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

public class StatStoreHours implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 店铺名称
     */
    private String storeName = "";

    /**
     * 店铺分类编号
     */
    private int storeClassId;

    /**
     * 店铺分类
     */
    private String storeClassName = "";

    /**
     * 日期(具体到天)
     */
    private String statDate;

    /**
     * 小时
     */
    private int statHour = 0;

    /**
     * 下单量
     */
    private long ordersNum = 0;

    /**
     * 下单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 下单產品数
     */
    private long ordersGoodsNum = 0;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreClassId() {
        return storeClassId;
    }

    public void setStoreClassId(int storeClassId) {
        this.storeClassId = storeClassId;
    }

    public String getStoreClassName() {
        return storeClassName;
    }

    public void setStoreClassName(String storeClassName) {
        this.storeClassName = storeClassName;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public int getStatHour() {
        return statHour;
    }

    public void setStatHour(int statHour) {
        this.statHour = statHour;
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

    public long getOrdersGoodsNum() {
        return ordersGoodsNum;
    }

    public void setOrdersGoodsNum(long ordersGoodsNum) {
        this.ordersGoodsNum = ordersGoodsNum;
    }

    @Override
    public String toString() {
        return "StatStoreHours{" +
                "statId=" + statId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeClassId=" + storeClassId +
                ", storeClassName='" + storeClassName + '\'' +
                ", statDate=" + statDate +
                ", statHour=" + statHour +
                ", ordersNum=" + ordersNum +
                ", ordersAmount=" + ordersAmount +
                ", ordersGoodsNum=" + ordersGoodsNum +
                '}';
    }
}