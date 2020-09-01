package com.ftofs.twant.entity;

import java.util.List;

/**
 * 分類產品數據結構
 * @author zwm
 */
public class CategoryCommodity {
    public CategoryCommodity(int categoryId, String categoryName, String imageUrl) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;//appImageUrl
    }

    public int categoryId;
    public int deep;//1 2 3
    public List<CategoryCommodity> categoryCommodityList;
    public String categoryName;
    public String imageUrl;
}
