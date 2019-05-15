package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消费保障退出
 *
 * @author sjz
 * Created 2017/4/17 11:50
 */
public class ContractQuitapplyVo {
	private int ctqId;
	private int ctqItemid;
	private int ctqStoreid;
	private String ctqStorename;
	private String ctqAddtime;
	private int ctqAuditstate;
	private String ctqItemname;
	private String ctqState;
	private String cause;
	
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getCtqId() {
		return ctqId;
	}
	public void setCtqId(int ctqId) {
		this.ctqId = ctqId;
	}
	public int getCtqItemid() {
		return ctqItemid;
	}
	public void setCtqItemid(int ctqItemid) {
		this.ctqItemid = ctqItemid;
	}
	public int getCtqStoreid() {
		return ctqStoreid;
	}
	public void setCtqStoreid(int ctqStoreid) {
		this.ctqStoreid = ctqStoreid;
	}
	public String getCtqStorename() {
		return ctqStorename;
	}
	public void setCtqStorename(String ctqStorename) {
		this.ctqStorename = ctqStorename;
	}
	public String getCtqAddtime() {
		return ctqAddtime;
	}
	public void setCtqAddtime(String ctqAddtime) {
		this.ctqAddtime = ctqAddtime;
	}
	public int getCtqAuditstate() {
		return ctqAuditstate;
	}
	public void setCtqAuditstate(int ctqAuditstate) {
		this.ctqAuditstate = ctqAuditstate;
	}
	public String getCtqItemname() {
		return ctqItemname;
	}
	public void setCtqItemname(String ctqItemname) {
		this.ctqItemname = ctqItemname;
	}
	public String getCtqState() {
		return ctqState;
	}
	public void setCtqState(String ctqState) {
		this.ctqState = ctqState;
	}
}