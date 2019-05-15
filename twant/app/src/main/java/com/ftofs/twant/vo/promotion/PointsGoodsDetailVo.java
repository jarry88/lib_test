package com.ftofs.twant.vo.promotion;

import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.vo.goods.GoodsMobileBodyVo;
import com.ftofs.twant.vo.goods.SpecJsonVo;
import com.ftofs.twant.vo.goods.goodsdetail.GoodsAttrVo;
import com.ftofs.twant.vo.goods.goodsdetail.GoodsSpecValueJsonVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 积分商品详情
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:20
 */
public class PointsGoodsDetailVo {
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
     * 商品买点
     */
    private String jingle="";
    /**
     * 商品分类编号
    */
    private int categoryId=0;
    /**
     * 二维码
     */
    private String goodsQRCode;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 品牌编号
     */
    private int brandId;
    /**
     * 商品描述
     */
    private String goodsBody;
    /**
     * 移动端端描述
     */
    private String mobileBody;
    /**
     * 移动端描述数组
     */
    private List<GoodsMobileBodyVo> mobileBodyVoList = new ArrayList<>();
    /**
     * 商品货号
     */
    private String goodsSerial;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    private int goodsStatus;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 一级地区编号
     */
    private int areaId1;
    /**
     * 二级地区编号
     */
    private int areaId2;
    /**
     * 被關注数量
     */
    private int goodsFavorite;
    /**
     * 被点击数量
     */
    private int goodsClick;
    /**
     * 评价数量
     */
    private Integer evaluateNum;
    /**
     * 好评率
     */
    private Integer goodsRate;
    /**
     * 销售数量
     */
    private int goodsSaleNum;
    /**
     * 商品图片
     */
    private String imageSrc;
    /**
     * 规格JSON
     */
    private List<SpecJsonVo> specJson = new ArrayList<>();
    /**
     * 商品规格名称JSON
     */
    private List<String> goodsSpecNameList = new ArrayList<>();
    /**
     * 商品编号和规格值编号JSON
     */
    private String goodsSpecValues;
    /**
     * 商品编号和规格值编号JSON
     */
    private List<GoodsSpecValueJsonVo> goodsSpecValueJson;
    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList;
    /**
     * 商品参数
     */
    private List<GoodsAttrVo> goodsAttrList;
    /**
     * 顶部关联板式编号
     */
    private int formatTop;
    /**
     * 底部关联板式编号
     */
    private int formatBottom;
    /**
     * 商品列表
     */
    private List<Goods> goodsList;
    /**
     * 商品销售与形式
     */
    private int goodsModal;
    /**
     * 起购量0
     */
    private int batchNum0;
    /**
     * 起购量0  终点
     */
    private int batchNum0End;
    /**
     * 起购量1
     */
    private int batchNum1;
    /**
     * 起购点1 终点
     */
    private int batchNum1End;
    /**
     * 起购量2
     */
    private int batchNum2;
    /**
     * 起购价0-原价
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
    private BigDecimal webPriceMin=new BigDecimal(0);
    /**
     * PC端促销进行状态
     */
    private int webUsable;
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
    private BigDecimal appPriceMin=new BigDecimal(0);
    /**
     * APP端促销进行状态
     */
    private int appUsable;
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
    private BigDecimal wechatPriceMin=new BigDecimal(0);
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable;
    /**
     * 限时折扣促销编号
     */
    private int promotionId;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 积分商品编号
     */
    private int pointsGoodsId;
    /**
     * 所需积分
     */
    private int expendPoints = 0;
    /**
     * 领取店铺券限制的会员等级
     */
    private int limitMemberGradeLevel = 0;
    /**
     * 领取店铺券限制的会员等级名称
     */
    private String limitMemberGradeName = "";
    /**
     * 积分商品创建时间
     */

    private Timestamp createTime;

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
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

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getFormatBottom() {
        return formatBottom;
    }

    public void setFormatBottom(int formatBottom) {
        this.formatBottom = formatBottom;
    }

    public int getFormatTop() {
        return formatTop;
    }

    public void setFormatTop(int formatTop) {
        this.formatTop = formatTop;
    }

    public List<GoodsAttrVo> getGoodsAttrList() {
        return goodsAttrList;
    }

    public void setGoodsAttrList(List<GoodsAttrVo> goodsAttrList) {
        this.goodsAttrList = goodsAttrList;
    }

    public String getGoodsBody() {
        return goodsBody;
    }

    public void setGoodsBody(String goodsBody) {
        this.goodsBody = goodsBody;
    }

    public String getMobileBody() {
        return mobileBody;
    }

    public void setMobileBody(String mobileBody) {
        this.mobileBody = mobileBody;
    }

    public List<GoodsMobileBodyVo> getMobileBodyVoList() {
        return mobileBodyVoList;
    }

    public void setMobileBodyVoList(List<GoodsMobileBodyVo> mobileBodyVoList) {
        this.mobileBodyVoList = mobileBodyVoList;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsQRCode() {
        return goodsQRCode;
    }

    public void setGoodsQRCode(String goodsQRCode) {
        this.goodsQRCode = goodsQRCode;
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

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public List<String> getGoodsSpecNameList() {
        return goodsSpecNameList;
    }

    public void setGoodsSpecNameList(List<String> goodsSpecNameList) {
        this.goodsSpecNameList = goodsSpecNameList;
    }

    public List<GoodsSpecValueJsonVo> getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(List<GoodsSpecValueJsonVo> goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public String getGoodsSpecValues() {
        return goodsSpecValues;
    }

    public void setGoodsSpecValues(String goodsSpecValues) {
        this.goodsSpecValues = goodsSpecValues;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public List<SpecJsonVo> getSpecJson() {
        return specJson;
    }

    public void setSpecJson(List<SpecJsonVo> specJson) {
        this.specJson = specJson;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getPointsGoodsId() {
        return pointsGoodsId;
    }

    public void setPointsGoodsId(int pointsGoodsId) {
        this.pointsGoodsId = pointsGoodsId;
    }

    public int getExpendPoints() {
        return expendPoints;
    }

    public void setExpendPoints(int expendPoints) {
        this.expendPoints = expendPoints;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PointsGoodsDetailVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", goodsQRCode='" + goodsQRCode + '\'' +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", mobileBodyVoList=" + mobileBodyVoList +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", colorId=" + colorId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", goodsFavorite=" + goodsFavorite +
                ", goodsClick=" + goodsClick +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", imageSrc='" + imageSrc + '\'' +
                ", specJson=" + specJson +
                ", goodsSpecNameList=" + goodsSpecNameList +
                ", goodsSpecValues='" + goodsSpecValues + '\'' +
                ", goodsSpecValueJson=" + goodsSpecValueJson +
                ", goodsImageList=" + goodsImageList +
                ", goodsAttrList=" + goodsAttrList +
                ", formatTop=" + formatTop +
                ", formatBottom=" + formatBottom +
                ", goodsList=" + goodsList +
                ", goodsModal=" + goodsModal +
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
                ", unitName='" + unitName + '\'' +
                ", pointsGoodsId=" + pointsGoodsId +
                ", expendPoints=" + expendPoints +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
