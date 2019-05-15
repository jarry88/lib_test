package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.vo.promotion.GiftVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liusf
 * @create 2019/1/9 16:02
 * @description 商品详情视图类
 * @params
 * @return
 */
public class GoodsInfoVo {
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
    private Timestamp promotionStartTime=new Timestamp(0);
    /**
     * 促销结束时间
     */
    private Timestamp promotionEndTime=new Timestamp(0);
    /**
     * 活动状态
     */
    private int promotionState = 0;
    /**
     * 活动类型
     */
    private int promotionType = 1;
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
     * 颜色规格值编号<br>
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

    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList = new ArrayList<>();

    /**
     * 商品SPU信息
     */
    private String goodsName;
    private String jingle;
    private int batchNum0;
    private int batchNum0End;
    private int batchNum1;
    private int batchNum1End;
    private int batchNum2;
    private BigDecimal goodsPrice;
    private BigDecimal batchPrice0;
    private BigDecimal batchPrice1;
    private BigDecimal batchPrice2;
    private int goodsModal;
    private Integer evaluateNum;
    private Integer goodsRate;
    private int goodsStatus;
    private String areaInfo;
    private int storeId;
    private String specJson;
    private String goodsSpecValueJson;
    private int categoryId;
    private Timestamp createTime;
    private int categoryId1;
    private int categoryId2;
    private int categoryId3;
    private List<SpecJsonVo> specJsonVoList;
    private String unitName;
    private BigDecimal webPriceMin;
    private BigDecimal appPriceMin;
    private Integer groupId;
    private Integer isPointsGoods;

    /**
     * 商品详情
     * @param goods
     * @param goodsCommon
     */
    public GoodsInfoVo(Goods goods, GoodsCommon goodsCommon) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsSpecs = goods.getGoodsSpecs();
        this.goodsSpecString = goods.getGoodsSpecString();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.specValueIds = goods.getSpecValueIds();
        this.goodsPrice0 = goods.getGoodsPrice0();
        this.goodsPrice1 = goods.getGoodsPrice1();
        this.goodsPrice2 = goods.getGoodsPrice2();
        this.webPrice0 = goods.getWebPrice0();
        this.webPrice1 = goods.getWebPrice1();
        this.webPrice2 = goods.getWebPrice2();
        this.webUsable = goods.getWebUsable();
        this.appPrice0 = goods.getAppPrice0();
        this.appPrice1 = goods.getAppPrice1();
        this.appPrice2 = goods.getAppPrice2();
        this.appUsable = goods.getAppUsable();
        this.wechatPrice0 = goods.getWechatPrice0();
        this.wechatPrice1 = goods.getWechatPrice1();
        this.wechatPrice2 = goods.getWechatPrice2();
        this.wechatUsable = goods.getWechatUsable();
        this.promotionId = goods.getPromotionId();
        this.promotionStartTime = goods.getPromotionStartTime();
        this.promotionEndTime = goods.getPromotionEndTime();
        this.promotionState = goods.getPromotionState();
        this.promotionType = goods.getPromotionType();
        this.promotionTypeText = goods.getPromotionTypeText();
        this.promotionTitle = goods.getPromotionTitle();
        this.goodsSerial = goods.getGoodsSerial();
        this.colorId = goods.getColorId();
        this.goodsStorage = goods.getGoodsStorage();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.giftVoList = goods.getGiftVoList();
        this.isGift = goods.getIsGift();
        this.isGroup = goods.getIsGroup();
        this.groupPrice = goods.getGroupPrice();
        this.limitAmount = goods.getLimitAmount();
        this.isSeckill = goods.getIsSeckill();
        this.reserveStorage = goods.getReserveStorage();
        this.barCode = goods.getBarCode();

        this.goodsName = goodsCommon.getGoodsName();
        this.jingle = goodsCommon.getJingle();
        this.batchNum0 = goodsCommon.getBatchNum0();
        this.batchNum0End = goodsCommon.getBatchNum0End();
        this.batchNum1 = goodsCommon.getBatchNum1();
        this.batchNum1End = goodsCommon.getBatchNum1End();
        this.batchNum2 = goodsCommon.getBatchNum2();
        this.goodsPrice = goodsCommon.getBatchPrice0();
        this.batchPrice0 = goodsCommon.getBatchPrice0();
        this.batchPrice1 = goodsCommon.getBatchPrice1();
        this.batchPrice2 = goodsCommon.getBatchPrice2();
        this.goodsModal = goodsCommon.getGoodsModal();
        this.evaluateNum = goodsCommon.getEvaluateNum();
        this.goodsRate = goodsCommon.getGoodsRate();
        this.goodsStatus = goodsCommon.getGoodsState();
        this.colorId = goods.getColorId();
        this.areaInfo = goodsCommon.getAreaInfo();
        this.storeId = goodsCommon.getStoreId();
        this.specJson = goodsCommon.getSpecJson();
        this.goodsSpecValueJson = goodsCommon.getGoodsSpecValueJson();
        this.categoryId = goodsCommon.getCategoryId();
        this.createTime = goodsCommon.getCreateTime();
        this.imageSrc = goods.getImageSrc();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.categoryId2 = goodsCommon.getCategoryId2();
        this.categoryId3 = goodsCommon.getCategoryId3();
        this.unitName = goodsCommon.getUnitName();
        this.webPriceMin = goodsCommon.getWebPriceMin();
        this.appPriceMin = goodsCommon.getAppPriceMin();
        this.webPriceMin = goodsCommon.getWebPriceMin();
        this.groupId = goodsCommon.getGroupId();
        this.isPointsGoods = goodsCommon.getIsPointsGoods();
        this.appUsable = goodsCommon.getAppUsable();
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

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

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
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

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public List<SpecJsonVo> getSpecJsonVoList() {
        return specJsonVoList;
    }

    public void setSpecJsonVoList(List<SpecJsonVo> specJsonVoList) {
        this.specJsonVoList = specJsonVoList;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getWebPriceMin() {
        return webPriceMin;
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
    }

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getIsPointsGoods() {
        return isPointsGoods;
    }

    public void setIsPointsGoods(Integer isPointsGoods) {
        this.isPointsGoods = isPointsGoods;
    }

    @Override
    public String toString() {
        return "GoodsInfoVo{" +
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
                ", reserveStorage=" + reserveStorage +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
