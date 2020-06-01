package com.ftofs.twant.seller.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 【賣家】規格選擇Map的Item
 * @author zwm
 */
public class SellerSpecMapItem {
    public int specId;
    public String specName;
    public boolean selected;
    public List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
}



