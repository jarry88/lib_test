package com.ftofs.twant.domain.store;

public class StoreClass {
    /**
     * 店铺分类ID
     */
    private int id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 保证金数额
     */
    private int bail;

    /**
     * 排序
     */
    private int sort;

    /**
     * 父ID
     */
    private int parentId;//Add By Nick.Chung 2018/7/25 11:17 增加主營行業二級分類

    /**
     * 分類圖標
     */
    private String picUrl;//Add By Nick.Chung 2018/9/26 11:38 增加分類圖標

    /**
     * 分類圖片
     */
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBail() {
        return bail;
    }

    public void setBail(int bail) {
        this.bail = bail;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "StoreClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bail=" + bail +
                ", sort=" + sort +
                '}';
    }
}

