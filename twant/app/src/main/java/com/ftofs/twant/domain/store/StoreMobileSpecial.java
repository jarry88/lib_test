package com.ftofs.twant.domain.store;

public class StoreMobileSpecial {
    /**
     * 专题编号
     */
    private int specialId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 专题描述
     */
    private String specialDesc;

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getSpecialDesc() {
        return specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    @Override
    public String toString() {
        return "StoreMobileSpecial{" +
                "specialId=" + specialId +
                ", storeId=" + storeId +
                ", specialDesc='" + specialDesc + '\'' +
                '}';
    }
}

