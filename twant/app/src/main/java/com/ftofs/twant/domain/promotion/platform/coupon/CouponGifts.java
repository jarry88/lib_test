package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;
import java.math.BigDecimal;


public class CouponGifts implements Serializable,Cloneable {
    /**
     * 礼包自增ID
     */
    private int giftsId;

    /**
     * 礼包名称
     */
    private String giftsName = "";

    /**
     * 礼包说明
     */
    private String giftsExplain = "";

    /**
     * 类型 CouponGiftsType
     */
    private int giftsType = 0;

    /**
     * 礼包面值
     */
    private BigDecimal giftsPrice = new BigDecimal(0);

    /**
     * 平台券活动ID数组JsonN
     */
    private String activityIdJson = "";


    //以下为礼包领取规则
    /**
     * 礼包可发放数量（0表示不限制）
     */
    private int totalNum = 0;

    /**
     * 礼包活动开始时间（即开始领取礼包时间，为空表示立即开始）
     */
    private String giftsStartTime = null;

    /**
     * 礼包活动结束时间（即结束领取礼包时间，为空表示长期有效）
     */
    private String giftsEndTime = null;


    //领取礼包页面设置
    /**
     * 页面主题颜色
     */
    private String themeColor = "";

    /**
     * banner图片（PC端）
     */
    private String bannerImgPc = "";

    /**
     * banner图片（App端）
     */
    private String bannerImgApp = "";


    //以下为礼包附加信息
    /**
     * 礼包创建时间
     */
    private String addTime = null;

    /**
     * 礼包最后更新时间
     */
    private String updateTime = null;

    /**
     * 最后更新的管理员编号
     */
    private int adminId = 0;

    /**
     * 最后更新的管理员名称
     */
    private String adminName = "";

    /**
     * 礼包状态 CouponGiftsState
     */
    private int giftsState = 0;

    /**
     * 礼包已发放的数量
     */
    private int giveoutNum = 0;


    //非数据库属性
    /**
     * 礼包类型文本
     */
    private String giftsTypeText = "";

    /**
     * 礼包可发放数量文本
     */
    private String totalNumText = "";

    /**
     * 礼包过期状态 0未过期，1已过期
     */
    private int giftsExpiredState = 0;

    /**
     * 礼包当前状态标识
     */
    private String giftsCurrentStateSign = "";

    /**
     * 礼包当前状态文本
     */
    private String giftsCurrentStateText = "";

    /**
     * 礼包活动开始时间文本
     */
    private String giftsStartTimeText = "";

    /**
     * 礼包活动结束时间文本
     */
    private String giftsEndTimeText = "";

    /**
     * banner图片Url（PC端）
     */
    private String bannerImgPcUrl = "";

    /**
     * banner图片Url（APP端）
     */
    private String bannerImgAppUrl = "";

    /**
     * 页面主题颜色显示值，默认显示为#ffffff
     */
    private String themeColorText = "";


    public int getGiftsId() {
        return giftsId;
    }

    public void setGiftsId(int giftsId) {
        this.giftsId = giftsId;
    }

    public String getGiftsName() {
        return giftsName;
    }

    public void setGiftsName(String giftsName) {
        this.giftsName = giftsName;
    }

    public String getGiftsExplain() {
        return giftsExplain;
    }

    public void setGiftsExplain(String giftsExplain) {
        this.giftsExplain = giftsExplain;
    }

    public int getGiftsType() {
        return giftsType;
    }

    public void setGiftsType(int giftsType) {
        this.giftsType = giftsType;
    }

    public BigDecimal getGiftsPrice() {
        return giftsPrice;
    }

    public void setGiftsPrice(BigDecimal giftsPrice) {
        this.giftsPrice = giftsPrice;
    }

    public String getActivityIdJson() {
        return activityIdJson;
    }

    public void setActivityIdJson(String activityIdJson) {
        this.activityIdJson = activityIdJson;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getGiftsStartTime() {
        return giftsStartTime;
    }

    public void setGiftsStartTime(String giftsStartTime) {
        this.giftsStartTime = giftsStartTime;
    }

    public String getGiftsEndTime() {
        return giftsEndTime;
    }

    public void setGiftsEndTime(String giftsEndTime) {
        this.giftsEndTime = giftsEndTime;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getBannerImgPc() {
        return bannerImgPc;
    }

    public void setBannerImgPc(String bannerImgPc) {
        this.bannerImgPc = bannerImgPc;
    }

    public String getBannerImgApp() {
        return bannerImgApp;
    }

    public void setBannerImgApp(String bannerImgApp) {
        this.bannerImgApp = bannerImgApp;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getGiftsState() {
        return giftsState;
    }

    public void setGiftsState(int giftsState) {
        this.giftsState = giftsState;
    }

    public int getGiveoutNum() {
        return giveoutNum;
    }

    public void setGiveoutNum(int giveoutNum) {
        this.giveoutNum = giveoutNum;
    }

    public String getGiftsTypeText() {
        return giftsTypeText;
    }

    public void setGiftsTypeText(String giftsTypeText) {
        this.giftsTypeText = giftsTypeText;
    }

    public String getTotalNumText() {
        if (totalNum > 0) {
            return String.valueOf(totalNum);
        }else{
            return "不限";
        }
    }

    public void setTotalNumText(String totalNumText) {
        this.totalNumText = totalNumText;
    }

    public int getGiftsExpiredState() {
        return giftsExpiredState;
    }

    public void setGiftsExpiredState(int giftsExpiredState) {
        this.giftsExpiredState = giftsExpiredState;
    }

    public String getGiftsCurrentStateSign() {
        return giftsCurrentStateSign;
    }

    public void setGiftsCurrentStateSign(String giftsCurrentStateSign) {
        this.giftsCurrentStateSign = giftsCurrentStateSign;
    }

    public String getGiftsCurrentStateText() {
        return giftsCurrentStateText;
    }

    public void setGiftsCurrentStateText(String giftsCurrentStateText) {
        this.giftsCurrentStateText = giftsCurrentStateText;
    }

    public String getGiftsStartTimeText() {
        return "";
    }

    public void setGiftsStartTimeText(String giftsStartTimeText) {
        this.giftsStartTimeText = giftsStartTimeText;
    }

    public String getGiftsEndTimeText() {
        return "";
    }

    public void setGiftsEndTimeText(String giftsEndTimeText) {
        this.giftsEndTimeText = giftsEndTimeText;
    }

    public String getBannerImgPcUrl() {
        return bannerImgPc;
    }

    public void setBannerImgPcUrl(String bannerImgPcUrl) {
        this.bannerImgPcUrl = bannerImgPcUrl;
    }

    public String getBannerImgAppUrl() {
        return bannerImgApp;
    }

    public void setBannerImgAppUrl(String bannerImgAppUrl) {
        this.bannerImgAppUrl = bannerImgAppUrl;
    }

    public String getThemeColorText() {
        return themeColorText;
    }

    public void setThemeColorText(String themeColorText) {
        this.themeColorText = themeColorText;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "CouponGifts{" +
                "giftsId=" + giftsId +
                ", giftsName='" + giftsName + '\'' +
                ", giftsExplain='" + giftsExplain + '\'' +
                ", giftsType=" + giftsType +
                ", giftsPrice=" + giftsPrice +
                ", activityIdJson='" + activityIdJson + '\'' +
                ", totalNum=" + totalNum +
                ", giftsStartTime=" + giftsStartTime +
                ", giftsEndTime=" + giftsEndTime +
                ", themeColor='" + themeColor + '\'' +
                ", bannerImgPc='" + bannerImgPc + '\'' +
                ", bannerImgApp='" + bannerImgApp + '\'' +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", giftsState=" + giftsState +
                ", giveoutNum=" + giveoutNum +
                ", giftsTypeText='" + giftsTypeText + '\'' +
                ", totalNumText='" + totalNumText + '\'' +
                ", giftsExpiredState=" + giftsExpiredState +
                ", giftsCurrentStateSign='" + giftsCurrentStateSign + '\'' +
                ", giftsCurrentStateText='" + giftsCurrentStateText + '\'' +
                ", giftsStartTimeText='" + giftsStartTimeText + '\'' +
                ", giftsEndTimeText='" + giftsEndTimeText + '\'' +
                ", bannerImgPcUrl='" + bannerImgPcUrl + '\'' +
                ", bannerImgAppUrl='" + bannerImgAppUrl + '\'' +
                ", themeColorText='" + themeColorText + '\'' +
                '}';
    }
}