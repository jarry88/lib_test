package com.ftofs.twant.domain.show;

import java.io.Serializable;
import java.math.BigDecimal;


public class ShowOrdersReward implements Serializable {
    /**
     * 主键、自增
     */
    private Integer rewardId;

    /**
     * 支付单号
     */
    private long paySn;

    /**
     * 支付方式代码
     */
    private String payCode = "";

    /**
     * 客户端标识
     */
    private String clientType;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付成功标识
     */
    private int payState = 0;

    /**
     * 支付金额
     */
    private BigDecimal payAmount = new BigDecimal(0);

    /**
     * 作者酬金
     */
    private BigDecimal authorFee = new BigDecimal(0);

    /**
     * 平台酬金
     */
    private BigDecimal siteFee = new BigDecimal(0);

    /**
     * 网站手续费
     */
    private int rewardCommission = 0;

    /**
     * 消费方会员ID
     */
    private int memberId;

    /**
     * 消费方会员名称
     */
    private String memberName;

    /**
     * 晒宝id
     */
    private Integer showOrdersId;

    /**
     * 晒宝标题
     */
    private String showOrdersTitle;

    /**
     * 作者会员ID
     */
    private int authorMemberId;

    /**
     * 作者会员名称
     */
    private String authorMemberName;

    public Integer getRewardId() {
        return rewardId;
    }

    public void setRewardId(Integer rewardId) {
        this.rewardId = rewardId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
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

    public Integer getShowOrdersId() {
        return showOrdersId;
    }

    public void setShowOrdersId(Integer showOrdersId) {
        this.showOrdersId = showOrdersId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getShowOrdersTitle() {
        return showOrdersTitle;
    }

    public void setShowOrdersTitle(String showOrdersTitle) {
        this.showOrdersTitle = showOrdersTitle;
    }

    public int getAuthorMemberId() {
        return authorMemberId;
    }

    public void setAuthorMemberId(int authorMemberId) {
        this.authorMemberId = authorMemberId;
    }

    public String getAuthorMemberName() {
        return authorMemberName;
    }

    public void setAuthorMemberName(String authorMemberName) {
        this.authorMemberName = authorMemberName;
    }

    public BigDecimal getAuthorFee() {
        return authorFee;
    }

    public void setAuthorFee(BigDecimal authorFee) {
        this.authorFee = authorFee;
    }

    public BigDecimal getSiteFee() {
        return siteFee;
    }

    public void setSiteFee(BigDecimal siteFee) {
        this.siteFee = siteFee;
    }

    public int getRewardCommission() {
        return rewardCommission;
    }

    public void setRewardCommission(int rewardCommission) {
        this.rewardCommission = rewardCommission;
    }

    @Override
    public String toString() {
        return "ShowOrdersReward{" +
                "rewardId=" + rewardId +
                ", paySn=" + paySn +
                ", payCode='" + payCode + '\'' +
                ", clientType='" + clientType + '\'' +
                ", payTime=" + payTime +
                ", payState=" + payState +
                ", payAmount=" + payAmount +
                ", authorFee=" + authorFee +
                ", siteFee=" + siteFee +
                ", rewardCommission=" + rewardCommission +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", showOrdersId=" + showOrdersId +
                ", showOrdersTitle='" + showOrdersTitle + '\'' +
                ", authorMemberId=" + authorMemberId +
                ", authorMemberName='" + authorMemberName + '\'' +
                '}';
    }
}
