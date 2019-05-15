package com.ftofs.twant.domain.store;

public class StoreNavigation {
    /**
     * 导航编号
     */
    private int id;

    /**
     * 导航标题
     */
    private String title;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 导航内容
     */
    private String content;

    /**
     * 导航排序
     */
    private int sort;

    /**
     * 是否显示
     */
    private int isShow;

    /**
     * 导航URL
     */
    private String url;

    /**
     * 是否新页面打开导航URL
     */
    private int isNewPage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsNewPage() {
        return isNewPage;
    }

    public void setIsNewPage(int isNewPage) {
        this.isNewPage = isNewPage;
    }

    @Override
    public String toString() {
        return "StoreNavigation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", storeId=" + storeId +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", isShow=" + isShow +
                ", url='" + url + '\'' +
                ", isNewPage='" + isNewPage + '\'' +
                '}';
    }
}

