package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.sql.Timestamp;

public class EmailCode implements Serializable {
    /**
     * 自增编码
     */
    private int logId;

    /**
     * 接收邮件
     */
    private String email = "";

    /**
     * 动态码
     */
    private String authCode = "";

    /**
     * 请求IP
     */
    private String logIp = "";

    /**
     * 邮件内容
     */
    private String content = "";

    /**
     * 邮件类型
     */
    private int sendType = 0;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 使用状态 0为未使用，1为已使用
     */
    private int usedState = 0;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getLogIp() {
        return logIp;
    }

    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getUsedState() {
        return usedState;
    }

    public void setUsedState(int usedState) {
        this.usedState = usedState;
    }

    @Override
    public String toString() {
        return "EmailCode{" +
                "logId=" + logId +
                ", email='" + email + '\'' +
                ", authCode='" + authCode + '\'' +
                ", logIp='" + logIp + '\'' +
                ", content='" + content + '\'' +
                ", sendType=" + sendType +
                ", addTime=" + addTime +
                ", usedState=" + usedState +
                '}';
    }
}
