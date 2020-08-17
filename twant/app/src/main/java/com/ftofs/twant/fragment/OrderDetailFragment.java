package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.OrderDetailGoodsAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.GroupBuyStatus;
import com.ftofs.twant.constant.OrderOperation;
import com.ftofs.twant.constant.OrderState;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EvaluationGoodsItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.store.StoreVo;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SharePopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
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
import cn.snailpad.easyjson.EasyJSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static android.view.View.GONE;

/**
 * 訂單詳情
 *
 * @author zwm
 */
public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int ordersId;
    int storeId;
    int tariffBuy;
    String storeName;
    String paySnStr;
    int payId;
    double ordersAmount = -1;
    String paymentTypeCode; // 当前的支付方式

    int isGroup = Constant.FALSE_INT;
    int goId;
    long groupEndTime;

    // 倒計時
    CountDownTimer countDownTimer;

    OrderDetailGoodsAdapter adapter;
    List<OrderDetailGoodsItem> orderDetailGoodsItemList = new ArrayList<>();


    HorizontalScrollView hsvBottomToolbar;

    TextView tvReceiverName;

    TextView tvMobile;

    TextView tvAddress;

    TextView tvStoreName;

    TextView tvFreightAmount;

    TextView tvOrdersAmount;

    TextView tvOrdersStateName;

    TextView tvShipTime;


    TextView tvSendTime;

    TextView tvLeaveMessage;

    LinearLayout llLeaveMessageContainer;

    RelativeLayout rlTaxContainer;

    TextView tvTaxAmount;
    String storePhone;

    TextView tvGroupCountDown;

    boolean needReloadData;

    public static final int GO_STATE_ONGOING = 0;
    public static final int GO_STATE_SUCCESS = 1;
    public static final int GO_STATE_FAILURE = 2;
    int goState;
    int ordersState;
    int showRefundWaiting;

    // 團購分享數據
    String groupShareTitle;
    String groupShareImage;
    String groupShareContent;
    String groupShareUrl;
    int groupShareCommonId;
    int groupShareGoodsId;
    double groupSharePrice;


    public static final String TEXT_MEMBER_BUY_AGAIN = "showMemberBuyAgain";
    public static final String TEXT_MEMBER_RECEIVE = "showMemberReceive";
    public static final String TEXT_REFUND_WAITING = "showRefundWaiting";
    public static final String TEXT_MEMBER_REFUND_ALL = "showMemberRefundAll";
    public static final String TEXT_EVALUATION = "showEvaluation";
    public static final String TEXT_SHIP_SEARCH = "showShipSearch";
    public static final String TEXT_MEMBER_CANCEL = "showMemberCancel";
    public static final String TEXT_MEMBER_DELETE = "showMemberDelete";
    public static final String TEXT_MEMBER_PAY = "showMemberPay";

    String[] buttonNameList = new String[]{
            TEXT_MEMBER_BUY_AGAIN, TEXT_MEMBER_RECEIVE, TEXT_REFUND_WAITING, TEXT_MEMBER_REFUND_ALL,
            TEXT_EVALUATION, TEXT_SHIP_SEARCH, TEXT_MEMBER_CANCEL, TEXT_MEMBER_DELETE, TEXT_MEMBER_PAY};

    Map<String, String> orderButtonNameMap = new HashMap<>();

    SwipeRefreshLayout swipeRefreshContainer;

    TextView tvOrderStatus;

    TextView tvOrderStatusDesc;

    ImageView iconOrderStatus;

    TextView tvGoodsAmount;

    LinearLayout llShipType;

    TextView btnBuyAgain;

    TextView btnPayOrder;
    private LinearLayout llTakeGoodsContainer;
    private TextView tvStorePhone;
    private TextView tvBusinessTimeWeekend;
    private TextView tvBusinessTimeWorkingDay;
    private TextView tvTakeAddr;
    private TextView tvStoreTransport;


    public void back() {
        hideSoftInputPop();
    }


    TextView tvFragmentTitle;

    RelativeLayout toolBar;


    public void buyAgain(View v) {
        SLog.info("isGroup[%d], goState[%d]", isGroup, goState);
        if (isGroup == Constant.TRUE_INT && goState != GO_STATE_FAILURE) { // 如果是團購，則為邀請好友
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, SharePopup.generateGoodsShareLink(groupShareCommonId, groupShareGoodsId), groupShareTitle,
                            groupShareContent, groupShareImage, EasyJSONObject.generate("shareType", SharePopup.SHARE_TYPE_GOODS,
                            "commonId", groupShareCommonId, "goodsName", groupShareTitle,
                            "goodsImage", groupShareImage, "goodsPrice", groupSharePrice)))
                    .show();
        } else {
            SLog.info("添加到購物袋%d",ordersId);
            for (OrderDetailGoodsItem goodsItem:orderDetailGoodsItemList){
                if (Config.PROD) {
                    HashMap<String, Object> analyticsDataMap = new HashMap<>();
                    analyticsDataMap.put("commonId", goodsItem.commonId);
                    MobclickAgent.onEventObject(TwantApplication.getInstance(), UmengAnalyticsActionName.GOODS_ADD_TO_CART, analyticsDataMap);
                }

                Util.changeCartContent(_mActivity, goodsItem.goodsId, 1, data -> {ToastUtil.success(_mActivity, "添加購物袋成功");
                    Util.startFragment(CartFragment.newInstance(true));});
            }
        }
    }

    public void payOrder(View v) {
        SLog.info("添加到購物袋%d",ordersId);
        Util.startFragment(PayVendorFragment.newInstance(payId, ordersAmount, 0));
    }


    LinearLayout btnGotoStore;

    LinearLayout llOrderDetailGoodsList;

    LinearLayout btnAdvisoryService;

    LinearLayout btnDialStorePhone;

    TextView tvTakeCode;

    RelativeLayout rlTakeCodeContainer;

    TextView tvOrdersSn;

    TextView tvCreateTime;

    LinearLayout llOrderCreateTimeContainer;

    LinearLayout llOrderSendTimeContainer;

    TextView tvPayType;

    LinearLayout llOrderPayType;

    TextView tvPaySn;

    LinearLayout llOrderPaySnContainer;

    TextView tvPaymentTime;

    LinearLayout llOrderPaymentTimeContainer;

    TextView tvShipType;

    TextView tvShipDate;

    LinearLayout llShipInfo;

    TextView tvStoreSendInfo;

    SquareGridLayout sglImageContainer;

    LinearLayout llStoreSendInfoContainer;

    TextView tvStoreWelfare;

    TextView tvPlatformWelfare;

    LinearLayout llOrderButtonContainer;

    RelativeLayout rlSendContainer;


    public static OrderDetailFragment newInstance(int ordersId) {
        Bundle args = new Bundle();

        args.putInt("ordersId", ordersId);
        SLog.info("ordersId[%d]", ordersId);

        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        hsvBottomToolbar = (HorizontalScrollView) view.findViewById(R.id.hsv_bottom_toolbar);
        tvReceiverName = (TextView) view.findViewById(R.id.tv_receiver_name);
        tvMobile = (TextView) view.findViewById(R.id.tv_mobile);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvStoreName = (TextView) view.findViewById(R.id.tv_store_name);
        tvFreightAmount = (TextView) view.findViewById(R.id.tv_freight_amount);
        tvOrdersAmount = (TextView) view.findViewById(R.id.tv_orders_amount);
        tvOrdersStateName = (TextView) view.findViewById(R.id.tv_orders_state_name);
        tvShipTime = (TextView) view.findViewById(R.id.tv_ship_time);
        tvSendTime = (TextView) view.findViewById(R.id.tv_send_time);
        tvLeaveMessage = (TextView)view. findViewById(R.id.tv_leave_message);
        llLeaveMessageContainer = (LinearLayout) view.findViewById(R.id.ll_leave_message_container);
        rlTaxContainer = (RelativeLayout) view.findViewById(R.id.rl_tax_container);
        tvTaxAmount = (TextView) view.findViewById(R.id.tv_tax_amount);
        swipeRefreshContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_container);
        tvOrderStatus = (TextView) view.findViewById(R.id.tv_order_status);
        tvOrderStatusDesc = (TextView) view.findViewById(R.id.tv_order_status_desc);
        iconOrderStatus = (ImageView) view.findViewById(R.id.icon_order_status);
        tvGoodsAmount = (TextView) view.findViewById(R.id.tv_goods_amount);
        llShipType = (LinearLayout) view.findViewById(R.id.ll_ship_type);
        btnBuyAgain = (TextView) view.findViewById(R.id.btn_buy_again);
        btnPayOrder = (TextView) view.findViewById(R.id.btn_pay_order);
        tvFragmentTitle = (TextView) view.findViewById(R.id.tv_fragment_title);
        toolBar = (RelativeLayout) view.findViewById(R.id.tool_bar);
        btnGotoStore = (LinearLayout) view.findViewById(R.id.btn_goto_store);
        llOrderDetailGoodsList = (LinearLayout) view.findViewById(R.id.ll_order_detail_goods_list);
        btnAdvisoryService = (LinearLayout) view.findViewById(R.id.btn_advisory_service);
        btnDialStorePhone = (LinearLayout) view.findViewById(R.id.btn_dial_store_phone);
        tvTakeCode = (TextView) view.findViewById(R.id.tv_take_code);
        rlTakeCodeContainer = (RelativeLayout) view.findViewById(R.id.rl_take_code_container);
        llTakeGoodsContainer = (LinearLayout) view.findViewById(R.id.ll_get_goods_info);
        tvOrdersSn = (TextView) view.findViewById(R.id.tv_orders_sn);
        tvCreateTime = (TextView) view.findViewById(R.id.tv_create_time);
        llOrderCreateTimeContainer = (LinearLayout) view.findViewById(R.id.ll_order_create_time_container);
        llOrderSendTimeContainer = (LinearLayout) view.findViewById(R.id.ll_order_send_time_container);
        tvPayType = (TextView) view.findViewById(R.id.tv_pay_type);
        llOrderPayType = (LinearLayout) view.findViewById(R.id.ll_order_pay_type);
        tvPaySn = (TextView) view.findViewById(R.id.tv_pay_sn);
        llOrderPaySnContainer = (LinearLayout) view.findViewById(R.id.ll_order_pay_sn_container);
        tvPaymentTime = (TextView) view.findViewById(R.id.tv_payment_time);
        llOrderPaymentTimeContainer = (LinearLayout) view.findViewById(R.id.ll_order_payment_time_container);
        tvShipType = (TextView) view.findViewById(R.id.tv_ship_type);
        tvShipDate = (TextView) view.findViewById(R.id.tv_ship_date);
        llShipInfo = (LinearLayout) view.findViewById(R.id.ll_ship_info);
        tvStoreSendInfo = (TextView) view.findViewById(R.id.tv_store_send_info);
        sglImageContainer = (SquareGridLayout) view.findViewById(R.id.sgl_image_container);
        llStoreSendInfoContainer = (LinearLayout) view.findViewById(R.id.ll_store_send_info_container);
        tvStoreWelfare = (TextView) view.findViewById(R.id.tv_store_welfare);
        tvPlatformWelfare = (TextView) view.findViewById(R.id.tv_platform_welfare);
        llOrderButtonContainer = (LinearLayout) view.findViewById(R.id.ll_order_button_container);
        rlSendContainer = (RelativeLayout) view.findViewById(R.id.rl_send_container);

        tvStorePhone=view.findViewById(R.id.tv_store_phone);
        tvBusinessTimeWeekend=view.findViewById(R.id.tv_business_time_weekend);
        tvBusinessTimeWorkingDay=view.findViewById(R.id.tv_business_time_working_day);
        tvTakeAddr=view.findViewById(R.id.tv_take_addr);
        tvStoreTransport=view.findViewById(R.id.tv_store_transport);
        view.findViewById(R.id.btn_pay_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payOrder((View) v);
            }
        });
        view.findViewById(R.id.btn_buy_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAgain((View) v);
            }
        });
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        ordersId = args.getInt("ordersId");
        SLog.info("ordersId[%d]", ordersId);

        //下拉刷新監聽設置處理
        //设置为不能刷新
        swipeRefreshContainer.setEnabled(false);
        swipeRefreshContainer.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {//模拟耗时操作
                //取消刷新
                hsvBottomToolbar.postDelayed(() -> loadOrderDetail(), 1500);
                swipeRefreshContainer.setRefreshing(false);
            }, 200);
        });
        orderButtonNameMap.put(buttonNameList[0], getString(R.string.text_buy_again));
        orderButtonNameMap.put(buttonNameList[1], getString(R.string.text_have_received));
        orderButtonNameMap.put(buttonNameList[2], getString(R.string.text_refund_in_progress));
        orderButtonNameMap.put(buttonNameList[3], getString(R.string.text_refund_all));
        orderButtonNameMap.put(buttonNameList[4], getString(R.string.text_order_comment));
        orderButtonNameMap.put(buttonNameList[5], getString(R.string.text_view_logistics));
        orderButtonNameMap.put(buttonNameList[6], getString(R.string.text_cancel_order));
        orderButtonNameMap.put(buttonNameList[7], getString(R.string.text_delete_order));
        orderButtonNameMap.put(buttonNameList[8], getString(R.string.text_pay_order));
// 倒序排序，與iOS保持一致
        Util.arrayReverse(buttonNameList);

        Util.setOnClickListener(view, R.id.btn_goto_store, this);
        Util.setOnClickListener(view, R.id.btn_dial_store_phone, this);
        Util.setOnClickListener(view, R.id.btn_advisory_service, this);

        tvGroupCountDown = view.findViewById(R.id.tv_group_count_down);

        LinearLayout llOrderDetailGoodsList = view.findViewById(R.id.ll_order_detail_goods_list);
        adapter = new OrderDetailGoodsAdapter(_mActivity, llOrderDetailGoodsList, R.layout.order_detail_goods_item);
        adapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                OrderDetailGoodsItem item = orderDetailGoodsItemList.get(position);
                int id = view.getId();
                SLog.info("id[%d]", id);

                if (id == R.id.btn_goto_goods) {
                    Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0));
                } else if (id == R.id.btn_refund) {
                    Util.startFragment(GoodsRefundFragment.newInstance(EasyJSONObject.generate(
                            "action", Constant.ACTION_REFUND,
                            "ordersId", item.ordersId,
                            "ordersGoodsId", item.ordersGoodsId).toString()));
                } else if (id == R.id.btn_refund_all) {
                    Util.startFragment(GoodsRefundFragment.newInstance(EasyJSONObject.generate(
                            "action", Constant.ACTION_REFUND_ALL,
                            "ordersId", item.ordersId).toString()));
                } else if (id == R.id.btn_refund_waiting) {
                    Util.startFragment(RefundFragment.newInstance());
                } else if (id == R.id.btn_return) {
                    Util.startFragment(GoodsRefundFragment.newInstance(EasyJSONObject.generate(
                            "action", Constant.ACTION_RETURN,
                            "ordersId", item.ordersId,
                            "ordersGoodsId", item.ordersGoodsId).toString()));
                } else if (id == R.id.btn_complain) {
                    Util.startFragment(GoodsRefundFragment.newInstance(EasyJSONObject.generate(
                            "action", Constant.ACTION_COMPLAIN,
                            "ordersId", item.ordersId,
                            "ordersGoodsId", item.ordersGoodsId).toString()));
                }
            }
        });

        Util.setOnClickListener(view, R.id.tv_fragment_title, this);

        loadOrderDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        stopCountDown();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        SLog.info("OrderDetailFragment::onEBMessage(), messageType[%s]", message.messageType);
        if (message.messageType == EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL) {
            // 重新加載訂單詳情
            SLog.info("重新加載訂單詳情");
            needReloadData = true;
        }
    }

    private boolean handleOrderButtonClick(String tag) {
        try {
            boolean consumed = false;
            for (String buttonName : buttonNameList) {
                if (buttonName.equals(tag)) {
                    consumed = true;
                    break;
                }
            }

            if (!consumed) {
                return false;
            }

            SLog.info("tag[%s]", tag);
            // 取消訂單 或 刪除訂單
            if (tag.equals(TEXT_MEMBER_CANCEL) || tag.equals(TEXT_MEMBER_DELETE)) {
                OrderOperation operationType = OrderOperation.ORDER_OPERATION_TYPE_CANCEL;
                if (tag.equals(TEXT_MEMBER_DELETE)) {
                    operationType = OrderOperation.ORDER_OPERATION_TYPE_DELETE;
                }
                final OrderOperation finalOperationType = operationType;

                String confirmText = "確定要取消訂單嗎?";
                if (operationType == OrderOperation.ORDER_OPERATION_TYPE_DELETE) {
                    confirmText = "確定要刪除訂單嗎?";
                }
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
                        }).asCustom(new TwConfirmPopup(_mActivity, confirmText, null, new OnConfirmCallback() {
                    @Override
                    public void onYes() {
                        SLog.info("onYes");
                        orderOperation(finalOperationType);
                    }

                    @Override
                    public void onNo() {
                        SLog.info("onNo");
                    }
                }))
                        .show();
            } else if (tag.equals(TEXT_MEMBER_RECEIVE)) { // 確認收貨
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
                        }).asCustom(new TwConfirmPopup(_mActivity, "確認收貨嗎?", null, new OnConfirmCallback() {
                    @Override
                    public void onYes() {
                        SLog.info("onYes");
                        confirmReceive();
                    }

                    @Override
                    public void onNo() {
                        SLog.info("onNo");
                    }
                }))
                        .show();
            } else if (tag.equals(TEXT_MEMBER_BUY_AGAIN)) {
                orderOperation(OrderOperation.ORDER_OPERATION_TYPE_BUY_AGAIN);
            } else if (tag.equals(TEXT_EVALUATION)) {
                List<EvaluationGoodsItem> evaluationGoodsItemList = new ArrayList<>();
                for (OrderDetailGoodsItem orderDetailGoodsItem : orderDetailGoodsItemList) {
                    EvaluationGoodsItem evaluationGoodsItem = new EvaluationGoodsItem(orderDetailGoodsItem.commonId, orderDetailGoodsItem.imageSrc,
                            orderDetailGoodsItem.goodsName, orderDetailGoodsItem.goodsFullSpecs);
                    evaluationGoodsItemList.add(evaluationGoodsItem);
                }
                Util.startFragment(GoodsEvaluationFragment.newInstance(ordersId, storeId, storeName, evaluationGoodsItemList));
            } else if (tag.equals(TEXT_MEMBER_PAY)) {
                start(PayVendorFragment.newInstance(payId, ordersAmount, 0));
            } else if (tag.equals(TEXT_SHIP_SEARCH)) {
                SLog.info("查看物流");
                Util.startFragment(OrderLogisticsInfoFragment.newInstance(ordersId));
            } else if (tag.equals(TEXT_MEMBER_REFUND_ALL)) {
                SLog.info("全部退款");
                Util.startFragment(GoodsRefundFragment.newInstance(EasyJSONObject.generate(
                        "action", Constant.ACTION_REFUND_ALL,
                        "ordersId", ordersId
                ).toString()));
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            if (handleOrderButtonClick((String) tag)) {
                return;
            }
        }

        int id = v.getId();
        switch (id) {
            case R.id.btn_dial_store_phone:
                SLog.info("storePhone[%s]", storePhone);
                if (StringUtil.isEmpty(storePhone)) {
                    ToastUtil.error(_mActivity, getString(R.string.text_seller_phone_not_set));
                    return;
                }
                Util.dialPhone(_mActivity, storePhone);
                break;
            case R.id.btn_advisory_service:
                GoodsInfo goodsInfo = new GoodsInfo();
                goodsInfo.commonId = orderDetailGoodsItemList.get(0).commonId;
                goodsInfo.goodsName = orderDetailGoodsItemList.get(0).goodsName;
                goodsInfo.imageSrc = orderDetailGoodsItemList.get(0).imageSrc;
                goodsInfo.imageSrc = orderDetailGoodsItemList.get(0).imageSrc;
                goodsInfo.showSendBtn = true;
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreCustomerServicePopup(_mActivity, storeId,goodsInfo)).show();
                break;
            case R.id.btn_goto_store:
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            case R.id.tv_fragment_title:
                if (Config.DEVELOPER_MODE) {
                    hsvBottomToolbar.fullScroll(View.FOCUS_RIGHT);
                }
                break;
            default:
                break;
        }
    }

    private void orderOperation(OrderOperation operationType) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", ordersId);

            String path;
            if (operationType == OrderOperation.ORDER_OPERATION_TYPE_CANCEL) {
                path = Api.PATH_CANCEL_ORDER;
            } else if (operationType == OrderOperation.ORDER_OPERATION_TYPE_DELETE) {
                path = Api.PATH_DELETE_ORDER;
            } else if (operationType == OrderOperation.ORDER_OPERATION_TYPE_BUY_AGAIN) {
                path = Api.PATH_BUY_AGAIN;
                params.set("clientType", Constant.CLIENT_TYPE_ANDROID);
            } else {
                return;
            }
            SLog.info("params[%s]", params);
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

                        if (operationType == OrderOperation.ORDER_OPERATION_TYPE_CANCEL) {
                            ToastUtil.success(_mActivity, "取消訂單成功");
                        } else if (operationType == OrderOperation.ORDER_OPERATION_TYPE_DELETE) {
                            ToastUtil.success(_mActivity, "刪除訂單成功");
                        } else if (operationType == OrderOperation.ORDER_OPERATION_TYPE_BUY_AGAIN) {
                            ToastUtil.success(_mActivity, "訂單已添加到購物袋");
                            start(CartFragment.newInstance(true));
                        }

                        hideSoftInputPop();
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 確認收貨
     */
    private void confirmReceive() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "ordersId", ordersId);

        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_CONFIRM_RECEIVE, params, new UICallback() {
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

                    ToastUtil.success(_mActivity, "確認收貨成功");
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadOrderDetail() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "ordersId", ordersId);

        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_ORDER_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
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

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    isGroup = responseObj.getInt("datas.isGroup");
                    if (isGroup == Constant.TRUE_INT) {
                        btnBuyAgain.setText("邀請好友");

                        if (responseObj.exists("datas.shareVo")) {
                            EasyJSONObject shareVo = responseObj.getSafeObject("datas.shareVo");

                            /*
                                "title": "1分錢也是愛",
                                "image": "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/8d/7e/8d7e2879963eb6bfd7f912e52af304d4.png",
                                "content": "1分錢也是愛的測試商品",
                                "shareUrl": "http://localhost:8080/web/goods/3508",
                                "commonId": 3508,
                                "goodsId": 4742,
                                "price": 100.00
                             */

                            groupShareTitle = shareVo.getSafeString("title");
                            groupShareImage = StringUtil.normalizeImageUrl(shareVo.getSafeString("image"));
                            groupShareContent = shareVo.getSafeString("content");
                            groupShareUrl = shareVo.getSafeString("shareUrl");
                            groupShareCommonId = shareVo.getInt("commonId");
                            groupShareGoodsId = shareVo.getInt("goodsId");
                            groupSharePrice = shareVo.getDouble("price");
                        }
                    }

                    EasyJSONObject ordersVo = responseObj.getSafeObject("datas.ordersVo");
                    String receiverName = ordersVo.getSafeString("receiverName");
                    String mobile = ordersVo.getSafeString("receiverPhone");
                    String address = ordersVo.getSafeString("receiverAreaInfo") + ordersVo.getSafeString("receiverAddress");
                    tariffBuy =ordersVo.getInt("tariffBuy");
                    double tariffAmount =0;
                    if (tariffBuy == Constant.TRUE_INT) {
                        tariffAmount = ordersVo.getDouble("taxAmount");
                        rlTaxContainer.setVisibility(View.VISIBLE);
                    }

                    // 读取paymentTypeCode
                    if (ordersVo.exists("paymentTypeCode")) {
                        paymentTypeCode = ordersVo.getSafeString("paymentTypeCode");
                    }
                    // 如果读不到，尝试读取paymentCode
                    if (StringUtil.isEmpty(paymentTypeCode) && ordersVo.exists("paymentCode")) {
                        paymentTypeCode = ordersVo.getSafeString("paymentCode");
                    }
                    SLog.info("paymentTypeCode[%s]", paymentTypeCode);
                    adapter.setPaymentTypeCode(paymentTypeCode);

                    storeId = ordersVo.getInt("storeId");
                    storeName = ordersVo.getSafeString("storeName");
                    String ordersStateName = ordersVo.getSafeString("ordersStateName");
                    ordersState = ordersVo.getInt("ordersState");
                    showRefundWaiting = ordersVo.getInt("showRefundWaiting");

                    switch (ordersState) {
                        case OrderState.TO_BE_PAY:
                            iconOrderStatus.setImageResource(R.drawable.icon_wait_pay);
                            Jarbon createTime = Jarbon.parse(ordersVo.getSafeString("createTime"));
                            int diff =createTime.diffInMinutes(new Jarbon());

                            tvOrderStatusDesc.setText(String.format("您的訂單還未付款，%d小時%d分鐘後將取消訂單請及時完成支付~", 1-diff / 60, 60-diff % 60));
                            llShipInfo.setVisibility(View.GONE);
                            llShipType.setVisibility(View.GONE);
                            btnBuyAgain.setVisibility(View.GONE);
                            btnPayOrder.setVisibility(View.VISIBLE);
                            break;
                        case OrderState.TO_BE_SEND:
                            iconOrderStatus.setImageResource(R.drawable.icon_wait_send);
                            tvOrderStatusDesc.setText("商家正在處理您的商品，請耐心等待商家發貨。");
                            llShipInfo.setVisibility(View.GONE);
                            llShipType.setVisibility(View.GONE);
                            break;
                        case OrderState.TO_BE_RECEIVE:
                            tvOrderStatusDesc.setText("您購買的商品已經發貨，請耐心等待快遞人員的派送。");
                            iconOrderStatus.setImageResource(R.drawable.icon_wait_receive);
                            break;
                        case OrderState.TO_BE_COMMENT:
                            tvOrderStatusDesc.setText("您購買的商品已經完成派送，祝您使用愉快。");
                            iconOrderStatus.setImageResource(R.drawable.icon_order_ok);
                            break;
                        default:
                            break;
                    }
                    double freightAmount = ordersVo.getDouble("freightAmount");
                    double itemAmount= ordersVo.getDouble("itemAmount");
                    double storeDiscountAmount=ordersVo.getDouble("storeDiscountAmount");
                    double couponAmount=ordersVo.getDouble("couponAmount");
                    ordersAmount = ordersVo.getDouble("ordersAmount");
                    String shipTime = ordersVo.getSafeString("shipTime");
                    String finishTime=ordersVo.getSafeString("finishTime");
                    if (StringUtil.isEmpty(finishTime)) {
                        llOrderSendTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvSendTime.setText(finishTime);
                    }
                    String paymentName=ordersVo.getSafeString("paymentName");
                    tvPayType.setText(paymentName);
                    String shipName=ordersVo.getSafeString("shipName");
                    tvShipType.setText(shipName);
                    String ordersExplain=ordersVo.getSafeString("ordersExplain");

                    EasyJSONArray explainImages=ordersVo.getSafeArray("explainImages");
                    if (!StringUtil.isEmpty(ordersExplain)) {
                        llStoreSendInfoContainer.setVisibility(View.VISIBLE);
                        tvStoreSendInfo.setText(ordersExplain);
                    }

                    if (explainImages != null && explainImages.length() > 0) {
                        sglImageContainer.setVisibility(View.VISIBLE);
                        for (int i = 0; i < sglImageContainer.getColumnCount() && i < explainImages.length(); i++) {
                            EasyJSONObject object=explainImages.getObject(i);
                            ImageView imageView = new ImageView(_mActivity);
                            sglImageContainer.addImageView(imageView,null,object.getSafeString("imageUrl"));
                        }
                    }
                    String receiverMessage=ordersVo.getSafeString("receiverMessage");
                    if (!StringUtil.isEmpty(receiverMessage)) {
                        llLeaveMessageContainer.setVisibility(View.VISIBLE);
                        tvLeaveMessage.setText(receiverMessage);
                    }
                    // 單據信息
                    String invoiceTitle = ordersVo.getSafeString("invoiceTitle");
                    String invoiceContent = ordersVo.getSafeString("invoiceContent");
                    String invoiceCode = ordersVo.getSafeString("invoiceCode");
                    if (!StringUtil.isEmpty(invoiceTitle) && !StringUtil.isEmpty(invoiceContent) && !StringUtil.isEmpty(invoiceCode)) {
                        Receipt receipt = new Receipt();
                        receipt.header = invoiceTitle;
                        receipt.content = invoiceContent;
                        receipt.taxPayerId = invoiceCode;
                    }

                    // 自提碼
                    if (responseObj.exists("datas.ordersVo.takeCode")) {
                        int takeCode = responseObj.getInt("datas.ordersVo.takeCode");
                        if (takeCode > 0) {
                            rlTakeCodeContainer.setVisibility(View.VISIBLE);
                            if (responseObj.exists("datas.storeVo")) {
                                EasyJSONObject jsonObject = responseObj.getObject("datas.storeVo");
                                llTakeGoodsContainer.setVisibility(View.VISIBLE);
                                    StoreVo storeVo = StoreVo.parse(jsonObject);
                                String weekDayStartTime = jsonObject.getSafeString("weekDayStartTime");
                                String weekDayEndTime = jsonObject.getSafeString("weekDayEndTime");
                                String restDayStartTime = jsonObject.getSafeString("restDayStartTime");
                                String restDayEndTime = jsonObject.getSafeString("restDayEndTime");
                                String chainTrafficLine = jsonObject.getSafeString("chainTrafficLine");
                                tvStorePhone.setText(storeVo.getChainPhone());
                                tvTakeAddr.setText(storeVo.getChainAreaInfo()+storeVo.getChainAddress());
                                tvStoreTransport.setText(chainTrafficLine);
                                String weekDayRange = storeVo.getWeekDayStart() + " 至 " + storeVo.getWeekDayEnd();
                                String weekDayRangeTime = weekDayStartTime + "-" + weekDayEndTime;
                                String restDayRange = storeVo.getRestDayStart() + " 至 " + storeVo.getRestDayEnd();
                                String restDayRangeTime = restDayStartTime + "-" + restDayEndTime;
                                if (StringUtil.isEmpty(storeVo.getWeekDayStart())) {
                                    tvBusinessTimeWorkingDay.setVisibility(View.GONE);
                                    tvBusinessTimeWorkingDay.setText(weekDayRangeTime);
                                } else {
                                    tvBusinessTimeWorkingDay.setText(weekDayRange + "   " + weekDayRangeTime);
                                }
//                                ToastUtil.success(_mActivity,restDayRange.concat(restDayRangeTime));
                                if (StringUtil.isEmpty(storeVo.getRestDayStart())) {
                                    tvBusinessTimeWeekend.setVisibility(View.GONE);
                                    tvBusinessTimeWeekend.setText(restDayRangeTime);

                                } else {
                                    tvBusinessTimeWeekend.setText(restDayRange + "   " + restDayRangeTime);
                                }
                            }
                            tvTakeCode.setText(String.valueOf(takeCode));
                        }
                    }
                    long ordersSn = ordersVo.getLong("ordersSn");
                    paySnStr = ordersVo.getSafeString("paySnStr");
                    payId = ordersVo.getInt("payId");
                    String createTime = ordersVo.getSafeString("createTime");
                    String paymentTime = ordersVo.getSafeString("paymentTime");
                    String sendTime = ordersVo.getSafeString("sendTime");
                    if (responseObj.exists("datas.sellerMobile")) {
                        storePhone = responseObj.getSafeString("datas.sellerMobile");
                    }
                    tvReceiverName.setText(receiverName);
                    tvMobile.setText(mobile);
                    tvAddress.setText(address);
                    tvStoreName.setText(storeName);
                    tvOrderStatus.setText(ordersStateName);
                    tvOrdersStateName.setText(ordersStateName);
                    //运费
                    tvFreightAmount.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1,2));

                    //产品总额
                    tvGoodsAmount.setText(StringUtil.formatPrice(_mActivity, itemAmount, 1,2));
                    //商店优惠
                    tvStoreWelfare.setText("-"+StringUtil.formatPrice(_mActivity, storeDiscountAmount, 1,2));
                    //平台优惠
                    tvPlatformWelfare.setText("-"+StringUtil.formatPrice(_mActivity, couponAmount, 1,2));
                    //应付金额
                    tvOrdersAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1,2));
                    tvShipDate.setText(shipTime);
                    tvOrdersSn.setText(String.valueOf(ordersSn));
                    if (StringUtil.isEmpty(createTime)) {
                        //下单时间
                        llOrderCreateTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvCreateTime.setText(createTime);
                    }

                    if (StringUtil.isEmpty(paymentTime)) {
                        //支付时间
                        llOrderPaymentTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvPaymentTime.setText(paymentTime);
                    }

                    if (StringUtil.isEmpty(paySnStr)) {
                        //支付单号
                        llOrderPaySnContainer.setVisibility(View.GONE);
                    } else {
                        tvPaySn.setText(paySnStr);
                    }

                    if (StringUtil.isEmpty(sendTime)) {
                        //发货时间
                        rlSendContainer.setVisibility(View.GONE);
                    } else {
                        tvShipTime.setText(sendTime);
                    }

                    List<String> showButtonNameList = new ArrayList<>();

                    for (String buttonName : buttonNameList) {
                        if (TEXT_MEMBER_PAY.equals(buttonName)) { // 訂單詳情里暫不顯示支付按鈕
                            continue;
                        }
                        if (ordersVo.getInt(buttonName) == 1) {
                            SLog.info("__buttonName[%s]", buttonName);
                            showButtonNameList.add(buttonName);
                        }
                    }

                    llOrderButtonContainer.removeAllViews();
                    int size = showButtonNameList.size();
                    SLog.info("按鈕個數[%d]", size);
                    for (int i = 0; i < size; ++i) {
                        String buttonName = showButtonNameList.get(i);
                        TextView button = new TextView(_mActivity);
                        button.setTag(buttonName);

                        String buttonNameChinese = orderButtonNameMap.get(buttonName);
                        button.setGravity(Gravity.CENTER);
                        button.setTextSize(14);
                        button.setText(buttonNameChinese);

                        // 最后一個與其它樣式不同
                        if (i == size - 1) {
                            button.setTextColor(Color.parseColor("#FFFFFF"));
                            button.setBackgroundResource(R.drawable.smaller_red_button);
                        } else {
                            button.setTextColor(getResources().getColor(R.color.tw_black, null));
                            button.setBackgroundResource(R.drawable.smaller_outline_button);
                        }

                        button.setOnClickListener(OrderDetailFragment.this);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dip2px(_mActivity, 105),
                                Util.dip2px(_mActivity, 39));
                        llOrderButtonContainer.addView(button, layoutParams);
                    }
                    hsvBottomToolbar.postDelayed(() -> {
                        if (hsvBottomToolbar != null) {
                            hsvBottomToolbar.fullScroll(View.FOCUS_RIGHT);
                        }

                    }, 50);

                    View rlGroupStatusContainer = contentView.findViewById(R.id.rl_group_status_container);


                    goId = ordersVo.getInt("goId");
                    if (goId > 0) {
                        rlGroupStatusContainer.setVisibility(View.VISIBLE);

                        LinearLayout llGroupMemberList = contentView.findViewById(R.id.ll_group_member_list);
                        TextView tvGroupStatus = contentView.findViewById(R.id.tv_group_status);


                        goState = ordersVo.getInt("goState");
                        /*
                         0拼團中 1拼團成功 2拼團失敗
                         */
                        if (Config.DEVELOPER_MODE) {
                            // goState = 0;
                        }
                        if (goState == GO_STATE_ONGOING) {
                            tvGroupStatus.setText("拼團中");
                            tvGroupCountDown.setVisibility(View.VISIBLE);
                            groupEndTime = ordersVo.getLong("groupEndTime");
                            if (Config.DEVELOPER_MODE) {
                                // groupEndTime = 1593920850000L;
                            }
                            tvOrderStatus.setText("拼團中");
                            tvOrderStatusDesc.setText("您的訂單正在努力成團中，快來邀請好友一起參與吧");
                            startCountDown(groupEndTime);
                        } else if (goState == GO_STATE_SUCCESS) {
                            tvGroupStatus.setText("拼團成功");
                        } else if (goState == GO_STATE_FAILURE) {
                            tvOrderStatus.setText("拼團失敗");
                            tvOrderStatusDesc.setText("該訂單已自動進行退款，敬請關注其他活動信息");
                            tvGroupStatus.setText("拼團失敗");
                            btnBuyAgain.setVisibility(View.VISIBLE);
                            btnBuyAgain.setText("再次購買");
                        }

                        List<String> groupMemberAvatarList = new ArrayList<>(); // 團購成員列表
                        int groupRemainNum = ordersVo.getInt("groupRemainNum");  // 還差多少個人成團
                        EasyJSONArray groupLogOpenVoList = ordersVo.getSafeArray("groupLogOpenVoList");
                        for (Object object : groupLogOpenVoList) {
                            EasyJSONObject groupLogOpenVo = (EasyJSONObject) object;
                            String memberAvatar = groupLogOpenVo.getSafeString("memberAvatar");
                            groupMemberAvatarList.add(memberAvatar);
                        }

                        for (int i = 0; i < groupRemainNum; i++) {
                            groupMemberAvatarList.add(""); // 待加入的團員頭像地址用空字符串表示
                        }

                        llGroupMemberList.removeAllViews();
                        for (String memberAvatarUrl : groupMemberAvatarList) {
                            /*
                            android:layout_width="32dp"
                            android:layout_height="32dp"
                            android:layout_marginRight="8dp"
                            android:src="@drawable/cs_avatar_160"
                            app:civ_border_color="@android:color/white"
                            app:civ_border_width="1dp">
                             */
                            CircleImageView imgAvatar = new CircleImageView(_mActivity);
                            imgAvatar.setBorderColor(Color.WHITE);
                            imgAvatar.setBorderWidth(Util.dip2px(_mActivity, 1));

                            if (StringUtil.isEmpty(memberAvatarUrl)) { // 待加入的團員頭像
                                Glide.with(_mActivity).load(R.drawable.icon_question_mark).centerCrop().into(imgAvatar);
                            } else {
                                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(memberAvatarUrl)).centerCrop().into(imgAvatar);
                            }

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    Util.dip2px(_mActivity, 32),
                                    Util.dip2px(_mActivity, 32)
                            );
                            layoutParams.rightMargin = Util.dip2px(_mActivity, 8);

                            llGroupMemberList.addView(imgAvatar, layoutParams);
                        }
                    } else {
                        rlGroupStatusContainer.setVisibility(View.GONE);
                    }


                    int showMemberComplain = ordersVo.getInt("showMemberComplain");
                    EasyJSONArray ordersGoodsVoList = ordersVo.getSafeArray("ordersGoodsVoList");
                    orderDetailGoodsItemList.clear();

                    int showMemberRefundAll = ordersVo.getInt("showMemberRefundAll");
                    for (Object object : ordersGoodsVoList) {
                        EasyJSONObject goodsVo = (EasyJSONObject) object;

                        orderDetailGoodsItemList.add(new OrderDetailGoodsItem(
                                goodsVo.getInt("commonId"),
                                goodsVo.getInt("goodsId"),
                                goodsVo.getInt("ordersId"),
                                ordersState,
                                showRefundWaiting,
                                showMemberRefundAll,
                                goodsVo.getInt("ordersGoodsId"),
                                goodsVo.getSafeString("imageSrc"),
                                goodsVo.getSafeString("goodsName"),
                                goodsVo.getDouble("goodsPrice"),
                                goodsVo.getInt("buyNum"),
                                goodsVo.getSafeString("goodsFullSpecs"),
                                goodsVo.getInt("refundType"),
                                goodsVo.getInt("showRefund"),
                                showMemberComplain,
                                goodsVo.getInt("complainId")
//                                (float )goodsVo.getDouble("tariffAmount")
                            )
                        );
                    }

                    tvTaxAmount.setText(StringUtil.formatPrice(_mActivity,tariffAmount,1,2));
                    adapter.setData(orderDetailGoodsItemList);
                    needReloadData = false;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 啟動倒數計算
     * @param endTime 結束時間戳
     */
    private void startCountDown(long endTime) {
        long remainTime = endTime - System.currentTimeMillis(); // 離結束還剩下多少毫秒
        SLog.info("endTime[%s], remainTime[%s]", endTime, remainTime);

        stopCountDown();  // 先停止上一次倒數

        if (remainTime <= 0) {
            tvGroupCountDown.setText("結束");
            return;
        }

        countDownTimer = new CountDownTimer(remainTime, 100) {
            public void onTick(long millisUntilFinished) {
                // SLog.info("threadId[%s]", Thread.currentThread().getId());

                long now = System.currentTimeMillis();
                TimeInfo timeInfoGroup = Time.groupTimeDiff(now, endTime);

                if (timeInfoGroup == null) {
                    tvGroupCountDown.setText("結束");
                } else {
                    tvGroupCountDown.setText(String.format("%d:%02d:%02d.%d",
                            timeInfoGroup.hour, timeInfoGroup.minute, timeInfoGroup.second, timeInfoGroup.milliSecond / 100));
                }
            }
            public void onFinish() {
                tvGroupCountDown.setText("結束");
            }
        }.start();
    }

    private void stopCountDown() {
        SLog.info("stopCountDown()");

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = null;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (needReloadData) {
            hsvBottomToolbar.postDelayed(() -> loadOrderDetail(), 1500);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}


