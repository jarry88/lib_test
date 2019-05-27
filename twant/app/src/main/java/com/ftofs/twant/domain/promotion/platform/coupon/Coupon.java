package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

public class Coupon implements Serializable,Cloneable {
    /**
     * 自增ID
     */
    private int couponId;

    /**
     * 平台券编码
     */
    private String couponCode = "";

    /**
     * 平台券活动ID
     */
    private int activityId;

    /**
     * 活动名称
     */
    private String activityName = "";

    /**
     * 类型 CouponActivityType
     */
    private int activityType = 0;

    /**
     * 券面额
     */
    private BigDecimal couponPrice = new BigDecimal(0);

    /**
     * 消费限额（为0表示不限额使用）
     */
    private BigDecimal limitAmount = new BigDecimal(0);

    /**
     * 券使用有效期开始时间
     */
    private String useStartTime;

    /**
     * 券使用有效期结束时间
     */
    private String useEndTime;

    /**
     * 券限制适用品类的范围类型 CouponUseGoodsRange
     */
    private int useGoodsRange = 0;

    /**
     * 券限制适用品类的范围说明
     */
    private String useGoodsRangeExplain = "";

    /**
     * PC端是否可用
     */
    private int webUsable = 0;

    /**
     * APP端是否可用（包括wap、ios、android）
     */
    private int appUsable = 0;

    /**
     * 微信端是否可用
     */
    private int wechatUsable = 0;

    /**
     * 店铺承担比例（结算时店铺对优惠金额的承担比例）
     */
    private double storeCommitmentRate = 0;

    /**
     * 领取时间
     */
    private String activeTime;

    /**
     * 平台券状态 CouponState
     */
    private int couponState = 0;

    /**
     * 拥有者会员ID
     */
    private int memberId = 0;

    /**
     * 拥有者会员名称
     */
    private String memberName = "";

    //非数据库字段
    /**
     * 客户端类型标识
     */
    private String usableClientType= "";

    /**
     * 客户端类型文本
     */
    private String usableClientTypeText= "";

    /**
     * 平台券当前状态标识
     */
    private String couponCurrentStateSign = "";

    /**
     * 平台券当前状态文本
     */
    private String couponCurrentStateText = "";

    /**
     * 过期状态 0未过期，1已过期
     */
    private int couponExpiredState = 0;

    /**
     * 有效期开始时间文本
     */
    private String useStartTimeText = "";

    /**
     * 有效期结束时间文本
     */
    private String useEndTimeText = "";

    /**
     * 券限制适用品类的范围类型文本
     */
    private String useGoodsRangeText = "";

    /**
     * 消费限额文本
     */
    private String limitAmountText = "";

    /**
     * 消费限额文本1
     */
    private String limitText = "";

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(String useStartTime) {
        this.useStartTime = useStartTime;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public int getUseGoodsRange() {
        return useGoodsRange;
    }

    public void setUseGoodsRange(int useGoodsRange) {
        this.useGoodsRange = useGoodsRange;
    }

    public String getUseGoodsRangeExplain() {
        return useGoodsRangeExplain;
    }

    public void setUseGoodsRangeExplain(String useGoodsRangeExplain) {
        this.useGoodsRangeExplain = useGoodsRangeExplain;
    }

    public int getWebUsable() {
        return webUsable;
    }

    public void setWebUsable(int webUsable) {
        this.webUsable = webUsable;
    }

    public int getAppUsable() {
        return appUsable;
    }

    public void setAppUsable(int appUsable) {
        this.appUsable = appUsable;
    }

    public int getWechatUsable() {
        return wechatUsable;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public double getStoreCommitmentRate() {
        return storeCommitmentRate;
    }

    public void setStoreCommitmentRate(double storeCommitmentRate) {
        this.storeCommitmentRate = storeCommitmentRate;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public int getCouponState() {
        return couponState;
    }

    public void setCouponState(int couponState) {
        this.couponState = couponState;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getUsableClientType() {
        return usableClientType;
    }

    public void setUsableClientType(String usableClientType) {
        this.usableClientType = usableClientType;
    }

    public String getUsableClientTypeText() {
        return usableClientTypeText;
    }

    public void setUsableClientTypeText(String usableClientTypeText) {
        this.usableClientTypeText = usableClientTypeText;
    }

    public String getCouponCurrentStateSign() {
        return couponCurrentStateSign;
    }

    public void setCouponCurrentStateSign(String couponCurrentStateSign) {
        this.couponCurrentStateSign = couponCurrentStateSign;
    }

    public String getCouponCurrentStateText() {
        return couponCurrentStateText;
    }

    public void setCouponCurrentStateText(String couponCurrentStateText) {
        this.couponCurrentStateText = couponCurrentStateText;
    }

    public int getCouponExpiredState() {
        return couponExpiredState;
    }

    public void setCouponExpiredState(int couponExpiredState) {
        this.couponExpiredState = couponExpiredState;
    }

    public String getUseStartTimeText() {
        return useStartTimeText;
    }

    public void setUseStartTimeText(String useStartTimeText) {
        this.useStartTimeText = useStartTimeText;
    }

    public String getUseEndTimeText() {
        return useEndTimeText;
    }

    public void setUseEndTimeText(String useEndTimeText) {
        this.useEndTimeText = useEndTimeText;
    }

    public String getUseGoodsRangeText() {
        return useGoodsRangeText;
    }

    public void setUseGoodsRangeText(String useGoodsRangeText) {
        this.useGoodsRangeText = useGoodsRangeText;
    }

    public String getLimitAmountText() {
        return limitAmountText;
    }

    public void setLimitAmountText(String limitAmountText) {
        this.limitAmountText = limitAmountText;
    }

    public String getLimitText() {
        return limitText;
    }

    public void setLimitText(String limitText) {
        this.limitText = limitText;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "couponId=" + couponId +
                ", couponCode='" + couponCode + '\'' +
                ", activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityType=" + activityType +
                ", couponPrice=" + couponPrice +
                ", limitAmount=" + limitAmount +
                ", useStartTime=" + useStartTime +
                ", useEndTime=" + useEndTime +
                ", useGoodsRange=" + useGoodsRange +
                ", useGoodsRangeExplain='" + useGoodsRangeExplain + '\'' +
                ", webUsable=" + webUsable +
                ", appUsable=" + appUsable +
                ", wechatUsable=" + wechatUsable +
                ", storeCommitmentRate=" + storeCommitmentRate +
                ", activeTime=" + activeTime +
                ", couponState=" + couponState +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", usableClientType='" + usableClientType + '\'' +
                ", usableClientTypeText='" + usableClientTypeText + '\'' +
                ", couponCurrentStateSign='" + couponCurrentStateSign + '\'' +
                ", couponCurrentStateText='" + couponCurrentStateText + '\'' +
                ", couponExpiredState=" + couponExpiredState +
                ", useStartTimeText='" + useStartTimeText + '\'' +
                ", useEndTimeText='" + useEndTimeText + '\'' +
                ", useGoodsRangeText='" + useGoodsRangeText + '\'' +
                ", limitAmountText='" + limitAmountText + '\'' +
                '}';
    }
}
