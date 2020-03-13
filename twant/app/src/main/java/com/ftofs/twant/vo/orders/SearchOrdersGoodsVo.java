package com.ftofs.twant.vo.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @copyright Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license http://www.shopnc.net
 * @link http://www.shopnc.net
 *
 * 买家订单產品列表
 *
 * @author hbj
 * Created 2017/4/13 14:46
 */
public class SearchOrdersGoodsVo {
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
     * 完整规格
     */
    private String goodsFullSpecs;
    /**
     * 退款单号
     */
    private long refundSn;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 折扣标题(详见OrdersGoodsType常量)
     */
    private String promotionTitle = "";
    /**
     * 投诉Id
     */
    private int complainId;
    /**
     * 消保
     */
    private List<GoodsContractVo> goodsContractVoList = new ArrayList<>();
    /**
     * 服务列表
     */
    private List<String> goodsServicesList = new ArrayList<>();
    /**
     * cj[ 產品是否显示退款](该字段不存到搜索引擎里)
     * 1.显示 ， 2 不显示
     */
    private int showRefund = 0;
    /**
     * 已退款金额(该字段不存到搜索引擎里)
     */
    private BigDecimal refundAmount;
    /**
     * 已有退款记录时显示是否显示"查看退款详情"按钮(该字段不存到搜索引擎里)
     */
    private int showRefundInfo = 0;
    /**
     *  已有退款记录时显示显示"查看退款还是退货详情"按钮(该字段不存到搜索引擎里)
     */
    private int refundType;
    /**
     * 退款id(该字段不存到搜索引擎里)
     */
    private int refundId;
    /**
     * 是否可以投诉(该字段不存到搜索引擎里)
     */
    private int showMemberComplain = 0;
    /**
     * 是否可以查看退款(该字段不存到搜索引擎里)
     */
    private int showViewRefund = 0;
    /**
     * 是否可以查看退货(该字段不存到搜索引擎里)
     */
    private int showViewReturn = 0;
    /**
     * 虚拟產品是否申请过退款
     */
    private int virtualRefundApplyed = 0;
    /**
     * 虚拟產品是否允许退款(当旗下至少有一个虚拟码可退时，就可以申请退款)(该字段不存到搜索引擎里)
     */
    private int allowVirtualRefund = 0;
    /**
     * 是否存在被锁定中的虚拟码(该字段不存到搜索引擎里)
     */
    private int isExistLock = 0;
    /**
     * 產品促销标签标题(只有订单列表、详情显示產品促销类型使用)(该字段不存到搜索引擎里)
     */
    private String tagTitle = "";


    public SearchOrdersGoodsVo() {
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
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

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
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

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
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

    public List<String> getGoodsServicesList() {
        return goodsServicesList;
    }

    public void setGoodsServicesList(List<String> goodsServicesList) {
        this.goodsServicesList = goodsServicesList;
    }

    public int getShowMemberComplain() {
        return showMemberComplain;
    }

    public void setShowMemberComplain(int showMemberComplain) {
        this.showMemberComplain = showMemberComplain;
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

    public void setIsExistLock(int isExistLock) {
        this.isExistLock = isExistLock;
    }

    public int getVirtualRefundApplyed() {
        return virtualRefundApplyed;
    }

    public void setVirtualRefundApplyed(int virtualRefundApplyed) {
        this.virtualRefundApplyed = virtualRefundApplyed;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    @Override
    public String toString() {
        return "SearchOrdersGoodsVo{" +
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
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", refundSn=" + refundSn +
                ", unitName='" + unitName + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", complainId=" + complainId +
                ", goodsContractVoList=" + goodsContractVoList +
                ", goodsServicesList=" + goodsServicesList +
                ", showRefund=" + showRefund +
                ", refundAmount=" + refundAmount +
                ", showRefundInfo=" + showRefundInfo +
                ", refundType=" + refundType +
                ", refundId=" + refundId +
                ", showMemberComplain=" + showMemberComplain +
                ", showViewRefund=" + showViewRefund +
                ", showViewReturn=" + showViewReturn +
                ", isExistLock=" + isExistLock +
                ", tagTitle='" + tagTitle + '\'' +
                '}';
    }
}
