package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 后台品牌JSON数据实体
 * 
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class BrandAdminListVo {
    /**
     * 品牌编号
     */
    private int brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 英文名称
     */
    private String brandEnglish;
    /**
     * 品牌首字母
     */
    private String brandInitial;
    /**
     * 品牌图片
     */
    private String brandImage;
    /**
     * 品牌标签名称
     */
    private String brandLabelNames;
    /**
     * 品牌标签编号
     */
    private String brandLabelIds;
    /**
     * 品牌排序
     */
    private int brandSort = 0;
    /**
     * 是否推荐
     */
    private int isRecommend;
    /**
     * 是否审核
     */
    private int applyState;
    /**
     * 展示方式
     */
    private int showType;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandEnglish() {
        return brandEnglish;
    }

    public void setBrandEnglish(String brandEnglish) {
        this.brandEnglish = brandEnglish;
    }

    public String getBrandInitial() {
        return brandInitial;
    }

    public void setBrandInitial(String brandInitial) {
        this.brandInitial = brandInitial;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getBrandLabelNames() {
        return brandLabelNames;
    }

    public void setBrandLabelNames(String brandLabelNames) {
        this.brandLabelNames = brandLabelNames;
    }

    public String getBrandLabelIds() {
        return brandLabelIds;
    }

    public void setBrandLabelIds(String brandLabelIds) {
        this.brandLabelIds = brandLabelIds;
    }

    public int getBrandSort() {
        return brandSort;
    }

    public void setBrandSort(int brandSort) {
        this.brandSort = brandSort;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getApplyState() {
        return applyState;
    }

    public void setApplyState(int applyState) {
        this.applyState = applyState;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return "BrandAdminListVo{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", brandEnglish='" + brandEnglish + '\'' +
                ", brandInitial='" + brandInitial + '\'' +
                ", brandImage='" + brandImage + '\'' +
                ", brandLabelNames='" + brandLabelNames + '\'' +
                ", brandLabelIds='" + brandLabelIds + '\'' +
                ", brandSort=" + brandSort +
                ", isRecommend=" + isRecommend +
                ", applyState=" + applyState +
                ", showType=" + showType +
                '}';
    }
}
