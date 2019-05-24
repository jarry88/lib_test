package com.ftofs.twant.domain.complain;


import java.text.SimpleDateFormat;

public class ComplainTalk {
    /**
     * 主键
     */
    private int talkId;

    /**
     * 投诉Id
     */
    private int complainId;

    /**
     * 发言人id(买家Id/商家id/管理员Id)
     */
    private int userId;

    /**
     * 发言人名(买家用户名/商家店铺名/管理员登录名)
     */
    private String talkUser;

    /**
     * 对话类型
     */
    private int talkType;

    /**
     * 对话内容
     */
    private String talkContent;

    /**
     * 对话时间
     */
    private String talkTime;

    /**
     * 是否被禁止
     */
    private int disableState = 0;

    /**
     * 对话角色
     */
    private String talkRole;

    private String talkDate;

    public int getTalkId() {
        return talkId;
    }

    public void setTalkId(int talkId) {
        this.talkId = talkId;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTalkUser() {
        return talkUser;
    }

    public void setTalkUser(String talkUser) {
        this.talkUser = talkUser;
    }

    public int getTalkType() {
        return talkType;
    }

    public void setTalkType(int talkType) {
        this.talkType = talkType;
    }

    public String getTalkContent() {
        return talkContent;
    }

    public void setTalkContent(String talkContent) {
        this.talkContent = talkContent;
    }

    public String getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(String talkTime) {
        this.talkTime = talkTime;
    }

    public String getTalkRole() {
        return talkRole;
    }

    public void setTalkRole(String talkRole) {
        this.talkRole = talkRole;
    }

    public int getDisableState() {
        return disableState;
    }

    public void setDisableState(int disableState) {
        this.disableState = disableState;
    }

    public String getTalkDate() {
        return new SimpleDateFormat("yyyy/MM/dd").format(talkTime);
    }

    public void setTalkDate(String talkDate) {
        this.talkDate = talkDate;
    }

    @Override
    public String toString() {
        return "ComplainTalk{" +
                "talkId=" + talkId +
                ", complainId=" + complainId +
                ", userId=" + userId +
                ", talkUser='" + talkUser + '\'' +
                ", talkType=" + talkType +
                ", talkContent='" + talkContent + '\'' +
                ", talkTime=" + talkTime +
                ", disableState=" + disableState +
                ", talkRole='" + talkRole + '\'' +
                ", talkDate='" + talkDate + '\'' +
                '}';
    }
}
