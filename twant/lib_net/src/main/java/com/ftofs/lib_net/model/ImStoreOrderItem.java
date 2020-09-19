package com.ftofs.lib_net.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

/**
 * Im聊天時，選擇發送的訂單項
 * @author zwm
 */
public class ImStoreOrderItem {
    public int ordersId;
    public String ordersSn;
    public String goodsImg;
    public String goodsName;
    public double ordersAmount;
    public String  createTime;//下單時間

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("ordersId[%d], ordersSn[%s], goodsImage[%s], goodsName[%s]",
                ordersId, ordersSn, goodsImg, goodsName);
    }
}
