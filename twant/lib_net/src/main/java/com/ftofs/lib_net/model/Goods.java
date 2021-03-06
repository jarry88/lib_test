package com.ftofs.lib_net.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzp.lib_common.constant.Constant;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.utils.StringUtil;

import cn.snailpad.easyjson.EasyJSONObject;

import static com.gzp.lib_common.utils.StringUtilKt.safeModel;

/**
 * 產品數據結構
 * @author zwm
 */
public class Goods implements MultiItemEntity {

    public double appPriceMin;
    public boolean showDiscount;
    public int isVirtual;
    public int tariffEnable;
    private double promotionDiscountRate;
    private double batchPrice0;
    private int goodsStorage=1;
    public int goodsStatus=1;
    public int buyNum;
    public int appUsable;
    public int promotionType = Constant.PROMOTION_TYPE_NONE;
    public int goodsModal;

    private int itemType;
    public int id;
    public String imageUrl;
    public String name;
    public String jingle;
    public double price;

    //来自zone的信息
    public int commonId;
    public String goodsName;
    public String goodsImage;

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

    public Goods(int itemType) {
        this.itemType = itemType;
    }

    public static Goods parse(EasyJSONObject goods) throws Exception {
        SLog.info("goods[%s]",goods.toString());
        String goodsName = goods.getSafeString("goodsName");
        String goodsImage="";
        if (goods.exists("goodsImage")) {

            goodsImage = goods.getSafeString("goodsImage");
        } else if(goods.exists("imageSrc")){
            goodsImage = goods.getSafeString("imageSrc");
        }
        int commonId = goods.getInt("commonId");
        String jingle = "";
        if (goods.exists("jingle")) {
            jingle = goods.getSafeString("jingle");
        } else {
            jingle=goods.getSafeString("goodsFullSpecs");
        }
        double price=0,batchPrice0=0;
        int appUsable=0;
        if (goods.exists("batchPrice0")) {
            batchPrice0=goods.getDouble("batchPrice0");
        }
        if (goods.exists("appUsable")) {
            appUsable = goods.getInt("appUsable");
        } else if (goods.exists("goodsPrice")) {
            price = goods.getDouble("goodsPrice");
        }

        if (goods.exists("appPriceMin")) {
            price=goods.getDouble("appPriceMin");
        }




        Goods goods1=new Goods(commonId,goodsImage,goodsName,jingle,price);
        if (goods.exists("promotionType")) {
            goods1.promotionType = goods.getInt("promotionType");
        }

        if (appUsable > 0 && goods1.promotionType == Constant.PROMOTION_TYPE_TIME_LIMITED_DISCOUNT) {
            goods1.showDiscount = true;
            goods1.batchPrice0 = batchPrice0;
            goods1.appUsable = appUsable;
        }
        if (goods.exists("buyNum")) {
            goods1.buyNum = goods.getInt("buyNum");
        }
        if (goods.exists("promotionDiscountRate")) {
            double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");
            goods1.promotionDiscountRate = promotionDiscountRate;
//            SLog.info("%s",promotionDiscountRate);
        }
        if (goods.exists("goodsStorage")) {
            goods1.goodsStorage = goods.getInt("goodsStorage");
        }
        if (goods.exists("goodsStatus")) {
            goods1.goodsStatus = goods.getInt("goodsStatus");
        }
        if (goods.exists("isVirtual")) {
            goods1.isVirtual = goods.getInt("isVirtual");
        }
        if (goods.exists("tariffEnable")) {
            goods1.tariffEnable = goods.getInt("tariffEnable");
        }
        goods1.goodsModal = safeModel(goods);
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

    public boolean hasGoodsStorage() {
        return goodsStorage > 0;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }
}
