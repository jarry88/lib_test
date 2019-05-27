package com.ftofs.twant.domain.member;

import java.math.BigDecimal;

public class PurchaseQuotedprice {
	/**
	 * 报价单主键ID
	 */
	private Integer id;

	/**
	 * 商家ID
	 */
	private Integer sellerId;

	/**
	 * 买家采购计划表ID
	 */
	private Integer purchaseBuyId;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 价格说明
	 */
	private String priceMemo;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 报价说明
	 */
	private String content;

	/**
	 * 附件
	 */
	private String annex;

	/**
	 * 答复日期
	 */
	private String replyTime;

	/**
	 * 公司名称
	 */
	private String companyName;

	/**
	 * 联系人
	 */
	private String contacts;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 状态 0关闭 1开启
	 */
	private Integer state;

	/**
	 * 数量
	 */
	private BigDecimal num;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 采购单标题
	 * 冗余字段 用于查询
	 */
	private String purchaseBuyTitle;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSellerId() {
		return sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getPurchaseBuyId() {
		return purchaseBuyId;
	}

	public void setPurchaseBuyId(Integer purchaseBuyId) {
		this.purchaseBuyId = purchaseBuyId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPriceMemo() {
		return priceMemo;
	}

	public void setPriceMemo(String priceMemo) {
		this.priceMemo = priceMemo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPurchaseBuyTitle() {
		return purchaseBuyTitle;
	}

	public void setPurchaseBuyTitle(String purchaseBuyTitle) {
		this.purchaseBuyTitle = purchaseBuyTitle;
	}
}
