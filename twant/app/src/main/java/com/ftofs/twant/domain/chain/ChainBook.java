package com.ftofs.twant.domain.chain;

public class ChainBook {
    /**
     * 主键
     */
    private int chainBookId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 商品(sku)ID
     */
    private int goodsId = 0;

    /**
     * 购买数量
     */
    private int buyNum;

    /**
     * 商品(spu)ID
     */
    private int commonId;

    /**
     * 门店商品ID
     */
    private int chainGoodsId;

    /**
     * 门店ID
     */
    private String chainName;

    /**
     * 门店ID
     */
    private int chainId;

    /**
     * 商品服务
     */
    private String goodsServices;

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getChainBookId() {
        return chainBookId;
    }

    public void setChainBookId(int chainBookId) {
        this.chainBookId = chainBookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getChainGoodsId() {
        return chainGoodsId;
    }

    public void setChainGoodsId(int chainGoodsId) {
        this.chainGoodsId = chainGoodsId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getGoodsServices() {
        return goodsServices;
    }

    public void setGoodsServices(String goodsServices) {
        this.goodsServices = goodsServices;
    }
}
