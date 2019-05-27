package com.ftofs.twant.domain.distribution;

import java.io.Serializable;
import java.math.BigDecimal;

public class DistributionStoreStat implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 日期(具体到天)
     */
    private String statDate;

    /**
     * 小时
     */
    private int statHour = 0;

    /**
     * 推广量
     */
    private long distributionNum = 0;

    /**
     * 推广金额
     */
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public int getStatHour() {
        return statHour;
    }

    public void setStatHour(int statHour) {
        this.statHour = statHour;
    }

    public long getDistributionNum() {
        return distributionNum;
    }

    public void setDistributionNum(long distributionNum) {
        this.distributionNum = distributionNum;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String toString() {
        return "DistributionStoreStat{" +
                "statId=" + statId +
                ", storeId=" + storeId +
                ", statDate=" + statDate +
                ", statHour=" + statHour +
                ", distributionNum=" + distributionNum +
                ", commissionAmount=" + commissionAmount +
                '}';
    }
}
