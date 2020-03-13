package com.ftofs.twant.vo.orders;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 订单赠品Vo
 * 
 * @author hbj
 * Created 2017/4/13 14:46
 */
public class OrdersGiftVo {
    /**
     * SKU
     */
    private int goodsId;
    /**
     * 订单Id
     */
    private int ordersId;
    /**
     * SPU
     */
    private int commonId;
    /**
     * 赠送数量
     */
    private int giftNum;
    /**
     * 產品名称
     */
    private String goodsName;
    /**
     * 规格
     */
    private String goodsFullSpecs;
    /**
     * 图片
     */
    private String imageSrc = "";
    /**
     * 计量单位
     */
    private String unitName = "";

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "OrdersGiftVo{" +
                "goodsId=" + goodsId +
                ", ordersId=" + ordersId +
                ", commonId=" + commonId +
                ", giftNum=" + giftNum +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
