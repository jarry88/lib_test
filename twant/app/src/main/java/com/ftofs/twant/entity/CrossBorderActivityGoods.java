package com.ftofs.twant.entity;

public class CrossBorderActivityGoods {
    public int activityType; // 砍價還是拼團

    public int bargainId;
    public int groupId;
    public int goodsId;
    public int commonId;
    public String goodsImage;
    public String goodsName;
    public double price;

    public CrossBorderActivityGoods(int activityType, int goodsId, int commonId, String goodsImage, String goodsName, double price) {
        this.activityType = activityType;
        this.goodsId = goodsId;
        this.commonId = commonId;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.price = price;
    }
}
