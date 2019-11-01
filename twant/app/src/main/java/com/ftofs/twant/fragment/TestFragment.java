package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import org.urllib.Query;
import org.urllib.Url;
import org.urllib.Urls;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    ImageView imageView;
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

        imageView = view.findViewById(R.id.image_view);

        Util.setOnClickListener(view, R.id.btn_test, this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            // start(H5GameFragment.newInstance("http://gogo.so/1.html"));
            // http://192.168.240.21:9898/vue/mobile_seller/src/assets/js/test.html
            // start(H5GameFragment.newInstance("http://192.168.240.21:9898/vue/mobile_seller/src/assets/js/test2.html?q=1211"));

            // Glide.with(_mActivity).load("file:///android_asset/double_eleven/double_eleven_dynamic.gif").into(imageView);
            start(GifFragment.newInstance("https://gfile.oss-cn-hangzhou.aliyuncs.com/takewant/1.gif"));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
