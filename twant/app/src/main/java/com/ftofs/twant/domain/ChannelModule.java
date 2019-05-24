package com.ftofs.twant.domain;

import java.io.Serializable;


public class ChannelModule implements Serializable {
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
    private String moduleUpdateTime;

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

    public int getModuleShow() {
        return moduleShow;
    }

    public void setModuleShow(int moduleShow) {
        this.moduleShow = moduleShow;
    }

    public String getModuleUpdateTime() {
        return moduleUpdateTime;
    }

    public void setModuleUpdateTime(String moduleUpdateTime) {
        this.moduleUpdateTime = moduleUpdateTime;
    }

    public String getModuleTemplateName() {
        return moduleTemplateName;
    }

    public void setModuleTemplateName(String moduleTemplateName) {
        this.moduleTemplateName = moduleTemplateName;
    }

    @Override
    public String toString() {
        return "ChannelModule{" +
                "moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", moduleTemplateName='" + moduleTemplateName + '\'' +
                ", moduleShow=" + moduleShow +
                ", moduleUpdateTime=" + moduleUpdateTime +
                '}';
    }
}
