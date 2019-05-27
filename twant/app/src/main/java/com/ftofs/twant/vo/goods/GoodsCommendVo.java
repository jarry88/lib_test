package com.ftofs.twant.vo.goods;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 推荐商品
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:06
 */
public class GoodsCommendVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品图
     */
    private String imageSrc;
    
    private Integer commonId;
    
    private String goodsFullSpecs;

	public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public Integer getCommonId() {
		return commonId;
	}

	public void setCommonId(Integer commonId) {
		this.commonId = commonId;
	}

	public String getGoodsFullSpecs() {
		return goodsFullSpecs;
	}

	public void setGoodsFullSpecs(String goodsFullSpecs) {
		this.goodsFullSpecs = goodsFullSpecs;
	}

	@Override
	public String toString() {
		return "GoodsCommendVo [goodsId=" + goodsId + ", goodsName=" + goodsName + ", goodsPrice=" + goodsPrice
				+ ", imageSrc=" + imageSrc + ", commonId=" + commonId + ", goodsFullSpecs=" + goodsFullSpecs + "]";
	}

}
