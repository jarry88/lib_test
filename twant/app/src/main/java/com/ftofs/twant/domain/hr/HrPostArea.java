package com.ftofs.twant.domain.hr;

public class HrPostArea {

    /**
     * 地區id
     */
    private int areaId;

    /**
     * 地區名稱
     */
    private String areaName;

    /**
     * 地區父id
     */
    private int parentAreaId;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getParentAreaId() {
        return parentAreaId;
    }

    public void setParentAreaId(int parentAreaId) {
        this.parentAreaId = parentAreaId;
    }

    @Override
    public String toString() {
        return "HrPostArea{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", parentAreaId=" + parentAreaId +
                '}';
    }
}
