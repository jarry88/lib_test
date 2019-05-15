package com.ftofs.twant.domain;

public class ArticlePosition {
    /**
     * 主键、自增
     */
    private int positionId;

    /**
     * 位置名称
     */
    private String positionTitle;

    /**
     * 是否可以新增下面的分类
     */
    private int allowAddCategory = 1;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public int getAllowAddCategory() {
        return allowAddCategory;
    }

    public void setAllowAddCategory(int allowAddCategory) {
        this.allowAddCategory = allowAddCategory;
    }

    @Override
    public String toString() {
        return "ArticlePosition{" +
                "positionId=" + positionId +
                ", positionTitle='" + positionTitle + '\'' +
                ", allowAddCategory=" + allowAddCategory +
                '}';
    }
}
