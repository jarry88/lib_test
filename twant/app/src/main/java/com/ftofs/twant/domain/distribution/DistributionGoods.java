package com.ftofs.twant.domain.distribution;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class DistributionGoods implements Serializable {
    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 佣金比例
     */
    private int commissionRate = 0;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 总推广量（订单数量）
     */
    private long ordersCount = 0;

    /**
     * 总佣金
     */
    private BigDecimal commissionTotal = BigDecimal.ZERO;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    @Override
    public String toString() {
        return "DistributionGoods{" +
                "commonId=" + commonId +
                ", storeId=" + storeId +
                ", commissionRate=" + commissionRate +
                ", addTime=" + addTime +
                ", ordersCount=" + ordersCount +
                ", commissionTotal=" + commissionTotal +
                '}';
    }
}
