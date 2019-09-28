package com.ftofs.twant.entity.order;


/**
 * 訂單詳情的商品列表Item
 * @author zwm
 */
public class OrderDetailGoodsItem {
    public OrderDetailGoodsItem(int commonId, int goodsId, int ordersId, int ordersGoodsId,
                                String imageSrc, String goodsName, float goodsPrice, int buyNum,
                                String goodsFullSpecs, int refundType, int showRefund, int showMemberComplain,
                                int complainId) {
        this.commonId = commonId;
        this.goodsId = goodsId;
        this.ordersId = ordersId;
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
    }

    public int commonId;
    public int goodsId;
    public int ordersId;
    public int ordersGoodsId;
    public String imageSrc;
    public String goodsName;
    public float goodsPrice;
    public int buyNum;
    public String goodsFullSpecs;

    public int refundType;
    public int showRefund;
    public int showMemberComplain;
    public int complainId;
}
