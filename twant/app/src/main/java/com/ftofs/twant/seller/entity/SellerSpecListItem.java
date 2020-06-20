package com.ftofs.twant.seller.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 【賣家】規格組列表項
 * @author zwm
 */
public class SellerSpecListItem {
    // 平臺預設規格
    public static final int SPEC_TYPE_SYSTEM = 1;
    // 自定義規格
    public static final int SPEC_TYPE_CUSTOM = 2;

    public int specType;

    public int specId;
    public String specName;
    public List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();

    public SellerSpecListItem(int specType, int specId, String specName) {
        this.specType = specType;
        this.specId = specId;
        this.specName = specName;
    }
}
