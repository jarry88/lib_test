package com.ftofs.twant.entity;

public class StoreSortCriteriaItem {
    public int id;
    public String name;
    public boolean selected;

    public StoreSortCriteriaItem(int id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }
}
