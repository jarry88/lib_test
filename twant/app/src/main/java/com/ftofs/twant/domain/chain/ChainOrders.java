package com.ftofs.twant.domain.chain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChainOrders implements Serializable, Cloneable {
    /**
     * 订单主键ID、自增
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 订单状态
     * 0-已取消 10-未支付 25-已支付 40-已取货
     */
    private int ordersState;

    /**
     * 支付单ID
     */
    private int payId;

    /**
     * 支付单号
     */
    private long paySn;

    /**
     * 会员ID
     */
    private int memberId;

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
     * 收货人地址
     */
    private String receiverAddress = "";

    /**
     * 下单时间
     */
    private String createTime;

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
    private String paymentTime;

    /**
     * 订单完成时间
     */
    private String finishTime;

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
    private String evaluationTime;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票抬头
     */
    private String invoiceContent;

    /**
     * 纳税人识别号或统一社会信用代码
     */
    private String invoiceCode;

    /**
     * 订单取消原因
     */
    private Integer cancelReason = 6;

    /**
     * 关闭时间
     */
    private String cancelTime;

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
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 预计取货时间
     */
    private String takeTime;

    /**
     * 店员ID
     */
    private int clerkId;

    /**
     * 店员
     */
    private String clerkName;

    /**
     * 门店付款的具体付款方式
     */
    private String chainPaymentName;

    /**
     * 门店订单类型
     */
    private int ordersType;

    /**
     * 送货时间
     */
    private String sendTime;

    /**
     * 订单下的商品使用的平台券总金额
     */
    private BigDecimal couponAmount = new BigDecimal(0);

    /**
     * 订单的商品使用的平台券总金额中平台应承担的总金额
     */
    private BigDecimal shopCommitmentAmount = BigDecimal.ZERO;

    /**
     * 买家留言
     */
    private String receiverMessage = "";

    /**
     * 在线付款支付方式名
     */
    private String paymentName;

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

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
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

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
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

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    /**
     * 订单状态
     * @return
     */
    public String getOrdersStateName() {
        return String.valueOf(ordersState);
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
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

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getPaymentName() {
        return paymentCode.toString();
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Integer getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Integer cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return
     */
    public String getCancelReasonContent() {
        return cancelReason.toString();

    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    /**
     * 自动取消时间
     * @return
     */
    public String getAutoCancelTime() {
        return null;
    }

    public String getCancelRole() {
        return cancelRole;
    }

    public void setCancelRole(String cancelRole) {
        this.cancelRole = cancelRole;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getShopCommitmentAmount() {
        return shopCommitmentAmount;
    }

    public void setShopCommitmentAmount(BigDecimal shopCommitmentAmount) {
        this.shopCommitmentAmount = shopCommitmentAmount;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChainOrders{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
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
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
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
                ", sendTime='" + sendTime + '\'' +
                ", couponAmount=" + couponAmount +
                ", shopCommitmentAmount=" + shopCommitmentAmount +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", paymentName='" + paymentName + '\'' +
                '}';
    }
}
