package com.ftofs.twant.entity;

/**
 * 跨城購首頁導航Item
 * @author zwm
 */
public class CrossBorderNavItem {
    public int navId;
    public String navName;
    public String icon;
    public String linkTypeApp;
    public String linkValueApp;

    public CrossBorderNavItem() {
    }

    public CrossBorderNavItem(int navId, String navName, String icon, String linkTypeApp, String linkValueApp) {
        this.navId = navId;
        this.navName = navName;
        this.icon = icon;
        this.linkTypeApp = linkTypeApp;
        this.linkValueApp = linkValueApp;
    }

    @Override
    public String toString() {
        return "CrossBorderNavItem{" +
                "navId=" + navId +
                ", navName='" + navName + '\'' +
                ", icon='" + icon + '\'' +
                ", linkTypeApp='" + linkTypeApp + '\'' +
                ", linkValueApp='" + linkValueApp + '\'' +
                '}';
    }
}
