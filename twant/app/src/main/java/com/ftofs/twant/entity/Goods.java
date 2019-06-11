package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 商品數據結構
 * @author zwm
 */
public class Goods implements MultiItemEntity {
    /**
     * 正常的商品Item
     */
    public static final int ITEM_TYPE_NORMAL = 1;
    /**
     * 為了防止底部工具欄遮擋的Item
     */
    public static final int ITEM_TYPE_PADDING = 2;

    public Goods(int id, String imageUrl, String name, String jingle, double price) {
        itemType = ITEM_TYPE_NORMAL;

        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.jingle = jingle;
        this.price = price;
    }

    public Goods() {
        itemType = ITEM_TYPE_PADDING;
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
