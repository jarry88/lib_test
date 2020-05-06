package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 產品數據結構
 * @author zwm
 */
public class Goods implements MultiItemEntity {

    public Goods(int commonId, String imageUrl, String name, String jingle, double price) {
        itemType = Constant.ITEM_TYPE_NORMAL;

        this.id = commonId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.jingle = jingle;
        this.price = price;
    }

    public Goods() {
        itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
    }

    private int itemType;
    public int id;
    public String imageUrl;
    public String name;
    public String jingle;
    public double price;

    public static Goods parse(EasyJSONObject goods) throws Exception {

        String goodsName = goods.getSafeString("goodsName");

        String goodsImage = goods.getSafeString("goodsImage");
        int commonId = goods.getInt("commonId");
        String jingle = "";
        if (goods.exists("jingle")) {
          jingle= goods.getSafeString("jingle");
        }
        double price;
        int appUsable = goods.getInt("appUsable");
        if (appUsable > 0) {
            price =  goods.getDouble("appPriceMin");
        } else {
            price =  goods.getDouble("batchPrice0");
        }

        double batchPrice0 =  goods.getDouble("batchPrice0");
//        double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");
        return new Goods(commonId,goodsImage,goodsName,jingle,price);
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
