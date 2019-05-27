package com.ftofs.twant.domain.distribution;

import java.io.Serializable;

public class DistributorGoods implements Serializable {
    /**
     * 分销商商品编号
     */
    private int distributorGoodsId;

    /**
     * 分销商商品 spu id
     */
    private int commonId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 分销商 会员名称
     */
    private String storeName;

    /**
     * 分销商id
     */
    private int distributorId;

    /**
     * 分销商 会员id
     */
    private int memberId;

    /**
     * 分销商 会员名称
     */
    private String memberName;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 该会员的商品推广数量
     */
    private int distributionCount ;

    /**
     * 选品库分组
     */
    private int distributorFavoritesId = 0 ;

    /**
     * 商品图片地址
     */
    private String goodsImageSrc = "";

    /**
     * 商品图片名称
     */
    private String goodsImageName = "";

    /**
     * 商品名称
     */
    private String goodsName = "";

    /**
     * wap 分享地址
     */
    private String wapShareUrl = "";

    public int getDistributorGoodsId() {
        return distributorGoodsId;
    }

    public void setDistributorGoodsId(int distributorGoodsId) {
        this.distributorGoodsId = distributorGoodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getDistributorFavoritesId() {
        return distributorFavoritesId;
    }

    public void setDistributorFavoritesId(int distributorFavoritesId) {
        this.distributorFavoritesId = distributorFavoritesId;
    }

    public String getGoodsImageSrc() {
        return goodsImageSrc;
    }

    public void setGoodsImageSrc(String goodsImageSrc) {
        this.goodsImageSrc = goodsImageSrc;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getWapShareUrl() {
        return "";
    }

    public void setWapShareUrl(String wapShareUrl) {
        this.wapShareUrl = wapShareUrl;
    }

    public String getGoodsImageName() {
        return goodsImageName;
    }

    public void setGoodsImageName(String goodsImageName) {
        this.goodsImageName = goodsImageName;
    }


    public int getDistributionCount() {
        return distributionCount;
    }

    public void setDistributionCount(int distributionCount) {
        this.distributionCount = distributionCount;
    }

    @Override
    public String toString() {
        return "DistributorGoods{" +
                "distributorGoodsId=" + distributorGoodsId +
                ", commonId=" + commonId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", distributorId=" + distributorId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", addTime=" + addTime +
                ", distributionCount=" + distributionCount +
                ", distributorFavoritesId=" + distributorFavoritesId +
                ", goodsImageSrc='" + goodsImageSrc + '\'' +
                ", goodsImageName='" + goodsImageName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", wapShareUrl='" + wapShareUrl + '\'' +
                '}';
    }
}
