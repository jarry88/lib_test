package com.ftofs.twant.domain;

public class BusinessDistrict {
    /**
     * 主鍵id
     */
    private int districtId;

    /**
     * 商圈名稱
     */
    private String districtName;

    /**
     * 一級地區編號
     */
    private int districtAreaId1;

    /**
     * 二級地區編號
     */
    private int districtAreaId2;

    /**
     * 地區信息
     */
    private String districtAreaInfo;

    /**
     * 商圈描述
     */
    private String districtDesc;

    /**
     * 排序
     */
    private int sort;

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getDistrictAreaId1() {
        return districtAreaId1;
    }

    public void setDistrictAreaId1(int districtAreaId1) {
        this.districtAreaId1 = districtAreaId1;
    }

    public int getDistrictAreaId2() {
        return districtAreaId2;
    }

    public void setDistrictAreaId2(int districtAreaId2) {
        this.districtAreaId2 = districtAreaId2;
    }

    public String getDistrictAreaInfo() {
        return districtAreaInfo;
    }

    public void setDistrictAreaInfo(String districtAreaInfo) {
        this.districtAreaInfo = districtAreaInfo;
    }

    public String getDistrictDesc() {
        return districtDesc;
    }

    public void setDistrictDesc(String districtDesc) {
        this.districtDesc = districtDesc;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
