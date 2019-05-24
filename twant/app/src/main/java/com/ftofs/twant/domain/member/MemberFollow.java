package com.ftofs.twant.domain.member;

import java.io.Serializable;


public class MemberFollow implements Serializable{
    /**
     * 会员名称
     */
    private String memberName;

    /**
     *  被关注的会员
     */
    private String followMemberName;

    /**
     * 關注時間
     */
    private String createTime;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getFollowMemberName() {
        return followMemberName;
    }

    public void setFollowMemberName(String followMemberName) {
        this.followMemberName = followMemberName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MemberFollow{" +
                "memberName='" + memberName + '\'' +
                ", followMemberName='" + followMemberName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
