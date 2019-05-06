package com.ftofs.twant.entity;

/**
 * 分類品牌數據結構
 * @author zwm
 */
public class CategoryBrand {
    public CategoryBrand(int brandId, String brandNameChinese, String brandNameEnglish, String imageUrl) {
        this.brandId = brandId;
        this.brandNameChinese = brandNameChinese;
        this.brandNameEnglish = brandNameEnglish;
        this.imageUrl = imageUrl;
    }

    public int brandId;
    public String brandNameChinese;
    public String brandNameEnglish;
    public String imageUrl;
}
