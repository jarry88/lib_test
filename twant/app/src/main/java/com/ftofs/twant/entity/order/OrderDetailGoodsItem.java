package com.ftofs.twant.entity.order;


/**
 * 訂單詳情的產品列表Item
 * @author zwm
 */
public class OrderDetailGoodsItem {

    public OrderDetailGoodsItem(int commonId, int goodsId, int ordersId, int orderState, int showRefundWaiting, int showMemberRefundAll,
                                int ordersGoodsId,
                                String imageSrc, String goodsName, double goodsPrice, int buyNum,
                                String goodsFullSpecs, int refundType, int showRefund, int showMemberComplain,
                                int complainId) {
        this.commonId = commonId;
        this.goodsId = goodsId;
        this.ordersId = ordersId;
        this.orderState = orderState;
        this.showRefundWaiting = showRefundWaiting;
        this.showMemberRefundAll = showMemberRefundAll;
        this.ordersGoodsId = ordersGoodsId;
        this.imageSrc = imageSrc;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.buyNum = buyNum;
        this.goodsFullSpecs = goodsFullSpecs;

        this.refundType = refundType;
        this.showRefund = showRefund;
        this.showMemberComplain = showMemberComplain;
        this.complainId = complainId;
//        this.tariffAmount = tariffAmount;

    }

    public int commonId;
    public int goodsId;
    public int ordersId;
    public int orderState;
    public int showRefundWaiting;
    public int showMemberRefundAll;
    public int ordersGoodsId;
    public String imageSrc;
    public String goodsName;
    public double goodsPrice;
    public int buyNum;
    public String goodsFullSpecs;

    public int refundType;
    public int showRefund;
    public int showMemberComplain;
    public int complainId;
}
