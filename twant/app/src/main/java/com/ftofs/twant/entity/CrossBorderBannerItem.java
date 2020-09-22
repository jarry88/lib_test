package com.ftofs.twant.entity;

public class CrossBorderBannerItem {
    public int bannerId;
    public String image;
    public String linkTypeApp;
    public String linkValueApp;

    public CrossBorderBannerItem() {
    }

    public CrossBorderBannerItem(int bannerId, String image, String linkTypeApp, String linkValueApp) {
        this.bannerId = bannerId;
        this.image = image;
        this.linkTypeApp = linkTypeApp;
        this.linkValueApp = linkValueApp;
    }
}
