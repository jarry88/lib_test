package com.ftofs.twant.entity;

/**
 * 每個Sku的瀏覽數據
 * @author zwm
 */
public class SkuGalleryItem {
    public int goodsId;  // 商品Id
    public String imageSrc; // 圖片
    public String goodsSpecString; // 規格
    public double price; // 價格
    public String specValueIds; // 各個規格Id

    public SkuGalleryItem(int goodsId, String imageSrc, String goodsSpecString, double price, String specValueIds) {
        this.goodsId = goodsId;
        this.imageSrc = imageSrc;
        this.goodsSpecString = goodsSpecString;
        this.price = price;
        this.specValueIds = specValueIds;
    }
}
