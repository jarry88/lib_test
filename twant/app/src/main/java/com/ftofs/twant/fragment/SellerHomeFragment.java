package com.ftofs.twant.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsGalleryAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreAnnouncement;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.fragment.SellerGoodsListFragment;
import com.ftofs.twant.seller.fragment.SellerOrderListFragment;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.CustomRecyclerView;
import com.ftofs.twant.widget.StoreAnnouncementPopup;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.XPopup;
import com.rd.PageIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



import butterknife.OnClick;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextViewUtil;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static android.view.View.VISIBLE;

/**
 * @author gzp
 * Created 2020-4-28 上午 10:21
 * 商家首頁
 */

public class SellerHomeFragment extends BaseFragment implements AutoVerticalScrollTextViewUtil.OnMyClickListener {


    CircleImageView imgSellerAvatar;

    TextView tvOrderWaitSendCount;

    TextView tvOrderWaitSendInfoCount;

//    @OnClick(R.id.ll_add_goods)
//    void goAddGoods() {
//        Util.startFragment(AddGoodsFragment.newInstance());
//    }


    void goToSellerGoodsList() {
        Util.startFragment(SellerGoodsListFragment.newInstance());
    }


    ViewFlipper viewFlipper;


    TextView tvRefundWaitingCount;

    TextView tvGoodsCommonFailCount;

    TextView tvBillCount;

    TextView tvMemberNickName;
    private AutoVerticalScrollTextViewUtil verticalScrollUtil;
    private List<StoreAnnouncement> storeAnnouncementList;
    private ArrayList<CharSequence> announcementTextList=new ArrayList<>();

    LinearLayout llShopAnnouncementContainer;

    TextView tvOrderWaitPayCount;

    TextView tvGoodsCommonWaitCount;


    TextView tvGoodsCommonBanCount;

    TextView tvGoodsStockOnlineAlarmCount;

    TextView tvReturnWaitingCount;

    TextView tvGoodsStockAlarmCount;


    TextView tvComplainAccessCount;


    TextView tvComplainTalkCount;

    TextView tvDiscountGoogsCount;
    private GoodsGalleryAdapter goodsGalleryAdapter;
    private int currGalleryPosition;
    //店鋪營業狀態
    private int storeState;

    ImageView btnPlay;


    void playVideo() {
        String videoId = Util.getYoutubeVideoId(storeVideoUrl);

        if (!StringUtil.isEmpty(videoId)) {
            Util.playYoutubeVideo(_mActivity, videoId);
        }
    }

    void showPopup(){
        SLog.info("click");
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new StoreAnnouncementPopup(_mActivity, storeAnnouncementList))
                .show();
    }
    private String storeVideoUrl;


    void popBack() {
        pop();
    }


    SwitchButton swBusinessState;

    ImageView sellerHomeBackground;

    TextView tvStoreName;

    TextView tvGoodsCommonOfflineAndPassCount;


    CircleImageView imgStoreAvatar;

    TextView tvStoreSignature;

    AutoVerticalScrollTextView tvVerticalScroll;

    TextView tvTodayAmount;


    TextView tvTodayOrderCount;

    TextView tvTodayViewCount;

    TextView tvMessageCount;

    TextView tvTodayCommentedCount;


    void showGoodsInfo() {
        TextView textGoods = getView().findViewById(R.id.btn_goods_info);
        TextView textOrders = getView().findViewById(R.id.btn_orders_info);
        textOrders .setBackgroundResource(R.drawable.grey_20dp_bg);
        textOrders.setTextColor(getResources().getColor(R.color.tw_black));
        textGoods .setBackgroundResource(R.drawable.blue_20dp_bg);
        textGoods.setTextColor(getResources().getColor(R.color.tw_white));
        getView().findViewById(R.id.ll_container_goods_info).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.ll_container_orders_info).setVisibility(View.GONE);
    }


    void showOrdersInfo() {
        TextView textGoods = getView().findViewById(R.id.btn_goods_info);
        TextView textOrders = getView().findViewById(R.id.btn_orders_info);
        textGoods .setBackgroundResource(R.drawable.grey_20dp_bg);
        textGoods.setTextColor(getResources().getColor(R.color.tw_black));
        textOrders .setBackgroundResource(R.drawable.blue_20dp_bg);
        textOrders.setTextColor(getResources().getColor(R.color.tw_white));
        getView().findViewById(R.id.ll_container_goods_info).setVisibility(View.GONE);
        getView().findViewById(R.id.ll_container_orders_info).setVisibility(View.VISIBLE);
    }


    void gotoSellerRefund() {
        Util.startFragment(SellerRefundFragment.newInstance());
        return;//暫時屏蔽該功能
    }

    void gotoSellerOrderList() {
        Util.startFragment(SellerOrderListFragment.newInstance());
    }

    void gotoSellerGoodsList() {
        Util.startFragment(SellerGoodsListFragment.newInstance());
    }


    void gotoSellerOrderSend() {
        Util.startFragment(SellerOrderListFragment.newInstance(Constant.ORDER_STATUS_TO_BE_SHIPPED));
    }


    TextView tvOnSaleCount;


    PageIndicatorView pageIndicatorView;

    CustomRecyclerView rvGalleryImageList;
    public static SellerHomeFragment newInstance() {

        SellerHomeFragment fragment = new SellerHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(verticalScrollUtil != null) {
            verticalScrollUtil.stop();
        }
    }
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


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadSellerData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        imgSellerAvatar = (CircleImageView) view.findViewById(R.id.img_seller_avatar);
        tvOrderWaitSendCount = (TextView) view.findViewById(R.id.tv_orders_wait_send_count);
        tvOrderWaitSendInfoCount = (TextView) view.findViewById(R.id.tv_orders_wait_send);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.vf_vertical_scroll);
        tvRefundWaitingCount = (TextView) view.findViewById(R.id.tv_refund_waiting_count);
        tvGoodsCommonFailCount = (TextView) view.findViewById(R.id.tv_goods_common_fail_count);
        tvBillCount = (TextView) view.findViewById(R.id.tv_bill_count);
        tvMemberNickName = (TextView) view.findViewById(R.id.tv_seller_member_name);
        llShopAnnouncementContainer = (LinearLayout) view.findViewById(R.id.ll_shop_announcement_container);
        tvOrderWaitPayCount = (TextView) view.findViewById(R.id.tv_orders_wait_pay_count);
        tvGoodsCommonWaitCount = (TextView) view.findViewById(R.id.tv_goods_common_wait_count);
        tvGoodsCommonBanCount = (TextView) view.findViewById(R.id.tv_goods_common_ban_count);
        tvGoodsStockOnlineAlarmCount = (TextView) view.findViewById(R.id.tv_goods_stock_online_alarm_count);
        tvReturnWaitingCount = (TextView) view.findViewById(R.id.tv_return_waiting_count);
        tvGoodsStockAlarmCount = (TextView) view.findViewById(R.id.tv_goods_stock_alarm_count);
        tvComplainAccessCount = (TextView) view.findViewById(R.id.tv_complain_access_count);
        tvComplainTalkCount = (TextView) view.findViewById(R.id.tv_complain_talk_count);
        tvDiscountGoogsCount = (TextView) view.findViewById(R.id.tv_discount_goods_count);
        btnPlay = (ImageView) view.findViewById(R.id.btn_play);
        swBusinessState = (SwitchButton) view.findViewById(R.id.sw_seller_business_state);
        sellerHomeBackground = (ImageView) view.findViewById(R.id.img_seller_home_background);
        tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        tvGoodsCommonOfflineAndPassCount = (TextView) view.findViewById(R.id.tv_goods_offline_and_pass_count);
        imgStoreAvatar = (CircleImageView) view.findViewById(R.id.img_avatar);
        tvStoreSignature = (TextView) view.findViewById(R.id.tv_store_signature);
        tvVerticalScroll = (AutoVerticalScrollTextView) view.findViewById(R.id.tv_vertical_scroll);
        tvTodayAmount = (TextView) view.findViewById(R.id.tv_today_amount);
        tvTodayOrderCount = (TextView) view.findViewById(R.id.tv_today_order_count);
        tvTodayViewCount = (TextView) view.findViewById(R.id.tv_today_view_count);
        tvMessageCount = (TextView) view.findViewById(R.id.tv_message_count);
        tvTodayCommentedCount = (TextView) view.findViewById(R.id.tv_new_comment_count);
        tvOnSaleCount = (TextView) view.findViewById(R.id.tv_goods_common_onsale_count);
        pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.pageIndicatorView);
        rvGalleryImageList = (CustomRecyclerView) view.findViewById(R.id.rv_gallery_image_list);
        view.findViewById(R.id.ll_seller_order_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSellerOrderSend();
            }
        });
        view.findViewById(R.id.ll_goods_list_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSellerGoodsList();
            }
        });
        view.findViewById(R.id.ll_order_list_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSellerOrderList();
            }
        });
        view.findViewById(R.id.btn_goto_seller_refund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSellerRefund();
            }
        });
        view.findViewById(R.id.btn_orders_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersInfo();
            }
        });
        view.findViewById(R.id.btn_goods_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoodsInfo();
            }
        });
        view.findViewById(R.id.btn_goto_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBack();
            }
        });
        view.findViewById(R.id.ll_shop_announcement_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
        view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
        view.findViewById(R.id.ll_seller_goods_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSellerGoodsList();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storeAnnouncementList = new ArrayList<>();
        announcementTextList = new ArrayList<>();
        timer = new Timer();
        setImageBanner();


        swBusinessState.setText("  營業  ", " 休息  ");
        swBusinessState.setOnClickListener(v -> {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                updateSwitchButton();
                return;
            }

            int setStoreState=1-storeState;
            Api.postUI(Api.PATH_SELLER_ISOPEN, EasyJSONObject.generate("token", token, "state", setStoreState), new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    updateSwitchButton();
                    ToastUtil.showNetworkError(_mActivity,e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity,responseObj)) {
                        updateSwitchButton();
                        return;
                    }
                    try {
                        ToastUtil.success(_mActivity,responseObj.getSafeString("datas.success"));
                        storeState = setStoreState;
                        updateSwitchButton();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        });
//        swBusinessState.setBackColorRes(getResources().getColor(R.color.tw_yellow));
//        swBusinessState.setThumbColorRes(getResources().getColor(R.color.tw_blue));
        // 初始化
//        loadSellerData();

    }

    private void initView() {

        verticalScrollUtil = new AutoVerticalScrollTextViewUtil(tvVerticalScroll, announcementTextList);
        verticalScrollUtil.setDuration(3000)// 设置上下滚动時間间隔
                .start();   // 如果只有一條，是否可以不調用start ?
        // 点击事件监听
        verticalScrollUtil.setOnMyClickListener(SellerHomeFragment.this);
        // 如果沒有公告，則隱藏
        if (storeAnnouncementList.size() < 1) {
            llShopAnnouncementContainer.setVisibility(View.GONE);
            return;
        } else if(storeAnnouncementList.size()==1){
            llShopAnnouncementContainer.findViewById(R.id.img_notice_more).setVisibility(View.GONE);
        }
    }

    private void loadSellerData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        Api.getUI(Api.PATH_SELLER_INDEX, EasyJSONObject.generate("token", token), new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responsetr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }
                updateView(responseObj);
                startCountDown();
            }
        });
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        stopCountDown();
    }

    private void updateView(EasyJSONObject responseObj) {
        try {
            if (responseObj.exists("datas.todayViewCount")) {
                String todayViewCount = responseObj.getSafeString("datas.todayViewCount");
                tvTodayViewCount.setText(String.valueOf(todayViewCount));
            }
            if (responseObj.exists("datas.messageCount")) {
                int messageCount = responseObj.getInt("datas.messageCount");
                tvMessageCount.setText(String.valueOf(messageCount));
            }
            String storeSignature = responseObj.getSafeString("datas.storeSignature");
            tvStoreSignature.setText(storeSignature);
            String memberAvatar = responseObj.getSafeString("datas.memberAvatar");
            if (!StringUtil.isEmpty(memberAvatar)) {
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(memberAvatar)).centerCrop().into(imgSellerAvatar);
            }
            String memberNickName = responseObj.getSafeString("datas.memberNickName");
            tvMemberNickName.setText(memberNickName);
            String storeName = responseObj.getSafeString("datas.storeName");
            tvStoreName.setText(storeName);
            int billCount = responseObj.getInt("datas.billCount");
            tvBillCount.setText(String.valueOf(billCount));
            double ordersAmount = responseObj.getDouble("datas.ordersAmount");
            tvTodayAmount.setText(String.format("%.2f",ordersAmount));
            storeState = responseObj.getInt("datas.storeState");
            updateSwitchButton();

            String storeWeekDayEndTime = responseObj.getSafeString("datas.storeWeekDayEndTime");
            String storeAvatar = responseObj.getSafeString("datas.storeAvatar");
            if (!StringUtil.isEmpty(storeAvatar)) {
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeAvatar)).centerCrop().into(imgStoreAvatar);
            }
            String storeVideo = responseObj.getSafeString("datas.storeVideo");
            String storeRestDayStart = responseObj.getSafeString("datas.storeRestDayStart");
            int ordersCount = responseObj.getInt("datas.ordersCount");
            tvTodayOrderCount.setText(String.valueOf(ordersCount));
            int ordersWaitSendCount = responseObj.getInt("datas.ordersWaitSendCount");
            tvOrderWaitSendCount.setText(String.valueOf(ordersWaitSendCount));
            tvOrderWaitSendInfoCount.setText(String.valueOf(ordersWaitSendCount));
            int ordersWaitPayCount = responseObj.getInt("datas.ordersWaitPayCount");
            tvOrderWaitPayCount.setText(String.valueOf(ordersWaitPayCount));
            int refundWaitingCount = responseObj.getInt("datas.refundWaitingCount");
            tvRefundWaitingCount.setText(String.valueOf(refundWaitingCount));
            int goodsCommonVerifyFailCount = responseObj.getInt("datas.goodsCommonVerifyFailCount");
            tvGoodsCommonFailCount.setText(String.valueOf(goodsCommonVerifyFailCount));
            int goodsCommonOnSaleCount = responseObj.getInt("datas.goodsCommonOnSaleCount");
            tvOnSaleCount.setText(String.valueOf(goodsCommonOnSaleCount));
            int newCommonCount = responseObj.getInt("datas.newCommentCount");
            tvTodayCommentedCount.setText(String.valueOf(newCommonCount));
            int complainAccessCount = responseObj.getInt("datas.complainAccessCount");
            tvComplainAccessCount.setText(String.valueOf(complainAccessCount));
            int complainTalkCount = responseObj.getInt("datas.complainTalkCount");
            tvComplainTalkCount.setText(String.valueOf(complainTalkCount));
            int goodsStockAlarmCount = responseObj.getInt("datas.goodsStockAlarmCount");
            tvGoodsStockAlarmCount.setText(String.valueOf(goodsStockAlarmCount));
            int goodsCommonWaitCount = responseObj.getInt("datas.goodsCommonWaitCount");
            tvGoodsCommonWaitCount.setText(String.valueOf(goodsCommonWaitCount));
            int goodsStockOnlineAlarmCount = responseObj.getInt("datas.goodsStockOnlineAlarmCount");
            tvGoodsStockOnlineAlarmCount.setText(String.valueOf(goodsStockOnlineAlarmCount));
            int returnWaitingCount = responseObj.getInt("datas.returnWaitingCount");
            tvReturnWaitingCount.setText(String.valueOf(returnWaitingCount));
            int goodsCommonOfflineAndPassCount = responseObj.getInt("datas.goodsCommonOfflineAndPassCount");
            tvGoodsCommonOfflineAndPassCount.setText(String.valueOf(goodsCommonOfflineAndPassCount));
            int goodsCommonBanCount = responseObj.getInt("datas.goodsCommonBanCount");
            tvGoodsCommonBanCount.setText(String.valueOf(goodsCommonBanCount));

            int discountGoogsCount = responseObj.getInt("datas.discountGoogsCount");
            tvDiscountGoogsCount.setText(String.valueOf(discountGoogsCount));
            EasyJSONArray storeNoticeList = responseObj.getSafeArray("datas.storeNoticeList");

            announcementTextList.clear();
            storeAnnouncementList.clear();
            for (Object object : storeNoticeList) {
                EasyJSONObject announcement = (EasyJSONObject) object;
                String title = announcement.getSafeString("title");
                String content = announcement.getSafeString("content");
                StoreAnnouncement storeAnnouncement = new StoreAnnouncement(announcement.getInt("articleId"), title, content);
                storeAnnouncement.createTime = Jarbon.parse(announcement.getSafeString("createTime")).getTimestampMillis();
                storeAnnouncementList.add(storeAnnouncement);
                announcementTextList.add(Html.fromHtml("<font color='#2A292A' size = '2'>" + title + "</font>"));
                TextView textView = new TextView(_mActivity);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setText(Html.fromHtml("<font color='#2A292A' size = '2'>" + title + "</font>"));
                viewFlipper.addView(textView);
            }
            SLog.info("%d",storeAnnouncementList.size());
            initView();


            EasyJSONArray figureList = responseObj.getSafeArray("datas.figureList");
            currGalleryImageList.clear();
            for (Object object : figureList) {
                if (!Util.isJsonNull(object)) {
                    currGalleryImageList.add(String.valueOf(object));
                }
            }
            if (responseObj.exists("datas.storeVideo")) {
                storeVideo = responseObj.getSafeString("datas.storeVideo");
                if (StringUtil.isYoutubeUrl(storeVideo)) {
                    storeVideoUrl = storeVideo;
                    btnPlay.setVisibility(VISIBLE);
                }
            }
            updateBanner(false);
            updateNoticeView();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateNoticeView() {
        // 如果沒有公告，則隱藏
        if (storeAnnouncementList.size() < 1) {
            llShopAnnouncementContainer.setVisibility(View.GONE);
            return;
        } else if (storeAnnouncementList.size() == 1) {
            llShopAnnouncementContainer.findViewById(R.id.img_notice_more).setVisibility(View.GONE);
        }
    }

    private void updateSwitchButton() {
        swBusinessState.setChecked(storeState== Constant.TRUE_INT);
        swBusinessState.setLinkTextColor(getResources().getColor(storeState== Constant.FALSE_INT?R.color.tw_white:R.color.tw_black,getActivity().getTheme()));
        swBusinessState.setTextColor(getResources().getColor(storeState== Constant.FALSE_INT?R.color.tw_white:R.color.tw_black,getActivity().getTheme()));
//        swBusinessState.setThumbColorRes(R.color.tw_white);
        SLog.info("sb [%s],storestate [%s],coloer [%s]",swBusinessState.isChecked(),storeState,swBusinessState.getCurrentTextColor());

    }

    @Override
    public void onMyClickListener(int i, CharSequence charSequence) {

    }
    private void updateBanner(boolean hasSlider) {
        if ( currGalleryImageList.size() < 1) {
            currGalleryImageList.add("placeholder");  // 如果沒有圖片，加一張默認的空櫥窗占位圖
        }
        SLog.info("設置banner數據 [%d]",currGalleryImageList.size());
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

                if (newState == RecyclerView.SCROLL_STATE_IDLE&&!StringUtil.isArrayEmpty(currGalleryImageList)) {
                    try{
                        currGalleryPosition = ((LinearLayoutManager) rvGalleryImageList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        SLog.info("currPosition[%d],newState[%d]", currGalleryPosition,newState);
                        int position = currGalleryPosition % currGalleryImageList.size();
                        pageIndicatorView.setSelection(position);
                    }catch (Exception e) {
                       SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
            try{
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
            }catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
            }
        }, 500, 3000);  // 0.5秒后启动，每隔3秒运行一次
    }

    private void stopCountDown() {
        bannerStart = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
