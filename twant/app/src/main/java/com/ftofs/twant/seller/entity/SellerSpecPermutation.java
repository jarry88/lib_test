package com.ftofs.twant.seller.entity;

import java.util.ArrayList;
import java.util.List;

public class SellerSpecPermutation {
    public int goodsId;
    public List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
    public String specValueString;  // 規格值拼接起來，比如 紅色/xss
    public double price;
    public String goodsSN;  // 商品編號
    public int storage; // 庫存
    public int reserved; // 預存庫存
}
