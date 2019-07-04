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

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.adapter.OrderDetailGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 訂單詳情
 * @author zwm
 */
public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int ordersId;

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
    LinearLayout llOrderSendTimeContainer;
    TextView tvSendTime;

    LinearLayout llOrderButtonContainer;

    String storePhone;

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

    String[] buttonNameList = new String[] {
            TEXT_MEMBER_BUY_AGAIN, TEXT_MEMBER_RECEIVE, TEXT_REFUND_WAITING, TEXT_MEMBER_REFUND_ALL,
            TEXT_EVALUATION, TEXT_SHIP_SEARCH, TEXT_MEMBER_CANCEL, TEXT_MEMBER_DELETE};

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
        llOrderSendTimeContainer = view.findViewById(R.id.ll_order_send_time_container);
        tvSendTime = view.findViewById(R.id.tv_send_time);

        llOrderButtonContainer = view.findViewById(R.id.ll_order_button_container);

        Util.setOnClickListener(view, R.id.btn_dial_store_phone, this);
        Util.setOnClickListener(view, R.id.btn_advisory_service, this);

        RecyclerView rvOrderDetailGoodsList = view.findViewById(R.id.rv_order_detail_goods_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderDetailGoodsList.setLayoutManager(layoutManager);
        adapter = new OrderDetailGoodsAdapter(_mActivity, R.layout.order_detail_goods_item, orderDetailGoodsItemList);
        rvOrderDetailGoodsList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadOrderDetail();
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
                new XPopup.Builder(getContext())
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
                new XPopup.Builder(getContext())
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
                Util.dialPhone(_mActivity, storePhone);
                break;
            case R.id.btn_advisory_service:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreCustomerServicePopup(_mActivity, staffList))
                        .show();
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
                            ToastUtil.show(_mActivity, "取消訂單成功");
                        } else if(operationType == ORDER_OPERATION_TYPE_DELETE) {
                            ToastUtil.show(_mActivity, "刪除訂單成功");
                        } else if (operationType == ORDER_OPERATION_TYPE_BUY_AGAIN) {
                            ToastUtil.show(_mActivity, "訂單已添加到購物車");
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

                    ToastUtil.show(_mActivity, "確認收貨成功");
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

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_ORDER_DETAIL, params, new UICallback() {
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

                    EasyJSONArray serviceStaffList = responseObj.getArray("datas.serviceStaffList");
                    for (Object object : serviceStaffList) {
                        EasyJSONObject serviceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        staff.staffId = serviceStaff.getInt("staffId");
                        staff.staffName = serviceStaff.getString("staffName");
                        staff.avatar = serviceStaff.getString("avatar");

                        staffList.add(staff);
                    }

                    EasyJSONObject ordersVo = responseObj.getObject("datas.ordersVo");
                    String receiverName = ordersVo.getString("receiverName");
                    String mobile = ordersVo.getString("receiverPhone");
                    String address = ordersVo.getString("receiverAreaInfo") + ordersVo.getString("receiverAddress");

                    String storeName = ordersVo.getString("storeName");
                    String ordersStateName = ordersVo.getString("ordersStateName");
                    float freightAmount = (float) ordersVo.getDouble("freightAmount");
                    float ordersAmount = (float) ordersVo.getDouble("ordersAmount");
                    String shipTime = ordersVo.getString("shipTime");

                    // 發票信息
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
                    String createTime = ordersVo.getString("createTime");
                    String paymentTime = ordersVo.getString("paymentTime");
                    String sendTime = ordersVo.getString("sendTime");

                    storePhone = ordersVo.getString("storePhone");

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


                    EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
                    for (Object object : ordersGoodsVoList) {
                        EasyJSONObject goodsVo = (EasyJSONObject) object;

                        orderDetailGoodsItemList.add(new OrderDetailGoodsItem(
                                goodsVo.getInt("goodsId"),
                                goodsVo.getString("imageSrc"),
                                goodsVo.getString("goodsName"),
                                (float) goodsVo.getDouble("goodsPrice"),
                                goodsVo.getInt("buyNum"),
                                goodsVo.getString("goodsFullSpecs")));
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
}
