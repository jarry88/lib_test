package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class Spec implements Serializable {
    /**
     * 规格编号
     */
    private int specId;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 店铺编号
     * 0为平台设置
     */
    private int storeId = 0;

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

    @Override
    public String toString() {
        return "Spec{" +
                "specId=" + specId +
                ", specName='" + specName + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
