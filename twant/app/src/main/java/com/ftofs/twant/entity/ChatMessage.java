package com.ftofs.twant.entity;

import com.hyphenate.chat.EMMessage;

public class ChatMessage {
    public static final int MY_MESSAGE = 1;
    public static final int YOUR_MESSAGE = 2;

    public String messageId;
    public int origin; // 我發的消息還是別人發的消息
    public long timestamp; // 毫秒
    public EMMessage.Type messageType;
    public String memberName;
    public String nickname;
    public String avatar;
    public String content;
}
