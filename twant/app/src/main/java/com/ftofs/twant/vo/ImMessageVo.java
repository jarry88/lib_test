package com.ftofs.twant.vo;

import com.ftofs.twant.domain.ImMessage;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 在线聊天vo
 *
 * @author cj
 * Created 2017-4-14 下午 6:19
 */
public class ImMessageVo {
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
     * 1,买家 ， 2 。卖家
     */
    private int fromUserType = 0;

    /**
     * 接收人id
     */
    private int  toUserId= 0;
    /**
     * 接受人名称
     */
    private String toUserName = "";
    /**
     * 接收人类型
     * 1,买家 ， 2 。卖家
     */
    private int  toUserType= 1;

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
     * 消息类型
     * 0.普通消息 1.图片消息
     */
    private Integer messageType = 0;

    /**
     * 消息发送时间
     */
    private long addTime;

    /**
     * 发信人店铺id
     */
    private int fromUserStoreId = 0;

    /**
     * 发信人店铺名称
     */
    private String fromUserStoreName = "";

    /**
     * 收信人店铺id
     */
    private int toUserStoreId = 0;

    /**
     * 收信人店铺名称
     */
    private String toUserStoreName = "";

    public ImMessageVo(ImMessage imMessage) {
        this.messageId = imMessage.getMessageId();
        this.fromUserId = imMessage.getFromUserId();
        this.fromUserName = imMessage.getFromUserName();
        this.fromUserType = imMessage.getFromUserType();
        this.toUserId = imMessage.getToUserId();
        this.toUserName = imMessage.getToUserName();
        this.toUserType = imMessage.getToUserType();
        this.messageContent = imMessage.getMessageContent();
        this.messageState = imMessage.getMessageState();
        this.messageType = imMessage.getMessageType();
        this.addTime = imMessage.getAddTime();
    }

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

    public int getFromUserStoreId() {
        return fromUserStoreId;
    }

    public void setFromUserStoreId(int fromUserStoreId) {
        this.fromUserStoreId = fromUserStoreId;
    }

    public String getFromUserStoreName() {
        return fromUserStoreName;
    }

    public void setFromUserStoreName(String fromUserStoreName) {
        this.fromUserStoreName = fromUserStoreName;
    }

    public int getToUserStoreId() {
        return toUserStoreId;
    }

    public void setToUserStoreId(int toUserStoreId) {
        this.toUserStoreId = toUserStoreId;
    }

    public String getToUserStoreName() {
        return toUserStoreName;
    }

    public void setToUserStoreName(String toUserStoreName) {
        this.toUserStoreName = toUserStoreName;
    }

    @Override
    public String toString() {
        return "ImMessageVo{" +
                "messageId=" + messageId +
                ", fromUserId=" + fromUserId +
                ", fromUserName='" + fromUserName + '\'' +
                ", fromUserType=" + fromUserType +
                ", toUserId=" + toUserId +
                ", toUserName='" + toUserName + '\'' +
                ", toUserType=" + toUserType +
                ", messageContent='" + messageContent + '\'' +
                ", messageState=" + messageState +
                ", messageType=" + messageType +
                ", addTime=" + addTime +
                ", fromUserStoreId=" + fromUserStoreId +
                ", fromUserStoreName='" + fromUserStoreName + '\'' +
                ", toUserStoreId=" + toUserStoreId +
                ", toUserStoreName='" + toUserStoreName + '\'' +
                '}';
    }
}
