package com.ftofs.twant.entity;

/**
 * 分類商品數據結構
 * @author zwm
 */
public class CategoryCommodity {
    public CategoryCommodity(int categoryId, String categoryName, String imageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
    }

    public int categoryId;
    public String categoryName;
    public String imageUrl;
}
