package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.goods.GoodsImage;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:12
 */
public class GoodsVo {
    /**
     * 商品SKU编号
     */
    private int goodsId=0;
    /**
     * 商品SPU
     */
    private int commonId=0;
    /**
     * 商品名称
     */
    private String goodsName="";
    /**
     * 创建时间
     */

    private String createTime="";
    /**
     * 是否推荐
     */
    private int isCommend=0;
    /**
     * 商品状态<br>
     * 0下架，1正常，10违规禁售
     */
    private int goodsState=0;
    /**
     * 违规禁售原因
     */
    private String stateRemark="";
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */
    private int goodsVerify=0;
    /**
     * 审核失败原因
     */
    private String verifyRemark="";
    /**
     * 商品卖点
     */
    private String jingle="";
    /**
     * 起购量0
     */
    private int batchNum0=0;
    /**
     * 起购量0  终点
     */
    private int batchNum0End=0;
    /**
     * 起购量1
     */
    private int batchNum1=0;
    /**
     * 起购点1 终点
     */
    private int batchNum1End=0;
    /**
     * 起购量2
     */
    private int batchNum2=0;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice=new BigDecimal(0);
    /**
     * 起购价0
     */
    private BigDecimal batchPrice0=new BigDecimal(0);
    /**
     * 起购价1
     */
    private BigDecimal batchPrice1=new BigDecimal(0);
    /**
     * 起购价2
     */
    private BigDecimal batchPrice2=new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal webPriceMin=new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal appPriceMin=new BigDecimal(0);
    /**
     * 商品最低价
     */
    private BigDecimal wechatPriceMin=new BigDecimal(0);
    /**
     * 销售模式
     * 零售1  批发2
     */
    private int goodsModal=0;
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
    private int goodsSaleNum=0;
    /**
     * 库存
     */
    private int goodsStorage=0;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    private int goodsStatus=0;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId=0;
    /**
     * 所在地
     */
    private String areaInfo="";
    /**
     * 店铺编号
     */
    private int storeId=0;
    /**
     * 店铺名称
     */
    private String storeName="";
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    private int isOwnShop=0;
    /**
     * 商品图
     */
    private String imageSrc="";
    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList=new ArrayList<>();
    /**
     * 计量单位
     */
    private String unitName="";
    /**
     * 促销编号
     */
    private int promotionId=0;
    /**
     * 活动类型
     */
    private int promotionType=0;
    /**
     * 活动类型文字
     */
    private String promotionTypeText="";
    /**
     * 价格区间
     */
    private String priceRange="";
    /**
     * 库存
     */
    private long storage=0;
    /**
     * 规格JSON
     */
    private String specJson="";
    /**
     * 规格JSON
     */
    private List<SpecJsonVo> specJsonVoList = new ArrayList<SpecJsonVo>();
    /**
     * 商品编号和规格值编号JSON
     */
    private String goodsSpecValueJson="";
    /**
     * 商品分类编号
     */
    private int categoryId=0;
    /**
     * 商品分类名称
     */
    private String categoryName="";
    /**
     * 商品一级分类
     */
    private int categoryId1=0;
    /**
     * 商品二级分类
     */
    private int categoryId2=0;
    /**
     * 商品三级分类
     */
    private int categoryId3=0;
    /**
     * 阶梯下个，key是区间，value价格
     */
    private List<BatchNumPriceVo> batchNumPriceVoList = new ArrayList<>();
    /**
     * 拼团编号
     */
    private Integer groupId;
    /**
     * 是否为积分商品
     */
    private Integer isPointsGoods = 0;
    /**
     * 是否为秒杀商品
     */
    private int isSeckill = 0;

    /**
     * 是否為虛擬商品
     */
    private int isVirtual = 0;

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
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

    public BigDecimal getWechatPriceMin() {
        return wechatPriceMin;
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
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

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
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

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public long getStorage() {
        return storage;
    }

    public void setStorage(long storage) {
        this.storage = storage;
    }

    public String getSpecJson() {
        return specJson;
    }

    public void setSpecJson(String specJson) {
        this.specJson = specJson;
    }

    public List<SpecJsonVo> getSpecJsonVoList() {
        return specJsonVoList;
    }

    public void setSpecJsonVoList(List<SpecJsonVo> specJsonVoList) {
        this.specJsonVoList = specJsonVoList;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public List<BatchNumPriceVo> getBatchNumPriceVoList() {
        return batchNumPriceVoList;
    }

    public void setBatchNumPriceVoList(List<BatchNumPriceVo> batchNumPriceVoList) {
        this.batchNumPriceVoList = batchNumPriceVoList;
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

    public int getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(int isSeckill) {
        this.isSeckill = isSeckill;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", createTime=" + createTime +
                ", isCommend=" + isCommend +
                ", goodsState=" + goodsState +
                ", stateRemark='" + stateRemark + '\'' +
                ", goodsVerify=" + goodsVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", jingle='" + jingle + '\'' +
                ", batchNum0=" + batchNum0 +
                ", batchNum0End=" + batchNum0End +
                ", batchNum1=" + batchNum1 +
                ", batchNum1End=" + batchNum1End +
                ", batchNum2=" + batchNum2 +
                ", goodsPrice=" + goodsPrice +
                ", batchPrice0=" + batchPrice0 +
                ", batchPrice1=" + batchPrice1 +
                ", batchPrice2=" + batchPrice2 +
                ", webPriceMin=" + webPriceMin +
                ", appPriceMin=" + appPriceMin +
                ", wechatPriceMin=" + wechatPriceMin +
                ", goodsModal=" + goodsModal +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", colorId=" + colorId +
                ", areaInfo='" + areaInfo + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsImageList=" + goodsImageList +
                ", unitName='" + unitName + '\'' +
                ", promotionId=" + promotionId +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", storage=" + storage +
                ", specJson='" + specJson + '\'' +
                ", specJsonVoList=" + specJsonVoList +
                ", goodsSpecValueJson='" + goodsSpecValueJson + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", batchNumPriceVoList=" + batchNumPriceVoList +
                ", groupId=" + groupId +
                ", isPointsGoods=" + isPointsGoods +
                ", isSeckill=" + isSeckill +
                ", isVirtual=" + isVirtual +
                '}';
    }
}
