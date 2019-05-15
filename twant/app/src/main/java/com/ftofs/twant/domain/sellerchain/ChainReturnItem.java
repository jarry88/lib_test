package com.ftofs.twant.domain.sellerchain;

import java.math.BigDecimal;

/**
 * @function 門店退貨項
 * @description
 * @param
 * @return
 * @author Nick.Chung
 * @create 2018/9/22 19:25
 */
public class ChainReturnItem {
    /**
     * 退貨理由
     */
    private String buyerMessage;
    /**
     * 退款金額
     */
    private BigDecimal refundAmount;
    /**
     * 訂單編號
     */
    private Integer ordersId;
    /**
     * 訂單商品編號
     */
    private Integer ordersGoodsId;
    /**
     * 退貨理由編號
     */
    private Integer reasonId;
    /**
     * 退貨數量
     */
    private Integer goodsNum;
    /**
     * 買家編號
     */
    private Integer memberId;
    /**
     * 門店編號
     */
    private Integer storeId;

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Integer getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Integer ordersId) {
        this.ordersId = ordersId;
    }

    public Integer getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(Integer ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}