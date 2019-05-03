package com.ftofs.twant.entity;

public class CategoryMenu {
    public CategoryMenu(int categoryId, String categoryNameChinese, String categoryNameEnglish) {
        this.categoryId = categoryId;
        this.categoryNameChinese = categoryNameChinese;
        this.categoryNameEnglish = categoryNameEnglish;
    }

    public int categoryId;
    public String categoryNameChinese;
    public String categoryNameEnglish;
}
