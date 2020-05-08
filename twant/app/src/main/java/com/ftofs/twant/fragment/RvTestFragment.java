package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RvTestFragment extends BaseFragment {
    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    ViewPager viewPager;

    View vwAnchor;

    View containerView;
    int containerHeight;
    TabLayout tabLayout;
    int tabHeight;

    FirstFragment firstFragment;
    SecondFragment secondFragment;

    public static RvTestFragment newInstance() {
        Bundle args = new Bundle();

        RvTestFragment fragment = new RvTestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rv_test, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerView = view.findViewById(R.id.container_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        vwAnchor = view.findViewById(R.id.vw_anchor);

        titleList.add("商品");
        titleList.add("店鋪");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        firstFragment = FirstFragment.newInstance();
        secondFragment = SecondFragment.newInstance();
        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SLog.info( "page %d" ,position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        containerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int viewPagerY = Util.getYOnScreen(viewPager);
                int containerViewY = Util.getYOnScreen(containerView);
                int tabY = Util.getYOnScreen(tabLayout);
                int anchorViewY = Util.getYOnScreen(vwAnchor);

                SLog.info("viewPagerY[%s], containerViewY[%s], tabY[%s], anchorViewY[%s]",
                        viewPagerY, containerViewY, tabY, anchorViewY);

                if (anchorViewY <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
                    firstFragment.setNestedScrollingEnabled(true);
                    secondFragment.setNestedScrollingEnabled(true);
                } else {
                    firstFragment.setNestedScrollingEnabled(false);
                    secondFragment.setNestedScrollingEnabled(false);
                }
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerHeight == 0) {
            containerHeight = containerView.getHeight();
            tabHeight = tabLayout.getHeight();
            SLog.info("containerHeight[%d], tabHeight[%d]", containerHeight, tabHeight);

            ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
            layoutParams.height = containerHeight - tabHeight;
            viewPager.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
