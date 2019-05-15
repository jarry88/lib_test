package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class MemberMessageSetting implements Serializable {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 消息模板编号
     */
    private String tplCode;

    /**
     * 是否接收
     */
    private int isReceive;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    @Override
    public String toString() {
        return "MemberMessageSetting{" +
                "memberId=" + memberId +
                ", tplCode='" + tplCode + '\'' +
                ", isReceive=" + isReceive +
                '}';
    }
}
