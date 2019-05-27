package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.math.BigDecimal;

public class PredepositCash implements Serializable {
    /**
     * 自增编码
     */
    private int cashId;

    /**
     * 提现编号
     */
    private String cashSn = "";

    /**
     * 会员编号
     */
    private int memberId = 0;

    /**
     * 提现金额
     */
    private BigDecimal amount = new BigDecimal(0);

    /**
     * 收款公司比如支付宝、建行等
     */
    private String receiveCompany = "";

    /**
     * 收款账号
     */
    private String receiveAccount = "";

    /**
     * 开户人姓名
     */
    private String receiveUser = "";

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 状态（PredepositCashState状态 0未处理 1提现成功  2提现失败）
     */
    private int state = 0;

    /**
     * 操作审核的管理员编号
     */
    private int adminId = 0;

    /**
     * 操作审核的管理员
     */
    private String adminName = "";

    /**
     * 拒绝提现理由
     */
    private String refuseReason = "";

    /**
     * 状态文本
     */
    private String stateText = "";

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public String getCashSn() {
        return cashSn;
    }

    public void setCashSn(String cashSn) {
        this.cashSn = cashSn;
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

    public String getReceiveCompany() {
        return receiveCompany;
    }

    public void setReceiveCompany(String receiveCompany) {
        this.receiveCompany = receiveCompany;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getStateText() {
        return stateText;
    }

    @Override
    public String toString() {
        return "PredepositCash{" +
                "cashId=" + cashId +
                ", cashSn='" + cashSn + '\'' +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", receiveCompany='" + receiveCompany + '\'' +
                ", receiveAccount='" + receiveAccount + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", addTime=" + addTime +
                ", payTime=" + payTime +
                ", state=" + state +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", refuseReason='" + refuseReason + '\'' +
                ", stateText='" + stateText + '\'' +
                '}';
    }
}