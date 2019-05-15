package com.ftofs.twant.domain.chain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ChainStatOrders implements Serializable {
    /**
     * 订单主键ID
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn = 0L;

    /**
     * 订单状态
     * 0-已取消 10-未支付 25-已支付 40-已取货
     */
    private int ordersState = 0;

    /**
     * 订单来源
     * web/app/wechat
     */
    private String ordersFrom = "";

    /**
     * 订单详细来源
     * web/ios/android/wap/wechat
     */
    private String ordersFrom1 = "";

    /**
     * 支付单ID
     */
    private int payId = 0;

    /**
     * 支付单号
     */
    private long paySn = 0L;

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 收货人电话
     */
    private String receiverPhone = "";

    /**
     * 收货人姓名
     */
    private String receiverName = "";

    /**
     * 下单时间
     */
    private Timestamp createTime;

    /**
     * 支付方式代码
     */
    private String paymentCode = "";

    /**
     * 支付使用终端(WAP,WEB,APP)
     */
    private String paymentClientType = "";

    /**
     * 外部交易号
     */
    private String outOrdersSn = "";

    /**
     * 支付时间
     */
    private Timestamp paymentTime;

    /**
     * 订单完成时间
     */
    private Timestamp finishTime;

    /**
     * 订单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 预存款支付金额
     */
    private BigDecimal predepositAmount = new BigDecimal(0);

    /**
     * 评价状态
     * 0-未评价 1-已评价
     */
    private int evaluationState = 0;

    /**
     * 评价时间
     */
    private Timestamp evaluationTime;

    /**
     * 订单取消原因
     */
    private Integer cancelReason = 6;

    /**
     * 关闭时间
     */
    private Timestamp cancelTime;

    /**
     * 订单关闭操作主体
     */
    private String cancelRole = "";

    /**
     * 是否是管理员点击的收款
     */
    private int adminReceivePayState = 0;

    /**
     * 删除状态 0-未删除/1-已删除
     */
    private int deleteState = 0;

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 预计取货时间
     */
    private Timestamp takeTime;

    /**
     * 店员ID
     */
    private int clerkId = 0;

    /**
     * 店员
     */
    private String clerkName = "";

    /**
     * 门店付款的具体付款方式
     */
    private String chainPaymentName = "";

    /**
     * 门店订单类型
     */
    private int ordersType;

    /**
     * 下单时间 具体到天
     */
    private String createTimeDate;

    /**
     * 下单时间 小时
     */
    private String createTimeHour = "";

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getOrdersState() {
        return ordersState;
    }

    public void setOrdersState(int ordersState) {
        this.ordersState = ordersState;
    }

    public String getOrdersFrom() {
        return ordersFrom;
    }

    public void setOrdersFrom(String ordersFrom) {
        this.ordersFrom = ordersFrom;
    }

    public String getOrdersFrom1() {
        return ordersFrom1;
    }

    public void setOrdersFrom1(String ordersFrom1) {
        this.ordersFrom1 = ordersFrom1;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentClientType() {
        return paymentClientType;
    }

    public void setPaymentClientType(String paymentClientType) {
        this.paymentClientType = paymentClientType;
    }

    public String getOutOrdersSn() {
        return outOrdersSn;
    }

    public void setOutOrdersSn(String outOrdersSn) {
        this.outOrdersSn = outOrdersSn;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
    }

    public int getEvaluationState() {
        return evaluationState;
    }

    public void setEvaluationState(int evaluationState) {
        this.evaluationState = evaluationState;
    }

    public Timestamp getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Timestamp evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public Integer getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Integer cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelRole() {
        return cancelRole;
    }

    public void setCancelRole(String cancelRole) {
        this.cancelRole = cancelRole;
    }

    public int getAdminReceivePayState() {
        return adminReceivePayState;
    }

    public void setAdminReceivePayState(int adminReceivePayState) {
        this.adminReceivePayState = adminReceivePayState;
    }

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
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

    public Timestamp getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Timestamp takeTime) {
        this.takeTime = takeTime;
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

    public String getChainPaymentName() {
        return chainPaymentName;
    }

    public void setChainPaymentName(String chainPaymentName) {
        this.chainPaymentName = chainPaymentName;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public String getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(String createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    public String getCreateTimeHour() {
        return createTimeHour;
    }

    public void setCreateTimeHour(String createTimeHour) {
        this.createTimeHour = createTimeHour;
    }

    @Override
    public String toString() {
        return "ChainStatOrders{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", ordersFrom1='" + ordersFrom1 + '\'' +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", createTime=" + createTime +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentTime=" + paymentTime +
                ", finishTime=" + finishTime +
                ", ordersAmount=" + ordersAmount +
                ", predepositAmount=" + predepositAmount +
                ", evaluationState=" + evaluationState +
                ", evaluationTime=" + evaluationTime +
                ", cancelReason=" + cancelReason +
                ", cancelTime=" + cancelTime +
                ", cancelRole='" + cancelRole + '\'' +
                ", adminReceivePayState=" + adminReceivePayState +
                ", deleteState=" + deleteState +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", takeTime=" + takeTime +
                ", clerkId=" + clerkId +
                ", clerkName='" + clerkName + '\'' +
                ", chainPaymentName='" + chainPaymentName + '\'' +
                ", ordersType=" + ordersType +
                ", createTimeDate='" + createTimeDate + '\'' +
                ", createTimeHour='" + createTimeHour + '\'' +
                '}';
    }
}