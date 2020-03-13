package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class StoreGrade implements Serializable {
    /**
     * 等级编号
     */
    private int id;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 產品限制
     */
    private int goodsLimit;

    /**
     * 图片空间容量（MB）
     */
    private int albumLimit;

    /**
     * 推荐產品限制
     */
    private int recommendLimit;

    /**
     * 店铺模板数量
     */
    private int templateCount;

    /**
     * 店铺模板名称
     */
    private String template;

    /**
     * 价格
     */
    private int price;

    /**
     * 申请说明
     */
    private String description;

    /**
     * 排序
     */
    private int sort;

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

    public int getGoodsLimit() {
        return goodsLimit;
    }

    public void setGoodsLimit(int goodsLimit) {
        this.goodsLimit = goodsLimit;
    }

    public int getAlbumLimit() {
        return albumLimit;
    }

    public void setAlbumLimit(int albumLimit) {
        this.albumLimit = albumLimit;
    }

    public int getRecommendLimit() {
        return recommendLimit;
    }

    public void setRecommendLimit(int recommendLimit) {
        this.recommendLimit = recommendLimit;
    }

    public int getTemplateCount() {
        return templateCount;
    }

    public void setTemplateCount(int templateCount) {
        this.templateCount = templateCount;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "StoreGrade{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", goodsLimit=" + goodsLimit +
                ", albumLimit=" + albumLimit +
                ", recommendLimit=" + recommendLimit +
                ", templateCount=" + templateCount +
                ", template='" + template + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", sort=" + sort +
                '}';
    }
}

