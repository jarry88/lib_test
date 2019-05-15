package com.ftofs.twant.vo.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ftofs.twant.domain.member.PurchaseBuy;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 采购
 *
 * @author sjz
 * Created 2017/4/17 11:53
 */
public class PurchaseBuyVo {
	
	private String title;
	private String proName;
	private String norm;
	private BigDecimal num;
	private String unit;
	private BigDecimal price;
	private String priceMemo;
	private String picUrl;
	private String content;
	private String contacts;
	private String phone;
	private String purType;
	private String supplierArea;
	private String receiveArea;
	private String purchaseId;
	
	private String createTime;
	private String status;
	private String statusStr;
	private String purchaseBuyId;
	private String memberName;//买家昵称
	private String memberArea;//买家所在地区
	private String endTime;
	private String quotedpriceCount;
	private int needInvoice;//是否需要发票
	private String needInvoiceStr;
	private String remainDays;//剩余天数
	private int categoryId;
	private int contactType;//联系方式 1报价后可见 2公开
	private String showQuotedprice;//报价截止时间到期后才能查看报价单
	private String purchaseBuyCount;//采购计划数
	private String receiveTime;//期望收货日期
	
	private List<PurchaseBuy> purchaseBuyList=new ArrayList<PurchaseBuy>();
	private List<PurchaseBuy> categoryPurchaseBuyList=new ArrayList<PurchaseBuy>();
	
	public String getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public List<PurchaseBuy> getCategoryPurchaseBuyList() {
		return categoryPurchaseBuyList;
	}
	public void setCategoryPurchaseBuyList(List<PurchaseBuy> categoryPurchaseBuyList) {
		this.categoryPurchaseBuyList = categoryPurchaseBuyList;
	}
	public List<PurchaseBuy> getPurchaseBuyList() {
		return purchaseBuyList;
	}
	public void setPurchaseBuyList(List<PurchaseBuy> purchaseBuyList) {
		this.purchaseBuyList = purchaseBuyList;
	}
	public String getPurchaseBuyCount() {
		return purchaseBuyCount;
	}
	public void setPurchaseBuyCount(String purchaseBuyCount) {
		this.purchaseBuyCount = purchaseBuyCount;
	}
	public int getContactType() {
		return contactType;
	}
	public void setContactType(int contactType) {
		this.contactType = contactType;
	}
	public String getShowQuotedprice() {
		return showQuotedprice;
	}
	public void setShowQuotedprice(String showQuotedprice) {
		this.showQuotedprice = showQuotedprice;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getRemainDays() {
		return remainDays;
	}
	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}
	public String getNeedInvoiceStr() {
		return needInvoiceStr;
	}
	public void setNeedInvoiceStr(String needInvoiceStr) {
		this.needInvoiceStr = needInvoiceStr;
	}
	public int getNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(int needInvoice) {
		this.needInvoice = needInvoice;
	}
	public String getQuotedpriceCount() {
		return quotedpriceCount;
	}
	public void setQuotedpriceCount(String quotedpriceCount) {
		this.quotedpriceCount = quotedpriceCount;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberArea() {
		return memberArea;
	}
	public void setMemberArea(String memberArea) {
		this.memberArea = memberArea;
	}
	public String getPurchaseBuyId() {
		return purchaseBuyId;
	}
	public void setPurchaseBuyId(String purchaseBuyId) {
		this.purchaseBuyId = purchaseBuyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusStr() {
		return statusStr;
	}
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	
	
}
