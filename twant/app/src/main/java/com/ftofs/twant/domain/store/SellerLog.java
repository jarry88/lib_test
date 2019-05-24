package com.ftofs.twant.domain.store;

import java.io.Serializable;


public class SellerLog implements Serializable {
    /**
     * 自增编码
     */
    private int logId;

    /**
     * 店铺编号
     */
    private int storeId = 0;

    /**
     * 商家编号
     */
    private int sellerId = 0;

    /**
     * 商家名称
     */
    private String sellerName = "";

    /**
     * 内容
     */
    private String logContent = "";

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * IP地址
     */
    private String logIp = "";

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getLogIp() {
        return logIp;
    }

    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    @Override
    public String toString() {
        return "SellerLog{" +
                "logId=" + logId +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", logContent='" + logContent + '\'' +
                ", addTime=" + addTime +
                ", logIp='" + logIp + '\'' +
                '}';
    }
}
