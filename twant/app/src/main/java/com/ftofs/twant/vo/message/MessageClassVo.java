package com.ftofs.twant.vo.message;

import com.ftofs.twant.domain.MessageTemplateCommon;
import com.ftofs.twant.domain.member.MemberMessage;
import com.ftofs.twant.domain.store.StoreMessage;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消息类型
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:13
 */
public class MessageClassVo {
    private int id;
    private String name;
    //Modify By yangjian 2019/5/9 12:12 description 新增icon
    private String icon;
    private List<MessageTemplateCommon> messageTemplateCommonList;
    private MemberMessage memberMessage;
    private StoreMessage storeMessage;
    private long messageUnreadCount = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MessageTemplateCommon> getMessageTemplateCommonList() {
        return messageTemplateCommonList;
    }

    public long getMessageUnreadCount() {
        return messageUnreadCount;
    }

    public void setMessageUnreadCount(long messageUnreadCount) {
        this.messageUnreadCount = messageUnreadCount;
    }

    public MemberMessage getMemberMessage() {
        return memberMessage;
    }

    public StoreMessage getStoreMessage() {
        return storeMessage;
    }

    public void setStoreMessage(StoreMessage storeMessage) {
        this.storeMessage = storeMessage;
    }

    public void setMemberMessage(MemberMessage memberMessage) {
        this.memberMessage = memberMessage;
    }

    public void setMessageTemplateCommonList(List<MessageTemplateCommon> messageTemplateCommonList) {
        this.messageTemplateCommonList = messageTemplateCommonList;
    }

    @Override
    public String toString() {
        return "MessageClassVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", messageTemplateCommonList=" + messageTemplateCommonList +
                ", memberMessage=" + memberMessage +
                ", storeMessage=" + storeMessage +
                ", messageUnreadCount=" + messageUnreadCount +
                '}';
    }
}
