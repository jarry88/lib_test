package com.ftofs.twant.entity;


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
    public String content;
}
