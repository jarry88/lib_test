package com.ftofs.twant.domain.store;

import java.io.Serializable;


public class SellerMemo implements Serializable {
	//便签id
	private Integer id;
	//售货员id
	private Integer sellerId;
	//便签内容
	private String content;
	//状态
	private Integer state;
	//创建时间
	private String createTime;
	//修改时间
	private String modifyTime;

	public SellerMemo() {
	}

	public SellerMemo(Integer sellerId, String content, Integer state,
			String createTime, String modifyTime) {
		this.sellerId = sellerId;
		this.content = content;
		this.state = state;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(Integer sellerId) {
		this.sellerId = sellerId;
	}


	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}


	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}