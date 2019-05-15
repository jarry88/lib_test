package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.store.Store;

import java.math.BigDecimal;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 文章中的商品Vo
 *
 * @author cj
 * Created 2017-10-24 下午 2:10
 */
public class ArticleGoodsCommonVo {
    /**
     * 商品SPU编号
     */
    private int commonId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品卖点
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
     * 商品最低价
     */
    private BigDecimal webPriceMin;

    /**
     * 商品最低价
     */
    private BigDecimal appPriceMin;

    /**
     * 商品最低价
     */
    private BigDecimal wechatPriceMin;

    /**
     * 图片路径
     */
    private String imageSrc;

    public ArticleGoodsCommonVo(GoodsCommon goodsCommon, Store store) {
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.jingle = goodsCommon.getJingle();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.imageSrc = goodsCommon.getImageSrc();
        this.webPriceMin = goodsCommon.getWebPriceMin();
        this.appPriceMin = goodsCommon.getAppPriceMin();
        this.wechatPriceMin = goodsCommon.getWechatPriceMin();
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
