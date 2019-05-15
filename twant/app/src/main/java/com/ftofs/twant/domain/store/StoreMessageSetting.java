package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class StoreMessageSetting implements Serializable {
    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 消息模板编号
     */
    private String tplCode;

    /**
     * 是否接收
     */
    private int isReceive;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    @Override
    public String toString() {
        return "StoreMessageSetting{" +
                "storeId=" + storeId +
                ", tplCode='" + tplCode + '\'' +
                ", isReceive=" + isReceive +
                '}';
    }
}
