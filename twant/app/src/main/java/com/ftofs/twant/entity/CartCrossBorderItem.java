package com.ftofs.twant.entity;

/**
 * 購物車跨境商品項
 * @author zwm
 */
public class CartCrossBorderItem {
    public CartCrossBorderItem(boolean selected, boolean isCrossBorder, int storeId, String storeName, int productCount) {
        this.selected = selected;
        this.isCrossBorder = isCrossBorder;
        this.storeName = storeName;
        this.productCount = productCount;
    }

    public boolean selected; // 是否選中

    public boolean isCrossBorder; // 是否為跨境購
    public String storeName; // 店鋪名稱
    public int productCount; // 產品數
}
