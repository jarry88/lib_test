package com.ftofs.twant.domain.promotion;

import java.io.Serializable;
import java.math.BigDecimal;

public class VoucherTemplate implements Serializable {
    /**
     * 自增编码
     */
    private int templateId;

    /**
     * 活动名称
     */
    private String templateTitle = "";

    /**
     * 活动描述
     */
    private String templateDescribe = "";

    /**
     * 店铺券有效期开始时间
     */
    private String useStartTime;

    /**
     * 店铺券有效期结束时间
     */
    private String useEndTime;

    /**
     * 店铺券有效天数，店铺券分为设置有效开始时间和结束时间或者设置有效天数两种，后者针对满额促销
     */
    private int validDays = 0;

    /**
     * 面额
     */
    private BigDecimal templatePrice = new BigDecimal(0);

    /**
     * 店铺券使用时的订单限额
     */
    private BigDecimal limitAmount = new BigDecimal(0);

    /**
     * 店铺编号
     */
    private int storeId = 0;

    /**
     * 店铺名称
     */
    private String storeName = "";

    /**
     * 最后更新的卖家编号
     */
    private int sellerId = 0;

    /**
     * 最后更新的卖家名称
     */
    private String sellerName = "";

    /**
     * 活动状态 1有效 2失效
     */
    private int templateState = 0;

    /**
     * 管理员更改活动状态的操作人
     */
    private int stateAdminId = 0;

    /**
     * 模版可发放的店铺券总数
     */
    private int totalNum = 0;

    /**
     * 模版已发放的店铺券数量
     */
    private int giveoutNum = 0;

    /**
     * 模版已经使用过的店铺券
     */
    private int usedNum = 0;

    /**
     * 活动创建时间
     */
    private String addTime;

    /**
     * 活动最后更新时间
     */
    private String updateTime;

    /**
     * 领取店铺券限制的会员等级
     */
    private int limitMemberGradeLevel = 0;

    /**
     * 领取店铺券限制的会员等级名称
     */
    private String limitMemberGradeName = "";

    /**
     * 类型 1卡密领取 2免费领取 4活动赠送
     */
    private int templateType = 0;

    /**
     * 活动开始时间（即开始领取券时间，为空表示立即开始）
     */
    private String templateStartTime = null;

    /**
     * PC端是否可用
     */
    private int webUsable;

    /**
     * APP端是否可用（包括wap、ios、android）
     */
    private int appUsable;

    /**
     * 微信端是否可用
     */
    private int wechatUsable;

    /**
     * 类型为卡密兑换是否已经生成下属卡密 0未生成 1已生成
     */
    private int haveCreated = 0;

    /**
     * bycj -- 领卷中心推荐
     */
    private int voucherCenterRecommend = 0;

    /**
     * 活动当前状态标识
     */
    private String templateCurrentStateSign = "";

    /**
     * 活动当前状态文本
     */
    private String templateCurrentStateText = "";

    /**
     * 活动类型文本
     */
    private String templateTypeText = "";

    /**
     * 过期状态 0未过期，1已过期
     */
    private int expiredState = 0;

    /**
     * 完成状态 0未完成，1已完成
     */
    private int completeState = 0;

    /**
     * 客户端类型标识
     */
    private String usableClientType= "";

    /**
     * 客户端类型文本
     */
    private String usableClientTypeText= "";

    /**
     * 已领完状态 0未领完，1已领完
     */
    private int withoutState = 0;

    /**
     * 店铺券有效期开始时间
     */
    private String useStartTimeText = "";

    /**
     * 店铺券有效期结束时间
     */
    private String useEndTimeText = "";

    /**
     * 消费限额文本
     */
    private String limitAmountText = "";

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public String getTemplateDescribe() {
        return templateDescribe;
    }

    public void setTemplateDescribe(String templateDescribe) {
        this.templateDescribe = templateDescribe;
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

    public BigDecimal getTemplatePrice() {
        return templatePrice;
    }

    public void setTemplatePrice(BigDecimal templatePrice) {
        this.templatePrice = templatePrice;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getTemplateState() {
        return templateState;
    }

    public void setTemplateState(int templateState) {
        this.templateState = templateState;
    }

    public int getStateAdminId() {
        return stateAdminId;
    }

    public void setStateAdminId(int stateAdminId) {
        this.stateAdminId = stateAdminId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
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

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getTemplateStartTime() {
        return templateStartTime;
    }

    public void setTemplateStartTime(String templateStartTime) {
        this.templateStartTime = templateStartTime;
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

    public int getHaveCreated() {
        return haveCreated;
    }

    public void setHaveCreated(int haveCreated) {
        this.haveCreated = haveCreated;
    }

    public int getVoucherCenterRecommend() {
        return voucherCenterRecommend;
    }

    public void setVoucherCenterRecommend(int voucherCenterRecommend) {
        this.voucherCenterRecommend = voucherCenterRecommend;
    }

    public String getTemplateCurrentStateSign() {
        return templateCurrentStateSign;
    }

    public void setTemplateCurrentStateSign(String templateCurrentStateSign) {
        this.templateCurrentStateSign = templateCurrentStateSign;
    }

    public String getTemplateCurrentStateText() {
        return templateCurrentStateText;
    }

    public void setTemplateCurrentStateText(String templateCurrentStateText) {
        this.templateCurrentStateText = templateCurrentStateText;
    }

    public String getTemplateTypeText() {
        return templateTypeText;
    }

    public void setTemplateTypeText(String templateTypeText) {
        this.templateTypeText = templateTypeText;
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

    public int getWithoutState() {
        return withoutState;
    }

    public void setWithoutState(int withoutState) {
        this.withoutState = withoutState;
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

    public String getLimitAmountText() {
        return limitAmountText;
    }

    public void setLimitAmountText(String limitAmountText) {
        this.limitAmountText = limitAmountText;
    }

    @Override
    public String toString() {
        return "VoucherTemplate{" +
                "templateId=" + templateId +
                ", templateTitle='" + templateTitle + '\'' +
                ", templateDescribe='" + templateDescribe + '\'' +
                ", useStartTime=" + useStartTime +
                ", useEndTime=" + useEndTime +
                ", validDays=" + validDays +
                ", templatePrice=" + templatePrice +
                ", limitAmount=" + limitAmount +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", templateState=" + templateState +
                ", stateAdminId=" + stateAdminId +
                ", totalNum=" + totalNum +
                ", giveoutNum=" + giveoutNum +
                ", usedNum=" + usedNum +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", templateType=" + templateType +
                ", templateStartTime=" + templateStartTime +
                ", webUsable=" + webUsable +
                ", appUsable=" + appUsable +
                ", wechatUsable=" + wechatUsable +
                ", haveCreated=" + haveCreated +
                ", voucherCenterRecommend=" + voucherCenterRecommend +
                ", templateCurrentStateSign='" + templateCurrentStateSign + '\'' +
                ", templateCurrentStateText='" + templateCurrentStateText + '\'' +
                ", templateTypeText='" + templateTypeText + '\'' +
                ", expiredState=" + expiredState +
                ", completeState=" + completeState +
                ", usableClientType='" + usableClientType + '\'' +
                ", usableClientTypeText='" + usableClientTypeText + '\'' +
                ", withoutState=" + withoutState +
                ", useStartTimeText='" + useStartTimeText + '\'' +
                ", useEndTimeText='" + useEndTimeText + '\'' +
                ", limitAmountText='" + limitAmountText + '\'' +
                '}';
    }
}
