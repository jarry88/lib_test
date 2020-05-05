package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.ftofs.twant.entity.WebSliderItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.BannerViewHolder;
import com.ftofs.twant.widget.SlantedWidget;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.lxj.xpopup.core.BasePopupView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.ftofs.twant.R.drawable.white_4dp_right_radius_bg;

public class ShoppingSessionFragment extends BaseFragment implements View.OnClickListener, NestedScrollingCallback {
    Unbinder unbinder;
    //雙列表數據源
    String from; // 来自哪个fragment调用
    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;

    @BindView(R.id.linkage)
    LinkageRecyclerView linkage;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.ll_filter_bar)
    LinearLayout llFilterBar;
    @BindView(R.id.et_keyword)
    EditText etKeyword;
    List<ElemeGroupedItem> items = new ArrayList<>();
    @BindView(R.id.banner_view)
    MZBannerView bannerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    List<WebSliderItem> webSliderItemList = new ArrayList<>();
    @BindView(R.id.ll_float_button_container)
    LinearLayout llFloatButtonContainer;
    private boolean carouselLoaded;
    private String backgroundColor;
    private int twColor;
    /*
    用于記錄滑動狀態，以處理浮動按鈕的顯示與隱藏
     */
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 800; // 浮動按鈕滑動顯示與隱藏效果的延遲時間(毫秒)
    boolean isScrolling = false; // 是否在滑動狀態
    long lastScrollingTimestamp = 0;  // 最近一次滑動的時間戳（毫秒）
    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示

    RecyclerView rvSecondList;
    RecyclerView rvPrimaryList;
    TextView tvFooter;

    int containerViewHeight = 0;


    public static ShoppingSessionFragment newInstance() {
        ShoppingSessionFragment fragment = new ShoppingSessionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_session, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_goto_top,this);
        Util.setOnClickListener(view,R.id.btn_goto_cart,this);
        Util.setOnClickListener(view,R.id.btn_back,this);
        // setScrollView();//設置滾動後linkage吸頂效果失敗

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
                    default:
                        break;
                }
            }
        });

        rvSecondList = linkage.findViewById(R.id.rv_secondary);
        rvPrimaryList = linkage.findViewById(R.id.rv_primary);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rvSecondList.getLayoutParams();
        LinearLayout .LayoutParams layoutParams1 = (LinearLayout .LayoutParams) rvPrimaryList.getLayoutParams();
        layoutParams.height =scrollView.getHeight()-44;
        layoutParams1.height =scrollView.getHeight();
//        rvSecondList.setLayoutParams(layoutParams);
        rvPrimaryList.setLayoutParams(layoutParams1);


        SLog.info("isNestedScrollingEnabled[%s]", rvSecondList.isNestedScrollingEnabled());
        rvSecondList.setNestedScrollingEnabled(false);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int linkageY = Util.getYOnScreen(linkage);
                int linkageY_ = linkageY + linkage.getHeight();
                int containerViewY = Util.getYOnScreen(scrollView);

                SLog.info("linkageY[%s], containerViewY[%s],linkageY_[%s]", linkageY, containerViewY,linkageY_);
                if (linkageY <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
                    rvSecondList.setNestedScrollingEnabled(true);
                } else {
                    rvSecondList.setNestedScrollingEnabled(false);
                }
            }
        });
        rvSecondList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                SLog.info("__newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    int linkageY_ = Util.getYOnScreen(linkage) + linkage.getHeight();
                    SLog.info("linkageY_[%s]", linkageY_);
                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFloatButton();
                }
            }
        });

        loadSessionData();
    }
    /**
     * 加載購物專場數據
     */
    private void loadSessionData() {
        //沒登錄也可以去
//        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
//        String token = User.getToken();
//        if (StringUtil.isEmpty(token)) {
//            return;
//        }

        EasyJSONObject params = EasyJSONObject.generate(
//                "token", token
                );


        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SHOP_SESSION, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]",responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }
                try {
                    backgroundColor = responseObj.getSafeString("datas.bannerBackgroundColor");
                    EasyJSONArray discountBannerList = responseObj.getSafeArray("datas.discountBannerList");
                    if (!Util.isJsonArrayEmpty(discountBannerList)) {
                        setBannerData(discountBannerList);
                    }
                    setSessionTextColor();
                    EasyJSONArray categoryGoodsList = responseObj.getSafeArray("datas.categoryGoodsList");
                    setLinkageData(categoryGoodsList);

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerViewHeight == 0) {
            containerViewHeight = scrollView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);

            ViewGroup.LayoutParams layoutParams = rvSecondList.getLayoutParams();
            LinearLayout .LayoutParams layoutParams1 = (LinearLayout.LayoutParams) rvPrimaryList.getLayoutParams();
//            layoutParams.height = containerViewHeight-44;
            layoutParams1.height = containerViewHeight;
            rvSecondList.setLayoutParams(layoutParams);
            rvPrimaryList.setLayoutParams(layoutParams1);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    private void setSessionTextColor() {
        llContainer.setBackgroundColor(Color.parseColor(backgroundColor));
        int redValue=Integer.parseInt(backgroundColor.substring(1, 3),16);
        int greenValue=Integer.parseInt(backgroundColor.substring(3, 5),16);
        int blueValue=Integer.parseInt(backgroundColor.substring(5, 7),16);
        double y = 0.299 * redValue + 0.587 * greenValue + 0.114 * blueValue;
        String s = "白";
        if (Math.ceil(y) < 128) {
            twColor = R.color.tw_white;
        } else{
            twColor = R.color.tw_black;
            s = "黑";
        }
        SLog.info("twcolor[%s]",s);
    }

    private void setBannerData(EasyJSONArray discountBannerList) {
        SLog.info("bannerListLength %d",discountBannerList.length());
        try {
        for (Object object : discountBannerList) {
            EasyJSONObject banner = (EasyJSONObject) object;

                String imageUrl = banner.getSafeString("imageUrl");
                WebSliderItem webSliderItem = new WebSliderItem(StringUtil.normalizeImageUrl(imageUrl), null, null, null, "[]");
                webSliderItemList.add(webSliderItem);
                // 设置数据
                bannerView.setPages(webSliderItemList, (MZHolderCreator<BannerViewHolder>) () -> new BannerViewHolder(webSliderItemList));

            carouselLoaded = true;
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

    private void setLinkageData(EasyJSONArray categoryGoodsList) {
        if (categoryGoodsList == null||categoryGoodsList.length()==0) {
            return;
        }
        items.clear();
        try {
        for (Object object : categoryGoodsList) {
            EasyJSONObject categoryData = (EasyJSONObject) object;

            String groupName = categoryData.getSafeString("categoryName");
            EasyJSONArray goodslist = categoryData.getSafeArray("goodsList");
            ElemeGroupedItem category = new ElemeGroupedItem(true, groupName);
            if(goodslist==null){
                continue;
            }
            items.add(category);
            for (Object object1 : goodslist) {
                EasyJSONObject goods = (EasyJSONObject) object1;
                String goodsName = goods.getSafeString("goodsName");
                String goodsImage = goods.getSafeString("goodsImage");
                int commonId = goods.getInt("commonId");
                String jingle  = goods.getSafeString("jingle");
                double price;
                int appUsable = goods.getInt("appUsable");
                if (appUsable > 0) {
                    price =  goods.getDouble("appPriceMin");
                } else {
                    price =  goods.getDouble("batchPrice0");
                }

                double batchPrice0 = goods.getDouble("batchPrice0");
                double promotionDiscountRate =  goods.getDouble("promotionDiscountRate");

                ElemeGroupedItem.ItemInfo goodsInfo = new ElemeGroupedItem.ItemInfo(goodsName, groupName, jingle, StringUtil.normalizeImageUrl(goodsImage),
                        StringUtil.formatPrice(getContext(), price, 0),promotionDiscountRate,batchPrice0,commonId,appUsable > 0);
                ElemeGroupedItem item1 = new ElemeGroupedItem(goodsInfo);
                items.add(item1);
                }

            }

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        ElemePrimaryAdapterConfig primaryAdapterConfig = new ElemePrimaryAdapterConfig();
        primaryAdapterConfig.setBackgroundColor(R.color.mask15_background_color, getResources().getDrawable(white_4dp_right_radius_bg));
        SLog.info("twColor%s",twColor);
        primaryAdapterConfig.setTwColor(twColor);
        ElemeSecondaryAdapterConfig secondaryAdapterConfig = new ElemeSecondaryAdapterConfig();
        linkage.init(items,primaryAdapterConfig,secondaryAdapterConfig);

    }

    private static class ElemePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

        private Context mContext;
        private int backgroundColor;
        private Drawable default_drawbg;

        public void setTwColor(int twColor) {
            this.twColor = twColor;
        }

        private int twColor;

        public void setContext(Context context) {
            mContext = context;
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
            TextView tvTitle = ((TextView) holder.mGroupTitle);
            tvTitle.setText(title);
            View blue = holder.mLayout.findViewById(R.id.view_border);
            blue.setVisibility(selected?View.VISIBLE:View.GONE);
            if (selected) {
                tvTitle.setBackground(default_drawbg);
                holder.mLayout.setBackgroundColor(Color.argb(0,0,0,0));
            } else {
                holder.mLayout.setBackgroundColor(Color.argb(26,0,0,0));
                tvTitle.setBackgroundColor(Color.argb(0,0,0,0));
            }
            tvTitle.setTextColor(selected ? ContextCompat.getColor(mContext,
                    com.kunminx.linkage.R.color.colorLightBlue):ContextCompat.getColor(mContext,twColor));
            tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
            tvTitle.setFocusable(selected);
            tvTitle.setFocusableInTouchMode(selected);
            tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
        }

        @Override
        public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
            //TODO
//            ToastUtil.error(mContext,title);
        }
    }

    private static class ElemeSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {

        private Context mContext;

        public void setContext(Context context) {
            mContext = context;
        }
        @Override
        public int getGridLayoutId() {
            return 0;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.adapter_eleme_secondary_linear;
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
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                     BaseGroupedItem<ElemeGroupedItem.ItemInfo> item) {

            try {
                ((TextView) holder.getView(R.id.tv_goods_name)).setText(item.info.getTitle());
                ((TextView) holder.getView(R.id.tv_goods_comment)).setText(item.info.getContent());
                ((TextView) holder.getView(R.id.tv_goods_price)).setText(StringUtil.formatPrice(mContext, Double.valueOf(item.info.getCost().substring(1)), 0, true));
                holder.getView(R.id.sw_price).setVisibility(item.info.show?View.VISIBLE:View.GONE);
                ((SlantedWidget) holder.getView(R.id.sw_price)).setDiscountInfo(mContext,item.info.getDiscount(),item.info.getOriginal());
                ImageView imageView =  holder.getView(R.id.iv_goods_img);
                Glide.with(mContext).load(item.info.getImgUrl()).centerCrop().into(imageView);
                holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                    Util.startFragment(GoodsDetailFragment.newInstance(item.info.commonId,0));
                });

                holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
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

    /*
    private void setScrollView() {
        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int LinkageY = Util.getYOnScreen(linkage);
            int FilterBarY = Util.getYOnScreen(llFilterBar);
            int FilterBarHegiht = 40;
            boolean scroll = LinkageY >= FilterBarY - FilterBarHegiht;
            scrollView.setOnTouchListener((v1, event) -> {return scroll;});
        });
    }
     */

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int userId=User.getUserId();
        if (id == R.id.btn_goto_cart) {
            if (userId > 0) {
                Util.startFragment(CartFragment.newInstance(true));
            } else {
                Util.showLoginFragment();
            }

        } else if (id == R.id.btn_goto_top) {
            scrollView.scrollTo(0,0);
            linkage.scrollTo(0,0);
            linkage.getPrimaryAdapter().setSelectedPosition(0);
            rvSecondList.scrollToPosition(0);
//            linkage.ge;
        } else if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
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
}
