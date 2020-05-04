package com.ftofs.twant.seller.entity;

public class SellerOrderStatus {
    public String statusDesc;
    public String timestamp;
    public boolean isLatestStatus;

    public SellerOrderStatus(String statusDesc, String timestamp) {
        this.statusDesc = statusDesc;
        this.timestamp = timestamp;
    }
}
