package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PostItem implements MultiItemEntity {
    public int itemType;

    public int postId;
    public String coverImage;
    public String postCategory;
    public String title;

    public String authorAvatar;
    public String authorNickname;
    public String createTime;

    public int postThumb;  // 點讚數
    public int postReply; // 回復/評論數
    public int postFollow;  // 關注數

    public String deadline;

    public int isLike;

    @Override
    public int getItemType() {
        return itemType;
    }
}
