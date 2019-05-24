package com.ftofs.twant.domain.promotion;

import java.io.Serializable;


public class VoucherPwd implements Serializable {
    /**
     * 自增编码
     */
    private int pwdId;

    /**
     * 活动ID
     */
    private int templateId = 0;

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
    private String activeTime;

    public int getPwdId() {
        return pwdId;
    }

    public void setPwdId(int pwdId) {
        this.pwdId = pwdId;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
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

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public String toString() {
        return "VoucherPwd{" +
                "pwdId=" + pwdId +
                ", templateId=" + templateId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", pwdCode='" + pwdCode + '\'' +
                ", activeTime=" + activeTime +
                '}';
    }
}
