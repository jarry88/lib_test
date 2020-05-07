package com.ftofs.twant.entity;

public class Item {
    public boolean isCategoryTitle;
    public int category;
    public int id;

    public Item(boolean isCategoryTitle, int category, int id) {
        this.isCategoryTitle = isCategoryTitle;
        this.category = category;
        this.id = id;
    }
}
