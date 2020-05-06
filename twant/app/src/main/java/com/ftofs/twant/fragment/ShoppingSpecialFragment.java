package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.ShopGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.view.TwantTabLayout;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 主題購物專場，同原有專場類似但是獨立，具有商品和店鋪及添加購物車，配置頁面主題顔色，生成結算金額等功能
 * @author gzp
 */
public class ShoppingSpecialFragment extends BaseFragment implements View.OnClickListener , NestedScrollingCallback {

    private Unbinder unbinder;
    @BindView(R.id.tv_zone_name)
    TextView tvZoneName;
    @BindView(R.id.stb_goods_tab_title)
    SimpleTabButton stbGoodsTabTitle;
    @BindView(R.id.stb_store_tab_title)
    SimpleTabButton stbStoreTabTitle;
    @BindView(R.id.btn_goods)
    SimpleTabButton stbTagGoods;
    @BindView(R.id.btn_store)
    SimpleTabButton stbTagStore;
    @BindView(R.id.shopping_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.shopping_viewpager)
    ViewPager viewPager;
    private List<String> titleList;
    private List<Fragment> fragmentList= new ArrayList<>();
    private int LINKAGE_FRAGMENT=0;
    private int STORE_FRAGMENT=1;
    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    private long lastScrollingTimestamp;
    private boolean isScrolling;
    private long FLOAT_BUTTON_SCROLLING_EFFECT_DELAY=800;
    private ShoppingLinkageFragment linkageFragment;
    private ShoppingStoreListFragment storeListFragment;


    @OnClick(R.id.btn_goods)
    void showGoodsListInfo() {
        SLog.info("showGoodsListInfo");
    }
    @OnClick(R.id.stb_goods_tab_title)
    void showGoodsListInfo1() {
        SLog.info("showGoodsListInfo1");
    }@OnClick(R.id.btn_store)
    void showStoresListInfo() {
        SLog.info("showStoresListInfo");
    }
    @OnClick(R.id.stb_store_tab_title)
    void showStoresListInfo1() {
        SLog.info("showStoresListInfo1");
    }
    @BindView(R.id.tool_bar)
    RelativeLayout rlToolBar;
    @BindView(R.id.ll_tab_button_container)
    LinearLayout llTabContainer;
    @BindView(R.id.cl_banner_container)
    ConstraintLayout clBannerContainer;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.ll_filter_bar)
    LinearLayout llFilterBar;

    @BindView(R.id.rv_single_goods_list)
    RecyclerView rvGoodsList;
    @BindView(R.id.rv_single_store_list)
    RecyclerView rvStoreList;

    List<ElemeGroupedItem> storeItems = new ArrayList<>();
    @BindView(R.id.banner_view)
    MZBannerView bannerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();
    @BindView(R.id.ll_float_button_container)
    LinearLayout llFloatButtonContainer;
    private int zoneId =1;
    private int zoneState;
    private int zoneType;
    int hasGoodsCategory;


    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }
    static class scrollStateHandler extends Handler {
        NestedScrollView scrollViewContainer;
        ShoppingSpecialFragment fragment;
        int lastY = -1;
        private boolean showFloatButton = true;

        public scrollStateHandler(ShoppingSpecialFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {// 打印 每次 Y坐标 滚动的距离
                //Slog.info(scrollView.getScrollY() + "");
                //    获取到 滚动的 Y 坐标距离
                int scrollY = scrollViewContainer.getScrollY();
                // 如果 滚动 的Y 坐标 的 最后一次 滚动到的Y坐标 一致说明  滚动已经完成
                if (scrollY == lastY) {
                    //  ScrollView滚动完成  处理相应的事件
                } else {
                    // 滚动的距离 和 之前的不相等 那么 再发送消息 判断一次
// 将滚动的 Y 坐标距离 赋值给 lastY
                    lastY = scrollY;
                    this.sendEmptyMessageDelayed(0, 10);
                }
            }
        }


    }
    public static ShoppingSpecialFragment newInstance(int zoneId) {
        ShoppingSpecialFragment fragment = new ShoppingSpecialFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.zoneId = zoneId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_special,container,false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int linkageY = Util.getYOnScreen(linkage);
//                int linkageY_ = linkageY + linkage.getHeight();
//                int containerViewY = Util.getYOnScreen(scrollView);
//
//                SLog.info("linkageY[%s], containerViewY[%s],linkageY_[%s]", linkageY, containerViewY,linkageY_);
//                if (linkageY <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
//                    rvSecondList.setNestedScrollingEnabled(true);
//                } else {
//                    rvSecondList.setNestedScrollingEnabled(false);
//                }
//            }
//        });
        storeListFragment = ShoppingStoreListFragment.newInstance(this);
        linkageFragment = ShoppingLinkageFragment.newInstance(this);

        initView(view);
//        loadData();
        loadTestData();
    }

    private void loadTestData() {
        String responseStr = AssetsUtil.loadText(_mActivity, "tangram/test.json");
        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
        if (ToastUtil.checkError(_mActivity, responseObj)) {
            return;
        }
        updateView(responseObj);
    }

    private void loadData() {


        // 獲取商店首頁信息
        String path = Api.PATH_SHOPPING_ZONE + "/" + zoneId;
        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
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
            zoneState = zoneVo.getInt("zoneState");
            zoneType = zoneVo.getInt("zoneType");

            String appColor = zoneVo.getSafeString("appColor");
            //調試階段試色
            String appLogo = zoneVo.getSafeString("appLogo");
            String zoneName = zoneVo.getSafeString("zoneName");
            tvZoneName.setText(zoneName);
            updateAppColor(appColor);

            String storeTabTitle = zoneVo.getSafeString("storeTabTitle");
            stbStoreTabTitle.setText(storeTabTitle);
            stbTagStore.setText(storeTabTitle);
            String goodsTabTitle = zoneVo.getSafeString("goodsTabTitle");
            stbTagGoods.setText(goodsTabTitle);
            stbGoodsTabTitle.setText(goodsTabTitle);
            tabLayout.getTabAt(LINKAGE_FRAGMENT).setText(goodsTabTitle);
            tabLayout.getTabAt(1).setText(storeTabTitle);


            EasyJSONArray appAdImageList = zoneVo.getArray("appAdImageList");
            setBannerData(appAdImageList);
            EasyJSONArray zoneGoodsVoList = zoneVo.getArray("zoneGoodsVoList");
            EasyJSONArray zoneStoreVoList = zoneVo.getArray("zoneStoreVoList");
            SLog.info("zoneStoreVoList,[%s]",zoneStoreVoList.toString());
            if (zoneStoreVoList != null) {
                SLog.info("設置商店列表數據");
                storeListFragment.setStoreList(zoneStoreVoList);
            }
            EasyJSONArray zoneGoodsCategoryVoList = zoneVo.getArray("zoneGoodsCategoryVoList");
            //商品列表
            if (hasGoodsCategory == Constant.TRUE_INT) {
                if (zoneGoodsCategoryVoList != null) {
                    linkageFragment.setLinkageData(zoneGoodsCategoryVoList);
                }

            } else {
                SLog.info("無類別商品標簽數據");
                linkageFragment.setGoodVoList(zoneGoodsVoList);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }



    private void updateAppColor(String appColor) {
        if ("#FFFFFF".equals(appColor)) {
            tvZoneName.setTextColor(getResources().getColor(R.color.tw_black));
        }
        rlToolBar.setBackgroundColor(Color.parseColor(appColor));
        int[] colors={Color.parseColor(appColor),0xff8B3097, 0xffD14E7A};
        GradientDrawable bannerBackGround = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        clBannerContainer.setBackground(bannerBackGround);
        tabLayout.setTabTextColors(Color.parseColor(appColor),Color.parseColor(StringUtil.addAlphaToColor(appColor,60)));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(appColor));
    }
    private void showFloatButton() {
        if (floatButtonShown){
            return;
        }

        // 防止延遲時序問題導致的空引用異常
        if (llFloatButtonContainer == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity, 0);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llFloatButtonContainer.getLayoutParams();
        layoutParams.rightMargin = Util.dip2px(_mActivity,  -30.25f);
        llFloatButtonContainer.setLayoutParams(layoutParams);
        floatButtonShown = false;
    }





    @Override
    public void onClick(View v) {

    }



    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void initView(View view) {
        initBanner();
        initTabList();
    }

    private void initBanner() {
        //設置banner頁圓角
        bannerView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 5);
            }
        });
        bannerView.setClipToOutline(true);
        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                WebSliderItem webSliderItem = webSliderItemList.get(i);
                String linkType = webSliderItem.linkType;
                SLog.info("i = %d, linkType[%s]", i, linkType);
                if (StringUtil.isEmpty(linkType)) {
                    return;
                }
                switch (linkType) {
                    case "none":
                        // 无操作
                        break;
                    case "url":
                        // 外部鏈接
                        Util.startFragment(ExplorerFragment.newInstance(webSliderItem.linkValue, true));
                        break;
                    case "keyword":
                        // 关键字
                        String keyword = webSliderItem.linkValue;
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("keyword", keyword).toString()));
                        break;
                    case "goods":
                        // 產品
                        int commonId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.parseInt(webSliderItem.linkValue);
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                        break;
                    case "category":
                        // 產品搜索结果页(分类)
                        String cat = webSliderItem.linkValue;
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("cat", cat).toString()));
                        break;
                    case "brandList":
                        // 品牌列表
                        break;
                    case "voucherCenter":
                        // 领券中心
                        break;
                    case "activityUrl":
                        Util.startFragment(H5GameFragment.newInstance(webSliderItem.linkValue, true));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initTabList() {
        titleList = new ArrayList<>();
        titleList.add("-");
        titleList.add("-");
        tabLayout.setOnClickListener(this);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(LINKAGE_FRAGMENT)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(STORE_FRAGMENT)));


        fragmentList.add(linkageFragment);
        fragmentList.add(storeListFragment);

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);

        viewPager.setAdapter(adapter);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height=2000;
        //此處應獲取屏幕高度減標題

        SLog.info("scrollView.getHeight() [%d]",scrollView.getHeight());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int linkageY = Util.getYOnScreen(tabLayout);
//                int containerViewY = Util.getYOnScreen(scrollView);

//                SLog.info("linkageY[%s], containerViewY[%s],", linkageY, containerViewY);
//                if (linkageY <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
//                    rvSecondList.setNestedScrollingEnabled(true);
//                } else {
//                    rvSecondList.setNestedScrollingEnabled(false);
//                }
            }
        });
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
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
    @Override
    public void onCbStartNestedScroll() {
        isScrolling = true;
        lastScrollingTimestamp = System.currentTimeMillis();
        hideFloatButton();
    }

    @Override
    public void onCbStopNestedScroll() {
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
}
