package com.ftofs.twant.entity.cart;

import java.util.ArrayList;
import java.util.List;

public class SpuStatus extends BaseStatus {
    public StoreStatus parent;
    public List<SkuStatus> skuStatusList = new ArrayList<>();
    private int storeId;
    private String storeName;
    private int goodsId; // Sku Id
    private int cartId;
    private int count;  // 數量
    private double price;  // 價錢
    private int limitBuy; //限購狀態
    private boolean isCrossBorder; // 是否支持跨境購

    @Override
    public void changeCheckStatus(boolean checked, int phrase) {
        super.changeCheckStatus(checked, phrase);

        if (phrase == PHRASE_CAPTURE) {
//            for (SkuStatus skuStatus : skuStatusList) {
//                skuStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
//            }
        } else if (phrase == PHRASE_TARGET) {
            // 狀態向下傳播
//            for (SkuStatus skuStatus : skuStatusList) {
//                skuStatus.changeCheckStatus(checked, PHRASE_CAPTURE);
//            }
            // 狀態向上傳播
            parent.changeCheckStatus(checked, PHRASE_BUBBLE);
        } else if (phrase == PHRASE_BUBBLE) {
            // 如果是冒泡階段，要所有子項目都選中了，才選中自己
            boolean allChecked = true;
            for (SkuStatus skuStatus : skuStatusList) {
                if (!skuStatus.isChecked()) {
                    allChecked = false;
                }
            }

            super.changeCheckStatus(allChecked, PHRASE_BUBBLE);
            parent.changeCheckStatus(allChecked, PHRASE_BUBBLE);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setLimitState(int limitBuy) {
        this.limitBuy = limitBuy;
    }

    public boolean isCrossBorder() {
        return isCrossBorder;
    }

    public void setCrossBorder(boolean crossBorder) {
        isCrossBorder = crossBorder;
    }
}
