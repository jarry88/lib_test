package com.ftofs.twant.domain;

public class FreightTemplate {
    /**
     * 主键、自增
     */
    private int freightId;

    /**
     * 标题
     */
    private String title;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 最后编辑时间
     */
    private String editTime;

    /**
     * 计价方式 按体积、重量、件数
     */
    private String calcType;

    /**
     * 是否包邮 1-是，0-否
     */
    private int freightFree = 0;

    /**
     * 第一级地区ID组成的串，以英文"_"隔开，两端也有"_"
     */
    private String areaId = "";

    public int getFreightId() {
        return freightId;
    }

    public void setFreightId(int freightId) {
        this.freightId = freightId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public int getFreightFree() {
        return freightFree;
    }

    public void setFreightFree(int freightFree) {
        this.freightFree = freightFree;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public String toString() {
        return "FreightTemplate{" +
                "freightId=" + freightId +
                ", title='" + title + '\'' +
                ", storeId=" + storeId +
                ", editTime=" + editTime +
                ", calcType='" + calcType + '\'' +
                ", freightFree=" + freightFree +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}
