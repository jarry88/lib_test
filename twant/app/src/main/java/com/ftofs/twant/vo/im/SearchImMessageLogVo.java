package com.ftofs.twant.vo.im;



/**
 * @author liusf
 * @create 2019/3/21 12:06
 * @description IM聊天記錄，用于搜索引擎建立索引
 */
public class SearchImMessageLogVo {
    /**
     * 自增编码
     */
    private int id;

    /**
     * 發送人
     */
    private String sendFrom;

    /**
     * 接收人（群Id/多人/單人)
     */
    private String target;

    /**
     * 目標類型(users/chatgroups/chatrooms)
     */
    private String targetType;

    /**
     * 群名
     */
    private String groupName = "";

    /**
     * 消息內容
     */
    private String messageContent;

    /**
     * 消息類型(txt/img/emoji/file/audio/video)
     */
    private String messageType;

    /**
     * 發送時間
     */
    private String sendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
