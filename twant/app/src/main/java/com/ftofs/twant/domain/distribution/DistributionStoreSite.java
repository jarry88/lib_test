package com.ftofs.twant.domain.distribution;

import java.io.Serializable;

public class DistributionStoreSite implements Serializable {
    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 佣金比例
     */
    private int commissionRate;

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

    @Override
    public String toString() {
        return "DistributionStoreSite{" +
                "storeId=" + storeId +
                ", commissionRate=" + commissionRate +
                '}';
    }
}
