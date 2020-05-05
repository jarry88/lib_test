package com.ftofs.twant.entity;

import com.ftofs.twant.constant.Constant;

import java.util.List;

public class OrderItem {
    public OrderItem(int orderId, String storeName, String ordersStateName, double freightAmount,
                     double ordersAmount, boolean showMemberCancel, boolean showMemberBuyAgain,
                     boolean showShipSearch, boolean showEvaluation, boolean showMemberReceive,
                     List<OrderSkuItem> orderSkuItemList, List<GiftItem> giftItemList) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.ordersState = Constant.SERVER_ORDERS_STATE_UNINITIALIZED;
        this.ordersStateName = ordersStateName;
        this.freightAmount = freightAmount;
        this.ordersAmount = ordersAmount;
        this.showMemberCancel = showMemberCancel;
        this.showMemberBuyAgain = showMemberBuyAgain;
        this.showShipSearch = showShipSearch;
        this.showEvaluation = showEvaluation;
        this.showMemberReceive = showMemberReceive;
        this.orderSkuItemList = orderSkuItemList;
        this.giftItemList = giftItemList;
    }

    public int orderId;
    public String storeName;
    public int ordersState; // 服務器端定義的訂單狀態
    public String ordersStateName;
    public double freightAmount;
    public double ordersAmount;
    public boolean showMemberCancel;
    public boolean showMemberBuyAgain;
    public boolean showShipSearch;
    public boolean showEvaluation;
    public boolean showMemberReceive;
    public List<OrderSkuItem> orderSkuItemList;
    public List<GiftItem> giftItemList;
}
