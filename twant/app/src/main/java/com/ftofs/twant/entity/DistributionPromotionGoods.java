package com.ftofs.twant.entity;

public class DistributionPromotionGoods {
    public boolean selected; // 是否選中

    /*
    "commonId": 3401,
                "goodsName": "06-16 CleanseBot除蟎殺菌機械人",
                "imageName": "image/57/4e/574ea0d43aeaf278e33dce66baf71969.jpg",
                "batchPrice0": 20.00,
                "batchPrice2": 0.10,
                "totalStorage": 2992,
                "storeName": null,
                "storeId": 20,
                "commissionType": 1,
                "commissionLevel1": 5.00,
                "commissionLevel2": 10.00,
                "commissionAmount": 0.00,
                "commissionAmount2": 0.00,
     */

    public int commonId;
    public String goodsName;
    public String imageName;
    public double batchPrice0;
    public double batchPrice2;
    public double goodsPrice;
    public int commissionType;  // 分銷佣金類型 1比例 2固定
    public double commissionLevel1;
    public double commissionLevel2;
    public double commissionAmount;
    public double commissionAmount2;
}
