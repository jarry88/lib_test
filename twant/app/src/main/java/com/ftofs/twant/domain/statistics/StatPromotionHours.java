package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class StatPromotionHours implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 商品促销类型 GoodsPromotionType 对应
     */
    private int promotionType = 0;

    /**
     * 日期(具体到天)
     */
    private Timestamp statDate;

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
     * 下单商品数
     */
    private long ordersGoodsNum = 0;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public Timestamp getStatDate() {
        return statDate;
    }

    public void setStatDate(Timestamp statDate) {
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
        return "StatPromotionHours{" +
                "statId=" + statId +
                ", promotionType=" + promotionType +
                ", statDate=" + statDate +
                ", statHour=" + statHour +
                ", ordersNum=" + ordersNum +
                ", ordersAmount=" + ordersAmount +
                ", ordersGoodsNum=" + ordersGoodsNum +
                '}';
    }
}