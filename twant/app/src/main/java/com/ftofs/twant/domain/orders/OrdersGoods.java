package com.ftofs.twant.domain.orders;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrdersGoods implements Serializable {
    /**
     * 订单產品编号
     * 主键、自增
     */
    private int ordersGoodsId;

    /**
     * 订单ID
     */
    private int ordersId;

    /**
     * 產品Id
     */
    private int goodsId;

    /**
     * 產品SPU
     */
    private int commonId;

    /**
     * 產品名称
     */
    private String goodsName;

    /**
     * 產品原价
     */
    private BigDecimal basePrice = new BigDecimal(0);

    /**
     * 產品单价
     */
    private BigDecimal goodsPrice = new BigDecimal(0);

    /**
     * 產品实际支付总金额(扣除所有促销后)
     */
    private BigDecimal goodsPayAmount = new BigDecimal(0);

    /**
     * 购买数量
     */
    private int buyNum;

    /**
     * 產品图片
     */
    private String goodsImage;

    /**
     * 產品促销类型
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
     * 產品分类ID
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
     * 是否拥有赠品
     */
    private int isGift = 0;

    /**
     * 优惠套装Id
     */
    private int bundlingId = 0;

    /**
     * 推广订单
     */
    private int distributionOrdersId;

    /**
     * bycj -- 已退款金额
     */
    private BigDecimal refundPrice = new BigDecimal(0);

    /**
     * 產品货号
     */
    private String goodsSerial;

    /**
     * 试用资格Id
     */
    private int trysApplyId;

    private BigDecimal commissionAmount;

    /**
     * 投诉Id
     */
    private int complainId;

    /**
     * 消保
     */
    private String contract;

    /**
     * 秒杀活动產品 主键
     */
    private int seckillGoodsId;

    /**
     * 砍价，开砍记录主键
     */
    private int bargainOpenId;

    /**
     * 海外购產品税率
     */
    private BigDecimal taxTate;

    /**
     * 海外购產品税费
     */
    private BigDecimal taxAmount = new BigDecimal(0);

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

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public int getDistributionOrdersId() {
        return distributionOrdersId;
    }

    public void setDistributionOrdersId(int distributionOrdersId) {
        this.distributionOrdersId = distributionOrdersId;
    }

    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public int getTrysApplyId() {
        return trysApplyId;
    }

    public void setTrysApplyId(int trysApplyId) {
        this.trysApplyId = trysApplyId;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public int getBargainOpenId() {
        return bargainOpenId;
    }

    public void setBargainOpenId(int bargainOpenId) {
        this.bargainOpenId = bargainOpenId;
    }

    public BigDecimal getTaxTate() {
        return taxTate;
    }

    public void setTaxTate(BigDecimal taxTate) {
        this.taxTate = taxTate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    @Override
    public String toString() {
        return "OrdersGoods{" +
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
                ", isGift=" + isGift +
                ", bundlingId=" + bundlingId +
                ", distributionOrdersId=" + distributionOrdersId +
                ", refundPrice=" + refundPrice +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", trysApplyId=" + trysApplyId +
                ", commissionAmount=" + commissionAmount +
                ", complainId=" + complainId +
                ", contract='" + contract + '\'' +
                ", seckillGoodsId=" + seckillGoodsId +
                ", bargainOpenId=" + bargainOpenId +
                ", taxTate=" + taxTate +
                ", taxAmount=" + taxAmount +
                ", joinBigSale=" + joinBigSale +
                '}';
    }
}
