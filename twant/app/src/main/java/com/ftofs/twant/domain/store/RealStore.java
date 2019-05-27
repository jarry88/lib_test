package com.ftofs.twant.domain.store;

public class RealStore {
    /**
     * 编号
     */
    private int realStoreId;

    /**
     * 名称
     */
    private String realStoreName;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 一级地区编号
     */
    private int areaId1 = 0;

    /**
     * 二级地区编号
     */
    private int areaId2 = 0;

    /**
     * 地区内容
     */
    private String areaInfo;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 交通线路
     */
    private String trafficLine;

    /**
     * 高德经度
     */
    private Float lng;

    /**
     * 高德纬度
     */
    private Float lat;

    /**
     * 高德经度字符串形式
     */
    private String latString;

    /**
     * 高德纬度字符串形式
     */
    private String lngString;

    public int getRealStoreId() {
        return realStoreId;
    }

    public void setRealStoreId(int realStoreId) {
        this.realStoreId = realStoreId;
    }

    public String getRealStoreName() {
        return realStoreName;
    }

    public void setRealStoreName(String realStoreName) {
        this.realStoreName = realStoreName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTrafficLine() {
        return trafficLine;
    }

    public void setTrafficLine(String trafficLine) {
        this.trafficLine = trafficLine;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getLatString() {
        return latString;
    }

    public void setLatString(String latString) {
        this.latString = latString;
    }

    public String getLngString() {
        return lngString;
    }

    public void setLngString(String lngString) {
        this.lngString = lngString;
    }

    @Override
    public String toString() {
        return "RealStore{" +
                "realStoreId=" + realStoreId +
                ", realStoreName='" + realStoreName + '\'' +
                ", storeId=" + storeId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaInfo='" + areaInfo + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", trafficLine='" + trafficLine + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", latString='" + latString + '\'' +
                ", lngString='" + lngString + '\'' +
                '}';
    }
}
