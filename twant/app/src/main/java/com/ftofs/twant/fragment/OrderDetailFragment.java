package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.OrderDetailGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.PayPopup;
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

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單詳情
 * @author zwm
 */
public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int ordersId;
    int storeId;
    String paySnStr;
    int payId;
    float ordersAmount = -1;

    OrderDetailGoodsAdapter adapter;
    List<OrderDetailGoodsItem> orderDetailGoodsItemList = new ArrayList<>();
    List<CustomerServiceStaff> staffList = new ArrayList<>();

    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;
    TextView tvStoreName;
    TextView tvOrdersStateName;
    TextView tvFreightAmount;
    TextView tvOrdersAmount;
    TextView tvShipTime;
    TextView tvOrdersSn;

    LinearLayout llOrderCreateTimeContainer;
    TextView tvCreateTime;
    LinearLayout llOrderPaymentTimeContainer;
    TextView tvPaymentTime;
    LinearLayout llOrderPaySnContainer;
    TextView tvPaySn;
    LinearLayout llOrderSendTimeContainer;
    TextView tvSendTime;

    LinearLayout llOrderButtonContainer;

    String storePhone;
    boolean needReloadData;

    public static final int ORDER_OPERATION_TYPE_CANCEL = 1;
    public static final int ORDER_OPERATION_TYPE_DELETE = 2;
    public static final int ORDER_OPERATION_TYPE_BUY_AGAIN = 3;

    public static final String TEXT_MEMBER_BUY_AGAIN = "showMemberBuyAgain";
    public static final String TEXT_MEMBER_RECEIVE = "showMemberReceive";
    public static final String TEXT_REFUND_WAITING = "showRefundWaiting";
    public static final String TEXT_MEMBER_REFUND_ALL = "showMemberRefundAll";
    public static final String TEXT_EVALUATION = "showEvaluation";
    public static final String TEXT_SHIP_SEARCH = "showShipSearch";
    public static final String TEXT_MEMBER_CANCEL = "showMemberCancel";
    public static final String TEXT_MEMBER_DELETE = "showMemberDelete";
    public static final String TEXT_MEMBER_PAY = "showMemberPay";

    String[] buttonNameList = new String[] {
            TEXT_MEMBER_BUY_AGAIN, TEXT_MEMBER_RECEIVE, TEXT_REFUND_WAITING, TEXT_MEMBER_REFUND_ALL,
            TEXT_EVALUATION, TEXT_SHIP_SEARCH, TEXT_MEMBER_CANCEL, TEXT_MEMBER_DELETE, TEXT_MEMBER_PAY};

    Map<String, String> orderButtonNameMap = new HashMap<>();

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        ordersId = args.getInt("ordersId");
        SLog.info("ordersId[%d]", ordersId);

        orderButtonNameMap.put(buttonNameList[0], getString(R.string.text_buy_again));
        orderButtonNameMap.put(buttonNameList[1], getString(R.string.text_have_received));
        orderButtonNameMap.put(buttonNameList[2], getString(R.string.text_refund_in_progress));
        orderButtonNameMap.put(buttonNameList[3], getString(R.string.text_refund_all));
        orderButtonNameMap.put(buttonNameList[4], getString(R.string.text_order_comment));
        orderButtonNameMap.put(buttonNameList[5], getString(R.string.text_view_logistics));
        orderButtonNameMap.put(buttonNameList[6], getString(R.string.text_cancel_order));
        orderButtonNameMap.put(buttonNameList[7], getString(R.string.text_delete_order));
        orderButtonNameMap.put(buttonNameList[8], getString(R.string.text_pay_order));

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvAddress = view.findViewById(R.id.tv_address);
        tvStoreName = view.findViewById(R.id.tv_store_name);
        tvOrdersStateName = view.findViewById(R.id.tv_orders_state_name);
        tvFreightAmount = view.findViewById(R.id.tv_freight_amount);
        tvOrdersAmount = view.findViewById(R.id.tv_orders_amount);
        tvShipTime = view.findViewById(R.id.tv_ship_time);
        tvOrdersSn = view.findViewById(R.id.tv_orders_sn);

        llOrderCreateTimeContainer = view.findViewById(R.id.ll_order_create_time_container);
        tvCreateTime = view.findViewById(R.id.tv_create_time);
        llOrderPaymentTimeContainer = view.findViewById(R.id.ll_order_payment_time_container);
        tvPaymentTime = view.findViewById(R.id.tv_payment_time);
        llOrderPaySnContainer = view.findViewById(R.id.ll_order_pay_sn_container);
        tvPaySn = view.findViewById(R.id.tv_pay_sn);
        llOrderSendTimeContainer = view.findViewById(R.id.ll_order_send_time_container);
        tvSendTime = view.findViewById(R.id.tv_send_time);

        llOrderButtonContainer = view.findViewById(R.id.ll_order_button_container);

        Util.setOnClickListener(view, R.id.btn_goto_store, this);
        Util.setOnClickListener(view, R.id.btn_dial_store_phone, this);
        Util.setOnClickListener(view, R.id.btn_advisory_service, this);

        RecyclerView rvOrderDetailGoodsList = view.findViewById(R.id.rv_order_detail_goods_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderDetailGoodsList.setLayoutManager(layoutManager);
        adapter = new OrderDetailGoodsAdapter(_mActivity, R.layout.order_detail_goods_item, orderDetailGoodsItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
        rvOrderDetailGoodsList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadOrderDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
                int operationType = ORDER_OPERATION_TYPE_CANCEL;
                if (tag.equals(TEXT_MEMBER_DELETE)) {
                    operationType = ORDER_OPERATION_TYPE_DELETE;
                }
                final int finalOperationType = operationType;

                String confirmText = "確定要取消訂單嗎?";
                if (operationType == ORDER_OPERATION_TYPE_DELETE) {
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
                orderOperation(ORDER_OPERATION_TYPE_BUY_AGAIN);
            } else if (tag.equals(TEXT_EVALUATION)) {
                Util.startFragment(GoodsEvaluationFragment.newInstance());
            } else if (tag.equals(TEXT_MEMBER_PAY)) {
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new PayPopup(_mActivity, (MainActivity) _mActivity, payId))
                        .show();
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
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_dial_store_phone:
                SLog.info("storePhone[%s]", storePhone);
                if (StringUtil.isEmpty(storePhone)) {
                    ToastUtil.error(_mActivity, getString(R.string.text_seller_phone_not_set));
                    return;
                }
                Util.dialPhone(_mActivity, storePhone);
                break;
            case R.id.btn_advisory_service:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreCustomerServicePopup(_mActivity, storeId))
                        .show();
                break;
            case R.id.btn_goto_store:
                Util.startFragment(ShopMainFragment.newInstance(storeId));
                break;
            default:
                break;
        }
    }

    private void orderOperation(final int operationType) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", ordersId);

            String path;
            if (operationType == ORDER_OPERATION_TYPE_CANCEL) {
                path = Api.PATH_CANCEL_ORDER;
            } else if (operationType == ORDER_OPERATION_TYPE_DELETE) {
                path = Api.PATH_DELETE_ORDER;
            } else if (operationType == ORDER_OPERATION_TYPE_BUY_AGAIN) {
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

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        if (operationType == ORDER_OPERATION_TYPE_CANCEL) {
                            ToastUtil.success(_mActivity, "取消訂單成功");
                        } else if(operationType == ORDER_OPERATION_TYPE_DELETE) {
                            ToastUtil.success(_mActivity, "刪除訂單成功");
                        } else if (operationType == ORDER_OPERATION_TYPE_BUY_AGAIN) {
                            ToastUtil.success(_mActivity, "訂單已添加到購物車");
                        }

                        pop();
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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, "確認收貨成功");
                    pop();
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

        final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                .asLoading(getString(R.string.text_loading))
                .show();
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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONArray serviceStaffList = responseObj.getArray("datas.serviceStaffList");
                    for (Object object : serviceStaffList) {
                        EasyJSONObject serviceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        Util.packStaffInfo(staff, serviceStaff);

                        staffList.add(staff);
                    }

                    EasyJSONObject ordersVo = responseObj.getObject("datas.ordersVo");
                    String receiverName = ordersVo.getString("receiverName");
                    String mobile = ordersVo.getString("receiverPhone");
                    String address = ordersVo.getString("receiverAreaInfo") + ordersVo.getString("receiverAddress");

                    storeId = ordersVo.getInt("storeId");
                    String storeName = ordersVo.getString("storeName");
                    String ordersStateName = ordersVo.getString("ordersStateName");
                    float freightAmount = (float) ordersVo.getDouble("freightAmount");
                    ordersAmount = (float) ordersVo.getDouble("ordersAmount");
                    String shipTime = ordersVo.getString("shipTime");

                    // 單據信息
                    String invoiceTitle = ordersVo.getString("invoiceTitle");
                    String invoiceContent = ordersVo.getString("invoiceContent");
                    String invoiceCode = ordersVo.getString("invoiceCode");
                    if (!StringUtil.isEmpty(invoiceTitle) && !StringUtil.isEmpty(invoiceContent) && !StringUtil.isEmpty(invoiceCode)) {
                        Receipt receipt = new Receipt();
                        receipt.header = invoiceTitle;
                        receipt.content = invoiceContent;
                        receipt.taxPayerId = invoiceCode;
                    }

                    long ordersSn = ordersVo.getLong("ordersSn");
                    paySnStr = ordersVo.getString("paySnStr");
                    payId = ordersVo.getInt("payId");
                    String createTime = ordersVo.getString("createTime");
                    String paymentTime = ordersVo.getString("paymentTime");
                    String sendTime = ordersVo.getString("sendTime");

                    if (responseObj.exists("datas.sellerMobile")) {
                        storePhone = responseObj.getString("datas.sellerMobile");
                    }

                    tvReceiverName.setText(getString(R.string.text_receiver) + ":  " + receiverName);
                    tvMobile.setText(mobile);
                    tvAddress.setText(address);
                    tvStoreName.setText(storeName);
                    tvOrdersStateName.setText(ordersStateName);
                    tvFreightAmount.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1));
                    tvOrdersAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1));
                    tvShipTime.setText(shipTime);
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
                        llOrderSendTimeContainer.setVisibility(View.GONE);
                    } else {
                        tvSendTime.setText(sendTime);
                    }

                    List<String> showButtonNameList = new ArrayList<>();

                    for (String buttonName : buttonNameList) {
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


                    int showMemberComplain = ordersVo.getInt("showMemberComplain");
                    EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
                    for (Object object : ordersGoodsVoList) {
                        EasyJSONObject goodsVo = (EasyJSONObject) object;

                        orderDetailGoodsItemList.add(new OrderDetailGoodsItem(
                                goodsVo.getInt("commonId"),
                                goodsVo.getInt("goodsId"),
                                goodsVo.getInt("ordersId"),
                                goodsVo.getInt("ordersGoodsId"),
                                goodsVo.getString("imageSrc"),
                                goodsVo.getString("goodsName"),
                                (float) goodsVo.getDouble("goodsPrice"),
                                goodsVo.getInt("buyNum"),
                                goodsVo.getString("goodsFullSpecs"),
                                goodsVo.getInt("refundType"),
                                goodsVo.getInt("showRefund"),
                                showMemberComplain,
                                goodsVo.getInt("complainId")));
                    }
                    adapter.setNewData(orderDetailGoodsItemList);
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (needReloadData) {
            loadOrderDetail();
            needReloadData = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}


