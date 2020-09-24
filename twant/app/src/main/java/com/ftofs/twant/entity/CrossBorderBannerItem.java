package com.ftofs.twant.entity;

public class CrossBorderBannerItem {
    public int bannerId;
    public String image;
    public String linkTypeApp;
    public String linkValueApp;
    public String backgroundColorApp;

    public CrossBorderBannerItem() {
    }

    public CrossBorderBannerItem(int bannerId, String image, String linkTypeApp, String linkValueApp, String backgroundColorApp) {
        this.bannerId = bannerId;
        this.image = image;
        this.linkTypeApp = linkTypeApp;
        this.linkValueApp = linkValueApp;
        this.backgroundColorApp = backgroundColorApp;
    }
}
