package com.ftofs.twant.domain.seckill;

import java.math.BigDecimal;

public class SeckillGoods {
    /**
     * 主键
     */
    private int seckillGoodsId;

    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SPU
     */
    private int commonId;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /**
     * 库存
     */
    private int goodsStorage;

    /**
     * 限购数量
     */
    private int limitAmount;

    /**
     * 秒杀排期编号
     */
    private int scheduleId;

    /**
     * 开始时间
     */
    private String startTime = "";

    /**
     * 结束时间
     */
    private String endTime = "";

    /**
     * 状态
     */
    private int scheduleState = 1;

    /**
     * 状态文字
     */
    private String scheduleStateText;

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(int scheduleState) {
        this.scheduleState = scheduleState;
    }

    public String getScheduleStateText() {
        return scheduleStateText;
    }

    public void setScheduleStateText(String scheduleStateText) {
        this.scheduleStateText = scheduleStateText;
    }

    @Override
    public String toString() {
        return "SeckillGoods{" +
                "seckillGoodsId=" + seckillGoodsId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsPrice=" + goodsPrice +
                ", goodsStorage=" + goodsStorage +
                ", limitAmount=" + limitAmount +
                ", scheduleId=" + scheduleId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", scheduleState=" + scheduleState +
                ", scheduleStateText='" + scheduleStateText + '\'' +
                '}';
    }
}
