package com.ftofs.twant.entity;

/**
 * 商鋪搜索结果项
 * @author zwm
 */
public class StoreSearchItem {
    public StoreSearchItem(String storeAvatarUrl, String storeName, String storeFigureImage) {
        this.storeAvatarUrl = storeAvatarUrl;
        this.storeName = storeName;
        this.storeFigureImage = storeFigureImage;
    }

    public String storeAvatarUrl;
    public String storeName;
    public String storeFigureImage;
}
