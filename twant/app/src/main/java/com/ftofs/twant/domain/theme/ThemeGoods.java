package com.ftofs.twant.domain.theme;

import java.io.Serializable;
import java.sql.Timestamp;

public class ThemeGoods implements Serializable{
    /**
     * 商城活动商品编号
     */
    private int themeGoodsId;

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String StoreName;

    /**
     * 商城活动编号
     */
    private int themeId;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 审核状态
     * 0未通过，1已通过，10审核中
     */
    private int themeVerify;

    /**
     * 审核失败原因
     */
    private String verifyRemark;

    /**
     * 审核时间
     */
    private Timestamp verifyTime;

    public int getThemeGoodsId() {
        return themeGoodsId;
    }

    public void setThemeGoodsId(int themeGoodsId) {
        this.themeGoodsId = themeGoodsId;
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

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getThemeVerify() {
        return themeVerify;
    }

    public void setThemeVerify(int themeVerify) {
        this.themeVerify = themeVerify;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public Timestamp getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Timestamp verifyTime) {
        this.verifyTime = verifyTime;
    }

    @Override
    public String toString() {
        return "ThemeGoods{" +
                "themeGoodsId=" + themeGoodsId +
                ", commonId=" + commonId +
                ", storeId=" + storeId +
                ", StoreName='" + StoreName + '\'' +
                ", themeId=" + themeId +
                ", addTime=" + addTime +
                ", themeVerify=" + themeVerify +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", verifyTime=" + verifyTime +
                '}';
    }
}