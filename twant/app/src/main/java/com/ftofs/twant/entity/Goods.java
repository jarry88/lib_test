package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

/**
 * 商品數據結構
 * @author zwm
 */
public class Goods implements MultiItemEntity {

    public Goods(int id, String imageUrl, String name, String jingle, double price) {
        itemType = Constant.ITEM_TYPE_NORMAL;

        this.id = id;
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

    @Override
    public int getItemType() {
        return itemType;
    }
}
