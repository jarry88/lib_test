package com.ftofs.twant.vo.chain;

import com.ftofs.twant.domain.chain.ChainPromotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 门店
 *
 * @author shopnc.feng
 * Created 2017/6/6 17:36
 */
public class ChainVo {
    /**
     * 门店编号
     */
    private int chainId;
    /**
     * 门店名称
     */
    private String chainName;
    /**
     * 店主名称
     */
    private String clerkName;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 一级地区编号
     */
    private int areaId1 = 0;
    /**
     * 二级地区编号
     */
    private int areaId2 = 0;
    /**
     * 三级地区编号
     */
    private int areaId3 = 0;
    /**
     * 四级地区编号
     */
    private int areaId4 = 0;
    /**
     * 地区内容
     */
    private String areaInfo;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String chainPhone;
    /**
     * 营业时间
     */
    private String chainOpeningTime;
    /**
     * 交通线路
     */
    private String chainTrafficLine;
    /**
     * 图片路径1
     */
    private String imageSrc1="";
    /**
     * 图片路径2
     */
    private String imageSrc2="";
    /**
     * 高德经度
     */
    private Double lng = 0d;
    /**
     * 高德纬度
     */
    private Double lat = 0d;
    /**
     * 好评率
     */
    private int chainGoodsRate = 100;
    /**
     * 门店评分
     */
    private int chainCriterion = 5;
    /**
     * 產品价格
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;
    /**
     * 门店类型
     */
    private String className = "门店类型";
    /**
     * 店内活动
     */
    private List<ChainPromotion> chainPromotionList = new ArrayList<>();

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public int getAreaId3() {
        return areaId3;
    }

    public void setAreaId3(int areaId3) {
        this.areaId3 = areaId3;
    }

    public int getAreaId4() {
        return areaId4;
    }

    public void setAreaId4(int areaId4) {
        this.areaId4 = areaId4;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChainPhone() {
        return chainPhone;
    }

    public void setChainPhone(String chainPhone) {
        this.chainPhone = chainPhone;
    }

    public String getChainOpeningTime() {
        return chainOpeningTime;
    }

    public void setChainOpeningTime(String chainOpeningTime) {
        this.chainOpeningTime = chainOpeningTime;
    }

    public String getChainTrafficLine() {
        return chainTrafficLine;
    }

    public void setChainTrafficLine(String chainTrafficLine) {
        this.chainTrafficLine = chainTrafficLine;
    }

    public String getImageSrc1() {
        return imageSrc1;
    }

    public void setImageSrc1(String imageSrc1) {
        this.imageSrc1 = imageSrc1;
    }

    public String getImageSrc2() {
        return imageSrc2;
    }

    public void setImageSrc2(String imageSrc2) {
        this.imageSrc2 = imageSrc2;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public int getChainGoodsRate() {
        return chainGoodsRate;
    }

    public void setChainGoodsRate(int chainGoodsRate) {
        this.chainGoodsRate = chainGoodsRate;
    }

    public int getChainCriterion() {
        return chainCriterion;
    }

    public void setChainCriterion(int chainCriterion) {
        this.chainCriterion = chainCriterion;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ChainPromotion> getChainPromotionList() {
        return chainPromotionList;
    }

    public void setChainPromotionList(List<ChainPromotion> chainPromotionList) {
        this.chainPromotionList = chainPromotionList;
    }

    @Override
    public String toString() {
        return "ChainVo{" +
                "chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", clerkName='" + clerkName + '\'' +
                ", storeId=" + storeId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", areaId3=" + areaId3 +
                ", areaId4=" + areaId4 +
                ", areaInfo='" + areaInfo + '\'' +
                ", address='" + address + '\'' +
                ", chainPhone='" + chainPhone + '\'' +
                ", chainOpeningTime='" + chainOpeningTime + '\'' +
                ", chainTrafficLine='" + chainTrafficLine + '\'' +
                ", imageSrc1='" + imageSrc1 + '\'' +
                ", imageSrc2='" + imageSrc2 + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", chainGoodsRate=" + chainGoodsRate +
                ", chainCriterion=" + chainCriterion +
                ", goodsPrice=" + goodsPrice +
                ", className='" + className + '\'' +
                ", chainPromotionList=" + chainPromotionList +
                '}';
    }
}
