package com.ftofs.twant.vo.goods;

import java.math.BigDecimal;

/**
 * 接口专题项目產品数据Vo
 * Created by dqw on 2016/04/13.
 */
public class GoodsLiteVo {
    /**
     * 產品编号
     */
    private int commonId;
    /**
     * 產品名称
     */
    private String goodsName;
    /**
     * 產品APP价格，等同于AppPrice用于后台显示
     */
    private BigDecimal goodsPrice;
    /**
     * 產品APP价格
     */
    private BigDecimal appPriceMin;
    /**
     * 產品微信价格
     */
    private BigDecimal wechatPriceMin;
    /**
     * 產品图片Url
     */
    private String imageUrl;

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

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
    }

    public BigDecimal getWechatPriceMin() {
        return wechatPriceMin;
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return "GoodsLiteVo{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", appPriceMin=" + appPriceMin +
                ", wechatPriceMin=" + wechatPriceMin +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

