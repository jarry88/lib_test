package com.ftofs.twant.domain.distribution;

import java.io.Serializable;


public class DistributorJoin implements Serializable {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCartNumber;

    /**
     * 身份证正面照
     */
    private String idCartFrontImage;

    /**
     * 身份证反面照
     */
    private String idCartBackImage;

    /**
     * 手持身份证照
     */
    private String idCartHandImage;

    /**
     * 银行收款人姓名
     */
    private String payPerson;

    /**
     * 银行开户名
     */
    private String bankAccountName;

    /**
     * 收款帐号
     */
    private String bankAccountNumber;

    /**
     * 账户类型
     */
    private String accountType;

    /**
     * 审核状态
     */
    private int state = 0;

    /**
     * 管理员审核信息
     */
    private String joininMessage = "";

    /**
     * 申请提交时间
     */
    private String joininTime;

    /**
     * 处理时间
     */
    private String handleTime;

    /**
     *  身份证正面照 图片地址
     */
    private String idCartFrontImageUrl = "";

    /**
     *  身份证反面照 图片地址
     */
    private String idCartBackImageUrl = "";

    /**
     * 手持身份证 图片地址
     */
    private String idCartHandImageUrl = "";

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

    public String getIdCartNumber() {
        return idCartNumber;
    }

    public void setIdCartNumber(String idCartNumber) {
        this.idCartNumber = idCartNumber;
    }

    public String getIdCartFrontImage() {
        return idCartFrontImage;
    }

    public void setIdCartFrontImage(String idCartFrontImage) {
        this.idCartFrontImage = idCartFrontImage;
    }

    public String getIdCartBackImage() {
        return idCartBackImage;
    }

    public void setIdCartBackImage(String idCartBackImage) {
        this.idCartBackImage = idCartBackImage;
    }

    public String getIdCartHandImage() {
        return idCartHandImage;
    }

    public void setIdCartHandImage(String idCartHandImage) {
        this.idCartHandImage = idCartHandImage;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getJoininTime() {
        return joininTime;
    }

    public void setJoininTime(String joininTime) {
        this.joininTime = joininTime;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getJoininMessage() {
        return joininMessage;
    }

    public void setJoininMessage(String joininMessage) {
        this.joininMessage = joininMessage;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPayPerson() {
        return payPerson;
    }

    public void setPayPerson(String payPerson) {
        this.payPerson = payPerson;
    }


    public String getIdCartFrontImageUrl() {
        return idCartFrontImage;
    }

    public String getIdCartBackImageUrl() {
        return idCartBackImage;
    }

    public String getIdCartHandImageUrl() {
        return idCartHandImage;
    }

    @Override
    public String toString() {
        return "DistributorJoin{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", realName='" + realName + '\'' +
                ", idCartNumber='" + idCartNumber + '\'' +
                ", idCartFrontImage='" + idCartFrontImage + '\'' +
                ", idCartBackImage='" + idCartBackImage + '\'' +
                ", idCartHandImage='" + idCartHandImage + '\'' +
                ", payPerson='" + payPerson + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", state=" + state +
                ", joininMessage='" + joininMessage + '\'' +
                ", joininTime=" + joininTime +
                ", handleTime=" + handleTime +
                '}';
    }
}

