package com.ftofs.twant.entity;

public class CrossBorderShoppingZoneItem {
    public int zoneId;
    public String zoneName;
    public String appLogo;

    public CrossBorderShoppingZoneItem() {
    }

    public CrossBorderShoppingZoneItem(int zoneId, String zoneName, String appLogo) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.appLogo = appLogo;
    }

    @Override
    public String toString() {
        return "CrossBorderShoppingZoneItem{" +
                "zoneId=" + zoneId +
                ", zoneName='" + zoneName + '\'' +
                ", appLogo='" + appLogo + '\'' +
                '}';
    }
}
