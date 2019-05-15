package com.ftofs.twant.vo.goods.goodsdetail;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品编号与规格组合
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:05
 */
public class GoodsSpecValueJsonVo {

    private int goodsId;
    private List<Integer> specValueIds;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public List<Integer> getSpecValueIds() {
        return specValueIds;
    }

    public void setSpecValueIds(List<Integer> specValueIds) {
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
