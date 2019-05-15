package com.ftofs.twant.vo.cart;

import java.util.ArrayList;
import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 购物车车SPU
 *
 * @author hbj
 * Created 2017/4/14 17:32
 */
public class CartSpuVo {
    /**
     * sku list
     */
    private List<CartItemVo> cartItemVoList = new ArrayList<>();
    /**
     * spu ID
     */
    private int commonId;
    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 销售模式
     */
    private int goodsModal;
    /**
     * 起购量0
     */
    private int batchNum0;
    /**
     * 起购量1
     */
    private int batchNum1;
    /**
     * 起购量2
     */
    private int batchNum2;
    /**
     * 起购量0  终点
     */
    private int batchNum0End = 0;
    /**
     * 起购量1  终点
     */
    private int batchNum1End = 0;
    /**
     * 商品状态
     */
    private int goodsStatus;
    /**
     * 购物车spu状态(有效/无效)
     * 商品下架 -> 无效
     * 商品正处于预定状态 -> 无效
     */
    private int isValid;
    /**
     * 购买数量,只适用于优惠套装的情况
     */
    private int buyNum;
    /**
     * 优惠套装ID
     */
    private int bundlingId = 0;
    /**
     * 门店ID
     */
    private int chainId;
    /**
     * spu ID，当商品为门店商品时，该值为真正的commonId[仅购物车列表使用]
     */
    private int realCommonId;
    /**
     * 商品类型，详见CartItemVo.goodsType
     */
    private int goodsType;

    public CartSpuVo(){}

    public List<CartItemVo> getCartItemVoList() {
        return cartItemVoList;
    }

    public void setCartItemVoList(List<CartItemVo> cartItemVoList) {
        this.cartItemVoList = cartItemVoList;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public int getBatchNum0() {
        return batchNum0;
    }

    public void setBatchNum0(int batchNum0) {
        this.batchNum0 = batchNum0;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum2() {
        return batchNum2;
    }

    public void setBatchNum2(int batchNum2) {
        this.batchNum2 = batchNum2;
    }

    public int getBatchNum0End() {
        return batchNum0End;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public int getBatchNum1End() {
        return batchNum1End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public int getRealCommonId() {
        return realCommonId;
    }

    public void setRealCommonId(int realCommonId) {
        this.realCommonId = realCommonId;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    /**
     * @return
     */
    public int getIsValid() {
        return this.isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    @Override
    public String toString() {
        return "CartSpuVo{" +
                "cartItemVoList=" + cartItemVoList +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsModal=" + goodsModal +
                ", batchNum0=" + batchNum0 +
                ", batchNum1=" + batchNum1 +
                ", batchNum2=" + batchNum2 +
                ", batchNum0End=" + batchNum0End +
                ", batchNum1End=" + batchNum1End +
                ", goodsStatus=" + goodsStatus +
                ", isValid=" + isValid +
                ", buyNum=" + buyNum +
                ", bundlingId=" + bundlingId +
                ", chainId=" + chainId +
                ", realCommonId=" + realCommonId +
                ", goodsType=" + goodsType +
                '}';
    }
}
