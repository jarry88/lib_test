package com.ftofs.twant.domain.store;

import java.sql.Timestamp;

public class StoreOfflinePayment {
    /**
     * 主鍵ID
     */
    private int id;

    /**
     * 店鋪ID
     */
    private int storeId;

    /**
     * 支付類型ID
     */
    private int paymentId;

    /**
     * 創建時間
     */
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "StoreOfflinePayment{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", paymentId=" + paymentId +
                ", createTime=" + createTime +
                '}';
    }
}
