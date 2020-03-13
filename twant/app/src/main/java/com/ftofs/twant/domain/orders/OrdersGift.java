package com.ftofs.twant.domain.orders;

import java.io.Serializable;

public class OrdersGift implements Serializable {
    /**
     * 订单產品编号
     * 主键、自增
     */
    private int ordersGiftId;

    /**
     * 订单產品表主键，只有產品级赠品时，该值才>0
     */
    private int ordersGoodsId = 0;

    /**
     * 赠品编号
     */
    private int giftId;

    /**
     * 订单ID
     */
    private int ordersId;

    /**
     * 產品Id
     */
    private int goodsId;

    /**
     * 產品SPU
     */
    private int commonId;

    /**
     * 赠品数量
     */
    private int giftNum;

    /**
     * 赠品类型，1满优惠赠品、2產品赠品
     */
    private int giftType;

    /**
     * 项目编号，如满优惠编号、主產品编号
     */
    private int itemId;

    /**
     * 项目编号，记录產品赠品的主產品SPU
     */
    private int itemCommonId;

    /**
     * 產品名称
     */
    private String goodsName;

    /**
     * 计量单位
     */
    private String unitName;

    /**
     * 完整规格
     * 例“颜色：红色；尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 图片路径
     */
    private String imageSrc = "";

    /**
     * 產品货号
     */
    private String goodsSerial;

    public int getOrdersGiftId() {
        return ordersGiftId;
    }

    public void setOrdersGiftId(int ordersGiftId) {
        this.ordersGiftId = ordersGiftId;
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
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

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemCommonId() {
        return itemCommonId;
    }

    public void setItemCommonId(int itemCommonId) {
        this.itemCommonId = itemCommonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    @Override
    public String toString() {
        return "OrdersGift{" +
                "ordersGiftId=" + ordersGiftId +
                ", ordersGoodsId=" + ordersGoodsId +
                ", giftId=" + giftId +
                ", ordersId=" + ordersId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", giftNum=" + giftNum +
                ", giftType=" + giftType +
                ", itemId=" + itemId +
                ", itemCommonId=" + itemCommonId +
                ", goodsName='" + goodsName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
