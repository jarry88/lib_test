package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.goods.Goods;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品sku
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:11
 */
public class GoodsSkuVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;
    /**
     * 商品价格0
     */
    private BigDecimal goodsPrice0;
    /**
     * 商品价格1
     */
    private BigDecimal goodsPrice1;
    /**
     * 商品价格2
     */
    private BigDecimal goodsPrice2;
    /**
     * 全部价格
     */
    private String goodsPriceAll;
    /**
     * 商品货号
     */
    private String goodsSerial;
    /**
     * 颜色规格值编号
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 库存
     */
    private int goodsStorage;

    public GoodsSkuVo(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.commonId = goods.getCommonId();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.goodsPrice0 = goods.getGoodsPrice0();
        this.goodsPrice1 = goods.getGoodsPrice1();
        this.goodsPrice2 = goods.getGoodsPrice2();
        this.goodsSerial = goods.getGoodsSerial();
        this.colorId = goods.getColorId();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goods.getGoodsStorage();
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

    public String getGoodsPriceAll() {
        return goodsPriceAll;
    }

    public void setGoodsPriceAll(String goodsPriceAll) {
        this.goodsPriceAll = goodsPriceAll;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
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

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    @Override
    public String toString() {
        return "GoodsSkuVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice0=" + goodsPrice0 +
                ", goodsPrice1=" + goodsPrice1 +
                ", goodsPrice2=" + goodsPrice2 +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", colorId=" + colorId +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsStorage=" + goodsStorage +
                '}';
    }
}
