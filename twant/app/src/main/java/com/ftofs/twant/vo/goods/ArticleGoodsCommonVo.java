package com.ftofs.twant.vo.goods;

import java.math.BigDecimal;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 想要帖中的產品Vo
 *
 * @author cj
 * Created 2017-10-24 下午 2:10
 */
public class ArticleGoodsCommonVo {
    /**
     * 產品SPU编号
     */
    private int commonId;
    /**
     * 產品名称
     */
    private String goodsName;
    /**
     * 產品卖点
     */
    private String jingle;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName = "";
    /**
     * 產品最低价
     */
    private BigDecimal webPriceMin;

    /**
     * 產品最低价
     */
    private BigDecimal appPriceMin;

    /**
     * 產品最低价
     */
    private BigDecimal wechatPriceMin;

    /**
     * 图片路径
     */
    private String imageSrc;

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

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getWebPriceMin() {
        return webPriceMin;
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "ArticleGoodsCommonVo{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", jingle='" + jingle + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", webPriceMin=" + webPriceMin +
                ", appPriceMin=" + appPriceMin +
                ", wechatPriceMin=" + wechatPriceMin +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
