package com.ftofs.twant.vo.store;

import java.util.List;

/**
 * @Description: 首頁商店推薦
 * @Auther: yangjian
 * @Date: 2018/9/27 10:34
 */
public class StoreRecommendVo {
    /**
     * 最新
     */
    private List<StoreGoodsVo> news;
    /**
     * 促銷
     */
    private List<StoreGoodsVo> promotion;
    /**
     * 推薦
     */
    private List<StoreGoodsVo> recommend;
    /**
     * 熱銷
     */
    private List<StoreGoodsVo> hotsell;

    public List<StoreGoodsVo> getNews() {
        return news;
    }

    public void setNews(List<StoreGoodsVo> news) {
        this.news = news;
    }

    public List<StoreGoodsVo> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<StoreGoodsVo> promotion) {
        this.promotion = promotion;
    }

    public List<StoreGoodsVo> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<StoreGoodsVo> recommend) {
        this.recommend = recommend;
    }

    public List<StoreGoodsVo> getHotsell() {
        return hotsell;
    }

    public void setHotsell(List<StoreGoodsVo> hotsell) {
        this.hotsell = hotsell;
    }

    @Override
    public String toString() {
        return "StoreRecommendVo{" +
                "news=" + news +
                ", promotion=" + promotion +
                ", recommend=" + recommend +
                ", hotsell=" + hotsell +
                '}';
    }
}
