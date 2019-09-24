package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 訂單列表里面的支付Item
 * 里面包含一個或多個店鋪Item
 * @author zwm
 */
public class PayItem implements MultiItemEntity {
    public int itemType;
    public int payId;
    public float payAmount;
    public boolean showPayButton;
    public List<OrderItem> orderItemList = new ArrayList<>();

    public PayItem(int payId, float payAmount, boolean showPayButton) {
        this.itemType = Constant.ITEM_TYPE_NORMAL;
        this.payId = payId;
        this.payAmount = payAmount;
        this.showPayButton = showPayButton;
    }

    public PayItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
