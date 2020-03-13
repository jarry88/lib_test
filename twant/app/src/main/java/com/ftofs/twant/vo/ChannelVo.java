package com.ftofs.twant.vo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 频道Vo
 *
 * @author cj
 * Created 2017-4-14 下午 6:19
 */
public class ChannelVo implements Serializable {
    /**
     * 频道id
     */
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
     * 產品分类名称
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
    private String channelUpdateTime;


    /**
     * 是否显示
     * 1:显示 0： 不显示
     */
    private int channelShow;


    /**
     * 携带的 中部 module 信息
     */
    private List<ChannelModuleVo> middleModuleVoArrayList = new ArrayList<ChannelModuleVo>();

    /**
     * 携带的顶部module 信息
     */
    private List<ChannelModuleVo> topModuleVoArrayList = new ArrayList<ChannelModuleVo>() ;

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

    public String getChannelUpdateTime() {
        return channelUpdateTime;
    }

    public void setChannelUpdateTime(String channelUpdateTime) {
        this.channelUpdateTime = channelUpdateTime;
    }

    public int getChannelShow() {
        return channelShow;
    }

    public void setChannelShow(int channelShow) {
        this.channelShow = channelShow;
    }

    public List<ChannelModuleVo> getMiddleModuleVoArrayList() {
        return middleModuleVoArrayList;
    }

    public void setMiddleModuleVoArrayList(List<ChannelModuleVo> middleModuleVoArrayList) {
        this.middleModuleVoArrayList = middleModuleVoArrayList;
    }

    public List<ChannelModuleVo> getTopModuleVoArrayList() {
        return topModuleVoArrayList;
    }

    public void setTopModuleVoArrayList(List<ChannelModuleVo> topModuleVoArrayList) {
        this.topModuleVoArrayList = topModuleVoArrayList;
    }

    @Override
    public String toString() {
        return "ChannelVo{" +
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
                ", middleModuleVoArrayList=" + middleModuleVoArrayList +
                ", topModuleVoArrayList=" + topModuleVoArrayList +
                '}';
    }
}
