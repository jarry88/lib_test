package com.ftofs.twant.domain.statistics;

import java.io.Serializable;

public class StatPriceRange implements Serializable {
    /**
     * 自增编码
     */
    private int rangeId;

    /**
     * 金额
     */
    private int rangePrice = 0;

    /**
     * 金额范围类型（StatPriceRangeType）
     */
    private String rangeType = "";

    public int getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(int rangePrice) {
        this.rangePrice = rangePrice;
    }

    public int getRangeId() {
        return rangeId;
    }

    public void setRangeId(int rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeType() {
        return rangeType;
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    @Override
    public String toString() {
        return "StatPriceRange{" +
                "rangeId=" + rangeId +
                ", rangePrice=" + rangePrice +
                ", rangeType='" + rangeType + '\'' +
                '}';
    }
}
