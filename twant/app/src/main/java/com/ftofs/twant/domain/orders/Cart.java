package com.ftofs.twant.domain.orders;

import java.io.Serializable;

public class Cart implements Serializable {
    /**
     * 购物车编号
     * 主键、自增
     */
    private int cartId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 產品(sku)ID
     */
    private int goodsId = 0;

    /**
     * 购买数量
     */
    private int buyNum;

    /**
     * 產品(spu)ID
     */
    private int commonId = 0;

    /**
     * 组合套装Id
     */
    private int bundlingId = 0;

    /**
     * 推广订单
     */
    private int distributionOrdersId;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = Math.abs(goodsId);
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
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

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", memberId=" + memberId +
                ", goodsId=" + goodsId +
                ", buyNum=" + buyNum +
                ", commonId=" + commonId +
                ", bundlingId=" + bundlingId +
                ", distributionOrdersId=" + distributionOrdersId +
                '}';
    }
}
