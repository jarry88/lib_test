package com.ftofs.twant.domain.chain;

public class ChainCart {
    /**
     * 购物车编号[主键、自增]
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
    private int commonId;

    /**
     * 门店產品ID
     */
    private int chainGoodsId;

    /**
     * 门店ID
     */
    private int chainId;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = Math.abs(cartId);
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
        this.goodsId = goodsId;
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

    public int getChainGoodsId() {
        return chainGoodsId;
    }

    public void setChainGoodsId(int chainGoodsId) {
        this.chainGoodsId = chainGoodsId;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    @Override
    public String toString() {
        return "ChainCart{" +
                "cartId=" + cartId +
                ", memberId=" + memberId +
                ", goodsId=" + goodsId +
                ", buyNum=" + buyNum +
                ", commonId=" + commonId +
                ", chainGoodsId=" + chainGoodsId +
                ", chainId=" + chainId +
                '}';
    }
}
