package com.ftofs.twant.vo;

import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.vo.goods.BatchNumPriceVo;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品实体，用于搜索引擎建立索引
 *
 * @author dqw
 * Created 2017/4/17 11:56
 */
public class SearchGoodsVo {
    /**
     * 商品SPU编号
     */
    private int commonId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品卖点
     */
    private String jingle;
    /**
     * 商品规格字符串
     */
    private String specString;
    /**
     * 商品sku规格列表
     */
    private List<SearchGoodsSpecVo> goodsSpecList;
    /**
     * 商品分类编号
     */
    private int categoryId;
    /**
     * 商品分类编号（三级）
     */
    private List<Integer> categoryIds;
    /**
     * 商品分类名称
     */
    private String categoryName = "";
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 品牌编号
     */
    private int brandId;
    /**
     * 品牌名称
     */
    private String brandName = "";
    /**
     * 品牌英文名称
     */
    private String brandEnglish = "";
    /**
     * 店铺商品分类
     */
    private List<Integer> labelIdList;
    /**
     * 是否参加促销
     * 1参加促销
     */
    private int joinBigSale;
    /**
     * 商品状态<br>
     * 0下架，1正常，10违规禁售
     */
    private int goodsState;
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */
    private int goodsVerify;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    private int goodsStatus = 0;
    /**
     * 省市县(区)内容
     */
    private String areaInfo;
    /**
     * 商品运费
     */
    private BigDecimal goodsFreight;
    /**
     * 运费模板ID
     */
    private int freightTemplateId = 0;
    /**
     * 配送区域
     */
    private List<Integer> freightArea;
    /**
     * 起购量0
     */
    private int batchNum0;
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
    private BigDecimal webPrice0;
    /**
     * PC端起购价1
     */
    private BigDecimal webPrice1;
    /**
     * PC端起购价2
     */
    private BigDecimal webPrice2;
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
    private BigDecimal appPrice0;
    /**
     * APP端起购价1
     */
    private BigDecimal appPrice1;
    /**
     * APP端起购价2
     */
    private BigDecimal appPrice2;
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
    private BigDecimal wechatPrice0;
    /**
     * 微信端起购价1
     */
    private BigDecimal wechatPrice1;
    /**
     * 微信端起购价2
     */
    private BigDecimal wechatPrice2;
    /**
     * 商品最低价
     */
    private BigDecimal wechatPriceMin;
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable = 0;
    /**
     * 计量单位
     */
    private String unitName = "";
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
     * 活动类型
     */
    private int promotionType = 1;
    /**
     * 销售模式
     * 零售1  批发2
     */
    private int goodsModal;
    /**
     * 被關注数量
     */
    private int goodsFavorite = 0;
    /**
     * 评价数量
     */
    private int evaluateNum = 0;
    /**
     * 好评率
     */
    private int goodsRate = 0;
    /**
     * 销售数量
     */
    private int goodsSaleNum = 0;
    /**
     * 图片名称
     */
    private String imageName;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 商品sku图片列表
     */
    private List<GoodsImage> goodsImageList;
    /**
     * 阶梯下个，key是区间，value价格
     */
    private List<BatchNumPriceVo> batchNumPriceVoList = new ArrayList<>();
    /**
     * 是否有赠品
     */
    private int isGift;
    /**
     * 店铺名称
     */
    private String storeName = "";
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    private int isOwnShop = 0;
    /**
     * 卖家id</br>
     */
    private int sellerId;
    /**
     * 分佣比例
     */
    private int commissionRate;
    /**
     * web佣金
     */
    private BigDecimal webCommission;
    /**
     * app佣金
     */
    private BigDecimal appCommission;
    /**
     * wechat佣金
     */
    private BigDecimal wechatCommission;
    /**
     * 可用店铺券
     */
    private int usableVoucher;
    /**
     * 总推广量（订单数量）
     */
    private long ordersCount;
    /**
     * 总佣金
     */
    private BigDecimal commissionTotal;
    /**
     * 属性
     */
    private List<Integer> attr;
    /**
     * 是否为分销商品
     */
    private int isDistribution;
    /**
     * 搜索优先级
     */
    private int searchBoost;
    /**
     * 预留字段
     */
    private String extendString0 = "";
    private String extendString1 = "";
    private String extendString2 = "";
    private String extendString3 = "";
    private String extendString4 = "";
    private String extendString5 = "";
    private String extendString6 = "";
    private String extendString7 = "";
    private String extendString8 = "";
    private String extendString9 = "";

    /**
     * 品牌所在地
     */
    private int extendInt0 = 0;

    /**
     * 評論數量
     */
    private int extendInt1 = 0;

    /**
     * 1是0否含有商品視頻
     */
    private int extendInt2 = 0;

    private int extendInt3 = 0;
    private int extendInt4 = 0;
    private int extendInt5 = 0;
    private int extendInt6 = 0;
    private int extendInt7 = 0;
    private int extendInt8 = 0;
    private int extendInt9 = 0;
    private BigDecimal extendPrice0 = BigDecimal.ZERO;
    private BigDecimal extendPrice1 = BigDecimal.ZERO;
    private BigDecimal extendPrice2 = BigDecimal.ZERO;
    private BigDecimal extendPrice3 = BigDecimal.ZERO;
    private BigDecimal extendPrice4 = BigDecimal.ZERO;
    private BigDecimal extendPrice5 = BigDecimal.ZERO;
    private BigDecimal extendPrice6 = BigDecimal.ZERO;
    private BigDecimal extendPrice7 = BigDecimal.ZERO;
    private BigDecimal extendPrice8 = BigDecimal.ZERO;
    private BigDecimal extendPrice9 = BigDecimal.ZERO;

    //Modify By liusf 2019/1/2 14:58 商品創建時間
    private String extendTime0;

    private String extendTime1;
    private String extendTime2;
    private String extendTime3;
    private String extendTime4;
    private String extendTime5;
    private String extendTime6;
    private String extendTime7;
    private String extendTime8;
    private String extendTime9;

    public SearchGoodsVo() {
    }

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

    public String getSpecString() {
        return specString;
    }

    public void setSpecString(String specString) {
        this.specString = specString;
    }

    public List<SearchGoodsSpecVo> getGoodsSpecList() {
        return goodsSpecList;
    }

    public void setGoodsSpecList(List<SearchGoodsSpecVo> goodsSpecList) {
        this.goodsSpecList = goodsSpecList;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandEnglish() {
        return brandEnglish;
    }

    public void setBrandEnglish(String brandEnglish) {
        this.brandEnglish = brandEnglish;
    }

    public List<Integer> getLabelIdList() {
        return labelIdList;
    }

    public void setLabelIdList(List<Integer> labelIdList) {
        this.labelIdList = labelIdList;
    }

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public int getGoodsVerify() {
        return goodsVerify;
    }

    public void setGoodsVerify(int goodsVerify) {
        this.goodsVerify = goodsVerify;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
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

    public List<Integer> getFreightArea() {
        return freightArea;
    }

    public void setFreightArea(List<Integer> freightArea) {
        this.freightArea = freightArea;
    }

    public int getBatchNum0() {
        return batchNum0;
    }

    public void setBatchNum0(int batchNum0) {
        this.batchNum0 = batchNum0;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(int goodsRate) {
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

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public List<BatchNumPriceVo> getBatchNumPriceVoList() {
        return batchNumPriceVoList;
    }

    public void setBatchNumPriceVoList(List<BatchNumPriceVo> batchNumPriceVoList) {
        this.batchNumPriceVoList = batchNumPriceVoList;
    }

    public int getBatchNum0End() {
        return batchNum0End;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public int getBatchNum1End() {
        return batchNum1End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getWebCommission() {
        return webCommission;
    }

    public void setWebCommission(BigDecimal webCommission) {
        this.webCommission = webCommission;
    }

    public BigDecimal getAppCommission() {
        return appCommission;
    }

    public void setAppCommission(BigDecimal appCommission) {
        this.appCommission = appCommission;
    }

    public BigDecimal getWechatCommission() {
        return wechatCommission;
    }

    public void setWechatCommission(BigDecimal wechatCommission) {
        this.wechatCommission = wechatCommission;
    }

    public int getUsableVoucher() {
        return usableVoucher;
    }

    public void setUsableVoucher(int usableVoucher) {
        this.usableVoucher = usableVoucher;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public List<Integer> getAttr() {
        return attr;
    }

    public void setAttr(List<Integer> attr) {
        this.attr = attr;
    }

    public int getIsDistribution() {
        return isDistribution;
    }

    public void setIsDistribution(int isDistribution) {
        this.isDistribution = isDistribution;
    }

    public int getSearchBoost() {
        return searchBoost;
    }

    public void setSearchBoost(int searchBoost) {
        this.searchBoost = searchBoost;
    }

    public String getExtendString0() {
        return extendString0;
    }

    public void setExtendString0(String extendString0) {
        this.extendString0 = extendString0;
    }

    public String getExtendString1() {
        return extendString1;
    }

    public void setExtendString1(String extendString1) {
        this.extendString1 = extendString1;
    }

    public String getExtendString2() {
        return extendString2;
    }

    public void setExtendString2(String extendString2) {
        this.extendString2 = extendString2;
    }

    public String getExtendString3() {
        return extendString3;
    }

    public void setExtendString3(String extendString3) {
        this.extendString3 = extendString3;
    }

    public String getExtendString4() {
        return extendString4;
    }

    public void setExtendString4(String extendString4) {
        this.extendString4 = extendString4;
    }

    public String getExtendString5() {
        return extendString5;
    }

    public void setExtendString5(String extendString5) {
        this.extendString5 = extendString5;
    }

    public String getExtendString6() {
        return extendString6;
    }

    public void setExtendString6(String extendString6) {
        this.extendString6 = extendString6;
    }

    public String getExtendString7() {
        return extendString7;
    }

    public void setExtendString7(String extendString7) {
        this.extendString7 = extendString7;
    }

    public String getExtendString8() {
        return extendString8;
    }

    public void setExtendString8(String extendString8) {
        this.extendString8 = extendString8;
    }

    public String getExtendString9() {
        return extendString9;
    }

    public void setExtendString9(String extendString9) {
        this.extendString9 = extendString9;
    }

    public int getExtendInt0() {
        return extendInt0;
    }

    public void setExtendInt0(int extendInt0) {
        this.extendInt0 = extendInt0;
    }

    public int getExtendInt1() {
        return extendInt1;
    }

    public void setExtendInt1(int extendInt1) {
        this.extendInt1 = extendInt1;
    }

    public int getExtendInt2() {
        return extendInt2;
    }

    public void setExtendInt2(int extendInt2) {
        this.extendInt2 = extendInt2;
    }

    public int getExtendInt3() {
        return extendInt3;
    }

    public void setExtendInt3(int extendInt3) {
        this.extendInt3 = extendInt3;
    }

    public int getExtendInt4() {
        return extendInt4;
    }

    public void setExtendInt4(int extendInt4) {
        this.extendInt4 = extendInt4;
    }

    public int getExtendInt5() {
        return extendInt5;
    }

    public void setExtendInt5(int extendInt5) {
        this.extendInt5 = extendInt5;
    }

    public int getExtendInt6() {
        return extendInt6;
    }

    public void setExtendInt6(int extendInt6) {
        this.extendInt6 = extendInt6;
    }

    public int getExtendInt7() {
        return extendInt7;
    }

    public void setExtendInt7(int extendInt7) {
        this.extendInt7 = extendInt7;
    }

    public int getExtendInt8() {
        return extendInt8;
    }

    public void setExtendInt8(int extendInt8) {
        this.extendInt8 = extendInt8;
    }

    public int getExtendInt9() {
        return extendInt9;
    }

    public void setExtendInt9(int extendInt9) {
        this.extendInt9 = extendInt9;
    }

    public BigDecimal getExtendPrice0() {
        return extendPrice0;
    }

    public void setExtendPrice0(BigDecimal extendPrice0) {
        this.extendPrice0 = extendPrice0;
    }

    public BigDecimal getExtendPrice1() {
        return extendPrice1;
    }

    public void setExtendPrice1(BigDecimal extendPrice1) {
        this.extendPrice1 = extendPrice1;
    }

    public BigDecimal getExtendPrice2() {
        return extendPrice2;
    }

    public void setExtendPrice2(BigDecimal extendPrice2) {
        this.extendPrice2 = extendPrice2;
    }

    public BigDecimal getExtendPrice3() {
        return extendPrice3;
    }

    public void setExtendPrice3(BigDecimal extendPrice3) {
        this.extendPrice3 = extendPrice3;
    }

    public BigDecimal getExtendPrice4() {
        return extendPrice4;
    }

    public void setExtendPrice4(BigDecimal extendPrice4) {
        this.extendPrice4 = extendPrice4;
    }

    public BigDecimal getExtendPrice5() {
        return extendPrice5;
    }

    public void setExtendPrice5(BigDecimal extendPrice5) {
        this.extendPrice5 = extendPrice5;
    }

    public BigDecimal getExtendPrice6() {
        return extendPrice6;
    }

    public void setExtendPrice6(BigDecimal extendPrice6) {
        this.extendPrice6 = extendPrice6;
    }

    public BigDecimal getExtendPrice7() {
        return extendPrice7;
    }

    public void setExtendPrice7(BigDecimal extendPrice7) {
        this.extendPrice7 = extendPrice7;
    }

    public BigDecimal getExtendPrice8() {
        return extendPrice8;
    }

    public void setExtendPrice8(BigDecimal extendPrice8) {
        this.extendPrice8 = extendPrice8;
    }

    public BigDecimal getExtendPrice9() {
        return extendPrice9;
    }

    public void setExtendPrice9(BigDecimal extendPrice9) {
        this.extendPrice9 = extendPrice9;
    }

    public String getExtendTime0() {
        return extendTime0;
    }

    public void setExtendTime0(String extendTime0) {
        this.extendTime0 = extendTime0;
    }

    public String getExtendTime1() {
        return extendTime1;
    }

    public void setExtendTime1(String extendTime1) {
        this.extendTime1 = extendTime1;
    }

    public String getExtendTime2() {
        return extendTime2;
    }

    public void setExtendTime2(String extendTime2) {
        this.extendTime2 = extendTime2;
    }

    public String getExtendTime3() {
        return extendTime3;
    }

    public void setExtendTime3(String extendTime3) {
        this.extendTime3 = extendTime3;
    }

    public String getExtendTime4() {
        return extendTime4;
    }

    public void setExtendTime4(String extendTime4) {
        this.extendTime4 = extendTime4;
    }

    public String getExtendTime5() {
        return extendTime5;
    }

    public void setExtendTime5(String extendTime5) {
        this.extendTime5 = extendTime5;
    }

    public String getExtendTime6() {
        return extendTime6;
    }

    public void setExtendTime6(String extendTime6) {
        this.extendTime6 = extendTime6;
    }

    public String getExtendTime7() {
        return extendTime7;
    }

    public void setExtendTime7(String extendTime7) {
        this.extendTime7 = extendTime7;
    }

    public String getExtendTime8() {
        return extendTime8;
    }

    public void setExtendTime8(String extendTime8) {
        this.extendTime8 = extendTime8;
    }

    public String getExtendTime9() {
        return extendTime9;
    }

    public void setExtendTime9(String extendTime9) {
        this.extendTime9 = extendTime9;
    }

    @Override
    public String toString() {
        return "SearchGoodsVo{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", specString='" + specString + '\'' +
                ", goodsSpecList=" + goodsSpecList +
                ", categoryId=" + categoryId +
                ", categoryIds=" + categoryIds +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", brandEnglish='" + brandEnglish + '\'' +
                ", labelIdList=" + labelIdList +
                ", joinBigSale=" + joinBigSale +
                ", goodsState=" + goodsState +
                ", goodsVerify=" + goodsVerify +
                ", goodsStatus=" + goodsStatus +
                ", areaInfo='" + areaInfo + '\'' +
                ", goodsFreight=" + goodsFreight +
                ", freightTemplateId=" + freightTemplateId +
                ", freightArea=" + freightArea +
                ", batchNum0=" + batchNum0 +
                ", batchNum1=" + batchNum1 +
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
                ", unitName='" + unitName + '\'' +
                ", promotionId=" + promotionId +
                ", promotionStartTime=" + promotionStartTime +
                ", promotionEndTime=" + promotionEndTime +
                ", promotionState=" + promotionState +
                ", promotionType=" + promotionType +
                ", goodsModal=" + goodsModal +
                ", goodsFavorite=" + goodsFavorite +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", imageName='" + imageName + '\'' +
                ", goodsImageList=" + goodsImageList +
                ", isGift=" + isGift +
                ", categoryName='" + categoryName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", sellerId=" + sellerId +
                ", commissionRate=" + commissionRate +
                ", webCommission=" + webCommission +
                ", appCommission=" + appCommission +
                ", wechatCommission=" + wechatCommission +
                ", usableVoucher=" + usableVoucher +
                ", ordersCount=" + ordersCount +
                ", commissionTotal=" + commissionTotal +
                ", attr=" + attr +
                ", isDistribution=" + isDistribution +
                ", searchBoost=" + searchBoost +
                ", extendString0='" + extendString0 + '\'' +
                ", extendString1='" + extendString1 + '\'' +
                ", extendString2='" + extendString2 + '\'' +
                ", extendString3='" + extendString3 + '\'' +
                ", extendString4='" + extendString4 + '\'' +
                ", extendString5='" + extendString5 + '\'' +
                ", extendString6='" + extendString6 + '\'' +
                ", extendString7='" + extendString7 + '\'' +
                ", extendString8='" + extendString8 + '\'' +
                ", extendString9='" + extendString9 + '\'' +
                ", extendInt0=" + extendInt0 +
                ", extendInt1=" + extendInt1 +
                ", extendInt2=" + extendInt2 +
                ", extendInt3=" + extendInt3 +
                ", extendInt4=" + extendInt4 +
                ", extendInt5=" + extendInt5 +
                ", extendInt6=" + extendInt6 +
                ", extendInt7=" + extendInt7 +
                ", extendInt8=" + extendInt8 +
                ", extendInt9=" + extendInt9 +
                ", extendPrice0=" + extendPrice0 +
                ", extendPrice1=" + extendPrice1 +
                ", extendPrice2=" + extendPrice2 +
                ", extendPrice3=" + extendPrice3 +
                ", extendPrice4=" + extendPrice4 +
                ", extendPrice5=" + extendPrice5 +
                ", extendPrice6=" + extendPrice6 +
                ", extendPrice7=" + extendPrice7 +
                ", extendPrice8=" + extendPrice8 +
                ", extendPrice9=" + extendPrice9 +
                ", extendTime0=" + extendTime0 +
                ", extendTime1=" + extendTime1 +
                ", extendTime2=" + extendTime2 +
                ", extendTime3=" + extendTime3 +
                ", extendTime4=" + extendTime4 +
                ", extendTime5=" + extendTime5 +
                ", extendTime6=" + extendTime6 +
                ", extendTime7=" + extendTime7 +
                ", extendTime8=" + extendTime8 +
                ", extendTime9=" + extendTime9 +
                '}';
    }
}

