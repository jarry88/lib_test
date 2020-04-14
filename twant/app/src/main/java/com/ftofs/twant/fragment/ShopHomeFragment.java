package com.ftofs.twant.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.GoodsGalleryAdapter;
import com.ftofs.twant.adapter.NestedScrollingFragmentAdapter;
import com.ftofs.twant.adapter.StoreFriendsAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.InStorePersonItem;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.entity.StoreFriendsItem;
import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.entity.WantedPostItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TencentLocationTask;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AmapPopup;
import com.ftofs.twant.widget.DataImageView;
import com.ftofs.twant.widget.ImagePopup;
import com.ftofs.twant.widget.InStorePersonPopup;
import com.ftofs.twant.widget.LockableNestedScrollView;
import com.ftofs.twant.widget.MerchantIntroductionPopup;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.StoreAnnouncementPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.ftofs.twant.widget.TwQRCodePopup;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.rd.PageIndicatorView;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

import static android.view.View.VISIBLE;


/**
 * 商店首頁Fragment
 * @author zwm
 */
public class ShopHomeFragment extends BaseFragment implements View.OnClickListener, AutoVerticalScrollTextViewUtil.OnMyClickListener {
    ShopMainFragment parentFragment;
    @BindView(R.id.img_store_status)
    ImageView imgStoreStatus;
    @BindView(R.id.img_store_figure)
    ImageView imgShopLogo;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_shop_signature)
    TextView tvShopSignature;
    @BindView(R.id.tv_store_open_days)
    TextView tvShopOpenDay;
    @BindView(R.id.tv_shop_open_day)
    TextView tvOpenDay;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.ll_sns_container)
    LinearLayout llSnsContainer;

    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    private int currGalleryPosition;
    @BindView(R.id.rv_gallery_image_list)
    RecyclerView rvGalleryImageList;

    @BindView(R.id.tv_like_count)
    TextView tvLikeCount;
    @BindView(R.id.tv_store_view)
    TextView tvStoreView;
    int likeCount;
    TextView tvFavoriteCount;
    int favoriteCount;
    int isFavorite;
    ImageView btnStoreFavorite;

    int isLike;
    ImageView btnStoreThumb;

    TextView btnShopMap;

    private LinearLayout llPayWayContainer;

    LinearLayout llFirstCommentContainer;
    LinearLayout llShopAnnouncementContainer;


    private TextView tvBusinessTimeWorkingDay;
    private TextView tvBusinessTimeWeekend;

    int storeId;
    double storeDistance;  // 我與商店的距離
    StoreMapInfo storeMapInfo;
    String storeName;
    String storeSignature;
    String storeAvatarUrl;
    String storePhone;
    String storeAddress;
    double storeLongitude;
    double storeLatitude;

    // 店鋪形象視頻相關
    ImageView btnPlay;
    String storeVideoUrl;

    private TextView tvPhoneNumber;
    private TextView tvShopAddress;

    @BindView(R.id.container_view)
    NestedScrollView containerView;
    int containerViewHeight;
    private scrollStateHandler mHandler;

    static class scrollStateHandler extends Handler {
        NestedScrollView scrollViewContainer;
        ShopHomeFragment fragment;
        ShopMainFragment mainFragment;
        int lastY = -1;
        private boolean showFloatButton = true;

        public scrollStateHandler(ShopHomeFragment fragment) {
            this.fragment = fragment;
            this.mainFragment = (ShopMainFragment) fragment.getParentFragment();
            this.scrollViewContainer = fragment.containerView;
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
                    mainFragment.isScrolling = false;
                    mainFragment.onCbStartNestedScroll();
                } else {
                    // 滚动的距离 和 之前的不相等 那么 再发送消息 判断一次
// 将滚动的 Y 坐标距离 赋值给 lastY
                    lastY = scrollY;
                    this.sendEmptyMessageDelayed(0, 10);
                }
            }
        }


    }

    List<StoreAnnouncement> storeAnnouncementList = new ArrayList<>();
    private ArrayList<CharSequence> announcementTextList = new ArrayList<>();
    private AutoVerticalScrollTextViewUtil verticalScrollUtil;
    AutoVerticalScrollTextView tvVerticalScroll;

    String merchantIntroduction;

    StoreFriendsAdapter adapter;
    List<StoreFriendsItem> storeFriendsItemList = new ArrayList<>();

    TextView tvVisitorCount;

    List<WantedPostItem> wantedPostItemList = new ArrayList<>();

    int inStorePersonCount;


    private List<Fragment> fragments;
    private CommonFragmentPagerAdapter fragmentAdapter;
    private List<String> tabs;
    private int tabCount = 4;
    /** 首頁 */
    public static final int HOME_FRAGMENT = 0;
    /** 簡介 */
    public static final int INTRODUCTION_FRAGMENT = 1;
    /** 商店說說 */
    public static final int COMMENT_FRAGMENT = 2;
    /** 公告 */
    public static final int NOTICE_FRAGMENT = 3;

    List<InStorePersonItem> inStorePersonItemList = new ArrayList<>();
    private Unbinder unbinder;
    private GoodsGalleryAdapter goodsGalleryAdapter;
    private List<String> currGalleryImageList=new ArrayList<>();
    private Timer timer;
    private boolean bannerStart;
    private CountDownHandler countDownHandler;
    private String storeBusInfo;
    private int storeReply;


    static class CountDownHandler extends Handler {

        private PageIndicatorView pageIndicatorView;
        private RecyclerView rvGalleryImageList;

        public CountDownHandler(PageIndicatorView indicatorView,RecyclerView rvGalleryImageList) {
            this.rvGalleryImageList = rvGalleryImageList;
            this.pageIndicatorView = indicatorView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currGalleryPosition = msg.arg1;
            pageIndicatorView.setSelection(currGalleryPosition);
            rvGalleryImageList.scrollToPosition(currGalleryPosition);
        }
    }

    public static ShopHomeFragment newInstance() {
        Bundle args = new Bundle();

        ShopHomeFragment fragment = new ShopHomeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_home, container, false);
        unbinder= ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();
        storeId = parentFragment.storeId;

        viewpager.setOffscreenPageLimit(tabCount-1);
        imgShopLogo.setOnClickListener(this);
        containerView.setOnClickListener(this);

        btnPlay = view.findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);

        tvLikeCount = view.findViewById(R.id.tv_like_count);
        btnStoreThumb = view.findViewById(R.id.btn_store_thumb);
        Util.setOnClickListener(view, R.id.ll_uo_thumb_up_container, this);

        llPayWayContainer = view.findViewById(R.id.ll_pay_way_container);

        tvBusinessTimeWorkingDay = view.findViewById(R.id.tv_business_time_working_day);
        tvBusinessTimeWeekend = view.findViewById(R.id.tv_business_time_weekend);

        tvFavoriteCount = view.findViewById(R.id.tv_favorite_count);
        btnStoreFavorite = view.findViewById(R.id.btn_store_favorite);
        Util.setOnClickListener(view, R.id.ll_uo_like_container, this);

        tvVisitorCount = view.findViewById(R.id.tv_visitor_count);
        Util.setOnClickListener(view, R.id.ll_uo_share_container, this);

        llFirstCommentContainer = view.findViewById(R.id.ll_first_comment_container);
        llShopAnnouncementContainer = view.findViewById(R.id.ll_shop_announcement_container);
        llShopAnnouncementContainer.setOnClickListener(this);

        tvVerticalScroll = view.findViewById(R.id.tv_vertical_scroll);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvShopAddress = view.findViewById(R.id.tv_shop_address);

        btnShopMap = view.findViewById(R.id.btn_shop_map);
        btnShopMap.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.rl_shop_comment_container, this);
        Util.setOnClickListener(view, R.id.btn_show_all_store_friends, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);
        Util.setOnClickListener(view, R.id.btn_search, this);


        RecyclerView rvStoreFriendsList = view.findViewById(R.id.rv_store_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvStoreFriendsList.setLayoutManager(layoutManager);
        adapter = new StoreFriendsAdapter(R.layout.store_friends_item, storeFriendsItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreFriendsItem item = storeFriendsItemList.get(position);
                Util.startFragment(MemberInfoFragment.newInstance(item.memberName));
            }
        });
        rvStoreFriendsList.setAdapter(adapter);

        initTableData();
        loadStoreData();
        setImageBanner();

        parentFragment.adjustFlHeight(true);
        parentFragment.toolbar.setAlpha(0.0f);
        parentFragment.preToolbar.setAlpha(1.0f);
        parentFragment.toolbar.setBackgroundResource(R.drawable.white_border_type_d);
        containerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int rvPostListY = Util.getYOnScreen(smartTabLayout);
                int containerViewY = Util.getYOnScreen(containerView);

//                 SLog.info("rvPostListY[%s], containerViewY[%s]", rvPostListY, containerViewY);
                broadcastNestedScrollingEnabled(rvPostListY <= containerViewY+parentFragment.tool.getHeight());  // 如果列表滑动到顶部，则启用嵌套滚动
                hideBarTitle(scrollY);
            }
        });
        mHandler = new ShopHomeFragment.scrollStateHandler(this);
    }

    private void hideBarTitle( int scrollY) {
        int fadingHeight = 255;
        int initHeight = 20;
        if (scrollY > initHeight) {
            parentFragment.hideTitle(true);
            if (scrollY > fadingHeight) {
                parentFragment.toolbar.setAlpha(1.0f);
                parentFragment.preToolbar.setAlpha(0);

            } else {
                parentFragment.toolbar.setAlpha((float) scrollY / fadingHeight);
                parentFragment.preToolbar.setAlpha(1.0f-(float) scrollY / fadingHeight);

                //rlTopBarContainer.getBackground().setAlpha(scrollY);
            }
        } else {
            parentFragment.hideTitle(false);
            parentFragment.toolbar.setAlpha(0.0f);
            parentFragment.preToolbar.setAlpha(1.0f);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void broadcastNestedScrollingEnabled(boolean enable) {
        for (int i = 0; i < fragments.size(); i++) {
            ((ScrollableBaseFragment) fragments.get(i)).setScrollable(enable);
        }
    }

    private void initTableData() {
        tabs = new ArrayList<>();
        tabs.add(getString(R.string.shop_bottom_bar_title_home));
        tabs.add("簡介");
        tabs.add("說說");
        tabs.add("公告");
        fragments = new ArrayList<>();
        fragments.add(StoreHomeFragment.newInstance());

        fragments.add(StoreAboutFragment.newInstance(storeId));

        fragments.add(StoreCommentFragment.newInstance(storeId));

        fragments.add(StoreNoticeFragment.newInstance());
        initView();
        initListener();
    }


    private void initView() {
        fragmentAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), tabs, fragments);
        viewpager.setAdapter(fragmentAdapter);

        smartTabLayout.setCustomTabView(new MyTabProvider());
        smartTabLayout.setViewPager(viewpager);
        for (int i = 0; i < tabCount; i++) {
            ((TextView)smartTabLayout.getTabAt(i).findViewById(R.id.tvTabText))
                    .setTextColor(getContext().getColor(i == 0 ? R.color.tw_black : R.color.tw_text_dark));
        }
    }
    private void initListener(){
        smartTabLayout.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                for (int i = 0; i < tabCount; i++) {
                    ((TextView)smartTabLayout.getTabAt(i).findViewById(R.id.tvTabText))
                            .setTextColor(getContext().getColor(i == position ? R.color.tw_black : R.color.tw_text_dark));
                }
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SLog.info("changePageHere");
            }
        });
    }

    private class MyTabProvider implements SmartTabLayout.TabProvider{
        private LayoutInflater inflater;

        public MyTabProvider(){
            inflater = LayoutInflater.from(_mActivity);
        }

        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            View view = inflater.inflate(R.layout.category_tab,container,false);
            TextView textView = view.findViewById(R.id.tvTabText);
            textView.setText(tabs.get(position));
            return view;
        }
    }


    private void loadStoreData() {
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        try {
            // 獲取商店首頁信息
            String path = Api.PATH_SHOP_HOME + "/" + parentFragment.getStoreId();
            String token = User.getToken();
            EasyJSONObject params = EasyJSONObject.generate();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }
            //上傳用戶位置
            params = Util.upLocation(params);

            SLog.info("path[%s], params[%s]", path, params.toString());
            Api.getUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();

                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONObject storeInfo = responseObj.getSafeObject("datas.storeInfo");
                        SLog.info(String.format("storeInfo[%s]",storeInfo.toString()));
                        setStoreInfo(storeInfo);
                        boolean hasSlider = storeInfo.exists("storeSlider");
                        SLog.info("hasSlider,%s",hasSlider);
                        if (hasSlider) {
                            EasyJSONArray storeSlider = storeInfo.getSafeArray("storeSlider");
                            currGalleryImageList.clear();
                            for (Object object2 : storeSlider) {
                                currGalleryImageList.add(object2.toString());
                            }
                        }
                        String shopDay = storeInfo.getString("shopDay");
//                        int storeView=  responseObj.getInt("datas.storeView");
                        int storeView=  storeInfo.getInt("storeView");
                        tvStoreView.setText(StringUtil.formatPostView(storeView));
//                        tvShopOpenDay.setText(shopDay);
                        updateBanner(hasSlider);

                         //好友

                        inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_LABEL, null, null, TwantApplication.getStringRes(R.string.text_friend)));
                        EasyJSONArray friends = null;
                        if (responseObj.exists("datas.friendList")) {
                            friends = responseObj.getArray("datas.friendList");
                        }
                        if (!Util.isJsonNull(friends)) {
                            if (friends.length() > 0) {
                                for (Object object : friends) {
                                    EasyJSONObject friend = (EasyJSONObject) object;
                                    String memberName = friend.getSafeString("memberName");
                                    String avatar = friend.getSafeString("avatar");
                                    String nickname = friend.getSafeString("nickName");
                                    InStorePersonItem inStorePersonItem = new InStorePersonItem(InStorePersonItem.TYPE_ITEM, memberName, avatar, nickname);
                                    inStorePersonItemList.add(inStorePersonItem);
                                }
                            }
                            inStorePersonCount += friends.length();
                        } else { // 為空則添加提示
                            inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_EMPTY_HINT, null, null, null));
                        }

                        // 居民
                        inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_LABEL, null, null, getString(R.string.text_store_friend)));
                        EasyJSONArray members = responseObj.getArray("datas.memberList");
                        if (!Util.isJsonNull(members)) {
                            if (members.length() > 0) {
                                for (Object object : members) {
                                    EasyJSONObject friend = (EasyJSONObject) object;
                                    String memberName = friend.getSafeString("memberName");
                                    String avatar = friend.getSafeString("avatar");
                                    String nickname = friend.getSafeString("nickName");
                                    SLog.info("name:%s,isfriend:%d",nickname,friend.getInt("isFriend"));
                                    InStorePersonItem inStorePersonItem = new InStorePersonItem(InStorePersonItem.TYPE_ITEM, memberName, avatar, nickname);
                                    inStorePersonItemList.add(inStorePersonItem);
                                    StoreFriendsItem sfitem = new StoreFriendsItem(memberName,avatar);
                                    storeFriendsItemList.add(sfitem);
                                }
                            }
                            inStorePersonCount += members.length();
                        } else { // 為空則添加提示
                            inStorePersonItemList.add(new InStorePersonItem(InStorePersonItem.TYPE_EMPTY_HINT, null, null, null));
                        }

                        adapter.setNewData(storeFriendsItemList);


                        // 顯示遊客數量
                        int storeFriendsCount = responseObj.getInt("datas.visitorNum");
                        String storeFriendsCountDesc = String.format(getString(R.string.text_store_friends_count_template), storeFriendsCount);
                        tvVisitorCount.setText(storeFriendsCountDesc);


                        // 社交分享
                        EasyJSONArray snsArray = responseObj.getSafeArray("datas.socialList");
                        setStoreSnsInfo(snsArray);

//
                        // 支付方式
                        EasyJSONArray paymentArray = responseObj.getSafeArray("datas.storePaymentList");
                        setStorePaymentInfo(paymentArray);
//
                        // 鎮店之寶
                        if (responseObj.exists("datas.townShopGoods")) {
                            EasyJSONArray featuresGoodsVoList = responseObj.getSafeArray("datas.townShopGoods");
                            if (featuresGoodsVoList.length() > 0){
                                ((StoreHomeFragment) fragments.get(HOME_FRAGMENT)).setGoodsListDate(featuresGoodsVoList);
                            }
                        }


                        // 最新產品
                        EasyJSONArray newGoodsVoList = responseObj.getSafeArray("datas.newGoods");

                        ((StoreHomeFragment)fragments.get(HOME_FRAGMENT)).newGoodsListData(newGoodsVoList);

                        // 商店熱賣
                        EasyJSONArray hotGoodsVoList = responseObj.getSafeArray("datas.hotSaleGoods");

                        ((StoreHomeFragment) fragments.get(HOME_FRAGMENT)).hotGoodsListData(hotGoodsVoList);

                        // 商店公告
                        EasyJSONArray announcements = responseObj.getSafeArray("datas.announcements");
                        setAnnouncements(announcements);

                        // 店鋪形象視頻
                        if (storeInfo.exists("videoUrl")) {
                            storeVideoUrl = storeInfo.getSafeString("videoUrl");
                            btnPlay.setVisibility(StringUtil.isEmpty(storeVideoUrl) ? View.GONE : VISIBLE);
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

    public void setStoreSnsInfo(EasyJSONArray snsArray) {
        try {
            SLog.info("__setStoreSnsInfo[%s]", snsArray);
            // 添加一个显示店铺二维码的按钮
            DataImageView snsImage = new DataImageView(_mActivity);
            snsImage.setOnClickListener(this);
            snsImage.setId(R.id.btn_show_store_qr_code);
            snsImage.setCustomData(EasyJSONObject.generate("socialName", "takewant", "socialForm", 0));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 31), Util.dip2px(_mActivity, 31));
            layoutParams.setMarginEnd(Util.dip2px(_mActivity, 8));
            Glide.with(this).load(R.drawable.app_logo).into(snsImage);
            llSnsContainer.addView(snsImage, layoutParams);

            for (Object object : snsArray) {
                EasyJSONObject snsObject = (EasyJSONObject) object;
                String snsImageUrl = StringUtil.normalizeImageUrl(snsObject.getSafeString("socialLogoChecked"));

                snsImage = new DataImageView(_mActivity);
                snsImage.setOnClickListener(this);
                snsImage.setCustomData(snsObject);

                layoutParams = new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 31), Util.dip2px(_mActivity, 31));
                layoutParams.setMarginEnd(Util.dip2px(_mActivity, 8));
                Glide.with(this).load(snsImageUrl).into(snsImage);
                llSnsContainer.addView(snsImage, layoutParams);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public void setStorePaymentInfo(EasyJSONArray paymentArray) {
        if (paymentArray != null && paymentArray.length() > 0) {
            for (Object object : paymentArray) {
                EasyJSONObject paymentObject = (EasyJSONObject) object;
                String payWayImageUrl = null;
                try {
                    payWayImageUrl = StringUtil.normalizeImageUrl(paymentObject.getSafeString("paymentLogo"));
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
                ImageView payWayImage = new ImageView(_mActivity);

                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 28), Util.dip2px(_mActivity, 20));
                layoutParams.setMarginEnd(Util.dip2px(_mActivity, 8));
                Glide.with(this).load(payWayImageUrl).into(payWayImage);
                llPayWayContainer.addView(payWayImage, layoutParams);
            }
        }
    }

    private void updateBanner(boolean hasSlider) {
        if (!hasSlider || currGalleryImageList.size() < 1) {
            currGalleryImageList.clear();
            currGalleryImageList.add("placeholder");  // 如果沒有圖片，加一張默認的空櫥窗占位圖
        }
        goodsGalleryAdapter.setNewData(currGalleryImageList);
        if (currGalleryImageList.size() > 1) {
            rvGalleryImageList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (rvGalleryImageList == null) {
                        return;
                    }
                    
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
            pageIndicatorView.setVisibility(View.VISIBLE);
        } else {
            pageIndicatorView.setCount(1);
            pageIndicatorView.setVisibility(VISIBLE);
        }
    }

    private void setStoreInfo(EasyJSONObject storeInfo) {
        try {
            storeName = storeInfo.getSafeString("storeName");
            tvStoreName.setText(storeName);
            parentFragment.setShopName(storeName);

            storeAvatarUrl = StringUtil.normalizeImageUrl(storeInfo.getSafeString("storeAvatar"));
            String storeLogoUrl=StringUtil.normalizeImageUrl(storeInfo.getSafeString("storeLogoUrl"));
            // 商店頭像
            if (StringUtil.isEmpty(storeLogoUrl)) {
                Glide.with(ShopHomeFragment.this).load(R.drawable.default_store_avatar).centerCrop().into(imgShopLogo);
            } else {
                Glide.with(ShopHomeFragment.this).load(storeLogoUrl).centerCrop().into(imgShopLogo);
            }
            // 將商店頭像設置到工具欄按鈕
            parentFragment.setImgBottomBarShopAvatar(storeAvatarUrl);
            //設置營業狀態
            int storeOpen = storeInfo.getInt("isOpen");
            switchStoreOpenState(storeOpen);

            // 商店簽名
            storeSignature = storeInfo.getSafeString("storeSignature");
            if (StringUtil.isEmpty(storeSignature)) {
                tvShopSignature.setText("該店鋪還沒有簽名喲~");
            } else {
                tvShopSignature.setText(storeSignature);
            }

            // 開店天數getString(R.string.text_store_open_day_prefix) +
            tvOpenDay.setText(getString(R.string.text_store_open_day_prefix) +storeInfo.getSafeString("shopDay"));
            tvShopOpenDay.setText( storeInfo.getSafeString("shopDay"));

            // 商店形象圖
            String shopFigureUrl = StringUtil.normalizeImageUrl(storeInfo.getSafeString("storeFigureImage"));
            likeCount = storeInfo.getInt("likeCount");
            tvLikeCount.setText(String.valueOf(likeCount));
            favoriteCount = storeInfo.getInt("collectCount");
            tvFavoriteCount.setText(R.string.text_follow);

            isLike = storeInfo.getInt("isLike");
            isFavorite = storeInfo.getInt("isFavorite");
            SLog.info("isLike[%d], isFavorite[%d]", isLike, isFavorite);
            updateFavoriteView();
            updateThumbView();
            // 營業時間
            setWorkTime(storeInfo.getSafeString("weekDayStart"), storeInfo.getSafeString("weekDayStartTime"), storeInfo.getSafeString("weekDayEnd"), storeInfo.getSafeString("weekDayEndTime"),
                    storeInfo.getSafeString("restDayStart"), storeInfo.getSafeString("restDayStartTime"), storeInfo.getSafeString("restDayEnd"), storeInfo.getSafeString("restDayEndTime"));
            // 商店電話
            storePhone = storeInfo.getSafeString("chainPhone");
            // 商店地址
            storeAddress = storeInfo.getSafeString("chainAreaInfo") + storeInfo.getSafeString("chainAddress");
            // 商家介紹
            merchantIntroduction = storeInfo.getSafeString("storeIntroduce");
            ((StoreAboutFragment) fragments.get(INTRODUCTION_FRAGMENT)).setIntroduction(merchantIntroduction); // 关于我们

            setStoreContact(storePhone, storeAddress);
            storeBusInfo = storeInfo.getSafeString("chainTrafficLine");

            Object lngObj = storeInfo.get("lng");
            if (!Util.isJsonNull(lngObj)) {
                storeLongitude = (double) lngObj;
            }
            Object latObj = storeInfo.get("lat");
            if (!Util.isJsonNull(latObj)) {
                storeLatitude = (double) latObj;
            }

            String storeDistanceStr = storeInfo.getSafeString("distance");
            storeDistance = Double.valueOf(storeDistanceStr);
            SLog.info("storeDistance[%.2f]", storeDistance);

            storeMapInfo = new StoreMapInfo(storeLongitude, storeLatitude, storeDistance, 0, 0,
                    storeName, storeAddress, storePhone,storeBusInfo);
            showStoreMapButton();
            storeReply = storeInfo.getInt("storeReply");
//4220            tabs.get(COMMENT_FRAGMENT).concat("()");

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public void setWorkTime(String weekDayStart, String weekDayStartTime, String weekDayEnd, String weekDayEndTime,
                            String restDayStart, String restDayStartTime, String restDayEnd, String restDayEndTime) {
        SLog.info("weekDayStart[%s], weekDayStartTime[%s], weekDayEnd[%s], weekDayEndTime[%s], restDayStart[%s], restDayStartTime[%s], restDayEnd[%s], restDayEndTime[%s]",
                weekDayStart, weekDayStartTime, weekDayEnd, weekDayEndTime, restDayStart, restDayStartTime, restDayEnd, restDayEndTime);

        String weekDayRange = weekDayStart + "至" + weekDayEnd;
        String weekDayRangeTime = weekDayStartTime + "-" + weekDayEndTime;
        String restDayRange = restDayStart + "至" + restDayEnd;
        String restDayRangeTime = restDayStartTime + "-" + restDayEndTime;

        if (StringUtil.isEmpty(weekDayStart)) {
            tvBusinessTimeWeekend.setVisibility(View.GONE);
            tvBusinessTimeWorkingDay.setText(weekDayRangeTime);
        } else {
            tvBusinessTimeWorkingDay.setText(weekDayRange + "   " + weekDayRangeTime);
        }

        if (StringUtil.isEmpty(restDayStart)) {
            tvBusinessTimeWeekend.setVisibility(View.GONE);
        } else {
            tvBusinessTimeWeekend.setText(restDayRange + "   " + restDayRangeTime);
        }
    }

    public void setStoreContact(String storePhone, String storeAddress) {
        tvPhoneNumber.setText(storePhone);
        tvShopAddress.setText(storeAddress);
    }

    private void setAnnouncements(EasyJSONArray announcements) {
        SLog.info("_announcements[%s]", announcements.toString());
        for (Object object : announcements) {
            EasyJSONObject announcement = (EasyJSONObject) object;
            String title;
            String content;
            try {
                title = announcement.getSafeString("announcementsTitle");
                content = announcement.getSafeString("announcementContent");
                StoreAnnouncement storeAnnouncement = new StoreAnnouncement(
                        announcement.getInt("id"), title, content);
                storeAnnouncement.createTime = announcement.getLong("createTime");
                storeAnnouncementList.add(storeAnnouncement);
                announcementTextList.add(Html.fromHtml("<font color='#FFFFFF' size = '2'>" + title + "</font>"));
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        // 初始化
        verticalScrollUtil = new AutoVerticalScrollTextViewUtil(tvVerticalScroll, announcementTextList);
        verticalScrollUtil.setDuration(3000)// 设置上下滚动時間间隔
                .start();   // 如果只有一條，是否可以不調用start ?
        // 点击事件监听
        verticalScrollUtil.setOnMyClickListener(ShopHomeFragment.this);

        // 如果沒有公告，則隱藏
        if (storeAnnouncementList.size() < 1) {
            llShopAnnouncementContainer.setVisibility(View.GONE);
            return;
        } else if(storeAnnouncementList.size()==1){
            llShopAnnouncementContainer.findViewById(R.id.img_notice_more).setVisibility(View.GONE);
        }
        ((StoreNoticeFragment) fragments.get(NOTICE_FRAGMENT)).setAnnouncementData(storeAnnouncementList);
    }

    private void switchStoreOpenState(int storeOpen) {
        if (storeOpen == 1) {
            imgStoreStatus.setImageResource(R.drawable.store_open);
        } else {
            imgStoreStatus.setImageResource(R.drawable.store_closed);
        }
    }


    private void loadStoreHrPost(){
        try{
            String path = Api.PATH_STORE_HRPOST+"/"+parentFragment.getStoreId();
            EasyJSONObject parse = EasyJSONObject.generate();
            Api.getUI(path, parse, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity,e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        EasyJSONArray hrPostList = responseObj.getSafeArray("datas.hrPostList");
                        if (hrPostList != null && hrPostList.length() > 0) {
                            SLog.info("hrPostList.length[%d]", hrPostList.length());
                            int index = 0;
                            for (Object object : hrPostList) {
                                EasyJSONObject hrPost = (EasyJSONObject) object;

                                WantedPostItem item = new WantedPostItem();
                                item.postId = hrPost.getInt("postId");
                                item.postTitle = hrPost.getSafeString("postTitle");
                                item.postType = hrPost.getSafeString("postType");
                                item.postArea = hrPost.getSafeString("postArea");
                                item.salaryType = hrPost.getSafeString("salaryType");
                                item.salaryRange = hrPost.getSafeString("salaryRange");
                                item.currency = hrPost.getSafeString("currency");
                                item.postDescription = hrPost.getSafeString("postDescription");
                                item.mailbox = hrPost.getSafeString("mailbox");
                                item.isFavor = hrPost.getInt("isFavor");

                                wantedPostItemList.add(item);
                                index++;
                            }
                        }
                    }catch (Exception e){
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e){
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (v instanceof DataImageView) {
            DataImageView dataImageView = (DataImageView) v;
            Object customData = dataImageView.getCustomData();
            if (customData instanceof EasyJSONObject) {
                EasyJSONObject dataObject = (EasyJSONObject) customData;

                try {
                    String socialName = dataObject.getSafeString("socialName");
                    int socialForm = dataObject.getInt("socialForm");
                    SLog.info("socialName[%s], socialForm[%d]", socialName, socialForm);

                    // socialForm   1 链接，2 二维码 3 賬號類型
                    if (socialForm == 1) {
                        Intent intent =new Intent(Intent.ACTION_VIEW);
                        String accountAddress = dataObject.getSafeString("accountAddress");
                        if (StringUtil.isEmpty(accountAddress)) {
                            return;
                        }
                        Uri uri = Uri.parse(accountAddress);
                        intent.setData(uri);

                        _mActivity.startActivity(intent);
                    } else if (socialForm == 2) {
                        String accountImageAddress = StringUtil.normalizeImageUrl(dataObject.getSafeString("accountImageAddress"));
                        new XPopup.Builder(_mActivity)
                                .asCustom(new TwQRCodePopup(_mActivity, accountImageAddress))
                                .show();
                    } else if (socialForm == 3) {
                        String account = dataObject.getSafeString("account");

                        String content = String.format("%s: %s", socialName, account);
                        ClipboardUtils.copyText(_mActivity, account);
                        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                                .setPopupCallback(new XPopupCallback() {
                                    @Override
                                    public void onShow() {
                                    }
                                    @Override
                                    public void onDismiss() {
                                    }
                                }).asCustom(new TwConfirmPopup(_mActivity, "社交帳號已複製", content, new OnConfirmCallback() {
                            @Override
                            public void onYes() {
                                SLog.info("onYes");
                            }

                            @Override
                            public void onNo() {
                                SLog.info("onNo");
                            }
                        }))
                                .show();
                    }

                    /*
                    if (socialName.equals("WeChat")) {  // 如果是微信，显示微信二维码
                        String accountImageAddress = dataObject.getSafeString("accountImageAddress");
                        new XPopup.Builder(_mActivity)
                                .asCustom(new ImagePopup(_mActivity, accountImageAddress))
                                .show();
                    } else if (socialName.equals("Facebook") || socialName.equals("Instagram") || socialName.equals("WhatsApp")) { // 打开社交网址用户主页
                        Intent intent =new Intent(Intent.ACTION_VIEW);
                        String accountAddress = dataObject.getSafeString("accountAddress");
                        if (StringUtil.isEmpty(accountAddress)) {
                            return;
                        }
                        Uri uri = Uri.parse(accountAddress);
                        intent.setData(uri);

                        _mActivity.startActivity(intent);
                    }
                     */
                } catch (EasyJSONException e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        }

        switch (id) {
            case R.id.ll_uo_thumb_up_container:
                switchThumbState();
                break;
            case R.id.ll_uo_like_container:
                switchFavoriteState();
                break;
            case R.id.btn_shop_map:
                if (storeMapInfo == null) {
                    return;
                }
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new AmapPopup(_mActivity, storeMapInfo))
                        .show();
                break;
            case R.id.img_shop_avatar:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new MerchantIntroductionPopup(_mActivity, merchantIntroduction))
                        .show();
                break;
            case R.id.ll_uo_share_container:
                pullShare();
                break;
            case R.id.rl_shop_comment_container:
                Util.startFragment(CommentListFragment.newInstance(storeId, Constant.COMMENT_CHANNEL_STORE));
                break;
            case R.id.container_view:
                    SLog.info("scroll view");
                    parentFragment.onCbStopNestedScroll();
                    break;
//            case R.id.ll_store_wanted_container:
//                new XPopup.Builder(_mActivity)
//                        // 如果不加这个，评论弹窗会移动到软键盘上面
//                        .moveUpToKeyboard(false)
//                        .asCustom(new StoreWantedPopup(_mActivity, wantedPostItemList))
//                        .show();
//                break;
            case R.id.ll_shop_announcement_container:
                showAnnouncementPopup();
                break;
            case R.id.btn_show_all_store_friends:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new InStorePersonPopup(_mActivity, inStorePersonCount, inStorePersonItemList))
                        .show();
                break;
            case R.id.img_store_figure:
                if (Config.DEVELOPER_MODE) {
                    tvShopSignature.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_show_store_qr_code:
                String storeUrl = Config.WEB_BASE_URL + "/store/" + storeId;
                new XPopup.Builder(_mActivity)
                        .asCustom(new TwQRCodePopup(_mActivity, storeUrl))
                        .show();
                break;
            case R.id.btn_play:
                String videoId = Util.getYoutubeVideoId(storeVideoUrl);
                if (!Util.isYoutubeInstalled(_mActivity)) {
                    ToastUtil.error(_mActivity, getString(R.string.install_youtube_player_hint));
                    return;
                }

                if (!StringUtil.isEmpty(videoId)) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(_mActivity, Config.YOUTUBE_DEVELOPER_KEY, videoId);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    public void pullShare() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new SharePopup(_mActivity, SharePopup.generateStoreShareLink(storeId), storeName, storeSignature, storeAvatarUrl, EasyJSONObject.generate(
                        "shareType", SharePopup.SHARE_TYPE_STORE,
                        "storeId", storeId,
                        "storeAvatar", storeAvatarUrl,
                        "storeName", storeName,
                        "storeSignature", storeSignature
                )))
                .show();
    }

    private void switchThumbState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId,
                "isLike", 1 - isLike,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);


        Api.postUI(Api.PATH_STORE_LIKE, params, new UICallback() {
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
                        likeCount++;
                    } else {
                        likeCount--;
                    }
                    updateThumbView();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void switchFavoriteState() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId,
                "clientType", Constant.CLIENT_TYPE_ANDROID,
                "token", token);

        String path;
        if (isFavorite == Constant.ONE) {
            path = Api.PATH_STORE_FAVORITE_DELETE;
        } else {
            path = Api.PATH_STORE_FAVORITE_ADD;
        }

        SLog.info("path[%s], params[%s]", path, params);
        Api.postUI(path, params, new UICallback() {
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
                    if (isFavorite == 1) {
                        favoriteCount++;
                    } else {
                        favoriteCount--;
                    }
                    updateFavoriteView();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void setImageBanner() {
        // 使RecyclerView像ViewPager一样的效果，一次只能滑一页，而且居中显示
        // https://www.jianshu.com/p/e54db232df62
        countDownHandler = new CountDownHandler(pageIndicatorView, rvGalleryImageList);
        rvGalleryImageList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        (new PagerSnapHelper()).attachToRecyclerView(rvGalleryImageList);
        goodsGalleryAdapter = new GoodsGalleryAdapter(_mActivity, currGalleryImageList);

        rvGalleryImageList.setAdapter(goodsGalleryAdapter);
        rvGalleryImageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currGalleryPosition = ((LinearLayoutManager) rvGalleryImageList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    SLog.info("currPosition[%d],newState[%d]", currGalleryPosition,newState);
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
    private void updateThumbView() {
        if (isLike == Constant.ONE) {
            btnStoreThumb.setImageResource(R.drawable.icon_store_thumb_red);
        } else {
            btnStoreThumb.setImageResource(R.drawable.icon_store_thumb_grey);
        }
        tvLikeCount.setText(String.valueOf(likeCount));
    }

    private void updateFavoriteView() {
        if (isFavorite == Constant.ONE) {
            btnStoreFavorite.setImageResource(R.drawable.icon_store_favorite_yellow);
            tvFavoriteCount.setText(R.string.text_followed);
            tvFavoriteCount.setTextColor(getResources().getColor(R.color.tw_yellow, null));
        } else {
            btnStoreFavorite.setImageResource(R.drawable.icon_store_favorite_grey);
            tvFavoriteCount.setText(R.string.text_follow);
            tvFavoriteCount.setTextColor(getResources().getColor(R.color.tw_black, null));

        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(verticalScrollUtil != null) {
            verticalScrollUtil.stop();
        }
    }

    @Override
    public void onMyClickListener(int i, CharSequence charSequence) {
        showAnnouncementPopup();
    }

    private void showAnnouncementPopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new StoreAnnouncementPopup(_mActivity, storeAnnouncementList))
                .show();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerViewHeight == 0) {
            containerViewHeight = containerView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);

            int tabHeight = smartTabLayout.getHeight();
            int toolBarHeight = parentFragment.tool.getHeight();

            ViewGroup.LayoutParams layoutParams = viewpager.getLayoutParams();
            layoutParams.height = containerViewHeight - tabHeight-toolBarHeight;
            viewpager.setLayoutParams(layoutParams);
        }

        startCountDown();

        if (PermissionUtil.hasPermission(new String[] {Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION})) {
            TencentLocationTask.doLocation(_mActivity);
        }

        showStoreMapButton();
    }

    private void showStoreMapButton() {
        if (storeMapInfo != null && storeMapInfo.storeLatitude > 0.1 && storeMapInfo.storeLongitude > 0.1) {
            btnShopMap.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        stopCountDown();
    }
    private void startCountDown() {
        if (timer == null) {
            timer = new Timer();
        }

        if (bannerStart) {
            return;
        }
        // 定时服务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                SLog.info("threadId[%s]", Thread.currentThread().getId());

                Message message = new Message();
                int position = ((LinearLayoutManager) rvGalleryImageList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//                SLog.info("position [%d],sum[%d]",position,currGalleryImageList.size());
                int size = currGalleryImageList.size();
                if (size > 0) {
                    currGalleryPosition = (position+1) % currGalleryImageList.size();
                    message.arg1 = currGalleryPosition;
                    if (countDownHandler != null) {
                        countDownHandler.sendMessage(message);
                    }
                }
            }
        }, 500, 3000);  // 0.5秒后启动，每隔2秒运行一次
    }

    private void stopCountDown() {
        bannerStart = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
