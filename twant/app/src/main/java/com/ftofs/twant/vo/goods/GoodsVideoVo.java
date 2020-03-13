package com.ftofs.twant.vo.goods;

import com.ftofs.twant.vo.store.StoreVo;

import java.util.List;

/**
 * @author liusf
 * @create 2019/1/22 14:15
 * @description 產品视频视图类
 */
public class GoodsVideoVo {
    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 店铺信息
     */
    private StoreVo storeVo;

    /**
     * 產品列表
     */
    private List<GoodsCommonVo> goodsCommonList;

    /**
     * 播放次数
     */
    private long playTimes = 0;

    /**
     * 讚想數量
     */
    private long likeCount = 0;

    /**
     * 收藏數量
     */
    private long favoriteCount = 0;

    /**
     * 更新時間
     */
    private String updateTime = "";

    /**
     * 距上次更新秒數
     */
    private long second = 0;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public List<GoodsCommonVo> getGoodsCommonList() {
        return goodsCommonList;
    }

    public void setGoodsCommonList(List<GoodsCommonVo> goodsCommonList) {
        this.goodsCommonList = goodsCommonList;
    }

    public long getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(long playTimes) {
        this.playTimes = playTimes;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }
}
