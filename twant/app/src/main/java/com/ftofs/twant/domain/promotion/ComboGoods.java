package com.ftofs.twant.domain.promotion;

import java.sql.Timestamp;

public class ComboGoods {
	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 主商品SPU ID 
	 */
	private Integer commonId;

	/**
	 * 组合商品SPU ID
	 */
	private Integer comboCommonId;

	/**
	 * 店铺ID
	 */
	private Integer storeId;

	/**
	 * 组合商品分类名称
	 */
	private String comboClass;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCommonId() {
		return commonId;
	}
	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}
	public Integer getComboCommonId() {
		return comboCommonId;
	}
	public void setComboCommonId(Integer comboCommonId) {
		this.comboCommonId = comboCommonId;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public String getComboClass() {
		return comboClass;
	}
	public void setComboClass(String comboClass) {
		this.comboClass = comboClass;
	}
}
