package com.ftofs.twant.entity;

public class GeneralFilterItem {
    public int id;
    public String name;
    public boolean selected;

    public GeneralFilterItem(int id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }
}
