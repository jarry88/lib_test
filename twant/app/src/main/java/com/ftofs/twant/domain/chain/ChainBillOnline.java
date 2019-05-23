package com.ftofs.twant.domain.chain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;

public class ChainBillOnline implements Serializable, Cloneable {
    /**
     * 主键、自增
     */
    private int billId;

    /**
     * 结算单号
     */
    private int billSn;

    /**
     * (结算周期)开始时间
     */
    private String startTime;

    /**
     * (结算周期)截止时间
     */
    private String endTime;

    /**
     * 结算金额
     */
    private BigDecimal billAmount = new BigDecimal(0);

    /**
     * 订单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 平台承担平台券总金额
     */
    private BigDecimal couponAmount = BigDecimal.ZERO;

    /**
     * 出账时间(生成结算单的时间)
     */
    private String createTime;

    /**
     * 账单状态
     */
    private int billState = 0;

    /**
     * 门店ID
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 结算备注
     */
    private String billNote;

    public String getStartDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(startTime);
    }

    public String getEndDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(endTime);
    }

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(createTime);
    }

    public String getBillStateName() {
        if (billState == 0) {
            return "未结算";
        } else if (billState == 1) {
            return "已结算";
        }
        return "";
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBillSn() {
        return billSn;
    }

    public void setBillSn(int billSn) {
        this.billSn = billSn;
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

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBillState() {
        return billState;
    }

    public void setBillState(int billState) {
        this.billState = billState;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getBillNote() {
        return billNote;
    }

    public void setBillNote(String billNote) {
        this.billNote = billNote;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    @Override
    public ChainBillOnline clone() throws CloneNotSupportedException {
        return (ChainBillOnline) super.clone();
    }

    @Override
    public String toString() {
        return "ChainBillOnline{" +
                "billId=" + billId +
                ", billSn=" + billSn +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", billAmount=" + billAmount +
                ", ordersAmount=" + ordersAmount +
                ", couponAmount=" + couponAmount +
                ", createTime=" + createTime +
                ", billState=" + billState +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", billNote='" + billNote + '\'' +
                '}';
    }
}
