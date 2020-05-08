package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 產品數據結構
 * @author zwm
 */
public class Goods implements MultiItemEntity {

    public boolean showDiscount;
    private double promotionDiscountRate;
    private double batchPrice0;

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

    public Goods(int itemType) {
        this.itemType = itemType;
    }

    public static Goods parse(EasyJSONObject goods) throws Exception {

        String goodsName = goods.getSafeString("goodsName");

        String goodsImage = goods.getSafeString("goodsImage");
        int commonId = goods.getInt("commonId");
        String jingle = "";
        if (goods.exists("jingle")) {
            jingle = goods.getSafeString("jingle");
        } else {
            jingle=goods.getSafeString("goodsFullSpecs");
        }
        double price;
        int appUsable = goods.getInt("appUsable");
        if (appUsable > 0) {
            price =  goods.getDouble("appPriceMin");
        } else {
            price =  goods.getDouble("batchPrice0");
        }

        Goods goods1=new Goods(commonId,goodsImage,goodsName,jingle,price);
        if (goods.exists("promotionDiscountRate")) {

            double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");
            if (appUsable > 0) {
                goods1.showDiscount = true;
            }
            double batchPrice0 =  goods.getDouble("batchPrice0");
            goods1.promotionDiscountRate = promotionDiscountRate;
            goods1.batchPrice0 = batchPrice0;
//            SLog.info("%s",promotionDiscountRate);
        }
        return goods1 ;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public double getOriginal() {
        return batchPrice0;
    }

    public double getDiscount() {
        return promotionDiscountRate;
    }
}
