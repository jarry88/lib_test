package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.google.android.material.tabs.TabLayout;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.LinkageSecondaryAdapter;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
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
    @BindView(R.id.btn_goods)
    SimpleTabButton stbTagGoods;
    @BindView(R.id.btn_store)
    SimpleTabButton stbTagStore;
    @BindView(R.id.shopping_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.shopping_viewpager)
    ViewPager viewPager;
    @BindView(R.id.linkage)
    LinkageRecyclerView linkage;
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
    private Typeface typeFace;
    private RecyclerView rvSecondList;
    private RecyclerView rvPrimaryList;
    List<ElemeGroupedItem> items = new ArrayList<>();
    private int containerViewHeight;
    private int tabHeight = 0;
    public boolean linkageShow =false;


    @OnClick(R.id.btn_goods)
    void showGoodsListInfo() {
        SLog.info("showGoodsListInfo");
    }
    @OnClick(R.id.btn_store)
    void showStoresListInfo() {
        SLog.info("showStoresListInfo");
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
    @BindView(R.id.ll_banner_container)
    LinearLayout llBanner;


    List<ElemeGroupedItem> storeItems = new ArrayList<>();
    @BindView(R.id.banner_view)
    MZBannerView bannerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();
    @BindView(R.id.ll_float_button_container)
    LinearLayout llFloatButtonContainer;
    private int zoneId =20;
    private int zoneState;
    private int zoneType;
    int hasGoodsCategory;


    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }
    @OnClick(R.id.btn_goto_cart)
    void gotoCart() {
        int userId = User.getUserId();
        if (userId > 0) {
            Util.startFragment(CartFragment.newInstance(true));
        } else {
            Util.showLoginFragment();
        }
    }
    @OnClick(R.id.btn_goto_top)
    void gotoTop(){
        scrollView.scrollTo(0,0);
        if (linkage.getVisibility() == View.VISIBLE) {
            linkage.scrollTo(0,0);
            linkage.getPrimaryAdapter().setSelectedPosition(0);
            rvSecondList.scrollToPosition(0);
        }
    }

    public boolean getScrollEnale() {
        int Y=Util.getYOnScreen(viewPager);
        if (Y >= Util.dip2px(_mActivity, 88)) {
            return true;
        }
        return false;
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
        initLinkage();
//        loadData();
        loadTestData();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (tabHeight == 0) {
            tabHeight = tabLayout.getHeight();
            SLog.info("tabHeight[%d]", tabHeight);
        }
        if (linkage.getVisibility() == View.VISIBLE) {
            linkageShow = true;
        }else {
            linkageShow = false;
        }
        if (hasGoodsCategory==1) {
            SLog.info("primary[%s] width [%d]", rvPrimaryList.getVisibility(),rvPrimaryList.getWidth());

            if (containerViewHeight == 0) {
                containerViewHeight = scrollView.getHeight();
                SLog.info("containerViewHeight[%d]", containerViewHeight);

                ViewGroup.LayoutParams layoutParams = rvSecondList.getLayoutParams();
                LinearLayout .LayoutParams layoutParams1 = (LinearLayout.LayoutParams) rvPrimaryList.getLayoutParams();
            layoutParams.height = containerViewHeight-tabLayout.getHeight();
                layoutParams1.height = containerViewHeight;
//                layoutParams1.weight = Util.dip2px(_mActivity,80);
                rvSecondList.setLayoutParams(layoutParams);

                rvPrimaryList.setLayoutParams(layoutParams1);
            }

            if (tabLayout.getSelectedTabPosition() == 0) {
                rvPrimaryList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadTestData() {
        String responseStr = AssetsUtil.loadText(_mActivity, "tangram/test.json");
        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
        if (ToastUtil.checkError(_mActivity, responseObj)) {
            return;
        }
//        updateView(responseObj);
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
            zoneState = zoneVo.getInt("zoneState");
            zoneType = zoneVo.getInt("zoneType");

            String appColor = zoneVo.getSafeString("appColor");
            //調試階段試色
            String appLogo = zoneVo.getSafeString("appLogo");
            String zoneName = zoneVo.getSafeString("zoneName");
            tvZoneName.setText(zoneName);
            updateAppColor(appColor);

            String storeTabTitle = zoneVo.getSafeString("storeTabTitle");
            stbTagStore.setText(storeTabTitle);
            String goodsTabTitle = zoneVo.getSafeString("goodsTabTitle");
            stbTagGoods.setText(goodsTabTitle);
            tabLayout.getTabAt(LINKAGE_FRAGMENT).setText(goodsTabTitle);
            tabLayout.getTabAt(1).setText(storeTabTitle);


            EasyJSONArray appAdImageList = zoneVo.getArray("appAdImageList");

            if (appAdImageList != null) {
                setBannerData(appAdImageList);
            }
            EasyJSONArray zoneGoodsVoList = zoneVo.getArray("zoneGoodsVoList");
            EasyJSONArray zoneStoreVoList = zoneVo.getArray("zoneStoreVoList");
//            SLog.info("zoneStoreVoList,[%s]",zoneStoreVoList.toString());
            if (zoneStoreVoList != null) {
                SLog.info("設置商店列表數據");
                storeListFragment.setStoreList(zoneStoreVoList);
            } else {
                tabLayout.setVisibility(View.GONE);
                tabHeight=0;
            }
            EasyJSONArray zoneGoodsCategoryVoList = zoneVo.getArray("zoneGoodsCategoryVoList");
            //商品列表
            if (hasGoodsCategory == Constant.TRUE_INT) {
                linkageShow = true;
                if (zoneGoodsCategoryVoList != null) {

                    updateLinkage(zoneGoodsCategoryVoList);
                }

            } else {
                linkageShow = false;
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
        llBanner.setBackground(bannerBackGround);
        tabLayout.setTabTextColors(Color.parseColor(StringUtil.addAlphaToColor(appColor,60)),Color.parseColor(appColor));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(appColor));
    }
    public void showFloatButton() {
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

    public void hideFloatButton() {
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
        linkage = view.findViewById(R.id.linkage);
        initLinkage();
        initBanner();
        initTabList();
    }

    private void initLinkage() {
        rvSecondList = linkage.findViewById(R.id.rv_secondary);
        rvPrimaryList = linkage.findViewById(R.id.rv_primary);
        rvSecondList.setBackgroundColor(getResources().getColor(R.color.tw_white));
        rvPrimaryList.setBackgroundColor(Color.parseColor("#fff3f3f3"));

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvSecondList.getLayoutParams();
        layoutParams.height = Util.getScreenDimension(_mActivity).second - Util.dip2px(_mActivity,44)-tabHeight;
//        rvSecondList.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) rvPrimaryList.getLayoutParams();
//        layoutParams1.height =parentFragment.scrollView.getHeight();
        layoutParams1.height =layoutParams.height ;
        rvPrimaryList.setLayoutParams(layoutParams1);

        SLog.info("isNestedScrollingEnabled[%s]", rvSecondList.isNestedScrollingEnabled());
        SLog.info("scrollView.getHeight() [%d]",scrollView.getHeight());


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int linkageY = Util.getYOnScreen(linkage);
                int viewPagerY = Util.getYOnScreen(viewPager);
                int linkageY_ = linkageY + linkage.getHeight();
                int containerViewY = Util.getYOnScreen(scrollView);

                // SLog.info("viewPagerY[%s],linkageY[%s], containerViewY[%s],tablayout[%s]",viewPagerY, linkageY, containerViewY,tabHeight );

                if (!linkageShow) {
                    boolean openChildScroll=viewPagerY <= containerViewY+tabHeight;
                    // 如果列表滑动到顶部，则启用嵌套滚动
                    SLog.info("設置子頁面滾動[%s]",openChildScroll);
                    linkageFragment.setNestedScrollingEnabled(openChildScroll);
                    storeListFragment.setNestedScrollingEnabled(openChildScroll);

                } else {
                    boolean openLinkageScroll=linkageY <= containerViewY+tabHeight;
                    SLog.info("設置二級滾動[%s]",openLinkageScroll);
                    rvSecondList.setNestedScrollingEnabled(openLinkageScroll);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvSecondList.getLayoutParams();
                    layoutParams.height = Util.getScreenDimension(_mActivity).second -tabHeight;
                    rvSecondList.setLayoutParams(layoutParams);
                }
                SLog.info("viewPagerY[%s],linkageY[%s], containerViewY[%s],tablayout[%s]",viewPagerY, linkageY, containerViewY,tabHeight );

            }
        });
        rvSecondList.setNestedScrollingEnabled(false);
//        rvSecondList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                SLog.info("__newState[%d]", newState);
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    int linkageY_ = Util.getYOnScreen(linkage) + linkage.getHeight();
//                    SLog.info("linkageY_[%s],height", linkageY_,linkage.getHeight());
////                    hideFloatButton();
//                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
//
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
////                    showFloatButton();
//                }
//            }
//        });

    }
    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;
        private int backgroundColor;
        private Drawable default_drawbg;

        public ElemePrimaryAdapterConfig(Context context) {
            this.mContext = context;
        }

        public void setTwColor(int twColor) {
            this.twColor = twColor;
        }

        private int twColor = R.color.tw_black;

        @Override
        public void setContext(Context context) {
//            mContext = ;
        }

        public void setBackgroundColor(int color, Drawable bg) {
            backgroundColor = color;
            default_drawbg = bg;
        }


        @Override
        public int getLayoutId() {
            return R.layout.adapter_linkage_primary;
        }

        @Override
        public int getGroupTitleViewId() {
            return R.id.tv_group;
        }

        @Override
        public int getRootViewId() {
            return R.id.layout_group;
        }

        @Override
        public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
            TextView tvCategory = ((TextView) holder.mGroupTitle);

            tvCategory.setText(title);

            View blue = holder.mLayout.findViewById(R.id.view_border);
            blue.setVisibility(View.GONE);
            if (selected) {
                tvCategory.setTextColor(Color.parseColor("#2A292A"));
                tvCategory.setBackgroundColor(Color.parseColor("#ffffffff"));
                holder.mLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
            } else {
                tvCategory.setTextColor(Color.parseColor("#999999"));

                holder.mLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
                tvCategory.setBackgroundColor(Color.argb(0, 0, 0, 0));
            }
            tvCategory.setTextColor(ContextCompat.getColor(mContext,
                    selected ? R.color.tw_black : R.color.tw_black));
            tvCategory.setTypeface(Typeface.defaultFromStyle(selected ?Typeface.BOLD:Typeface.NORMAL));
//            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvCategory.setMaxLines(2);
            tvCategory.setMaxEms(8);
            tvCategory.setEllipsize(TextUtils.TruncateAt.END);
            tvCategory.setFocusable(selected);
            tvCategory.setFocusableInTouchMode(selected);
//            tvTitle.setMarqueeRepeatLimit(selected ? -1 : 0);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
//            ToastUtil.error(mContext, title);
        }
    }
    private static class ElemeSecondaryAdapterConfig implements ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private final Typeface typeFace;
        private Context mContext;

        public ElemeSecondaryAdapterConfig(Typeface typeface,Context context) {
            this.typeFace = typeface;
            this.mContext =context;
        }

        @Override
        public void setContext(Context context) {
//            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_shopping_zone_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return R.layout.black_layout;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return 2;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder secondHolder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            try {
                ((TextView) secondHolder.getView(R.id.tv_goods_name)).setText(item.info.getTitle());
                ((TextView) secondHolder.getView(R.id.tv_goods_comment)).setText(item.info.getContent());
                TextView tvPrice=secondHolder.getView(R.id.tv_goods_price);
                tvPrice.setText(StringUtil.formatPrice(mContext, Double.valueOf(item.info.getCost().substring(1)), 0, true));
                tvPrice.setTypeface(typeFace);
                UiUtil.toPriceUI(tvPrice,12);

                if (item.info.show) {
                    TextView tvOriginalPrice=secondHolder.getView(R.id.tv_goods_original_price);
                    tvOriginalPrice.setVisibility(View.VISIBLE);
                    tvOriginalPrice.setText(StringUtil.formatPrice(mContext, item.info.getOriginal(), 0, true));
                    tvOriginalPrice.setTypeface(typeFace);
                    // 原價顯示刪除線
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                secondHolder.getView(R.id.sw_price).setVisibility( View.GONE);
//                ((SlantedWidget) holder.getView(R.id.sw_price)).setDiscountInfo(mContext, item.info.getDiscount(), item.info.getOriginal());
                ImageView imageView = secondHolder.getView(R.id.img_goods_item);
                Glide.with(mContext).load(item.info.getImgUrl()).centerCrop().into(imageView);
                secondHolder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                    Util.startFragment(GoodsDetailFragment.newInstance(item.info.commonId, 0));
                });

                secondHolder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                    if (!item.info.hasStorage()) {
                        ToastUtil.error(mContext,"該產品已售罄，看看其他的吧");
                        return;
                    }
                    new XPopup.Builder(mContext)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SpecSelectPopup(mContext, Constant.ACTION_ADD_TO_CART, item.info.commonId, null, null, null, 1, null, null, 0, 2, null))
                            .show();
                    //TODO
                });
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

        }
    }



    private void updateLinkage(EasyJSONArray zoneGoodsCategoryVoList) {
        try {
            SLog.info("設置二級聯動列表數據");
            items.clear();
            for (Object object : zoneGoodsCategoryVoList) {
                EasyJSONObject categoryData = (EasyJSONObject) object;

                String groupName = categoryData.getSafeString("categoryName");
                EasyJSONArray goodsList = categoryData.getSafeArray("zoneGoodsVoList");
                ElemeGroupedItem category = new ElemeGroupedItem(true, groupName);
                if (goodsList == null) {
                    continue;
                }
                items.add(category);
                for (Object object1 : goodsList) {
                    EasyJSONObject goods = (EasyJSONObject) object1;
                    String goodsName = goods.getSafeString("goodsName");
                    String goodsImage = goods.getSafeString("goodsImage");
                    int commonId = goods.getInt("commonId");
                    String jingle = goods.getSafeString("goodsFullSpecs");
                    double price;
                    int appUsable = goods.getInt("appUsable");
                    if (appUsable > 0) {
                        price = goods.getDouble("appPriceMin");
                    } else {
                        price = goods.getDouble("batchPrice0");
                    }

                    double batchPrice0 = goods.getDouble("batchPrice0");
                    double promotionDiscountRate = goods.getDouble("promotionDiscountRate");

                    int goodsStorage=goods.getInt("goodsStorage");
                    ElemeGroupedItem.ItemInfo goodsInfo = new ElemeGroupedItem.ItemInfo(goodsName, groupName, jingle, StringUtil.normalizeImageUrl(goodsImage),
                            StringUtil.formatPrice(getContext(), price, 0), promotionDiscountRate, batchPrice0, commonId, appUsable > 0,goodsStorage);
                    ElemeGroupedItem item1 = new ElemeGroupedItem(goodsInfo);
                    items.add(item1);
                }

            }
            Typeface typeface =AssetsUtil.getTypeface(_mActivity,"fonts/din_alternate_bold.ttf");
            ElemeSecondaryAdapterConfig secondaryAdapterConfig = new ElemeSecondaryAdapterConfig(typeface,getContext());
            ElemePrimaryAdapterConfig primaryAdapterConfig=new ElemePrimaryAdapterConfig(getContext());
            linkage.init(items, primaryAdapterConfig, secondaryAdapterConfig);
            viewPager.setVisibility(View.GONE);
            linkage.setVisibility(View.VISIBLE);


        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    public void showSpecSelectPopup(int commonId) {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, Constant.ACTION_ADD_TO_CART, commonId, null, null, null, 1, null, null, 0, 2, null))
                .show();
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
        int heightPadding = Util.getScreenDimension(_mActivity).first * 9 / 16 - Util.dip2px(_mActivity, 16);
        bannerView.setIndicatorPadding(0,heightPadding,0,0);
        UiUtil.addBannerPageClick(bannerView,webSliderItemList);
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
        layoutParams.height=Util.getScreenDimension(getContext()).second-Util.dip2px(_mActivity,88);
        //此處應獲取屏幕高度減標題

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }
    private void setBannerData(EasyJSONArray discountBannerList) {
        ToastUtil.success(_mActivity,"設置banner數據" );
        SLog.info("bannerListLength %d",discountBannerList.length());

        try {
            for (Object object : discountBannerList) {
                String imageUrl = String.valueOf(object);
                WebSliderItem webSliderItem = new WebSliderItem(StringUtil.normalizeImageUrl(imageUrl), null, null, null, "[]");
                webSliderItemList.add(webSliderItem);
                // 设置数据
//                bannerView.setPages(webSliderItemList, (MZHolderCreator<BannerViewHolder>) () -> new BannerViewHolder(webSliderItemList));

//                carouselLoaded = true;
            }
            if (discountBannerList.length() == 1) {
                SLog.info("here0");
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
