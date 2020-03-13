package com.ftofs.twant.vo.goods;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品SKU，產品发布使用
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:07
 */
public class GoodsJsonVo {
    private int goodsId;
    private String goodsSpecs;
    private String goodsFullSpecs;
    private String specValueIds;
    private BigDecimal goodsPrice0;
    private BigDecimal goodsPrice1;
    private BigDecimal goodsPrice2;
    private String goodsSerial;
    private int goodsStorage;
    private int colorId;
    private String imageSrc;
    //Modify By liusf 2018/10/15 11:50 添加預留庫存
    private int reserveStorage;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(String goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    public BigDecimal getGoodsPrice0() {
        return goodsPrice0;
    }

    public void setGoodsPrice0(BigDecimal goodsPrice0) {
        this.goodsPrice0 = goodsPrice0;
    }

    public BigDecimal getGoodsPrice1() {
        return goodsPrice1;
    }

    public void setGoodsPrice1(BigDecimal goodsPrice1) {
        this.goodsPrice1 = goodsPrice1;
    }

    public BigDecimal getGoodsPrice2() {
        return goodsPrice2;
    }

    public void setGoodsPrice2(BigDecimal goodsPrice2) {
        this.goodsPrice2 = goodsPrice2;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getReserveStorage() {
        return reserveStorage;
    }

    public void setReserveStorage(int reserveStorage) {
        this.reserveStorage = reserveStorage;
    }

    @Override
    public String toString() {
        return "GoodsJsonVo{" +
                "goodsId=" + goodsId +
                ", goodsSpecs='" + goodsSpecs + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", specValueIds='" + specValueIds + '\'' +
                ", goodsPrice0=" + goodsPrice0 +
                ", goodsPrice1=" + goodsPrice1 +
                ", goodsPrice2=" + goodsPrice2 +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsStorage=" + goodsStorage +
                ", colorId=" + colorId +
                ", imageSrc='" + imageSrc + '\'' +
                ", reserveStorage='" + reserveStorage + '\'' +
                '}';
    }
}
