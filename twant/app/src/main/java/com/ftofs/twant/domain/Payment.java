package com.ftofs.twant.domain;

import java.io.Serializable;

public class Payment implements Serializable {
    private int paymentId;

    /**
     * 支付代码
     */
    private String paymentCode;

    /**
     * 支付名称
     */
    private String paymentName = "";

    /**
     * 支付配置信息
     */
    private String paymentInfo = "";

    /**
     * 支付开关
     * 0-关闭 1-开启
     */
    private Integer paymentState = 0;

    /**
     * 哪个终端的支付接口 WEB/WAP/APP
     */
    private String clientType;

    /**
     * 使用场景描述
     */
    private String clientTypeText;

    /**
     * 支付平台 0全平台/1线上支付/2线下收款
     */
    private int paymentPlatform;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public Integer getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(Integer paymentState) {
        this.paymentState = paymentState;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientTypeText() {
        return clientTypeText;
    }

    public void setClientTypeText(String clientTypeText) {
        this.clientTypeText = clientTypeText;
    }

    public int getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(int paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", paymentInfo='" + paymentInfo + '\'' +
                ", paymentState=" + paymentState +
                ", clientType='" + clientType + '\'' +
                ", clientTypeText='" + clientTypeText + '\'' +
                ", paymentPlatform='" + paymentPlatform + '\'' +
                '}';
    }
}
