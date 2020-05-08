package com.ftofs.twant.entity;



public class Menu {
    public int category;
    public String categoryName;
    public boolean isSelected;
    public int categoryTitlePosition;  // 分類標題在item列表中的位置

    public Menu(String category, boolean isSelected, int categoryTitlePosition) {
        this.categoryName = category;
        this.isSelected = isSelected;
        this.categoryTitlePosition = categoryTitlePosition;
    }
}

