package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class JobInfoItem implements MultiItemEntity {
    public static final int TYPE_DISPLAY = 2;
    public static final int TYPE_POST = 1;
    public int postId;
    public String postName;
    public String positionType;  // 兼職/全職
    public String salaryType;  // 月薪日新
    public String salaryRange;//薪資範圍
    public String storeAvatar;//商店頭像
    public String storeName;
    public String jobName;
    public String workArea; // 澳门 氹仔

    public String storeMemberName;
    public String storeNickName;
    public String bossAvatar;

    public boolean showPersonInfo;
    public String workDescription;
    public String mailbox;
    public int isFavor;
    public boolean isJobDescExpanded;  // 職位詳情是否已經展開
    public int itemType=JobInfoItem.TYPE_POST;
    public int relationshipId;
    public int shopId;
    public int isLook;

    @Override
    public int getItemType() {
        return itemType;
    }
}
