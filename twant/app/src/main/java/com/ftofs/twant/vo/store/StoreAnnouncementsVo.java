package com.ftofs.twant.vo.store;

/**
 * @Description: 店铺公告视图对象
 * @Auther: yangjian
 * @Date: 2018/12/14 09:56
 */
public class StoreAnnouncementsVo {
    /**
     * 公告id
     */
    private int id;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 公告标题
     */
    private String announcementsTitle;
    /**
     * 公告排序
     */
    private int announcementsSort;
    /**
     * 外链url
     */
    private String announcementsUrl;
    /**
     * 公告内容
     */
    private String announcementContent;
    /**
     * 发布人员
     */
    private String createBy;
    /**
     * 最新休改人员
     */
    private String modifyBy;
    /**
     * 发布时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 状态 0下线  1上线中
     */
    private int stat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAnnouncementsTitle() {
        return announcementsTitle;
    }

    public void setAnnouncementsTitle(String announcementsTitle) {
        this.announcementsTitle = announcementsTitle;
    }

    public int getAnnouncementsSort() {
        return announcementsSort;
    }

    public void setAnnouncementsSort(int announcementsSort) {
        this.announcementsSort = announcementsSort;
    }

    public String getAnnouncementsUrl() {
        return announcementsUrl;
    }

    public void setAnnouncementsUrl(String announcementsUrl) {
        this.announcementsUrl = announcementsUrl;
    }

    public String getAnnouncementContent() {
        return announcementContent;
    }

    public void setAnnouncementContent(String announcementContent) {
        this.announcementContent = announcementContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "StoreAnnouncementsVo{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", announcementsTitle='" + announcementsTitle + '\'' +
                ", announcementsSort=" + announcementsSort +
                ", announcementsUrl='" + announcementsUrl + '\'' +
                ", announcementContent='" + announcementContent + '\'' +
                ", createBy='" + createBy + '\'' +
                ", modifyBy='" + modifyBy + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", stat=" + stat +
                '}';
    }
}
