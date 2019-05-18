package com.ftofs.twant.entity;

import java.util.List;

public class OrderItem {
    public OrderItem(String storeName, String ordersStateName, float freightAmount, float ordersAmount, List<OrderSkuItem> orderSkuItemList) {
        this.storeName = storeName;
        this.ordersStateName = ordersStateName;
        this.freightAmount = freightAmount;
        this.ordersAmount = ordersAmount;
        this.orderSkuItemList = orderSkuItemList;
    }

    public String storeName;
    public String ordersStateName;
    public float freightAmount;
    public float ordersAmount;
    public boolean showPayButton;
    public int payId;
    public List<OrderSkuItem> orderSkuItemList;

    public void setShowPayButton(boolean showPayButton) {
        this.showPayButton = showPayButton;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }
}
