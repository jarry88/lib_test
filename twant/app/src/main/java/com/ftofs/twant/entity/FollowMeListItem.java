package com.ftofs.twant.entity;

/**
 * 【關注我的】列表Item
 */
public class FollowMeListItem {
    public FollowMeListItem(String memberName, String avatar) {
        this.memberName = memberName;
        this.avatar = avatar;
    }

    public String memberName;
    public String avatar;
}
