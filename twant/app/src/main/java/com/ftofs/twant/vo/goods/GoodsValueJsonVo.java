package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品编号与规格组合
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:12
 */
public class GoodsValueJsonVo {

    private int goodsId;
    private String specValueIds;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(String specValueIds) {
        this.specValueIds = specValueIds;
    }

    @Override
    public String toString() {
        return "GoodsSpecValueJsonVo{" +
                "goodsId=" + goodsId +
                ", specValueIds='" + specValueIds + '\'' +
                '}';
    }
}
