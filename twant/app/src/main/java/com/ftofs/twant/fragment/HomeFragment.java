package com.ftofs.twant.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.DoubleElevenPopup;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.NestedScrollingParent2Layout;
import com.lxj.xpopup.XPopup;
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
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener, NestedScrollingCallback {
    LinearLayout llNewArrivalsContainer;
    MZBannerView bannerView;
    float density;

    int goodsCommonCount;
    int wantPostCount;
    int storeCount;

    TextView tvStoreCount;
    TextView tvGoodsCount;
    TextView tvPostCount;

    /**
     * 【店鋪形像圖】的寬度
     */
    int storeFigureContainerWidth;
    int remainWidth; // 占掉外邊距的寬度


    public static final int ANIM_COUNT = 4;
    ObjectAnimator[] animatorArr = new ObjectAnimator[ANIM_COUNT];

    // 三大口號的高亮背景
    View vwSloganHighLighter;

    LinearLayout llFloatButtonContainer;
    NestedScrollingParent2Layout scrollingParent2Layout;
    NestedScrollView contentView;

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

    DoubleElevenPopup doubleElevenPopup; // 雙十一活動彈窗
    ImageView iconDoubleElevenEntrance;

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
        Pair<Integer, Integer> screenDimension = Util.getScreenDimemsion(_mActivity);
        storeFigureContainerWidth = screenDimension.first -  2 * Util.dip2px(_mActivity, 32.5f); // 屏幕寬度減去兩邊32.5dp
        remainWidth = storeFigureContainerWidth - Util.dip2px(_mActivity, 52);
        SLog.info("storeFigureContainerWidth[%d], remainWidth[%d]", storeFigureContainerWidth, remainWidth);

        tvStoreCount = view.findViewById(R.id.tv_store_count);
        tvGoodsCount = view.findViewById(R.id.tv_goods_count);
        tvPostCount = view.findViewById(R.id.tv_post_count);

        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_category, this);
        Util.setOnClickListener(view, R.id.btn_category_store, this);
        Util.setOnClickListener(view, R.id.btn_category_goods, this);
        Util.setOnClickListener(view, R.id.btn_category_brand, this);
        Util.setOnClickListener(view, R.id.ll_search_box, this);
        Util.setOnClickListener(view, R.id.btn_message, this);
        Util.setOnClickListener(view, R.id.btn_goto_double_eleven, this);

        Util.setOnClickListener(view, R.id.btn_add_feedback, this);
        Util.setOnClickListener(view, R.id.btn_goto_top, this);

        scrollingParent2Layout = view.findViewById(R.id.scrolling_parent);
        scrollingParent2Layout.setCallback(this);
        contentView = view.findViewById(R.id.content_view);
        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);

        llNewArrivalsContainer = view.findViewById(R.id.ll_new_arrivals_container);
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
                        // String url = "https://www.jianshu.com/p/596a168c33d7";
                        String url = webSliderItem.linkValue;
                        Util.startFragment(ExplorerFragment.newInstance(url, true));
                        break;
                    case "keyword":
                        // 关键字
                        String keyword = webSliderItem.linkValue;
                        Util.startFragment(SearchResultFragment.newInstance(SearchType.GOODS.name(),
                                EasyJSONObject.generate("keyword", keyword).toString()));
                        break;
                    case "goods":
                        // 商品
                        int commonId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                        break;
                    case "store":
                        // 店铺
                        int storeId = Integer.valueOf(webSliderItem.linkValue);
                        Util.startFragment(ShopMainFragment.newInstance(storeId));
                        break;
                    case "category":
                        // 商品搜索结果页(分类)
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
                        Util.startDoubleElevenFragment();
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

        iconDoubleElevenEntrance = view.findViewById(R.id.icon_double_eleven_entrance);
        Glide.with(_mActivity).load("file:///android_asset/double_eleven/double_eleven_dynamic.gif")
                .into(iconDoubleElevenEntrance);
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
            loadNewArrivals();
        }

        bannerView.start();

        // 獲取上次顯示的時間戳
        long doubleElevenPopupShownTimestamp = Hawk.get(SPField.FIELD_DOUBLE_ELEVEN_POPUP_SHOWN_TIMESTAMP, 0L);
        // doubleElevenPopupShownTimestamp = 0;
        long now = System.currentTimeMillis();
        long doubleElevenTimestamp = Jarbon.parse("2019-11-11").getTimestampMillis();
        SLog.info("doubleElevenTimestamp[%s]", doubleElevenTimestamp);
        // 最多只顯示一次活動彈窗，而且要雙11當天后才顯示
        if (now >= doubleElevenTimestamp && doubleElevenPopupShownTimestamp == 0) {
            if (doubleElevenPopup == null) {
                doubleElevenPopup = (DoubleElevenPopup) new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new DoubleElevenPopup(_mActivity));
            }
            if (!doubleElevenPopup.isShown()) {
                doubleElevenPopup.show();
                // 記錄這一次顯示的時間
                Hawk.put(SPField.FIELD_DOUBLE_ELEVEN_POPUP_SHOWN_TIMESTAMP, System.currentTimeMillis());
            }
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        bannerView.pause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_category) {
            Util.startFragment(CategoryFragment.newInstance(CategoryFragment.TAB_STORE));
        } else if (id == R.id.btn_category_store) {
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
            Util.startFragment(SearchFragment.newInstance(SearchType.ALL));
        } else if (id == R.id.btn_test) {
            if (Config.DEVELOPER_MODE) {
                // Util.startFragment(RegisterConfirmFragment.newInstance("0086", "13417785707", 10));
                Util.startFragment(TestFragment.newInstance());
            }
        } else if (id == R.id.btn_add_feedback) {
            Util.startFragment(CommitFeedbackFragment.newInstance());
        } else if (id == R.id.btn_goto_top) {
            scrollingParent2Layout.scrollTo(0, 0);
            contentView.scrollTo(0, 0);
        } else if (id == R.id.btn_goto_double_eleven) {
            Util.startDoubleElevenFragment();
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

    public static class BannerViewHolder implements MZViewHolder<WebSliderItem> {
        private ImageView mImageView;

        public static final int GOODS_IMAGE_COUNT = 3;
        ImageView imgDesktop;
        ImageView[] goodsImageArr = new ImageView[GOODS_IMAGE_COUNT];

        List<WebSliderItem> webSliderItemList;

        public BannerViewHolder(List<WebSliderItem> webSliderItemList) {
            this.webSliderItemList = webSliderItemList;
        }

        public void setGoodsImageVisibility(int visibility) {
            for (int i = 0; i < GOODS_IMAGE_COUNT; ++i) {
                goodsImageArr[i].setVisibility(visibility);
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
            for (int i = 0; i < GOODS_IMAGE_COUNT; i++) {
                goodsImageArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int commonId = (int) v.getTag();
                        SLog.info("commonId[%d]", commonId);
                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                    }
                });
            }
            return view;
        }

        @Override
        public void onBind(Context context, int position, WebSliderItem webSliderItem) {
            try {
                // 数据绑定
                String imageUrl = StringUtil.normalizeImageUrl(webSliderItem.image);
                Glide.with(context).load(imageUrl).centerCrop().into(mImageView);

                if (webSliderItem.linkType.equals("store")) {
                    imgDesktop.setVisibility(View.VISIBLE);
                    setGoodsImageVisibility(View.VISIBLE);

                    String goodsCommons = webSliderItem.goodsCommons;
                    EasyJSONArray goodsArray = (EasyJSONArray) EasyJSONArray.parse(goodsCommons);

                    int i = 0;
                    for (Object object : goodsArray) {
                        EasyJSONObject goods = (EasyJSONObject) object;
                        int commonId = goods.getInt("commonId");
                        String goodsImage = StringUtil.normalizeImageUrl(goods.getString("goodsImage"));
                        Glide.with(context).load(goodsImage).centerCrop().into(goodsImageArr[i]);
                        goodsImageArr[i].setTag(commonId);
                        ++i;
                    }
                } else {
                    imgDesktop.setVisibility(View.GONE);
                    setGoodsImageVisibility(View.GONE);
                }
            } catch (Exception e) {

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
                // SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    goodsCommonCount = responseObj.getInt("datas.goodsCommonCount");
                    if (goodsCommonCount > 0) {
                        tvGoodsCount.setText(String.valueOf(goodsCommonCount));
                        tvGoodsCount.setVisibility(View.VISIBLE);
                    }

                    wantPostCount = responseObj.getInt("datas.wantPostCount");
                    if (wantPostCount > 0) {
                        tvPostCount.setText(String.valueOf(wantPostCount));
                        tvPostCount.setVisibility(View.VISIBLE);
                    }

                    storeCount = responseObj.getInt("datas.storeCount");
                    if (storeCount > 0) {
                        tvStoreCount.setText(String.valueOf(storeCount));
                        tvStoreCount.setVisibility(View.VISIBLE);
                    }

                    EasyJSONArray itemList = responseObj.getArray("datas.webSliderItem");
                    webSliderItemList.clear();
                    for (Object object : itemList) {
                        EasyJSONObject itemObj = (EasyJSONObject) object;

                        WebSliderItem webSliderItem = new WebSliderItem(
                                itemObj.getString("image"),
                                itemObj.getString("linkType"),
                                itemObj.getString("linkValue"),
                                itemObj.getString("goodsIds"),
                                itemObj.getString("goodsCommons"));
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

                    carouselLoaded = true;
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 加載最新入駐
     */
    private void loadNewArrivals() {
        SLog.info("loadNewArrivals");

        String token = User.getToken();
        SLog.info("token[%s]", token);

        try {
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            Api.postUI(Api.PATH_NEW_ARRIVALS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    // SLog.info("PATH_NEW_ARRIVALS, responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    // SLog.info("responseObj[%s]", responseObj);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        SLog.info("Error!responseObj is invalid");
                        return;
                    }

                    try {
                        float ratio = density / 2.5f;
                        if (ratio < 1) {
                            ratio = 0.95f;
                        }
                        ratio = 1;
                        SLog.info("__ratio[%s]", ratio);

                        EasyJSONArray storeList = responseObj.getArray("datas.storeList");
                        SLog.info("storeList size[%d]", storeList.length());

                        llNewArrivalsContainer.removeAllViews();
                        for (Object object : storeList) {
                            View storeView = LayoutInflater.from(_mActivity).inflate(R.layout.store_view, llNewArrivalsContainer, false);

                            EasyJSONObject store = (EasyJSONObject) object;

                            // 獲取店鋪Id
                            final int storeId = store.getInt("storeVo.storeId");

                            // 設置店鋪名稱
                            String storeName = store.getString("storeVo.storeName");
                            TextView tvStoreName = storeView.findViewById(R.id.tv_store_name);
                            tvStoreName.setText(storeName);

                            // 設置店鋪類別
                            TextView tvStoreClass = storeView.findViewById(R.id.tv_store_class);
                            String className = store.getString("storeVo.className");
                            String[] classNameArr = className.split(",");  // 拆分中英文
                            tvStoreClass.setText(classNameArr[0]);


                            // 店鋪形象圖
                            String storeFigureImageUrl = StringUtil.normalizeImageUrl(store.getString("storeVo.storeFigureImage"));
                            ImageView imgStoreFigure = storeView.findViewById(R.id.img_store_figure);
                            Glide.with(_mActivity).load(storeFigureImageUrl).centerCrop().into(imgStoreFigure);

                            int index = 0;
                            // 店鋪的3個商品展示
                            for (Object object2 : store.getArray("goodsList")) {
                                EasyJSONObject goodsObject = (EasyJSONObject) object2;
                                String imageSrc = goodsObject.getString("imageSrc");
                                int commonId = goodsObject.getInt("commonId");
                                String uri = StringUtil.normalizeImageUrl(imageSrc);
                                LinearLayout llGoodsImageContainer = null;

                                int dimen;
                                if (index == 0) {
                                    dimen = (int) (ratio * remainWidth * 79 / 258);
                                    // SLog.info("__dimen[%d]", dimen);
                                    ImageView goodsImageLeft = storeView.findViewById(R.id.goods_image_left);
                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageLeft.getLayoutParams();
                                    layoutParams.width = dimen;
                                    layoutParams.height = dimen;
                                    goodsImageLeft.setLayoutParams(layoutParams);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageLeft);
                                    llGoodsImageContainer = storeView.findViewById(R.id.goods_image_left_container);
                                    if (density < 2.5f) {
                                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) llGoodsImageContainer.getLayoutParams();
                                        layoutParams1.leftMargin = (int) (0.5 * layoutParams1.leftMargin);
                                        llGoodsImageContainer.setLayoutParams(layoutParams1);
                                    }
                                } else if (index == 1) {
                                    dimen = (int) (ratio * remainWidth * 100 / 258);
                                    // SLog.info("__dimen[%d]", dimen);
                                    ImageView goodsImageMiddle = storeView.findViewById(R.id.goods_image_middle);
                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageMiddle.getLayoutParams();
                                    layoutParams.width = dimen;
                                    layoutParams.height = dimen;
                                    goodsImageMiddle.setLayoutParams(layoutParams);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageMiddle);
                                    llGoodsImageContainer = storeView.findViewById(R.id.goods_image_middle_container);
                                } else if (index == 2) {
                                    dimen = (int) (ratio * remainWidth * 79 / 258);
                                    // SLog.info("__dimen[%d]", dimen);
                                    ImageView goodsImageRight = storeView.findViewById(R.id.goods_image_right);
                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) goodsImageRight.getLayoutParams();
                                    layoutParams.width = dimen;
                                    layoutParams.height = dimen;
                                    goodsImageRight.setLayoutParams(layoutParams);
                                    Glide.with(_mActivity).load(uri).centerCrop().into(goodsImageRight);
                                    llGoodsImageContainer = storeView.findViewById(R.id.goods_image_right_container);
                                    if (density < 2.5f) {
                                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) llGoodsImageContainer.getLayoutParams();
                                        layoutParams1.rightMargin = (int) (0.5 * layoutParams1.rightMargin);
                                        SLog.info("_____RIGHT[%d]", layoutParams1.rightMargin);
                                        llGoodsImageContainer.setLayoutParams(layoutParams1);
                                    }
                                }

                                llGoodsImageContainer.setVisibility(View.VISIBLE);
                                llGoodsImageContainer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                                    }
                                });

                                ++index;
                            }

                            // 添加控件到容器中
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            int marginTop = Util.dip2px(_mActivity, 15);
                            int marginBottom = Util.dip2px(_mActivity, 20);
                            layoutParams.setMargins(0, marginTop, 0, marginBottom);

                            storeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                                }
                            });
                            llNewArrivalsContainer.addView(storeView, layoutParams);

                            newArrivalsLoaded = true;
                        }
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (EasyJSONException e) {
            SLog.info("Error!%s", e.getMessage());
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
}


