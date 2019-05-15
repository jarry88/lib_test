package com.ftofs.twant.domain.member;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PurchaseBuy {
	/**
	 * 采购计划 主键ID
	 */
	private Integer id;

	/**
	 * 买家ID
	 */
	private Integer memberId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 商品名称
	 */
	private String proName;

	/**
	 * 规格
	 */
	private String norm;

	/**
	 * 数量
	 */
	private BigDecimal num;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 价格说明   议价，不可议价，面议
	 */
	private String priceMemo;

	/**
	 * 图片地址
	 */
	private String picUrl;

	/**
	 * 详细说明
	 */
	private String content;

	/**
	 * 联系人
	 */
	private String contacts;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 采购截止日期
	 */
	private Timestamp endTime;

	/**
	 * 采购类型   现货/标准品、紧急采购、二手商品、库存、求租、代理加盟、合作
	 */
	private String purType;

	/**
	 * 期望供应商所在地
	 */
	private String supplierArea;

	/**
	 * 收货地
	 */
	private String receiveArea;

	/**
	 * 状态 0关闭 1开启
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	/**
	 * 是否需要发票 1:需要 0:不需要
	 */
	private Integer needInvoice;

	/**
	 * 分类编号（一级）
	 */
	private Integer categoryId;

	/**
	 * 联系方式 1报价后可见 2公开
	 */
	private Integer contactType;

	/**
	 * 报价截止时间到期后才能查看报价单 1是 0否
	 */
	private Integer showQuotedprice;

	/**
	 * 期望收货日期
	 */
	private Timestamp receiveTime;

	/**
	 * 采购单号
	 */
	private String purchaseId;
	
	public String getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	public Timestamp getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Timestamp receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Integer getContactType() {
		return contactType;
	}
	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}
	public Integer getShowQuotedprice() {
		return showQuotedprice;
	}
	public void setShowQuotedprice(Integer showQuotedprice) {
		this.showQuotedprice = showQuotedprice;
	}
	public Integer getNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(Integer needInvoice) {
		this.needInvoice = needInvoice;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getNorm() {
		return norm;
	}
	public void setNorm(String norm) {
		this.norm = norm;
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getPurType() {
		return purType;
	}
	public void setPurType(String purType) {
		this.purType = purType;
	}
	public String getSupplierArea() {
		return supplierArea;
	}
	public void setSupplierArea(String supplierArea) {
		this.supplierArea = supplierArea;
	}
	public String getReceiveArea() {
		return receiveArea;
	}
	public void setReceiveArea(String receiveArea) {
		this.receiveArea = receiveArea;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
