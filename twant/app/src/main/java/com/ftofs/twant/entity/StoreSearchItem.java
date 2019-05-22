package com.ftofs.twant.entity;

/**
 * 商鋪搜索结果项
 * @author zwm
 */
public class StoreSearchItem {
    public StoreSearchItem(int storeId, String storeAvatarUrl, String storeName, String storeFigureImage,
                           float distance, String shopDay, int likeCount, int goodsCommonCount) {
        this.storeId = storeId;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeName = storeName;
        this.storeFigureImage = storeFigureImage;
        this.distance = distance;
        this.shopDay = shopDay;
        this.likeCount = likeCount;
        this.goodsCommonCount = goodsCommonCount;
    }

    public int storeId;
    public String storeAvatarUrl;
    public String storeName;
    public String storeFigureImage;
    public float distance;
    public String shopDay;
    /**
     * 點贊數量
     */
    public int likeCount;
    public int goodsCommonCount;
}
