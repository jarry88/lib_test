package com.ftofs.twant.entity;

/**
 * 我的好友列表Item
 * @author zwm
 */
public class MyFriendListItem {
    public MyFriendListItem(String memberName, String avatarUrl, String nickname, int level, String memberSignature) {
        this.memberName = memberName;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.level = level;
        this.memberSignature = memberSignature;
    }

    public String memberName;
    public String avatarUrl;
    public String nickname;
    public int level;
    public String memberSignature;
}
