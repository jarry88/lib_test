package com.ftofs.twant.domain;

import java.math.BigDecimal;

public class FreightArea {
    /**
     * 主键、自增
     */
    private int freightAreaId;

    /**
     * 运费模板Id
     */
    private int freightId;

    /**
     * 第三级地区ID组成的串，以英文","隔开，两端也有","
     */
    private String areaId = "";

    /**
     * 地区name组成的串，以英文","隔开
     */
    private String areaName = "";

    /**
     * 首[件数、重量、体积]
     */
    private BigDecimal item1 = new BigDecimal(0);

    /**
     * 价格
     */
    private BigDecimal price1 = new BigDecimal(0);

    /**
     * 续[件数、重量、体积]
     */
    private BigDecimal item2 = new BigDecimal(0);

    /**
     * 价格
     */
    private BigDecimal price2 = new BigDecimal(0);

    public int getFreightAreaId() {
        return freightAreaId;
    }

    public void setFreightAreaId(int freightAreaId) {
        this.freightAreaId = freightAreaId;
    }

    public int getFreightId() {
        return freightId;
    }

    public void setFreightId(int freightId) {
        this.freightId = freightId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal getItem1() {
        return item1;
    }

    public void setItem1(BigDecimal item1) {
        this.item1 = item1;
    }

    public BigDecimal getItem2() {
        return item2;
    }

    public void setItem2(BigDecimal item2) {
        this.item2 = item2;
    }

    @Override
    public String toString() {
        return "FreightArea{" +
                "freightAreaId=" + freightAreaId +
                ", freightId=" + freightId +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", item1=" + item1 +
                ", price1=" + price1 +
                ", item2=" + item2 +
                ", price2=" + price2 +
                '}';
    }
}
