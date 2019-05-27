package com.ftofs.twant.vo.wantpost;

/**
 * @author liusf
 * @create 2019/2/18 11:39
 * @description 貼文分類視圖類
 */
public class WantPostCategoryVo  {
    /**
     *分類id
     */
    private int  categoryId;

    /**
     * 分類名稱
     */
    private String categoryName;

    /**
     * 分類排序
     */
    private String categorySort;

    /**
     * 層級
     */
    private int deep;

    /**
     * 父id
     */
    private int parentId;

    /**
     * 父級分類名稱
     */
    private String parentName = "";

    /**
     * 分類下貼文數量
     */
    private long wantPostCount = 0;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public long getWantPostCount() {
        return wantPostCount;
    }

    public void setWantPostCount(long wantPostCount) {
        this.wantPostCount = wantPostCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(String categorySort) {
        this.categorySort = categorySort;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
