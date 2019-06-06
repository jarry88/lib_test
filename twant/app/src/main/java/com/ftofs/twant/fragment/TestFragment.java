package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();

        LatLng latLng = new LatLng(23.02,113.76);
        // 设置显示比例
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

        aMap.addMarker(new MarkerOptions().position(latLng)
                .title("hello world")  // 设置 Marker覆盖物的 標題
                .snippet("DefaultMarker")); // 设置 Marker覆盖物的 文字描述
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));//这个是关键  如果不设置的话中心点是北京，汇出现目标点在地图上显示不了
    }

    @Override
    public void onClick(View v) {
        
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
