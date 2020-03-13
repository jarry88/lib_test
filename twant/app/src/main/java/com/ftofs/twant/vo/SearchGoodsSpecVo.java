package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品规格实体，用于搜索引擎建立索引
 *
 * @author dqw
 * Created 2017/6/1 11:56
 */
public class SearchGoodsSpecVo {
    /**
     * 產品sku编号
     */
    private int goodsId;
    /**
     * 產品sku规格
     */
    private String spec;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "SearchGoodsSpecVo{" +
                "goodsId=" + goodsId +
                ", spec='" + spec + '\'' +
                '}';
    }
}

