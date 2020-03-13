package com.ftofs.twant.task;

import android.content.Context;

import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.log.SLog;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

public class TencentLocationTask {
    public static void doLocation(Context context) {
        SLog.info("doLocation");
        TencentLocationRequest request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(context);
        TwTencentLocationListener locationListener = new TwTencentLocationListener(locationManager);
        int error = locationManager.requestLocationUpdates(request, locationListener);
        SLog.info("error[%d]", error);
    }
}
