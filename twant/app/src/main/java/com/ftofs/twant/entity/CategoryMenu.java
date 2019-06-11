package com.ftofs.twant.entity;

public class CategoryMenu {
    public CategoryMenu(int categoryId, String categoryNameChinese, String categoryNameEnglish) {
        this.selected = false;
        this.categoryId = categoryId;
        this.categoryNameChinese = categoryNameChinese;
        this.categoryNameEnglish = categoryNameEnglish;
    }

    /**
     * 這項菜單是否選中
     */
    public boolean selected;
    public int categoryId;
    public String categoryNameChinese;
    public String categoryNameEnglish;
}
