package com.ftofs.twant.vo;

import com.ftofs.twant.domain.ChannelModule;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 频道模块
 *
 * @author cj
 * Created 2017-4-14 下午 6:19
 */
public class ChannelModuleVo implements Serializable {
    private int moduleId;
    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块类型
     */
    private String moduleType;

    /**
     * 模版名称
     */
    private String moduleTemplateName;

    /**
     * 是否显示
     * 1:显示 0： 不显示
     */
    private int moduleShow;

    /**
     * 更新时间
     */
    private Timestamp moduleUpdateTime;

    /**
     * 模块详情数据
     */
    private List<ChannelModuleItemVo> channelModuleItemVoList = new ArrayList<ChannelModuleItemVo>();

    public ChannelModuleVo(ChannelModule channelModule) {
        this.moduleId = channelModule.getModuleId();
        this.moduleName = channelModule.getModuleName();
        this.moduleType = channelModule.getModuleType();
        this.moduleTemplateName = channelModule.getModuleTemplateName();
        this.moduleShow = channelModule.getModuleShow();
        this.moduleUpdateTime = channelModule.getModuleUpdateTime();
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleTemplateName() {
        return moduleTemplateName;
    }

    public void setModuleTemplateName(String moduleTemplateName) {
        this.moduleTemplateName = moduleTemplateName;
    }

    public int getModuleShow() {
        return moduleShow;
    }

    public void setModuleShow(int moduleShow) {
        this.moduleShow = moduleShow;
    }

    public Timestamp getModuleUpdateTime() {
        return moduleUpdateTime;
    }

    public void setModuleUpdateTime(Timestamp moduleUpdateTime) {
        this.moduleUpdateTime = moduleUpdateTime;
    }

    public List<ChannelModuleItemVo> getChannelModuleItemVoList() {
        return channelModuleItemVoList;
    }

    public void setChannelModuleItemVoList(List<ChannelModuleItemVo> channelModuleItemVoList) {
        this.channelModuleItemVoList = channelModuleItemVoList;
    }

    @Override
    public String toString() {
        return "ChannelModuleVo{" +
                "moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", moduleTemplateName='" + moduleTemplateName + '\'' +
                ", moduleShow=" + moduleShow +
                ", moduleUpdateTime=" + moduleUpdateTime +
                ", channelModuleItemVoList=" + channelModuleItemVoList +
                '}';
    }
}
