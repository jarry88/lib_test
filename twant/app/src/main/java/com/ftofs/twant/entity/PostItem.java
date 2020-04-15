package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.util.StringUtil;

public class PostItem extends MyFollowItem implements MultiItemEntity {
    public int itemType;
    public final static int POST_TYPE_ABOUT_GOOD =6;
    public final static int POST_TYPE_DEFAULT = 1;
    public final static int POST_TYPE_WANT = 2;
    public final static int POST_TYPE_MESSAGE = 3;
    public final static int POST_TYPE_AUTHOR = 4;
    public final static int POST_TYPE_MY_FOLLOW = 5;
    public final static int POST_TYPE_ABOUT_SHOP =7;
    public int postId;
    public String coverImage;
    public String postCategory;
    public String title;
    public String content;

    public String authorAvatar;
    public String authorNickname;
    public String createTime;

    public int postThumb;  // 讚想數
    public int postReply; // 回覆/評論數
    public int postFollow;  // 關注數
    public int postView;  // 瀏覽量

    public String storeName;//商店暱稱
    public String shopAvatar;//商店頭像
    public String goodsName;//產品名稱
    public String goodsimage;//產品圖片
    public String goodsPrice;
    public int commonId;

    public String deadline;

    public int isLike;
    public int isFav;

    public int comeTrueState;  // comeTrueState 成真狀態 1是 0否

    // 最后一項的動畫的顯示狀態
    public int animShowStatus;
    public int postLike ;
    public int isDelete;

    @Override
    public int getItemType() {
        return itemType;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("shopAvatar[%s],coverImage[%s]",shopAvatar,coverImage);
    }
}
