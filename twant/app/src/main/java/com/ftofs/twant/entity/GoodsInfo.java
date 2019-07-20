package com.ftofs.twant.entity;

/**
 * 商品的各種SKU的信息
 * @author zwm
 */
public class GoodsInfo {
    public int goodsId;
    public int commonId;
    public String goodsFullSpecs;
    public String specValueIds;
    public float goodsPrice0; // 原價
    public float price;  // 最終價，如果沒有打折，最終價與原價相同
    public String imageSrc;
    public int goodsStorage;  // 商品庫存
    public int limitAmount;   // 每人限購多少
    public String unitName;
}
