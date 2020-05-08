package com.ftofs.twant.tangram;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.FirstFragment;
import com.ftofs.twant.fragment.SecondFragment;
import com.ftofs.twant.fragment.ShoppingLinkageFragment;
import com.ftofs.twant.fragment.ShoppingStoreListFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lxj.xpopup.core.BasePopupView;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class LinkageTestFragment extends BaseFragment implements View.OnClickListener {
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
    private int zoneId;
    private int hasGoodsCategory;
    private TextView tvZoneName;
    private ShoppingLinkageFragment withoutCategoryFragment;
    private RelativeLayout rlToolBar;
    private ShoppingStoreListFragment storeListFragment;

    public static LinkageTestFragment newInstance(int zoneId) {
        Bundle args = new Bundle();

        LinkageTestFragment fragment = new LinkageTestFragment();
        fragment.zoneId = zoneId;
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lingkage_shopping, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvZoneName = view.findViewById(R.id.tv_zone_name);
        rlToolBar = view.findViewById(R.id.tool_bar);
        Util.setOnClickListener(view,R.id.btn_back,this);

        containerView = view.findViewById(R.id.container_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        vwAnchor = view.findViewById(R.id.vw_anchor);

        titleList.add("商品");
        titleList.add("-");
        titleList.add("店鋪");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));


        firstFragment = FirstFragment.newInstance();
//        secondFragment = SecondFragment.newInstance();
        storeListFragment =ShoppingStoreListFragment.newInstance();
        withoutCategoryFragment = ShoppingLinkageFragment.newInstance();

        fragmentList.add(firstFragment);
        fragmentList.add(storeListFragment);
        fragmentList.add(withoutCategoryFragment);

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
                boolean nestedScroll = tabY <= containerViewY;
                  // 如果列表滑动到顶部，则启用嵌套滚动
                if (firstFragment != null) {
                    firstFragment.setNestedScrollingEnabled(nestedScroll);
                }
                    storeListFragment.setNestedScrollingEnabled(nestedScroll);
                if (withoutCategoryFragment != null) {
                    withoutCategoryFragment.setNestedScroll(nestedScroll);
                }
            }
        });
        //加載數據
        loadData();
    }
    private void loadData() {
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();


        // 獲取商店首頁信息
        String path = Api.PATH_SHOPPING_ZONE + "/" + zoneId;
        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]",responseStr);
                //測試數據
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }
                updateView(responseObj);
            }
        });
    }
    private void updateView(EasyJSONObject responseObj) {
        try {
            EasyJSONObject zoneVo = responseObj.getObject("datas.zoneVo");
            hasGoodsCategory = zoneVo.getInt("hasGoodsCategory");
            zoneId = zoneVo.getInt("zoneId");
            //第一階段無用
//            zoneState = zoneVo.getInt("zoneState");
//            zoneType = zoneVo.getInt("zoneType");
//            String appLogo = zoneVo.getSafeString("appLogo");

            String appColor = zoneVo.getSafeString("appColor");
            //調試階段試色
            String zoneName = zoneVo.getSafeString("zoneName");
            tvZoneName.setText(zoneName);
            updateThemeColor(appColor);
//
            String storeTabTitle = zoneVo.getSafeString("storeTabTitle");
            String goodsTabTitle = zoneVo.getSafeString("goodsTabTitle");
            if (!StringUtil.isEmpty(storeTabTitle)) {
                tabLayout.getTabAt(2).setText(storeTabTitle);
            }
            if (!StringUtil.isEmpty(goodsTabTitle)) {
                tabLayout.getTabAt(0).setText(goodsTabTitle);
                tabLayout.getTabAt(1).setText(goodsTabTitle);
            }

//
//            EasyJSONArray appAdImageList = zoneVo.getArray("appAdImageList");
//            if (appAdImageList != null) {
//                setBannerData(appAdImageList);
//            }
            EasyJSONArray zoneGoodsVoList = zoneVo.getArray("zoneGoodsVoList");
            EasyJSONArray zoneStoreVoList = zoneVo.getArray("zoneStoreVoList");
            if (zoneStoreVoList != null&&zoneStoreVoList.length()>0) {
                SLog.info("設置商店列表數據");
                storeListFragment.setStoreList(zoneStoreVoList);
            } else {
                //隱藏tab
            }
            EasyJSONArray zoneGoodsCategoryVoList = zoneVo.getArray("zoneGoodsCategoryVoList");
            //商品列表
            if (hasGoodsCategory == Constant.TRUE_INT) {
//                tabLayout.removeTabAt(1);

                if (zoneGoodsCategoryVoList != null) {

//                    updateLinkage(zoneGoodsCategoryVoList);
                }


            } else {
                SLog.info("無類別商品標簽數據");
//                tabLayout.removeTabAt(0);
                withoutCategoryFragment.setGoodVoList(zoneGoodsVoList);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }

    private void updateThemeColor(String appColor) {
        if ("#FFFFFF".equals(appColor)) {
            tvZoneName.setTextColor(getResources().getColor(R.color.tw_black));
        }
        rlToolBar.setBackgroundColor(Color.parseColor(appColor));
//        llBanner.setBackground(bannerBackGround);
        tabLayout.setTabTextColors(Color.parseColor(StringUtil.addAlphaToColor(appColor,60)),Color.parseColor(appColor));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(appColor));
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
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }
}
