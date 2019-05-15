package com.ftofs.twant.vo.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 采购报价
 *
 * @author dqw
 * Created 2017/4/17 11:54
 */
public class PurchaseQuotedpriceVo {

	private Integer purchaseBuyId;
	private BigDecimal price;
	private String priceMemo;
	private String title;
	private String content;
	private String annex;
	private String replyTime;
	private String companyName;
	private String contacts;
	private String phone;
	private BigDecimal num;
	private String unit;
	
	private String purchaseQuotedpriceId;
	private String replyTimeStr;
	private String createTime;
	private String proName;
	private String status;
	private String statusStr;
	
	private List<PurchaseChatVo> purchaseChatVoList=new ArrayList<PurchaseChatVo>();//留言列表
	
	private String sellerId;
	
	private PurchaseBuyVo purchaseBuyVo=new PurchaseBuyVo();
	
	private String lastChatRole;//最后留言者 seller member
	private String lastChatTime;//最后留言时间

	public String getLastChatRole() {
		return lastChatRole;
	}
	public void setLastChatRole(String lastChatRole) {
		this.lastChatRole = lastChatRole;
	}
	public String getLastChatTime() {
		return lastChatTime;
	}
	public void setLastChatTime(String lastChatTime) {
		this.lastChatTime = lastChatTime;
	}
	public PurchaseBuyVo getPurchaseBuyVo() {
		return purchaseBuyVo;
	}
	public void setPurchaseBuyVo(PurchaseBuyVo purchaseBuyVo) {
		this.purchaseBuyVo = purchaseBuyVo;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public List<PurchaseChatVo> getPurchaseChatVoList() {
		return purchaseChatVoList;
	}
	public void setPurchaseChatVoList(List<PurchaseChatVo> purchaseChatVoList) {
		this.purchaseChatVoList = purchaseChatVoList;
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
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReplyTimeStr() {
		return replyTimeStr;
	}
	public void setReplyTimeStr(String replyTimeStr) {
		this.replyTimeStr = replyTimeStr;
	}
	public String getPurchaseQuotedpriceId() {
		return purchaseQuotedpriceId;
	}
	public void setPurchaseQuotedpriceId(String purchaseQuotedpriceId) {
		this.purchaseQuotedpriceId = purchaseQuotedpriceId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getNum() {
		return num;
	}
	public void setNum(BigDecimal num) {
		this.num = num;
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
}
