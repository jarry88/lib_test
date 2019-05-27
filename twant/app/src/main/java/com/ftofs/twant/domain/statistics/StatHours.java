package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

public class StatHours implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 日期
     */
    private String statDate;

    /**
     * 小时
     */
    private int statHour = 0;

    /**
     * 新增会员数
     */
    private long newMemberNum = 0;

    /**
     * 下单量
     */
    private long ordersNum = 0;

    /**
     * 下单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 新增店铺数
     */
    private long newStoreNum = 0;

    /**
     * 预存款充值金额
     */
    private BigDecimal predepositRechargeAmount = new BigDecimal(0);

    /**
     * 预存款提现金额
     */
    private BigDecimal predepositCashAmount = new BigDecimal(0);

    /**
     * 预存款消费金额
     */
    private BigDecimal predepositConsumeAmount = new BigDecimal(0);

    /**
     * 预存款退款金额
     */
    private BigDecimal predepositRefundAmount = new BigDecimal(0);

    /**
     * 预存款余额（可用金额+冻结金额）
     */
    private BigDecimal predepositBalanceAmount = new BigDecimal(0);

    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 新增商品数
     */
    private long newGoodsCommonNum = 0;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
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

    public long getNewMemberNum() {
        return newMemberNum;
    }

    public void setNewMemberNum(long newMemberNum) {
        this.newMemberNum = newMemberNum;
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

    public long getNewStoreNum() {
        return newStoreNum;
    }

    public void setNewStoreNum(long newStoreNum) {
        this.newStoreNum = newStoreNum;
    }

    public BigDecimal getPredepositRechargeAmount() {
        return predepositRechargeAmount;
    }

    public void setPredepositRechargeAmount(BigDecimal predepositRechargeAmount) {
        this.predepositRechargeAmount = predepositRechargeAmount;
    }

    public BigDecimal getPredepositCashAmount() {
        return predepositCashAmount;
    }

    public void setPredepositCashAmount(BigDecimal predepositCashAmount) {
        this.predepositCashAmount = predepositCashAmount;
    }

    public BigDecimal getPredepositConsumeAmount() {
        return predepositConsumeAmount;
    }

    public void setPredepositConsumeAmount(BigDecimal predepositConsumeAmount) {
        this.predepositConsumeAmount = predepositConsumeAmount;
    }

    public BigDecimal getPredepositRefundAmount() {
        return predepositRefundAmount;
    }

    public void setPredepositRefundAmount(BigDecimal predepositRefundAmount) {
        this.predepositRefundAmount = predepositRefundAmount;
    }

    public BigDecimal getPredepositBalanceAmount() {
        return predepositBalanceAmount;
    }

    public void setPredepositBalanceAmount(BigDecimal predepositBalanceAmount) {
        this.predepositBalanceAmount = predepositBalanceAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public long getNewGoodsCommonNum() {
        return newGoodsCommonNum;
    }

    public void setNewGoodsCommonNum(long newGoodsCommonNum) {
        this.newGoodsCommonNum = newGoodsCommonNum;
    }

    @Override
    public String toString() {
        return "StatHours{" +
                "statId=" + statId +
                ", statDate=" + statDate +
                ", statHour=" + statHour +
                ", newMemberNum=" + newMemberNum +
                ", ordersNum=" + ordersNum +
                ", ordersAmount=" + ordersAmount +
                ", newStoreNum=" + newStoreNum +
                ", predepositRechargeAmount=" + predepositRechargeAmount +
                ", predepositCashAmount=" + predepositCashAmount +
                ", predepositConsumeAmount=" + predepositConsumeAmount +
                ", predepositRefundAmount=" + predepositRefundAmount +
                ", predepositBalanceAmount=" + predepositBalanceAmount +
                ", refundAmount=" + refundAmount +
                ", newGoodsCommonNum=" + newGoodsCommonNum +
                '}';
    }
}