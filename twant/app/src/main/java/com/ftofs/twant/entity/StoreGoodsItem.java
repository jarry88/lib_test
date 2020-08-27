package com.ftofs.twant.entity;

import com.ftofs.twant.util.StringUtil;

import cn.snailpad.easyjson.EasyJSONObject;

public class StoreGoodsItem {
    public int commonId;
    public String imageSrc;
    public String goodsName;
    public String jingle;
    public double price;
    public int goodsModel;

    public static StoreGoodsItem parse(EasyJSONObject easyJSONObject)throws Exception {
        StoreGoodsItem storeGoodsItem = new StoreGoodsItem();
        storeGoodsItem.commonId = easyJSONObject.getInt("commonId");
        storeGoodsItem.imageSrc = easyJSONObject.getSafeString("imageSrc");
        storeGoodsItem.goodsName = easyJSONObject.getSafeString("goodsName");
        storeGoodsItem.jingle = easyJSONObject.getSafeString("jingle");
        storeGoodsItem.goodsModel = StringUtil.safeModel(easyJSONObject);
        return storeGoodsItem;
    }
}
