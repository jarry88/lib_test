package com.ftofs.twant.entity;

public class NewFriendItem {
    public static final int STATUS_INITIAL = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_DENIED = 2;

    public String memberName;
    public String avatarUrl;
    public String nickname;
    public String remark;    // 驗證消息
    public int status;
}
