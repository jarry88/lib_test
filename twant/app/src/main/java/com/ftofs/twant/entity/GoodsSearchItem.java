package com.ftofs.twant.entity;

/**
 * 商品搜索结果项
 * @author zwm
 */
public class GoodsSearchItem {
    public GoodsSearchItem(String imageSrc, String storeAvatarUrl, int storeId, String storeName, int commonId,
                           String goodsName, String jingle, float price, String nationalFlag) {
        this.imageSrc = imageSrc;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeId = storeId;
        this.storeName = storeName;
        this.commonId = commonId;
        this.goodsName = goodsName;
        this.jingle = jingle;
        this.price = price;
        this.nationalFlag = nationalFlag;
    }

    public String imageSrc;
    public String storeAvatarUrl;
    public int storeId;
    public String storeName;
    public int commonId;
    public String goodsName;
    public String jingle;
    public float price;
    public String nationalFlag;
}
