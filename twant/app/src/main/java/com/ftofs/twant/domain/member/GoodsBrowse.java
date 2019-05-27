package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class GoodsBrowse implements Serializable {
    /**
     * 自增编码
     */
    private int browseId;

    /**
     * 商品SPU编号
     */
    private int commonId = 0;

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 浏览时间
     */
    private String addTime;

    /**
     * 商品分类ID
     */
    private int goodsCategoryId = 0;

    /**
     * 商品一级分类
     */
    private int goodsCategoryId1 = 0;

    /**
     * 商品二级分类
     */
    private int goodsCategoryId2 = 0;

    /**
     * 商品三级分类
     */
    private int goodsCategoryId3 = 0;

    public int getBrowseId() {
        return browseId;
    }

    public void setBrowseId(int browseId) {
        this.browseId = browseId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(int goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public int getGoodsCategoryId1() {
        return goodsCategoryId1;
    }

    public void setGoodsCategoryId1(int goodsCategoryId1) {
        this.goodsCategoryId1 = goodsCategoryId1;
    }

    public int getGoodsCategoryId2() {
        return goodsCategoryId2;
    }

    public void setGoodsCategoryId2(int goodsCategoryId2) {
        this.goodsCategoryId2 = goodsCategoryId2;
    }

    public int getGoodsCategoryId3() {
        return goodsCategoryId3;
    }

    public void setGoodsCategoryId3(int goodsCategoryId3) {
        this.goodsCategoryId3 = goodsCategoryId3;
    }

    @Override
    public String toString() {
        return "GoodsBrowse{" +
                "browseId=" + browseId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                ", addTime=" + addTime +
                ", goodsCategoryId=" + goodsCategoryId +
                ", goodsCategoryId1=" + goodsCategoryId1 +
                ", goodsCategoryId2=" + goodsCategoryId2 +
                ", goodsCategoryId3=" + goodsCategoryId3 +
                '}';
    }
}
