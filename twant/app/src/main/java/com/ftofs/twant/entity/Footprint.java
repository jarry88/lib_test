package com.ftofs.twant.entity;

/**
 * 【瀏覽記憶】數據結構
 * @author zwm
 */
public class Footprint {
    /**
     * 瀏覽記憶選中狀態的常量定義
     */
    // 沒選中任何東西
    public static final int SELECT_STATUS_NONE = 0;
    // 選中了商品項
    public static final int SELECT_STATUS_GOODS = 1;
    // 選中了店鋪項
    public static final int SELECT_STATUS_STORE = 2;
    // 選中了日期項
    public static final int SELECT_STATUS_DATE = 4;


    public Footprint(int footprintId, String date, int storeId, String storeName, int commonId, String imageSrc,
                     String goodsName, String jingle, float price) {
        this.footprintId = footprintId;
        this.date = date;
        this.storeId = storeId;
        this.storeName = storeName;
        this.commonId = commonId;
        this.imageSrc = imageSrc;
        this.goodsName = goodsName;
        this.jingle = jingle;
        this.price = price;
    }

    public int footprintId;
    /**
     * 日期， ，2019-05-31 這種格式
     */
    public String date;
    public int storeId;
    public String storeName;
    public int commonId;
    public String imageSrc;
    public String goodsName;
    public String jingle;
    public float price;
}
