package com.ftofs.twant.vo.advertorial;

import java.math.BigDecimal;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 文章中的图集vo
 * 
 * @author cj
 * Created 2017-9-29 下午 5:10
 */
public class AdvertorialArticleItemAlbumVo {
    /**
     * 类型
     */
    private int type ;
    /**
     * input 输入的地址
     */
    private String goodsUrl ="" ;
    /**
     * 图片名称
     */
    private String imageName  ="";
    /**
     * 商品commonId
     */
    private int commonId ;
    /**
     * 商品名称
     */
    private String goodsName = "" ;
    /**
     * 商品价格
     */
    private BigDecimal price=new BigDecimal(0)  ;
    /**
     * 分佣比例
     */
    private int commissionRate ;
    /**
     * 备注
     */
    private String desc  = "";
    /**
     * 图片地址
     */
    private String imageUrl ="";
    /**
     * 推广商品id
     */
    private int distributorGoodsId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDistributorGoodsId() {
        return distributorGoodsId;
    }

    public void setDistributorGoodsId(int distributorGoodsId) {
        this.distributorGoodsId = distributorGoodsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AdvertorialArticleAlbumVo{" +
                "type=" + type +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", imageName='" + imageName + '\'' +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", commissionRate=" + commissionRate +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", distributorGoodsId=" + distributorGoodsId +
                '}';
    }
}
