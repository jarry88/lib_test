package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.adapter.PayItemListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.entity.PayItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.PayPopup;
import com.ftofs.twant.widget.TwTabButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

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
 * 訂單頁面
 * @author zwm
 */
public class BillFragment extends BaseFragment implements View.OnClickListener, TwTabButton.TtbOnSelectListener {
    int orderStatus;

    RecyclerView rvOrderList;

    List<PayItem> payItemList = new ArrayList<>();
    List<OrderItem> orderItemList = new ArrayList<>();

    PayItemListAdapter payItemListAdapter;
    OrderListAdapter orderListAdapter;
    TwTabButton[] tvOrderStatusArr = new TwTabButton[5];
    int[] orderStatusIds = new int[] {R.id.btn_bill_all, R.id.btn_bill_to_be_paid, R.id.btn_bill_to_be_shipped,
           R.id.btn_bill_to_be_received, R.id.btn_bill_to_be_commented};

    int twRed;
    int twBlack;
    boolean needRefresh;

    TextView tvAllCount;
    TextView tvToBePaidCount;
    TextView tvToBeShippedCount;
    TextView tvToBeReceivedCount;
    TextView tvToBeCommentedCount;

    public static BillFragment newInstance(int orderStatus) {
        Bundle args = new Bundle();

        args.putInt("orderStatus", orderStatus);
        BillFragment fragment = new BillFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        orderStatus = args.getInt("orderStatus", Constant.ORDER_STATUS_ALL);
        SLog.info("orderStatus[%d]", orderStatus);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);

        tvAllCount = view.findViewById(R.id.tv_all_count);
        tvToBePaidCount = view.findViewById(R.id.tv_to_be_paid_count);
        tvToBeShippedCount = view.findViewById(R.id.tv_to_be_shipped_count);
        tvToBeReceivedCount = view.findViewById(R.id.tv_to_be_received_count);
        tvToBeCommentedCount = view.findViewById(R.id.tv_to_be_commented_count);

        Util.setOnClickListener(view, R.id.tv_fragment_title, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);

        int index = 0;
        for (int id : orderStatusIds) {
            TwTabButton tvOrderStatus = view.findViewById(id);
            tvOrderStatusArr[index] = tvOrderStatus;
            tvOrderStatus.setTtbOnSelectListener(this);
            ++index;
        }

        rvOrderList = view.findViewById(R.id.rv_order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(layoutManager);
        payItemListAdapter = new PayItemListAdapter(_mActivity, R.layout.pay_item, payItemList);
        payItemListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_pay_order) {
                    int payId = payItemList.get(position).payId;
                    SLog.info("payId[%d]", payId);
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new PayPopup(_mActivity, (MainActivity) _mActivity, payId))
                            .show();
                }
            }
        });
        // rvOrderList.setAdapter(payItemListAdapter);

        orderListAdapter = new OrderListAdapter(_mActivity, R.layout.order_item, orderItemList);
        orderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        // rvOrderList.setAdapter(orderListAdapter);

        tvOrderStatusArr[orderStatus].setStatus(Constant.STATUS_SELECTED);
        handleOrderStatusSwitch(orderStatusIds[orderStatus]);
        loadOrderData(orderStatus);
        loadOrderCountData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        SLog.info("BillFragment::onEBMessage()");
        if (message.messageType == EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST) {
            SLog.info("重新加載訂單列表數據, orderStatus[%d]", orderStatus);
            needRefresh = true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.tv_fragment_title) {
            if (Config.DEVELOPER_MODE) {
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);
            }
        } else if (id == R.id.btn_search) {
            start(OrderSearchFragment.newInstance());
        } else if (id == R.id.btn_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 6))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_ORDER))
                    .show();
        }
    }

    /**
     * 處理狀態切換按鈕
     * @param id
     */
    private void handleOrderStatusSwitch(int id) {
        int index = 0;
        for (int i = 0; i < orderStatusIds.length; i++) {
            if (orderStatusIds[i] == id) {
                index = i;
            } else {
                tvOrderStatusArr[i].setStatus(Constant.STATUS_UNSELECTED);
            }
        }

        orderStatus = index;
    }

    private void loadOrderCountData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_ORDER_COUNT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                /*
                all: 全部
                new: 待付款
                pay: 待發貨
                finish: 已完成
                send: 待收貨
                noeval: 待評論
                 */
                try {
                    int allCount = responseObj.getInt("datas.all");
                    int newCount = responseObj.getInt("datas.new");
                    int payCount = responseObj.getInt("datas.pay");
                    int sendCount = responseObj.getInt("datas.send");
                    int noevalCount = responseObj.getInt("datas.noeval");

                    if (allCount > 0) {
                        tvAllCount.setText(String.valueOf(allCount));
                        tvAllCount.setVisibility(View.VISIBLE);
                    } else {
                        tvAllCount.setVisibility(View.GONE);
                    }

                    if (newCount > 0) {
                        tvToBePaidCount.setText(String.valueOf(newCount));
                        tvToBePaidCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBePaidCount.setVisibility(View.GONE);
                    }

                    if (payCount > 0) {
                        tvToBeShippedCount.setText(String.valueOf(payCount));
                        tvToBeShippedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeShippedCount.setVisibility(View.GONE);
                    }

                    if (sendCount > 0) {
                        tvToBeReceivedCount.setText(String.valueOf(sendCount));
                        tvToBeReceivedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeReceivedCount.setVisibility(View.GONE);
                    }

                    if (noevalCount > 0) {
                        tvToBeCommentedCount.setText(String.valueOf(noevalCount));
                        tvToBeCommentedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeCommentedCount.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void loadOrderData(int orderStatus) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("token", token);

            String ordersState = getOrdersState(orderStatus);
            if (!StringUtil.isEmpty(ordersState)) {
                params.set("ordersState", ordersState);
            }
            SLog.info("params[%s]", params);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_ORDER_LIST, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        EasyJSONArray ordersPayVoList = responseObj.getArray("datas.ordersPayVoList");
                        payItemList.clear();
                        for (Object object : ordersPayVoList) { // PayObject
                            EasyJSONObject ordersPayVo = (EasyJSONObject) object;

                            int payId = ordersPayVo.getInt("payId");
                            float payAmount = (float) ordersPayVo.getDouble("ordersOnlineDiffAmount");

                            boolean showPayButton = false;
                            if (Constant.ORDER_STATUS_TO_BE_PAID == orderStatus ||
                                    (Constant.ORDER_STATUS_ALL == orderStatus && payAmount > 0.0001f)) {
                                // 如果是【待付款】Tab的訂單或【全部】Tab里面有付款金額的訂單，都需要付款處理
                                showPayButton = true;
                            }

                            PayItem payItem = null;
                            if (showPayButton) { // 如果是需要支付的支付單，最外層作為一個Item(對應于一個支付單有多個訂單的情況)
                                payItem = new PayItem(payId, payAmount, true);
                            }

                            EasyJSONArray ordersVoList = ordersPayVo.getArray("ordersVoList");
                            for (Object object2 : ordersVoList) { // OrderVo 一一對應于店鋪
                                if (!showPayButton) {
                                    payItem = new PayItem(payId, payAmount, false);
                                }
                                EasyJSONObject ordersVo = (EasyJSONObject) object2;

                                int ordersId = ordersVo.getInt("ordersId");
                                String ordersStateName = ordersVo.getString("ordersStateName");
                                String storeName = ordersVo.getString("storeName");
                                float freightAmount = (float) ordersVo.getDouble("freightAmount");
                                float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

                                int showMemberCancel = ordersVo.getInt("showMemberCancel");
                                int showMemberBuyAgain = ordersVo.getInt("showMemberBuyAgain");
                                int showShipSearch = ordersVo.getInt("showShipSearch");
                                int showEvaluation = ordersVo.getInt("showEvaluation");
                                SLog.info("showMemberCancel[%d], showMemberBuyAgain[%d], showShipSearch[%d], showEvaluation[%d]",
                                        showMemberCancel, showMemberBuyAgain, showShipSearch, showEvaluation);

                                List<OrderSkuItem> orderSkuItemList = new ArrayList<>();
                                // 獲取Sku列表
                                EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
                                for (Object object3 : ordersGoodsVoList) { // Sku
                                    EasyJSONObject ordersGoodsVo = (EasyJSONObject) object3;

                                    String goodsName = ordersGoodsVo.getString("goodsName");
                                    String imageSrc = ordersGoodsVo.getString("imageSrc");
                                    float goodsPrice = (float) ordersGoodsVo.getDouble("goodsPrice");
                                    String goodsFullSpecs = ordersGoodsVo.getString("goodsFullSpecs");
                                    int buyNum = ordersGoodsVo.getInt("buyNum");

                                    orderSkuItemList.add(new OrderSkuItem(goodsName, imageSrc, goodsPrice, goodsFullSpecs, buyNum));
                                }  // END OF Sku


                                OrderItem orderItem = new OrderItem(ordersId, storeName, ordersStateName, freightAmount, ordersAmount,
                                        showMemberCancel == 1, showMemberBuyAgain == 1, showShipSearch == 1,
                                        showEvaluation == 1, orderSkuItemList);

                                payItem.orderItemList.add(orderItem);
                                if (!showPayButton) {
                                    payItemList.add(payItem);
                                }
                            } // END OF Order Object

                            if (showPayButton) { // 如果是需要支付的支付單，最外層作為一個Item
                                payItemList.add(payItem);
                            }
                        } // END OF Pay Object
                        SLog.info("payItemList:count[%d]", payItemList.size());
                        payItemListAdapter.setNewData(payItemList);
                        rvOrderList.setAdapter(payItemListAdapter);
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadOrderData failed");
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 獲取訂單狀態描述文本
     * @param orderStatus
     * @return
     */
    private String getOrdersState(int orderStatus) {
        // new-待付款,pay-待发货,send-待收货,noeval-待评价,finish-已完成,cancel-已取消
        if (orderStatus == Constant.ORDER_STATUS_TO_BE_PAID) {
            return "new";
        } else if (orderStatus == Constant.ORDER_STATUS_TO_BE_SHIPPED) {
            return "pay";
        } else if (orderStatus == Constant.ORDER_STATUS_TO_BE_RECEIVED) {
            return "send";
        } else if (orderStatus == Constant.ORDER_STATUS_TO_BE_COMMENTED) {
            return "noeval";
        }
        return null;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onSelect(TwTabButton tabButton) {
        int id = tabButton.getId();
        handleOrderStatusSwitch(id);
        loadOrderData(orderStatus);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (needRefresh) {
            loadOrderCountData();
            SLog.info("onSupportVisible::orderStatus[%d]", orderStatus);
            loadOrderData(orderStatus);
            needRefresh = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
