package com.ftofs.twant.domain.promotion;

import com.ftofs.twant.vo.promotion.GiftVo;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

public class Conform implements Serializable {
    /**
     * 满优惠编号
     */
    private int conformId;

    /**
     * 满优惠名称
     */
    private String conformName;

    /**
     * 满优惠标题
     */
    private String conformTitle;

    /**
     * 满优惠标题，用于外部显示
     */
    private String conformTileFinal;

    /**
     * 活动开始时间
     */
    private String startTime;

    /**
     * 活动结束时间
     */
    private String endTime;

    /**
     * 活动状态
     */
    private int conformState;

    /**
     * 金额限制
     */
    private BigDecimal limitAmount;

    /**
     * 减免金额
     */
    private BigDecimal conformPrice;

    /**
     * 是否包邮
     */
    private int isFreeFreight;

    /**
     * 排除地区
     */
    private String ruleOutAreaIds = "";

    /**
     * 排除地区
     */
    private String ruleOutAreaNames = "";

    /**
     * 店铺券模板编号
     */
    private int templateId;

    /**
     * 面额
     */
    private BigDecimal templatePrice = new BigDecimal(0);

    /**
     * 赠品标记
     */
    private Integer isGift;

    /**
     * 赠品列表
     */
    private List<GiftVo> giftVoList = new ArrayList<>();

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 活动状态文字
     */
    private String conformStateText;

    /**
     * 活动完整规则
     */
    private String contentRule;

    /**
     * 活动简洁规则
     */
    private String shortRule;

    public int getConformId() {
        return conformId;
    }

    public void setConformId(int conformId) {
        this.conformId = conformId;
    }

    public String getConformName() {
        return conformName;
    }

    public void setConformName(String conformName) {
        this.conformName = conformName;
    }

    public String getConformTitle() {
        return conformTitle;
    }

    public void setConformTitle(String conformTitle) {
        this.conformTitle = conformTitle;
    }

    public String getConformTileFinal() {
        return conformTitle;
    }

    public void setConformTileFinal(String conformTileFinal) {
        this.conformTileFinal = conformTileFinal;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getConformState() {
        return conformState;
    }

    public void setConformState(int conformState) {
        this.conformState = conformState;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getConformPrice() {
        return conformPrice;
    }

    public void setConformPrice(BigDecimal conformPrice) {
        this.conformPrice = conformPrice;
    }

    public int getIsFreeFreight() {
        return isFreeFreight;
    }

    public void setIsFreeFreight(int isFreeFreight) {
        this.isFreeFreight = isFreeFreight;
    }

    public String getRuleOutAreaIds() {
        return ruleOutAreaIds;
    }

    public void setRuleOutAreaIds(String ruleOutAreaIds) {
        this.ruleOutAreaIds = ruleOutAreaIds;
    }

    public String getRuleOutAreaNames() {
        return ruleOutAreaNames;
    }

    public void setRuleOutAreaNames(String ruleOutAreaNames) {
        this.ruleOutAreaNames = ruleOutAreaNames;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public BigDecimal getTemplatePrice() {
        return templatePrice;
    }

    public void setTemplatePrice(BigDecimal templatePrice) {
        this.templatePrice = templatePrice;
    }

    public Integer getIsGift() {
        if (isGift == null) {
            return 0;
        } else {
            return isGift;
        }
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public List<GiftVo> getGiftVoList() {
        return giftVoList;
    }

    public void setGiftVoList(List<GiftVo> giftVoList) {
        this.giftVoList = giftVoList;
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

    public String getConformStateText() {
        return conformStateText;
    }

    public void setConformStateText(String conformStateText) {
        this.conformStateText = conformStateText;
    }

    public String getContentCartRule() {
        return contentRule;
    }
    public void setContentRule(String contentRule) {
        this.contentRule = contentRule;
    }

    public String getShortRule() {
        return shortRule;
    }

    public void setShortRule(String shortRule) {
        this.shortRule = shortRule;
    }

    @Override
    public String toString() {
        return "Conform{" +
                "conformId=" + conformId +
                ", conformName='" + conformName + '\'' +
                ", conformTitle='" + conformTitle + '\'' +
                ", conformTileFinal='" + conformTileFinal + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", conformState=" + conformState +
                ", limitAmount=" + limitAmount +
                ", conformPrice=" + conformPrice +
                ", isFreeFreight=" + isFreeFreight +
                ", ruleOutAreaIds='" + ruleOutAreaIds + '\'' +
                ", ruleOutAreaNames='" + ruleOutAreaNames + '\'' +
                ", templateId=" + templateId +
                ", templatePrice=" + templatePrice +
                ", isGift=" + isGift +
                ", giftVoList=" + giftVoList +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", conformStateText='" + conformStateText + '\'' +
                ", contentRule='" + contentRule + '\'' +
                ", shortRule='" + shortRule + '\'' +
                '}';
    }
}
