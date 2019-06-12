package com.ftofs.twant.entity;

import java.util.List;

/**
 * 商鋪搜索结果项
 * @author zwm
 */
public class StoreSearchItem {
    public StoreSearchItem(int storeId, String storeAvatarUrl, String storeName, String mainBusiness, String storeFigureImage,
                           float distance, String shopDay, int likeCount, int goodsCommonCount, List<String> goodsImageList) {
        this.storeId = storeId;
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeName = storeName;
        this.mainBusiness = mainBusiness;
        this.storeFigureImage = storeFigureImage;
        this.distance = distance;
        this.shopDay = shopDay;
        this.likeCount = likeCount;
        this.goodsCommonCount = goodsCommonCount;
        this.goodsImageList = goodsImageList;
    }

    public int storeId;
    public String storeAvatarUrl;
    public String storeName;
    public String mainBusiness;
    public String storeFigureImage;
    public float distance;
    public String shopDay;
    /**
     * 點贊數量
     */
    public int likeCount;
    public int goodsCommonCount;
    /**
     * 店鋪的前3個商品的照片
     */
    public List<String> goodsImageList;
}
