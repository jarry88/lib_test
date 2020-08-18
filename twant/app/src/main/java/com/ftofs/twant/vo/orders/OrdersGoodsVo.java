package com.ftofs.twant.vo.orders;

import com.ftofs.twant.domain.orders.Orders;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 订单產品
 * 
 * @author hbj
 * Created 2017/4/13 14:46
 */
public class OrdersGoodsVo {
    /**
     * 订单產品编号
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
     * 產品(购买时的)单价
     */
    private BigDecimal goodsPrice;
    /**
     * (该產品)实付支付总金额
     */
    private BigDecimal goodsPayAmount;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 產品图片
     */
    private String goodsImage;
    /**
     * 產品图片带URL
     */
    private String imageSrc;
    /**
     * 產品类型
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
     * 完整规格
     */
    private String goodsFullSpecs;
    /**
     * 佣金金额
     */
    private BigDecimal commissionAmount;
    /**
     * 会员名称
     */
    private String memberName;
    /**
     * 下单时间
     */
    private String createTime;
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
     * cj[ 產品是否显示退款]
     * 1.显示 ， 2 不显示
     */
    private int showRefund = 0 ;

    /**
     * 已有退款记录时显示退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 已有退款记录时显示是否显示"查看退款详情"按钮
     */
    private int showRefundInfo= 0;
    /**
     *  已有退款记录时显示显示"查看退款还是退货详情"按钮
     */
    private int refundType;

    /**
     * 退款id
     */
    private int refundId ;

    /**
     * 退款单号
     */
    private long refundSn;
    /**
     * 產品原价
     */
    private BigDecimal basePrice;
    /**
     * 促销价比原价节省金额(產品级)
     */
    private BigDecimal savePrice;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 折扣标题
     */
    private String promotionTitle = "";
    /**
     * 產品货号
     */
    private String goodsSerial;
    /**
     * 投诉Id
     */
    private int complainId;
    /**
     * 消保
     */
    private List<GoodsContractVo> goodsContractVoList = new ArrayList<>();
    /**
     * 海外购產品税率
     */
    private BigDecimal taxRate;
    /**
     * 海外购產品税费
     */
    private BigDecimal taxAmount = new BigDecimal(0);
    /**
     * 是否参加促销
     */
    private int joinBigSale = 1;

    /**
     * 是否可以查看退款(该字段不存到搜索引擎里)
     */
    private int showViewRefund = 0;
    /**
     * 是否可以查看退货(该字段不存到搜索引擎里)
     */
    private int showViewReturn = 0;
    private int ifoodmacauCount=0;

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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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

    public int getShowRefund() {
        return showRefund;
    }

    public void setShowRefund(int showRefund) {
        this.showRefund = showRefund;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getShowRefundInfo() {
        return showRefundInfo;
    }

    public void setShowRefundInfo(int showRefundInfo) {
        this.showRefundInfo = showRefundInfo;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public long getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(long refundSn) {
        this.refundSn = refundSn;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getSavePrice() {
        return savePrice;
    }

    public void setSavePrice(BigDecimal savePrice) {
        this.savePrice = savePrice;
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

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public List<GoodsContractVo> getGoodsContractVoList() {
        return goodsContractVoList;
    }

    public void setGoodsContractVoList(List<GoodsContractVo> goodsContractVoList) {
        this.goodsContractVoList = goodsContractVoList;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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

    public int getShowViewRefund() {
        return showViewRefund;
    }

    public void setShowViewRefund(int showViewRefund) {
        this.showViewRefund = showViewRefund;
    }

    public int getShowViewReturn() {
        return showViewReturn;
    }

    public void setShowViewReturn(int showViewReturn) {
        this.showViewReturn = showViewReturn;
    }

    @Override
    public String toString() {
        return "OrdersGoodsVo{" +
                "ordersGoodsId=" + ordersGoodsId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsPayAmount=" + goodsPayAmount +
                ", buyNum=" + buyNum +
                ", goodsImage='" + goodsImage + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsType=" + goodsType +
                ", storeId=" + storeId +
                ", memberId=" + memberId +
                ", commissionRate=" + commissionRate +
                ", categoryId=" + categoryId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", showRefund=" + showRefund +
                ", refundAmount=" + refundAmount +
                ", showRefundInfo=" + showRefundInfo +
                ", refundType=" + refundType +
                ", refundId=" + refundId +
                ", refundSn=" + refundSn +
                ", basePrice=" + basePrice +
                ", savePrice=" + savePrice +
                ", unitName='" + unitName + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", complainId=" + complainId +
                ", goodsContractVoList=" + goodsContractVoList +
                ", taxRate=" + taxRate +
                ", taxAmount=" + taxAmount +
                ", joinBigSale=" + joinBigSale +
                ", showViewRefund=" + showViewRefund +
                ", showViewReturn=" + showViewReturn +
                '}';
    }
    public static OrdersGoodsVo parse(EasyJSONObject object) throws Exception{
        OrdersGoodsVo vo = new OrdersGoodsVo();
        vo.ordersGoodsId = object.getInt("ordersGoodsId");
        vo.goodsId = object.getInt("goodsId");
        vo.commonId = object.getInt("commonId");
        vo.goodsType = object.getInt("goodsType");
        vo.storeId = object.getInt("storeId");
        vo.memberId = object.getInt("memberId");
        vo.ifoodmacauCount = object.getInt("ifoodmacauCount");
        vo.goodsName = object.getSafeString("goodsName");
        vo.goodsPrice = BigDecimal.valueOf(object.getDouble("goodsPrice"));
        vo.goodsPayAmount = BigDecimal.valueOf(object.getDouble("goodsPayAmount"));
        vo.goodsImage = object.getSafeString("goodsImage");
        vo.imageSrc = object.getSafeString("imageSrc");
        return vo;
    }
}
