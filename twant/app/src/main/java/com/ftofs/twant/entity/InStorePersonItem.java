package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 進店人員列表項
 * @author zwm
 */
public class InStorePersonItem implements MultiItemEntity {
    public static final int TYPE_LABEL = 1;  // 分類標簽名: 好友 店友
    public static final int TYPE_ITEM = 2;

    public int itemType;
    public String memberName;
    public String avatarUrl;
    public String nickname;

    public InStorePersonItem(int itemType, String memberName, String avatarUrl, String nickname) {
        this.itemType = itemType;
        this.memberName = memberName;
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
