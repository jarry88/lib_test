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

    public boolean isQuoteReply;  // 是否為引用回復，如果是引用回復時，下面的字段才有用
    public String quoteNickname;  // 引用評論作者的昵稱
    public String quoteContent;   // 引用評論的內容
}
