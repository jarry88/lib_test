package com.ftofs.twant.vo.goods.goodsdetail;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 *  產品属性实体，產品详情显示使用
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:05
 */
public class GoodsAttrVo {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GoodsAttrVo{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
