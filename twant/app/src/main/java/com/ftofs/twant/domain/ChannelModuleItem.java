package com.ftofs.twant.domain;

import java.io.Serializable;

public class ChannelModuleItem implements Serializable {
    private int moduleItemId;

    /**
     * 名称
     */
    private String moduleItemName;

    /**
     * 数据类型
     */
    private String moduleItemType;

    /**
     * 排序用于tag的
     */
    private int moduleItemSort;

    /**
     * 数据key ，用户标识tag 的类型
     */
    private String moduleItemKey;

    /**
     * 保存的json数据
     */
    private String moduleItemJson;

    /**
     * 模块编号
     */
    private int moduleId;

    public int getModuleItemId() {
        return moduleItemId;
    }

    public void setModuleItemId(int moduleItemId) {
        this.moduleItemId = moduleItemId;
    }

    public String getModuleItemName() {
        return moduleItemName;
    }

    public void setModuleItemName(String moduleItemName) {
        this.moduleItemName = moduleItemName;
    }

    public String getModuleItemType() {
        return moduleItemType;
    }

    public void setModuleItemType(String moduleItemType) {
        this.moduleItemType = moduleItemType;
    }

    public int getModuleItemSort() {
        return moduleItemSort;
    }

    public void setModuleItemSort(int moduleItemSort) {
        this.moduleItemSort = moduleItemSort;
    }

    public String getModuleItemKey() {
        return moduleItemKey;
    }

    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    public String getModuleItemJson() {
        return moduleItemJson;
    }

    public void setModuleItemJson(String moduleItemJson) {
        this.moduleItemJson = moduleItemJson;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String toString() {
        return "ChannelModuleItem{" +
                "moduleItemId=" + moduleItemId +
                ", moduleItemName='" + moduleItemName + '\'' +
                ", moduleItemType='" + moduleItemType + '\'' +
                ", moduleItemSort=" + moduleItemSort +
                ", moduleItemKey='" + moduleItemKey + '\'' +
                ", moduleItemJson='" + moduleItemJson + '\'' +
                ", moduleId=" + moduleId +
                '}';
    }
}
