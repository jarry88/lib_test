package com.ftofs.twant.tangram;

import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.constant.UmengAnalyticsPageName;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.CartFragment;
import com.ftofs.twant.fragment.FirstFragment;
import com.ftofs.twant.fragment.LinkageContainerFragment2;
import com.ftofs.twant.fragment.LinkageShoppingListFragment;
import com.ftofs.twant.fragment.SecondFragment;
import com.ftofs.twant.fragment.ShoppingSpecialLinkageFragment;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.kotlin.ZoneAdapter;
import com.ftofs.twant.kotlin.ZoneItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.umeng.analytics.MobclickAgent;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 *新購物專場入口頁
 */
public class NewShoppingSpecialFragment extends BaseFragment implements View.OnClickListener, NestedScrollingCallback {
    private MZBannerView bannerView;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();


    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    ViewPager viewPager;

    View vwAnchor;

    View containerView;
    int containerHeight;
    TabLayout tabLayout;
    RecyclerView rvList;
    int tabHeight;

    FirstFragment firstFragment;
    SecondFragment secondFragment;
    private int zoneId;
    private int zoneType;//專場類型 0通用 1店鋪 2商品
    static int DEFAULT_ZONE = 0;
    static int GOOD_ZONE = 2;
    static int SHOP_ZONE = 1;
    private int hasGoodsCategory;
    private TextView tvZoneName;
    private RelativeLayout rlToolBar;
    private LinkageShoppingListFragment storeListFragment;
    private ShoppingSpecialLinkageFragment shoppingLinkageFragment;
    private CommonFragmentPagerAdapter adapter;

    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    private long lastScrollingTimestamp;
    private boolean isScrolling;
    private long FLOAT_BUTTON_SCROLLING_EFFECT_DELAY=75;
    private LinearLayout llFloatButtonContainer;
    private boolean showTab=true;
    private LinearLayout llBanner;
    private LinkageContainerFragment2 linkageGoodsFragment2;
    private int zoneState=1;//專場狀態 0關閉 1開啟 2停用
    private ConstraintLayout rLcontainer;
    private LinearLayout appBackground;

    public static NewShoppingSpecialFragment newInstance(int zoneId) {
        Bundle args = new Bundle();

        NewShoppingSpecialFragment fragment = new NewShoppingSpecialFragment();
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

        if (Config.PROD) {
            MobclickAgent.onPageStart(UmengAnalyticsPageName.ACTIVITY_ZONE);

            HashMap<String, Object> analyticsDataMap = new HashMap<>();
            analyticsDataMap.put("zoneId", zoneId);
            MobclickAgent.onEventObject(TwantApplication.getInstance(), UmengAnalyticsActionName.ACTIVITY_ZONE, analyticsDataMap);
        }


        tvZoneName = view.findViewById(R.id.tv_zone_name);
        rlToolBar = view.findViewById(R.id.tool_bar);
        llBanner = view.findViewById(R.id.ll_banner_container);
        rLcontainer = view.findViewById(R.id.rl_container);
        appBackground = view.findViewById(R.id.app_background);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_goto_top,this);
        Util.setOnClickListener(view,R.id.btn_goto_cart,this);

        llFloatButtonContainer=view.findViewById(R.id.ll_float_button_container);

        containerView = view.findViewById(R.id.container_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        vwAnchor = view.findViewById(R.id.vw_anchor);
        rvList = view.findViewById(R.id.rv_other_zone_list);

        bannerView = view.findViewById(R.id.banner_view);
        initBanner();
        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
//        initViewPager();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (Config.PROD) {
            MobclickAgent.onPageEnd(UmengAnalyticsPageName.ACTIVITY_ZONE);
        }
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
                bannerView.getViewPager().setOnScrollChangeListener(null);
                bannerView.getViewPager().setNestedScrollingEnabled(false);
                bannerView.setCanLoop(false);
                bannerView.setNestedScrollingEnabled(false);
                bannerView.setHorizontalFadingEdgeEnabled(false);
                bannerView.setHorizontalScrollBarEnabled(false);
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
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中加粗
                updateTabTextView(tab ,true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabTextView(tab ,false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
//                SLog.info("nestedScroll[%s],viewPagerY[%s], containerViewY[%s], tabY[%s], anchorViewY[%s]",nestedScroll,
//                        viewPagerY, containerViewY, tabY, anchorViewY);
                // 如果列表滑动到顶部，则启用嵌套滚动

                if (storeListFragment != null) {
                    storeListFragment.getViewModel().getNestedScrollingEnable().setValue(nestedScroll);
                }
                if (linkageGoodsFragment2 != null) {
                    linkageGoodsFragment2.getViewModel().getNestedScrollingEnable().setValue(nestedScroll);
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
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {
        if (isSelect) {
            try {
                java.lang.reflect.Field fieldView= tab.getClass().getDeclaredField("mView");
                fieldView.setAccessible(true);
                View view= (View) fieldView.get(tab);
                java.lang.reflect.Field fieldTxt= view.getClass().getDeclaredField("mTextView");
                fieldTxt.setAccessible(true);
                TextView tabSelect= (TextView) fieldTxt.get(view);
                tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tabSelect.setText(tab.getText());
                SLog.info("設置加粗%s",tabSelect.getText());
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

            }

        } else {
            try {
                java.lang.reflect.Field fieldView= tab.getClass().getDeclaredField("view");
                fieldView.setAccessible(true);
                View view= (View) fieldView.get(tab);
                java.lang.reflect.Field fieldTxt= view.getClass().getDeclaredField("mTextView");
                fieldTxt.setAccessible(true);
                TextView tabSelect= (TextView) fieldTxt.get(view);
                tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tabSelect.setText(tab.getText());
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

            }
        }
    }
    private void loadData() {
        if (zoneId < 0 && Config.DEVELOPER_MODE) {
            String str = AssetsUtil.loadText(_mActivity, "tangram/test.json");
            EasyJSONObject responseObj = EasyJSONObject.parse(str);
            updateView(responseObj);
            return;
        }
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();


        // 獲取專場入口頁信息
        String path = Api.PATH_SHOPPING_ZONE + "/" + zoneId;
        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(path, "", "", e.getMessage());
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]",responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(path, "", responseStr, "");
                    pop();
                    return;
                }

                updateView(responseObj);
            }
        });
    }
    private void updateView(EasyJSONObject responseObj) {
        try {

            zoneState  = responseObj.getInt("datas.zoneState ");
            if(zoneState==Constant.ZONE_CLOSE_TYPE||zoneState==Constant.ZONE_STOP_TYPE){
                tvZoneName.setText("活動專場");
                llFloatButtonContainer.setVisibility(View.GONE);
                rLcontainer.setVisibility(View.VISIBLE);
                containerView.setVisibility(View.GONE);
                rlToolBar.setBackgroundColor(getResources().getColor(R.color.tw_yellow));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
                ZoneAdapter zoneAdapter = new ZoneAdapter(R.layout.layout_zone_item,R.layout.close_zone_top_item);
                zoneAdapter.showHeadView(true);
                zoneAdapter.showFootView(true);
//                zoneAdapter.showFootView(true);
                rvList.setLayoutManager(linearLayoutManager);
                rvList.setAdapter(zoneAdapter);
                List<ZoneItem> list = new ArrayList<>();
//                list.add(new ZoneItem(-1, "", "dd", "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/8e/b8/8eb8d7b9a7b1e96ae01b2b27c1663857.png"));

                if (responseObj.exists("datas.zoneList")) {
                    EasyJSONArray zoneList = responseObj.getSafeArray("datas.zoneList");
                    for (Object object : zoneList) {
                        list.add(ZoneItem.parase((EasyJSONObject) object));
                    }
                } else if(Util.inDev()){
                    list.add(new ZoneItem(5, "這是在安卓段寫死的測試數據", "dd", "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/8e/b8/8eb8d7b9a7b1e96ae01b2b27c1663857.png"));
                }

                zoneAdapter.addAll(list,true);
                return;
            }


            EasyJSONObject zoneVo = responseObj.getObject("datas.zoneVo");
            hasGoodsCategory = zoneVo.getInt("hasGoodsCategory");
            zoneId = zoneVo.getInt("zoneId");
            //第一階段無用
//            zoneState = zoneVo.getInt("zoneState");
            zoneType = zoneVo.getInt("zoneType");
//            String appLogo = zoneVo.getSafeString("appLogo");

            String appColor = zoneVo.getSafeString("appColor");
            //調試階段試色
            String zoneName = zoneVo.getSafeString("zoneName");
            tvZoneName.setText(zoneName);
            tvZoneName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            updateThemeColor(appColor);

            EasyJSONArray appAdImageList = zoneVo.getArray("appAdImageList");
            if (appAdImageList != null) {
                setBannerData(appAdImageList);
            }
//
            String storeTabTitle = zoneVo.getSafeString("storeTabTitle");
            String goodsTabTitle = zoneVo.getSafeString("goodsTabTitle");


            if (zoneType == DEFAULT_ZONE || zoneType == GOOD_ZONE) {
                if (StringUtil.isEmpty(goodsTabTitle)) {
                    goodsTabTitle = "商品列表";
                }
                    titleList.add(goodsTabTitle);

                tabLayout.addTab(tabLayout.newTab().setText(goodsTabTitle));
                linkageGoodsFragment2  = LinkageContainerFragment2.Companion.newInstance(zoneId,this);
                fragmentList.add(linkageGoodsFragment2);
            }

            if (zoneType == DEFAULT_ZONE || zoneType == SHOP_ZONE) {
                if (StringUtil.isEmpty(storeTabTitle)) {
                    storeTabTitle = "商店列表";
                }

                    titleList.add(storeTabTitle);
                tabLayout.addTab(tabLayout.newTab().setText(storeTabTitle));
                storeListFragment = new LinkageShoppingListFragment(zoneId, this);
                fragmentList.add(storeListFragment);
            }
            //刷新UI的邏輯
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
        SLog.info("onBackPressedSupport");
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
            if (linkageGoodsFragment2 != null) {
                linkageGoodsFragment2.scrollToTop();
            }
            if (storeListFragment != null) {
                storeListFragment.scrollToTop();
            }
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
//        SLog.info("onCbStartNestedScroll");
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

    public int getZoneId() {
        return zoneId;
    }
}
