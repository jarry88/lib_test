package com.ftofs.twant.entity;


import android.support.annotation.NonNull;

public class ChatMessage {
    public static final int MY_MESSAGE = 1;
    public static final int YOUR_MESSAGE = 2;

    public String messageId;
    public int origin; // 我發的消息還是別人發的消息
    public boolean showTimestamp; // 是否顯示消息時間
    public long timestamp; // 毫秒
    public int messageType;
    public String fromMemberName;
    public String toMemberName;
    public String nickname;
    public String avatar;
    public String content;  // 如果是普通文本消息，則為消息內容；如果是商品，則為商品Id，商品名稱等結構數據

    @NonNull
    @Override
    public String toString() {
        return String.format("messageId[%s], origin[%d], timestamp[%d], messageType[%d], fromMemberName[%s], toMemberName[%s], nickname[%s], avatar[%s], content[%s]",
                messageId, origin, timestamp, messageType, fromMemberName, toMemberName, nickname, avatar, content);
    }
}
