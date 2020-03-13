package com.ftofs.twant.domain.promotion;

import java.io.Serializable;
import java.math.BigDecimal;

public class Book implements Serializable {
    /**
     * 產品SKU
     */
    private int goodsId;

    /**
     * 產品SPU
     */
    private int commonId;

    /**
     * 產品名称
     */
    private String goodsName;

    /**
     * 店铺编号
     */
    private Integer storeId;

    /**
     * 定金支付百分比
     */
    private int downPercent;

    /**
     * 定金金额
     */
    private BigDecimal downPayment;

    /**
     * 尾款金额
     */
    private BigDecimal finalPayment;

    /**
     * 总额
     */
    private BigDecimal totalPayment;

    /**
     * 定金支付结束时间
     */
    private String downTime;

    /**
     * 自增排序
     */
    private String createTime;

    /**
     * 状态
     */
    private int bookState;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public int getDownPercent() {
        return downPercent;
    }

    public void setDownPercent(int downPercent) {
        this.downPercent = downPercent;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBookState() {
        return 1;
    }

    public void setBookState(int bookState) {
        this.bookState = bookState;
    }

    @Override
    public String toString() {
        return "Book{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", storeId=" + storeId +
                ", downPercent=" + downPercent +
                ", downPayment=" + downPayment +
                ", finalPayment=" + finalPayment +
                ", totalPayment=" + totalPayment +
                ", downTime=" + downTime +
                ", createTime=" + createTime +
                ", bookState=" + bookState +
                '}';
    }
}
