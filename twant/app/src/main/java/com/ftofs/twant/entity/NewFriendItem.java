package com.ftofs.twant.entity;

public class NewFriendItem {
    public static final int STATUS_INITIAL = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_DENIED = 2;

    public String memberName;
    public String avatarUrl;
    public String nickname;
    public String remark;    // 驗證消息
    public int status;  // -1: 不是好友 0: 請求好友(顯示【接受】按鈕) 1:為好友(顯示【已添加】)  3: 未處理(顯示同意添加) 4: 請求好友中（繼續顯示添加好友）
}
