package com.ftofs.twant.domain.distribution;

import java.io.Serializable;
import java.math.BigDecimal;


public class DistributionCommissionCash implements Serializable {
    /**
     * 自增编码
     */
    private int cashId;

    /**
     * 提现编号
     */
    private String cashSn = "";

    /**
     * 推广商编号
     */
    private int distributorId = 0;

    /**
     * 会员编号
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 提现金额
     */
    private BigDecimal amount = new BigDecimal(0);

    /**
     * 绑定的手机号 , 只记申请时的
     */
    private String bindPhone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCartNumber;

    /**
     * 账户类型
     * bank ,alipay
     */
    private String accountType;

    /**
     * 银行支行名称
     */
    private String bankAccountName;

    /**
     * 银行收款人姓名
     */
    private String payPerson;

    /**
     * 银行账号
     */
    private String bankAccountNumber;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 状态（状态 0未处理 1提现成功  2提现失败）
     */
    private int state = 0;

    /**
     * 操作审核的管理员编号
     */
    private int adminId;

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

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getPayPerson() {
        return payPerson;
    }

    public void setPayPerson(String payPerson) {
        this.payPerson = payPerson;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getStateText() {
        return stateText;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCartNumber() {
        return idCartNumber;
    }

    public void setIdCartNumber(String idCartNumber) {
        this.idCartNumber = idCartNumber;
    }

    @Override
    public String toString() {
        return "DistributionCommissionCash{" +
                "cashId=" + cashId +
                ", cashSn='" + cashSn + '\'' +
                ", distributorId=" + distributorId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", amount=" + amount +
                ", bindPhone='" + bindPhone + '\'' +
                ", realName='" + realName + '\'' +
                ", idCartNumber='" + idCartNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", payPerson='" + payPerson + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
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