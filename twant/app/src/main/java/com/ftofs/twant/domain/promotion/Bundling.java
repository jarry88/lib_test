package com.ftofs.twant.domain.promotion;

import com.ftofs.twant.domain.goods.GoodsCommon;

import java.io.Serializable;
import java.util.List;

public class Bundling implements Serializable {
    /**
     * 优惠套装编号
     */
    private int bundlingId;

    /**
     * 优惠套装名称
     */
    private String bundlingName;

    /**
     * 优惠套装标题
     */
    private String bundlingTitle;

    /**
     * 优惠套装标题，用于外部显示
     */
    private String bundlingTileFinal;

    /**
     * 优惠套装描述
     */
    private String bundlingExplain;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 推荐组合的SPU列表
     */
    private List<GoodsCommon> goodsCommonList;

    /**
     * 推荐组合產品列表
     */
    private List<BundlingGoods> bundlingGoodsList;

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public String getBundlingName() {
        return bundlingName;
    }

    public void setBundlingName(String bundlingName) {
        this.bundlingName = bundlingName;
    }

    public String getBundlingTitle() {
        return bundlingTitle;
    }

    public void setBundlingTitle(String bundlingTitle) {
        this.bundlingTitle = bundlingTitle;
    }

    public String getBundlingTileFinal() {
        return bundlingTitle;
    }

    public void setBundlingTileFinal(String bundlingTileFinal) {
        this.bundlingTileFinal = bundlingTileFinal;
    }

    public String getBundlingExplain() {
        return bundlingExplain;
    }

    public void setBundlingExplain(String bundlingExplain) {
        this.bundlingExplain = bundlingExplain;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<GoodsCommon> getGoodsCommonList() {
        return goodsCommonList;
    }

    public void setGoodsCommonList(List<GoodsCommon> goodsCommonList) {
        this.goodsCommonList = goodsCommonList;
    }

    public List<BundlingGoods> getBundlingGoodsList() {
        return bundlingGoodsList;
    }

    public void setBundlingGoodsList(List<BundlingGoods> bundlingGoodsList) {
        this.bundlingGoodsList = bundlingGoodsList;
    }

    @Override
    public String toString() {
        return "Bundling{" +
                "bundlingId=" + bundlingId +
                ", bundlingName='" + bundlingName + '\'' +
                ", bundlingTitle='" + bundlingTitle + '\'' +
                ", bundlingTileFinal='" + bundlingTileFinal + '\'' +
                ", bundlingExplain='" + bundlingExplain + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", storeId=" + storeId +
                ", goodsCommonList=" + goodsCommonList +
                ", bundlingGoodsList=" + bundlingGoodsList +
                '}';
    }
}
