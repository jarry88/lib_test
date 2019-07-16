package com.ftofs.twant.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 高德地圖彈窗
 * @author zwm
 */
public class AmapPopup extends BottomPopupView implements View.OnClickListener {
    Activity activity;
    MapView mapView;
    AMap aMap;

    StoreMapInfo storeMapInfo;
    Bundle savedInstanceState;

    public AmapPopup(@NonNull Activity activity, StoreMapInfo storeMapInfo, @Nullable Bundle savedInstanceState) {
        super(activity);

        this.activity = activity;
        this.storeMapInfo = storeMapInfo;
        this.savedInstanceState = savedInstanceState;
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
        tvStoreAddress.setText(storeMapInfo.storeAddress);

        TextView tvStoreDistance = findViewById(R.id.tv_store_distance);
        if (storeMapInfo.storeDistance < Constant.STORE_DISTANCE_THRESHOLD) {
            // 如果沒有距離，則隱藏距離信息
            tvStoreDistance.setVisibility(GONE);
        } else {
            String distanceText = activity.getString(R.string.text_distance) + String.format("%.1f") + "距km";
            tvStoreDistance.setText(distanceText);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        // 參考資料 https://blog.csdn.net/lhp15575865420/article/details/78150854
        LatLng latLng = new LatLng(storeMapInfo.storeLatitude,storeMapInfo.storeLongitude);
        // 设置显示比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        aMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_marker)))
                .title(storeMapInfo.storeName));  // 设置 Marker覆盖物的 標題
                // .snippet("店鋪文字描述")); // 设置 Marker覆盖物的 文字描述

        // 使高德地圖顯示指定的地點，如果不设置的话中心点是北京
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
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
            Intent intent;
            try {
                // 判斷是否已經安裝高德地圖
                if (Util.isPackageInstalled(activity, "com.autonavi.minimap")) {
                    SLog.info("已經安裝高德地圖");
                    String uri = "androidamap://navi?sourceApplication=" + activity.getString(R.string.app_name) + "&poiname=我的目的地&lat="+storeMapInfo.storeLatitude+"&lon="+storeMapInfo.storeLongitude+"&dev=0";
                    SLog.info("uri[%s]", uri);
                    intent = Intent.parseUri(uri, 0);
                    activity.startActivity(intent);
                } else {
                    SLog.info("未安裝高德地圖");
                    // 如果未安裝高德地圖，則跳轉到應用市場
                    ToastUtil.error(activity, "您尚未安裝高德地圖");
                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                SLog.info("Error!%s", e.getMessage());
            }

        }
    }
}
