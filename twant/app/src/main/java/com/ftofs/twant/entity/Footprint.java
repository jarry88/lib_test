package com.ftofs.twant.entity;

/**
 * 【我的足跡】數據結構
 * @author zwm
 */
public class Footprint {
    public Footprint(String date, int storeId, String storeName, int commonId, String imageSrc,
                     String goodsName, String jingle, float price) {
        this.date = date;
        this.storeId = storeId;
        this.storeName = storeName;
        this.commonId = commonId;
        this.imageSrc = imageSrc;
        this.goodsName = goodsName;
        this.jingle = jingle;
        this.price = price;
    }

    // 日期， ，2019-05-31 這種格式
    public String date;
    public int storeId;
    public String storeName;
    public int commonId;
    public String imageSrc;
    public String goodsName;
    public String jingle;
    public float price;
}
