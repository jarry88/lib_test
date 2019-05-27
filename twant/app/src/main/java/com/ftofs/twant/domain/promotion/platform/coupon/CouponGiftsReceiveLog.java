package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponGiftsReceiveLog implements Serializable,Cloneable {
    /**
     * 自增ID
     */
    private int logId;

    /**
     * 礼包ID
     */
    private int giftsId;

    /**
     * 礼包名称
     */
    private String giftsName = "";

    /**
     * 礼包面值
     */
    private BigDecimal giftsPrice = new BigDecimal(0);

    /**
     * 领取时间
     */
    private String activeTime;

    /**
     * 拥有者会员ID
     */
    private int memberId = 0;

    /**
     * 拥有者会员名称
     */
    private String memberName = "";

    /**
     * 日志内容
     */
    private String logContent = "";


    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getGiftsId() {
        return giftsId;
    }

    public void setGiftsId(int giftsId) {
        this.giftsId = giftsId;
    }

    public String getGiftsName() {
        return giftsName;
    }

    public void setGiftsName(String giftsName) {
        this.giftsName = giftsName;
    }

    public BigDecimal getGiftsPrice() {
        return giftsPrice;
    }

    public void setGiftsPrice(BigDecimal giftsPrice) {
        this.giftsPrice = giftsPrice;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
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

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CouponGiftsReceiveLog{" +
                "logId=" + logId +
                ", giftsId=" + giftsId +
                ", giftsName='" + giftsName + '\'' +
                ", giftsPrice=" + giftsPrice +
                ", activeTime=" + activeTime +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", logContent='" + logContent + '\'' +
                '}';
    }
}
