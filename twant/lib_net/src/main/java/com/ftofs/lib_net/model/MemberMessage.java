package com.ftofs.lib_net.model;

import java.io.Serializable;

public class MemberMessage implements Serializable {
    /**
     * 消息编号
     */
    private int messageId;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 创建时间
     */
    private String addTime;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 是否已读
     */
    private int isRead = 0;

    /**
     * 消息模板分类
     * 会员    交易-1001 退换货-1002 物流-1003 资产-1004 招聘-1006
     * 商家    交易-2001 退换货-2002 產品-2003 运营-2004
     */
    private Integer tplClass;

    /**
     * 特定数据编号
     */
    private String sn;

    /**
     * 消息模板编码
     */
    private String tplCode;

    /**
     * 跳转URL
     */
    private String messageUrl;

    /**
     * 消息擴展json
     */
    private String messageJson;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Integer getTplClass() {
        return tplClass;
    }

    public void setTplClass(Integer tplClass) {
        this.tplClass = tplClass;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String messageJson) {
        this.messageJson = messageJson;
    }

    @Override
    public String toString() {
        return "MemberMessage{" +
                "messageId=" + messageId +
                ", messageContent='" + messageContent + '\'' +
                ", addTime=" + addTime +
                ", memberId=" + memberId +
                ", isRead=" + isRead +
                ", tplClass=" + tplClass +
                ", sn='" + sn + '\'' +
                ", tplCode='" + tplCode + '\'' +
                '}';
    }
}
