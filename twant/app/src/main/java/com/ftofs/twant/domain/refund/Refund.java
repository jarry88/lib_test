package com.ftofs.twant.domain.refund;

import java.io.Serializable;
import java.math.BigDecimal;

public class Refund implements Serializable {
    /**
     * 退款id
     */
    private int refundId;

    /**
     * 订单id
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 申请编号
     */
    private long refundSn;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SKU编号
     */
    private int commonId;

    /**
     * 订单商品编号
     * 主键、自增
     */
    private int ordersGoodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 购买数量
     */
    private int goodsNum;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 订单类型类型
     * 类型:1默认2团购商品3限时折扣商品4组合套装
     */
    private int orderGoodsType = 1;

    /**
     * 申请类型
     * 申请类型:1为退款,2为退货,默认为1
     */
    private int refundType = 1;

    /**
     * 卖家处理状态:1为待审核,2为同意,3为不同意,默认为1
     */
    private int sellerState = 1;

    /**
     * 平台审核状态
     * 1为待审核,2为同意,3为不同意
     */
    private int adminState = 1;

    /**
     * 申请状态:1：为处理中（默认）,2：为待管理员处理,3：为已完成 ,4：会员取消
     */
    private int refundState = 1;

    /**
     * 会员取消
     * bycj#20190316
     */
    private int refundMemberCancel = 0 ;

    /**
     * 退货 会员超期未发货自动取消标识
     * bycj#20190316
     */
    private int returnMemberAutoCancel = 0 ;

    /**
     * 退货 卖家自动收货
     * bycj#20190316
     */
    private int returnSellerAutoReceive = 0  ;

    /**
     * 退货类型:1为不用退货,2为需要退货,默认为1
     */
    private int returnType = 1;

    /**
     * 订单锁定类型:1为不用锁定,2为需要锁定,默认为1
     */
    private int orderLock = 1;

    /**
     * 物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为1
     */
    private int goodsState = 1;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 卖家处理时间
     */
    private String sellerTime;

    /**
     * 平台处理时间
     */
    private String adminTime;

    /**
     * 结算时间
     */
    private String billTime;

    /**
     * 退款原因id
     */
    private int reasonId;

    /**
     * 原因内容
     */
    private String reasonInfo;

    /**
     * 图片
     */
    private String picJson;

    /**
     * 买家备注
     */
    private String buyerMessage;

    /**
     * 卖家备注
     */
    private String sellerMessage;

    /**
     * 管理员备注
     */
    private String adminMessage;

    /**
     * 快递公司编号
     * 主键、自增
     */
    private int shipId;

    /**
     * 发货单号
     */
    private String shipSn;

    /**
     * 发货时间
     */
    private String shipTime;

    /**
     * 收货延时时间
     */
    private String delayTime;

    /**
     * 收货时间
     */
    private String receiveTime;

    /**
     * 收货备注
     */
    private String receiveMessage;

    /**
     * 佣金比例
     */
    private int commissionRate;

    /**
     * 退款平台券金额(平台承担部分)，默认0，只有全部退款时才把平台承担平台券金额写到此处
     */
    private BigDecimal couponAmount = BigDecimal.ZERO;

    /**
     * 订单类型,与订单表的ordersType值对应(适合整单退款的情况，已支付未发货的退款)
     */
    private int ordersType = 0;

    /**
     * 退款来源
     * 1.会员 2.管理员
     * @return
     */
    private int refundSource = 1;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }

    public String getAdminTime() {
        return adminTime;
    }

    public void setAdminTime(String adminTime) {
        this.adminTime = adminTime;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }


    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getOrderGoodsType() {
        return orderGoodsType;
    }

    public void setOrderGoodsType(int orderGoodsType) {
        this.orderGoodsType = orderGoodsType;
    }

    public int getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(int orderLock) {
        this.orderLock = orderLock;
    }

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

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getPicJson() {
        return picJson;
    }

    public void setPicJson(String picJson) {
        this.picJson = picJson;
    }

    public int getReasonId() {
        return reasonId;
    }

    public void setReasonId(int reasonId) {
        this.reasonId = reasonId;
    }

    public String getReasonInfo() {
        return reasonInfo;
    }

    public void setReasonInfo(String reasonInfo) {
        this.reasonInfo = reasonInfo;
    }

    public String getReceiveMessage() {
        return receiveMessage;
    }

    public void setReceiveMessage(String receiveMessage) {
        this.receiveMessage = receiveMessage;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
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

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public int getSellerState() {
        return sellerState;
    }

    public void setSellerState(int sellerState) {
        this.sellerState = sellerState;
    }

    public String getSellerTime() {
        return sellerTime;
    }

    public void setSellerTime(String sellerTime) {
        this.sellerTime = sellerTime;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipTime() {
        return shipTime;
    }

    public void setShipTime(String shipTime) {
        this.shipTime = shipTime;
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

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public int getAdminState() {
        return adminState;
    }

    public void setAdminState(int adminState) {
        this.adminState = adminState;
    }

    public int getRefundSource() {
        return refundSource;
    }

    public void setRefundSource(int refundSource) {
        this.refundSource = refundSource;
    }

    public int getRefundMemberCancel() {
        return refundMemberCancel;
    }

    public void setRefundMemberCancel(int refundMemberCancel) {
        this.refundMemberCancel = refundMemberCancel;
    }

    public int getReturnMemberAutoCancel() {
        return returnMemberAutoCancel;
    }

    public void setReturnMemberAutoCancel(int returnMemberAutoCancel) {
        this.returnMemberAutoCancel = returnMemberAutoCancel;
    }

    public int getReturnSellerAutoReceive() {
        return returnSellerAutoReceive;
    }

    public void setReturnSellerAutoReceive(int returnSellerAutoReceive) {
        this.returnSellerAutoReceive = returnSellerAutoReceive;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", refundSn=" + refundSn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", ordersGoodsId=" + ordersGoodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNum=" + goodsNum +
                ", refundAmount=" + refundAmount +
                ", goodsImage='" + goodsImage + '\'' +
                ", orderGoodsType=" + orderGoodsType +
                ", refundType=" + refundType +
                ", sellerState=" + sellerState +
                ", adminState=" + adminState +
                ", refundState=" + refundState +
                ", refundMemberCancel=" + refundMemberCancel +
                ", returnMemberAutoCancel=" + returnMemberAutoCancel +
                ", returnSellerAutoReceive=" + returnSellerAutoReceive +
                ", returnType=" + returnType +
                ", orderLock=" + orderLock +
                ", goodsState=" + goodsState +
                ", addTime=" + addTime +
                ", sellerTime=" + sellerTime +
                ", adminTime=" + adminTime +
                ", billTime=" + billTime +
                ", reasonId=" + reasonId +
                ", reasonInfo='" + reasonInfo + '\'' +
                ", picJson='" + picJson + '\'' +
                ", buyerMessage='" + buyerMessage + '\'' +
                ", sellerMessage='" + sellerMessage + '\'' +
                ", adminMessage='" + adminMessage + '\'' +
                ", shipId=" + shipId +
                ", shipSn='" + shipSn + '\'' +
                ", shipTime=" + shipTime +
                ", delayTime=" + delayTime +
                ", receiveTime=" + receiveTime +
                ", receiveMessage='" + receiveMessage + '\'' +
                ", commissionRate=" + commissionRate +
                ", couponAmount=" + couponAmount +
                ", ordersType=" + ordersType +
                ", refundSource=" + refundSource +
                '}';
    }
}
