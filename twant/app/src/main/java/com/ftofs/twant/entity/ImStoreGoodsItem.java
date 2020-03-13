package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * IM聊天產品Item
 * @author
 */
public class ImStoreGoodsItem implements MultiItemEntity {
    /**
     * 頭部分類
     */
    public static final int ITEM_TYPE_HEADER = 1;
    /**
     * 產品Item
     */
    public static final int ITEM_TYPE_ITEM = 2;


    public ImStoreGoodsItem(int itemType) {
        this.itemType = itemType;
    }

    public int itemType;

    public String goodsName; // 如果是header類型，這里表示分類名稱
    public String goodsImg;
    public int commonId;


    @Override
    public int getItemType() {
        return itemType;
    }
}
