package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 進店人員列表項
 * @author zwm
 */
public class InStorePersonItem implements MultiItemEntity {
    public static final int TYPE_LABEL = 1;  // 分類標簽名: 好友 城友
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_EMPTY_HINT = 3;  // 進店人員列表為空的提示

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
    public StoreFriendsItem toStoreFriendItem(){
        StoreFriendsItem item = new StoreFriendsItem(memberName,avatarUrl);
        return  item;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
