package com.ftofs.twant.vo.im;

import com.ftofs.twant.domain.im.ImGroupDetail;
import com.ftofs.twant.domain.im.ImMemberStatus;
import com.ftofs.twant.vo.store.StoreVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 最近會話信息
 * @Auther: yangjian
 * @Date: 2018/11/10 18:26
 */
public class ImRecentContactsVo {
    /**
     * 消息id
     */
    private int id;
    /**
     * 發送方
     */
    private String sendFrom;
    /**
     * 接收方
     */
    private String target;
    /**
     * 接收方類型
     */
    private String targetType;
    /**
     * 群名稱
     */
    private String groupName;
    /**
     * 聯繫人暱稱
     */
    private String nickName;
    /**
     * 聯繫人會員名稱
     */
    private String memberName;
    /**
     * 接收方消息狀態
     */
    private ImMemberStatus imMemberStatus;
    /**
     * 接收方頭像
     */
    private String targetAvatar = "";
    /**
     * 最後一條消息
     */
    private String messageContent = "";
    /**
     * 最後一條消息類型
     */
    private String messageType ;
    /**
     * 消息是否已讀狀態
     */
    private String messageStatus;
    /**
     * 最後發送時間
     */

    private Timestamp sendTime;
    /**
     * 指定時間段或者指定條數的消息記錄
     */
    private List<ImMessageLogVo> imMessageLogList;
    /**
     * 群信息
     */
    private List<ImGroupDetail> imGroupDetailList;

    /**
     * 店鋪群信息
     */
    private StoreVo storeInfo;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTargetAvatar() {
        return targetAvatar;
    }

    public void setTargetAvatar(String targetAvatar) {
        this.targetAvatar = targetAvatar;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public List<ImMessageLogVo> getImMessageLogList() {
        return imMessageLogList;
    }

    public void setImMessageLogList(List<ImMessageLogVo> imMessageLogList) {
        this.imMessageLogList = imMessageLogList;
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

    public List<ImGroupDetail> getImGroupDetailList() {
        return imGroupDetailList;
    }

    public void setImGroupDetailList(List<ImGroupDetail> imGroupDetailList) {
        this.imGroupDetailList = imGroupDetailList;
    }

    public ImMemberStatus getImMemberStatus() {
        return imMemberStatus;
    }

    public void setImMemberStatus(ImMemberStatus imMemberStatus) {
        this.imMemberStatus = imMemberStatus;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public StoreVo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreVo storeInfo) {
        this.storeInfo = storeInfo;
    }
}
