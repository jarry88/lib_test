package com.ftofs.twant.vo;

import com.ftofs.twant.domain.goods.SpecValue;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 后台规格JSON数据实体
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:00
 */
public class SpecAndValueVo {
    /**
     * 规格编号
     */
    private int specId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 店铺编号<br>
     * 0为平台设置
     */
    private int storeId;
    /**
     * 所有规格值，英文“,”隔开
     */
    private String specValueNames;
    /**
     * 规格值
     */
    private List<SpecValue> specValueList;

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getSpecValueNames() {
        return specValueNames;
    }

    public void setSpecValueNames(String specValueNames) {
        this.specValueNames = specValueNames;
    }

    public List<SpecValue> getSpecValueList() {
        return specValueList;
    }

    public void setSpecValueList(List<SpecValue> specValueList) {
        this.specValueList = specValueList;
    }

    @Override
    public String toString() {
        return "SpecAndValueVo{" +
                "specId=" + specId +
                ", specName='" + specName + '\'' +
                ", storeId=" + storeId +
                ", specValueNames='" + specValueNames + '\'' +
                ", specValueList=" + specValueList +
                '}';
    }
}
