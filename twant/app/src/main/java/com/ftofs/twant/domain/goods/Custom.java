package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class Custom implements Serializable {
    /**
     * 自定义属性编号
     */
    private int customId;

    /**
     * 自定义属性名称
     */
    private String customName;

    /**
     * 分类编号
     */
    private int categoryId;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Custom{" +
                "customId=" + customId +
                ", customName='" + customName + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
