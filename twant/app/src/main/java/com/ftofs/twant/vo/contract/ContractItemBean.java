package com.ftofs.twant.vo.contract;

import java.math.BigDecimal;
import java.io.Serializable;

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
public class ContractItemBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ctiId;

	private String ctiName;

	private String ctiDescribe;

	private BigDecimal ctiCost;

	private String ctiIcon;

	private String ctiDescurl;

	private Integer ctiState=1;

	private Integer ctiSort;
	
	private String ctiIconUrl;

	public String getCtiIconUrl() {
		return ctiIconUrl;
	}

	public void setCtiIconUrl(String ctiIconUrl) {
		this.ctiIconUrl = ctiIconUrl;
	}

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
        return "ContractItemBean{" +
                "ctiId=" + ctiId +
                ", ctiName='" + ctiName + '\'' +
                ", ctiDescribe='" + ctiDescribe + '\'' +
                ", ctiCost=" + ctiCost +
                ", ctiIcon='" + ctiIcon + '\'' +
                ", ctiDescurl='" + ctiDescurl + '\'' +
                ", ctiState=" + ctiState +
                ", ctiSort=" + ctiSort +
                ", ctiIconUrl='" + ctiIconUrl + '\'' +
                '}';
    }
}