package com.ftofs.twant.domain.bargain;

import java.math.BigDecimal;

public class BargainOpen {
    /**
     * 开砍编号
     */
    private int openId;

    /**
     * 砍价活动编号
     */
    private int bargainId;

    /**
     * 砍过后的价格
     */
    private BigDecimal openPrice;

    /**
     * 会员自增编码
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 头像
     */
    private String avatar = "";

    /**
     * 头像路径
     */
    private String avatarUrl = "";

    /**
     * 开始时间
     */
    private String createTime;

    /**
     * 支付状态
     */
    private int paymentState = 0;

    /**
     * 砍价次数
     */
    private int bargainTimes= 1;

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }

    public int getBargainId() {
        return bargainId;
    }

    public void setBargainId(int bargainId) {
        this.bargainId = bargainId;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(int paymentState) {
        this.paymentState = paymentState;
    }

    public int getBargainTimes() {
        return bargainTimes;
    }

    public void setBargainTimes(int bargainTimes) {
        this.bargainTimes = bargainTimes;
    }

    @Override
    public String toString() {
        return "BargainOpen{" +
                "openId=" + openId +
                ", bargainId=" + bargainId +
                ", openPrice=" + openPrice +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", createTime=" + createTime +
                ", paymentState=" + paymentState +
                ", bargainTimes=" + bargainTimes +
                '}';
    }
}
