package com.ftofs.twant.domain;

import java.io.Serializable;

public class IndexFloorItem implements Serializable {
    private int floorItemId;

    /**
     * 名称
     */
    private String floorItemName;

    /**
     * 数据类型
     */
    private String floorItemType;

    /**
     * 排序用于tag的
     */
    private int floorItemSort;

    /**
     * 数据key ，用户标识tag 的类型
     */
    private String floorItemKey;

    /**
     * 保存的json数据
     */
    private String floorItemJson;

    /**
     * 楼层编号
     */
    private int floorId;

    public int getFloorItemId() {
        return floorItemId;
    }

    public void setFloorItemId(int floorItemId) {
        this.floorItemId = floorItemId;
    }

    public String getFloorItemName() {
        return floorItemName;
    }

    public void setFloorItemName(String floorItemName) {
        this.floorItemName = floorItemName;
    }

    public String getFloorItemType() {
        return floorItemType;
    }

    public void setFloorItemType(String floorItemType) {
        this.floorItemType = floorItemType;
    }

    public int getFloorItemSort() {
        return floorItemSort;
    }

    public void setFloorItemSort(int floorItemSort) {
        this.floorItemSort = floorItemSort;
    }

    public String getFloorItemKey() {
        return floorItemKey;
    }

    public void setFloorItemKey(String floorItemKey) {
        this.floorItemKey = floorItemKey;
    }

    public String getFloorItemJson() {
        return floorItemJson;
    }

    public void setFloorItemJson(String floorItemJson) {
        this.floorItemJson = floorItemJson;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    @Override
    public String toString() {
        return "IndexFloorItem{" +
                "floorItemId=" + floorItemId +
                ", floorItemName='" + floorItemName + '\'' +
                ", floorItemType='" + floorItemType + '\'' +
                ", floorItemSort='" + floorItemSort + '\'' +
                ", floorItemKey='" + floorItemKey + '\'' +
                ", floorItemJson='" + floorItemJson + '\'' +
                ", floorId=" + floorId +
                '}';
    }
}
