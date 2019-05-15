package com.ftofs.twant.domain.statistics;

import java.io.Serializable;

public class StatStorePriceRange implements Serializable {
    /**
     * 自增编码
     */
    private int rangeId;

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 金额
     */
    private int rangePrice = 0;

    /**
     * 金额范围类型（StatPriceRangeType）
     */
    private String rangeType = "";

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(int rangePrice) {
        this.rangePrice = rangePrice;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    @Override
    public String toString() {
        return "StatStorePriceRange{" +
                "rangeId=" + rangeId +
                ", storeId=" + storeId +
                ", rangePrice=" + rangePrice +
                ", rangeType='" + rangeType + '\'' +
                '}';
    }
}
