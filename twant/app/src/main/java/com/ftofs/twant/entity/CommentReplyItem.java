package com.ftofs.twant.entity;

public class CommentReplyItem {
    public int memberId;
    public String memberName;
    public int commentId;
    public String avatarUrl;
    public String nickname;
    public long createTime; // 毫秒
    public String content;
    public int isLike;
    public int commentLike; // 點贊數
}
