package com.ftofs.twant.vo.promotion;

import com.ftofs.twant.domain.promotion.VoucherTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 店铺券活动
 *
 * @author zxy
 * Created 2017/4/13 11:02
 */
public class VoucherTemplateVo {
    /**
     * 自增编码
     */
    private int templateId = 0;
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

    private Timestamp useStartTime = new Timestamp(0);
    /**
     * 店铺券有效期结束时间
     */

    private Timestamp useEndTime = new Timestamp(0);
    /**
     * 店铺券有效天数
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

    private Timestamp addTime = new Timestamp(0);
    /**
     * 活动最后更新时间
     */

    private Timestamp updateTime = new Timestamp(0);
    /**
     * 领取店铺券限制的会员等级
     */
    private int limitMemberGradeLevel = 0;
    /**
     * 领取店铺券限制的会员等级名称
     */
    private String limitMemberGradeName = "";
    /**
     * 类型 1卡密兑换 2免费领取 4活动赠送
     */
    private int templateType = 0;
    /**
     * 活动开始时间（即开始领取券时间，为空表示立即开始）
     */

    private Timestamp templateStartTime = null;
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
     * 类型为卡密兑换是否已经生成下属店铺券 0未生成 1已生成
     */
    private int haveCreated = 0;
    /**
     * 领券中心推荐
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
    /**
     * 会员是否已领取 0未领取 1已领取
     */
    private int memberIsReceive = 0;

    public VoucherTemplateVo(VoucherTemplate voucherTemplate, int memberIsReceive) {
        this.templateId = voucherTemplate.getTemplateId();
        this.templateTitle = voucherTemplate.getTemplateTitle();
        this.templateDescribe = voucherTemplate.getTemplateDescribe();
        this.useStartTime = voucherTemplate.getUseStartTime();
        this.useEndTime = voucherTemplate.getUseEndTime();
        this.validDays = voucherTemplate.getValidDays();
        this.templatePrice = voucherTemplate.getTemplatePrice();
        this.limitAmount = voucherTemplate.getLimitAmount();
        this.storeId = voucherTemplate.getStoreId();
        this.storeName = voucherTemplate.getStoreName();
        this.sellerId = voucherTemplate.getSellerId();
        this.sellerName = voucherTemplate.getSellerName();
        this.templateState = voucherTemplate.getTemplateState();
        this.totalNum = voucherTemplate.getTotalNum();
        this.giveoutNum = voucherTemplate.getGiveoutNum();
        this.usedNum = voucherTemplate.getUsedNum();
        this.addTime = voucherTemplate.getAddTime();
        this.updateTime = voucherTemplate.getUpdateTime();
        this.limitMemberGradeLevel = voucherTemplate.getLimitMemberGradeLevel();
        this.limitMemberGradeName = voucherTemplate.getLimitMemberGradeName();
        this.templateType = voucherTemplate.getTemplateType();
        this.templateStartTime = voucherTemplate.getTemplateStartTime();
        this.webUsable = voucherTemplate.getWebUsable();
        this.appUsable = voucherTemplate.getAppUsable();
        this.wechatUsable = voucherTemplate.getWechatUsable();
        this.haveCreated = voucherTemplate.getHaveCreated();
        this.voucherCenterRecommend = voucherTemplate.getVoucherCenterRecommend();
        this.templateCurrentStateSign = voucherTemplate.getTemplateCurrentStateSign();
        this.templateCurrentStateText = voucherTemplate.getTemplateCurrentStateText();
        this.templateTypeText = voucherTemplate.getTemplateTypeText();
        this.expiredState = voucherTemplate.getExpiredState();
        this.usableClientType= voucherTemplate.getUsableClientType();
        this.usableClientTypeText= voucherTemplate.getUsableClientTypeText();
        this.withoutState = voucherTemplate.getWithoutState();
        this.useStartTimeText = voucherTemplate.getUseStartTimeText();
        this.useEndTimeText = voucherTemplate.getUseEndTimeText();
        this.limitAmountText = voucherTemplate.getLimitAmountText();
        this.memberIsReceive = memberIsReceive;
    }
    public VoucherTemplateVo(VoucherTemplate voucherTemplate) {
        this.templateId = voucherTemplate.getTemplateId();
        this.templateTitle = voucherTemplate.getTemplateTitle();
        this.templateDescribe = voucherTemplate.getTemplateDescribe();
        this.useStartTime = voucherTemplate.getUseStartTime();
        this.useEndTime = voucherTemplate.getUseEndTime();
        this.validDays = voucherTemplate.getValidDays();
        this.templatePrice = voucherTemplate.getTemplatePrice();
        this.limitAmount = voucherTemplate.getLimitAmount();
        this.storeId = voucherTemplate.getStoreId();
        this.storeName = voucherTemplate.getStoreName();
        this.sellerId = voucherTemplate.getSellerId();
        this.sellerName = voucherTemplate.getSellerName();
        this.templateState = voucherTemplate.getTemplateState();
        this.totalNum = voucherTemplate.getTotalNum();
        this.giveoutNum = voucherTemplate.getGiveoutNum();
        this.usedNum = voucherTemplate.getUsedNum();
        this.addTime = voucherTemplate.getAddTime();
        this.updateTime = voucherTemplate.getUpdateTime();
        this.limitMemberGradeLevel = voucherTemplate.getLimitMemberGradeLevel();
        this.limitMemberGradeName = voucherTemplate.getLimitMemberGradeName();
        this.templateType = voucherTemplate.getTemplateType();
        this.templateStartTime = voucherTemplate.getTemplateStartTime();
        this.webUsable = voucherTemplate.getWebUsable();
        this.appUsable = voucherTemplate.getAppUsable();
        this.wechatUsable = voucherTemplate.getWechatUsable();
        this.haveCreated = voucherTemplate.getHaveCreated();
        this.voucherCenterRecommend = voucherTemplate.getVoucherCenterRecommend();
        this.templateCurrentStateSign = voucherTemplate.getTemplateCurrentStateSign();
        this.templateCurrentStateText = voucherTemplate.getTemplateCurrentStateText();
        this.templateTypeText = voucherTemplate.getTemplateTypeText();
        this.expiredState = voucherTemplate.getExpiredState();
        this.usableClientType= voucherTemplate.getUsableClientType();
        this.usableClientTypeText= voucherTemplate.getUsableClientTypeText();
        this.withoutState = voucherTemplate.getWithoutState();
        this.useStartTimeText = voucherTemplate.getUseStartTimeText();
        this.useEndTimeText = voucherTemplate.getUseEndTimeText();
        this.limitAmountText = voucherTemplate.getLimitAmountText();
    }
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

    public Timestamp getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(Timestamp useStartTime) {
        this.useStartTime = useStartTime;
    }

    public Timestamp getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(Timestamp useEndTime) {
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
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

    public Timestamp getTemplateStartTime() {
        return templateStartTime;
    }

    public void setTemplateStartTime(Timestamp templateStartTime) {
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

    public int getMemberIsReceive() {
        return memberIsReceive;
    }

    public void setMemberIsReceive(int memberIsReceive) {
        this.memberIsReceive = memberIsReceive;
    }

    @Override
    public String toString() {
        return "VoucherTemplateVo{" +
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
                ", usableClientType='" + usableClientType + '\'' +
                ", usableClientTypeText='" + usableClientTypeText + '\'' +
                ", withoutState=" + withoutState +
                ", useStartTimeText='" + useStartTimeText + '\'' +
                ", useEndTimeText='" + useEndTimeText + '\'' +
                ", limitAmountText='" + limitAmountText + '\'' +
                ", memberIsReceive=" + memberIsReceive +
                '}';
    }
}