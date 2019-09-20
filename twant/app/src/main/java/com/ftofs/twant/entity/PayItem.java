package com.ftofs.twant.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 訂單列表里面的支付Item
 * 里面包含一個或多個店鋪Item
 * @author zwm
 */
public class PayItem {
    public int payId;
    public float payAmount;
    public boolean showPayButton;
    public List<OrderItem> orderItemList = new ArrayList<>();

    public PayItem(int payId, float payAmount, boolean showPayButton) {
        this.payId = payId;
        this.payAmount = payAmount;
        this.showPayButton = showPayButton;

    }
}
