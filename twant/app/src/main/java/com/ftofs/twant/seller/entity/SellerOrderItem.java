package com.ftofs.twant.seller.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 賣家訂單列表項
 * @author zwm
 */
public class SellerOrderItem {
    // 操作位數據: 1,2,4,8,16,32,64,128,...
    public static final int MASK_SHIP = (1 << 0); // 顯示發貨按鈕
    public static final int MASK_REFUND = (1 << 1); // 顯示退款按鈕


    public String statusDesc; // 訂單狀態描述: 如【待付款】
    public String orderNo;  // 訂單編號
    public String orderTime; // 下單時間： 2020-04-09 18:56:57 這種格式
    public String from; // 來源: PC或手機等
    public String buyer; // 買家暱稱

    public List<SellerOrderSkuItem> sellerOrderSkuItemList = new ArrayList<>();

    public int goodsCount;  // 共多少件商品
    public String payVendor;  // 支付服務提供商： 微信支付或支付寶等
    public double amount; // 訂單總金額
    public double freight;

    public int operationMask; // 訂單操作的位標誌：如【退款】、【發貨】等
}
