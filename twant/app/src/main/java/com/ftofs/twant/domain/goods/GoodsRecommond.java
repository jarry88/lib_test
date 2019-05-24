package com.ftofs.twant.domain.goods;

import java.io.Serializable;


public class GoodsRecommond implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int recommondId;

    /**
     * 最末级分类id
     */
    private int categoryId = 0;

    /**
     * 分类说明
     */
    private String categoryText = "";

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * spu
     */
    private int commonId = 0;

    /**
     * 商品名称
     */
    private String goodsName = "";

    /**
     * 图片名称
     */
    private String goodsImageName = "";

    /**
     * 商品分类编号
     */
    private int goodsCategoryId = 0;

    /**
     * 一级分类
     */
    private int goodsCategoryId1 = 0;

    /**
     * 二级分类
     */
    private int goodsCategoryId2 = 0;

    /**
     * 三级分类
     */
    private int goodsCategoryId3 = 0;

    /**
     * 图片路径
     */
    private String goodsImageSrc;

    public int getRecommondId() {
        return recommondId;
    }

    public void setRecommondId(int recommondId) {
        this.recommondId = recommondId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
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

    public String getGoodsImageName() {
        return goodsImageName;
    }

    public void setGoodsImageName(String goodsImageName) {
        this.goodsImageName = goodsImageName;
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

    public String getGoodsImageSrc() {
        return goodsImageName;
    }

    public void setImageSrc(String goodsImageSrc) {
        this.goodsImageSrc = goodsImageSrc;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "GoodsRecommond{" +
                "recommondId=" + recommondId +
                ", categoryId=" + categoryId +
                ", categoryText='" + categoryText + '\'' +
                ", addTime=" + addTime +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImageName='" + goodsImageName + '\'' +
                ", goodsCategoryId=" + goodsCategoryId +
                ", goodsCategoryId1=" + goodsCategoryId1 +
                ", goodsCategoryId2=" + goodsCategoryId2 +
                ", goodsCategoryId3=" + goodsCategoryId3 +
                ", goodsImageSrc='" + goodsImageSrc + '\'' +
                '}';
    }
}
