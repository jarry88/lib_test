package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消费保障消费日志
 *
 * @author sjz
 * Created 2017/4/17 11:51
 */
public class ContractCostlogVo {
	private int clogId;
	private int clogItemid;
	private String clogItemname;
	private int clogStoreid;
	private String clogStorename;
	private int clogAdminid;
	private String clogAdminname;
	private String clogPrice;
	private String clogAddtime;
	private String clogDesc;
	
	public int getClogId() {
		return clogId;
	}
	public void setClogId(int clogId) {
		this.clogId = clogId;
	}
	public int getClogItemid() {
		return clogItemid;
	}
	public void setClogItemid(int clogItemid) {
		this.clogItemid = clogItemid;
	}
	public String getClogItemname() {
		return clogItemname;
	}
	public void setClogItemname(String clogItemname) {
		this.clogItemname = clogItemname;
	}
	public int getClogStoreid() {
		return clogStoreid;
	}
	public void setClogStoreid(int clogStoreid) {
		this.clogStoreid = clogStoreid;
	}
	public String getClogStorename() {
		return clogStorename;
	}
	public void setClogStorename(String clogStorename) {
		this.clogStorename = clogStorename;
	}
	public int getClogAdminid() {
		return clogAdminid;
	}
	public void setClogAdminid(int clogAdminid) {
		this.clogAdminid = clogAdminid;
	}
	public String getClogAdminname() {
		return clogAdminname;
	}
	public void setClogAdminname(String clogAdminname) {
		this.clogAdminname = clogAdminname;
	}
	public String getClogPrice() {
		return clogPrice;
	}
	public void setClogPrice(String clogPrice) {
		this.clogPrice = clogPrice;
	}
	public String getClogAddtime() {
		return clogAddtime;
	}
	public void setClogAddtime(String clogAddtime) {
		this.clogAddtime = clogAddtime;
	}
	public String getClogDesc() {
		return clogDesc;
	}
	public void setClogDesc(String clogDesc) {
		this.clogDesc = clogDesc;
	}
}