package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PredepositRecharge implements Serializable {
    /**
     * 自增编码
     */
    private int rechargeId;

    /**
     * 充值编号
     */
    private String rechargeSn = "";

    /**
     * 会员编号
     */
    private int memberId = 0;

    /**
     * 充值金额
     */
    private BigDecimal amount = new BigDecimal(0);

    /**
     * 支付方式标识
     */
    private String paymentCode = "";

    /**
     * 支付方式名称
     */
    private String paymentName = "";

    /**
     * 支付使用终端(WAP,WEB,APP)
     */
    private String paymentClientType = "";

    /**
     * 第三方支付接口交易号
     */
    private String tradeSn = "";

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 支付状态（PredepositRechargePayState状态   0未支付  1已支付）
     */
    private int payState = 0;

    /**
     * 支付时间
     */
    private Timestamp payTime;

    /**
     * 操作审核的管理员编号
     */
    private int adminId = 0;

    /**
     * 操作审核的管理员
     */
    private String adminName = "";

    /**
     * 支付状态文本
     */
    private String payStateText = "";

    public int getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(int rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getRechargeSn() {
        return rechargeSn;
    }

    public void setRechargeSn(String rechargeSn) {
        this.rechargeSn = rechargeSn;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentClientType() {
        return paymentClientType;
    }

    public void setPaymentClientType(String paymentClientType) {
        this.paymentClientType = paymentClientType;
    }

    public String getTradeSn() {
        return tradeSn;
    }

    public void setTradeSn(String tradeSn) {
        this.tradeSn = tradeSn;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPayStateText() {
        return payStateText;
    }

    @Override
    public String toString() {
        return "PredepositRecharge{" +
                "rechargeId=" + rechargeId +
                ", rechargeSn='" + rechargeSn + '\'' +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", tradeSn='" + tradeSn + '\'' +
                ", addTime=" + addTime +
                ", payState=" + payState +
                ", payTime=" + payTime +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", payStateText='" + payStateText + '\'' +
                '}';
    }
}
