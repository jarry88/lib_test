package com.ftofs.twant.entity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;

/**
 * 聊天會話
 * @author zwm
 */
public class ChatConversation implements Cloneable{
    public ChatConversation() {
        this.friendInfo = new FriendInfo();
    }

    public ChatConversation(FriendInfo friendInfo, String messageTime) {
        this.messageTime = messageTime;
        this.friendInfo = friendInfo;
    }
    // 會話列表中，最近一條消息的描述（如果是非文本消息的話）
    public static final String LAST_MESSAGE_DESC_IMAGE = "[圖片]";
    public static final String LAST_MESSAGE_DESC_ORDERS = "[訂單]";
    public static final String LAST_MESSAGE_DESC_GOODS = "[產品]";
    public static final String LAST_MESSAGE_DESC_ENC = "[電子名片]";
    public String sendTime="";


    //淺克隆

    @NonNull
    @Override
    public ChatConversation clone(){
        ChatConversation chatConversation = null;
        try {
            chatConversation = (ChatConversation) super.clone();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        return chatConversation;
    }

    public FriendInfo friendInfo;
    public String messageTime ;
    public int lastMessageType;
    public String lastMessage;  // 最近一條消息
    public long timestamp;  // 最近一條消息的時間
    public boolean isDoNotDisturb;  // 消息防打擾
    public int unreadCount; // 未讀消息條數
    public boolean isPlatformCustomer; // 是否為置頂的平台客服對話總聊天入口

    public int getUnreadCount() {
        return unreadCount;
    }
    public long getTimeStamp() {
        return timestamp;
    }
}