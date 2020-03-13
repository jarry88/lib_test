package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class SearchItem implements MultiItemEntity {
    public static final int CATEGORY_ID_NEW = -1;  // 最新產品
    public static final int CATEGORY_ID_HOT = -2;  // 商店熱賣

    // 固有的分類：最新產品和商店熱賣下面的
    public static final int ITEM_TYPE_CATEGORY = 1;
    public static final int ITEM_TYPE_GOODS = 2;

    // 跳轉到分類
    public static final int ACTION_GOTO_CATEGORY = 1;
    // 跳轉到產品
    public static final int ACTION_GOTO_GOODS = 2;


    public SearchItem(int itemType, int id, String name) {
        this.itemType = itemType;
        this.id = id;
        this.name = name;
    }

    public int itemType;
    /**
     * 如果itemType是category,表示分類Id
     * 如果itemType是goods，表示產品Id
     */
    public int id;
    public String name;

    public int storeId;
    public int action;

    @Override
    public int getItemType() {
        return itemType;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("itemType[%d], id[%d], name[%s], storeId[%d], action[%d]",
                itemType, id, name, storeId, action);
    }
}
