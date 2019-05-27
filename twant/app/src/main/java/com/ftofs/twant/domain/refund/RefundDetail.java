package com.ftofs.twant.domain.refund;

import java.io.Serializable;
import java.math.BigDecimal;

public class RefundDetail implements Serializable {
    /**
     * 退款单id
     */
    private int refundId;

    /**
     * 订单id
     */
    private int ordersId;

    /**
     * 支付宝退款批次号、非预定订单使用微信退款时，微信那边生成的退款单号
     */
    private String batchNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 在线退款金额
     */
    private BigDecimal payAmount = new BigDecimal(0);

    /**
     * 预存款退款金额
     */
    private BigDecimal pdAmount = new BigDecimal(0);

    /**
     * 充值卡退款金额
     */
    private BigDecimal rcbAmount = new BigDecimal(0);

    /**
     * 退款支付代码
     */
    private String refundCode = "predeposit";

    /**
     * 申请状态:1为处理中,2为已完成
     */
    private int refundState = 1;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 在线退款完成时间,默认为0
     */
    private String payTime;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPdAmount() {
        return pdAmount;
    }

    public void setPdAmount(BigDecimal pdAmount) {
        this.pdAmount = pdAmount;
    }

    public BigDecimal getRcbAmount() {
        return rcbAmount;
    }

    public void setRcbAmount(BigDecimal rcbAmount) {
        this.rcbAmount = rcbAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    @Override
    public String toString() {
        return "RefundDetail{" +
                "addTime=" + addTime +
                ", refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", batchNo='" + batchNo + '\'' +
                ", refundAmount=" + refundAmount +
                ", payAmount=" + payAmount +
                ", pdAmount=" + pdAmount +
                ", rcbAmount=" + rcbAmount +
                ", refundCode='" + refundCode + '\'' +
                ", refundState=" + refundState +
                ", payTime=" + payTime +
                '}';
    }
}
