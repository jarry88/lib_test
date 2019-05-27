package com.ftofs.twant.domain.store;

public class StoreSpecial {
    /**
     * 编号
     */
    private int specialId;

    /**
     * 标题
     */
    private String specialTitle;

    /**
     * 正文距顶部距离
     */
    private int marginTop = 0;

    /**
     * 背景色
     */
    private String backgroundColor;

    /**
     * 背景图
     */
    private String backgroundImage;

    /**
     * 背景图重复方式
     * no-repeat（不重复）|repeat（平铺）|repeat-x（x轴平铺）|repeat-y（y轴平铺）
     */
    private String backgroundRepeat = "no-repeat";

    /**
     * 专题状态
     * 0-不显示 1-显示
     */
    private int specialState;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 专题类型 0-普通专题 1-店铺装修首页
     */
    private int specialType = 0;

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    public String getSpecialTitle() {
        return specialTitle;
    }

    public void setSpecialTitle(String specialTitle) {
        this.specialTitle = specialTitle;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundRepeat() {
        return backgroundRepeat;
    }

    public void setBackgroundRepeat(String backgroundRepeat) {
        this.backgroundRepeat = backgroundRepeat;
    }

    public int getSpecialState() {
        return specialState;
    }

    public void setSpecialState(int specialState) {
        this.specialState = specialState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getSpecialType() {
        return specialType;
    }

    public void setSpecialType(int specialType) {
        this.specialType = specialType;
    }

    @Override
    public String toString() {
        return "StoreSpecial{" +
                "specialId=" + specialId +
                ", specialTitle='" + specialTitle + '\'' +
                ", marginTop=" + marginTop +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                ", backgroundRepeat='" + backgroundRepeat + '\'' +
                ", specialState=" + specialState +
                ", updateTime=" + updateTime +
                ", storeId=" + storeId +
                ", specialType=" + specialType +
                '}';
    }
}

