package com.ftofs.twant.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Channel implements Serializable {
    private int channelId;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 颜色名称
     */
    private String channelStyle;

    /**
     * 分类id
     */
    private int categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * seo 关键字
     */
    private String channelKeywords;

    /**
     * seo 描述
     */
    private String channelDescription;

     /**
     * 顶部id
     */
    private int channelTopId;

    /**
     * 楼层id 列表
     */
    private String channelFloorId;

    /**
     * 更新时间
     */
    private Timestamp channelUpdateTime;

    /**
     * 是否显示
     * 1:显示 0： 不显示
     */
    private int channelShow;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelStyle() {
        return channelStyle;
    }

    public void setChannelStyle(String channelStyle) {
        this.channelStyle = channelStyle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getChannelKeywords() {
        return channelKeywords;
    }

    public void setChannelKeywords(String channelKeywords) {
        this.channelKeywords = channelKeywords;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public int getChannelTopId() {
        return channelTopId;
    }

    public void setChannelTopId(int channelTopId) {
        this.channelTopId = channelTopId;
    }

    public String getChannelFloorId() {
        return channelFloorId;
    }

    public void setChannelFloorId(String channelFloorId) {
        this.channelFloorId = channelFloorId;
    }

    public Timestamp getChannelUpdateTime() {
        return channelUpdateTime;
    }

    public void setChannelUpdateTime(Timestamp channelUpdateTime) {
        this.channelUpdateTime = channelUpdateTime;
    }

    public int getChannelShow() {
        return channelShow;
    }

    public void setChannelShow(int channelShow) {
        this.channelShow = channelShow;
    }

    @Override
    public String toString() {
        return "WebChannel{" +
                "channelId=" + channelId +
                ", channelName='" + channelName + '\'' +
                ", channelStyle='" + channelStyle + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", channelKeywords='" + channelKeywords + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", channelTopId=" + channelTopId +
                ", channelFloorId='" + channelFloorId + '\'' +
                ", channelUpdateTime=" + channelUpdateTime +
                ", channelShow=" + channelShow +
                '}';
    }
}
