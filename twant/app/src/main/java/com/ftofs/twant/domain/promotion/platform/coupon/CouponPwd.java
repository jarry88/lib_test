package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;
import java.sql.Timestamp;

public class CouponPwd implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int pwdId;

    /**
     * 活动ID
     */
    private int activityId;

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 卡密
     */
    private String pwdCode = "";

    /**
     * 领取时间
     */
    private Timestamp activeTime;

    public int getPwdId() {
        return pwdId;
    }

    public void setPwdId(int pwdId) {
        this.pwdId = pwdId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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

    public String getPwdCode() {
        return pwdCode;
    }

    public void setPwdCode(String pwdCode) {
        this.pwdCode = pwdCode;
    }

    public Timestamp getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Timestamp activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CouponPwd{" +
                "pwdId=" + pwdId +
                ", activityId=" + activityId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", pwdCode='" + pwdCode + '\'' +
                ", activeTime=" + activeTime +
                '}';
    }
}