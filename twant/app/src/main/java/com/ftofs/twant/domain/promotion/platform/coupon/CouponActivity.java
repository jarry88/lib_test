package com.ftofs.twant.domain.promotion.platform.coupon;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponActivity implements Serializable,Cloneable {
    //以下为平台券活动基本设置
    /**
     * 活动自增ID
     */
    private int activityId;

    /**
     * 活动名称
     */
    private String activityName = "";

    /**
     * 活动说明
     */
    private String activityExplain = "";

    /**
     * 类型 CouponActivityType
     */
    private int activityType = 0;

    /**
     * 券面额
     */
    private BigDecimal couponPrice = new BigDecimal(0);

    //以下为平台券领取规则
    /**
     * 可领取券总数（卡密领取类型券最多限制1000个）
     */
    private int totalNum = 0;

    /**
     * 领取限制的会员等级
     */
    private int limitMemberGradeLevel = 0;

    /**
     * 领取限制的会员等级名称
     */
    private String limitMemberGradeName = "";

    /**
     * 券领取所需积分（积分领取类型）
     */
    private int expendPoints = 0;

    /**
     * 活动开始时间（即开始领取券时间，为空表示立即开始）
     */
    private String activityStartTime = null;


    //以下为平台券使用规则
    /**
     * 消费限额（为0表示不限额使用）
     */
    private BigDecimal limitAmount = new BigDecimal(0);

    /**
     * 券使用有效期开始时间
     */
    private String useStartTime = null;

    /**
     * 券使用有效期结束时间
     */
    private String useEndTime = null;

    /**
     * 从领取时间开始券有效天数，券分为设置有效开始时间和结束时间或者设置有效天数两种（活动赠送类型）
     */
    private int validDays = 0;

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

    //以下为平台券结算规则
    /**
     * 店铺承担比例（结算时店铺对优惠金额的承担比例）
     */
    private double storeCommitmentRate = 0;


    //以下为平台券附加信息
    /**
     * 类型为卡密兑换是否已经生成下属卡密 0未生成 1已生成
     */
    private int haveCreated = 0;

    /**
     * 活动创建时间
     */
    private String addTime = null;

    /**
     * 活动最后更新时间
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
     * 活动状态 CouponActivityState
     */
    private int activityState = 0;

    /**
     * 模版已发放的券数量
     */
    private int giveoutNum = 0;

    /**
     * 活动已经使用过的券数量
     */
    private int usedNum = 0;

    /**
     * 推荐状态 0不推荐 1推荐（将在领券中心显示）
     */
    private int recommendState = 0;

    //非数据库属性
    /**
     * 活动类型文本
     */
    private String activityTypeText = "";

    /**
     * 客户端类型标识
     */
    private String usableClientType= "";

    /**
     * 客户端类型文本
     */
    private String usableClientTypeText= "";

    /**
     * 活动当前状态标识
     */
    private String activityCurrentStateSign = "";

    /**
     * 活动当前状态文本
     */
    private String activityCurrentStateText = "";

    /**
     * 过期状态 0未过期，1已过期
     */
    private int expiredState = 0;

    /**
     * 完成状态 0未完成，1已完成
     */
    private int completeState = 0;

    /**
     * 有效期开始时间
     */
    private String useStartTimeText = "";

    /**
     * 有效期结束时间
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

    public String getActivityExplain() {
        return activityExplain;
    }

    public void setActivityExplain(String activityExplain) {
        this.activityExplain = activityExplain;
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

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getLimitMemberGradeLevel() {
        return limitMemberGradeLevel;
    }

    public void setLimitMemberGradeLevel(int limitMemberGradeLevel) {
        this.limitMemberGradeLevel = limitMemberGradeLevel;
    }

    public String getLimitMemberGradeName() {
        return limitMemberGradeName;
    }

    public void setLimitMemberGradeName(String limitMemberGradeName) {
        this.limitMemberGradeName = limitMemberGradeName;
    }

    public int getExpendPoints() {
        return expendPoints;
    }

    public void setExpendPoints(int expendPoints) {
        this.expendPoints = expendPoints;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
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

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
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

    public int getHaveCreated() {
        return haveCreated;
    }

    public void setHaveCreated(int haveCreated) {
        this.haveCreated = haveCreated;
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

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    public int getGiveoutNum() {
        return giveoutNum;
    }

    public void setGiveoutNum(int giveoutNum) {
        this.giveoutNum = giveoutNum;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }

    public int getRecommendState() {
        return recommendState;
    }

    public void setRecommendState(int recommendState) {
        this.recommendState = recommendState;
    }

    public String getActivityTypeText() {
        return activityTypeText;
    }

    public void setActivityTypeText(String activityTypeText) {
        this.activityTypeText = activityTypeText;
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

    public String getActivityCurrentStateSign() {
        return activityCurrentStateSign;
    }

    public void setActivityCurrentStateSign(String activityCurrentStateSign) {
        this.activityCurrentStateSign = activityCurrentStateSign;
    }

    public String getActivityCurrentStateText() {
        return activityCurrentStateText;
    }

    public void setActivityCurrentStateText(String activityCurrentStateText) {
        this.activityCurrentStateText = activityCurrentStateText;
    }

    public int getExpiredState() {
        return expiredState;
    }

    public void setExpiredState(int expiredState) {
        this.expiredState = expiredState;
    }

    public int getCompleteState() {
        return completeState;
    }

    public void setCompleteState(int completeState) {
        this.completeState = completeState;
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

    @Override
    public String toString() {
        return "CouponActivity{" +
                "activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityExplain='" + activityExplain + '\'' +
                ", activityType=" + activityType +
                ", couponPrice=" + couponPrice +
                ", totalNum=" + totalNum +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", expendPoints=" + expendPoints +
                ", activityStartTime=" + activityStartTime +
                ", limitAmount=" + limitAmount +
                ", useStartTime=" + useStartTime +
                ", useEndTime=" + useEndTime +
                ", validDays=" + validDays +
                ", useGoodsRange=" + useGoodsRange +
                ", useGoodsRangeExplain='" + useGoodsRangeExplain + '\'' +
                ", webUsable=" + webUsable +
                ", appUsable=" + appUsable +
                ", wechatUsable=" + wechatUsable +
                ", storeCommitmentRate=" + storeCommitmentRate +
                ", haveCreated=" + haveCreated +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", activityState=" + activityState +
                ", giveoutNum=" + giveoutNum +
                ", usedNum=" + usedNum +
                ", recommendState=" + recommendState +
                ", activityTypeText='" + activityTypeText + '\'' +
                ", usableClientType='" + usableClientType + '\'' +
                ", usableClientTypeText='" + usableClientTypeText + '\'' +
                ", activityCurrentStateSign='" + activityCurrentStateSign + '\'' +
                ", activityCurrentStateText='" + activityCurrentStateText + '\'' +
                ", expiredState=" + expiredState +
                ", completeState=" + completeState +
                ", useStartTimeText='" + useStartTimeText + '\'' +
                ", useEndTimeText='" + useEndTimeText + '\'' +
                ", useGoodsRangeText='" + useGoodsRangeText + '\'' +
                ", limitAmountText='" + limitAmountText + '\'' +
                '}';
    }
}
