package com.ftofs.twant.fragment;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsGalleryAdapter;
import com.ftofs.twant.adapter.StoreFriendsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.GiftVo;
import com.ftofs.twant.entity.GoodsConformItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.InStorePersonItem;
import com.ftofs.twant.entity.SkuGalleryItem;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecPair;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.entity.StoreFriendsItem;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.goods.GoodsMobileBodyVo;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.DataImageView;
import com.ftofs.twant.widget.InStorePersonPopup;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.StoreGiftPopup;
import com.ftofs.twant.widget.StoreVoucherPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 產品詳情頁面
 *
 * @author zwm
 */
public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 80;
    int discountState;//0沒有折扣信息、1未開始、2已開始、3以結束
    private static final int NO_DISCOUNT = 0;
    private static final int BEFORE_DISCOUNT=1;
    private static final int IN_DISCOUNT=2;
    private static final int OUT_DISCOUNT=3;
    Unbinder unbinder;
    // 產品Id
    int commonId;
    // 當前選中的goodsId
    int currGoodsId;

    // 商店Id
    int storeId;
    // 購買數量
    int buyNum = 1;

    // 滿優惠數
    int conformCount;
    // 贈品數
    int giftCount;

    String goodsVideoId;  // 產品視頻Id
    String detailVideoId; // 介紹視頻Id

    float promotionDiscountRate;
    TextView tvPromotionDiscountRate;

    /**
     * 限時折扣倒計時是否正在倒數
     */
    boolean isCountingDown;
    int discountEndTime;
    Timer timer;

    RecyclerView rvGalleryImageList;


    TextView tvGoodsPrice;
    TextView tvGoodsPriceFinal;
    TextView tvGoodsPriceOriginal;
    TextView tvGoodsName;
    TextView tvGoodsJingle;

    ImageView imgGoodsNationalFlag;
    TextView tvGoodsCountryName;
    TextView tvShipTo;
    TextView tvFreightAmount;
    TextView tvFansCount;
    TextView tvViewCount;
    TextView tvGoodsSale;

    ImageView iconFollow;
    TextView tvFollow;
    int isFavorite;  // 是否關注

    // 當前選中規格的產品名稱和賣點
    String goodsName;
    String jingle;

    ImageView btnGoodsThumb;
    int isLike; // 是否讚想
    int goodsLike;

    int allowSend;

    TextView tvThumbCount;
    TextView tvGoodsCommentCount;

    List<Spec> specList = new ArrayList<>();
    // 從逗號連接的specValueId定位出goodsId的Map
    Map<String, Integer> specValueIdMap = new HashMap<>();

    List<SpecPair> specPairList = new ArrayList<>();

    LinearLayout llGoodsDetailImageContainer;

    TextView tvCurrentSpecs;
    // 當前選中的SpecValueId列表
    List<Integer> selSpecValueIdList = new ArrayList<>();

    // skuId與sku圖片輪播列表的映射關系
    Map<Integer, List<String>> skuGoodsGalleryMap = new HashMap<>();
    GoodsGalleryAdapter goodsGalleryAdapter;
    int currGalleryPosition;
    List<String> currGalleryImageList = new ArrayList<>();
    List<SkuGalleryItem> skuGalleryItemList = new ArrayList<>();

    PageIndicatorView pageIndicatorView;

    RelativeLayout rlVoucherList;

    RelativeLayout btnShowConform;
    TextView tvConformHint;
    TextView tvGiftHint;


    TextView tvCountDownDay;
    TextView tvCountDownHour;
    TextView tvCountDownMinute;
    TextView tvCountDownSecond;

    RelativeLayout rlDiscountInfoContainer;
    RelativeLayout rlPriceTag;

    int inStorePersonCount;
    StoreFriendsAdapter adapter;
    List<StoreFriendsItem> storeFriendsItemList = new ArrayList<>();

    List<String> goodsDetailImageList = new ArrayList<>();

    ImageView btnPlay;

    // 產品說說條數
    int commentCount = 0;
    TextView tvCommentCount;
    LinearLayout llCommentContainer;
    LinearLayout llFirstCommentContainer;
    ImageView imgCommenterAvatar;
    TextView tvCommenterNickname;
    TextView tvComment;

    // 到貨通知按鈕
    TextView btnArrivalNotice;
    // 【想放購物袋】和【想要購買】的包裝容器，如果有庫存時顯示這個
    LinearLayout llStorageOkContainer;

    /**
     * 商店券和平台券列表
     */
    List<StoreVoucher> storeVoucherList = new ArrayList<>();

    /**
     * goodsId與贈品列表的映射表
     */
    Map<Integer, List<GiftItem>> giftMap = new HashMap<>();
    List<GoodsConformItem> goodsConformItemList = new ArrayList<>();
    Map<Integer, GoodsInfo> goodsInfoMap = new HashMap<>();

    List<InStorePersonItem> inStorePersonItemList = new ArrayList<>();
    CountDownHandler countDownHandler;

    boolean isDataValid;

    LinearLayout llVoucherContainer;
    ScrollView scrollViewContainer;
    RelativeLayout rlTopBarContainer;
    private SimpleTabManager simpleTabManager;
    CommentListFragment commentListFragment;
    ConstraintLayout llPage1;
    LinearLayout llPage2;
    LinearLayout llPage3;
    LinearLayout bootomBar;
    LinearLayout llFloatButton;
    private int commentChannel = Constant.COMMENT_CHANNEL_GOODS;//產品
    private Handler mHandler;
    private boolean showFloatButton = true;
    private boolean isScrolling = false;
    private int discountStartTime;
    private ViewPagerFragment viewPagerFragment;
    private int goodsStatus=1;
    private RelativeLayout preTopBarContainer;

    double goodsPrice;
    private int limitBuy;
    private int tariffEnable =Constant.FALSE_INT;
    private ImageView iconTariff;


    static class scrollStateHandler extends Handler {
        ScrollView scrollViewContainer;
        GoodsDetailFragment fragment;
        int lastY = -1;
        private boolean showFloatButton = true;

        public scrollStateHandler(GoodsDetailFragment fragment) {
            this.fragment = fragment;
            this.scrollViewContainer = fragment.scrollViewContainer;
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
                    fragment.isScrolling = false;
                    fragment.showFloatButton();
                } else {
                    // 滚动的距离 和 之前的不相等 那么 再发送消息 判断一次
// 将滚动的 Y 坐标距离 赋值给 lastY
                    lastY = scrollY;
                    this.sendEmptyMessageDelayed(0, 10);
                }
            }
        }


    }

    static class CountDownHandler extends Handler {
        WeakReference<GoodsDetailFragment> weakReference;

        public CountDownHandler(GoodsDetailFragment goodsDetailFragment) {
            weakReference = new WeakReference<>(goodsDetailFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TimeInfo timeInfo = (TimeInfo) msg.obj;
            GoodsDetailFragment goodsDetailFragment = weakReference.get();
            if (goodsDetailFragment.discountState == 2) {
                goodsDetailFragment.tvCountDownDay.setText(String.format("距結束 %d 天", timeInfo.day));
            } else if (goodsDetailFragment.discountState == 1) {
                goodsDetailFragment.tvCountDownDay.setText(String.format("距開始 %d 天", timeInfo.day));
            }
            goodsDetailFragment.tvCountDownHour.setText(String.format("%02d", timeInfo.hour));
            goodsDetailFragment.tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
            goodsDetailFragment.tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
        }
    }

    /**
     * 構建產品詳情頁面
     *
     * @param commonId spuId
     * @param goodsId  skuId, 用于選中指定的sku，如果傳0,表示默認選中第一個sku
     * @return
     */
    public static GoodsDetailFragment newInstance(int commonId, int goodsId) {
        Bundle args = new Bundle();

        args.putInt("commonId", commonId);
        args.putInt("goodsId", goodsId);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SLog.info("onViewCreated");
        countDownHandler = new CountDownHandler(this);
        EventBus.getDefault().register(this);
        iconTariff = view.findViewById(R.id.icon_tariffEnable);

        Bundle args = getArguments();
        commonId = args.getInt("commonId");
        currGoodsId = args.getInt("goodsId");
        SLog.info("commonId[%d], currGoodsId[%d]", commonId, currGoodsId);
        scrollViewContainer = view.findViewById(R.id.sv_content);
        scrollViewContainer.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            isScrolling = true;
        });
        mHandler = new scrollStateHandler(this);
        scrollViewContainer.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mHandler.sendEmptyMessageDelayed(0, 10);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                hideFloatButton();
            }
            return false;
        });
        //  用于 存储 上一次 滚动的Y坐标
        rlTopBarContainer = view.findViewById(R.id.tool_bar);
        preTopBarContainer = view.findViewById(R.id.rv_pre_tool_bar);
        llFloatButton = view.findViewById(R.id.ll_float_button_container);
        scrollViewContainer.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int fadingHeight = 255;
            int initHeight = 20;
            if (llPage1.getVisibility() == VISIBLE) {
                if (scrollY > initHeight) {
                    view.findViewById(R.id.btn_back).setVisibility(VISIBLE);
                    view.findViewById(R.id.btn_menu).setVisibility(VISIBLE);
                    view.findViewById(R.id.btn_search).setVisibility(VISIBLE);
                    if (scrollY > fadingHeight) {
                        rlTopBarContainer.setAlpha(1.0f);
                        preTopBarContainer.setAlpha(0);

                    } else {
                        rlTopBarContainer.setAlpha((float) scrollY / fadingHeight);
                        preTopBarContainer.setAlpha(1.0f-(float) scrollY / fadingHeight);

                        //rlTopBarContainer.getBackground().setAlpha(scrollY);
                    }
                } else {
                    view.findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.btn_menu).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.btn_search).setVisibility(View.INVISIBLE);
                    rlTopBarContainer.setAlpha(0.0f);
                    preTopBarContainer.setAlpha(1.0f);
                }
            } else {
                rlTopBarContainer.setAlpha(1.0f);
            }
        });
        simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                if (isRepeat) {
                    return;
                }
                int id = v.getId();
                SLog.info("id[%d]", id);
                switchPage(id);

            }
        };
        llPage1 = view.findViewById(R.id.ll_page_1);
        llPage2 = view.findViewById(R.id.ll_page_2);
        llPage3 = view.findViewById(R.id.ll_page_3);
        bootomBar = view.findViewById(R.id.rl_bottom_bar_container);

        Util.setOnClickListener(view, R.id.rl_price_tag, this);
        simpleTabManager.add(view.findViewById(R.id.stb_good_home));
        simpleTabManager.add(view.findViewById(R.id.stb_good_detail));
        simpleTabManager.add(view.findViewById(R.id.stb_good_comment));
        rvGalleryImageList = view.findViewById(R.id.rv_gallery_image_list);

        tvGoodsPrice = view.findViewById(R.id.tv_goods_price_left);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);

        btnPlay = view.findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);

        btnArrivalNotice = view.findViewById(R.id.btn_arrival_notice);
        btnArrivalNotice.setOnClickListener(this);
        llStorageOkContainer = view.findViewById(R.id.ll_storage_ok_container);

        rlDiscountInfoContainer = view.findViewById(R.id.rl_discount_info_container);
        rlPriceTag = view.findViewById(R.id.rl_price_tag);

        tvGoodsPriceOriginal = view.findViewById(R.id.tv_goods_price_original);
        // 原價顯示刪除線
        tvGoodsPriceOriginal.setPaintFlags(tvGoodsPriceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvGoodsPriceFinal = view.findViewById(R.id.tv_goods_price_final);

        tvPromotionDiscountRate = view.findViewById(R.id.tv_promotion_discount_rate);

        tvCountDownDay = view.findViewById(R.id.tv_count_down_day);
        tvCountDownHour = view.findViewById(R.id.tv_count_down_hour);
        tvCountDownMinute = view.findViewById(R.id.tv_count_down_minute);
        tvCountDownSecond = view.findViewById(R.id.tv_count_down_second);

        tvFreightAmount = view.findViewById(R.id.tv_freight_amount);

        tvCurrentSpecs = view.findViewById(R.id.tv_current_specs);

        imgGoodsNationalFlag = view.findViewById(R.id.img_goods_national_flag);
        tvGoodsCountryName = view.findViewById(R.id.tv_goods_country_name);
        tvShipTo = view.findViewById(R.id.tv_ship_to);

        iconFollow = view.findViewById(R.id.icon_follow);
        tvFollow = view.findViewById(R.id.tv_follow);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvViewCount = view.findViewById(R.id.tv_view_count);
        tvGoodsSale = view.findViewById(R.id.tv_goods_sale);

        llGoodsDetailImageContainer = view.findViewById(R.id.ll_goods_detail_image_container);
        btnGoodsThumb = view.findViewById(R.id.btn_goods_thumb);
        btnGoodsThumb.setOnClickListener(this);

        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        tvGoodsCommentCount = view.findViewById(R.id.tv_goods_comment_count);

        Util.setOnClickListener(view, R.id.btn_goods_share, this);

        llVoucherContainer = view.findViewById(R.id.ll_voucher_container);
        rlVoucherList = view.findViewById(R.id.rl_voucher_list);

        Util.setOnClickListener(view, R.id.ll_voucher_container, this);

        btnShowConform = view.findViewById(R.id.btn_show_conform);
        btnShowConform.setOnClickListener(this);
        tvConformHint = view.findViewById(R.id.tv_conform_hint);
        tvGiftHint = view.findViewById(R.id.tv_gift_hint);

        tvCommentCount = view.findViewById(R.id.tv_comment_count);
        Util.setOnClickListener(view, R.id.btn_goods_comment, this);

        llCommentContainer = view.findViewById(R.id.ll_comment_container);
        llCommentContainer.setOnClickListener(this);
        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);
        imgCommenterAvatar = view.findViewById(R.id.img_commenter_avatar);
        tvCommenterNickname = view.findViewById(R.id.tv_commenter_nickname);
        tvComment = view.findViewById(R.id.tv_comment);

        Util.setOnClickListener(view, R.id.btn_back_round, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search_round, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_menu_round, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);
        Util.setOnClickListener(view, R.id.btn_show_all_store_friends, this);
        Util.setOnClickListener(view, R.id.btn_add_to_cart, this);
        Util.setOnClickListener(view, R.id.btn_buy, this);
        Util.setOnClickListener(view, R.id.btn_select_spec, this);
        Util.setOnClickListener(view, R.id.btn_select_addr, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_follow, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_shop, this);
        Util.setOnClickListener(view, R.id.btn_goto_cart, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_customer_service, this);
        Util.setOnClickListener(view, R.id.btn_comment, this);

        RecyclerView rvStoreFriendsList = view.findViewById(R.id.rv_store_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvStoreFriendsList.setLayoutManager(layoutManager);
        adapter = new StoreFriendsAdapter(R.layout.store_friends_item, storeFriendsItemList);
        adapter.setOnItemClickListener((adapter, view1, position) -> {
            StoreFriendsItem item = storeFriendsItemList.get(position);
            Util.startFragment(MemberInfoFragment.newInstance(item.memberName));
        });
        rvStoreFriendsList.setAdapter(adapter);

        pageIndicatorView = view.findViewById(R.id.pageIndicatorView);
        setImageBanner(rvGalleryImageList);
    }

    private void setImageBanner(RecyclerView rvGalleryImageList) {
        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        rvGalleryImageList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        (new PagerSnapHelper()).attachToRecyclerView(rvGalleryImageList);
        goodsGalleryAdapter = new GoodsGalleryAdapter(_mActivity, currGalleryImageList);

        goodsGalleryAdapter.setOnItemClickListener(v -> {
            if (currGalleryImageList.size() == 0) {
                return;
            }
            int position = currGalleryPosition % currGalleryImageList.size();
            viewPagerFragment = ViewPagerFragment.newInstance(currGalleryImageList,false);
            // SLog.info(currGalleryImageList.toString());
            viewPagerFragment.start = position;
            Util.startFragment(viewPagerFragment);
            SLog.info("currPosition[%d][%d]", currGalleryPosition, position);
        });
        rvGalleryImageList.setAdapter(goodsGalleryAdapter);
        rvGalleryImageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currGalleryPosition = getCurrPosition();
                    SLog.info("currPosition[%d]", currGalleryPosition);
                    if (currGalleryImageList.size() == 0) {
                        return;
                    }
                    int position = currGalleryPosition % currGalleryImageList.size();
                    pageIndicatorView.setSelection(position);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void hideFloatButton() {
        if (showFloatButton) {
            SLog.info("調用隱藏");
            showFloatButton = false;
            llFloatButton.postDelayed(() -> {
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 0);
                        translateAnimation.setDuration(400);
                        translateAnimation.setFillAfter(true);
                        llFloatButton.setAnimation(translateAnimation);
                        llFloatButton.startAnimation(translateAnimation);
                        SLog.info("執行隱藏");
                    }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY
            );
        }
    }

    private void showFloatButton() {
        if (!showFloatButton) {
            if (isScrolling) {
                return;
            }
            llFloatButton.postDelayed(() -> {
                TranslateAnimation translateAnimation = new TranslateAnimation(100, 0, 0, 0);
                translateAnimation.setDuration(400);
                translateAnimation.setFillAfter(true);
                llFloatButton.setAnimation(translateAnimation);
                llFloatButton.startAnimation(translateAnimation);
                SLog.info("執行顯示");
                showFloatButton = true;
            }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY);
        }
    }

    private void switchPage(int id) {
        llPage1.setVisibility(GONE);
        llPage2.setVisibility(GONE);
        llPage3.setVisibility(GONE);
        if (id == R.id.stb_good_home) {
            llPage1.setVisibility(VISIBLE);
            llPage2.setVisibility(VISIBLE);
            bootomBar.setVisibility(VISIBLE);
        } else if (id == R.id.stb_good_detail) {
            llPage2.setVisibility(VISIBLE);
            bootomBar.setVisibility(GONE);
        } else if (id == R.id.stb_good_comment) {
            updateComment();
        }
    }

    private void updateComment() {
        if (commentListFragment != null) {
            commentListFragment.resetData();
        } else {
            SLog.info("switch here");

            commentListFragment = CommentListFragment.newInstance(commonId, Constant.COMMENT_CHANNEL_GOODS, false);
        }
        bootomBar.setVisibility(GONE);
        getFragmentManager().beginTransaction().replace(R.id.ll_page_3, commentListFragment).commit();
        getView().findViewById(R.id.ll_page_3).setVisibility(VISIBLE);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        int userId = User.getUserId();

        switch (id) {
            case R.id.btn_back_round:
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_search_round:
            case R.id.btn_search:

                start(ShopSearchFragment.newInstance(storeId, null));
                break;
            case R.id.btn_menu_round:
            case R.id.btn_menu:
                new XPopup.Builder(_mActivity)
                        .offsetX(-Util.dip2px(_mActivity, 11))
                        .offsetY(-Util.dip2px(_mActivity, 1))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                        .hasShadowBg(false) // 去掉半透明背景
                        .atView(v)
                        .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_GOODS))
                        .show();
                break;
            case R.id.btn_comment:
                if (userId == 0) {
                    Util.showLoginFragment();
                    return;
                }
                Util.startFragmentForResult(AddCommentFragment.newInstance(commonId, Constant.COMMENT_CHANNEL_GOODS), RequestCode.ADD_COMMENT.ordinal());
                break;

            case R.id.btn_show_all_store_friends:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new InStorePersonPopup(_mActivity, inStorePersonCount, inStorePersonItemList))
                        .show();
                break;
            case R.id.btn_add_to_cart:
                if(goodsStatus==0){
                    ToastUtil.error(_mActivity,"商品已下架");
                    return;
                }
                if (userId > 0) {
                    showSpecSelectPopup(Constant.ACTION_ADD_TO_CART);
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_goto_cart:
                if (userId > 0) {
                    Util.startFragment(CartFragment.newInstance(true));
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_buy:
                if(goodsStatus==0){
                    ToastUtil.error(_mActivity,"商品已下架");
                    return;
                }
                if (userId > 0) {
                    if (allowSend == 0) {
                        ToastUtil.error(_mActivity, getString(R.string.not_allow_send_hint));
                        return;
                    }
                    showSpecSelectPopup(Constant.ACTION_BUY);
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_select_spec:
                showSpecSelectPopup(Constant.ACTION_SELECT_SPEC);
                break;
            case R.id.btn_select_addr:
                if (userId > 0) {
                    startForResult(AddrManageFragment.newInstance(), RequestCode.CHANGE_ADDRESS.ordinal());
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_bottom_bar_follow:
                switchFavoriteState();
                break;
            case R.id.btn_goods_thumb:
                switchThumbState();
                break;
            case R.id.btn_bottom_bar_shop:
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            case R.id.ll_voucher_container:
                if (userId < 1) {
                    start(LoginFragment.newInstance());
                    return;
                }
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreVoucherPopup(_mActivity, storeVoucherList))
                        .show();
                break;
            case R.id.btn_show_conform:
                // 顯示促銷信息
                if (userId < 1) {
                    start(LoginFragment.newInstance());
                    return;
                }
                // 顯示哪個Tab
                int tabId;

                if (conformCount > 0) {
                    tabId = StoreGiftPopup.TAB_ID_CONFORM;
                } else if (giftCount > 0) {
                    tabId = StoreGiftPopup.TAB_ID_GIFT;
                } else {
                    return;
                }
                List<GiftItem> giftItemList = giftMap.get(currGoodsId);
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreGiftPopup(_mActivity, tabId, giftItemList, goodsConformItemList))
                        .show();
                break;
            case R.id.btn_goods_share:
                pullShare();
                break;
            case R.id.ll_comment_container:
            case R.id.btn_goods_comment:
                Util.startFragment(CommentListFragment.newInstance(commonId, Constant.COMMENT_CHANNEL_GOODS));
                break;
            case R.id.btn_bottom_bar_customer_service:
                showStoreCustomerService();
                break;
            case R.id.btn_arrival_notice:
                Util.startFragment(ArrivalNoticeFragment.newInstance(commonId, currGoodsId));
                break;
            case R.id.btn_play:
                Util.playYoutubeVideo(_mActivity, goodsVideoId);
                break;
            default:
                break;
        }
    }

    public void pullShare() {
        SLog.info("goodsPrice[%s]", goodsPrice);
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SharePopup(_mActivity, SharePopup.generateGoodsShareLink(commonId, currGoodsId), goodsName,
                        jingle, currGalleryImageList.get(0), EasyJSONObject.generate("shareType", SharePopup.SHARE_TYPE_GOODS,
                        "commonId", commonId, "goodsName", goodsName,
                        "goodsImage", currGalleryImageList.get(0), "goodsPrice", goodsPrice)))
                .show();
    }

    public void showStoreCustomerService() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new StoreCustomerServicePopup(_mActivity, storeId,goodsInfoMap.get(currGoodsId)))
                .show();
    }

    /**
     * 產品關注/取消關注
     */
    private void switchFavoriteState() {
        if(goodsStatus==0){
            ToastUtil.success(_mActivity,"商品已下架");
            return;
        }
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "state", 1 - isFavorite,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_GOODS_FAVORITE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    isFavorite = 1 - isFavorite;
                    updateFavoriteView();

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 產品讚想/取消讚想
     */
    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "state", 1 - isLike,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_GOODS_LIKE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    isLike = 1 - isLike;
                    if (isLike == 1) {
                        goodsLike++;
                    } else {
                        goodsLike--;
                    }
                    updateThumbView();

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 更新是否關注的顯示
     */
    private void updateFavoriteView() {
        if (isFavorite == Constant.ONE) {
            iconFollow.setImageResource(R.drawable.icon_store_favorite_yellow);
            tvFollow.setText(R.string.text_followed);
            tvFollow.setTextColor(_mActivity.getColor(R.color.tw_yellow));
        } else {
            iconFollow.setImageResource(R.drawable.icon_store_favorite_grey);
            tvFollow.setText(R.string.text_follow);
            tvFollow.setTextColor(_mActivity.getColor(R.color.tw_black));
        }
    }

    private void updateThumbView() {
        if (isLike == Constant.ONE) {
            btnGoodsThumb.setImageResource(R.drawable.icon_good_like_red_mini);
        } else {
            btnGoodsThumb.setImageResource(R.drawable.icon_goods_thumb_grey);
        }
        tvThumbCount.setText(String.valueOf(goodsLike));
    }

    private void showSpecSelectPopup(int action) {

        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, action, 0, specList, specValueIdMap, selSpecValueIdList, buyNum, goodsInfoMap, currGalleryImageList,limitBuy,discountState,skuGalleryItemList))
                .show();
    }

    /**
     * 獲取商店券和平台券列表
     */
    private void loadCouponList() {
        try {
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate("commonId", commonId);
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("__params[%s]", params);

            Api.postUI(Api.PATH_GOODS_DETAIL_COUPON_LIST, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        // 【領券】優惠
                        storeVoucherList.clear();
                        EasyJSONArray couponList = responseObj.getSafeArray("datas.list");
                        if (couponList.length() > 0) {
                            for (Object object : couponList) {
                                LinearLayout linearLayout = new LinearLayout(_mActivity);
                                linearLayout.setBackgroundResource(R.color.tw_yellow);
                                // linearLayout.setPadding(2,2,2,2);
                                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                                TextView textView = new TextView(_mActivity);
                                textView.setHeight(Util.dip2px(_mActivity, 21));
                                textView.setWidth(Util.dip2px(_mActivity, 20));
                                textView.setGravity(Gravity.CENTER);
                                textView.setText("領");
                                textView.setTextColor(getResources().getColor(android.R.color.white, null));
                                textView.setTextSize(12);
                                textView.setBackgroundResource(R.color.tw_yellow);
                                TextView tvVoucher = new TextView(_mActivity);
                                tvVoucher.setTextSize(12);
                                tvVoucher.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
                                tvVoucher.setGravity(Gravity.CENTER);
                                tvVoucher.setBackgroundResource(R.drawable.yellow_voucher_bg);
                                tvVoucher.setPadding(Util.dip2px(_mActivity, 5.5f), 0,
                                        Util.dip2px(_mActivity, 5.5f), 0);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.rightMargin = Util.dip2px(_mActivity, 10);
                                linearLayout.addView(textView);
                                linearLayout.addView(tvVoucher, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                EasyJSONObject voucher = (EasyJSONObject) object;
                                int limitAmount = (int) voucher.getDouble("searchCouponActivityVo.limitAmount");
                                int couponPrice = (int) voucher.getDouble("searchCouponActivityVo.couponPrice");

                                if (limitAmount == 0) {
                                    // 如果為0，表示無門檻
                                    // tvVoucher.setText(String.format("$%d無門檻", couponPrice));
                                    tvVoucher.setText(String.format("%d元券", couponPrice));
                                } else {
                                    // tvVoucher.setText(String.format("滿%d減%d", limitAmount, couponPrice));
                                    tvVoucher.setText(String.format("%d元券", couponPrice));
                                }
                                llVoucherContainer.addView(linearLayout, layoutParams);

                                String useGoodsRangeExplain = voucher.getSafeString("searchCouponActivityVo.useGoodsRangeExplain");
                                int memberIsReceive = voucher.getInt("memberIsReceive");
                                int storeId = voucher.getInt("searchCouponActivityVo.storeId");
                                String limitAmountText = voucher.getSafeString("searchCouponActivityVo.limitAmountText");
                                String usableClientTypeText = voucher.getSafeString("searchCouponActivityVo.usableClientTypeText");
                                String useStartTime = voucher.getSafeString("searchCouponActivityVo.useStartTime");
                                String useEndTime = voucher.getSafeString("searchCouponActivityVo.useEndTime");
                                String storeAvatar = voucher.getSafeString("searchCouponActivityVo.storeAvatar");

                                int state = Constant.COUPON_STATE_UNRECEIVED;
                                if (memberIsReceive == 1) {
                                    state = Constant.COUPON_STATE_RECEIVED;
                                }
                                StoreVoucher storeVoucher = new StoreVoucher(storeId, 0, useGoodsRangeExplain, storeAvatar, couponPrice,
                                        limitAmountText, usableClientTypeText, useStartTime, useEndTime, state);
                                storeVoucher.searchSn = voucher.getSafeString("searchCouponActivityVo.searchSn");

                                storeVoucherList.add(storeVoucher);
                            }

                            rlVoucherList.setVisibility(VISIBLE);
                        }
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void loadGoodsDetail() {
        // 清空一下數據，以便可以重復加載
        specList.clear();
        specPairList.clear();
        selSpecValueIdList.clear();
        storeFriendsItemList.clear();
        goodsConformItemList.clear();
        inStorePersonItemList.clear();
        String token = User.getToken();
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        String path = Api.PATH_GOODS_DETAIL + "/" + commonId;
        EasyJSONObject params = EasyJSONObject.generate();
        if (!StringUtil.isEmpty(token)) {
            try {
                params.set("token", token);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        SLog.info("path[%s], params[%s]", path, params);
        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject goodsDetail = responseObj.getSafeObject("datas.goodsDetail");
                    SLog.info("goodsDetail[%s]", goodsDetail);

                    goodsName = goodsDetail.getSafeString("goodsName");
                    tvGoodsName.setText(goodsName);
                    jingle = goodsDetail.getSafeString("jingle");
                    tvGoodsJingle.setText(jingle);
                    //產品状态 可以购买1，下架0
                    int status = goodsDetail.getInt("goodsStatus");
                    //針對用戶id限購 可以购买0，提示限購-1
                    setGoodsStatus(status);

                    promotionDiscountRate = (float) goodsDetail.getDouble("promotionDiscountRate");
                    tvPromotionDiscountRate.setText("限時" + promotionDiscountRate + "折");

                    String goodsNationalFlagUrl = StringUtil.normalizeImageUrl(responseObj.getSafeString("datas.goodsCountry.nationalFlag"));
                    Glide.with(GoodsDetailFragment.this).load(goodsNationalFlagUrl).into(imgGoodsNationalFlag);

                    tvGoodsCountryName.setText(responseObj.getSafeString("datas.goodsCountry.countryCn"));

                    if (responseObj.exists("datas.address.areaInfo")) {
                        String areaInfo = responseObj.getSafeString("datas.address.areaInfo");
                        tvShipTo.setText(areaInfo);
                    }


                    if (responseObj.exists("datas.freight.allowSend")) {
                        allowSend = responseObj.getInt("datas.freight.allowSend");
                        float freightAmount = (float) responseObj.getDouble("datas.freight.freightAmount");
                        if (allowSend == 1) {
                            tvFreightAmount.setText(getString(R.string.text_freight) + StringUtil.formatFloat(freightAmount));
                        } else {
                            tvFreightAmount.setText(getString(R.string.text_not_allow_send));
                        }
                    }

                    goodsPrice = Util.getSpuPrice(goodsDetail);
                    tvGoodsPrice.setText(StringUtil.formatFloat(goodsPrice));
                    UiUtil.toPriceUI(tvGoodsPrice,0);

                    // 是否点赞
                    isLike = goodsDetail.getInt("isLike");
                    goodsLike = goodsDetail.getInt("goodsLike");
                    updateThumbView();

                    // 是否關注
                    isFavorite = goodsDetail.getInt("isFavorite");
                    updateFavoriteView();

                    // 月銷量
                    int goodsSaleNum = goodsDetail.getInt("goodsSaleNum");
                    // 想粉數
                    int goodsFavorite = goodsDetail.getInt("goodsFavorite");
                    storeId = responseObj.getInt("datas.storeInfo.storeId");

                    int goodsClick = goodsDetail.getInt("goodsClick");
                    String goodsClickText = "瀏覽" + StringUtil.formatPostView(goodsClick) + "次";
                    tvViewCount.setText(goodsClickText);

                    tvFansCount.setText(getString(R.string.text_fans) + goodsFavorite);
                    tvGoodsSale.setText(getString(R.string.text_monthly_sale) + goodsSaleNum + getString(R.string.text_monthly_sale_unit));
                    // 隱藏銷量信息
                    tvGoodsSale.setVisibility(View.INVISIBLE);

                    // 下面開始組裝規格數據列表
                    EasyJSONArray specJson = goodsDetail.getSafeArray("specJson");
                    SLog.info("specJson[%s]", specJson);
                    for (Object object : specJson) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        Spec specItem = new Spec();

                        specItem.specId = specObj.getInt("specId");
                        specItem.specName = specObj.getSafeString("specName");

                        EasyJSONArray specValueList = specObj.getSafeArray("specValueList");

                        boolean first = true;
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValue = (EasyJSONObject) object2;
                            int specValueId = specValue.getInt("specValueId");
                            String specValueName = specValue.getSafeString("specValueName");
                            String imageSrc = specValue.getSafeString("imageSrc");

                            SpecValue specValueItem = new SpecValue(specValueId, specValueName, imageSrc);
                            specItem.specValueList.add(specValueItem);

                            if (first) {
                                specPairList.add(new SpecPair(specItem.specName, specValueName));
                                first = false;
                            }
                        }
                        specList.add(specItem);
                    }

                    SLog.info("fullSpecs[%s]", Util.formatSpecString(specPairList));
                    tvCurrentSpecs.setText(Util.formatSpecString(specPairList));

                    String goodsSpecValues = goodsDetail.getSafeString("goodsSpecValues");
                    EasyJSONArray goodsSpecValueArr = (EasyJSONArray) EasyJSONArray.parse(goodsSpecValues);
                    for (Object object : goodsSpecValueArr) {
                        EasyJSONObject mapItem = (EasyJSONObject) object;
                        SLog.info("kkkkey[%s], vvvalue[%s]", mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                        // 有些沒有規格的產品，只有一個goodsId，且specValueIds字符串為空串
                        specValueIdMap.put(mapItem.getSafeString("specValueIds"), mapItem.getInt("goodsId"));
                    }

                    SLog.info("specList.size[%d]", specList.size());
                    for (Spec spec : specList) {
                        SLog.info("SPEC_DUMP[%s]", spec);
                    }

                    if (goodsDetail.exists("detailVideo")) {
                        String detailVideoUrl = goodsDetail.getSafeString("detailVideo");
                        SLog.info("detailVideoUrl[%s]", detailVideoUrl);
                        if (!StringUtil.isEmpty(detailVideoUrl)) {
                            detailVideoId = Util.getYoutubeVideoId(detailVideoUrl);
                            if (!StringUtil.isEmpty(detailVideoId)) { // 如果有介紹視頻
                                String coverUrl = String.format("https://img.youtube.com/vi/%s/sddefault.jpg", detailVideoId);
                                View detailVideoView = LayoutInflater.from(_mActivity).inflate(R.layout.goods_detail_video, llGoodsDetailImageContainer, false);
                                ImageView imgDetailVideoCover = detailVideoView.findViewById(R.id.img_detail_video_cover);
                                Glide.with(_mActivity).load(coverUrl).centerCrop().into(imgDetailVideoCover);

                                detailVideoView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.playYoutubeVideo(_mActivity, detailVideoId);
                                    }
                                });

                                llGoodsDetailImageContainer.addView(detailVideoView);
                            }
                        }
                    }

                    // 產品詳情圖片
                    int imageIndex = 0;
                    EasyJSONArray easyJSONArray = responseObj.getSafeArray("datas.goodsMobileBodyVoList");
                    for (Object object : easyJSONArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        GoodsMobileBodyVo goodsMobileBodyVo = new GoodsMobileBodyVo();
                        goodsMobileBodyVo.setType(easyJSONObject.getSafeString("type"));
                        goodsMobileBodyVo.setValue(easyJSONObject.getSafeString("value"));
                        goodsMobileBodyVo.setWidth(easyJSONObject.getInt("width"));
                        goodsMobileBodyVo.setHeight(easyJSONObject.getInt("height"));

                        if (goodsMobileBodyVo.getType().equals("image")) {
                            String imageUrl = StringUtil.normalizeImageUrl(easyJSONObject.getSafeString("value"));

                            DataImageView imageView = new DataImageView(_mActivity);
                            imageView.setCustomData(imageIndex);
                            imageView.setAdjustViewBounds(true);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SLog.info("imageUrl[%s]", imageUrl);
                                    int currImageIndex = (int) ((DataImageView) v).getCustomData();
                                    Util.startFragment(ImageFragment.newInstance(currImageIndex, goodsDetailImageList));
                                }
                            });
                            // 加上.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)，防止加載長圖模糊的問題
                            // 參考 Glide加载图片模糊问题   https://blog.csdn.net/sinat_26710701/article/details/89384579
                            Glide.with(llGoodsDetailImageContainer).load(imageUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);
                            llGoodsDetailImageContainer.addView(imageView);

                            goodsDetailImageList.add(StringUtil.normalizeImageUrl(imageUrl));

                            imageIndex++;
                        } else if (goodsMobileBodyVo.getType().equals("text")) {
                            TextView textView = new TextView(_mActivity);
                            textView.setText(goodsMobileBodyVo.getValue());
                            textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                            textView.setTextSize(16);
                            textView.setGravity(Gravity.CENTER);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.leftMargin = Util.dip2px(_mActivity, 20);
                            layoutParams.rightMargin = layoutParams.leftMargin;
                            layoutParams.topMargin = Util.dip2px(_mActivity, 10);
                            layoutParams.bottomMargin = layoutParams.topMargin;
                            textView.setLayoutParams(layoutParams);

                            llGoodsDetailImageContainer.addView(textView);
                        }

                    }

                    // 視頻
                    boolean hasGoodsVideo = false;
                    if (goodsDetail.exists("goodsVideo")) {
                        String goodsVideoUrl = goodsDetail.getSafeString("goodsVideo");
                        if (!StringUtil.isEmpty(goodsVideoUrl)) {
                            goodsVideoId = Util.getYoutubeVideoId(goodsVideoUrl);
                            if (!StringUtil.isEmpty(goodsVideoId)) {
                                hasGoodsVideo = true;
                            }
                        }
                    }

                    if (hasGoodsVideo) {
                        btnPlay.setVisibility(VISIBLE);
                    } else {
                        btnPlay.setVisibility(GONE);
                    }
                    limitBuy = goodsDetail.getInt("limitBuy");

//                     goodsDetail.limitBuy   >0限購數量，0不限購，-1限購
//                    goodsDetail .tariffEnable  1是0否支持跨城購
//                    goodsDetail .goodsInfoVoList[]：tariffAmount   SKU跨城購稅費

                    SLog.info("goodsDetail exists,discount[%s]", goodsDetail.exists("discount"));


                    // 【贈品】優惠
                    boolean first = true;
                    EasyJSONArray goodsInfoVoList = responseObj.getSafeArray("datas.goodsDetail.goodsInfoVoList");
                    for (Object object : goodsInfoVoList) {
                        GoodsInfo goodsInfo = new GoodsInfo();

                        EasyJSONObject goodsInfoVo = (EasyJSONObject) object;
                        int goodsId = goodsInfoVo.getInt("goodsId");
                        EasyJSONArray giftVoList = goodsInfoVo.getSafeArray("giftVoList");

                        List<GiftItem> giftItemList = new ArrayList<>();
                        for (Object object2 : giftVoList) {
                            EasyJSONObject giftVo = (EasyJSONObject) object2;

                            GiftItem giftItem = (GiftItem) EasyJSONBase.jsonDecode(GiftItem.class, giftVo.toString());
                            giftItemList.add(giftItem);
                        }
                        // SLog.info("ddddddddddddddddddxxxxx[%d][%d]", goodsId, giftItemList.size());
                        giftMap.put(goodsId, giftItemList);
                        // 如果從外面傳進來的參數沒有指定goodsId, 則默認選中第一項sku
                        if (currGoodsId == 0 && first) {
                            currGoodsId = goodsId;
                            SLog.info("默認選中第一項sku, goodsId[%d]", goodsId);
                        }

                        // 獲取每個sku對應的輪播圖列表
                        List<String> goodsGalleryImageList = new ArrayList<>();
                        EasyJSONArray goodsImageList = goodsInfoVo.getSafeArray("goodsImageList");
                        for (Object object2 : goodsImageList) {
                            EasyJSONObject goodsImageObj = (EasyJSONObject) object2;
                            String imageName = goodsImageObj.getSafeString("imageName");
                            goodsGalleryImageList.add(imageName);
                        }

                        skuGoodsGalleryMap.put(goodsId, goodsGalleryImageList);

                        goodsInfo.goodsId = goodsId;
                        goodsInfo.commonId = commonId;
                        goodsInfo.goodsFullSpecs = goodsInfoVo.getSafeString("goodsFullSpecs");
                        goodsInfo.specValueIds = goodsInfoVo.getSafeString("specValueIds");
                        goodsInfo.goodsPrice0 =  goodsInfoVo.getDouble("goodsPrice0");
                        goodsInfo.price = Util.getSkuPrice(goodsInfoVo);
                        SLog.info("__goodsInfo.price[%s], goodsInfoVo[%s]", goodsInfo.price, goodsInfoVo.toString());
                        goodsInfo.imageSrc = goodsInfoVo.getSafeString("imageSrc");
                        goodsInfo.goodsStorage = goodsInfoVo.getInt("goodsStorage");
                        goodsInfo.reserveStorage = goodsInfoVo.getInt("reserveStorage");
                        goodsInfo.limitAmount = goodsInfoVo.getInt("limitAmount");
                        goodsInfo.unitName = goodsInfoVo.getSafeString("unitName");
                        goodsInfo.goodsName = goodsName;
                        goodsInfo.promotionType =goodsInfoVo.getInt("promotionType");

                        goodsInfoMap.put(goodsId, goodsInfo);

                        SkuGalleryItem skuGalleryItem = new SkuGalleryItem(
                                goodsId,
                                StringUtil.normalizeImageUrl(goodsInfo.imageSrc),
                                goodsInfoVo.getSafeString("goodsSpecString"),
                                goodsInfo.goodsPrice0,
                                goodsInfo.specValueIds
                        );
                        skuGalleryItemList.add(skuGalleryItem);

                        first = false;
                    }
                    // 限時折扣
                    EasyJSONObject discount = goodsDetail.getObject("discount");
                    if (discount != null) {
                        // 表明有限時折扣活動
                        String startTime = discount.getSafeString("startTime");
                        String endTime = discount.getSafeString("endTime");
                        Jarbon date = Jarbon.parse(endTime);
                        Jarbon startDate = Jarbon.parse(startTime);
                        discountStartTime = startDate.getTimestamp();
                        SLog.info("startTime[%s],endTime[%s],discountStart[%s]", startTime, endTime, discountState);
                        discountEndTime = date.getTimestamp();
                        SLog.info("discountStartTime[%d],discountEndTime[%d]", discountStartTime, discountEndTime);
                    }

                    // 【滿減】優惠
                    EasyJSONArray conformList = responseObj.getSafeArray("datas.goodsDetail.conformList");
                    if (conformList.length() > 0) {
                        first = true;
                        StringBuilder conformText = new StringBuilder();
                        for (Object object : conformList) {
                            if (!first) {
                                conformText.append(" / ");
                            }

                            EasyJSONObject conform = (EasyJSONObject) object;
                            conformText.append(conform.getSafeString("shortRule"));

                            GoodsConformItem goodsConformItem = new GoodsConformItem();
                            goodsConformItem.conformId = conform.getInt("conformId");
                            goodsConformItem.startTime = conform.getSafeString("startTime");
                            goodsConformItem.endTime = conform.getSafeString("endTime");
                            goodsConformItem.limitAmount = conform.getInt("limitAmount");
                            goodsConformItem.conformPrice = conform.getInt("conformPrice");
                            goodsConformItem.isFreeFreight = conform.getInt("isFreeFreight");
                            goodsConformItem.templateId = conform.getInt("templateId");
                            goodsConformItem.templatePrice = conform.getInt("templatePrice");

                            EasyJSONArray giftVoList = conform.getSafeArray("giftVoList");
                            if (giftVoList.length() > 0) {
                                goodsConformItem.giftVoList = new ArrayList<>();
                                for (Object object2 : giftVoList) {
                                    GiftVo giftVo = (GiftVo) EasyJSONBase.jsonDecode(GiftVo.class, object2.toString());
                                    SLog.info("giftVo[%s]", giftVo);
                                    goodsConformItem.giftVoList.add(giftVo);
                                }
                            }


                            goodsConformItemList.add(goodsConformItem);
                            first = false;
                        }
                        btnShowConform.setVisibility(VISIBLE);
                    }

                    // 初始化默認選擇
                    SLog.info("初始化默認選擇 currGoodsId[%d]", currGoodsId);
                    selectSku(currGoodsId);

                    commentCount = responseObj.getInt("datas.wantCommentVoInfoCount");
                    tvCommentCount.setText(String.format(getString(R.string.text_comment) + "(%d)", commentCount));
                    tvGoodsCommentCount.setText(String.valueOf(commentCount));

                    SLog.info("commentCount[%d]", commentCount);
                    if (commentCount > 0) {
                        // 如果有說說，顯示首條說說
                        EasyJSONObject wantCommentVoInfo = responseObj.getSafeObject("datas.wantCommentVoInfoList[0]");

                        String commenterAvatarUrl = StringUtil.normalizeImageUrl(wantCommentVoInfo.getSafeString("memberVo.avatar"));
                        SLog.info("commenterAvatarUrl[%s]", commenterAvatarUrl);
                        if (StringUtil.useDefaultAvatar(commenterAvatarUrl)) {
                            Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imgCommenterAvatar);
                        } else {
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(commenterAvatarUrl)).centerCrop().into(imgCommenterAvatar);
                        }

                        tvCommenterNickname.setText(wantCommentVoInfo.getSafeString("memberVo.nickName"));
                        String comment = wantCommentVoInfo.getSafeString("content");
                        if (StringUtil.isEmpty(comment)) {  // 如果說說內容為空，表明為圖片說說
                            comment = "[圖片]";
                        }
                        tvComment.setText(StringUtil.translateEmoji(_mActivity, comment, (int) tvComment.getTextSize()));
                    } else {
                        // 如果沒有說說，隱藏相應的控件
                        llFirstCommentContainer.setVisibility(GONE);
                    }

                    // 好友
                    inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_LABEL, null, null, getString(R.string.text_friend)));
                    EasyJSONArray friends = responseObj.getSafeArray("datas.memberAccessStatVo.friends");
                    if (friends.length() > 0) {
                        for (Object object : friends) {
                            EasyJSONObject friend = (EasyJSONObject) object;

                            String memberName = friend.getSafeString("memberName");
                            String avatar = friend.getSafeString("avatar");
                            String nickname = friend.getSafeString("nickName");

                            InStorePersonItem inStorePersonItem = new InStorePersonItem(InStorePersonItem.TYPE_ITEM, memberName, avatar, nickname);
                            inStorePersonItemList.add(inStorePersonItem);
                        }
                        inStorePersonCount += friends.length();
                    } else {
                        inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_EMPTY_HINT, null, null, null));
                    }


                    // 城友
                    inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_LABEL, null, null, getString(R.string.text_store_friend)));
                    EasyJSONArray members = responseObj.getSafeArray("datas.memberAccessStatVo.members");
                    if (members.length() > 0) {
                        for (Object object : members) {
                            EasyJSONObject friend = (EasyJSONObject) object;

                            String memberName = friend.getSafeString("memberName");
                            String avatar = friend.getSafeString("avatar");
                            String nickname = friend.getSafeString("nickName");

                            StoreFriendsItem storeFriendsItem = new StoreFriendsItem(memberName, avatar);
                            storeFriendsItemList.add(storeFriendsItem);

                            InStorePersonItem inStorePersonItem = new InStorePersonItem(InStorePersonItem.TYPE_ITEM, memberName, avatar, nickname);
                            inStorePersonItemList.add(inStorePersonItem);
                        }
                        inStorePersonCount += members.length();
                    } else {
                        inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_EMPTY_HINT, null, null, null));
                    }


                    isDataValid = true;
                    adapter.setNewData(storeFriendsItemList);

                    // 跨城購標識
                    tariffEnable = goodsDetail.getInt("tariffEnable");
                    if (tariffEnable == Constant.TRUE_INT) {
                        iconTariff.setVisibility(VISIBLE);
                    } else {
                        iconTariff.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateDiscount(GoodsInfo goodsInfo) {
        if (goodsInfo.promotionType == Constant.FALSE_INT) {
            discountState = NO_DISCOUNT;
        } else {
            float time = System.currentTimeMillis() / 1000 - discountStartTime;
            if (time < 0) {
                discountState = BEFORE_DISCOUNT;
            } else {
                time = System.currentTimeMillis() / 1000 - discountEndTime;
                if (time < 0) {
                    discountState = IN_DISCOUNT;
                } else {
                    discountState = OUT_DISCOUNT;
                }
            }
        }
        SLog.info("discountState[%d],goodsInfo.promotionType[%d]",discountState,goodsInfo.promotionType);
    }

    private void setGoodsStatus(int status) {
        goodsStatus = status;
        if (goodsStatus == 0) {
            getView().findViewById(R.id.ll_goods_take_off).setVisibility(VISIBLE);
            getView().findViewById(R.id.btn_buy).setBackgroundResource(R.drawable.icon_take_off_buy);
            getView().findViewById(R.id.btn_add_to_cart).setBackgroundResource(R.drawable.icon_take_off_cart);

        }
    }

    private void showGiftHint(List<GiftItem> giftItemList) {
        StringBuilder giftText = new StringBuilder("買就送");
        for (GiftItem giftItem : giftItemList) {
            giftText.append(giftItem.goodsName);
            giftText.append(" ");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        SLog.info("onDestroyView");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_SELECT_SPECS) {
            // 選擇規則完成
            SLog.info("data[%s]", message.data);
            try {
                EasyJSONObject easyJSONObject = EasyJSONObject.parse((String) message.data);
                int goodsId = easyJSONObject.getInt("goodsId");
                buyNum = easyJSONObject.getInt("quantity");
                selectSku(goodsId);
                showPriceTag(goodsInfoMap.get(goodsId));
            } catch (Exception e) {

            }
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_RELOAD_GOODS_DETAIL) {
            SLog.info("EBMessageType.MESSAGE_TYPE_RELOAD_GOODS_DETAIL");
            isDataValid = false;
        }
    }

    /**
     * 根據當前選中的specValueId列表，生成規格字符串
     *
     * @return
     */
    public String getFullSpecs() {
        int index = 0;
        List<SpecPair> specPairList = new ArrayList<>();
        for (Integer specValueId : selSpecValueIdList) {
            Spec spec = specList.get(index);

            String specValueName = "";
            for (SpecValue specValue : spec.specValueList) {
                if (specValueId == specValue.specValueId) {
                    specValueName = specValue.specValueName;
                    break;
                }
            }
            specPairList.add(new SpecPair(spec.specName, specValueName));
            ++index;
        }
        return Util.formatSpecString(specPairList);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        if(resultCode!=RESULT_OK){
            return;
        }
        if (requestCode == RequestCode.ADD_COMMENT.ordinal()) {
            SLog.info("updateComment");
            updateComment();
            return;
        }
        // 從哪個Fragment返回
        String from = data.getString("from");
        SLog.info("requestCode[%d], resultCode[%d], from[%s]", requestCode, resultCode, from);
        if (AddrManageFragment.class.getName().equals(from) || AddAddressFragment.class.getName().equals(from)) {
            // 從地址管理Fragment返回 或 從地址添加Fragment返回
            boolean isNoAddress = data.getBoolean("isNoAddress", false); // 標記是否刪除了所有地址
            if (isNoAddress) {
                // tvShipTo.setText("");
                return;
            }

            // 上一級Fragment返回的地址項
            AddrItem addrItem = data.getParcelable("addrItem");
            if (addrItem == null) {
                // tvShipTo.setText("");
                return;
            }
            SLog.info("addrItem: %s", addrItem);
            tvShipTo.setText(addrItem.areaInfo);

            updateFreight(addrItem);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("here onsupport");
        if (discountState==IN_DISCOUNT||discountState== BEFORE_DISCOUNT) {
            startCountDown();
        }

        if (!isDataValid) {
            loadCouponList();
            loadGoodsDetail();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("onSupportInvisible");
        stopCountDown();
    }

    private void startCountDown() {
        if (timer == null) {
            timer = new Timer();
        }

        if (isCountingDown) {
            return;
        }
        // 定时服务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long threadId = Thread.currentThread().getId();
                // SLog.info("threadId[%d]", threadId);

                TimeInfo timeInfo = Time.diff((int) (System.currentTimeMillis() / 1000), discountState == 2 ? discountEndTime : discountStartTime);
                if (timeInfo == null) {
                    return;
                }


                Message message = new Message();
                message.obj = timeInfo;

                if (countDownHandler != null) {
                    countDownHandler.sendMessage(message);
                }
            }
        }, 500, 1000);  // 0.5秒后启动，每隔1秒运行一次
    }

    private void stopCountDown() {
        isCountingDown = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 選擇當前Sku
     *
     * @param goodsId
     */
    private void selectSku(int goodsId) {
        currGoodsId = goodsId;

        GoodsInfo goodsInfo = goodsInfoMap.get(goodsId);
        if (goodsInfo == null) {
            return;
        }

        // 設置產品輪播圖
        currGalleryImageList = skuGoodsGalleryMap.get(goodsId);
        if (currGalleryImageList == null) {
            return;
        }

        goodsGalleryAdapter.setNewData(currGalleryImageList);
        pageIndicatorView.setVisibility(View.VISIBLE);
        if (currGalleryImageList.size() > 1) {
            resetGalleryPosition();
        } else {
            pageIndicatorView.setCount(1);
            pageIndicatorView.setVisibility(VISIBLE);
        }


        // 滿優惠數
        conformCount = goodsConformItemList.size();
        // 贈品數
        giftCount = 0;
        List<GiftItem> giftItemList = giftMap.get(currGoodsId);
        if (giftItemList != null && giftItemList.size() > 0) { // 獲取贈品數量
            giftCount = giftItemList.size();
        }
        SLog.info("currGoodsId[%d], giftCount[%d], conformCount[%d]", currGoodsId, giftCount, conformCount);

        int btnShowConformVisibility = VISIBLE;
        if (giftCount > 0) {
            tvGiftHint.setVisibility(VISIBLE);
        } else {
            tvGiftHint.setVisibility(GONE);
        }

        if (conformCount > 0) {
            tvConformHint.setVisibility(VISIBLE);
        } else {
            tvConformHint.setVisibility(GONE);
        }

        if (giftCount < 1 && conformCount < 1) {
            btnShowConformVisibility = GONE;
        }
        btnShowConform.setVisibility(btnShowConformVisibility);

        SLog.info("goodsInfo.specValueIds[%s]", goodsInfo.specValueIds);
        selSpecValueIdList = StringUtil.specValueIdsToList(goodsInfo.specValueIds);

        String fullSpecs = goodsInfo.goodsFullSpecs;
        SLog.info("fullSpecs[%s]", fullSpecs);
        tvCurrentSpecs.setText(fullSpecs);

        SLog.info("goodsPrice0[]%f,goodsprice[%f] ", goodsInfo.goodsPrice0, goodsInfo.price);
        showPriceTag(goodsInfo);

        // 看是否有現貨，如果沒有，則顯示到貨通知
        int finalStorage = goodsInfo.getFinalStorage();
        SLog.info("finalStorage[%d]", finalStorage);
        if (finalStorage > 0) {
            btnArrivalNotice.setVisibility(GONE);
            llStorageOkContainer.setVisibility(VISIBLE);
        } else {
            btnArrivalNotice.setVisibility(VISIBLE);
            llStorageOkContainer.setVisibility(GONE);
        }
    }

    /**
     * 是顯示價格標簽還是顯示折扣活動信息
     *
     * @param goodsInfo sku信息
     *
     */
    private void showPriceTag(GoodsInfo goodsInfo) {
        updateDiscount(goodsInfo);
        boolean showDiscountInfo = discountState==BEFORE_DISCOUNT||discountState==IN_DISCOUNT;
        rlPriceTag.setVisibility(showDiscountInfo?GONE:VISIBLE);
        rlDiscountInfoContainer.setVisibility(showDiscountInfo?VISIBLE:GONE);
        if (showDiscountInfo) {
            tvGoodsPriceFinal.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 1));
            UiUtil.toPriceUI(tvGoodsPrice,1);
            tvGoodsPriceOriginal.setText("原價 " + StringUtil.formatPrice(_mActivity, goodsInfo.goodsPrice0, 0));

            startCountDown();
        } else {
            tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 0));
            UiUtil.toPriceUI(tvGoodsPrice,13);

            stopCountDown();
        }
    }

    /**
     * 用戶切換了送貨目的地
     * 更新運費的顯示
     */
    private void updateFreight(AddrItem addrItem) {
        int areaId2 = 0;

        if (addrItem.areaIdList.size() >= 2) {
            areaId2 = addrItem.areaIdList.get(1);
        }

        if (areaId2 == 0) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId,
                "buyNum", buyNum,
                "areaId2", areaId2);
        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_REFRESH_FREIGHT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject freight = responseObj.getSafeObject("datas.freight");
                    allowSend = freight.getInt("allowSend");
                    if (allowSend == 1) {
                        float freightAmount = (float) freight.getDouble("freightAmount");
                        tvFreightAmount.setText(getString(R.string.text_freight) + StringUtil.formatFloat(freightAmount));
                    } else {
                        tvFreightAmount.setText(getString(R.string.text_not_allow_send));
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void resetGalleryPosition() {
        rvGalleryImageList.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 先去到大概中間的位置
                int targetPosition = Integer.MAX_VALUE / 2;
                // 然后去到倍數開始的位置
                targetPosition -= (targetPosition % currGalleryImageList.size());

                currGalleryPosition = targetPosition;
                rvGalleryImageList.scrollToPosition(currGalleryPosition);
                SLog.info("currGalleryPosition[%d]", currGalleryPosition);

                pageIndicatorView.setCount(currGalleryImageList.size());
                pageIndicatorView.setSelection(0);
            }
        }, 50);
    }

    /**
     * 获取当前显示的图片的position
     *
     * @return
     */
    private int getCurrPosition() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvGalleryImageList.getLayoutManager();
        int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        return position;
    }
}
