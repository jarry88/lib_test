package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsImage;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liusf
 * @create 2019/2/27 15:21
 * @description  商品導出Excel
 */
public class GoodsCommonExport {
    /**
     * 商品SPU编号
     */
    private int commonId;
    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品產地(國家)
     */
    private String goodsCountry;
    /**
     * 商品卖点
     */

    private String jingle;
    /**
     * 一级分类
     */
    private String categoryId1;
    /**
     * 二级分类
     */
    private String categoryId2;
    /**
     * 三级分类
     */
    private String categoryId3;
    /**
     * 规格JSON
     */
    private String specJson;
    /**
     * 商品编号和规格值编号JSON
     */
    private String goodsSpecValueJson;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 品牌名稱
     */
    private String brandName;
    /**
     * 商品创建时间
     */
    private String createTime;
    /**
     * 商品更新时间
     */
    private String updateTime;
    /**
     * 商品运费
     */
    private BigDecimal goodsFreight =  new BigDecimal("0");
    /**
     * 商品重量
     */
    private BigDecimal freightWeight = BigDecimal.ZERO;
    /**
     * 商品体积
     */
    private BigDecimal freightVolume = BigDecimal.ZERO;
    /**
     * 是否推荐
     */
    private int isCommend;
    /**
     * 起购量0
     */
    private int batchNum0 = 1;
    /**
     * 起购量0  终点
     */
    private int batchNum0End = 0;
    /**
     * 起购量1
     */
    private int batchNum1;
    /**
     * 起购点1 终点
     */
    private int batchNum1End = 0;
    /**
     * 起购量2
     */
    private int batchNum2;
    /**
     * 起购价0
     */
    private BigDecimal batchPrice0;
    /**
     * 起购价1
     */
    private BigDecimal batchPrice1;
    /**
     * 起购价2
     */
    private BigDecimal batchPrice2;
    /**
     * PC端起购价0
     */
    private BigDecimal webPrice0 = new BigDecimal(0);
    /**
     * PC端起购价1
     */
    private BigDecimal webPrice1 = new BigDecimal(0);
    /**
     * PC端起购价2
     */
    private BigDecimal webPrice2 = new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal webPriceMin;
    /**
     * PC端促销进行状态
     */
    private int webUsable = 0;
    /**
     * APP端起购价0
     */
    private BigDecimal appPrice0 = new BigDecimal(0);
    /**
     * APP端起购价1
     */
    private BigDecimal appPrice1 = new BigDecimal(0);
    /**
     * APP端起购价2
     */
    private BigDecimal appPrice2 = new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal appPriceMin;
    /**
     * APP端促销进行状态
     */
    private int appUsable = 0;
    /**
     * 微信端起购价0
     */
    private BigDecimal wechatPrice0 = new BigDecimal(0);
    /**
     * 微信端起购价1
     */
    private BigDecimal wechatPrice1 = new BigDecimal(0);
    /**
     * 微信端起购价2
     */
    private BigDecimal wechatPrice2 = new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal wechatPriceMin;
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable = 0;
    /**
     * 促销编号
     */
    private int promotionId = 0;
    /**
     * 促销开始时间
     */
    private String promotionStartTime;
    /**
     * 促销结束时间
     */
    private String promotionEndTime;
    /**
     * 活动状态
     */
    private int promotionState = 0;
    /**
     * 折扣
     */
    private Double promotionDiscountRate = 0.0;
    /**
     * 活动类型
     */
    private int promotionType = 1;
    /**
     * 活动类型文字
     */
    private String promotionTypeText;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 被關注数量
     */
    private int goodsFavorite = 0;
    /**
     * 被点击数量
     */
    private int goodsClick = 0;
    /**
     * 评价数量
     */
    private Integer evaluateNum = 0;
    /**
     * 好评率
     */
    private Integer goodsRate = 100;
    /**
     * 销售数量
     */
    private int goodsSaleNum = 0;
    /**
     * 图片名称
     */
    private String imageName = "";
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 是否参加促销
     * 1参加促销
     */
    private int joinBigSale = 1;
    /**
     * 商品图片
     */
    private List<GoodsImage> goodsImageList = new ArrayList<>();
    /**
     * 商品列表
     */
    private List<Goods> goodsList = new ArrayList<>();
    /**
     * 是否有赠品
     */
    private Integer isGift;
    /**
     * 搜索优先级
     */
    private Integer searchBoost = 0;
    /**
     * 是否虛擬商品 0否 1是
     */
    private Integer isVirtual = 0;
    /**
     * 商品視頻鏈接
     */
    private String goodsVideo;
    /**
     * 介紹視頻鏈接
     */
    private String detailVideo;
    /**
     * 是否开启水印 0否1是
     */
    private int isWaterMark;
    /**
     * 水印图片地址
     */
    private String waterMarkImage;

    /**
     * 水印位置-东南西北英文缩写
     */
    private String waterMarkPosition;

    /**
     * 點贊數量
     */
    private int goodsLike = 0;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCountry() {
        return goodsCountry;
    }

    public void setGoodsCountry(String goodsCountry) {
        this.goodsCountry = goodsCountry;
    }

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public String getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(String categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public String getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(String categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(String categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getSpecJson() {
        return specJson;
    }

    public void setSpecJson(String specJson) {
        this.specJson = specJson;
    }

    public String getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(String goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(BigDecimal goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public BigDecimal getFreightWeight() {
        return freightWeight;
    }

    public void setFreightWeight(BigDecimal freightWeight) {
        this.freightWeight = freightWeight;
    }

    public BigDecimal getFreightVolume() {
        return freightVolume;
    }

    public void setFreightVolume(BigDecimal freightVolume) {
        this.freightVolume = freightVolume;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
    }

    public int getBatchNum0() {
        return batchNum0;
    }

    public void setBatchNum0(int batchNum0) {
        this.batchNum0 = batchNum0;
    }

    public int getBatchNum0End() {
        return batchNum0End;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum1End() {
        return batchNum1End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
    }

    public int getBatchNum2() {
        return batchNum2;
    }

    public void setBatchNum2(int batchNum2) {
        this.batchNum2 = batchNum2;
    }

    public BigDecimal getBatchPrice0() {
        return batchPrice0;
    }

    public void setBatchPrice0(BigDecimal batchPrice0) {
        this.batchPrice0 = batchPrice0;
    }

    public BigDecimal getBatchPrice1() {
        return batchPrice1;
    }

    public void setBatchPrice1(BigDecimal batchPrice1) {
        this.batchPrice1 = batchPrice1;
    }

    public BigDecimal getBatchPrice2() {
        return batchPrice2;
    }

    public void setBatchPrice2(BigDecimal batchPrice2) {
        this.batchPrice2 = batchPrice2;
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

    public BigDecimal getWebPriceMin() {
        return webPriceMin;
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
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

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
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

    public BigDecimal getWechatPriceMin() {
        return wechatPriceMin;
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
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

    public Double getPromotionDiscountRate() {
        return promotionDiscountRate;
    }

    public void setPromotionDiscountRate(Double promotionDiscountRate) {
        this.promotionDiscountRate = promotionDiscountRate;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(Integer goodsRate) {
        this.goodsRate = goodsRate;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
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

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public Integer getIsGift() {
        return isGift;
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public Integer getSearchBoost() {
        return searchBoost;
    }

    public void setSearchBoost(Integer searchBoost) {
        this.searchBoost = searchBoost;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getGoodsVideo() {
        return goodsVideo;
    }

    public void setGoodsVideo(String goodsVideo) {
        this.goodsVideo = goodsVideo;
    }

    public String getDetailVideo() {
        return detailVideo;
    }

    public void setDetailVideo(String detailVideo) {
        this.detailVideo = detailVideo;
    }

    public int getIsWaterMark() {
        return isWaterMark;
    }

    public void setIsWaterMark(int isWaterMark) {
        this.isWaterMark = isWaterMark;
    }

    public String getWaterMarkImage() {
        return waterMarkImage;
    }

    public void setWaterMarkImage(String waterMarkImage) {
        this.waterMarkImage = waterMarkImage;
    }

    public String getWaterMarkPosition() {
        return waterMarkPosition;
    }

    public void setWaterMarkPosition(String waterMarkPosition) {
        this.waterMarkPosition = waterMarkPosition;
    }

    public int getGoodsLike() {
        return goodsLike;
    }

    public void setGoodsLike(int goodsLike) {
        this.goodsLike = goodsLike;
    }
}
