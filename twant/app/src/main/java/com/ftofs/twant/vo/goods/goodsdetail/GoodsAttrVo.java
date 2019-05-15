package com.ftofs.twant.vo.goods.goodsdetail;

import com.ftofs.twant.domain.goods.Attribute;
import com.ftofs.twant.domain.goods.AttributeValue;
import com.ftofs.twant.domain.goods.Custom;
import com.ftofs.twant.domain.goods.GoodsCustom;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 *  商品属性实体，商品详情显示使用
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:05
 */
public class GoodsAttrVo {
    private String name;
    private String value;

    public GoodsAttrVo() {
    }

    public GoodsAttrVo(Attribute attribute, AttributeValue attributeValue) {
        this.name = attribute.getAttributeName();
        this.value = attributeValue.getAttributeValueName();
    }

    public GoodsAttrVo(Custom custom, GoodsCustom goodsCustom) {
        this.name = custom.getCustomName();
        this.value = goodsCustom.getCustomValue();
    }

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
