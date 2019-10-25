package com.ftofs.twant.entity;

/**
 * 真實值數據結構
 * @author zwm
 */
public class TrustValueItem {
    public TrustValueItem(String title, String content, int value, String timestamp) {
        this.title = title;
        this.content = content;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String title;
    public String content;
    public int value;
    public String timestamp;
}
