package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.sql.Timestamp;

public class StoreMessage implements Serializable {
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
    private Timestamp addTime;

    /**
     * 卖家编号
     */
    private int sellerId;

    /**
     * 是否已读
     */
    private int isRead = 0;

    /**
     * 消息模板分类
     * 会员    交易-1001 退换货-1002 物流-1003 资产-1004
     * 商家    交易-2001 退换货-2002 商品-2003 运营-2004
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
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

    @Override
    public String toString() {
        return "StoreMessage{" +
                "messageId=" + messageId +
                ", messageContent='" + messageContent + '\'' +
                ", addTime=" + addTime +
                ", sellerId=" + sellerId +
                ", isRead=" + isRead +
                ", tplClass=" + tplClass +
                ", sn='" + sn + '\'' +
                ", tplCode='" + tplCode + '\'' +
                ", messageUrl='" + messageUrl + '\'' +
                '}';
    }
}
