package com.ftofs.twant.vo.buy;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 购买时使用到的SPU Vo
 *
 * @author hbj
 * Created 2017/4/13 14:41
 */
public class BuyGoodsSpuVo {
    /**
     * sku 列表
     */
    private List<BuyGoodsItemVo> buyGoodsItemVoList;
    /**
     * spu 名称
     */
    private String goodsName;
    /**
     * spu id
     */
    private Integer commonId;
    /**
     * 图片路径
     */
    private String imageSrc;

    public List<BuyGoodsItemVo> getBuyGoodsItemVoList() {
        return buyGoodsItemVoList;
    }

    public void setBuyGoodsItemVoList(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        this.buyGoodsItemVoList = buyGoodsItemVoList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getCommonId() {
        return commonId;
    }

    public void setCommonId(Integer commonId) {
        this.commonId = commonId;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "BuyGoodsSpuVo{" +
                "buyGoodsItemVoList=" + buyGoodsItemVoList +
                ", goodsName='" + goodsName + '\'' +
                ", commonId=" + commonId +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
