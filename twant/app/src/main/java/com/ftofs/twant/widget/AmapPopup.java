package com.ftofs.twant.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreMapInfo;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.XPopupUtils;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德地圖彈窗
 * @author zwm
 */
public class AmapPopup extends BottomPopupView implements View.OnClickListener {
    public static class MapAppInfo {
        public boolean available;
        public int id;
        public String name;
        public String uri;
        public String packageName;

        public MapAppInfo(boolean available, int id, String name, String uri, String packageName) {
            this.available = available;
            this.id = id;
            this.name = name;
            this.uri = uri;
            this.packageName = packageName;
        }
    }


    /**
     * 高德地圖
     */
    public static final int MAP_ID_AMAP = 0;
    /**
     * Google地圖
     */
    public static final int MAP_ID_GOOGLE = 1;

    Activity activity;
    MapView mapView;
//    AMap aMap;

    StoreMapInfo storeMapInfo;

    public List<MapAppInfo> mapAppInfoList;
    int availableMapCount;

    public AmapPopup(@NonNull Activity activity, StoreMapInfo storeMapInfo) {
        super(activity);

        this.activity = activity;
        this.storeMapInfo = storeMapInfo;

        mapAppInfoList = new ArrayList<>();
        mapAppInfoList.add(new MapAppInfo(false, MAP_ID_AMAP, "高德地圖", null, "com.autonavi.minimap"));
        mapAppInfoList.add(new MapAppInfo(false, MAP_ID_GOOGLE, "Google地圖", null, "com.google.android.apps.maps"));
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.amap_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dial_store_phone).setOnClickListener(this);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_map_navigation).setOnClickListener(this);

        TextView tvStoreAddress = findViewById(R.id.tv_store_address);
        TextView tvBusInfo = findViewById(R.id.tv_bus_info);
        tvStoreAddress.setText(storeMapInfo.storeAddress);
        if (StringUtil.isEmpty(storeMapInfo.busInfo)) {
            tvBusInfo.setVisibility(GONE);
        } else {
            tvBusInfo.setText(storeMapInfo.busInfo);
        }

        TextView tvStoreDistance = findViewById(R.id.tv_store_distance);
        if (storeMapInfo.storeDistance < Constant.STORE_DISTANCE_THRESHOLD) {
            // 如果沒有距離，則隱藏距離信息
            tvStoreDistance.setVisibility(GONE);
            findViewById(R.id.vw_line).setVisibility(GONE);
        } else {
            String distanceText = activity.getString(R.string.text_distance) + String.format("%.1f", storeMapInfo.storeDistance / 1000) + "km";
            tvStoreDistance.setText(distanceText);
        }

        mapView = findViewById(R.id.map_view);
        SLog.info("storeLatitude[%s], storeLongitude[%s]", storeMapInfo.storeLatitude,storeMapInfo.storeLongitude);
        LatLng latLng = new LatLng(storeMapInfo.storeLatitude,storeMapInfo.storeLongitude);
        // latLng = new LatLng(20, 115);
        TencentMap map = mapView.getMap();
        //设置卫星底图
        // map.setSatelliteEnabled(true);
        //设置地图中心点
        map.setCenter(latLng);
        //设置缩放级别
        map.setZoom(19);

        Bitmap bmMarker = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_map_marker);
        bmMarker = BitmapUtil.scaleBitmap(bmMarker, 0.7f);

        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                // .title("上海")
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(bmMarker))
                .draggable(false));


        // 初始化地圖應用狀態
        MapAppInfo mapAppInfo = mapAppInfoList.get(MAP_ID_AMAP);
        if (Util.isPackageInstalled(activity, mapAppInfo.packageName)) {
            SLog.info("已經安裝高德地圖");
            mapAppInfo.available = true;
            mapAppInfo.uri = "androidamap://navi?sourceApplication=" + activity.getString(R.string.app_name) + "&poiname=我的目的地&lat="+storeMapInfo.storeLatitude+"&lon="+storeMapInfo.storeLongitude+"&dev=0";
            availableMapCount++;
        }

        mapAppInfo = mapAppInfoList.get(MAP_ID_GOOGLE);
        if (Util.isPackageInstalled(activity, mapAppInfo.packageName)) {
            SLog.info("已經安裝Google地圖");
            mapAppInfo.available = true;
            mapAppInfo.uri = "google.navigation:q=" +storeMapInfo.storeLatitude+","+storeMapInfo.storeLongitude;
            availableMapCount++;
        }
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        mapView.onDestroy();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_dial_store_phone) {
            Util.dialPhone(activity, storeMapInfo.storePhone);
        } else if (id == R.id.btn_map_navigation) {
            /*
             * 處理方式
             * 1、如果安裝了多個地圖應用，則彈出列表讓用戶選擇
             * 2、如果只有一個地圖應用，則直接用那個地圖應用來導航
             * 3、如果沒有安裝地圖應用，則提示用戶安裝
             */

            if (availableMapCount > 1) {
                String[] availableApp = new String[availableMapCount];
                int i = 0;
                for (MapAppInfo item : mapAppInfoList) {
                    if (item.available) {
                        availableApp[i++] = item.name;
                    }
                }

                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .asCenterList("請選擇", availableApp,
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        SLog.info("position[%d], text[%s]", position, text);
                                        MapAppInfo item = mapAppInfoList.get(position);
                                        showMapNavigation(item);
                                    }
                                })
                        .show();
            } else if (availableMapCount == 1) {
                // 找出一個可用的地圖
                for (MapAppInfo item : mapAppInfoList) {
                    if (item.available) {
                        showMapNavigation(item);
                        break;
                    }
                }
            } else {
                SLog.info("未安裝任何地圖應用");
                // 如果未安裝地圖應用，則跳轉到應用市場
                ToastUtil.error(activity, "您尚未安裝任何地圖應用");
                Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            }
        }
    }

    private void showMapNavigation(MapAppInfo mapAppInfo) {
        if (mapAppInfo.id == MAP_ID_AMAP) {
            navigateWithAmap();
        } else if (mapAppInfo.id == MAP_ID_GOOGLE) {
            navigateWithGoogle();
        }
    }

    private void navigateWithAmap() {
        MapAppInfo mapAppInfo = mapAppInfoList.get(MAP_ID_AMAP);
        SLog.info("uri[%s]", mapAppInfo.uri);
        Intent intent = null;
        try {
            intent = Intent.parseUri(mapAppInfo.uri, 0);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        activity.startActivity(intent);
    }

    /**
     * 使用Google地圖導航
     * 參考
     * https://developers.google.com/maps/documentation/urls/guide
     * https://developers.google.com/maps/documentation/urls/android-intents
     */
    private void navigateWithGoogle() {
        MapAppInfo mapAppInfo = mapAppInfoList.get(MAP_ID_GOOGLE);
        SLog.info("uri[%s]", mapAppInfo.uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapAppInfo.uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.apps.maps");
        activity.startActivity(intent);
    }
}
