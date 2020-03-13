package com.ftofs.twant.domain.goods;

public class GoodsVideoLike {
    private int goodsVideoLikeId;

    /**
     * 視頻地址
     */
    private String goodsVideoUrl;

    /**
     * 商店ID
     */
    private int storeId;

    /**
     * 城友名稱
     */
    private String memberName;

    public int getGoodsVideoLikeId() {
        return goodsVideoLikeId;
    }

    public void setGoodsVideoLikeId(int goodsVideoLikeId) {
        this.goodsVideoLikeId = goodsVideoLikeId;
    }

    public String getGoodsVideoUrl() {
        return goodsVideoUrl;
    }

    public void setGoodsVideoUrl(String goodsVideoUrl) {
        this.goodsVideoUrl = goodsVideoUrl;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

}
