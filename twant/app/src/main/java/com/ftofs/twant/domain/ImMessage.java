package com.ftofs.twant.domain;

import java.io.Serializable;

public class ImMessage implements Serializable {
    /**
     * 消息编号
     */
    private int messageId;

    /**
     * 发送人id
     */
    private int fromUserId = 0;

    /**
     * 发送人名称
     */
    private String fromUserName = "";

    /**
     * 发送人类型
     * 1.买家 ， 2.卖家
     */
    private int fromUserType = 0;

    /**
     * 接收人id
     */
    private int toUserId = 0;

    /**
     * 接受人名称
     */
    private String toUserName = "";

    /**
     * 接收人类型
     * 1.买家 ， 2 .卖家
     */
    private int toUserType = 1;

    /**
     * 消息内容
     */
    private String messageContent = "";

    /**
     * 消息状态
     * 1.已读 ， 0 .未读
     */
    private Integer messageState = 0;

    /**
     * wap消息状态
     * 1.已读 ， 0 .未读
     */
    private Integer messageStateWap = 0;

    /**
     * android消息状态
     * 1.已读 ， 0 .未读
     */
    private Integer messageStateAndroid = 0;

    /**
     * ios消息状态
     * 1.已读 ， 0 .未读
     */
    private Integer messageStateIos = 0;

    /**
     * 消息类型
     * 0.普通消息 1.图片消息
     */
    private Integer messageType = 0;

    /**
     * 消息发送时间
     */
    private long addTime;

    /**
     * 消息发送时间文本
     */
    private String addTimeText;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public int getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(int fromUserType) {
        this.fromUserType = fromUserType;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getToUserType() {
        return toUserType;
    }

    public void setToUserType(int toUserType) {
        this.toUserType = toUserType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getMessageState() {
        return messageState;
    }

    public void setMessageState(Integer messageState) {
        this.messageState = messageState;
    }

    public Integer getMessageStateWap() {
        return messageStateWap;
    }

    public void setMessageStateWap(Integer messageStateWap) {
        this.messageStateWap = messageStateWap;
    }

    public Integer getMessageStateAndroid() {
        return messageStateAndroid;
    }

    public void setMessageStateAndroid(Integer messageStateAndroid) {
        this.messageStateAndroid = messageStateAndroid;
    }

    public Integer getMessageStateIos() {
        return messageStateIos;
    }

    public void setMessageStateIos(Integer messageStateIos) {
        this.messageStateIos = messageStateIos;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getAddTimeText() {
        return addTimeText;
    }

    public void setAddTimeText(String addTimeText) {
        this.addTimeText = addTimeText;
    }

    @Override
    public String toString() {
        return "ImMessage{" +
                "messageId=" + messageId +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                ", fromUserType=" + fromUserType +
                ", toUserId=" + toUserId +
                ", toUserName='" + toUserName + '\'' +
                ", toUserType=" + toUserType +
                ", messageContent='" + messageContent + '\'' +
                ", messageState=" + messageState +
                ", messageStateWap=" + messageStateWap +
                ", messageStateAndroid=" + messageStateAndroid +
                ", messageStateIos=" + messageStateIos +
                ", messageType=" + messageType +
                ", addTime=" + addTime +
                '}';
    }
}
