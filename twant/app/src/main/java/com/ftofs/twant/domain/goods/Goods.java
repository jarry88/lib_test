package com.ftofs.twant.domain.goods;

import com.ftofs.twant.vo.promotion.GiftVo;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

public class Goods implements Serializable,Cloneable {
    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SPU
     */
    private int commonId;

    /**
     * 规格
     * 例“红色,,,L”
     */
    private String goodsSpecs="";

    /**
     * 规格字符串
     */
    private String goodsSpecString = "";

    /**
     * 完整规格
     * 例“颜色：红色；尺码：L”
     */
    private String goodsFullSpecs="";

    /**
     * 所选规格值编号
     * 列 “1,2,3,4”
     */
    private String specValueIds="";

    /**
     * 商品价格0
     */
    private BigDecimal goodsPrice0 = BigDecimal.ZERO;

    /**
     * 商品价格1
     */
    private BigDecimal goodsPrice1 = BigDecimal.ZERO;

    /**
     * 商品价格2
     */
    private BigDecimal goodsPrice2 = BigDecimal.ZERO;

    /**
     * PC端起购价0
     */
    private BigDecimal webPrice0 = BigDecimal.ZERO;

    /**
     * PC端起购价1
     */
    private BigDecimal webPrice1 = BigDecimal.ZERO;

    /**
     * PC端起购价2
     */
    private BigDecimal webPrice2 = BigDecimal.ZERO;

    /**
     * PC端促销进行状态
     */
    private int webUsable = 0;

    /**
     * APP端起购价0
     */
    private BigDecimal appPrice0 = BigDecimal.ZERO;

    /**
     * APP端起购价1
     */
    private BigDecimal appPrice1 = BigDecimal.ZERO;

    /**
     * APP端起购价2
     */
    private BigDecimal appPrice2 = BigDecimal.ZERO;

    /**
     * APP端促销进行状态
     */
    private int appUsable = 0;

    /**
     * 微信端起购价0
     */
    private BigDecimal wechatPrice0 = BigDecimal.ZERO;

    /**
     * 微信端起购价1
     */
    private BigDecimal wechatPrice1 = BigDecimal.ZERO;

    /**
     * 微信端起购价2
     */
    private BigDecimal wechatPrice2 = BigDecimal.ZERO;

    /**
     * 微信端促销进行状态
     */
    private int wechatUsable = 0;

    /**
     * 限时折扣促销编号
     */
    private int promotionId = 0;

    /**
     * 促销开始时间
     */
    private String promotionStartTime="";

    /**
     * 促销结束时间
     */
    private String promotionEndTime="";

    /**
     * 活动状态
     */
    private int promotionState = 0;

    /**
     * 活动类型
     */
    private int promotionType = 0;

    /**
     * 活动类型文字
     */
    private String promotionTypeText="";

    /**
     * 折扣标题
     */
    private String promotionTitle="";

    /**
     * 商品货号
     */
    private String goodsSerial="";

    /**
     * 颜色规格值编号
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId=0;

    /**
     * 库存
     */
    private int goodsStorage=0;

    /**
     * 图片名称
     */
    private String imageName="";

    /**
     * 图片路径
     */
    private String imageSrc="";

    /**
     * 赠品列表
     */
    private List<GiftVo> giftVoList = new ArrayList<>();

    /**
     * 是否有赠品
     */
    private Integer isGift;

    /**
     * 拼团商品标记
     */
    private Integer isGroup;

    /**
     * 拼团价格
     */
    private BigDecimal groupPrice = BigDecimal.ZERO;

    /**
     * 限购数量
     */
    private int limitAmount = 0;

    /**
     * 秒杀商品标记
     */
    private int isSeckill;

    /**
     * 預留庫存
     */
    private int reserveStorage;

    /**
     * 條碼
     */
    private String barCode;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }

    public String getGoodsSpecString() {
        return goodsSpecString;
    }

    public void setGoodsSpecString(String goodsSpecString) {
        this.goodsSpecString = goodsSpecString;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    public BigDecimal getGoodsPrice0() {
        return goodsPrice0;
    }

    public void setGoodsPrice0(BigDecimal goodsPrice0) {
        this.goodsPrice0 = goodsPrice0;
    }

    public BigDecimal getGoodsPrice1() {
        return goodsPrice1;
    }

    public void setGoodsPrice1(BigDecimal goodsPrice1) {
        this.goodsPrice1 = goodsPrice1;
    }

    public BigDecimal getGoodsPrice2() {
        return goodsPrice2;
    }

    public void setGoodsPrice2(BigDecimal goodsPrice2) {
        this.goodsPrice2 = goodsPrice2;
    }

    public BigDecimal getWebPrice0() {
        return webPrice0;
    }

    public void setWebPrice0(BigDecimal webPrice0) {
        this.webPrice0 = webPrice0;
    }

    public BigDecimal getWebPrice1() {
        return webPrice1;
    }

    public void setWebPrice1(BigDecimal webPrice1) {
        this.webPrice1 = webPrice1;
    }

    public BigDecimal getWebPrice2() {
        return webPrice2;
    }

    public void setWebPrice2(BigDecimal webPrice2) {
        this.webPrice2 = webPrice2;
    }

    public int getWebUsable() {
        return webUsable;
    }

    public void setWebUsable(int webUsable) {
        this.webUsable = webUsable;
    }

    public BigDecimal getAppPrice0() {
        return appPrice0;
    }

    public void setAppPrice0(BigDecimal appPrice0) {
        this.appPrice0 = appPrice0;
    }

    public BigDecimal getAppPrice1() {
        return appPrice1;
    }

    public void setAppPrice1(BigDecimal appPrice1) {
        this.appPrice1 = appPrice1;
    }

    public BigDecimal getAppPrice2() {
        return appPrice2;
    }

    public void setAppPrice2(BigDecimal appPrice2) {
        this.appPrice2 = appPrice2;
    }

    public int getAppUsable() {
        return appUsable;
    }

    public void setAppUsable(int appUsable) {
        this.appUsable = appUsable;
    }

    public BigDecimal getWechatPrice0() {
        return wechatPrice0;
    }

    public void setWechatPrice0(BigDecimal wechatPrice0) {
        this.wechatPrice0 = wechatPrice0;
    }

    public BigDecimal getWechatPrice1() {
        return wechatPrice1;
    }

    public void setWechatPrice1(BigDecimal wechatPrice1) {
        this.wechatPrice1 = wechatPrice1;
    }

    public BigDecimal getWechatPrice2() {
        return wechatPrice2;
    }

    public void setWechatPrice2(BigDecimal wechatPrice2) {
        this.wechatPrice2 = wechatPrice2;
    }

    public int getWechatUsable() {
        return wechatUsable;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(String promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public String getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(String promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    public int getPromotionState() {
        return promotionState;
    }

    public void setPromotionState(int promotionState) {
        this.promotionState = promotionState;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionTypeText() {
        return promotionTypeText;
    }

    public void setPromotionTypeText(String promotionTypeText) {
        this.promotionTypeText = promotionTypeText;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public List<GiftVo> getGiftVoList() {
        return giftVoList;
    }

    public void setGiftVoList(List<GiftVo> giftVoList) {
        this.giftVoList = giftVoList;
    }

    public Integer getIsGift() {
        return isGift;
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }

    public BigDecimal getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(BigDecimal groupPrice) {
        this.groupPrice = groupPrice;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(int isSeckill) {
        this.isSeckill = isSeckill;
    }

    public int getReserveStorage() {
        return reserveStorage;
    }

    public void setReserveStorage(int reserveStorage) {
        this.reserveStorage = reserveStorage;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsSpecs='" + goodsSpecs + '\'' +
                ", goodsSpecString='" + goodsSpecString + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", specValueIds='" + specValueIds + '\'' +
                ", goodsPrice0=" + goodsPrice0 +
                ", goodsPrice1=" + goodsPrice1 +
                ", goodsPrice2=" + goodsPrice2 +
                ", webPrice0=" + webPrice0 +
                ", webPrice1=" + webPrice1 +
                ", webPrice2=" + webPrice2 +
                ", webUsable=" + webUsable +
                ", appPrice0=" + appPrice0 +
                ", appPrice1=" + appPrice1 +
                ", appPrice2=" + appPrice2 +
                ", appUsable=" + appUsable +
                ", wechatPrice0=" + wechatPrice0 +
                ", wechatPrice1=" + wechatPrice1 +
                ", wechatPrice2=" + wechatPrice2 +
                ", wechatUsable=" + wechatUsable +
                ", promotionId=" + promotionId +
                ", promotionStartTime=" + promotionStartTime +
                ", promotionEndTime=" + promotionEndTime +
                ", promotionState=" + promotionState +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", colorId=" + colorId +
                ", goodsStorage=" + goodsStorage +
                ", imageName='" + imageName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", giftVoList=" + giftVoList +
                ", isGift=" + isGift +
                ", isGroup=" + isGroup +
                ", groupPrice=" + groupPrice +
                ", limitAmount=" + limitAmount +
                ", isSeckill=" + isSeckill +
                '}';
    }
}
