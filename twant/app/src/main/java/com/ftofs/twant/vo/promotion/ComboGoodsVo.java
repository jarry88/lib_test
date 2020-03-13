package com.ftofs.twant.vo.promotion;

import java.util.ArrayList;
import java.util.List;

import com.ftofs.twant.domain.goods.GoodsCommon;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 组合產品
 *
 * @author sjz
 * Created 2017/4/17 11:55
 */
public class ComboGoodsVo {
	private String commonId;
	private String comboClass;
	List<GoodsCommon> goodsVoList=new ArrayList<>();
	
	private String comboCommonId;
	private String mainCommonId;
	
	public String getCommonId() {
		return commonId;
	}

	public void setCommonId(String commonId) {
		this.commonId = commonId;
	}

	public String getComboCommonId() {
		return comboCommonId;
	}

	public void setComboCommonId(String comboCommonId) {
		this.comboCommonId = comboCommonId;
	}

	public String getMainCommonId() {
		return mainCommonId;
	}

	public void setMainCommonId(String mainCommonId) {
		this.mainCommonId = mainCommonId;
	}

	public String getComboClass() {
		return comboClass;
	}

	public void setComboClass(String comboClass) {
		this.comboClass = comboClass;
	}

	public List<GoodsCommon> getGoodsVoList() {
		return goodsVoList;
	}

	public void setGoodsVoList(List<GoodsCommon> goodsVoList) {
		this.goodsVoList = goodsVoList;
	}

	@Override
	public String toString() {
		return "ComboGoodsVo{" +
				"commonId='" + commonId + '\'' +
				", comboClass='" + comboClass + '\'' +
				", goodsVoList=" + goodsVoList +
				", comboCommonId='" + comboCommonId + '\'' +
				", mainCommonId='" + mainCommonId + '\'' +
				'}';
	}
}
