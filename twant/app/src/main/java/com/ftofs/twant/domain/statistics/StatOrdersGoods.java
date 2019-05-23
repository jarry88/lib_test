package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;


public class StatOrdersGoods implements Serializable {
    /**
     * 订单商品编号
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
     * 商品SPU
     */
    private int commonId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品(购买时的)原价
     */
    private BigDecimal basePrice = new BigDecimal(0);

    /**
     * 商品(购买时的)单价
     */
    private BigDecimal goodsPrice = new BigDecimal(0);

    /**
     * (该商品)实付支付总金额
     */
    private BigDecimal goodsPayAmount = new BigDecimal(0);

    /**
     * 购买数量
     */
    private int buyNum;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 商品促销类型 GoodsPromotionType 对应
     */
    private int goodsType = 0;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 佣金比例
     */
    private int commissionRate;

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
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 计量单位
     */
    private String unitName;

    /**
     * 折扣标题
     */
    private String promotionTitle = "";

    /**
     * 优惠套装Id
     */
    private int bundlingId = 0;

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 下单时间 具体到天
     */
    private String createTimeDate;

    /**
     * 下单时间 小时
     */
    private int createTimeHour;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 会员名称
     */
    private String memberName;

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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsPayAmount() {
        return goodsPayAmount;
    }

    public void setGoodsPayAmount(BigDecimal goodsPayAmount) {
        this.goodsPayAmount = goodsPayAmount;
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

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
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

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(String createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    public int getCreateTimeHour() {
        return createTimeHour;
    }

    public void setCreateTimeHour(int createTimeHour) {
        this.createTimeHour = createTimeHour;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    @Override
    public String toString() {
        return "StatOrdersGoods{" +
                "ordersGoodsId=" + ordersGoodsId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", basePrice=" + basePrice +
                ", goodsPrice=" + goodsPrice +
                ", goodsPayAmount=" + goodsPayAmount +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", goodsType=" + goodsType +
                ", storeId=" + storeId +
                ", memberId=" + memberId +
                ", commissionRate=" + commissionRate +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", unitName='" + unitName + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", bundlingId=" + bundlingId +
                ", createTime=" + createTime +
                ", createTimeDate=" + createTimeDate +
                ", createTimeHour=" + createTimeHour +
                ", storeName='" + storeName + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
