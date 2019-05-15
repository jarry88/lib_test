package com.ftofs.twant.domain;

public class OfflinePayment {
    /**
     * 主鍵ID
     */
    private int paymentId;

    /**
     * 支付名稱
     */
    private String paymentName;

    /**
     * logo
     */
    private String paymentLogo;

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

    public String getPaymentLogo() {
        return paymentLogo;
    }

    public void setPaymentLogo(String paymentLogo) {
        this.paymentLogo = paymentLogo;
    }

    @Override
    public String toString() {
        return "OfflinePayment{" +
                "paymentId=" + paymentId +
                ", paymentName='" + paymentName + '\'' +
                ", paymentLogo='" + paymentLogo + '\'' +
                '}';
    }
}
