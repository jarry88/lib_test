package com.ftofs.twant.entity;

/**
 * 商店公告數據結構
 * @author zwm
 */
public class StoreAnnouncement {
    public long createTime;

    public StoreAnnouncement(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /**
     * 公告Id
     */
    public int id;
    public String title;
    public String content;
}
