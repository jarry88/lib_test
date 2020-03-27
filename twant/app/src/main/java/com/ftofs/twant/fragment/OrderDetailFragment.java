package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
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

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderDetailGoodsAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.OrderOperation;
import com.ftofs.twant.constant.OrderState;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EvaluationGoodsItem;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單詳情
 *
 * @author zwm
 */
public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int ordersId;
    int storeId;
    String storeName;
    String paySnStr;
    int payId;
    float ordersAmount = -1;

    OrderDetailGoodsAdapter adapter;
    List<OrderDetailGoodsItem> orderDetailGoodsItemList = new ArrayList<>();

    @BindView(R.id.hsv_bottom_toolbar)
    HorizontalScrollView hsvBottomToolbar;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_freight_amount)
    TextView tvFreightAmount;
    @BindView(R.id.tv_orders_amount)
    TextView tvOrdersAmount;
    @BindView(R.id.tv_orders_state_name)
    TextView tvOrdersStateName;
    @BindView(R.id.tv_ship_time)
    TextView tvShipTime;

    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_leave_message)
    TextView tvLeaveMessage;
    @BindView(R.id.ll_leave_message_container)
    LinearLayout llLeaveMessageContainer;

    String storePhone;
    boolean needReloadData;

    int ordersState;
    int showRefundWaiting;


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
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshContainer;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_order_status_desc)
    TextView tvOrderStatusDesc;
    @BindView(R.id.icon_order_status)
    ImageView iconOrderStatus;
    @BindView(R.id.tv_goods_amount)
    TextView tvGoodsAmount;
    @BindView(R.id.ll_ship_type)
    LinearLayout llShipType;

    @OnClick(R.id.btn_back)
    public void back() {
        hideSoftInputPop();
    }

    @BindView(R.id.tv_fragment_title)
    TextView tvFragmentTitle;
    @BindView(R.id.tool_bar)
    RelativeLayout toolBar;

    @OnClick(R.id.btn_buy_again)
    public void buyAgain(View v) {
        SLog.info("添加到購物袋%d",ordersId);
        for (OrderDetailGoodsItem goodsItem:orderDetailGoodsItemList){
            Util.changeCartContent(_mActivity, goodsItem.goodsId, 1, data -> {ToastUtil.success(_mActivity, "添加購物袋成功");
                Util.startFragment(CartFragment.newInstance(true));});
        }
    }

    @BindView(R.id.btn_goto_store)
    LinearLayout btnGotoStore;
    @BindView(R.id.ll_order_detail_goods_list)
    LinearLayout llOrderDetailGoodsList;
    @BindView(R.id.btn_advisory_service)
    LinearLayout btnAdvisoryService;
    @BindView(R.id.btn_dial_store_phone)
    LinearLayout btnDialStorePhone;
    @BindView(R.id.tv_take_code)
    TextView tvTakeCode;
    @BindView(R.id.rl_take_code_container)
    RelativeLayout rlTakeCodeContainer;
    @BindView(R.id.tv_orders_sn)
    TextView tvOrdersSn;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.ll_order_create_time_container)
    LinearLayout llOrderCreateTimeContainer;
    @BindView(R.id.ll_order_send_time_container)
    LinearLayout llOrderSendTimeContainer;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.ll_order_pay_type)
    LinearLayout llOrderPayType;
    @BindView(R.id.tv_pay_sn)
    TextView tvPaySn;
    @BindView(R.id.ll_order_pay_sn_container)
    LinearLayout llOrderPaySnContainer;
    @BindView(R.id.tv_payment_time)
    TextView tvPaymentTime;
    @BindView(R.id.ll_order_payment_time_container)
    LinearLayout llOrderPaymentTimeContainer;
    @BindView(R.id.tv_ship_type)
    TextView tvShipType;
    @BindView(R.id.tv_ship_date)
    TextView tvShipDate;
    @BindView(R.id.ll_ship_info)
    LinearLayout llShipInfo;
    @BindView(R.id.tv_store_send_info)
    TextView tvStoreSendInfo;
    @BindView(R.id.sgl_image_container)
    SquareGridLayout sglImageContainer;
    @BindView(R.id.ll_store_send_info_container)
    LinearLayout llStoreSendInfoContainer;
    @BindView(R.id.tv_store_welfare)
    TextView tvStoreWelfare;
    @BindView(R.id.tv_platform_welfare)
    TextView tvPlatformWelfare;
    @BindView(R.id.ll_order_button_container)
    LinearLayout llOrderButtonContainer;
    @BindView(R.id.rl_send_container)
    RelativeLayout rlSendContainer;

    private Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        SLog.info("OrderDetailFragment::onEBMessage()");
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

                    EasyJSONObject ordersVo = responseObj.getSafeObject("datas.ordersVo");
                    String receiverName = ordersVo.getSafeString("receiverName");
                    String mobile = ordersVo.getSafeString("receiverPhone");
                    String address = ordersVo.getSafeString("receiverAreaInfo") + ordersVo.getSafeString("receiverAddress");

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

                            tvOrderStatusDesc.setText(String.format("您的訂單還未付款，%d小時%d分鐘後將取消訂單請及時完成支付~", 23-diff / 60, 60-diff % 60));
                            llShipInfo.setVisibility(View.GONE);
                            llShipType.setVisibility(View.GONE);
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
                    float freightAmount = (float) ordersVo.getDouble("freightAmount");
                    float itemAmount=(float) ordersVo.getDouble("itemAmount");
                    float storeDiscountAmount=(float)ordersVo.getDouble("storeDiscountAmount");
                    float counponAmount=(float)ordersVo.getDouble("couponAmount");
                    ordersAmount = (float) ordersVo.getDouble("ordersAmount");
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
                    tvReceiverName.setText(getString(R.string.text_receiver) + ":  " + receiverName);
                    tvMobile.setText(mobile);
                    tvAddress.setText(address);
                    tvStoreName.setText(storeName);
                    tvOrderStatus.setText(ordersStateName);
                    tvOrdersStateName.setText(ordersStateName);
                    tvFreightAmount.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1));

                    tvGoodsAmount.setText(StringUtil.formatPrice(_mActivity, itemAmount, 1));
                    tvStoreWelfare.setText("-"+StringUtil.formatPrice(_mActivity, storeDiscountAmount, 1));
                    tvPlatformWelfare.setText("-"+StringUtil.formatPrice(_mActivity, counponAmount, 1));
                    tvOrdersAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1));
                    tvShipDate.setText(shipTime);
                    tvOrdersSn.setText(String.valueOf(ordersSn));
                    if (StringUtil.isEmpty(createTime)) {
                        llOrderCreateTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvCreateTime.setText(createTime);
                    }

                    if (StringUtil.isEmpty(paymentTime)) {
                        llOrderPaymentTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvPaymentTime.setText(paymentTime);
                    }

                    if (StringUtil.isEmpty(paySnStr)) {
                        llOrderPaySnContainer.setVisibility(View.GONE);
                    } else {
                        tvPaySn.setText(paySnStr);
                    }

                    if (StringUtil.isEmpty(sendTime)) {
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


                    int showMemberComplain = ordersVo.getInt("showMemberComplain");
                    EasyJSONArray ordersGoodsVoList = ordersVo.getSafeArray("ordersGoodsVoList");
                    orderDetailGoodsItemList.clear();
                    for (Object object : ordersGoodsVoList) {
                        EasyJSONObject goodsVo = (EasyJSONObject) object;

                        orderDetailGoodsItemList.add(new OrderDetailGoodsItem(
                                goodsVo.getInt("commonId"),
                                goodsVo.getInt("goodsId"),
                                goodsVo.getInt("ordersId"),
                                ordersState,
                                showRefundWaiting,
                                goodsVo.getInt("ordersGoodsId"),
                                goodsVo.getSafeString("imageSrc"),
                                goodsVo.getSafeString("goodsName"),
                                (float) goodsVo.getDouble("goodsPrice"),
                                goodsVo.getInt("buyNum"),
                                goodsVo.getSafeString("goodsFullSpecs"),
                                goodsVo.getInt("refundType"),
                                goodsVo.getInt("showRefund"),
                                showMemberComplain,
                                goodsVo.getInt("complainId")
                                )
                        );
                    }
                    adapter.setData(orderDetailGoodsItemList);
                    needReloadData = false;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
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


