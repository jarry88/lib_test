package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品图片，產品发布时使用
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:11
 */
public class GoodsPicVo {
    /**
     * 颜色规格值编号
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 图片名称
     */
    private String imageName;
    /**
     * 图片排序
     */
    private int imageSort;
    /**
     * 默认主图
     * 1是，0否
     */
    private int isDefault;

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

    @Override
    public String toString() {
        return "GoodsPicVo{" +
                "colorId=" + colorId +
                ", imageName='" + imageName + '\'' +
                ", imageSort=" + imageSort +
                ", isDefault=" + isDefault +
                '}';
    }
}
