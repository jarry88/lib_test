package com.ftofs.twant.domain;

public class AdminMobileArea {
    private Integer areaId;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 地区区号
     */
    private String areaCode;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "AdminMobileArea{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}
