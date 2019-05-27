package com.ftofs.twant.domain;

import java.math.BigDecimal;

public class Exchange {
    /**
     * 匯率主鍵、自增
     */
    private int id;

    /**
     *來源幣種
     */
    private String currencyOrigin;

    /**
     * 來源幣種描述
     */
    private String currencyOriginDesc;

    /**
     * 目標幣種
     */
    private String currencyTarget;

    /**
     * 目標幣種描述
     */
    private String currencyTargetDesc;

    /**
     * 匯率 保留2位小數
     */
    private BigDecimal exchangeRate;

    /**
     * 更新時間
     */
    private String modifyTime;

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyOrigin() {
        return currencyOrigin;
    }

    public void setCurrencyOrigin(String currencyOrigin) {
        this.currencyOrigin = currencyOrigin;
    }

    public String getCurrencyOriginDesc() {
        return currencyOriginDesc;
    }

    public void setCurrencyOriginDesc(String currencyOriginDesc) {
        this.currencyOriginDesc = currencyOriginDesc;
    }

    public String getCurrencyTarget() {
        return currencyTarget;
    }

    public void setCurrencyTarget(String currencyTarget) {
        this.currencyTarget = currencyTarget;
    }

    public String getCurrencyTargetDesc() {
        return currencyTargetDesc;
    }

    public void setCurrencyTargetDesc(String currencyTargetDesc) {
        this.currencyTargetDesc = currencyTargetDesc;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", currencyOrigin='" + currencyOrigin + '\'' +
                ", currencyOriginDesc='" + currencyOriginDesc + '\'' +
                ", currencyTarget='" + currencyTarget + '\'' +
                ", currencyTargetDesc='" + currencyTargetDesc + '\'' +
                ", exchangeRate=" + exchangeRate +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
