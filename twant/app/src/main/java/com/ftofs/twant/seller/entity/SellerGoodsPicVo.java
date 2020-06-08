package com.ftofs.twant.seller.entity;

public class SellerGoodsPicVo {
    public int colorId;
    public String colorName;
    public String imageName;  // 遠端圖片路徑（相對路徑）
    public String absolutePath;  // 本地圖片路徑（值不為空,表示待上傳）
    public int imageSort;
    public int isDefault;
}
