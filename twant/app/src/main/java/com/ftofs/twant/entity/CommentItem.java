package com.ftofs.twant.entity;

public class CommentItem {
    public int commentId;
    public int commentType;
    public int commentChannel;

    public String content;
    public int isLike; // 是否點贊
    public int commentLike; // 點贊數
    public int commentReply; // 回復數

    public String commenterAvatar;
    public String nickname;
    public String commentTime;
    public String imageUrl;
}
