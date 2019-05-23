package com.ftofs.twant.domain.chain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;

public class ChainBillOffline implements Serializable {
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
     * 订单金额
     */
    private BigDecimal billAmount = new BigDecimal(0);

    /**
     * 出账时间(生成结算单的时间)
     */
    private String createTime;

    /**
     * 店员编号
     */
    private int clerkId;

    /**
     * 店员名称
     */
    private String clerkName;

    /**
     * 门店ID
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    public String getStartDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(startTime);
    }

    public String getEndDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(endTime);
    }

    public String getCreateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(createTime);
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

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
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

    @Override
    public String toString() {
        return "ChainBillOffline{" +
                "billId=" + billId +
                ", billSn=" + billSn +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", billAmount=" + billAmount +
                ", createTime=" + createTime +
                ", clerkId=" + clerkId +
                ", clerkName='" + clerkName + '\'' +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                '}';
    }
}
