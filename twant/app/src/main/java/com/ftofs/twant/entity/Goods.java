package com.ftofs.twant.entity;

/**
 * 商品數據結構
 * @author zwm
 */
public class Goods {
    public Goods(int id, String imageUrl, String name, String jingle, double price) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.jingle = jingle;
        this.price = price;
    }

    public int id;
    public String imageUrl;
    public String name;
    public String jingle;
    public double price;
}
