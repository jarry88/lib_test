package com.ftofs.twant.domain.theme;

import java.text.SimpleDateFormat;

public class Theme {
    /**
     * 编号
     */
    private int themeId;

    /**
     * 标题
     */
    private String themeTitle;

    /**
     * 封面图
     * 前台没有活动列表，该字段预置
     */
    private String themeImage;

    /**
     * 正文距顶部距离
     */
    private int marginTop;

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
    private String backgroundRepeat;

    /**
     * 商城活动状态
     * 0-不显示 1-显示
     */
    private int themeState = 1;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 活动报名开始时间
     */
    private String enrollStartTime;

    /**
     * 活动报名截止时间
     */
    private String enrollEndTime;

    /**
     * 活动展示开始时间
     */
    private String showStartTime;

    /**
     * 活动展示结束时间
     */
    private String showEndTime;

    /**
     * 活动规则描述
     */
    private String themeDescribe = "";

    /**
     * 活动报名开始时间
     */
    private String enrollStartTimeString = "";

    /**
     * 活动报名截止时间
     */
    private String  enrollEndTimeString = "";

    /**
     * 活动展示开始时间
     */
    private String showStartTimeString = "";

    /**
     * 活动展示结束时间
     */
    private String showEndTimeString = "";

    /**
     * 商家是否可以参加活动提交商品
     * 0不可以，1可以
     */
    private int isInTheme = 0;

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getThemeTitle() {
        return themeTitle;
    }

    public void setThemeTitle(String themeTitle) {
        this.themeTitle = themeTitle;
    }

    public String getThemeImage() {
        return themeImage;
    }

    public void setThemeImage(String themeImage) {
        this.themeImage = themeImage;
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

    public int getThemeState() {
        return themeState;
    }

    public void setThemeState(int themeState) {
        this.themeState = themeState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEnrollStartTime() {
        return enrollStartTime;
    }

    public void setEnrollStartTime(String enrollStartTime) {
        this.enrollStartTime = enrollStartTime;
    }

    public String getEnrollEndTime() {
        return enrollEndTime;
    }

    public void setEnrollEndTime(String enrollEndTime) {
        this.enrollEndTime = enrollEndTime;
    }

    public String getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(String showStartTime) {
        this.showStartTime = showStartTime;
    }

    public String getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(String showEndTime) {
        this.showEndTime = showEndTime;
    }

    public String getThemeDescribe() {
        return themeDescribe;
    }

    public void setThemeDescribe(String themeDescribe) {
        this.themeDescribe = themeDescribe;
    }

    public String getEnrollStartTimeString() {
        return enrollStartTimeString;
    }

    public void setEnrollStartTimeString(String enrollStartTimeString) {
        this.enrollStartTimeString = enrollStartTimeString;
    }

    public String getEnrollEndTimeString() {
        return enrollEndTimeString;
    }

    public void setEnrollEndTimeString(String enrollEndTimeString) {
        this.enrollEndTimeString = enrollEndTimeString;
    }

    public String getShowStartTimeString() {
        return showStartTimeString;
    }

    public void setShowStartTimeString(String showStartTimeString) {
        this.showStartTimeString = showStartTimeString;
    }

    public String getShowEndTimeString() {
        return showEndTimeString;
    }

    public void setShowEndTimeString(String showEndTimeString) {
        this.showEndTimeString = showEndTimeString;
    }

    public int getIsInTheme() {
        return isInTheme;
    }

    public void setIsInTheme(int isInTheme) {
        this.isInTheme = isInTheme;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "themeId=" + themeId +
                ", themeTitle='" + themeTitle + '\'' +
                ", themeImage='" + themeImage + '\'' +
                ", marginTop=" + marginTop +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                ", backgroundRepeat='" + backgroundRepeat + '\'' +
                ", themeState=" + themeState +
                ", updateTime=" + updateTime +
                ", enrollStartTime=" + enrollStartTime +
                ", enrollEndTime=" + enrollEndTime +
                ", showStartTime=" + showStartTime +
                ", showEndTime=" + showEndTime +
                ", themeDescribe='" + themeDescribe + '\'' +
                ", enrollStartTimeString='" + enrollStartTimeString + '\'' +
                ", enrollEndTimeString='" + enrollEndTimeString + '\'' +
                ", showStartTimeString='" + showStartTimeString + '\'' +
                ", showEndTimeString='" + showEndTimeString + '\'' +
                ", isInTheme=" + isInTheme +
                '}';
    }
}
