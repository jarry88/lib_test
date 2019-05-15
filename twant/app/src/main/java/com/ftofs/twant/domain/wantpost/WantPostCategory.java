package com.ftofs.twant.domain.wantpost;

public class WantPostCategory {
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

    @Override
    public String toString() {
        return "WantPostCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categorySort='" + categorySort + '\'' +
                ", deep=" + deep +
                ", parentId=" + parentId +
                '}';
    }
}
