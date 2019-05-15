package com.ftofs.twant.domain.goods;

public class GoodsVideoPlay {
    /**
     * 主键
     */
    private int goodsVideoId;

    /**
     * 视频链接
     */
    private String goodsVideoUrl;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 视频播放次数
     */
    private long videoPlayTimes = 0;

    public int getGoodsVideoId() {
        return goodsVideoId;
    }

    public void setGoodsVideoId(int goodsVideoId) {
        this.goodsVideoId = goodsVideoId;
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

    public long getVideoPlayTimes() {
        return videoPlayTimes;
    }

    public void setVideoPlayTimes(long videoPlayTimes) {
        this.videoPlayTimes = videoPlayTimes;
    }
}
