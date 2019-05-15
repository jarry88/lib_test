package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberSignin implements Serializable {
    /**
     * 会员自增编码
     */
    private int signinId;

    /**
     * 会员id
     */
    private int signinMemberId;

    /**
     * 会员名称
     */
    private String signinMemberName = "";

    /**
     * 会员头像
     */
    private String signinMemberAvatar = "";

    /**
     * 添加时间
     */
    private Timestamp signinAddTime;

    /**
     * 添加积分点数
     */
    private int signinPoints;

    /**
     * 连续签到天数
     */
    private int signinDays;

    /**
     * 头像路径
     */
    private String signinMemberAvatarUrl = "";

    public int getSigninId() {
        return signinId;
    }

    public void setSigninId(int signinId) {
        this.signinId = signinId;
    }

    public int getSigninMemberId() {
        return signinMemberId;
    }

    public void setSigninMemberId(int signinMemberId) {
        this.signinMemberId = signinMemberId;
    }

    public String getSigninMemberName() {
        return signinMemberName;
    }

    public void setSigninMemberName(String signinMemberName) {
        this.signinMemberName = signinMemberName;
    }

    public Timestamp getSigninAddTime() {
        return signinAddTime;
    }

    public void setSigninAddTime(Timestamp signinAddTime) {
        this.signinAddTime = signinAddTime;
    }

    public int getSigninPoints() {
        return signinPoints;
    }

    public void setSigninPoints(int signinPoints) {
        this.signinPoints = signinPoints;
    }

    public int getSigninDays() {
        return signinDays;
    }

    public void setSigninDays(int signinDays) {
        this.signinDays = signinDays;
    }

    public long getSigninAddTimeUnix() {
        return this.signinAddTime.getTime() /1000 ;
    }

    public String getSigninMemberAvatar() {
        return signinMemberAvatar;
    }

    public void setSigninMemberAvatar(String signinMemberAvatar) {
        this.signinMemberAvatar = signinMemberAvatar;
    }

    public String getSigninMemberAvatarUrl() {
        return signinMemberAvatarUrl;
    }

    public void setSigninMemberAvatarUrl(String signinMemberAvatarUrl) {
        this.signinMemberAvatarUrl = signinMemberAvatarUrl;
    }



    @Override
    public String toString() {
        return "MemberSignin{" +
                "signinId=" + signinId +
                ", signinMemberId=" + signinMemberId +
                ", signinMemberName='" + signinMemberName + '\'' +
                ", signinMemberAvatar='" + signinMemberAvatar + '\'' +
                ", signinAddTime=" + signinAddTime +
                ", signinPoints=" + signinPoints +
                ", signinDays=" + signinDays +
                ", signinMemberAvatarUrl='" + signinMemberAvatarUrl + '\'' +
                '}';
    }
}
