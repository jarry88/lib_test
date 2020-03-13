package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

public class CommentReplyItem {
    public int memberId;
    public String memberName;
    public int commentId;
    public String avatarUrl;
    public String nickname;
    public long createTime; // 毫秒
    public String content;
    public int isLike;
    public int commentLike; // 讚想數

    public boolean isQuoteReply;  // 是否為引用回覆，如果是引用回覆時，下面的字段才有用
    public String quoteNickname;  // 引用說說作者的昵稱
    public String quoteContent;   // 引用說說的內容

    public List<String> imageList = new ArrayList<>();
    public int commentRole;
}
