package com.ftofs.twant.task;

import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.Location;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;

import cn.snailpad.easyjson.EasyJSONObject;

public class TwTencentLocationListener implements TencentLocationListener {
    TencentLocationManager locationManager;
    public TwTencentLocationListener(TencentLocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        SLog.info("TencentLocationListener::onLocationChanged, location[%s], error[%d], reason[%s]",
                location, error, reason);
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            long timestamp = System.currentTimeMillis();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            SLog.info("timestamp[%s], latitude[%s], longitude[%s]", timestamp, latitude, longitude);

            Location loc = new Location(longitude, latitude, timestamp);
            try {
                String locationStr = EasyJSONObject.jsonEncode(loc);
                SLog.info("locationStr[%s]", locationStr);
                Hawk.put(SPField.FIELD_AMAP_LOCATION, locationStr);
            } catch (Exception e) {

            }
        } else {
            // 定位失败
        }
        locationManager.removeUpdates(this);
        locationManager = null;
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        SLog.info("TencentLocationListener::onStatusUpdate, name[%s], status[%d], desc[%s]",
                name, status, desc);
    }
}
