package com.ftofs.twant.entity;


/**
 * 支付方式項
 * @author zwm
 */
public class PayWayItem {
    public int payWay;  // 取值： Constant.PAY_WAY_ONLINE、Constant.PAY_WAY_DELIVERY、Constant.PAY_WAY_FETCH
    public String payWayName; // 支付方式名稱
    public String payWayDesc; // 支付方式描述
    public boolean isSelected; // 當前是否選中狀態

    public int selectedIconResId; // 選中狀態的圖標資源Id
    public int unselectedIconResId;  // 未選中狀態的圖標資源Id

    public PayWayItem(int payWay, String payWayName, String payWayDesc, boolean isSelected, int selectedIconResId, int unselectedIconResId) {
        this.payWay = payWay;
        this.payWayName = payWayName;
        this.payWayDesc = payWayDesc;
        this.isSelected = isSelected;
        this.selectedIconResId = selectedIconResId;
        this.unselectedIconResId = unselectedIconResId;
    }
}
