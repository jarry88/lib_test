package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StudentAdapter;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Test2Fragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;

    public static Test2Fragment newInstance() {
        Bundle args = new Bundle();

        Test2Fragment fragment = new Test2Fragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        List<String> studentList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            studentList.add("Student:" + i);
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        StudentAdapter studentAdapter = new StudentAdapter(studentList);
        rvList.setAdapter(studentAdapter);


        List<CrossBorderBannerItem> bannerItemList = new ArrayList<>();
        CrossBorderBannerItem bannerItem = new CrossBorderBannerItem();
        bannerItem.image = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/0159525979bab9a8012193a329c12d.jpg";
        bannerItemList.add(bannerItem);

        bannerItem = new CrossBorderBannerItem();
        bannerItem.image = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/5f24336176dd4a80d94ba96d937992ca.png";
        bannerItemList.add(bannerItem);

        bannerItem = new CrossBorderBannerItem();
        bannerItem.image = "https://gfile.oss-cn-hangzhou.aliyuncs.com/img/v2-e2aa8d9c5237173c11954db9cc8b9c5b_1200x500.jpg";
        bannerItemList.add(bannerItem);

        Banner<CrossBorderBannerItem, BannerImageAdapter<CrossBorderBannerItem>> banner = view.findViewById(R.id.banner);
        banner.setAdapter(new BannerImageAdapter<CrossBorderBannerItem>(bannerItemList) {
            @Override
            public void onBindView(BannerImageHolder holder, CrossBorderBannerItem data, int position, int size) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                        .load(data.image)
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(_mActivity));
        banner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SLog.info("xxxxxx[%d]", position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                SLog.info("OnBannerClick[%d]", position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
