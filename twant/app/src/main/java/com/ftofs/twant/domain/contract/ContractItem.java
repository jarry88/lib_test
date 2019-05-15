package com.ftofs.twant.domain.contract;

import java.math.BigDecimal;

public class ContractItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private Integer ctiId;
	
	/**
	 * 保障项目名称
	 */
	private String ctiName;
	
	/**
	 * 保障项目描述
	 */
	private String ctiDescribe;
	
	/**
	 * 保证金
	 */
	private BigDecimal ctiCost;
	
	/**
	 * 图标
	 */
	private String ctiIcon;
	
	/**
	 * 内容介绍文章地址
	 */
	private String ctiDescurl;
	
	/**
	 * 状态 0关闭 1开启
	 */
	private Integer ctiState=1;
	
	/**
	 * 排序
	 */
	private Integer ctiSort;

	public Integer getCtiId() {
		return ctiId;
	}

	public void setCtiId(Integer ctiId) {
		this.ctiId = ctiId;
	}

	public String getCtiName() {
		return ctiName;
	}

	public void setCtiName(String ctiName) {
		this.ctiName = ctiName;
	}

	public String getCtiDescribe() {
		return ctiDescribe;
	}

	public void setCtiDescribe(String ctiDescribe) {
		this.ctiDescribe = ctiDescribe;
	}

	public BigDecimal getCtiCost() {
		return ctiCost;
	}

	public void setCtiCost(BigDecimal ctiCost) {
		this.ctiCost = ctiCost;
	}

	public String getCtiIcon() {
		return ctiIcon;
	}

	public void setCtiIcon(String ctiIcon) {
		this.ctiIcon = ctiIcon;
	}

	public String getCtiDescurl() {
		return ctiDescurl;
	}

	public void setCtiDescurl(String ctiDescurl) {
		this.ctiDescurl = ctiDescurl;
	}

	public Integer getCtiState() {
		return ctiState;
	}

	public void setCtiState(Integer ctiState) {
		this.ctiState = ctiState;
	}

	public Integer getCtiSort() {
		return ctiSort;
	}

	public void setCtiSort(Integer ctiSort) {
		this.ctiSort = ctiSort;
	}

    @Override
    public String toString() {
        return "ContractItem{" +
                "ctiId=" + ctiId +
                ", ctiName='" + ctiName + '\'' +
                ", ctiDescribe='" + ctiDescribe + '\'' +
                ", ctiCost=" + ctiCost +
                ", ctiIcon='" + ctiIcon + '\'' +
                ", ctiDescurl='" + ctiDescurl + '\'' +
                ", ctiState=" + ctiState +
                ", ctiSort=" + ctiSort +
                '}';
    }
}