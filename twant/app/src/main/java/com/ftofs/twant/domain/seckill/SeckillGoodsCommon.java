package com.ftofs.twant.domain.seckill;

import java.math.BigDecimal;

public class SeckillGoodsCommon {
    /**
     * 主键
     */
    private int seckillCommonId;

    /**
     * 產品SPU
     */
    private int commonId;

    /**
     * 產品价格
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /**
     * 销售数量
     */
    private int goodsSaleNum = 0;

    /**
     * 库存
     */
    private int goodsStorage;

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
     * 状态文字
     */
    private int scheduleState = 1;

    /**
     * 审核失败原因
     */
    private String verifyRemark;

    /**
     * 状态文字
     */
    private String scheduleStateText;

    public int getSeckillCommonId() {
        return seckillCommonId;
    }

    public void setSeckillCommonId(int seckillCommonId) {
        this.seckillCommonId = seckillCommonId;
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

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
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

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getScheduleStateText() {
        return scheduleStateText;
    }

    public void setScheduleStateText(String scheduleStateText) {
        this.scheduleStateText = scheduleStateText;
    }

    @Override
    public String toString() {
        return "SeckillGoodsCommon{" +
                "seckillCommonId=" + seckillCommonId +
                ", commonId=" + commonId +
                ", goodsPrice=" + goodsPrice +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", scheduleId=" + scheduleId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", scheduleState=" + scheduleState +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", scheduleStateText='" + scheduleStateText + '\'' +
                '}';
    }
}
