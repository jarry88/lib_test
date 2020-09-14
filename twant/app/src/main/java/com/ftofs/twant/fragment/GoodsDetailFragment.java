package com.ftofs.twant.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.GoodsGalleryAdapter;
import com.ftofs.twant.adapter.StoreFriendsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.GroupBuyStatus;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.constant.UmengAnalyticsPageName;
import com.ftofs.twant.domain.bargain.Bargain;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.entity.CustomActionData;
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
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.LogUtil;
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
import com.ftofs.twant.widget.TwConfirmPopup;
import com.ftofs.twant.widget.ViewMoreGroupPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.rd.PageIndicatorView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 80;
    int discountState;//0沒有折扣信息、1未開始、2已開始、3已結束
    private static final int NO_DISCOUNT = 0;
    private static final int BEFORE_DISCOUNT=1;
    private static final int IN_DISCOUNT=2;
    private static final int OUT_DISCOUNT=3;

    // 砍價Id
    int bargainId = Constant.INVALID_BARGAIN_ID;
    int bargainOpenId = Constant.INVALID_BARGAIN_OPEN_ID;
    int bargainState = Constant.BARGAIN_STATE_NOT_STARTED;

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

    double promotionDiscountRate;
    TextView tvPromotionDiscountRate;

    // public static final int MAX_SHOW_GROUP_MEMBER_COUNT = 11; // 最多显示多少个团购成员
    GroupBuyStatus currGroupBuyStatus = GroupBuyStatus.CLOSED;  // 当前的团购活动状态

    int groupRequireNum = 0;  // 成團所需人數
    // List<String> groupMemberAvatarList = new ArrayList<>();  // 參團用戶頭像
    TextView tvGroupBuyLabel;
    LinearLayout llGroupInfoContainer;
    int twLightGrey;
    TextView tvGroupBuyCountDownTitle;
    TextView tvGroupRemainDay;
    TextView tvGroupRemainHour;
    TextView tvGroupRemainMinute;
    TextView tvGroupRemainSecond;
    TextView tvGroupPrice;
    TextView tvGroupOriginalPrice;
    TextView tvGroupDiscountAmount;
    boolean currSKUGroupBuy; // 當前SKU是否有參與團購

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

    boolean isDataValid;

    LinearLayout llVoucherContainer;
    ScrollView scrollViewContainer;
    RelativeLayout rlTopBarContainer;
    private SimpleTabManager simpleTabManager;
    CommentListFragment commentListFragment;
    ConstraintLayout llPage1;
    LinearLayout llPage2;
    LinearLayout llPage3;
    LinearLayout bottomBar;
    LinearLayout llFloatButton;
    private int commentChannel = Constant.COMMENT_CHANNEL_GOODS;//產品
    private Handler mHandler;
    private boolean showFloatButton = true;
    private boolean isScrolling = false;

    private ViewPagerFragment viewPagerFragment;
    private int goodsStatus=1;
    private RelativeLayout preTopBarContainer;

    double goodsPrice;
    private int limitBuy;
    private int tariffEnable =Constant.FALSE_INT;
    private ImageView iconTariff;
    private View vwSeparator0;

    String goodsSpuImage;

    // 活動改造
    int promotionType = Constant.PROMOTION_TYPE_NONE;
    long promotionCountDownTime;

    TextView btnBargain;

    // 倒計時
    CountDownTimer countDownTimer;

    // 倒計時類型 future:距開始 ongoing:距結束
    public static final String COUNT_DOWN_TYPE_BEGIN = "future";  // 距開始
    public static final String COUNT_DOWN_TYPE_END = "ongoing";   // 距結束
    String promotionCountDownTimeType;  // 倒計時類型
    int groupOpenCount; // #正在拼團數量;
    LinearLayout llGroupListContainer;
    long promotionStartTime;
    long promotionEndTime;

    // 商品详情页最多显示2团，下面这两个为结束时间
    long group1EndTime;
    long group2EndTime;

    TextView tvCountDownTime1;
    TextView tvCountDownTime2;
    View btnQuickJoinGroup1; // 快速成團1
    int goId1 = Constant.INVALID_GO_ID;
    View btnQuickJoinGroup2; // 快速成團2
    int goId2 = Constant.INVALID_GO_ID;

    TextView btnBuy;
    TextView btnConsult;
    TextView btnAddToCart;
    ImageView btnAddToCartBg;

    TextView tvBargainRemainDay;
    TextView tvBargainRemainHour;
    TextView tvBargainRemainMinute;
    TextView tvBargainRemainSecond;
    private int goodsModel;
    private FrameLayout flAddToCart;

    TextView tvSecKillRemainDay;
    TextView tvSecKillRemainHour;
    TextView tvSecKillRemainMinute;
    TextView tvSecKillRemainSecond;

    View flSecKillLabel;
    View rlSecKillContainer;
    TextView tvSecKillPrice;
    TextView tvSecKillOriginalPrice;

    @Override
    public void onSimpleCall(Object data) {
        try {
            if (data instanceof CustomActionData) {
                CustomActionData customActionData = (CustomActionData) data;
                // 編輯彈窗保存時調用
                if (CustomAction.CUSTOM_ACTION_SELECT_JOIN_GROUP.ordinal() == customActionData.action) {
                    EasyJSONObject dataObj = (EasyJSONObject) customActionData.data;

                    int goId = dataObj.getInt("goId");
                    showSpecSelectPopup(Constant.ACTION_BUY, goId);
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

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

    /**
     * 構建產品詳情頁面
     *
     * @param commonId spuId
     * @param goodsId  skuId, 用于選中指定的sku，如果傳0,表示默認選中第一個sku
     * @param bargainId 砍價Id
     * @return
     */
    public static GoodsDetailFragment newInstance(int commonId, int goodsId, int bargainId) {
        Bundle args = new Bundle();

        args.putInt("commonId", commonId);
        args.putInt("goodsId", goodsId);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);
        fragment.bargainId = bargainId;

        return fragment;
    }

    public static GoodsDetailFragment newInstance(int commonId, int goodsId) {
        return newInstance(commonId, goodsId, Constant.INVALID_BARGAIN_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SLog.info("onViewCreated");

        twLightGrey = getResources().getColor(R.color.tw_light_grey, null);
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
        bottomBar = view.findViewById(R.id.rl_bottom_bar_container);

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

        // 团购相关控件
        tvGroupBuyLabel = view.findViewById(R.id.tv_group_buy_label);
        tvGroupBuyLabel.setVisibility(GONE);
        llGroupInfoContainer = view.findViewById(R.id.ll_group_info_container);
        llGroupInfoContainer.setVisibility(GONE);
        Util.setOnClickListener(view, R.id.btn_join_group, this);
        tvGroupBuyCountDownTitle = view.findViewById(R.id.tv_group_buy_count_down_title);
        tvGroupRemainDay = view.findViewById(R.id.tv_group_remain_day);
        tvGroupRemainHour = view.findViewById(R.id.tv_group_remain_hour);
        tvGroupRemainMinute = view.findViewById(R.id.tv_group_remain_minute);
        tvGroupRemainSecond = view.findViewById(R.id.tv_group_remain_second);
        llGroupListContainer = view.findViewById(R.id.ll_group_list_container);

        tvGroupPrice = view.findViewById(R.id.tv_group_price);
        tvGroupOriginalPrice = view.findViewById(R.id.tv_group_original_price);
        tvGroupDiscountAmount = view.findViewById(R.id.tv_group_discount_amount);

        tvCountDownTime1 = view.findViewById(R.id.tv_count_down_time_1);
        tvCountDownTime2 = view.findViewById(R.id.tv_count_down_time_2);

        tvBargainRemainDay = view.findViewById(R.id.tv_bargain_remain_day);
        tvBargainRemainHour = view.findViewById(R.id.tv_bargain_remain_hour);
        tvBargainRemainMinute = view.findViewById(R.id.tv_bargain_remain_minute);
        tvBargainRemainSecond = view.findViewById(R.id.tv_bargain_remain_second);

        tvSecKillRemainDay = view.findViewById(R.id.tv_sec_kill_remain_day);
        tvSecKillRemainHour = view.findViewById(R.id.tv_sec_kill_remain_hour);
        tvSecKillRemainMinute = view.findViewById(R.id.tv_sec_kill_remain_minute);
        tvSecKillRemainSecond = view.findViewById(R.id.tv_sec_kill_remain_second);
        flSecKillLabel = view.findViewById(R.id.fl_sec_kill_label);
        rlSecKillContainer = view.findViewById(R.id.rl_sec_kill_container);
        tvSecKillPrice = view.findViewById(R.id.tv_sec_kill_price);
        tvSecKillOriginalPrice = view.findViewById(R.id.tv_sec_kill_original_price);

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
        vwSeparator0 = view.findViewById(R.id.vw_separator_0);

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
        btnBargain = view.findViewById(R.id.btn_bargain);
        btnBargain.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back_round, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search_round, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_menu_round, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);
        Util.setOnClickListener(view, R.id.btn_show_all_store_friends, this);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);
        flAddToCart = view.findViewById(R.id.fl_add_to_cart_bg);
        btnAddToCart.setOnClickListener(this);
        if (bargainId != Constant.INVALID_BARGAIN_ID) {
            btnAddToCart.setText("原價購買");
        }
        btnAddToCartBg = view.findViewById(R.id.btn_add_to_cart_bg);
        Util.setOnClickListener(view, R.id.btn_select_spec, this);
        Util.setOnClickListener(view, R.id.btn_select_addr, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_follow, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_shop, this);
        Util.setOnClickListener(view, R.id.btn_goto_cart, this);
        Util.setOnClickListener(view, R.id.btn_bottom_bar_customer_service, this);
        Util.setOnClickListener(view, R.id.btn_comment, this);
        Util.setOnClickListener(view, R.id.tv_goods_name, this);
        btnBuy = view.findViewById(R.id.btn_buy);
        btnConsult = view.findViewById(R.id.btn_consult);
        btnBuy.setOnClickListener(this);
        btnConsult.setOnClickListener(this);
        if (bargainId != Constant.INVALID_BARGAIN_ID) {
            btnBuy.setText("砍一刀");
        }
        Util.setOnClickListener(view, R.id.btn_view_more_group, this); // 查看更加的拼團
        btnQuickJoinGroup1 = view.findViewById(R.id.btn_quick_join_group_1);
        btnQuickJoinGroup1.setOnClickListener(this);
        btnQuickJoinGroup2 = view.findViewById(R.id.btn_quick_join_group_2);
        btnQuickJoinGroup2.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_view_bargain_instruction, this);

        Util.setOnClickListener(view, R.id.btn_bargain_customer_service, this);
        Util.setOnClickListener(view, R.id.btn_bargain_cart, this);

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

        if (Config.PROD) {
            MobclickAgent.onPageStart(UmengAnalyticsPageName.GOODS);

            HashMap<String, Object> analyticsDataMap = new HashMap<>();
            analyticsDataMap.put("commonId", commonId);
            MobclickAgent.onEventObject(TwantApplication.getInstance(), UmengAnalyticsActionName.GOODS, analyticsDataMap);
        }
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
        if (bargainId != Constant.INVALID_BARGAIN_ID) { // 砍價功能隱藏浮動按鈕
            return;
        }
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
        if (bargainId != Constant.INVALID_BARGAIN_ID) { // 砍價功能隱藏浮動按鈕
            return;
        }
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
            bottomBar.setVisibility(VISIBLE);
        } else if (id == R.id.stb_good_detail) {
            llPage2.setVisibility(VISIBLE);
            bottomBar.setVisibility(GONE);
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
        bottomBar.setVisibility(GONE);
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
            case R.id.tv_goods_name:
                ClipboardUtils.copyText(_mActivity, goodsName);
                ToastUtil.success(_mActivity, "該商品名稱已複製");
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
                if (bargainId != Constant.INVALID_BARGAIN_ID) { // 原價購買
                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, currGoodsId));
                    return;
                }
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
            case R.id.btn_bargain_cart:
            case R.id.btn_goto_cart:
                if (userId > 0) {
                    Util.startFragment(CartFragment.newInstance(true));
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_buy:
                if (bargainId != Constant.INVALID_BARGAIN_ID) { // 砍一刀
                    if (bargainOpenId != Constant.INVALID_BARGAIN_OPEN_ID) { // 如果本人已經砍完，則跳轉到詳情頁
                        Util.startFragment(BargainDetailFragment.newInstance(bargainOpenId, commonId, currGoodsId));
                        return;
                    }

                    bargain();
                    return;
                }
                if(goodsStatus==0){
                    ToastUtil.error(_mActivity,"商品已下架");
                    return;
                }
                if (userId > 0) {
                    if (allowSend == 0) {
                        ToastUtil.error(_mActivity, getString(R.string.not_allow_send_hint));
                        return;
                    }

                    if (currSKUGroupBuy) {
                        if (COUNT_DOWN_TYPE_BEGIN.equals(promotionCountDownTimeType)) { // 團購活動未開始
                            int from = (int) (System.currentTimeMillis() / 1000);
                            int to = (int) (promotionStartTime / 1000);

                            TimeInfo timeInfo = Time.diff(from, to);
                            if (timeInfo != null) {
                                String title = String.format("距離開團還有%d天%d時%d分，您是否要使用原價進行購買", timeInfo.day, timeInfo.hour, timeInfo.minute);
                                new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                                        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                                      .asCustom(new TwConfirmPopup(_mActivity, title,null   , "立即購買", "等等團購",new OnConfirmCallback() {
                                    @Override
                                    public void onYes() {
                                        SLog.info("onYes");
                                        showSpecSelectPopup(Constant.ACTION_BUY);
                                    }

                                    @Override
                                    public void onNo() {
                                        SLog.info("onNo");
                                    }
                                })).show();

                                return;
                            }
                        } else if (COUNT_DOWN_TYPE_END.equals(promotionCountDownTimeType)) { // 團購活動已開始
                            new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                                  .asCustom(new TwConfirmPopup(_mActivity, "是否參與團購優惠活動",null   , "參與團購", "原價購買",new OnConfirmCallback() {
                                @Override
                                public void onYes() {
                                    SLog.info("onYes");
                                    showSpecSelectPopup(Constant.ACTION_BUY, 0);
                                }

                                @Override
                                public void onNo() {
                                    SLog.info("onNo");
                                    showSpecSelectPopup(Constant.ACTION_BUY);
                                }
                            })).show();
                            return;
                        }
                    }
                    showSpecSelectPopup(Constant.ACTION_BUY);
                } else {
                    Util.showLoginFragment();
                }
                break;
            case R.id.btn_join_group:
                showSpecSelectPopup(Constant.ACTION_BUY, 0);
                break;
            case R.id.btn_select_spec:
                if (bargainId != Constant.INVALID_BARGAIN_ID) { // 如果是砍價，不讓選擇規格

                    return;
                }
                if (Util.noPrice(goodsModel)) {//如果是詢價也不讓選規格
                    return;
                }
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
            case R.id.btn_bargain_customer_service:
            case R.id.btn_consult:
            case R.id.btn_bottom_bar_customer_service:
                showStoreCustomerService();
                break;
            case R.id.btn_arrival_notice:
                Util.startFragment(ArrivalNoticeFragment.newInstance(commonId, currGoodsId));
                break;
            case R.id.btn_play:
                Util.playYoutubeVideo(_mActivity, goodsVideoId);
                break;
            case R.id.btn_view_more_group:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ViewMoreGroupPopup(_mActivity, commonId, this))
                        .show();
                break;
            case R.id.btn_quick_join_group_1:
            case R.id.btn_quick_join_group_2:
                if (!User.isLogin()) {
                    Util.showLoginFragment();
                    return;
                }
                SLog.info("goId1[%d], goId2[%d]", goId1, goId2);
                if (id == R.id.btn_quick_join_group_1 && goId1 != Constant.INVALID_GO_ID) {
                    showSpecSelectPopup(Constant.ACTION_BUY, goId1);
                } else if (id == R.id.btn_quick_join_group_2 && goId2 != Constant.INVALID_GO_ID) {
                    showSpecSelectPopup(Constant.ACTION_BUY, goId2);
                }
                break;
            case R.id.btn_bargain:
                if (bargainOpenId == Constant.INVALID_BARGAIN_OPEN_ID) {
                    return;
                }
                Util.startFragment(BargainDetailFragment.newInstance(bargainOpenId, commonId, currGoodsId));
                break;
            case R.id.btn_view_bargain_instruction:
                Util.startFragment(H5GameFragment.newInstance(Config.MOBILE_WEB_BASE_URL + Constant.BARGAIN_INSTRUCTION_URL, null));
                break;
            default:
                break;
        }
    }

    public void pullShare() {
        SLog.info("goodsPrice[%s]", goodsPrice);
        String goodsImageUrl = null;

        try {
            if (currGalleryImageList.size() > 0) {
                goodsImageUrl = StringUtil.normalizeImageUrl(currGalleryImageList.get(0));
            }

            if (StringUtil.isEmpty(goodsImageUrl)) {
                goodsImageUrl = "";
            }

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, SharePopup.generateGoodsShareLink(commonId, currGoodsId), goodsName,
                            jingle, goodsImageUrl, EasyJSONObject.generate("shareType", SharePopup.SHARE_TYPE_GOODS,
                            "commonId", commonId, "goodsName", goodsName,
                            "goodsImage", goodsImageUrl, "goodsPrice", goodsPrice,"goodsModel",goodsModel)))
                    .show();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }


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
        showSpecSelectPopup(action, Constant.INVALID_GO_ID);
    }

    /**
     * 顯示規格選擇彈框
     * @param action 動作： 添加購物車；購買；選擇規格
     * @param goId 開團Id: 如果為0，表示自己新開團;  如果大於0，表示加入指定的團
     */
    private void showSpecSelectPopup(int action, int goId) {
        boolean groupBuyMode = (goId != Constant.INVALID_GO_ID);
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SpecSelectPopup(_mActivity, action, 0, specList, specValueIdMap, selSpecValueIdList, buyNum, goodsInfoMap, currGalleryImageList,limitBuy,discountState,skuGalleryItemList, groupBuyMode, goId))
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
                            int couponCount = 0;
                            for (Object object : couponList) {
                                EasyJSONObject voucher = (EasyJSONObject) object;
                                int couponPrice = (int) voucher.getDouble("searchCouponActivityVo.couponPrice");

                                if (couponCount < 3) {  // 只顯示前3個
                                    LinearLayout linearLayout = new LinearLayout(_mActivity);
                                    linearLayout.setBackgroundResource(R.color.tw_yellow);
                                    // linearLayout.setPadding(2,2,2,2);
                                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                                    TextView textView = new TextView(_mActivity);
                                    textView.setHeight(Util.dip2px(_mActivity, 21));
                                    textView.setWidth(Util.dip2px(_mActivity, 20));
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setText("領");
                                    textView.setTextColor(Color.WHITE);
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

                                    tvVoucher.setText(String.format("%d元券", couponPrice));

                                    llVoucherContainer.addView(linearLayout, layoutParams);
                                }

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
                                couponCount++;
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


    /**
     * 砍一刀
     */
    private void bargain() {
        SLog.info("砍一刀");
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "bargainId", bargainId,
                "token", token);

        String url = Api.PATH_BARGAIN_FIRST;
        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    bargainOpenId = responseObj.getInt("datas.openId");

                    ToastUtil.success(_mActivity, "砍價成功");
                    loadBargainGoods(true);

                    // 跳轉到砍價詳情頁
                    tvGoodsName.postDelayed(() -> Util.startFragment(BargainDetailFragment.newInstance(bargainOpenId, commonId, currGoodsId)), 800);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadData() {
        if (bargainId != Constant.INVALID_BARGAIN_ID) {
            loadBargainGoods(false);
        } else {
            loadGoodsDetail();
        }
    }

    /**
     * 【砍價】加載商品詳情
     */
    private void loadBargainGoodsDetail() {
        View contentView = getView();
        if (contentView == null) {
            return;
        }

        String token = User.getToken();

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
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
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

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SLog.info("imageUrl[%s],v[%s]", imageUrl,v instanceof DataImageView);
                                    int currImageIndex = (int) ((DataImageView) v).getCustomData();
                                    Util.startFragment(ImageFragment.newInstance(currImageIndex, goodsDetailImageList));

                                }
                            });
                            // 加上.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)，防止加載長圖模糊的問題
                            // 參考 Glide加载图片模糊问题   https://blog.csdn.net/sinat_26710701/article/details/89384579

                            String smallImageUrl = StringUtil.normalizeImageUrl(imageUrl, "?x-oss-process=image/resize,w_800"); // 限定宽度，防止加载图片OOM
                            SLog.info("smallImageUrl[%s]", smallImageUrl);
                            Glide.with(llGoodsDetailImageContainer).load(smallImageUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);
                            llGoodsDetailImageContainer.addView(imageView);

                            goodsDetailImageList.add(StringUtil.normalizeImageUrl(imageUrl));

                            imageIndex++;
                        } else if (goodsMobileBodyVo.getType().equals("text")) {
                            TextView textView = new TextView(_mActivity);
                            textView.setText(goodsMobileBodyVo.getValue());
                            textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                            textView.setTextSize(16);
                            // textView.setGravity(Gravity.CENTER);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.leftMargin = Util.dip2px(_mActivity, 20);
                            layoutParams.rightMargin = layoutParams.leftMargin;
                            layoutParams.topMargin = Util.dip2px(_mActivity, 10);
                            layoutParams.bottomMargin = layoutParams.topMargin;
                            textView.setLayoutParams(layoutParams);

                            llGoodsDetailImageContainer.addView(textView);
                        }
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });

    }

    /**
     * 加載砍價商品商品詳情
     * @param isReload  是否重新加載
     */
    private void loadBargainGoods(boolean isReload) {
        if (bargainId == Constant.INVALID_BARGAIN_ID) {
            return;
        }

        View contentView = getView();
        if (contentView == null) {
            return;
        }

        llFloatButton.setVisibility(GONE);

        // 隱藏【規格】、【送至】、【說說】、【城友】
        View rlSpecContainer = contentView.findViewById(R.id.rl_spec_container);
        rlSpecContainer.setVisibility(VISIBLE);
        rlSpecContainer.setBackground(null);
        contentView.findViewById(R.id.ic_specs_expand_button).setVisibility(GONE);
        contentView.findViewById(R.id.rl_send_to_container).setVisibility(GONE);
        contentView.findViewById(R.id.ll_comment_container).setVisibility(GONE);
        contentView.findViewById(R.id.rl_shop_friend_container).setVisibility(GONE);
        contentView.findViewById(R.id.cl_extra_info_container).setVisibility(GONE);
        contentView.findViewById(R.id.ll_normal_bottom_bar_container).setVisibility(GONE);
        contentView.findViewById(R.id.ll_bargain_bottom_bar_container).setVisibility(VISIBLE);

        contentView.findViewById(R.id.fl_bargain_label).setVisibility(VISIBLE);
        contentView.findViewById(R.id.ll_bargain_state_container).setVisibility(VISIBLE);

        // 設置背景為紅色
        btnAddToCartBg.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(_mActivity, R.color.tw_red)));

        promotionType = Constant.PROMOTION_TYPE_BARGAIN;
        promotionCountDownTimeType = COUNT_DOWN_TYPE_END;

        try {
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate(
                    "bargainId", bargainId);

            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            String url = Api.PATH_BARGAIN_GOODS;
            SLog.info("url[%s], params[%s]", url, params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    adapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        contentView.findViewById(R.id.ll_bargain_container).setVisibility(VISIBLE);

                        EasyJSONObject bargainOpen = responseObj.getSafeObject("datas.bargainOpen");
                        EasyJSONObject bargainGoodsDetailVo = responseObj.getSafeObject("datas.bargainGoodsDetailVo");
                        EasyJSONObject goodsCommon = bargainGoodsDetailVo.getSafeObject("goodsCommon");
                        EasyJSONArray goodsImageList = bargainGoodsDetailVo.getSafeArray("goodsImageList");
                        EasyJSONObject bargain = bargainGoodsDetailVo.getSafeObject("bargain");
                        EasyJSONObject goods = bargainGoodsDetailVo.getSafeObject("goods");
                        EasyJSONArray conformList = bargainGoodsDetailVo.getSafeArray("conformList");
                        EasyJSONArray giftVoList = bargainGoodsDetailVo.getSafeArray("giftVoList");

                        if (Util.isJsonObjectEmpty(bargainOpen)) {
                            contentView.findViewById(R.id.rl_bargain_info_container).setVisibility(GONE);
                        } else {
                            contentView.findViewById(R.id.rl_bargain_info_container).setVisibility(VISIBLE);

                            bargainOpenId = bargainOpen.getInt("openId");

                            int bargainTimes = bargainOpen.getInt("bargainTimes");
                            btnBargain.setText(bargainTimes + "人幫砍");

                            TextView tvBargainNickname = contentView.findViewById(R.id.tv_bargain_nickname);
                            String bargainNickname = "";
                            if (bargainOpen.exists("nickName")) {
                                bargainNickname = bargainOpen.getSafeString("nickName");
                            }
                            tvBargainNickname.setText(bargainNickname);

                            ImageView imgBargainAvatar = contentView.findViewById(R.id.img_bargain_avatar);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(bargainOpen.getSafeString("avatar")))
                                    .centerCrop().into(imgBargainAvatar);

                            TextView tvCurrentBargainPrice = contentView.findViewById(R.id.tv_current_bargain_price);
                            double openPrice = bargainOpen.getDouble("openPrice");
                            tvCurrentBargainPrice.setText(StringUtil.formatPrice(_mActivity, openPrice, 0));
                        }

                        if (isReload) { // 如果是重新加載，後面的不用處理
                            return;
                        }

                        goodsName = goodsCommon.getSafeString("goodsName");
                        tvGoodsName.setText(goodsName);
                        jingle = goodsCommon.getSafeString("jingle");
                        tvGoodsJingle.setText(jingle);
                        //產品状态 可以购买1，下架0
                        int goodsState = goodsCommon.getInt("goodsState");
                        //針對用戶id限購 可以购买0，提示限購-1
                        setGoodsStatus(goodsState);

                        goodsPrice = goods.getDouble("goodsPrice0");
                        ((TextView) contentView.findViewById(R.id.tv_bargain_state_price)).setText(StringUtil.formatFloat(goodsPrice));

                        bargainState = bargain.getInt("bargainState");
                        TextView tvBargainCountDownLabel = contentView.findViewById(R.id.tv_bargain_count_down_label);
                        if (bargainState == Constant.BARGAIN_STATE_NOT_STARTED) {
                            tvBargainCountDownLabel.setText("距離活動開始");
                            promotionCountDownTimeType = COUNT_DOWN_TYPE_BEGIN;
                        } else if (bargainState == Constant.BARGAIN_STATE_ONGOING) {
                            tvBargainCountDownLabel.setText("活動倒計時");
                            promotionCountDownTimeType = COUNT_DOWN_TYPE_END;
                        }


                        double bargainBottomPrice = bargain.getDouble("bottomPrice");
                        ((TextView) contentView.findViewById(R.id.tv_bargain_state_bottom_price))
                                .setText("底價 " + StringUtil.formatPrice(_mActivity, bargainBottomPrice, 0));

                        int bargainMemberCount = bargain.getInt("joinNumber");
                        ((TextView) contentView.findViewById(R.id.tv_bargain_state_member_count))
                                .setText(String.format("已有%d人參與", bargainMemberCount));

                        storeId = goodsCommon.getInt("storeId");

                        if (goodsCommon.exists("detailVideo")) {
                            String detailVideoUrl = goodsCommon.getSafeString("detailVideo");
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

                        String bargainStartTimeStr = bargain.getSafeString("startTime");
                        Jarbon bargainStartTimeJarbon = Jarbon.parse(bargainStartTimeStr);
                        promotionStartTime = bargainStartTimeJarbon.getTimestampMillis();
                        String bargainEndTimeStr = bargain.getSafeString("endTime");
                        Jarbon bargainEndTimeJarbon = Jarbon.parse(bargainEndTimeStr);
                        promotionEndTime = bargainEndTimeJarbon.getTimestampMillis();
                        SLog.info("promotionStartTime[%d], promotionEndTime[%d]", promotionStartTime, promotionEndTime);

                        llGroupListContainer.setVisibility(GONE);


                        // 【贈品】優惠
                        GoodsInfo goodsInfo = new GoodsInfo();
                        List<GiftItem> giftItemList = new ArrayList<>();
                        for (Object object2 : giftVoList) {
                            EasyJSONObject giftVo = (EasyJSONObject) object2;

                            GiftItem giftItem = (GiftItem) EasyJSONBase.jsonDecode(GiftItem.class, giftVo.toString());
                            giftItemList.add(giftItem);
                        }
                        // SLog.info("ddddddddddddddddddxxxxx[%d][%d]", goodsId, giftItemList.size());
                        giftMap.put(currGoodsId, giftItemList);


                        // 獲取每個sku對應的輪播圖列表
                        List<String> goodsGalleryImageList = new ArrayList<>();
                        for (Object object2 : goodsImageList) {
                            EasyJSONObject goodsImageObj = (EasyJSONObject) object2;
                            String imageName = goodsImageObj.getSafeString("imageName");
                            goodsGalleryImageList.add(imageName);
                        }

                        skuGoodsGalleryMap.put(currGoodsId, goodsGalleryImageList);

                        String goodsFullSpecs = goods.getSafeString("goodsFullSpecs");
                        tvCurrentSpecs.setText(goodsFullSpecs);

                        goodsInfo.goodsId = currGoodsId;
                        goodsInfo.commonId = commonId;
                        goodsInfo.goodsFullSpecs = goodsFullSpecs;
                        goodsInfo.goodsPrice0 =  goods.getDouble("goodsPrice0");
                        goodsInfo.price = Util.getSkuPrice(goods);
                        SLog.info("__goodsInfo.price[%s], goods[%s]", goodsInfo.price, goods.toString());
                        goodsInfo.imageSrc = goods.getSafeString("imageSrc");
                        goodsInfo.goodsStorage = goods.getInt("goodsStorage");
                        goodsInfo.reserveStorage = goods.getInt("reserveStorage");
                        goodsInfo.limitAmount = goods.getInt("limitAmount");
                        goodsInfo.goodsName = goodsName;
                        goodsInfo.promotionType =goods.getInt("promotionType");

                        goodsInfoMap.put(currGoodsId, goodsInfo);

                        SkuGalleryItem skuGalleryItem = new SkuGalleryItem(
                                currGoodsId,
                                StringUtil.normalizeImageUrl(goodsInfo.imageSrc),
                                goods.getSafeString("goodsSpecString"),
                                goodsInfo.goodsPrice0,
                                goodsInfo.specValueIds
                        );
                        skuGalleryItemList.add(skuGalleryItem);



                        // 【滿減】優惠
                        boolean first;
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

                                EasyJSONArray giftVoArr = conform.getSafeArray("giftVoList");
                                if (giftVoArr.length() > 0) {
                                    goodsConformItem.giftVoList = new ArrayList<>();
                                    for (Object object2 : giftVoArr) {
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

                        vwSeparator0.setVisibility(GONE);
                        rlPriceTag.setVisibility(GONE); //  隱藏價格標籤
                        isDataValid = true;

                        startCountDown();

                        loadCouponList();
                        loadBargainGoodsDetail();
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
        View contentView = getView();
        if (contentView == null) {
            return;
        }

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
                LogUtil.uploadAppLog(path, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(path, params.toString(), responseStr, "");
                    return;
                }

                long now = System.currentTimeMillis();

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

                    promotionDiscountRate = goodsDetail.getDouble("promotionDiscountRate");
                    StringBuffer discountDes = new StringBuffer("限時");
                    discountDes.append(String.format("%.2f", promotionDiscountRate)).append("折");
                    tvPromotionDiscountRate.setText(discountDes.toString());
                    if (responseObj.exists("datas.goodsCountry")) {
                        SLog.info("XXYYZZ");
                        EasyJSONObject goodsCountry =responseObj.getObject("datas.goodsCountry");
                        SLog.info("goodsCountry[%s]", goodsCountry);
                        if (goodsCountry != null) {
                            SLog.info("XXYYZZ");
                            String goodsNationalFlagUrl = StringUtil.normalizeImageUrl(goodsCountry.getSafeString("nationalFlag"));
                            Glide.with(GoodsDetailFragment.this).load(goodsNationalFlagUrl).into(imgGoodsNationalFlag);
                            tvGoodsCountryName.setText(goodsCountry.getSafeString("countryCn"));
                        }
                    }


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

                    goodsModel = StringUtil.safeModel(goodsDetail);
                    if (Util.noPrice(goodsModel)) {
                        UiUtil.toConsultUI(tvGoodsPrice);
                        flAddToCart.setVisibility(GONE);
                        btnBuy.setVisibility(GONE);
                        btnConsult.setVisibility(VISIBLE);

                    } else {
                        goodsPrice = Util.getSpuPrice(goodsDetail);
                        UiUtil.toPriceUI(tvGoodsPrice,0);
                    }

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
                    EasyJSONArray goodsSpecValueArr = EasyJSONArray.parse(goodsSpecValues);
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

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SLog.info("imageUrl[%s],v[%s]", imageUrl,v instanceof DataImageView);
                                    int currImageIndex = (int) ((DataImageView) v).getCustomData();
                                    Util.startFragment(ImageFragment.newInstance(currImageIndex, goodsDetailImageList));

                                }
                            });
                            // 加上.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)，防止加載長圖模糊的問題
                            // 參考 Glide加载图片模糊问题   https://blog.csdn.net/sinat_26710701/article/details/89384579

                            String smallImageUrl = StringUtil.normalizeImageUrl(imageUrl, "?x-oss-process=image/resize,w_800"); // 限定宽度，防止加载图片OOM
                            SLog.info("smallImageUrl[%s]", smallImageUrl);
                            Glide.with(llGoodsDetailImageContainer).load(smallImageUrl).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(imageView);
                            llGoodsDetailImageContainer.addView(imageView);

                            goodsDetailImageList.add(StringUtil.normalizeImageUrl(imageUrl));

                            imageIndex++;
                        } else if (goodsMobileBodyVo.getType().equals("text")) {
                            TextView textView = new TextView(_mActivity);
                            textView.setText(goodsMobileBodyVo.getValue());
                            textView.setTextColor(getResources().getColor(R.color.tw_black, null));
                            textView.setTextSize(16);
                            // textView.setGravity(Gravity.CENTER);
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

                    promotionType = goodsDetail.getInt("promotionType");
                    promotionCountDownTime = goodsDetail.getLong("promotionCountDownTime");
                    promotionCountDownTimeType = goodsDetail.getSafeString("promotionCountDownTimeType");
                    SLog.info("promotionType[%d]", promotionType);

                    llGroupListContainer.setVisibility(GONE);

                    if (promotionCountDownTime > 0) { // 有開啟推廣活動
                        String promotionStartTimeStr = goodsDetail.getSafeString("promotionStartTime");
                        if (!StringUtil.isEmpty(promotionStartTimeStr)) {
                            promotionStartTime = Jarbon.parse(promotionStartTimeStr).getTimestampMillis();
                        }

                        String promotionEndTimeStr = goodsDetail.getSafeString("promotionEndTime");
                        if (!StringUtil.isEmpty(promotionEndTimeStr)) {
                            promotionEndTime = Jarbon.parse(promotionEndTimeStr).getTimestampMillis();
                        }

                        if (promotionType == Constant.PROMOTION_TYPE_GROUP) { // 团购功能
                            SLog.info("___here");
                            EasyJSONObject groups = goodsDetail.getSafeObject("groups");
                            groupRequireNum = groups.getInt("groupRequireNum");
                            tvGroupBuyLabel.setText(groupRequireNum + "人拼團價");
                            tvGroupBuyLabel.setVisibility(VISIBLE);
                            llGroupInfoContainer.setVisibility(VISIBLE);

                            if (COUNT_DOWN_TYPE_END.equals(promotionCountDownTimeType)) { // 拼团活动已经开始
                                SLog.info("___here");
                                currGroupBuyStatus = GroupBuyStatus.ONGOING;
                                btnBuy.setText("想要拼團");
                            } else { // 拼团活动未开始
                                currGroupBuyStatus = GroupBuyStatus.NOT_STARTED;
                            }
                            SLog.info("currGroupBuyStatus[%s]", currGroupBuyStatus);

                            // 正在拼團的團的列表
                            groupOpenCount = goodsDetail.getInt("groupOpenCount");
                            if (groupOpenCount > 0) {
                                TextView tvGroupMemberCount = contentView.findViewById(R.id.tv_group_member_count);
                                tvGroupMemberCount.setText(groupOpenCount + "人正在拼團，可直接參與");

                                llGroupListContainer.setVisibility(VISIBLE);

                                EasyJSONArray groupLogOpenVoList = goodsDetail.getSafeArray("groupLogOpenVoList");

                                ImageView imgGroupMemberAvatar1 = contentView.findViewById(R.id.img_group_member_avatar_1);
                                TextView tvGroupMemberNickname1 = contentView.findViewById(R.id.tv_group_member_nickname_1);
                                TextView tvCountDownMember1 = contentView.findViewById(R.id.tv_count_down_member_1);
                                EasyJSONObject groupLogOpenVo = (EasyJSONObject) groupLogOpenVoList.get(0);

                                String memberAvatar = groupLogOpenVo.getSafeString("memberAvatar");
                                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(memberAvatar)).centerCrop().into(imgGroupMemberAvatar1);
                                String memberNickname = groupLogOpenVo.getSafeString("nickName");
                                tvGroupMemberNickname1.setText(memberNickname);
                                int requireNum = groupLogOpenVo.getInt("requireNum");
                                int joinedNum = groupLogOpenVo.getInt("joinedNum");
                                tvCountDownMember1.setText((requireNum - joinedNum) + "人");

                                group1EndTime = groupLogOpenVo.getLong("endTime"); // 獲取第1團的結束時間
                                goId1 = groupLogOpenVo.getInt("goId");

                                if (groupLogOpenVoList.length() > 1) { // 有2條記錄或以上
                                    ImageView imgGroupMemberAvatar2 = contentView.findViewById(R.id.img_group_member_avatar_2);
                                    TextView tvGroupMemberNickname2 = contentView.findViewById(R.id.tv_group_member_nickname_2);
                                    TextView tvCountDownMember2 = contentView.findViewById(R.id.tv_count_down_member_2);
                                    groupLogOpenVo = (EasyJSONObject) groupLogOpenVoList.get(1);

                                    memberAvatar = groupLogOpenVo.getSafeString("memberAvatar");
                                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(memberAvatar)).centerCrop().into(imgGroupMemberAvatar2);
                                    memberNickname = groupLogOpenVo.getSafeString("nickName");
                                    tvGroupMemberNickname2.setText(memberNickname);
                                    requireNum = groupLogOpenVo.getInt("requireNum");
                                    joinedNum = groupLogOpenVo.getInt("joinedNum");
                                    tvCountDownMember2.setText((requireNum - joinedNum) + "人");

                                    group2EndTime = groupLogOpenVo.getLong("endTime"); // 獲取第2團的結束時間
                                    goId2 = groupLogOpenVo.getInt("goId");
                                } else { // 如果只有1个团，则隐藏第2个团UI
                                    contentView.findViewById(R.id.ll_group2_container).setVisibility(GONE);
                                }
                            } else {
                                llGroupListContainer.setVisibility(GONE); // 没有记录条数，则隐藏
                            }

                            startCountDown();
                        } else if (promotionType == Constant.PROMOTION_TYPE_TIME_LIMITED_DISCOUNT ||  // 限時折扣
                                promotionType == Constant.PROMOTION_TYPE_SEC_KILL // 秒殺
                        ) {
                            startCountDown();
                        }
                    }

                    SLog.info("currGroupBuyStatus[%s]", currGroupBuyStatus);
                    if (currGroupBuyStatus == GroupBuyStatus.CLOSED) {
                        showHideGroupBuyView(false);
                    }


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
                        if (goodsInfoVo.exists("appPrice0")) {
                            goodsInfo.appPrice0 = goodsInfoVo.getDouble("appPrice0");
                        }
                        goodsInfo.price = Util.getSkuPrice(goodsInfoVo);
                        SLog.info("__goodsInfo.price[%s], goodsInfoVo[%s]", goodsInfo.price, goodsInfoVo.toString());
                        goodsInfo.imageSrc = goodsInfoVo.getSafeString("imageSrc");
                        goodsInfo.goodsStorage = goodsInfoVo.getInt("goodsStorage");
                        goodsInfo.reserveStorage = goodsInfoVo.getInt("reserveStorage");
                        goodsInfo.limitAmount = goodsInfoVo.getInt("limitAmount");
                        goodsInfo.unitName = goodsInfoVo.getSafeString("unitName");
                        goodsInfo.goodsName = goodsName;
                        goodsInfo.promotionType =goodsInfoVo.getInt("promotionType");
                        goodsInfo.appUsable =goodsInfoVo.getInt("appUsable");
                        goodsInfo.isGroup = 0;
                        Object tmpObj = goodsInfoVo.get("isGroup");
                        if (!Util.isJsonNull(tmpObj)) {
                            goodsInfo.isGroup = goodsInfoVo.getInt("isGroup");
                        }
                        if (goodsInfo.isGroup == Constant.TRUE_INT) {
                            goodsInfo.groupPrice = goodsInfoVo.getDouble("groupPrice");
                            goodsInfo.groupDiscountAmount = goodsInfo.appPrice0 - goodsInfo.groupPrice;
                        }

                        // 是否為秒殺
                        if (goodsInfoVo.optInt("isSeckill") == Constant.TRUE_INT &&
                                promotionCountDownTime > 0) {
                            goodsInfo.isSeckill = Constant.TRUE_INT;
                        }

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
                        vwSeparator0.setVisibility(View.VISIBLE);
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

                    if (goodsDetail.exists("imageSrc")) {
                        goodsSpuImage = goodsDetail.getSafeString("imageSrc");
                    }

                    loadCouponList();
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
            if (goodsInfo.appUsable == 1) {
                discountState = IN_DISCOUNT;  //匹配後端字段邏輯
            } else {
                long time = System.currentTimeMillis() - promotionEndTime;
                if (time < 0) {
                    discountState = BEFORE_DISCOUNT;
                } else {
                    time = System.currentTimeMillis() - promotionEndTime;
                    if (time < 0) {
                        discountState = IN_DISCOUNT;
                    } else {
                        discountState = OUT_DISCOUNT;
                    }
                }
            }

        }
        SLog.info("discountState[%d],goodsInfo.promotionType[%d]",discountState,goodsInfo.promotionType);
    }

    private void setGoodsStatus(int status) {
        goodsStatus = status;
        if (goodsStatus == 0) {
            View contentView = getView();
            if (contentView == null) {
                return;
            }
            contentView.findViewById(R.id.ll_goods_take_off).setVisibility(VISIBLE);
            btnBuy.setBackgroundResource(R.drawable.icon_take_off_buy);
            btnAddToCartBg.setImageResource(R.drawable.icon_take_off_cart);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SLog.info("onDestroyView");
        EventBus.getDefault().unregister(this);

        if (Config.PROD) {
            MobclickAgent.onPageEnd(UmengAnalyticsPageName.GOODS);
        }

        stopCountDown();
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
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        SLog.info("onSupportVisible");

        if (!isDataValid) {
            loadData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("onSupportInvisible");
    }

    /**
     * 顯示/隱藏GroupBuyView
     * @param show
     */
    private void showHideGroupBuyView(boolean show) {
        if (show) {
            tvGroupBuyLabel.setVisibility(VISIBLE);
            llGroupInfoContainer.setVisibility(VISIBLE);
        } else {
            tvGroupBuyLabel.setVisibility(GONE);
            llGroupInfoContainer.setVisibility(GONE);
        }
    }

    private void showHideSecKillView(boolean show) {
        if (show) {
            flSecKillLabel.setVisibility(VISIBLE);
            rlSecKillContainer.setVisibility(VISIBLE);
        } else {
            flSecKillLabel.setVisibility(GONE);
            rlSecKillContainer.setVisibility(GONE);
        }
    }

    /**
     * 啟動倒數計算
     */
    private void startCountDown() {
        long currTimeMillis = System.currentTimeMillis();
        long countDownTime = 0;  // 倒数目标时间的时间戳（毫秒）
        if (COUNT_DOWN_TYPE_BEGIN.equals(promotionCountDownTimeType)) { // 活动未开始，与开始时间对比
            countDownTime = promotionStartTime;
        } else { // 活动已开始，与结束时间对比
            countDownTime = promotionEndTime;
        }

        if (promotionType == Constant.PROMOTION_TYPE_SEC_KILL) { // 秒殺
            countDownTime = promotionCountDownTime * 1000 + currTimeMillis; // 乘以1000，秒 -》 毫秒
        }
        
        long endTime = countDownTime;
        long remainTime = endTime - currTimeMillis; // 離結束還剩下多少毫秒
        SLog.info("endTime[%s], remainTime[%s]", endTime, remainTime);

        stopCountDown();  // 先停止上一次倒數

        if (remainTime <= 0) {
            return;
        }

        countDownTimer = new CountDownTimer(remainTime, 100) {
            public void onTick(long millisUntilFinished) {
                // SLog.info("threadId[%s]", Thread.currentThread().getId());

                long now = System.currentTimeMillis();
                TimeInfo timeInfo = Time.diff((int) (now / 1000), (int) (endTime / 1000));

                if (promotionType == Constant.PROMOTION_TYPE_TIME_LIMITED_DISCOUNT) {
                    if (COUNT_DOWN_TYPE_END.equals(promotionCountDownTimeType)) {
                        tvCountDownDay.setText(String.format("距結束 %d 天", timeInfo.day));
                    } else {
                        tvCountDownDay.setText(String.format("距開始 %d 天", timeInfo.day));
                    }

                    tvCountDownHour.setText(String.format("%02d", timeInfo.hour));
                    tvCountDownMinute.setText(String.format("%02d", timeInfo.minute));
                    tvCountDownSecond.setText(String.format("%02d", timeInfo.second));
                } else if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
                    if (currGroupBuyStatus == GroupBuyStatus.ONGOING) {
                        tvGroupBuyCountDownTitle.setText("活動倒計時");
                    } else {
                        tvGroupBuyCountDownTitle.setText("距離活動開始");
                    }

                    if (timeInfo != null) {
                        tvGroupRemainDay.setText(String.format("%d天", timeInfo.day));
                        tvGroupRemainHour.setText(String.format("%02d", timeInfo.hour));
                        tvGroupRemainMinute.setText(String.format("%02d", timeInfo.minute));
                        tvGroupRemainSecond.setText(String.format("%02d", timeInfo.second));
                    }

                    TimeInfo timeInfoGroup;
                    if (group1EndTime > 0) { // 倒數1
                        timeInfoGroup = Time.groupTimeDiff(now, group1EndTime);
                        if (timeInfoGroup == null) {
                            updateStopGroup(1);
                        } else {
                            tvCountDownTime1.setText(String.format("%d:%02d:%02d.%d",
                                    timeInfoGroup.hour, timeInfoGroup.minute, timeInfoGroup.second, timeInfoGroup.milliSecond / 100));
                        }
                        if (group2EndTime > 0) { // 倒數2
                            timeInfoGroup = Time.groupTimeDiff(now, group2EndTime);
                            if (timeInfoGroup == null) {
                                updateStopGroup(2);
                            } else {
                                tvCountDownTime2.setText(String.format("%d:%02d:%02d.%d",
                                        timeInfoGroup.hour, timeInfoGroup.minute, timeInfoGroup.second, timeInfoGroup.milliSecond / 100));
                            }
                        }
                    }
                } else if (promotionType == Constant.PROMOTION_TYPE_SEC_KILL) {
                    if (timeInfo != null) {
                        tvSecKillRemainDay.setText(String.format("%d天", timeInfo.day));
                        tvSecKillRemainHour.setText(String.format("%02d", timeInfo.hour));
                        tvSecKillRemainMinute.setText(String.format("%02d", timeInfo.minute));
                        tvSecKillRemainSecond.setText(String.format("%02d", timeInfo.second));
                    }
                } else if (promotionType == Constant.PROMOTION_TYPE_BARGAIN) {
                    if (timeInfo != null) {
                        tvBargainRemainDay.setText(String.format("%d天", timeInfo.day));
                        tvBargainRemainHour.setText(String.format("%02d", timeInfo.hour));
                        tvBargainRemainMinute.setText(String.format("%02d", timeInfo.minute));
                        tvBargainRemainSecond.setText(String.format("%02d", timeInfo.second));
                    }
                }
            }
            public void onFinish() {
                if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
                    llGroupListContainer.setVisibility(GONE);
                }
            }
        }.start();
    }

    /**
     * 團購倒計時結束時更新團購列表View
     * @param group 目前商品詳情頁只顯示前2個團，表示是第1個團或是第2個團
     */
    private void updateStopGroup(int group) {
        if (group == 1) {
            tvCountDownTime1.setText("結束");
            btnQuickJoinGroup1.setVisibility(View.INVISIBLE);
        } else if (group == 2) {
            tvCountDownTime2.setText("結束");
            btnQuickJoinGroup2.setVisibility(View.INVISIBLE);
        }
    }

    private void stopCountDown() {
        SLog.info("stopCountDown()");

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = null;
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
        vwSeparator0.setVisibility(btnShowConformVisibility);

        SLog.info("goodsInfo.specValueIds[%s]", goodsInfo.specValueIds);
        selSpecValueIdList = StringUtil.specValueIdsToList(goodsInfo.specValueIds);

        String fullSpecs = goodsInfo.goodsFullSpecs;
        SLog.info("fullSpecs[%s]", fullSpecs);
        tvCurrentSpecs.setText(fullSpecs);

        SLog.info("goodsPrice0[%f],goodsprice[%f] ", goodsInfo.goodsPrice0, goodsInfo.price);
        showPriceTag(goodsInfo);

        // 更新團購信息
        currSKUGroupBuy = (goodsInfo.isGroup == Constant.TRUE_INT);
        if (currGroupBuyStatus != GroupBuyStatus.CLOSED && currSKUGroupBuy) {
            tvGroupPrice.setText(StringUtil.formatFloat(goodsInfo.groupPrice));
            tvGroupOriginalPrice.setText("原價 " + StringUtil.formatPrice(_mActivity, goodsInfo.appPrice0, 0));
            tvGroupDiscountAmount.setText("拼團立減 " + StringUtil.formatPrice(_mActivity, goodsInfo.groupDiscountAmount, 0));

            showHideGroupBuyView(true);
        } else {
            showHideGroupBuyView(false);
        }

        showHideSecKillView(goodsInfo.isSeckill == Constant.TRUE_INT);
        if (goodsInfo.isSeckill == Constant.TRUE_INT) {
            tvSecKillPrice.setText(StringUtil.formatFloat(goodsInfo.appPrice0));
            tvSecKillOriginalPrice.setText("原價 " + StringUtil.formatPrice(_mActivity, goodsInfo.goodsPrice0, 0));
        }


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
        SLog.info("promotionType[%d], promotionCountDownTime[%d]", promotionType, promotionCountDownTime);
        if (Util.noPrice(goodsModel)) {
            UiUtil.toConsultUI(tvGoodsPrice);
            rlPriceTag.setVisibility(VISIBLE);
        }
        else if (promotionType == Constant.PROMOTION_TYPE_NONE || promotionCountDownTime == 0) {
            tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 1));
            rlPriceTag.setVisibility(VISIBLE);
        } else if (promotionType == Constant.PROMOTION_TYPE_TIME_LIMITED_DISCOUNT) {
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
                tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsInfo.price, 1));
                SLog.info("__result[%s]", StringUtil.formatPrice(_mActivity, goodsInfo.price, 0));
                UiUtil.toPriceUI(tvGoodsPrice,13);

                stopCountDown();
            }
        } else if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
            rlPriceTag.setVisibility(GONE);
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
