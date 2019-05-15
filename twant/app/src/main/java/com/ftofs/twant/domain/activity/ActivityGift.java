package com.ftofs.twant.domain.activity;

import java.io.Serializable;
import java.math.BigDecimal;

public class ActivityGift implements Serializable {
    /**
     * 赠品编号
     * 主键、自增
     */
    private Integer giftId;

    /**
     * 商品名称
     */
    private String giftGoodsName;

    /**
     * 赠品显示价格
     */
    private BigDecimal giftPrice = new BigDecimal(0);

    /**
     * 图片名称
     */
    private String giftImageName = "";

    /**
     * 赠品状态 0.被删除 1.正常
     */
    private int giftState = 1 ;

    /**
     * 图片路径
     */
    private String imageSrc = "";

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftGoodsName() {
        return giftGoodsName;
    }

    public void setGiftGoodsName(String giftGoodsName) {
        this.giftGoodsName = giftGoodsName;
    }

    public BigDecimal getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(BigDecimal giftPrice) {
        this.giftPrice = giftPrice;
    }

    public String getGiftImageName() {
        return giftImageName;
    }

    public void setGiftImageName(String giftImageName) {
        this.giftImageName = giftImageName;
    }

    public int getGiftState() {
        return giftState;
    }

    public void setGiftState(int giftState) {
        this.giftState = giftState;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    @Override
    public String toString() {
        return "ActivityGift{" +
                "giftId=" + giftId +
                ", giftGoodsName='" + giftGoodsName + '\'' +
                ", giftPrice=" + giftPrice +
                ", giftImageName='" + giftImageName + '\'' +
                ", giftState=" + giftState +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
