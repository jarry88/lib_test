package com.ftofs.twant.domain.show;

import java.io.Serializable;

public class ShowOrdersItem implements Serializable {
    /**
     * 晒宝 item id
     * 主键、自增
     */
    private Integer showOrdersItemId;

    /**
     * 晒宝id
     */
    private int showOrdersId;

    /**
     * 标题图片
     */
    private String image;

    /**
     * 文字
     */
    private String text;

    /**
     * 文字在图片上面
     */
    private int textUp = 1;

    /**
     * 商品 goods commond id
     */
    private int goodsCommonId;

    /**
     * 推广商品id
     */
    private int distributorGoodsId;

    /**
     * 商品价格
     */
    private String goodsPrice = "";

    /**
     * 排序
     */
    private int sort;

    /**
     * 类型 1.图文 2.已购买订单 3。推广商品
     */
    private int type = 1;

    /**
     * 标题图片完整url
     */
    private String imageSrc = "";

    public Integer getShowOrdersItemId() {
        return showOrdersItemId;
    }

    public void setShowOrdersItemId(Integer showOrdersItemId) {
        this.showOrdersItemId = showOrdersItemId;
    }

    public int getShowOrdersId() {
        return showOrdersId;
    }

    public void setShowOrdersId(int showOrdersId) {
        this.showOrdersId = showOrdersId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextUp() {
        return textUp;
    }

    public void setTextUp(int textUp) {
        this.textUp = textUp;
    }

    public int getGoodsCommonId() {
        return goodsCommonId;
    }

    public void setGoodsCommonId(int goodsCommonId) {
        this.goodsCommonId = goodsCommonId;
    }

    public int getDistributorGoodsId() {
        return distributorGoodsId;
    }

    public void setDistributorGoodsId(int distributorGoodsId) {
        this.distributorGoodsId = distributorGoodsId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageSrc() {
        return imageSrc ;
    }

    @Override
    public String toString() {
        return "ShowOrdersItem{" +
                "showOrdersItemId=" + showOrdersItemId +
                ", showOrdersId=" + showOrdersId +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                ", textUp=" + textUp +
                ", goodsCommonId=" + goodsCommonId +
                ", distributorGoodsId=" + distributorGoodsId +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", sort=" + sort +
                ", type=" + type +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
