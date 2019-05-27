package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class SmsCode implements Serializable {
    /**
     * 自增编码
     */
    private int logId;

    /**
     * 接收手机号
     */
    private String mobilePhone = "";

    /**
     * 短信动态码
     */
    private String authCode = "";

    /**
     * 请求IP
     */
    private String logIp = "";

    /**
     * 短信内容
     */
    private String content = "";

    /**
     * 短信类型
     */
    private int sendType = 0;

    /**
     * 添加时间
     */
    private String addTime;

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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
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
        return "SmsCode{" +
                "logId=" + logId +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", authCode='" + authCode + '\'' +
                ", logIp='" + logIp + '\'' +
                ", content='" + content + '\'' +
                ", sendType=" + sendType +
                ", addTime=" + addTime +
                ", usedState=" + usedState +
                '}';
    }
}
