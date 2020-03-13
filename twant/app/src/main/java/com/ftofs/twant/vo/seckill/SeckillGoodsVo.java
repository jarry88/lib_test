package com.ftofs.twant.vo.seckill;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 秒杀產品SKU
 *
 * @author shopnc.feng
 * Created on 2017/9/19 11:54
 */
public class SeckillGoodsVo {
    /**
     * 主键
     */
    private int seckillGoodsId;
    /**
     * sku编号
     */
    private int goodsId;
    /**
     * 完整规格<br>
     * 例“颜色：红色；尺码：L”
     */
    private String goodsFullSpecs="";
    /**
     * 產品价格0
     */
    private BigDecimal goodsPrice0 = BigDecimal.ZERO;
    /**
     * 库存
     */
    private int goodsStorage;
    /**
     * 產品价格
     */
    private BigDecimal seckillGoodsPrice = BigDecimal.ZERO;
    /**
     * 库存
     */
    private int seckillGoodsStorage;
    /**
     * 限购数量
     */
    private int seckillLimitAmount;

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getGoodsPrice0() {
        return goodsPrice0;
    }

    public void setGoodsPrice0(BigDecimal goodsPrice0) {
        this.goodsPrice0 = goodsPrice0;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public BigDecimal getSeckillGoodsPrice() {
        return seckillGoodsPrice;
    }

    public void setSeckillGoodsPrice(BigDecimal seckillGoodsPrice) {
        this.seckillGoodsPrice = seckillGoodsPrice;
    }

    public int getSeckillGoodsStorage() {
        return seckillGoodsStorage;
    }

    public void setSeckillGoodsStorage(int seckillGoodsStorage) {
        this.seckillGoodsStorage = seckillGoodsStorage;
    }

    public int getSeckillLimitAmount() {
        return seckillLimitAmount;
    }

    public void setSeckillLimitAmount(int seckillLimitAmount) {
        this.seckillLimitAmount = seckillLimitAmount;
    }

    @Override
    public String toString() {
        return "SeckillGoodsVo{" +
                "seckillGoodsId=" + seckillGoodsId +
                ", goodsId=" + goodsId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice0=" + goodsPrice0 +
                ", goodsStorage=" + goodsStorage +
                ", seckillGoodsPrice=" + seckillGoodsPrice +
                ", seckillGoodsStorage=" + seckillGoodsStorage +
                ", seckillLimitAmount=" + seckillLimitAmount +
                '}';
    }
}
