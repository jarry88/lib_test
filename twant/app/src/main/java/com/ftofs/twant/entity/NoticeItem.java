package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class NoticeItem implements MultiItemEntity {
    public int itemType;
    public int id;
    public String title;
    public String tplCode;
    public String createTime;
    public String imageUrl;
    public String content;
    public boolean isRead;


    public NoticeItem(int itemType) {
        this.itemType = itemType;
    }

    public NoticeItem(int itemType, int id, String title, String tplCode, String createTime,
                      String imageUrl, String content, boolean isRead) {
        this.itemType = itemType;
        this.id = id;
        this.title = title;
        this.tplCode = tplCode;
        this.createTime = createTime;
        this.imageUrl = imageUrl;
        this.content = content;
        this.isRead = isRead;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
