package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class PostItem implements MultiItemEntity {
    public static final int ANIM_NOT_SHOWN = 0;
    public static final int ANIM_SHOWING = 1;
    public static final int ANIM_SHOWN = 2;


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
    public int postView;  // 瀏覽量

    public String deadline;

    public int isLike;
    public int isFav;

    // 最后一項的動畫的顯示狀態
    public int animShowStatus;

    @Override
    public int getItemType() {
        return itemType;
    }
}
