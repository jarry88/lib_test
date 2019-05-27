package com.ftofs.twant.vo.member;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 采购订单
 *
 * @author sjz
 * Created 2017/4/17 11:54
 */
public class PurchaseQueryVo {
	private String ordersState;
	private String beginTime;
	private String endTime;
	private String keyword;
	private String orderType;
	private String categoryId;
	private String categoryName;
	private String sort;
	private String priceMemo;
	private String priceMemoShow;

	public String getOrdersState() {
		return ordersState;
	}

	public void setOrdersState(String ordersState) {
		this.ordersState = ordersState;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPriceMemo() {
		return priceMemo;
	}

	public void setPriceMemo(String priceMemo) {
		this.priceMemo = priceMemo;
	}

	public String getPriceMemoShow() {
		return priceMemoShow;
	}

	public void setPriceMemoShow(String priceMemoShow) {
		this.priceMemoShow = priceMemoShow;
	}
}
