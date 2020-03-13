package com.ftofs.twant.vo.buy.freight;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 简化版的產品sku Vo
 * 
 * @author hbj
 * Created 2017/4/13 14:43
 */
public class BuyGoodsItemLiteVo {
    /**
     * 產品名称
     */
    private String goodsName;
    /**
     * 產品规格
     */
    private String goodsFullSpecs;
    /**
     * 图片路径
     */
    private String imageSrc;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "BuyGoodsItemLiteVo{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
