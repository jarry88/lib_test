package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.orm.FriendInfo;
import com.hyphenate.chat.EMMessage;

/**
 * 聊天會話
 * @author zwm
 */
public class ChatConversation implements MultiItemEntity {
    @Override
    public int getItemType() {
        return itemType;
    }

    /**
     * 交易物流消息
     */
    public static final int ITEM_TYPE_LOGISTICS = 1;
    /**
     * 退換貨消息
     */
    public static final int ITEM_TYPE_RETURN = 2;
    /**
     * IM聊天消息
     */
    public static final int ITEM_TYPE_IM = 3;


    // 會話列表中，最近一條消息的描述（如果是非文本消息的話）
    public static final String LAST_MESSAGE_DESC_IMAGE = "[圖片]";
    public static final String LAST_MESSAGE_DESC_ORDERS = "[訂單]";
    public static final String LAST_MESSAGE_DESC_GOODS = "[商品]";

    public ChatConversation(int itemType) {
        this.itemType = itemType;
        this.friendInfo = new FriendInfo();
    }

    public int itemType;
    public FriendInfo friendInfo;
    public int lastMessageType;
    public String lastMessage;  // 最近一條消息
    public long timestamp;  // 最近一條消息的時間
    public boolean isDoNotDisturb;  // 消息防打擾
    public int unreadCount; // 未讀消息條數
}
