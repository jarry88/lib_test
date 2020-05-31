package com.ftofs.twant.seller.entity;

public class SellerSpecItem {
    public static final int TYPE_SPEC = 1;
    public static final int TYPE_SPEC_VALUE = 2;

    public int type;

    public int id;
    public String name;

    public boolean selected; // 是否选中
}
