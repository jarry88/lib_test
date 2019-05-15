package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class StoreMessageSeller implements Serializable {
    /**
     * 卖家编号
     */
    private int sellerId;

    /**
     * 消息模板编码
     */
    private String tplCode;

    /**
     * 店铺编号
     */
    private int storeId;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "StoreMessageSeller{" +
                "sellerId=" + sellerId +
                ", tplCode='" + tplCode + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
