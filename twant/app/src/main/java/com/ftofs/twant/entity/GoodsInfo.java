package com.ftofs.twant.entity;

import com.ftofs.twant.domain.goods.GoodsImage;

/**
 * 產品的各種SKU的信息
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
    public int goodsStorage;  // 產品庫存
    public int reserveStorage;  // 預留庫存
    public int limitAmount;   // 每人限購多少
    public String unitName;
    public String goodsName;
    public Boolean showSendBtn;

    /**
     * 獲取最終庫存(產品的庫存減去預留庫存)
     * @return
     */
    public int getFinalStorage() {
        int finalStorage = goodsStorage - reserveStorage;
        if (finalStorage < 0) {
            finalStorage = 0;
        }

        return finalStorage;
    }

    /**
     * 獲取用戶可購買的數量(考慮限購)
     * @return
     */
    public int getAvailableStorage() {
        int finalStorage = getFinalStorage();
        // 如果沒有限購，直接返回最終庫存
        if (limitAmount == 0) {
            return finalStorage;
        }

        // 如果有限購，返回兩者較小的值
        return Math.min(limitAmount, finalStorage);
    }

    public GoodsImage toGoodsImage() {
        GoodsImage goodsImage = new GoodsImage();
        goodsImage.setImageSrc(imageSrc);
        goodsImage.setImageId(goodsId);
        return goodsImage;
    }
}
