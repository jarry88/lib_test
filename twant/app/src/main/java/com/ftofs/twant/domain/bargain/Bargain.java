package com.ftofs.twant.domain.bargain;

import java.math.BigDecimal;

public class Bargain {
    /**
     * 砍价活动编号
     */
    private int bargainId;

    /**
     * 產品SPU编号
     */
    private int commonId;

    /**
     * 產品SKU编号
     */
    private int goodsId;

    /**
     * 底价
     */
    private BigDecimal bottomPrice;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 砍价库存
     */
    private int bargainStorage;

    /**
     * 砍价区间低位
     */
    private BigDecimal intervalMin;

    /**
     * 砍价区间高位
     */
    private BigDecimal intervalMax;

    /**
     * 状态
     */
    private String bargainState = "";

    /**
     * 状态文字
     */
    private String bargainStateText;

    /**
     * 参加次数
     */
    private int joinNumber;

    public int getBargainId() {
        return bargainId;
    }

    public void setBargainId(int bargainId) {
        this.bargainId = bargainId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(BigDecimal bottomPrice) {
        this.bottomPrice = bottomPrice;
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

    public int getBargainStorage() {
        return bargainStorage;
    }

    public void setBargainStorage(int bargainStorage) {
        this.bargainStorage = bargainStorage;
    }

    public BigDecimal getIntervalMin() {
        return intervalMin;
    }

    public void setIntervalMin(BigDecimal intervalMin) {
        this.intervalMin = intervalMin;
    }

    public BigDecimal getIntervalMax() {
        return intervalMax;
    }

    public void setIntervalMax(BigDecimal intervalMax) {
        this.intervalMax = intervalMax;
    }

    public String getBargainState() {
        return bargainState;
    }

    public void setBargainState(String bargainState) {
        this.bargainState = bargainState;
    }

    public String getBargainStateText() {
        return bargainStateText;
    }

    public void setBargainStateText(String bargainStateText) {
        this.bargainStateText = bargainStateText;
    }

    public int getJoinNumber() {
        return joinNumber;
    }

    public void setJoinNumber(int joinNumber) {
        this.joinNumber = joinNumber;
    }

    @Override
    public String toString() {
        return "Bargain{" +
                "bargainId=" + bargainId +
                ", commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", bottomPrice=" + bottomPrice +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", bargainStorage=" + bargainStorage +
                ", intervalMin=" + intervalMin +
                ", intervalMax=" + intervalMax +
                ", bargainState=" + bargainState +
                ", bargainStateText='" + bargainStateText + '\'' +
                ", joinNumber=" + joinNumber +
                '}';
    }
}
