package com.ftofs.twant.domain.chain;

import java.io.Serializable;

public class ChainPayment implements Serializable {
    private int paymentId;

    /**
     * 排序
     */
    private int sort;

    /**
     * 支付名称
     */
    private String paymentName;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "ChainPayment{" +
                "paymentId=" + paymentId +
                ", sort=" + sort +
                ", paymentName='" + paymentName + '\'' +
                '}';
    }
}
