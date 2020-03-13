package com.ftofs.twant.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.NewArrivalsStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.entity.StoreItem;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.StickyStatusCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ActivityPopup;
import com.ftofs.twant.widget.HomeScrollingParentLayout;
import com.ftofs.twant.widget.RoundedDataImageView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.macau.pay.sdk.base.ConstantBase;
import com.orhanobut.hawk.Hawk;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 首頁
 * @author zwm
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener, NestedScrollingCallback,
        BaseQuickAdapter.RequestLoadMoreListener, StickyStatusCallback {
    NewArrivalsStoreAdapter adapter;
    List<StoreItem> storeItemList = new ArrayList<>();

    View floatView;
    LinearLayout stickyViewContainer;
    LinearLayout floatViewContainer;

    HomeScrollingParentLayout containerView;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;
    RecyclerView rvList;

    MZBannerView bannerView;
    float density;

    int goodsCommonCount;
    int wantPostCount;
    int storeCount;

    TextView tvStoreCount;
    TextView tvGoodsCount;
    TextView tvPostCount;

    /**
     * 【商店形像圖】的寬度
     */
    int storeFigureContainerWidth;
    int remainWidth; // 占掉外邊距的寬度


    public static final int ANIM_COUNT = 4;
    ObjectAnimator[] animatorArr = new ObjectAnimator[ANIM_COUNT];

    // 三大口號的高亮背景
    View vwSloganHighLighter;

    LinearLayout llFloatButtonContainer;
    LinearLayout contentView;

    boolean carouselLoaded;
    boolean newArrivalsLoaded;

    /*
    用于記錄滑動狀態，以處理浮動按鈕的顯示與隱藏
     */
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 800; // 浮動按鈕滑動顯示與隱藏效果的延遲時間(毫秒)
    boolean isScrolling = false; // 是否在滑動狀態
    long lastScrollingTimestamp = 0;  // 最近一次滑動的時間戳（毫秒）
    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示

    List<WebSliderItem> webSliderItemList = new ArrayList<>();


    ImageView iconActivityEntrance; // 首頁活動入口圖標
    View vwActivityEntrancePlaceholder;
    LinearLayout btnGotoActivity;

    private ImageView btnPublishWantPost;

    boolean popAd = false;
    String appPopupAdImage;
    String appPopupAdLinkType;
    String appPopupAdLinkValue;
    long showAppPopupAdTimestamp;

    String appIndexNavigationLinkType;
    String appIndexNavigationLinkValue;

    BasePopupView popupViewAd;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        density = Util.getDensity(_mActivity);
        SLog.info("density[%s]", density);
        initInfoSlog();
        Pair<Integer, Integer> screenDimension = Util.getScreenDimemsion(_mActivity);
        // 屏幕寬度減去兩邊32.5dp
        storeFigureContainerWidth = screenDimension.first -  2 * Util.dip2px(_mActivity, 32.5f);
        remainWidth = storeFigureContainerWidth - Util.dip2px(_mActivity, 52);
        SLog.info("storeFigureContainerWidth[%d], remainWidth[%d]", storeFigureContainerWidth, remainWidth);
        btnPublishWantPost = view.findViewById(R.id.btn_publish_want_post);
        //Glide.with(_mActivity).load("file:///android_asset/christmas/publish_want_post_dynamic.gif").centerCrop().into(btnPublishWantPost);

        tvStoreCount = view.findViewById(R.id.tv_store_count);
        tvGoodsCount = view.findViewById(R.id.tv_goods_count);
        tvPostCount = view.findViewById(R.id.tv_post_count);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_category_store, this);
        Util.setOnClickListener(view, R.id.btn_category_goods, this);
        Util.setOnClickListener(view, R.id.btn_category_brand, this);
        Util.setOnClickListener(view, R.id.ll_search_box, this);
        Util.setOnClickListener(view, R.id.btn_message, this);
        btnGotoActivity = view.findViewById(R.id.btn_goto_activity);
        btnGotoActivity.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_add_feedback, this);
        Util.setOnClickListener(view, R.id.btn_goto_top, this);
        Util.setOnClickListener(view,R.id.btn_publish_want_post,this);

        contentView = view.findViewById(R.id.content_view);
        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);

        containerView = view.findViewById(R.id.container_view);
        containerView.setStickyStatusCallback(this);
        containerView.setNestedScrollingCallback(this);
        stickyViewContainer = view.findViewById(R.id.sticky_view_container);
        floatViewContainer = view.findViewById(R.id.float_view_container);

        containerView.post(new Runnable() {
            @Override
            public void run() {
                // 初始化StickyView容器的高度
                int height = stickyViewContainer.getHeight();
                ViewGroup.LayoutParams layoutParams = stickyViewContainer.getLayoutParams();
                layoutParams.height = height;
                stickyViewContainer.setLayoutParams(layoutParams);

                layoutParams = floatViewContainer.getLayoutParams();
                layoutParams.height = height;
                floatViewContainer.setLayoutParams(layoutParams);
            }
        });

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new NewArrivalsStoreAdapter(storeItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreItem item = storeItemList.get(position);
                Util.startFragment(ShopMainFragment.newInstance(item.storeId));
            }
        });
        adapter.setOnItemChildClickListener((adapter, view1, position) -> {
            StoreItem item = storeItemList.get(position);
            if (item.goodsList == null || item.goodsList.size() == 0) {
                return;
            }
            int commonId = 0;
            int id = view1.getId();

            if (id == R.id.goods_image_left_container) {
                commonId = item.goodsList.get(0).id;
            } else if (id == R.id.goods_image_middle_container) {
                commonId = item.goodsList.get(1).id;
            } else if (id == R.id.goods_image_right_container) {
                commonId = item.goodsList.get(2).id;
            }

            Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
        });

        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);

        rvList.setAdapter(adapter);


        bannerView = view.findViewById(R.id.banner_view);
        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int i) {
                WebSliderItem webSliderItem = webSliderItemList.get(i);
                String linkType = webSliderItem.linkType;
                SLog.info("i = %d, linkType[%s]", i, linkType);

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
                        int commonId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.valueOf(webSliderItem.linkValue);
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
                    case "postId":
                        int postId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(PostDetailFragment.newInstance(postId));
                        break;
                    case "shopping":
                        Util.startFragment(ShoppingSessionFragment.newInstance());
                        break;
                    case "wantPost":
                        SearchPostParams searchPostParams = new SearchPostParams();
//                        searchPostParams.keyword = "";
                        ((MainFragment)getParentFragment()).showHideFragment(MainFragment.CIRCLE_FRAGMENT);
//                        Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
                        break;
                    default:
                        break;
                }
            }
        });


        vwSloganHighLighter = view.findViewById(R.id.vw_slogan_highlighter);

        long animDuration = 1500;
        int blockWidth = Util.getScreenDimemsion(_mActivity).first / 3; // 分3塊
        animatorArr[0] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",-blockWidth,0).setDuration(animDuration);
        animatorArr[1] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",0,blockWidth).setDuration(animDuration);
        animatorArr[2] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",blockWidth,2*blockWidth).setDuration(animDuration);
        animatorArr[3] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",2*blockWidth,3*blockWidth).setDuration(animDuration);

        for (int i = 0; i < ANIM_COUNT; i++) {
            int finalI = i;
            animatorArr[i].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int index = (finalI + 1) % ANIM_COUNT;
                    // SLog.info("index[%d]", index);
                    vwSloganHighLighter.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animatorArr[index].start();
                        }
                    }, 1000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        vwSloganHighLighter.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vwSloganHighLighter.getLayoutParams();
                layoutParams.width = blockWidth;
                vwSloganHighLighter.setLayoutParams(layoutParams);
                vwSloganHighLighter.setVisibility(View.VISIBLE);
                animatorArr[0].start();
            }
        }, 500);

        iconActivityEntrance = view.findViewById(R.id.icon_activity_entrance);
        vwActivityEntrancePlaceholder = view.findViewById(R.id.vw_activity_entrance_placeholder);
    }

    private void initInfoSlog() {
        SLog.info("MPayPackageName[%s],ConstantBase.mpay_uat_packetname[%s]",ConstantBase.MPayPackageName,ConstantBase.mpay_uat_packetname);
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        SLog.info("carouselLoaded[%s], newArrivalsLoaded[%s]", carouselLoaded, newArrivalsLoaded);
        // 加載輪播圖片
        if (!carouselLoaded) {
            loadCarousel();
        }

        // 加載最新入駐
        if (!newArrivalsLoaded) {
            loadNewArrivals(currPage + 1);
        }

        bannerView.start();
        //這裏可以保證每次覆蓋安裝時獲取當前用戶的身份數據
        ((TwantApplication)getActivity().getApplication()).updateCurrMemberInfo();
        // 顯示活動
        showPopupAd();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        bannerView.pause();
    }


    /**
     * 點擊導航欄活動按鈕
     */
    private void gotoActivity() {
        SLog.info("appIndexNavigationLinkType[%s]", appIndexNavigationLinkType);
        if ("activity".equals(appIndexNavigationLinkType)) { // 活動主頁
            Util.startFragment(H5GameFragment.newInstance(appIndexNavigationLinkValue, ""));
        } else if ("promotion".equals(appIndexNavigationLinkType)) { // 購物專場
            SLog.info("跳轉到購物專場");
            Util.startFragment(ShoppingSessionFragment.newInstance());
        }

    }

    private void showPopupAd() {
        // 每次resume時都顯示一次
        long resumeTimestamp = ((MainActivity) _mActivity).resumeTimestamp;
        if (popAd && showAppPopupAdTimestamp != resumeTimestamp ) {
            if (StringUtil.isEmpty(appPopupAdImage)) {
                return;
            }
            if (popupViewAd == null) {
                popupViewAd = new XPopup.Builder(_mActivity)
                        .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                        .dismissOnTouchOutside(true) // 点击外部是否关闭弹窗，默认为true
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ActivityPopup(_mActivity, appPopupAdImage,
                                appPopupAdLinkType, appPopupAdLinkValue));
            }

            if (popupViewAd.isDismiss()) {
                popupViewAd.show();
            }

            showAppPopupAdTimestamp = resumeTimestamp;  // 記錄最近一次顯示的時間戳
            popAd = false;

            String appVersion = BuildConfig.VERSION_NAME.replace(".", "_");
            SLog.info("appVersion[%s]", appVersion);

            String key = String.format(SPField.FIELD_POPUP_AD_STATUS_APP_VER, appVersion);
            Hawk.put(key, Constant.TRUE_INT);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_category_store) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.STORE.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
        } else if (id == R.id.btn_category_goods) {
            Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                    EasyJSONObject.generate("keyword", "").toString()));
        } else if (id == R.id.btn_category_brand) {
            SearchPostParams searchPostParams = new SearchPostParams();
            searchPostParams.keyword = "";
            Util.startFragment(CircleFragment.newInstance(true, searchPostParams));
        } else if (id == R.id.ll_search_box) {
            Util.startFragment(CategoryFragment.newInstance(SearchType.STORE, null));
        } else if (id == R.id.btn_test) {
            SLog.info("Config.PROD[%s]", Config.PROD);
            if (!Config.PROD) {
                Util.startFragment(TestFragment.newInstance());
//                    getFragmentManager().getFragments().size()
                // Util.startFragment(RegisterConfirmFragment.newInstance("0086", "13417785707", 10));
                // Util.startFragment(SoftToolPaneLayoutFragment.newInstance());
//                Util.startFragment(TestFragment.newInstance());
            }
        } else if (id == R.id.btn_add_feedback) {
            Util.startFragment(CommitFeedbackFragment.newInstance());
        } else if (id == R.id.btn_goto_top) {
            containerView.scrollTo(0, 0);
            rvList.scrollToPosition(0);
        } else if (id == R.id.btn_goto_activity) {
            // Util.startFragment(H5GameFragment.newInstance("https://www.twant.com/web/want/activity/christmas/appInfo", true));
            // Util.startChristmasFragment();
            gotoActivity();
        }else if (id == R.id.btn_publish_want_post) {
            Util.startFragment(AddPostFragment.newInstance(false));
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%d], id[%d], extra[%s]", type, id, extra);
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

    private void showFloatButton() {
        if (floatButtonShown ){
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
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested, rvListHeight[%d]", rvList.getHeight());

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadNewArrivals(currPage + 1);
    }

    @Override
    public void changeStickyStatus(boolean isAdd, Object object) {
        SLog.info("call, isAdd[%s]", isAdd);
        if (isAdd) {  // 將View浮動到頂部
            floatView = (View) object;
            floatViewContainer.addView(floatView);
            floatViewContainer.setVisibility(View.VISIBLE);
        } else {  // 將View取消浮動
            floatViewContainer.removeView(floatView);
            floatViewContainer.setVisibility(View.GONE);
        }
    }

    public void resetLoaded() {
        carouselLoaded = false;
        newArrivalsLoaded = false;
        currPage=0;
    }

    public static class BannerViewHolder implements MZViewHolder<WebSliderItem> {
        private ImageView mImageView;

        public static final int GOODS_IMAGE_COUNT = 3;
        ImageView imgDesktop;
        RoundedDataImageView[] goodsImageArr = new RoundedDataImageView[GOODS_IMAGE_COUNT];
        TextView[] goodsPriceArr = new TextView[GOODS_IMAGE_COUNT];

        List<WebSliderItem> webSliderItemList;

        public BannerViewHolder(List<WebSliderItem> webSliderItemList) {
            this.webSliderItemList = webSliderItemList;
        }

        public void setGoodsImageVisibility(int visibility,int count) {
            for (int i = 0; i < count; ++i) {
                goodsImageArr[i].setVisibility(visibility);
                goodsPriceArr[i].setVisibility(visibility);
            }
        }

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.carousel_banner_item,null);
            mImageView = view.findViewById(R.id.img_banner);

            imgDesktop = view.findViewById(R.id.img_goods_desktop);
            goodsImageArr[0] = view.findViewById(R.id.goods_image1);
            goodsImageArr[1] = view.findViewById(R.id.goods_image2);
            goodsImageArr[2] = view.findViewById(R.id.goods_image3);

            goodsPriceArr[0] = view.findViewById(R.id.tv_goods_price1);
            goodsPriceArr[1] = view.findViewById(R.id.tv_goods_price2);
            goodsPriceArr[2] = view.findViewById(R.id.tv_goods_price3);


            for (int i = 0; i < GOODS_IMAGE_COUNT; i++) {
                goodsImageArr[i].setOnClickListener(v -> {
                    int commonId = (int) ((RoundedDataImageView) v).getCustomData();
                    SLog.info("commonId[%d]", commonId);
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                });
            }
            return view;
        }

        @Override
        public void onBind(Context context, int position, WebSliderItem webSliderItem) {
                // 数据绑定
                String imageUrl = StringUtil.normalizeImageUrl(webSliderItem.image);
                Glide.with(context).load(imageUrl).centerCrop().into(mImageView);
                //SLog.info("webSliderItem.linkType，[%s]",webSliderItem.linkType);

                imgDesktop.setVisibility(View.GONE);
                setGoodsImageVisibility(View.GONE,GOODS_IMAGE_COUNT);
                if(!webSliderItem.goodsCommons.equals("[]")){
                    String goodsCommons = webSliderItem.goodsCommons;
                    EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);
                    //SLog.info("goodsArray%d",goodsArray.length());
                    for (int i=0;i<goodsArray.length();i++) {
                        EasyJSONObject goods;
                        try {
                            goods = goodsArray.getObject(i);
                            int commonId = goods.getInt("commonId");
                            float price = (float) goods.getDouble("goodsPrice0");
                            String goodsImage = StringUtil.normalizeImageUrl(goods.getSafeString("goodsImage"));
                            if (StringUtil.isEmpty(goodsImage)) {
                                goodsImageArr[i].setVisibility(View.GONE);
                            }
                            Glide.with(context).load(goodsImage).centerCrop().into(goodsImageArr[i]);
                            goodsImageArr[i].setCustomData(commonId);
                            goodsPriceArr[i].setText(StringUtil.formatPrice(context, price, 0,false));
                            goodsPriceArr[i].setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        }

                    if (goodsArray.length() > 0) {
                        imgDesktop.setVisibility(View.VISIBLE);
                        setGoodsImageVisibility(View.VISIBLE,goodsArray.length());
                    }
                }

            }
        }
    }


    /**
     * 加載輪播圖片
     */
    private void loadCarousel() {
        Api.getUI(Api.PATH_HOME_CAROUSEL, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    goodsCommonCount = responseObj.getInt("datas.goodsCommonCount");
                    // goodsCommonCount = 9999;
                    if (goodsCommonCount > 0) {
                        tvGoodsCount.setText(formatCount(goodsCommonCount));
                        tvGoodsCount.setVisibility(View.VISIBLE);
                    }

                    wantPostCount = responseObj.getInt("datas.wantPostCount");
                    // wantPostCount = 10000;
                    if (wantPostCount > 0) {
                        tvPostCount.setText(formatCount(wantPostCount));
                        tvPostCount.setVisibility(View.VISIBLE);
                    }

                    storeCount = responseObj.getInt("datas.storeCount");
                    // storeCount = 11001;
                    if (storeCount > 0) {
                        tvStoreCount.setText(formatCount(storeCount));
                        tvStoreCount.setVisibility(View.VISIBLE);
                    }

                    EasyJSONArray itemList = responseObj.getSafeArray("datas.webSliderItem");
                    SLog.info(itemList.toString());
                    webSliderItemList.clear();
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;

                        String goodsCommonsStr;
                        Object goodsCommons = itemObj.get("goodsCommons");
                        if (Util.isJsonNull(goodsCommons)) {
                            goodsCommonsStr = "[]";
                        } else if ("null".equals(goodsCommons.toString())) {
                            goodsCommonsStr = "[]";
                        } else {
                            goodsCommonsStr = goodsCommons.toString();
                        }

                        WebSliderItem webSliderItem = new WebSliderItem(
                                itemObj.getSafeString("image"),
                                itemObj.getSafeString("linkType"),
                                itemObj.getSafeString("linkValue"),
                                itemObj.getSafeString("goodsIds"),
                                goodsCommonsStr);
                        webSliderItemList.add(webSliderItem);
                    }

                    // 设置数据
                    bannerView.setPages(webSliderItemList, new MZHolderCreator<BannerViewHolder>() {
                        @Override
                        public BannerViewHolder createViewHolder() {
                            return new BannerViewHolder(webSliderItemList);
                        }
                    });
                    bannerView.start();


                    // 获取活动入口按钮信息

                    // 彈出廣告
                    String enableAppPopupAdStr;
                    Object tmpObj = responseObj.get("datas.enableAppPopupAd");
                    if (Util.isJsonNull(tmpObj)) {
                        enableAppPopupAdStr = "0";
                    } else {
                        enableAppPopupAdStr = tmpObj.toString();
                    }
                    int enableAppPopupAd = Integer.valueOf(enableAppPopupAdStr);
                    if (enableAppPopupAd == Constant.TRUE_INT) {
                        // 0 -- 当前APP版本只彈一次 1 -- 每次訪問彈出
                        String enableEveryTimeAppPopupAdStr;
                        tmpObj = responseObj.get("datas.enableEveryTimeAppPopupAd");
                        if (Util.isJsonNull(tmpObj)) {
                            enableEveryTimeAppPopupAdStr = "0";
                        } else {
                            enableEveryTimeAppPopupAdStr = tmpObj.toString();
                        }
                        int enableEveryTimeAppPopupAd = Integer.valueOf(enableEveryTimeAppPopupAdStr);

                        if (enableEveryTimeAppPopupAd == Constant.TRUE_INT) {
                            popAd = true;
                        } else { // 查看当前版本是否有弹出
                            String appVersion = BuildConfig.VERSION_NAME.replace(".", "_");
                            SLog.info("appVersion[%s]", appVersion);

                            String key = String.format(SPField.FIELD_POPUP_AD_STATUS_APP_VER, appVersion);
                            int popupCount = Hawk.get(key, Constant.FALSE_INT);
                            popAd = popupCount == Constant.FALSE_INT;
                        }

                        if (popAd) {
                            tmpObj = responseObj.get("datas.appPopupAdImage");
                            if (Util.isJsonNull(tmpObj)) {
                                appPopupAdImage = "";
                            } else {
                                appPopupAdImage = tmpObj.toString();
                            }

                            tmpObj = responseObj.get("datas.appPopupAdLinkType");
                            if (Util.isJsonNull(tmpObj)) {
                                appPopupAdLinkType = "";
                            } else {
                                appPopupAdLinkType = tmpObj.toString();
                            }

                            tmpObj = responseObj.get("datas.appPopupAdLinkValue");
                            if (Util.isJsonNull(tmpObj)) {
                                appPopupAdLinkValue = "";
                            } else {
                                appPopupAdLinkValue = tmpObj.toString();
                            }

                            SLog.info("appPopupAdImage[%s], appPopupAdLinkType[%s], appPopupAdLinkValue[%s]",
                                    appPopupAdImage, appPopupAdLinkType, appPopupAdLinkValue);
                            showPopupAd();
                        }
                    }


                    // 導航欄
                    String enableAppIndexNavigationStr;
                    tmpObj = responseObj.get("datas.enableAppIndexNavigation");
                    if (Util.isJsonNull(tmpObj)) {
                        enableAppIndexNavigationStr = "0";
                    } else {
                        enableAppIndexNavigationStr = tmpObj.toString();
                    }
                    int enableAppIndexNavigation = Integer.valueOf(enableAppIndexNavigationStr);
                    boolean activityEnable = (enableAppIndexNavigation == Constant.TRUE_INT);
                    if (activityEnable) {
                        String appIndexNavigationImage;
                        tmpObj = responseObj.get("datas.appIndexNavigationImage");
                        if (Util.isJsonNull(tmpObj)) {
                            appIndexNavigationImage = "";
                        } else {
                            appIndexNavigationImage = tmpObj.toString();
                        }

                        tmpObj = responseObj.get("datas.appIndexNavigationLinkType");
                        if (Util.isJsonNull(tmpObj)) {
                            appIndexNavigationLinkType = "";
                        } else {
                            appIndexNavigationLinkType = tmpObj.toString();
                        }

                        tmpObj = responseObj.get("datas.appIndexNavigationLinkValue");
                        if (Util.isJsonNull(tmpObj)) {
                            appIndexNavigationLinkValue = "";
                        } else {
                            appIndexNavigationLinkValue = tmpObj.toString();
                        }

                        // appIndexNavigationImage = "https://gfile.oss-cn-hangzhou.aliyuncs.com/takewant/7d78744d5d1dffc96022bba47123b0a8.png";
                        appIndexNavigationImage = StringUtil.normalizeImageUrl(appIndexNavigationImage);
                        SLog.info("appIndexNavigationImage[%s], appIndexNavigationLinkType[%s], appIndexNavigationLinkValue[%s]",
                                appIndexNavigationImage, appIndexNavigationLinkType, appIndexNavigationLinkValue);
                        Glide.with(_mActivity).load(appIndexNavigationImage).into(iconActivityEntrance);
                    }
                    vwActivityEntrancePlaceholder.setVisibility(activityEnable ? View.VISIBLE : View.GONE);
                    btnGotoActivity.setVisibility(activityEnable ? View.VISIBLE : View.GONE);

                    carouselLoaded = true;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 加載最新入駐
     */
    private void loadNewArrivals(int page) {
        SLog.info("loadNewArrivals");

        String token = User.getToken();
        SLog.info("token[%s]", token);

        try {
            EasyJSONObject params = EasyJSONObject.generate("page", page);
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("params[%s]", params);
            Api.getUI(Api.PATH_NEW_ARRIVALS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    adapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("PATH_NEW_ARRIVALS, responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        SLog.info("Error!responseObj is invalid");
                        adapter.loadMoreFail();
                        return;
                    }

                    try {
                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            adapter.loadMoreEnd();
                            adapter.setEnableLoadMore(false);
                        }


                        EasyJSONArray storeList = responseObj.getSafeArray("datas.storeList");
                        SLog.info("storeList size[%d]", storeList.length());

                        // 如果是加載第一頁的數據，先清除舊數據
                        if (page == 1) {
                            storeItemList.clear();
                        }

                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            StoreItem storeItem = new StoreItem();

                            // 獲取店鋪Id
                            storeItem.storeId = store.getInt("storeId");

                            // 設置店鋪名稱
                            storeItem.storeName = store.getSafeString("storeName");

                            // 設置商店類別
                            storeItem.storeClass = store.getSafeString("className");

                            // 店鋪形象圖
                            storeItem.storeFigureImage = StringUtil.normalizeImageUrl(store.getSafeString("storeFigureImage"));
                            EasyJSONArray goodsList= store.getSafeArray("goodsList");
                            if (goodsList.length() > 0) {
                                // 商店的3個產品展示
                                for (Object object2 : goodsList) {
                                    EasyJSONObject goodsObject = (EasyJSONObject) object2;
                                    Goods goods = new Goods();

                                    goods.imageUrl = goodsObject.getSafeString("goodsImage");
                                    goods.id = goodsObject.getInt("commonId");

                                    storeItem.goodsList.add(goods);
                                }
                            }
                            storeItemList.add(storeItem);
                        }

                        SLog.info("storeItemList.size[%d]", storeItemList.size());

                        if (!hasMore && storeItemList.size() > 1) {
                            // 如果全部加載完畢，添加加載完畢的提示
                            StoreItem item = new StoreItem();
                            item.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                            storeItemList.add(item);
                        }

                        adapter.loadMoreComplete();
                        newArrivalsLoaded = true;
                        adapter.setNewData(storeItemList);
                        currPage++;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
    }

    private String formatCount(int count) {
        /*
        0~9999： 直接顯示具體數量；
        9999~11000： 11K；
        11001~12000：12K；

        以此類推進行顯示；
        數量超過1萬後 以千位單位進行向上取整，單位為大寫 K；
         */
        return String.valueOf(count);
    }
}


