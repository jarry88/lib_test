package com.ftofs.twant.vo.store;

import com.ftofs.twant.domain.OfflinePayment;

/**
 * @author liusf
 * @create 2018/12/10 22:11
 * @description 商店綫下支付類型
 */
public class StoreOfflinePaymentVo {
    private int storePaymentId;
    private int storeId;
    private int paymentId;
    private String paymentName;
    private String paymentLogo;
    private OfflinePayment offlinePayment;

    public int getStorePaymentId() {
        return storePaymentId;
    }

    public void setStorePaymentId(int storePaymentId) {
        this.storePaymentId = storePaymentId;
    }

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

    public OfflinePayment getOfflinePayment() {
        return offlinePayment;
    }

    public void setOfflinePayment(OfflinePayment offlinePayment) {
        this.offlinePayment = offlinePayment;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
