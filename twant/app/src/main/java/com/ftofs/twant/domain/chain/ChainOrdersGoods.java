package com.ftofs.twant.domain.chain;

import java.math.BigDecimal;

public class ChainOrdersGoods {
    /**
     * 主键、自增
     */
    private int ordersGoodsId;

    /**
     * 订单ID
     */
    private int ordersId;

    /**
     * 商品Id
     */
    private int goodsId;

    /**
     * 门店商品Id
     */
    private int chainGoodsId;

    /**
     * 商品SPU
     */
    private int commonId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品(购买时的)单价
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /**
     * 购买数量
     */
    private int buyNum;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 门店ID
     */
    private int chainId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 店员ID
     */
    private int clerkId;

    /**
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 商品分类ID
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
     * 计量单位
     */
    private String unitName;

    /**
     * 商品货号
     */
    private String goodsSerial;

    /**
     * 商品服务
     */
    private String goodsServices;

    /**
     * 是否参加促销
     * 1参加促销
     */
    private int joinBigSale = 1;

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
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

    public String getUnitName() {
        return unitName;
    }

    public int getChainGoodsId() {
        return chainGoodsId;
    }

    public void setChainGoodsId(int chainGoodsId) {
        this.chainGoodsId = chainGoodsId;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    /**
     * 图片URL
     */
    public String getImageSrc() {
        return goodsImage;
    }

    public String getGoodsServices() {
        return goodsServices;
    }

    public void setGoodsServices(String goodsServices) {
        this.goodsServices = goodsServices;
    }

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    @Override
    public String toString() {
        return "ChainOrdersGoods{" +
                "ordersGoodsId=" + ordersGoodsId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", chainGoodsId=" + chainGoodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", chainId=" + chainId +
                ", memberId=" + memberId +
                ", clerkId=" + clerkId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", unitName='" + unitName + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsServices='" + goodsServices + '\'' +
                ", joinBigSale=" + joinBigSale +
                '}';
    }
}