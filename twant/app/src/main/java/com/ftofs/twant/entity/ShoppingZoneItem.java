package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import cn.snailpad.easyjson.EasyJSONObject;

public class ShoppingZoneItem {
    public int zoneId;
    public String zoneName;

    @NonNull
    @Override
    public String toString() {
        return String.format("zoneId[%s],applogo[%s],zoneName[%s]", zoneId, appLogo, zoneName);
    }

    public String appLogo;

    public static ShoppingZoneItem parse(EasyJSONObject zone) throws Exception {
            ShoppingZoneItem item = new ShoppingZoneItem();
            item.zoneId = zone.getInt("zoneId");
            item.appLogo = zone.getSafeString("appLogo");
            item.zoneName = zone.getSafeString("zoneName");
            return item;

    }
}
