package com.ftofs.twant.domain.member;



public class PurchaseChat {
	/**
	 * 买家商家沟通记录主键ID
	 */
	private Integer id;

	/**
	 * 商家报价单ID
	 */
	private Integer purchaseQuotedpriceId;

	/**
	 * 沟通内容
	 */
	private String content;

	/**
	 * 沟通人角色 seller:商家 member:买家
	 */
	private String role;

	/**
	 * 添加人ID
	 */
	private Integer userId;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 买家采购计划ID
	 */
	private Integer purchaseBuyId;
	
	public Integer getPurchaseBuyId() {
		return purchaseBuyId;
	}
	public void setPurchaseBuyId(Integer purchaseBuyId) {
		this.purchaseBuyId = purchaseBuyId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPurchaseQuotedpriceId() {
		return purchaseQuotedpriceId;
	}
	public void setPurchaseQuotedpriceId(Integer purchaseQuotedpriceId) {
		this.purchaseQuotedpriceId = purchaseQuotedpriceId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
