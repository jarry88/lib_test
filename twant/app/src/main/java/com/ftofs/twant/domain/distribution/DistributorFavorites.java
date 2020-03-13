package com.ftofs.twant.domain.distribution;

import java.io.Serializable;

public class DistributorFavorites implements Serializable {
    /**
     * 编号
     */
    private int distributorFavoritesId;

    /**
     * 推广產品id
     */
    private int distributorId;

    /**
     * 名称
     */
    private String distributorFavoritesName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否是默认文件夹
     */
    private int isDefault = 0;

    public int getDistributorFavoritesId() {
        return distributorFavoritesId;
    }

    public void setDistributorFavoritesId(int distributorFavoritesId) {
        this.distributorFavoritesId = distributorFavoritesId;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorFavoritesName() {
        return distributorFavoritesName;
    }

    public void setDistributorFavoritesName(String distributorFavoritesName) {
        this.distributorFavoritesName = distributorFavoritesName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "DistributorFavorites{" +
                "distributorFavoritesId=" + distributorFavoritesId +
                ", distributorId=" + distributorId +
                ", distributorFavoritesName='" + distributorFavoritesName + '\'' +
                ", createTime=" + createTime +
                ", isDefault=" + isDefault +
                '}';
    }
}
