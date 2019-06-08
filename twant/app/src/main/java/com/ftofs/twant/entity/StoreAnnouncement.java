package com.ftofs.twant.entity;

/**
 * 店鋪公告數據結構
 * @author zwm
 */
public class StoreAnnouncement {
    public StoreAnnouncement(int id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * 公告Id
     */
    public int id;
    public String title;
}
