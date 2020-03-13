package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

/**
 * Im聊天時，選擇發送的訂單項
 * @author zwm
 */
public class ImStoreOrderItem {
    public int ordersId;
    public String ordersSn;
    public String goodsImage;
    public String goodsName;

    @NonNull
    @Override
    public String toString() {
        return String.format("ordersId[%d], ordersSn[%s], goodsImage[%s], goodsName[%s]",
                ordersId, ordersSn, goodsImage, goodsName);
    }
}
