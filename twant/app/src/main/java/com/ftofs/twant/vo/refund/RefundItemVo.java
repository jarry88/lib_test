package com.ftofs.twant.vo.refund;

import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.orders.OrdersGoodsVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @copyright Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license http://www.shopnc.net
 * @link http://www.shopnc.net
 *
 * 退款vo
 *
 * @author cj
 * Created 2017-4-14 下午 6:17
 */
public class RefundItemVo {
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
     * 店铺信息
     */
    private Store storeInfo;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 会员名称
     */

    private String memberName;

    /**
     * 產品SKU编号
     */

    private int goodsId;

    /**
     * 產品SPU编号
     */

    private int commonId;


    /**
     * 订单產品编号</br>
     * 主键、自增
     */

    private int ordersGoodsId;

    /**
     * 產品名称
     */

    private String goodsName;

    /**
     * 购买数量
     */

    private int goodsNum;

    /**
     * 退款金额
     */

    private BigDecimal refundAmount;

    /**
     * 產品图片
     */

    private String goodsImage;

    /**
     * 订单类型类型</br>
     * 类型:1默认2团购產品3限时折扣產品4组合套装
     */

    private int orderGoodsType = 1;

    /**
     * 申请类型</br>
     * 申请类型:1为退款,2为退货,默认为1
     */

    private int refundType = 1;

    /**
     * 卖家处理状态:1为待审核,2为同意,3为不同意,默认为1
     */

    private int sellerState = 1;
    /**
     * 申请状态:1为处理中,2为待管理员处理,3为已完成,默认为1
     */

    private int refundState = 1;

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
     * 卖家处理时间
     */
    private String adminTime;


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
     * 快递公司编号</br>
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

    //新增
    /**
     * 卖家状态文字
     */
    private String sellerStateText;
    /**
     * 退款状态文字
     */
    private String refundStateText;

    /**
     * 卖家不处理退款退货申请时按同意处理
     */
    private int maxDayRefundConfirm = 1;

    /**
     * 退货的產品 买家发货多少天后卖家不确认收货，卖家自动确认收货
     * bycj#20190316
     */
    private int maxDayReturnAutoReceive = 1;

    /**
     * 退货的產品多少天不发货自动取消退货申请
     * bycj#20190316
     */
    private int maxDayReturnAutoCancel = 1;

    /**
     * 会员中心-退货-发货按钮是否显示
     */
    private int showMemberReturnShip;

    /**
     * 会员中心-退货-延迟按钮是否显示
     */
    private int showMemberReturnDelay;

    /**
     * 店铺-退货-收货按钮是否显示
     */
    private int showStoreReturnReceive;

    /**
     * 店铺-退货 -是否显示未收到货
     */
    private int showStoreReturnUnreceived;

    /**
     * 大后台-退货- 是否显示处理按钮
     */
    private int showAdminReturnHandle;

    /**
     * 商家是否能够重新处理
     */
    private int resetHandleForSeller;
    /**
     * 店铺是否显示处理按钮
     */
    private int showStoreHandel;

    /**
     * 退款平台券金额，默认0，只有全部退款时才把平台承担平台券金额写到此处
     */
    private BigDecimal couponAmount = BigDecimal.ZERO;

    private List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<>();
    /**
     * 订单类型
     */
    private int ordersType;
    /**
     * 產品图片带URL
     */
    private String imageSrc;

    /**
     * 退款退货详情
     */
    private RefundDetailVo refundDetailVo;

    /**
     * 退款来源
     * 1.会员 2.管理员
     * @return
     */
    private int refundSource = 1;
    /**
     * 平台审核状态
     * 1为待审核,2为同意,3为不同意
     */
    private int adminState = 1;

    /**
     *  退款来源 文字
     */

    private String refundSourceText = "";
    /**
     * 管理员审核状态文字
     */
    private String adminStateText = "";


    /**
     * 当前退款 状态
     * bycj#20190316
     */
    private String currentStateText;
    /**
     *  退款 admin 是否可以处理
     */
    private int enableAdminHandle = 0;
    /**
     * 会员中心 - 标识会员是否可以取消该退款单
     * bycj#20190316
     */
    private int enableMemberCancel;
    /**
     * 会员取消
     * bycj#20190316
     */
    private int refundMemberCancel = 0;
    /**
     * 退货 会员超期未发货自动取消标识
     * bycj#20190316
     */
    private int returnMemberAutoCancel = 0;
    /**
     * 退货 卖家自动收货
     * bycj#20190316
     */
    private int returnSellerAutoReceive = 0;

    private String sellerStateTextForSelf;
    private String refundStateTextForSelf;

    private BigDecimal refundCommissionAmount;

    private String refundSnStr;

    private String ordersSnStr;

    public void setSellerStateTextForSelf(String sellerStateTextForSelf) {
        this.sellerStateTextForSelf = sellerStateTextForSelf;
    }

    public void setRefundStateTextForSelf(String refundStateTextForSelf) {
        this.refundStateTextForSelf = refundStateTextForSelf;
    }

    public void setRefundCommissionAmount(BigDecimal refundCommissionAmount) {
        this.refundCommissionAmount = refundCommissionAmount;
    }

    public String getRefundSnStr() {
        return refundSnStr;
    }

    public void setRefundSnStr(String refundSnStr) {
        this.refundSnStr = refundSnStr;
    }

    public String getOrdersSnStr() {
        return ordersSnStr;
    }

    public void setOrdersSnStr(String ordersSnStr) {
        this.ordersSnStr = ordersSnStr;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
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

    public long getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(long refundSn) {
        this.refundSn = refundSn;
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

    public Store getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(Store storeInfo) {
        this.storeInfo = storeInfo;
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

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
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

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public int getOrderGoodsType() {
        return orderGoodsType;
    }

    public void setOrderGoodsType(int orderGoodsType) {
        this.orderGoodsType = orderGoodsType;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getSellerState() {
        return sellerState;
    }

    public void setSellerState(int sellerState) {
        this.sellerState = sellerState;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getOrderLock() {
        return orderLock;
    }

    public void setOrderLock(int orderLock) {
        this.orderLock = orderLock;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getSellerTime() {
        return sellerTime;
    }

    public void setSellerTime(String sellerTime) {
        this.sellerTime = sellerTime;
    }

    public String getAdminTime() {
        return adminTime;
    }

    public void setAdminTime(String adminTime) {
        this.adminTime = adminTime;
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

    public String getPicJson() {
        return picJson;
    }

    public void setPicJson(String picJson) {
        this.picJson = picJson;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getSellerMessage() {
        return sellerMessage;
    }

    public void setSellerMessage(String sellerMessage) {
        this.sellerMessage = sellerMessage;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
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

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveMessage() {
        return receiveMessage;
    }

    public void setReceiveMessage(String receiveMessage) {
        this.receiveMessage = receiveMessage;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getSellerStateText() {
        return sellerStateText;
    }

    public void setSellerStateText(String sellerStateText) {
        this.sellerStateText = sellerStateText;
    }

    public String getRefundStateText() {
        return refundStateText;
    }

    public void setRefundStateText(String refundStateText) {
        this.refundStateText = refundStateText;
    }

    public int getMaxDayRefundConfirm() {
        return maxDayRefundConfirm;
    }

    public void setMaxDayRefundConfirm(int maxDayRefundConfirm) {
        this.maxDayRefundConfirm = maxDayRefundConfirm;
    }

    public int getMaxDayReturnAutoReceive() {
        return maxDayReturnAutoReceive;
    }

    public void setMaxDayReturnAutoReceive(int maxDayReturnAutoReceive) {
        this.maxDayReturnAutoReceive = maxDayReturnAutoReceive;
    }

    public int getMaxDayReturnAutoCancel() {
        return maxDayReturnAutoCancel;
    }

    public void setMaxDayReturnAutoCancel(int maxDayReturnAutoCancel) {
        this.maxDayReturnAutoCancel = maxDayReturnAutoCancel;
    }

    public int getShowMemberReturnShip() {
        return showMemberReturnShip;
    }

    public void setShowMemberReturnShip(int showMemberReturnShip) {
        this.showMemberReturnShip = showMemberReturnShip;
    }

    public int getShowMemberReturnDelay() {
        return showMemberReturnDelay;
    }

    public void setShowMemberReturnDelay(int showMemberReturnDelay) {
        this.showMemberReturnDelay = showMemberReturnDelay;
    }

    public int getShowStoreReturnReceive() {
        return showStoreReturnReceive;
    }

    public void setShowStoreReturnReceive(int showStoreReturnReceive) {
        this.showStoreReturnReceive = showStoreReturnReceive;
    }

    public int getShowStoreReturnUnreceived() {
        return showStoreReturnUnreceived;
    }

    public void setShowStoreReturnUnreceived(int showStoreReturnUnreceived) {
        this.showStoreReturnUnreceived = showStoreReturnUnreceived;
    }

    public int getShowAdminReturnHandle() {
        return showAdminReturnHandle;
    }

    public void setShowAdminReturnHandle(int showAdminReturnHandle) {
        this.showAdminReturnHandle = showAdminReturnHandle;
    }

    public int getResetHandleForSeller() {
        return resetHandleForSeller;
    }

    public void setResetHandleForSeller(int resetHandleForSeller) {
        this.resetHandleForSeller = resetHandleForSeller;
    }

    public int getShowStoreHandel() {
        return showStoreHandel;
    }

    public void setShowStoreHandel(int showStoreHandel) {
        this.showStoreHandel = showStoreHandel;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public List<OrdersGoodsVo> getOrdersGoodsVoList() {
        return ordersGoodsVoList;
    }

    public void setOrdersGoodsVoList(List<OrdersGoodsVo> ordersGoodsVoList) {
        this.ordersGoodsVoList = ordersGoodsVoList;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public RefundDetailVo getRefundDetailVo() {
        return refundDetailVo;
    }

    public void setRefundDetailVo(RefundDetailVo refundDetailVo) {
        this.refundDetailVo = refundDetailVo;
    }

    public int getRefundSource() {
        return refundSource;
    }

    public void setRefundSource(int refundSource) {
        this.refundSource = refundSource;
    }

    public int getAdminState() {
        return adminState;
    }

    public void setAdminState(int adminState) {
        this.adminState = adminState;
    }

    public String getRefundSourceText() {
        return refundSourceText;
    }

    public void setRefundSourceText(String refundSourceText) {
        this.refundSourceText = refundSourceText;
    }

    public String getAdminStateText() {
        return adminStateText;
    }

    public void setAdminStateText(String adminStateText) {
        this.adminStateText = adminStateText;
    }

    public String getCurrentStateText() {
        return currentStateText;
    }

    public void setCurrentStateText(String currentStateText) {
        this.currentStateText = currentStateText;
    }

    public int getEnableAdminHandle() {
        return enableAdminHandle;
    }

    public void setEnableAdminHandle(int enableAdminHandle) {
        this.enableAdminHandle = enableAdminHandle;
    }

    public int getEnableMemberCancel() {
        return enableMemberCancel;
    }

    public void setEnableMemberCancel(int enableMemberCancel) {
        this.enableMemberCancel = enableMemberCancel;
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

    public String getSellerStateTextForSelf() {
        return sellerStateTextForSelf;
    }

    public String getRefundStateTextForSelf() {
        return refundStateTextForSelf;
    }

    public BigDecimal getRefundCommissionAmount() {
        return refundCommissionAmount;
    }

    @Override
    public String toString() {
        return "RefundItemVo{" +
                "refundId=" + refundId +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", refundSn=" + refundSn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeInfo=" + storeInfo +
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
                ", refundState=" + refundState +
                ", returnType=" + returnType +
                ", orderLock=" + orderLock +
                ", goodsState=" + goodsState +
                ", addTime=" + addTime +
                ", sellerTime=" + sellerTime +
                ", adminTime=" + adminTime +
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
                ", sellerStateText='" + sellerStateText + '\'' +
                ", refundStateText='" + refundStateText + '\'' +
                ", maxDayRefundConfirm=" + maxDayRefundConfirm +
                ", maxDayReturnAutoReceive=" + maxDayReturnAutoReceive +
                ", maxDayReturnAutoCancel=" + maxDayReturnAutoCancel +
                ", showMemberReturnShip=" + showMemberReturnShip +
                ", showMemberReturnDelay=" + showMemberReturnDelay +
                ", showStoreReturnReceive=" + showStoreReturnReceive +
                ", showStoreReturnUnreceived=" + showStoreReturnUnreceived +
                ", showAdminReturnHandle=" + showAdminReturnHandle +
                ", resetHandleForSeller=" + resetHandleForSeller +
                ", showStoreHandel=" + showStoreHandel +
                ", couponAmount=" + couponAmount +
                ", ordersGoodsVoList=" + ordersGoodsVoList +
                ", ordersType=" + ordersType +
                ", imageSrc='" + imageSrc + '\'' +
                ", refundDetailVo=" + refundDetailVo +
                ", refundSource=" + refundSource +
                ", adminState=" + adminState +
                ", refundSourceText='" + refundSourceText + '\'' +
                ", adminStateText='" + adminStateText + '\'' +
                ", currentStateText='" + currentStateText + '\'' +
                ", enableAdminHandle=" + enableAdminHandle +
                ", enableMemberCancel=" + enableMemberCancel +
                ", refundMemberCancel=" + refundMemberCancel +
                ", returnMemberAutoCancel=" + returnMemberAutoCancel +
                ", returnSellerAutoReceive=" + returnSellerAutoReceive +
                '}';
    }
}
