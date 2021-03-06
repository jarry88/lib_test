package com.ftofs.twant.domain;

public class Article {
    /**
     * 想要帖主键、自增
     */
    private int articleId;

    /**
     * 想要帖标题
     */
    private String title;

    /**
     * 想要帖分类ID
     */
    private int categoryId;

    /**
     * 外链
     */
    private String url;

    /**
     * 想要帖内容
     */
    private String content = "";

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否允许删除
     * 1-是 0-否
     */
    private int allowDelete = 1;

    /**
     * 分类排序
     */
    private int sort = 0;

    private String categoryTitle = "";

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(int allowDelete) {
        this.allowDelete = allowDelete;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", allowDelete=" + allowDelete +
                ", sort=" + sort +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
}
