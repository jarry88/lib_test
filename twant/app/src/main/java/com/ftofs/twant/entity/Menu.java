package com.ftofs.twant.entity;



public class Menu {
    public int category;
    public boolean isSelected;
    public int categoryTitlePosition;  // 分類標題在item列表中的位置

    public Menu(int category, boolean isSelected, int categoryTitlePosition) {
        this.category = category;
        this.isSelected = isSelected;
        this.categoryTitlePosition = categoryTitlePosition;
    }
}

