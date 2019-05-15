package com.ftofs.twant.domain.contract;

import java.io.Serializable;
import java.math.BigDecimal;

public class Contract implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    private Integer ctId;
    
    /**
     * 店铺ID
     */
    private Integer ctStoreid;
    
    /**
     * 店铺名称
     */
    private String ctStorename;
    
    /**
     * 服务项目ID
     */
    private Integer ctItemid;
    
    /**
     * 申请审核状态0未审核1审核通过2审核失败3已支付保证金4保证金审核通过5保证金审核失败
     */
    private Integer ctAuditstate;
    
    /**
     * 加入状态 0未申请 1已申请 2已加入
     */
    private Integer ctJoinstate;
    
    /**
     * 保证金余额
     */
    private BigDecimal ctCost;
    
    /**
     * 永久关闭 0永久关闭 1开启
     */
    private Integer ctClosestate;
    
    /**
     * 退出申请状态0未申请 1已申请 2申请失败
     */
    private Integer ctQuitstate;

	public Integer getCtId() {
		return ctId;
	}

	public void setCtId(Integer ctId) {
		this.ctId = ctId;
	}

	public Integer getCtStoreid() {
		return ctStoreid;
	}

	public void setCtStoreid(Integer ctStoreid) {
		this.ctStoreid = ctStoreid;
	}

	public String getCtStorename() {
		return ctStorename;
	}

	public void setCtStorename(String ctStorename) {
		this.ctStorename = ctStorename;
	}

	public Integer getCtItemid() {
		return ctItemid;
	}

	public void setCtItemid(Integer ctItemid) {
		this.ctItemid = ctItemid;
	}

	public Integer getCtAuditstate() {
		return ctAuditstate;
	}

	public void setCtAuditstate(Integer ctAuditstate) {
		this.ctAuditstate = ctAuditstate;
	}

	public Integer getCtJoinstate() {
		return ctJoinstate;
	}

	public void setCtJoinstate(Integer ctJoinstate) {
		this.ctJoinstate = ctJoinstate;
	}

	public BigDecimal getCtCost() {
		return ctCost;
	}

	public void setCtCost(BigDecimal ctCost) {
		this.ctCost = ctCost;
	}

	public Integer getCtClosestate() {
		return ctClosestate;
	}

	public void setCtClosestate(Integer ctClosestate) {
		this.ctClosestate = ctClosestate;
	}

	public Integer getCtQuitstate() {
		return ctQuitstate;
	}

	public void setCtQuitstate(Integer ctQuitstate) {
		this.ctQuitstate = ctQuitstate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
