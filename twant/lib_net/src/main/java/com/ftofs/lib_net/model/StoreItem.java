package com.ftofs.lib_net.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.gzp.lib_common.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 店鋪item
 * @author zwm
 */
public class StoreItem implements MultiItemEntity {
    public int itemType;
    public int storeId;
    public String storeName;
    public String storeClass;
    public String storeFigureImage;
    public String storeAvatar;

    public List<Goods> goodsList;
    public List<Goods> zoneGoodsVoList;
    public String className;

    public StoreItem() {
        this.itemType = Constant.ITEM_TYPE_NORMAL;
        this.goodsList = new ArrayList<>();
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
