package com.ftofs.twant.entity.order;

import com.ftofs.twant.entity.Goods;

import java.util.List;

/**
 * 首頁最新想要店Item
 * @author zwm
 */
public class NewArrivalsStoreItem {
    public int storeId;
    public String storeClass;
    public String storeName;
    public String storeFigure;
    // 前三個商品展示
    public List<Goods> goodsList;

    public NewArrivalsStoreItem(int storeId, String storeClass, String storeName, String storeFigure, List<Goods> goodsList) {
        this.storeId = storeId;
        this.storeClass = storeClass;
        this.storeName = storeName;
        this.storeFigure = storeFigure;
        this.goodsList = goodsList;
    }
}
