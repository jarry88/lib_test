package com.ftofs.twant.entity;

public class SkuSpecViewItem {
    public static final int STATUS_LEFT = 1; // 在左邊的Item，文字右對齊
    public static final int STATUS_CENTER = 2; // 中間的項目，文字居中對齊
    public static final int STATUS_RIGHT = 3; // 在右邊的Item，文字左對齊

    public String goodsFullSpecs;
    public int status;

    public SkuSpecViewItem(String goodsFullSpecs, int status) {
        this.goodsFullSpecs = goodsFullSpecs;
        this.status = status;
    }
}
