package com.ftofs.twant.task;

import android.content.Context;

import androidx.annotation.Nullable;

import com.ftofs.twant.interfaces.CommonCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.ToastUtil;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.yanzhenjie.permission.runtime.Permission;

public class TencentLocationTask {
    public static void doLocation(Context context) {
        doLocation(context, false);
    }


    /**
     * 進行定位操作
     * @param context
     * @param showErrorMessage  如果沒有定位權限，是否顯示錯誤信息
     */
    public static void doLocation(Context context, boolean showErrorMessage) {
        SLog.info("doLocation");
        PermissionUtil.actionWithPermission(context, new String[] {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION}, "", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                TencentLocationRequest request = TencentLocationRequest.create();
                TencentLocationManager locationManager = TencentLocationManager.getInstance(context);
                TwTencentLocationListener locationListener = new TwTencentLocationListener(locationManager);
                int error = locationManager.requestLocationUpdates(request, locationListener);
                SLog.info("error[%d]", error);
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                SLog.info("您拒絕了授權，無法使用定位功能>_<");
                if (showErrorMessage) {
                    ToastUtil.error(context, "您拒絕了授權，無法使用定位功能>_<");
                }
                return null;
            }
        });
    }
}
