package com.ftofs.twant.domain.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ContractCostlog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Integer clogId;
	
	/**
	 * 保障项目ID
	 */
	private Integer clogItemid;
	
	/**
	 * 保障项目名称
	 */
	private String clogItemname;
	
	/**
	 * 店铺ID
	 */
	private Integer clogStoreid;
	
	/**
	 * 店铺名称
	 */
	private String clogStorename;
	
	/**
	 * 操作管理员ID
	 */
	private Integer clogAdminid;
	
	/**
	 * 操作管理员名称
	 */
	private String clogAdminname;
	
	/**
	 * 金额
	 */
	private BigDecimal clogPrice;
	
	/**
	 * 添加时间
	 */
	private Timestamp clogAddtime;
	
	/**
	 * 描述
	 */
	private String clogDesc;

	public Integer getClogId() {
		return clogId;
	}

	public void setClogId(Integer clogId) {
		this.clogId = clogId;
	}

	public Integer getClogItemid() {
		return clogItemid;
	}

	public void setClogItemid(Integer clogItemid) {
		this.clogItemid = clogItemid;
	}

	public String getClogItemname() {
		return clogItemname;
	}

	public void setClogItemname(String clogItemname) {
		this.clogItemname = clogItemname;
	}

	public Integer getClogStoreid() {
		return clogStoreid;
	}

	public void setClogStoreid(Integer clogStoreid) {
		this.clogStoreid = clogStoreid;
	}

	public String getClogStorename() {
		return clogStorename;
	}

	public void setClogStorename(String clogStorename) {
		this.clogStorename = clogStorename;
	}

	public Integer getClogAdminid() {
		return clogAdminid;
	}

	public void setClogAdminid(Integer clogAdminid) {
		this.clogAdminid = clogAdminid;
	}

	public String getClogAdminname() {
		return clogAdminname;
	}

	public void setClogAdminname(String clogAdminname) {
		this.clogAdminname = clogAdminname;
	}

	public BigDecimal getClogPrice() {
		return clogPrice;
	}

	public void setClogPrice(BigDecimal clogPrice) {
		this.clogPrice = clogPrice;
	}

	public Timestamp getClogAddtime() {
		return clogAddtime;
	}

	public void setClogAddtime(Timestamp clogAddtime) {
		this.clogAddtime = clogAddtime;
	}

	public String getClogDesc() {
		return clogDesc;
	}

	public void setClogDesc(String clogDesc) {
		this.clogDesc = clogDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}