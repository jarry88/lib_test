package com.ftofs.twant.vo.contract;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消费保障
 *
 * @author sjz
 * Created 2017/4/17 11:49
 */
public class ContractItemVo {
	private int ctiId;
	private String ctiName;
	private String ctiDescribe;
	private String ctiCost;
	private String ctiIcon;
	private String ctiDescurl;
	private String ctiState;
	private String ctiSort;
	private String state;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getCtiId() {
		return ctiId;
	}
	public void setCtiId(int ctiId) {
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
	public String getCtiCost() {
		return ctiCost;
	}
	public void setCtiCost(String ctiCost) {
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
	public String getCtiState() {
		return ctiState;
	}
	public void setCtiState(String ctiState) {
		this.ctiState = ctiState;
	}
	public String getCtiSort() {
		return ctiSort;
	}
	public void setCtiSort(String ctiSort) {
		this.ctiSort = ctiSort;
	} 
}
