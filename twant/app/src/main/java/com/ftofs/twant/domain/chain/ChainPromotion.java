package com.ftofs.twant.domain.chain;



public class ChainPromotion {
    /**
     * 店内活动编号
     */
    private int promotionId;

    /**
     * 活动名称
     */
    private String promotionName;

    /**
     * 活动图片
     */
    private String promotionImage;

    /**
     * 活动图片地址
     */
    private String promotionImageSrc;

    /**
     * 活动开始时间
     */
    private String startTime;

    /**
     * 活动结束时间
     */
    private String endTime;

    /**
     * 活动介绍
     */
    private String promotionDescription;

    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionImage() {
        return promotionImage;
    }

    public void setPromotionImage(String promotionImage) {
        this.promotionImage = promotionImage;
    }

    public String getPromotionImageSrc() {
        return promotionImage;
    }

    public void setPromotionImageSrc(String promotionImageSrc) {
        this.promotionImageSrc = promotionImageSrc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    @Override
    public String toString() {
        return "ChainPromotion{" +
                "promotionId=" + promotionId +
                ", promotionName='" + promotionName + '\'' +
                ", promotionImage='" + promotionImage + '\'' +
                ", promotionImageSrc='" + promotionImageSrc + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", promotionDescription='" + promotionDescription + '\'' +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                '}';
    }
}
