package com.ftofs.twant.domain.chain;

import java.math.BigDecimal;

public class ChainGoods {
    /**
     * 门店商品Id
     */
    private int chainGoodsId;

    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SPU
     */
    private int commonId;

    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /**
     * 库存
     */
    private int goodsStorage;

    /**
     * 销售数量
     */
    private int goodsSaleNum = 0;

    /**
     * 商品更新时间（只有在门店店员更新门店商品时需要更新时间。）
     */
    private String updateTime;

    public int getChainGoodsId() {
        return chainGoodsId;
    }

    public void setChainGoodsId(int chainGoodsId) {
        this.chainGoodsId = chainGoodsId;
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

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ChainGoods{" +
                "chainGoodsId=" + chainGoodsId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsStorage=" + goodsStorage +
                ", goodsSaleNum=" + goodsSaleNum +
                ", updateTime=" + updateTime +
                '}';
    }
}
