package com.ftofs.twant.domain.orders;

public class CartBundling {
    /**
     * 主键
     */
    private int cartBundlingId;

    /**
     * 购物车id
     */
    private int cartId;

    /**
     * 商品(sku)ID
     */
    private int goodsId = 0;

    /**
     * 商品(spu)ID
     */
    private int commonId = 0;

    /**
     * 会员Id
     */
    private int memberId = 0;

    public int getCartBundlingId() {
        return cartBundlingId;
    }

    public void setCartBundlingId(int cartBundlingId) {
        this.cartBundlingId = cartBundlingId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "CartBundlingVo{" +
                "cartBundlingId=" + cartBundlingId +
                ", cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                '}';
    }
}
