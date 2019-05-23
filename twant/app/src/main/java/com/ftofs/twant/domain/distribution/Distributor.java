package com.ftofs.twant.domain.distribution;

import java.io.Serializable;
import java.math.BigDecimal;


public class Distributor implements Serializable {
    /**
     * 会员分销商编号
     */
    private int distributorId;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 绑定的手机号 , 只记申请时的
     */
    private String bindPhone;

    /**
     * 绑定的邮箱 , 只记申请时的
     */
    private String bindEmail;

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
     * 银行开户名
     */
    private String bankAccountName;

    /**
     * 银行收款人姓名
     */
    private String payPerson ;

    /**
     * 银行账号
     */
    private String bankAccountNumber;

    /**
     * 账户类型
     * bank ,alipay
     */
    private String accountType;

    /**
     * 分销商状态
     * 1.正常 2.删除
     */
    private int state = 1;

    /**
     * 入住时间
     */
    private String joininTime;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    /**
     * 分销单数
     */
    private int distributionOrdersCount = 0;

    /**
     * 已结算佣金
     */
    private BigDecimal payCommission = new BigDecimal(0);

    /**
     * 未结算佣金
     */
    private BigDecimal unpayCommission = new BigDecimal(0);

    /**
     * 分销金额
     */
    private BigDecimal distributionAmount = new BigDecimal(0);

    /**
     * 可提现佣金
     */
    private BigDecimal commissionAvailable = new BigDecimal(0);

    /**
     * 冻结佣金金额
     */
    private BigDecimal commissionFreeze  = new BigDecimal(0);

    /**
     *默认的选品库分组编号
     */
    private int defaultFavoritesId  = 0;

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
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

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getBindEmail() {
        return bindEmail;
    }

    public void setBindEmail(String bindEmail) {
        this.bindEmail = bindEmail;
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

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getDistributionOrdersCount() {
        return distributionOrdersCount;
    }

    public void setDistributionOrdersCount(int distributionOrdersCount) {
        this.distributionOrdersCount = distributionOrdersCount;
    }

    public BigDecimal getPayCommission() {
        return payCommission;
    }

    public void setPayCommission(BigDecimal payCommission) {
        this.payCommission = payCommission;
    }

    public BigDecimal getUnpayCommission() {
        return unpayCommission;
    }

    public void setUnpayCommission(BigDecimal unpayCommission) {
        this.unpayCommission = unpayCommission;
    }

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
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

    public BigDecimal getCommissionAvailable() {
        return commissionAvailable;
    }

    public void setCommissionAvailable(BigDecimal commissionAvailable) {
        this.commissionAvailable = commissionAvailable;
    }

    public BigDecimal getCommissionFreeze() {
        return commissionFreeze;
    }

    public void setCommissionFreeze(BigDecimal commissionFreeze) {
        this.commissionFreeze = commissionFreeze;
    }

    public int getDefaultFavoritesId() {
        return defaultFavoritesId;
    }

    public void setDefaultFavoritesId(int defaultFavoritesId) {
        this.defaultFavoritesId = defaultFavoritesId;
    }

    @Override
    public String toString() {
        return "Distributor{" +
                "distributorId=" + distributorId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", bindPhone='" + bindPhone + '\'' +
                ", bindEmail='" + bindEmail + '\'' +
                ", realName='" + realName + '\'' +
                ", idCartNumber='" + idCartNumber + '\'' +
                ", idCartFrontImage='" + idCartFrontImage + '\'' +
                ", idCartBackImage='" + idCartBackImage + '\'' +
                ", idCartHandImage='" + idCartHandImage + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", payPerson='" + payPerson + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", state=" + state +
                ", joininTime=" + joininTime +
                ", lastLoginTime=" + lastLoginTime +
                ", distributionOrdersCount=" + distributionOrdersCount +
                ", payCommission=" + payCommission +
                ", unpayCommission=" + unpayCommission +
                ", distributionAmount=" + distributionAmount +
                ", commissionAvailable=" + commissionAvailable +
                ", commissionFreeze=" + commissionFreeze +
                ", defaultFavoritesId=" + defaultFavoritesId +
                '}';
    }
}

