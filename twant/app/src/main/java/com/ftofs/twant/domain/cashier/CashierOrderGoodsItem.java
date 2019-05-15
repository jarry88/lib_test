package com.ftofs.twant.domain.cashier;

public class CashierOrderGoodsItem {
	private Integer commonId;
	private Integer goodsId;//skuId
	private Integer buyNum;//数量
	private Integer cartId;//購物車id

	public Integer getCommonId() {
		return commonId;
	}
	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	public Integer getCartId() {
		return cartId;
	}
	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}
}
