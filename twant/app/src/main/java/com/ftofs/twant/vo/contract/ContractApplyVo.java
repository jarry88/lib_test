package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消费保障申请
 *
 * @author sjz
 * Created 2017/4/17 11:49
 */
public class ContractApplyVo {
	
	private int ctaId;
	private int ctaItemid;
	private int ctaStoreid;
	private String ctaStorename;
	private String ctaAddtime;
	private int ctaAuditstate;
	private String ctaCost;
	private String ctaCostimg;
	private String ctaItemname;
	private String ctaState;
	private String cause;//原因
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCtaState() {
		return ctaState;
	}
	public void setCtaState(String ctaState) {
		this.ctaState = ctaState;
	}
	public String getCtaItemname() {
		return ctaItemname;
	}
	public void setCtaItemname(String ctaItemname) {
		this.ctaItemname = ctaItemname;
	}
	public int getCtaId() {
		return ctaId;
	}
	public void setCtaId(int ctaId) {
		this.ctaId = ctaId;
	}
	public int getCtaItemid() {
		return ctaItemid;
	}
	public void setCtaItemid(int ctaItemid) {
		this.ctaItemid = ctaItemid;
	}
	public int getCtaStoreid() {
		return ctaStoreid;
	}
	public void setCtaStoreid(int ctaStoreid) {
		this.ctaStoreid = ctaStoreid;
	}
	public String getCtaStorename() {
		return ctaStorename;
	}
	public void setCtaStorename(String ctaStorename) {
		this.ctaStorename = ctaStorename;
	}
	public String getCtaAddtime() {
		return ctaAddtime;
	}
	public void setCtaAddtime(String ctaAddtime) {
		this.ctaAddtime = ctaAddtime;
	}
	public int getCtaAuditstate() {
		return ctaAuditstate;
	}
	public void setCtaAuditstate(int ctaAuditstate) {
		this.ctaAuditstate = ctaAuditstate;
	}
	public String getCtaCost() {
		return ctaCost;
	}
	public void setCtaCost(String ctaCost) {
		this.ctaCost = ctaCost;
	}
	public String getCtaCostimg() {
		return ctaCostimg;
	}
	public void setCtaCostimg(String ctaCostimg) {
		this.ctaCostimg = ctaCostimg;
	}
}