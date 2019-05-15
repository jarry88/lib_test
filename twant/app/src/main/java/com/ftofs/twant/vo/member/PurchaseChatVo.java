package com.ftofs.twant.vo.member;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 采购对话
 *
 * @author sjz
 * Created 2017/4/17 11:54
 */
public class PurchaseChatVo {
	private Integer purchaseQuotedpriceId;
	private String content;
	private String role;
	private String createTime;
	private Integer purchaseBuyId;
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getPurchaseBuyId() {
		return purchaseBuyId;
	}
	public void setPurchaseBuyId(Integer purchaseBuyId) {
		this.purchaseBuyId = purchaseBuyId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
}
