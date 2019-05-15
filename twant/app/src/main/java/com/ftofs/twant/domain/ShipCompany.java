package com.ftofs.twant.domain;

import java.io.Serializable;

public class ShipCompany implements Serializable {
    /**
     * 快递公司编号
     * 主键、自增
     */
    private int shipId;

    /**
     * 公司名称
     */
    private String shipName;

    /**
     * 是否启用
     * 1-是 0-否
     */
    private int shipState;

    /**
     * 公司英文代码
     */
    private String shipCode;

    /**
     * 公司名称拼音首字母(大写)
     */
    private String shipLetter;

    /**
     * 是否经常使用
     * 0-否 1-是
     */
    private int shipType;

    /**
     * 公司网址
     */
    private String shipUrl;

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getShipState() {
        return shipState;
    }

    public void setShipState(int shipState) {
        this.shipState = shipState;
    }
    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipLetter() {
        return shipLetter;
    }

    public void setShipLetter(String shipLetter) {
        this.shipLetter = shipLetter;
    }

    public int getShipType() {
        return shipType;
    }

    public void setShipType(int shipType) {
        this.shipType = shipType;
    }

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    @Override
    public String toString() {
        return "ShipCompany{" +
                "shipId=" + shipId +
                ", shipName='" + shipName + '\'' +
                ", shipState=" + shipState +
                ", shipCode='" + shipCode + '\'' +
                ", shipLetter=" + shipLetter +
                ", shipType=" + shipType +
                ", shipUrl='" + shipUrl + '\'' +
                '}';
    }
}
