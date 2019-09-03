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
    public int reserveStorage;  // 預留庫存
    public int limitAmount;   // 每人限購多少
    public String unitName;

    /**
     * 計算最終庫存
     * @return
     */
    public int getFinalStorage() {
        // 商品的庫存需要減去預留庫存
        int finalStorage = goodsStorage - reserveStorage;
        if (finalStorage < 0) {
            finalStorage = 0;
        }

        // 如果沒有限購，直接返回最終庫存
        if (limitAmount == 0) {
            return finalStorage;
        }

        // 如果有限購，返回兩者較小的值
        return Math.min(limitAmount, finalStorage);
    }
}
