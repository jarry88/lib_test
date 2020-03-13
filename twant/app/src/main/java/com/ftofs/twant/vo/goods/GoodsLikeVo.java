package com.ftofs.twant.vo.goods;

import com.ftofs.twant.vo.store.StoreVo;

import java.math.BigDecimal;

/**
 * @author liusf
 * @create 2018/11/22 10:46
 * @description 猜你喜歡產品數據
 */
public class GoodsLikeVo {
    /**
     * 產品SPU編號
     */
    private int commonId;

    /**
     * 產品名稱
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
     * 產品圖片
     */
    private String imageUrl;

    /**
     * 產品所在商店
     */
    private StoreVo storeVo;

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

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }
}
