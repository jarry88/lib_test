package com.ftofs.twant.domain.bargain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BargainOpenLog {
    /**
     * 记录编号
     */
    private int logId;

    /**
     * 开砍编号
     */
    private int openId;

    /**
     * 砍掉金额
     */
    private BigDecimal bargainPrice = BigDecimal.ZERO;

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
     * 创建时间
     */
    private Timestamp createTime;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getOpenId() {
        return openId;
    }

    public void setOpenId(int openId) {
        this.openId = openId;
    }

    public BigDecimal getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(BigDecimal bargainPrice) {
        this.bargainPrice = bargainPrice;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BargainOpenLog{" +
                "logId=" + logId +
                ", openId=" + openId +
                ", bargainPrice=" + bargainPrice +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
