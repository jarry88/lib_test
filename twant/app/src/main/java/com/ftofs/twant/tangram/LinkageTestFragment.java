package com.ftofs.twant.tangram;

import android.app.Notification;
import android.graphics.Color;
import android.graphics.Outline;
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
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.CartFragment;
import com.ftofs.twant.fragment.FirstFragment;
import com.ftofs.twant.fragment.SecondFragment;
import com.ftofs.twant.fragment.ShoppingLinkageFragment;
import com.ftofs.twant.fragment.ShoppingSpecialLinkageFragment;
import com.ftofs.twant.fragment.ShoppingStoreListFragment;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.view.ViewOutlineProvider;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class LinkageTestFragment extends BaseFragment implements View.OnClickListener, NestedScrollingCallback {
    private MZBannerView bannerView;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();


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
    private ShoppingSpecialLinkageFragment shoppingLinkageFragment;
    private CommonFragmentPagerAdapter adapter;

    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    private long lastScrollingTimestamp;
    private boolean isScrolling;
    private long FLOAT_BUTTON_SCROLLING_EFFECT_DELAY=75;
    private LinearLayout llFloatButtonContainer;
    private boolean showTab=true;
    private LinearLayout llBanner;

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
        llBanner = view.findViewById(R.id.ll_banner_container);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_goto_top,this);
        Util.setOnClickListener(view,R.id.btn_goto_cart,this);

        llFloatButtonContainer=view.findViewById(R.id.ll_float_button_container);

        containerView = view.findViewById(R.id.container_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        vwAnchor = view.findViewById(R.id.vw_anchor);

        shoppingLinkageFragment = ShoppingSpecialLinkageFragment.newInstance();
//        secondFragment = SecondFragment.newInstance();
        storeListFragment =ShoppingStoreListFragment.newInstance();
        withoutCategoryFragment = ShoppingLinkageFragment.newInstance();

        bannerView = view.findViewById(R.id.banner_view);
        initBanner();
        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
//        initViewPager();
        loadData();
    }
    private void initBanner() {
        //設置banner頁圓角
        bannerView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), Util.dip2px(_mActivity,8));
            }
        });
        bannerView.setClipToOutline(true);
        int heightPadding = Util.getScreenDimension(_mActivity).first * 9 / 16 - Util.dip2px(_mActivity, 45);
        bannerView.setIndicatorPadding(0,heightPadding,0,0);
        UiUtil.addBannerPageClick(bannerView,webSliderItemList);
    }
    private void setBannerData(EasyJSONArray discountBannerList) {
        SLog.info("bannerListLength %d",discountBannerList.length());
        try {
            for (Object object : discountBannerList) {
                String imageUrl = String.valueOf(object);
                WebSliderItem webSliderItem = new WebSliderItem(StringUtil.normalizeImageUrl(imageUrl), null, null, null, "[]");
                webSliderItemList.add(webSliderItem);
                // 设置数据
                bannerView.setPages(webSliderItemList, (MZHolderCreator<BannerViewHolder>) () -> new BannerViewHolder(webSliderItemList));

//                carouselLoaded = true;
            }
            if (discountBannerList.length() == 1) {
                bannerView.setCanLoop(false);
            } else {
                bannerView.start();
                bannerView.setDelayedTime(2500);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    private void initViewPager() {
        adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
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

                boolean nestedScroll = tabY <= containerViewY;
                if (!showTab) {
                    nestedScroll = viewPagerY <= containerViewY;
                }
                SLog.info("nestedScroll[%s],viewPagerY[%s], containerViewY[%s], tabY[%s], anchorViewY[%s]",nestedScroll,
                        viewPagerY, containerViewY, tabY, anchorViewY);
                // 如果列表滑动到顶部，则启用嵌套滚动


                storeListFragment.setNestedScrollingEnabled(nestedScroll);
                if (hasGoodsCategory==1) {
                    shoppingLinkageFragment.setNestedScrollingEnabled(nestedScroll);
                }else {
                    withoutCategoryFragment.setNestedScrollingEnabled(nestedScroll);
                }

                if (!nestedScroll) {
                    if (oldScrollY > scrollY) {
                        onCbStartNestedScroll();
                    } else {
                        onCbStopNestedScroll();
                    }
                }
            }
        });
        //加載數據
    }

    private void loadData() {
        if (zoneId < 0 && Config.DEVELOPER_MODE) {
            String str = AssetsUtil.loadText(_mActivity, "tangram/test.json");
            EasyJSONObject responseObj = EasyJSONObject.parse(str);
            updateView(responseObj);
            return;
        }
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
                    pop();
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

            EasyJSONArray appAdImageList = zoneVo.getArray("appAdImageList");
            if (appAdImageList != null) {
                setBannerData(appAdImageList);
            }
//
            String storeTabTitle = zoneVo.getSafeString("storeTabTitle");
            String goodsTabTitle = zoneVo.getSafeString("goodsTabTitle");


            EasyJSONArray zoneGoodsVoList = zoneVo.getArray("zoneGoodsVoList");

            EasyJSONArray zoneGoodsCategoryVoList = zoneVo.getArray("zoneGoodsCategoryVoList");
            //商品列表
            if (hasGoodsCategory == Constant.TRUE_INT) {
                if (!StringUtil.isEmpty(goodsTabTitle)) {
                    titleList.add(goodsTabTitle);
                    tabLayout.addTab(tabLayout.newTab().setText(goodsTabTitle));
                    fragmentList.add(shoppingLinkageFragment);
//                tabLayout.removeTabAt(1);
                shoppingLinkageFragment.setDataList(zoneGoodsCategoryVoList);
                    shoppingLinkageFragment.setNestedScroll(this);
                }

            } else {
                if (!StringUtil.isEmpty(goodsTabTitle)) {
                    titleList.add(goodsTabTitle);
                    tabLayout.addTab(tabLayout.newTab().setText(goodsTabTitle));

                    fragmentList.add(withoutCategoryFragment);
                    SLog.info("無類別商品標簽數據");
//                tabLayout.removeTabAt(0);
                    withoutCategoryFragment.setNestedScroll(this);
                    withoutCategoryFragment.setGoodVoList(zoneGoodsVoList);
                }

            }

            EasyJSONArray zoneStoreVoList = zoneVo.getSafeArray("zoneStoreVoList");
            if (zoneStoreVoList != null && zoneStoreVoList.length() > 0) {
                SLog.info("設置商店列表數據");
                if (!StringUtil.isEmpty(storeTabTitle)) {
                    titleList.add(storeTabTitle);
                    tabLayout.addTab(tabLayout.newTab().setText(storeTabTitle));
                    fragmentList.add(storeListFragment);
                    storeListFragment.setOnNestedScroll(this);
                    storeListFragment.setStoreList(zoneStoreVoList);
                }

            }

            initViewPager();

            if (tabLayout.getTabCount() <= 1) {
                tabLayout.setVisibility(View.GONE);
                showTab = false;

                setViewPagerHeight();
            }

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }
    void setViewPagerHeight() {
        tabHeight = tabLayout.getHeight();
        SLog.info("containerHeight[%d], tabHeight[%d]", containerHeight, tabHeight);

        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();

        if (showTab) {
            layoutParams.height = containerHeight - tabHeight;
        } else {
            layoutParams.height = containerHeight;
        }
        viewPager.setLayoutParams(layoutParams);
    }


    private void updateThemeColor(String appColor) {
        if ("#FFFFFF".equals(appColor)) {
            tvZoneName.setTextColor(getResources().getColor(R.color.tw_black));
        }
        rlToolBar.setBackgroundColor(Color.parseColor(appColor));
        llBanner.setBackgroundColor(Color.parseColor(appColor));
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
        onCbStopNestedScroll();
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
        int userId= User.getUserId();
        if (id == R.id.btn_goto_cart) {
            if (userId > 0) {
                Util.startFragment(CartFragment.newInstance(true));
            } else {
                Util.showLoginFragment();
            }

        } else if (id == R.id.btn_goto_top) {
            containerView.scrollTo(0,0);
            if (hasGoodsCategory == 1) {
                shoppingLinkageFragment.scrollToTop();
            } else if(withoutCategoryFragment!=null){
                withoutCategoryFragment.scrollToTop();
            }
            storeListFragment.scrollToTop();
        }
    }

    public void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0, 2, null))
                .show();
    }

    @Override
    public void onCbStartNestedScroll() {
        SLog.info("onCbStartNestedScroll");
        isScrolling = true;
        lastScrollingTimestamp = System.currentTimeMillis();
        hideFloatButton();
    }

    @Override
    public void onCbStopNestedScroll() {
        SLog.info("onCbStopNestedScroll");
        isScrolling = false;
        llFloatButtonContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScrolling) {
                    return;
                }

                showFloatButton();
            }
        }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY);
    }
    private void showFloatButton() {
        if (floatButtonShown){
            return;
        }

        // 防止延遲時序問題導致的空引用異常
        if (llFloatButtonContainer == null) {
            return;
        }
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
//        layoutParams.rightMargin = Util.dip2px(_mActivity, 0);
//        llFloatButtonContainer.setLayoutParams(layoutParams);
//        floatButtonShown = true;
        TranslateAnimation translateAnimation = new TranslateAnimation(100, 0, 0, 0);
        translateAnimation.setDuration(250);
        translateAnimation.setFillAfter(true);
        llFloatButtonContainer.setAnimation(translateAnimation);
        llFloatButtonContainer.startAnimation(translateAnimation);
        SLog.info("執行顯示");
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }
        floatButtonShown = false;
        llFloatButtonContainer.postDelayed(() -> {
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 0);
                    translateAnimation.setDuration(400);
                    translateAnimation.setFillAfter(true);
            llFloatButtonContainer.setAnimation(translateAnimation);
            llFloatButtonContainer.startAnimation(translateAnimation);
                    SLog.info("執行隱藏");
                }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY
        );
    }
}
