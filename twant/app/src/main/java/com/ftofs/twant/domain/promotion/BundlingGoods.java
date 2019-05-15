package com.ftofs.twant.domain.promotion;

import java.io.Serializable;
import java.math.BigDecimal;

public class BundlingGoods implements Serializable {
    /**
     * 优惠套装编号
     */
    private int bundlingId;

    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    public BundlingGoods() {
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    @Override
    public String toString() {
        return "BundlingGoods{" +
                "bundlingId=" + bundlingId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsPrice=" + goodsPrice +
                '}';
    }
}
