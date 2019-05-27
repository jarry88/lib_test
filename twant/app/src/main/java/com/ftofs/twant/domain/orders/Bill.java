package com.ftofs.twant.domain.orders;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bill implements Serializable {
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
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 运费金额(仅页面显示时备用，结算时该字段不参与结算，因为订单金额里已经包含运费了)
     */
    private BigDecimal freightAmount = new BigDecimal(0);

    /**
     * 平台收取佣金金额
     */
    private BigDecimal commissionAmount = new BigDecimal(0);

    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 退还佣金金额
     */
    private BigDecimal refundCommissionAmount = new BigDecimal(0);

    /**
     * 平台应(与商家)结算金额
     */
    private BigDecimal billAmount = new BigDecimal(0);

    /**
     * 出账时间(生成结算单的时间)
     */
    private String createTime = "";

    /**
     * 账单状态
     * 10默认(已出账单)、20店家已确认、30平台已审核、40结算完成
     */
    private int billState = 10;

    /**
     * (平台给商家)付款日期
     */
    private String paymentTime;

    /**
     * (平台给商家)付款备注
     */
    private String paymentNote;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 平台承担平台券总金额
     */
    private BigDecimal couponAmount = BigDecimal.ZERO;

    /**
     * 费用
     */
    private BigDecimal costAmount = new BigDecimal(0);

    /**
     * 平台券总金额(全部退款)
     */
    private BigDecimal refundCouponAmount = new BigDecimal(0);

    /**
     * 预定订单(买家、系统自动)取消后未退的定金金额
     */
    private BigDecimal bookAmount = new BigDecimal(0);

    /**
     * bycj-- 商品推广需要扣除的佣金
     */
    private BigDecimal distributionAmount = new BigDecimal(0);

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

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundCommissionAmount() {
        return refundCommissionAmount;
    }

    public void setRefundCommissionAmount(BigDecimal refundCommissionAmount) {
        this.refundCommissionAmount = refundCommissionAmount;
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

    public int getBillState() {
        return billState;
    }

    public void setBillState(int billState) {
        this.billState = billState;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentNote() {
        return paymentNote;
    }

    public void setPaymentNote(String paymentNote) {
        this.paymentNote = paymentNote;
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

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getRefundCouponAmount() {
        return refundCouponAmount;
    }

    public void setRefundCouponAmount(BigDecimal refundCouponAmount) {
        this.refundCouponAmount = refundCouponAmount;
    }

    public BigDecimal getBookAmount() {
        return bookAmount;
    }

    public void setBookAmount(BigDecimal bookAmount) {
        this.bookAmount = bookAmount;
    }

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billSn=" + billSn +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", ordersAmount=" + ordersAmount +
                ", freightAmount=" + freightAmount +
                ", commissionAmount=" + commissionAmount +
                ", refundAmount=" + refundAmount +
                ", refundCommissionAmount=" + refundCommissionAmount +
                ", billAmount=" + billAmount +
                ", createTime=" + createTime +
                ", billState=" + billState +
                ", paymentTime=" + paymentTime +
                ", paymentNote='" + paymentNote + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", couponAmount=" + couponAmount +
                ", costAmount=" + costAmount +
                ", refundCouponAmount=" + refundCouponAmount +
                ", bookAmount=" + bookAmount +
                ", distributionAmount=" + distributionAmount +
                '}';
    }
}
