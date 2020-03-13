package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class GoodsImage implements Serializable {
    /**
     * 產品图片编号
     */
    private int imageId=0;

    /**
     * 產品SPU编号
     */
    private int commonId=0;

    /**
     * 颜色规格值编号
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId=0;

    /**
     * 图片名称
     */
    private String imageName="";

    /**
     * 图片排序
     */
    private int imageSort=0;

    /**
     * 默认主图
     * 1是，0否
     */
    private int isDefault=0;

    /**
     * 图片路径
     */
    private String imageSrc="";

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageSort() {
        return imageSort;
    }

    public void setImageSort(int imageSort) {
        this.imageSort = imageSort;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getImageSrc() {
        return imageName;
    }

    @Override
    public String toString() {
        return "GoodsImage{" +
                "imageId=" + imageId +
                ", commonId=" + commonId +
                ", colorId=" + colorId +
                ", imageName='" + imageName + '\'' +
                ", imageSort=" + imageSort +
                ", isDefault=" + isDefault +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
