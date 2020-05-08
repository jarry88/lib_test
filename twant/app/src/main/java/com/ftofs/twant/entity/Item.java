package com.ftofs.twant.entity;

public class Item {
    public boolean isCategoryTitle;
    public int category;
    public int id;
    public Goods goods;



    public Item(boolean isCategoryTitle, int category, int id, Goods goods) {
        this.isCategoryTitle = isCategoryTitle;
        this.category = category;
        this.id = id;
        this.goods = goods;
    }
    public Item(boolean isCategoryTitle, int category, int id) {
        this(isCategoryTitle, category, id, null);
    }
}
