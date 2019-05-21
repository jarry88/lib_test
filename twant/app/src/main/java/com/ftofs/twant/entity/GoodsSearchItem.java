package com.ftofs.twant.entity;

/**
 * 商品搜索结果项
 * @author zwm
 */
public class GoodsSearchItem {
    public GoodsSearchItem(String imageSrc, String storeAvatarUrl, String storeName, int commonId, String goodsName, String jingle, float price) {
        this.imageSrc = imageSrc;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeName = storeName;
        this.commonId = commonId;
        this.goodsName = goodsName;
        this.jingle = jingle;
        this.price = price;
    }

    public String imageSrc;
    public String storeAvatarUrl;
    public String storeName;
    public int commonId;
    public String goodsName;
    public String jingle;
    public float price;
}
