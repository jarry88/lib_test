package com.ftofs.twant.domain.cashier;

import java.io.Serializable;
import java.util.List;

public class CashierOrder implements Serializable{
	
	//支付方式
	private String paymentCode;
	//商品列表
	private  List<CashierOrderGoodsItem> goodsItems;
	
	public String getPaymentCode() {
		return paymentCode;
	}
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	public List<CashierOrderGoodsItem> getGoodsItems() {
		return goodsItems;
	}
	public void setGoodsItems(List<CashierOrderGoodsItem> goodsItems) {
		this.goodsItems = goodsItems;
	}
	
}
