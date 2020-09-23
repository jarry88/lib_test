package com.ftofs.twant.entity;

public class Store {
    public int storeId;
    public String storeName;
    public String storeAvatar;
    public String storeFigureImage;

    public Store() {
    }

    public Store(int storeId, String storeName, String storeAvatar, String storeFigureImage) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAvatar = storeAvatar;
        this.storeFigureImage = storeFigureImage;
    }
}
