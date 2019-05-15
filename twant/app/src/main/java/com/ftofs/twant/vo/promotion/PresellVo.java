package com.ftofs.twant.vo.promotion;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 全款预售
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:20
 */
public class PresellVo {
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品价格
     */
    private BigDecimal price;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PresellVo{" +
                "goodsId=" + goodsId +
                ", price=" + price +
                '}';
    }
}
