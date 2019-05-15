package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class SpecValue implements Serializable {
    /**
     * 规格值编号
     */
    private int specValueId;

    /**
     * 规格编号
     */
    private int specId;

    /**
     * 规格值名称
     */
    private String specValueName;

    public int getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(int specValueId) {
        this.specValueId = specValueId;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    @Override
    public String toString() {
        return "SpecValue{" +
                "specValueId=" + specValueId +
                ", specId=" + specId +
                ", specValueName='" + specValueName + '\'' +
                '}';
    }
}
