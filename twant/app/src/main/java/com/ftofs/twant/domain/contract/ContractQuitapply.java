package com.ftofs.twant.domain.contract;

public class ContractQuitapply implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer ctqId;
	
	/**
	 * 项目ID
	 */
	private Integer ctqItemid;
	
	/**
	 * 项目名称
	 */
	private String ctqItemname;
	
	/**
	 * 店铺ID
	 */
	private Integer ctqStoreid;
	
	/**
	 * 店铺名称
	 */
	private String ctqStorename;
	
	/**
	 * 添加时间
	 */
	private String ctqAddtime;
	
	/**
	 * 审核状态0未审核1审核通过2审核失败
	 */
	private Integer ctqAuditstate;

	public Integer getCtqId() {
		return ctqId;
	}

	public void setCtqId(Integer ctqId) {
		this.ctqId = ctqId;
	}

	public Integer getCtqItemid() {
		return ctqItemid;
	}

	public void setCtqItemid(Integer ctqItemid) {
		this.ctqItemid = ctqItemid;
	}

	public String getCtqItemname() {
		return ctqItemname;
	}

	public void setCtqItemname(String ctqItemname) {
		this.ctqItemname = ctqItemname;
	}

	public Integer getCtqStoreid() {
		return ctqStoreid;
	}

	public void setCtqStoreid(Integer ctqStoreid) {
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

	public Integer getCtqAuditstate() {
		return ctqAuditstate;
	}

	public void setCtqAuditstate(Integer ctqAuditstate) {
		this.ctqAuditstate = ctqAuditstate;
	}
}