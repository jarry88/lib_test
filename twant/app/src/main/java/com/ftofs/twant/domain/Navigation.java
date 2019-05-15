package com.ftofs.twant.domain;

public class Navigation {
    /**
     * 主键
     */
    private int navId;

    /**
     * 导航标题
     */
    private String title;

    /**
     * 导航跳转链接
     */
    private String url;

    /**
     * 导航跳转链接类型
     */
    private String type;

    /**
     * 显示位置
     */
    private int positionId = 2;

    /**
     * 是否新窗口打开 1-是，0-否
     */
    private int openType;

    /**
     * 分类排序
     */
    private int sort = 0;

    public int getNavId() {
        return navId;
    }

    public void setNavId(int navId) {
        this.navId = navId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Navigation{" +
                "navId=" + navId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", positionId=" + positionId +
                ", openType=" + openType +
                ", sort=" + sort +
                '}';
    }
}
