package com.ftofs.twant.seller.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * 【賣家】主規格值
 * @author zwm
 */
public class SellerMainSpecValue {
    public int specValue;
    public String specValueName;
    public List<SellerMainSpecValueImage> sellerMainSpecValueImageList = new ArrayList<>();
}
