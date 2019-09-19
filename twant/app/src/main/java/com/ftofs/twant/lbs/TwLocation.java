package com.ftofs.twant.lbs;


import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.Location;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;


/**
 * Takewant定位類
 * @author zwm
 */
public class TwLocation implements AMapLocationListener {
    public static double longitude;  // 当前的经度
    public static double latitude;  // 当前的纬度
    public static long timestamp;  // 定位的時間


    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    public TwLocation(Context context) {
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        // mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }


    /**
     * 获取定位类型的中文描述
     * @param locationType
     * @return
     */
    private String getLocationTypeDesc(int locationType) {
        switch (locationType) {
            case 0:
                return "定位失败";
            case 1:
                return "GPS定位结果";
            case 2:
                return "前次定位结果";
            case 4:
                return "缓存定位结果";
            case 5:
                return "Wifi定位结果";
            case 6:
                return "基站定位结果";
            case 8:
                return "离线定位结果";
            default:
                return "定位类型错误";

        }
    }

    public void onDestroy() {
        mlocationClient.onDestroy();
    }

    public void startLocation() {
        SLog.info("startLocation");
        mlocationClient.startLocation();
    }

    public void stopLocation() {
        mlocationClient.stopLocation();
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                timestamp = System.currentTimeMillis();
                latitude = amapLocation.getLatitude();
                longitude = amapLocation.getLongitude();

                SLog.info("timestamp[%s], latitude[%s], longitude[%s]", timestamp, latitude, longitude);

                Location location = new Location(longitude, latitude, timestamp);
                try {
                    String locationStr = EasyJSONObject.jsonEncode(location);
                    SLog.info("locationStr[%s]", locationStr);
                    Hawk.put(SPField.FIELD_AMAP_LOCATION, locationStr);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                SLog.info("AmapError:location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
}

