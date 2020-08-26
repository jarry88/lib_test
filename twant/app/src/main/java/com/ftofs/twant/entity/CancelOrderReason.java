package com.ftofs.twant.entity;

public class CancelOrderReason {
    public String id;
    public String text;
    public boolean selected;  // 是否選中

    public CancelOrderReason(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
