package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PostItem implements MultiItemEntity {
    public static final int ITEM_TYPE_NORMAL = 1;
    public static final int ITEM_TYPE_LOAD_END_HINT = 2;  // 數據全部加載完成的提示

    public int itemType;

    public int postId;
    public String coverImage;
    public String postCategory;
    public String title;

    public String authorAvatar;
    public String authorNickname;
    public String createTime;

    public int postThumb;  // 點贊數
    public int postReply; // 回復/評論數
    public int postFollow;  // 關注數

    public String deadline;

    @Override
    public int getItemType() {
        return itemType;
    }
}
