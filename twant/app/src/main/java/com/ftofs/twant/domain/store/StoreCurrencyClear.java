package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class StoreCurrencyClear implements Serializable {
	//主键
	private Integer id;
	//总额
	private BigDecimal amount;
	//详情json格式(前端组合)
	private String description;
	//店铺id
	private Integer storeId;
	//清算状态(清算状态：0-上班,1-下班)
	private Integer clearStatus;
	//状态：1-正常，0-删除
	private Integer state;
	//创建时间
	private Timestamp createTime;
	//更新时间
	private Timestamp updateTime;

	public StoreCurrencyClear() {
	}

	public StoreCurrencyClear(BigDecimal amount, String description, Integer storeId,
			 Integer clearStatus, Integer state, Timestamp createTime,Timestamp updateTime) {
		this.amount = amount;
		this.description = description;
		this.storeId = storeId;
		this.clearStatus = clearStatus;
		this.state = state;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStoreId() {
		return this.storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getClearStatus() {
		return this.clearStatus;
	}

	public void setClearStatus(Integer clearStatus) {
		this.clearStatus = clearStatus;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

}