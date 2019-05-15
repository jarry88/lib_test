package com.ftofs.twant.domain.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ContractApply implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer ctaId;
	
	/**
	 * 保障项目ID
	 */
	private Integer ctaItemid;
	
	/**
	 * 店铺ID
	 */
	private Integer ctaStoreid;
	
	/**
	 * 店铺名称
	 */
	private String ctaStorename;
	
	/**
	 * 申请时间
	 */
	private Timestamp ctaAddtime;
	
	/**
	 * 审核状态 0未审核 1审核通过 2审核失败 3保证金待审核 4保证金审核通过 5保证金审核失败
	 */
	private Integer ctaAuditstate=0;
	
	/**
	 * 保证金金额
	 */
	private BigDecimal ctaCost;
	
	/**
	 * 保证金付款凭证图片
	 */
	private String ctaCostimg;

	public Integer getCtaId() {
		return ctaId;
	}

	public void setCtaId(Integer ctaId) {
		this.ctaId = ctaId;
	}

	public Integer getCtaItemid() {
		return ctaItemid;
	}

	public void setCtaItemid(Integer ctaItemid) {
		this.ctaItemid = ctaItemid;
	}

	public Integer getCtaStoreid() {
		return ctaStoreid;
	}

	public void setCtaStoreid(Integer ctaStoreid) {
		this.ctaStoreid = ctaStoreid;
	}

	public String getCtaStorename() {
		return ctaStorename;
	}

	public void setCtaStorename(String ctaStorename) {
		this.ctaStorename = ctaStorename;
	}

	public Timestamp getCtaAddtime() {
		return ctaAddtime;
	}

	public void setCtaAddtime(Timestamp ctaAddtime) {
		this.ctaAddtime = ctaAddtime;
	}

	public Integer getCtaAuditstate() {
		return ctaAuditstate;
	}

	public void setCtaAuditstate(Integer ctaAuditstate) {
		this.ctaAuditstate = ctaAuditstate;
	}

	public BigDecimal getCtaCost() {
		return ctaCost;
	}

	public void setCtaCost(BigDecimal ctaCost) {
		this.ctaCost = ctaCost;
	}

	public String getCtaCostimg() {
		return ctaCostimg;
	}

	public void setCtaCostimg(String ctaCostimg) {
		this.ctaCostimg = ctaCostimg;
	}
}