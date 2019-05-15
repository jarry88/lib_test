package com.ftofs.twant.domain.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GoodsCommon implements Serializable,Cloneable {
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
     * Modify By yangjian 2019/1/4 14:42
     */
    private Integer goodsCountry=0;

    /**
     * 商品卖点
     */
    private String jingle;

    /**
     * 商品分类编号
     */
    private int categoryId;

    /**
     * 一级分类
     */
    private int categoryId1;

    /**
     * 二级分类
     */
    private int categoryId2;

    /**
     * 三级分类
     */
    private int categoryId3;

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
     * 品牌编号
     */
    private int brandId;

    /**
     * 商品状态
     * 0下架，1正常，10违规禁售
     */
    private int goodsState;

    /**
     * 违规禁售原因
     */
    private String stateRemark;

    /**
     * 审核状态
     * 0未通过，1已通过，10审核中
     */
    private int goodsVerify;

    /**
     * 审核失败原因
     */
    private String verifyRemark;

    /**
     * 商品创建时间
     */
    private Timestamp createTime;

    /**
     * 商品更新时间
     */
    private Timestamp updateTime;

    /**
     * 一级地区编号
     */
    private int areaId1 = 0;

    /**
     * 二级地区编号
     */
    private int areaId2 = 0;

    /**
     * 省市县(区)内容
     */
    private String areaInfo;

    /**
     * 商品运费
     */
    private BigDecimal goodsFreight =  new BigDecimal("0");

    /**
     * 运费模板ID
     */
    private int freightTemplateId = 0;

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
    private Timestamp promotionStartTime;

    /**
     * 促销结束时间
     */
    private Timestamp promotionEndTime;

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
    private int promotionType = 0;

    /**
     * 活动类型文字
     */
    private String promotionTypeText;

    /**
     * 销售模式
     * 零售1  批发2
     */
    private int goodsModal;

    /**
     * 商品规格名称JSON
     */
    private String goodsSpecNames;

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
     * 拼团编号
     */
    private Integer groupId;

    /**
     * 是否为编辑团购商品
     */
    private int isGroupEdit = 0;

    /**
     * 是否为分销商品
     */
    private Integer isDistribution = 0;

    /**
     * 是否为积分商品
     */
    private Integer isPointsGoods = 0;

    /**
     * 消保编号1
     */
    private Integer contractItem1 = 0;

    /**
     * 消保编号2
     */
    private Integer contractItem2 = 0;

    /**
     * 消保编号3
     */
    private Integer contractItem3 = 0;

    /**
     * 消保编号4
     */
    private Integer contractItem4 = 0;

    /**
     * 消保编号5
     */
    private Integer contractItem5 = 0;

    /**
     * 消保编号6
     */
    private Integer contractItem6 = 0;

    /**
     * 消保编号7
     */
    private Integer contractItem7 = 0;

    /**
     * 消保编号8
     */
    private Integer contractItem8 = 0;

    /**
     * 消保编号9
     */
    private Integer contractItem9 = 0;

    /**
     * 消保编号10
     */
    private Integer contractItem10 = 0;

    /**
     * 搜索优先级
     */
    private Integer searchBoost = 0;

    /**
     * 支持过期退款
     */
    private Integer virtualOverdueRefund = 0;

    /**
     * 海外商品税率
     */
    private BigDecimal foreignTaxRate = BigDecimal.ZERO;

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
     * Modify By liusf 2019/1/9 11:01 被點贊数量
     */
    private int goodsLike = 0;

    /**
     * 評論數量
     */
    private Integer commentCount = 0;

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

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
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

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public String getStateRemark() {
        return stateRemark;
    }

    public void setStateRemark(String stateRemark) {
        this.stateRemark = stateRemark;
    }

    public int getGoodsVerify() {
        return goodsVerify;
    }

    public void setGoodsVerify(int goodsVerify) {
        this.goodsVerify = goodsVerify;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public BigDecimal getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(BigDecimal goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public int getFreightTemplateId() {
        return freightTemplateId;
    }

    public void setFreightTemplateId(int freightTemplateId) {
        this.freightTemplateId = freightTemplateId;
    }

    public BigDecimal getFreightWeight() {
        if (freightWeight == null) {
            return BigDecimal.ZERO;
        }
        return freightWeight;
    }

    public void setFreightWeight(BigDecimal freightWeight) {
        this.freightWeight = freightWeight;
    }

    public BigDecimal getFreightVolume() {
        if (freightVolume == null) {
            return BigDecimal.ZERO;
        }
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
        return (batchNum1 == 0) ? batchNum0End : batchNum1 - 1;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum1End() {
        return (batchNum2 == 0) ? batchNum1End : batchNum2 - 1;
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

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
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
        return getWebPrice0();
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
    }

    public int getWebUsable() {
        return 1;
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
        return getAppPrice0();
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
    }

    public int getAppUsable() {
        return 1;
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
        return getWechatPrice0();
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
    }

    public int getWechatUsable() {
        return 0;
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

    public Timestamp getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(Timestamp promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public Timestamp getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(Timestamp promotionEndTime) {
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

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public String getGoodsSpecNames() {
        return goodsSpecNames;
    }

    public void setGoodsSpecNames(String goodsSpecNames) {
        this.goodsSpecNames = goodsSpecNames;
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
        return imageName;
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

    public int getWeb() {
        return webUsable;
    }

    public int getApp() {
        return appUsable;
    }

    public int getWechat() {
        return wechatUsable;
    }

    public Integer getIsGift() {
        if (isGift == null) {
            return 0;
        }
        return isGift;
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public Integer getGroupId() {
        if (groupId == null) {
            return 0;
        }
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public int getIsGroupEdit() {
        return isGroupEdit;
    }

    public void setIsGroupEdit(int isGroupEdit) {
        this.isGroupEdit = isGroupEdit;
    }

    public Integer getIsDistribution() {
        if (isDistribution == null) {
            return 0;
        }
        return isDistribution;

    }

    public void setIsDistribution(Integer isDistribution) {
        this.isDistribution = isDistribution;
    }

    public Integer getIsPointsGoods() {
        if (isPointsGoods == null) {
            return 0;
        }
        return isPointsGoods;
    }

    public void setIsPointsGoods(Integer isPointsGoods) {
        this.isPointsGoods = isPointsGoods;
    }

    public Integer getContractItem1() {
        return contractItem1;
    }

    public void setContractItem1(Integer contractItem1) {
        this.contractItem1 = contractItem1;
    }

    public Integer getContractItem2() {
        return contractItem2;
    }

    public void setContractItem2(Integer contractItem2) {
        this.contractItem2 = contractItem2;
    }

    public Integer getContractItem3() {
        return contractItem3;
    }

    public void setContractItem3(Integer contractItem3) {
        this.contractItem3 = contractItem3;
    }

    public Integer getContractItem4() {
        return contractItem4;
    }

    public void setContractItem4(Integer contractItem4) {
        this.contractItem4 = contractItem4;
    }

    public Integer getContractItem5() {
        return contractItem5;
    }

    public void setContractItem5(Integer contractItem5) {
        this.contractItem5 = contractItem5;
    }

    public Integer getContractItem6() {
        return contractItem6;
    }

    public void setContractItem6(Integer contractItem6) {
        this.contractItem6 = contractItem6;
    }

    public Integer getContractItem7() {
        return contractItem7;
    }

    public void setContractItem7(Integer contractItem7) {
        this.contractItem7 = contractItem7;
    }

    public Integer getContractItem8() {
        return contractItem8;
    }

    public void setContractItem8(Integer contractItem8) {
        this.contractItem8 = contractItem8;
    }

    public Integer getContractItem9() {
        return contractItem9;
    }

    public void setContractItem9(Integer contractItem9) {
        this.contractItem9 = contractItem9;
    }

    public Integer getContractItem10() {
        return contractItem10;
    }

    public void setContractItem10(Integer contractItem10) {
        this.contractItem10 = contractItem10;
    }

    public Integer getSearchBoost() {
        return searchBoost;
    }

    public void setSearchBoost(Integer searchBoost) {
        this.searchBoost = searchBoost;
    }

    public Integer getVirtualOverdueRefund() {
        return virtualOverdueRefund;
    }

    public void setVirtualOverdueRefund(Integer virtualOverdueRefund) {
        this.virtualOverdueRefund = virtualOverdueRefund;
    }

    public BigDecimal getForeignTaxRate() {
        return foreignTaxRate;
    }

    public void setForeignTaxRate(BigDecimal foreignTaxRate) {
        this.foreignTaxRate = foreignTaxRate;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }

    @Override
    public GoodsCommon clone() throws CloneNotSupportedException {
        return (GoodsCommon) super.clone();
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

    public Integer getGoodsCountry() {
        return goodsCountry;
    }

    public void setGoodsCountry(Integer goodsCountry) {
        this.goodsCountry = goodsCountry;
    }

    public int getGoodsLike() {
        return goodsLike;
    }

    public void setGoodsLike(int goodsLike) {
        this.goodsLike = goodsLike;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "GoodsCommon{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCountry='" + goodsCountry + '\'' +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", specJson='" + specJson + '\'' +
                ", goodsSpecValueJson='" + goodsSpecValueJson + '\'' +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsState=" + goodsState +
                ", stateRemark='" + stateRemark + '\'' +
                ", goodsVerify=" + goodsVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaInfo='" + areaInfo + '\'' +
                ", goodsFreight=" + goodsFreight +
                ", freightTemplateId=" + freightTemplateId +
                ", freightWeight=" + freightWeight +
                ", freightVolume=" + freightVolume +
                ", isCommend=" + isCommend +
                ", batchNum0=" + batchNum0 +
                ", batchNum0End=" + batchNum0End +
                ", batchNum1=" + batchNum1 +
                ", batchNum1End=" + batchNum1End +
                ", batchNum2=" + batchNum2 +
                ", batchPrice0=" + batchPrice0 +
                ", batchPrice1=" + batchPrice1 +
                ", batchPrice2=" + batchPrice2 +
                ", webPrice0=" + webPrice0 +
                ", webPrice1=" + webPrice1 +
                ", webPrice2=" + webPrice2 +
                ", webPriceMin=" + webPriceMin +
                ", webUsable=" + webUsable +
                ", appPrice0=" + appPrice0 +
                ", appPrice1=" + appPrice1 +
                ", appPrice2=" + appPrice2 +
                ", appPriceMin=" + appPriceMin +
                ", appUsable=" + appUsable +
                ", wechatPrice0=" + wechatPrice0 +
                ", wechatPrice1=" + wechatPrice1 +
                ", wechatPrice2=" + wechatPrice2 +
                ", wechatPriceMin=" + wechatPriceMin +
                ", wechatUsable=" + wechatUsable +
                ", promotionId=" + promotionId +
                ", promotionStartTime=" + promotionStartTime +
                ", promotionEndTime=" + promotionEndTime +
                ", promotionState=" + promotionState +
                ", promotionDiscountRate=" + promotionDiscountRate +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", goodsModal=" + goodsModal +
                ", goodsSpecNames='" + goodsSpecNames + '\'' +
                ", unitName='" + unitName + '\'' +
                ", goodsFavorite=" + goodsFavorite +
                ", goodsClick=" + goodsClick +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", imageName='" + imageName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", joinBigSale=" + joinBigSale +
                ", goodsImageList=" + goodsImageList +
                ", goodsList=" + goodsList +
                ", isGift=" + isGift +
                ", groupId=" + groupId +
                ", isGroupEdit=" + isGroupEdit +
                ", isDistribution=" + isDistribution +
                ", isPointsGoods=" + isPointsGoods +
                ", contractItem1=" + contractItem1 +
                ", contractItem2=" + contractItem2 +
                ", contractItem3=" + contractItem3 +
                ", contractItem4=" + contractItem4 +
                ", contractItem5=" + contractItem5 +
                ", contractItem6=" + contractItem6 +
                ", contractItem7=" + contractItem7 +
                ", contractItem8=" + contractItem8 +
                ", contractItem9=" + contractItem9 +
                ", contractItem10=" + contractItem10 +
                ", searchBoost=" + searchBoost +
                ", virtualOverdueRefund=" + virtualOverdueRefund +
                ", foreignTaxRate=" + foreignTaxRate +
                ", isVirtual=" + isVirtual +
                ", goodsVideo='" + goodsVideo + '\'' +
                ", detailVideo='" + detailVideo + '\'' +
                ", isWaterMark=" + isWaterMark +
                ", waterMarkImage='" + waterMarkImage + '\'' +
                ", waterMarkPosition='" + waterMarkPosition + '\'' +
                '}';
    }
}
