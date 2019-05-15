package com.ftofs.twant.vo.theme;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.domain.theme.ThemeGoods;
import com.ftofs.twant.vo.goods.BatchNumPriceVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商城活动商品
 * 
 * @author shopnc.szq
 * Created 2017/8/31 14:02
 */
public class ThemeGoodsVo {
    /**
     * 商城活动商品
     */
    private int themeGoodsId;
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

    private Timestamp createTime=new Timestamp(0);
    /**
     * 是否推荐
     */
    private int isCommend=0;
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
     * PC端促销进行状态
     */
    private int webUsable;
    /**
     * APP端促销进行状态
     */
    private int appUsable;
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable;
    /**
     * 活动类型
     */
    private int promotionType=0;
    /**
     * 活动类型文字
     */
    private String promotionTypeText="";
    /**
     * 销售模式
     * 零售1  批发2
     */
    private int goodsModal=0;
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
     * 商品图
     */
    private String imageSrc="";
    /**
     * 商品图名称
     */
    private String imageName = "" ;

    /**
     * 计量单位
     */
    private String unitName="";
    /**
     * 价格区间
     */
    private String priceRange="";
    /**
     * 是否有赠品
     */
    private int isGift;
    /**
     * 好评率
     */
    private int goodsRate = 0;
    /**
     * 评价数量
     */
    private int evaluateNum = 0;
    /**
     * 库存
     */
    private long storage=0;
    /**
     * 阶梯下个，key是区间，value价格
     */
    private List<BatchNumPriceVo> batchNumPriceVoList = new ArrayList<>();
    /**
     * 佣金比例
     */
    private int commissionRate;


    /**
     * 添加时间
     */

    private Timestamp addTime;

    /**
     * 店铺编号
     */
    private int storeId =0;
    /**
     * 店铺名称
     */
    private String storeName = "" ;
    /**
     * 商城活动编号
     */

    private int themeId;
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */

    private int themeVerify;

    /**
     * 审核失败原因
     */

    private String verifyRemark;

    /**
     * 审核时间
     */


    private Timestamp verifyTime;

    /**
     * 审核状态前台展示
     */
    private String themeVerifyString;

    public String getThemeVerifyString() {
        String themeVerifyString = "";
        switch (themeVerify) {
            case 0:
                themeVerifyString = "未通过";
                break;
            case 1:
                themeVerifyString = "已通过";
                break;
            case 10:
                themeVerifyString = "审核中";
                break;
            default:
                break;
        }
        return themeVerifyString;
    }

    public int getThemeGoodsId() {
        return themeGoodsId;
    }

    public void setThemeGoodsId(int themeGoodsId) {
        this.themeGoodsId = themeGoodsId;
    }

    public void setThemeVerifyString(String themeVerifyString) {
        this.themeVerifyString = themeVerifyString;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getIsCommend() {
        return isCommend;
    }

    public void setIsCommend(int isCommend) {
        this.isCommend = isCommend;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(int goodsRate) {
        this.goodsRate = goodsRate;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public List<BatchNumPriceVo> getBatchNumPriceVoList() {
        return batchNumPriceVoList;
    }

    public void setBatchNumPriceVoList(List<BatchNumPriceVo> batchNumPriceVoList) {
        this.batchNumPriceVoList = batchNumPriceVoList;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public int getThemeVerify() {
        return themeVerify;
    }

    public void setThemeVerify(int themeVerify) {
        this.themeVerify = themeVerify;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public Timestamp getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Timestamp verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Override
    public String toString() {
        return "ThemeGoodsVo{" +
                "themeGoodsId=" + themeGoodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", createTime=" + createTime +
                ", isCommend=" + isCommend +
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
                ", webUsable=" + webUsable +
                ", appUsable=" + appUsable +
                ", wechatUsable=" + wechatUsable +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", goodsModal=" + goodsModal +
                ", goodsSaleNum=" + goodsSaleNum +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", imageSrc='" + imageSrc + '\'' +
                ", imageName='" + imageName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", isGift=" + isGift +
                ", goodsRate=" + goodsRate +
                ", evaluateNum=" + evaluateNum +
                ", storage=" + storage +
                ", batchNumPriceVoList=" + batchNumPriceVoList +
                ", commissionRate=" + commissionRate +
                ", addTime=" + addTime +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", themeId=" + themeId +
                ", themeVerify=" + themeVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", verifyTime=" + verifyTime +
                ", themeVerifyString='" + themeVerifyString + '\'' +
                '}';
    }
}
