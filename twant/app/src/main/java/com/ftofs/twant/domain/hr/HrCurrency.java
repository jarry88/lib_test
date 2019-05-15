package com.ftofs.twant.domain.hr;

public class HrCurrency {
    /**
     * 薪資單位id
     */
    private int currencyId;

    /**
     * 薪資貨幣名稱
     */
    private String currencyName;

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return "HrCurrency{" +
                "currencyId=" + currencyId +
                ", currencyName='" + currencyName + '\'' +
                '}';
    }
}
