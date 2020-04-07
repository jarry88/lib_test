package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PayItemListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.OrderOperation;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.entity.PayItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.view.CustomerLinearLayoutManager;
import com.ftofs.twant.widget.BlackDropdownMenu;
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
 * 訂單列表頁面 和 訂單搜索頁面
 * @author zwm
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener,
        TwTabButton.TtbOnSelectListener, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 用途：訂單列表
     */
    public static final int USAGE_LIST = 1;
    /**
     * 用途：訂單搜索
     */
    public static final int USAGE_SEARCH = 2;


    /**
     * 用于哪種用途
     */
    int usage;

    int orderStatus;

    // 當前加載第幾頁
    int currPage = 0;
    boolean hasMore;
    int selTabId; // 当前选中的Tab的Id
    String keyword;  // 搜索關鍵字（產品標題或訂單號），訂單搜索時才用到

    RecyclerView rvOrderList;

    List<PayItem> payItemList = new ArrayList<>();

    PayItemListAdapter payItemListAdapter;
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

    EditText etKeyword;

    public static OrderFragment newInstance(int orderStatus, int usage) {
        Bundle args = new Bundle();

        args.putInt("orderStatus", orderStatus);
        args.putInt("usage", usage);

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        orderStatus = args.getInt("orderStatus", Constant.ORDER_STATUS_ALL);
        usage = args.getInt("usage");
        SLog.info("orderStatus[%d], usage[%d]", orderStatus, usage);
        selTabId = orderStatusIds[orderStatus];

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);

        if (usage == USAGE_LIST) {
            view.findViewById(R.id.ll_order_list_toolbar_container).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ll_order_search_toolbar_container).setVisibility(View.GONE);

            tvAllCount = view.findViewById(R.id.tv_all_count);
            tvToBePaidCount = view.findViewById(R.id.tv_to_be_paid_count);
            tvToBeShippedCount = view.findViewById(R.id.tv_to_be_shipped_count);
            tvToBeReceivedCount = view.findViewById(R.id.tv_to_be_received_count);
            tvToBeCommentedCount = view.findViewById(R.id.tv_to_be_commented_count);

            Util.setOnClickListener(view, R.id.tv_fragment_title, this);
            Util.setOnClickListener(view, R.id.btn_back_list, this);
            Util.setOnClickListener(view, R.id.btn_search, this);
            Util.setOnClickListener(view, R.id.btn_menu, this);

            int index = 0;
            for (int id : orderStatusIds) {
                TwTabButton tvOrderStatus = view.findViewById(id);
                tvOrderStatusArr[index] = tvOrderStatus;
                tvOrderStatus.setTtbOnSelectListener(this);
                ++index;
            }
        } else {
            view.findViewById(R.id.ll_order_list_toolbar_container).setVisibility(View.GONE);
            view.findViewById(R.id.ll_order_search_toolbar_container).setVisibility(View.VISIBLE);

            etKeyword = view.findViewById(R.id.et_keyword);
            etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        keyword = v.getText().toString().trim();
                        if (StringUtil.isEmpty(keyword)) {
                            ToastUtil.error(_mActivity, getString(R.string.input_order_search_hint));
                            return true;
                        }
                        // doSearch(keyword);
                        currPage = 0;
                        loadOrderData(Constant.ORDER_STATUS_ALL, currPage + 1);
                        return true;
                    }

                    return false;
                }
            });
            Util.setOnClickListener(view, R.id.btn_back_search, this);
        }

        rvOrderList = view.findViewById(R.id.rv_order_list);
        CustomerLinearLayoutManager layoutManager = new CustomerLinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(layoutManager);
        payItemListAdapter = new PayItemListAdapter(_mActivity, payItemList, this);
        payItemListAdapter.setEnableLoadMore(true);
        payItemListAdapter.setOnLoadMoreListener(this, rvOrderList);
        payItemListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_pay_order) {
                    PayItem payItem = payItemList.get(position);
                    start(PayVendorFragment.newInstance(payItem.payId, payItem.payAmount, 0));
                }
            }
        });

        // 設置空頁面
        payItemListAdapter.setEmptyView(R.layout.layout_placeholder_no_order, rvOrderList);
        rvOrderList.setAdapter(payItemListAdapter);

        if (usage == USAGE_LIST) {
            tvOrderStatusArr[orderStatus].setStatus(Constant.STATUS_SELECTED);
            handleOrderStatusSwitch(orderStatusIds[orderStatus]);
            loadOrderData(orderStatus, currPage + 1);
            loadOrderCountData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        SLog.info("OrderFragment::onEBMessage()");
        if (message.messageType == EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST) {
            SLog.info("重新加載訂單列表數據, orderStatus[%d]", orderStatus);
            needRefresh = true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back_list || id == R.id.btn_back_search) {
            hideSoftInputPop();
        } else if (id == R.id.tv_fragment_title) {
            if (Config.DEVELOPER_MODE) {
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);
            }
        } else if (id == R.id.btn_search) {
            start(OrderFragment.newInstance(Constant.ORDER_STATUS_ALL, OrderFragment.USAGE_SEARCH));
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

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
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
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadOrderData(int orderStatus, int page) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "page", page);

            if (usage == USAGE_LIST) {
                String ordersState = getOrdersState(orderStatus);
                if (!StringUtil.isEmpty(ordersState)) {
                    params.set("ordersState", ordersState);
                }
            } else {
                params.set("keyword", keyword);
            }

            SLog.info("params[%s]", params);

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            payItemListAdapter.loadMoreEnd();
                            payItemListAdapter.setEnableLoadMore(false);
                        }

                        SLog.info("PAGE[%d]", page);
                        if (page == 1) {  // 如果是第1頁，清空原有數據
                            payItemList.clear();
                        }
                        EasyJSONArray ordersPayVoList = responseObj.getSafeArray("datas.ordersPayVoList");
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

                            EasyJSONArray ordersVoList = ordersPayVo.getSafeArray("ordersVoList");
                            for (Object object2 : ordersVoList) { // OrderVo 一一對應于商店
                                if (!showPayButton) {
                                    payItem = new PayItem(payId, payAmount, false);
                                }
                                EasyJSONObject ordersVo = (EasyJSONObject) object2;

                                int ordersId = ordersVo.getInt("ordersId");
                                String ordersStateName = ordersVo.getSafeString("ordersStateName");
                                String storeName = ordersVo.getSafeString("storeName");
                                float freightAmount = (float) ordersVo.getDouble("freightAmount");
                                float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

                                int showMemberCancel = ordersVo.getInt("showMemberCancel");
                                int showMemberBuyAgain = ordersVo.getInt("showMemberBuyAgain");
                                int showShipSearch = ordersVo.getInt("showShipSearch");
                                int showEvaluation = ordersVo.getInt("showEvaluation");
                                int showMemberReceive = ordersVo.getInt("showMemberReceive");
                                /*
                                SLog.info("showMemberCancel[%d], showMemberBuyAgain[%d], showShipSearch[%d], showEvaluation[%d], showMemberReceive[%d]",
                                        showMemberCancel, showMemberBuyAgain, showShipSearch, showEvaluation, showMemberReceive);
                                        */

                                List<OrderSkuItem> orderSkuItemList = new ArrayList<>();
                                // 獲取Sku列表
                                EasyJSONArray ordersGoodsVoList = ordersVo.getSafeArray("ordersGoodsVoList");
                                for (Object object3 : ordersGoodsVoList) { // Sku
                                    EasyJSONObject ordersGoodsVo = (EasyJSONObject) object3;

                                    int commonId = ordersGoodsVo.getInt("commonId");
                                    int goodsId = ordersGoodsVo.getInt("goodsId");
                                    String goodsName = ordersGoodsVo.getSafeString("goodsName");
                                    String imageSrc = ordersGoodsVo.getSafeString("imageSrc");
                                    double goodsPrice =  ordersGoodsVo.getDouble("goodsPrice");
                                    String goodsFullSpecs = ordersGoodsVo.getSafeString("goodsFullSpecs");
                                    int buyNum = ordersGoodsVo.getInt("buyNum");

                                    orderSkuItemList.add(new OrderSkuItem(commonId, goodsId, goodsName, imageSrc, goodsPrice, goodsFullSpecs, buyNum));
                                }  // END OF Sku

                                // 獲取贈品列表
                                List<GiftItem> giftItemList = new ArrayList<>();
                                EasyJSONArray ordersGiftVoList = ordersVo.getSafeArray("ordersGiftVoList");
                                for (Object object3 : ordersGiftVoList) {
                                    EasyJSONObject ordersGiftVo = (EasyJSONObject) object3;
                                    GiftItem giftItem = new GiftItem();
                                    giftItem.commonId = ordersGiftVo.getInt("commonId");
                                    giftItem.goodsId = ordersGiftVo.getInt("goodsId");
                                    giftItem.giftNum = ordersGiftVo.getInt("giftNum");
                                    giftItem.goodsName = ordersGiftVo.getSafeString("goodsName");

                                    giftItemList.add(giftItem);
                                }



                                OrderItem orderItem = new OrderItem(ordersId, storeName, ordersStateName, freightAmount, ordersAmount,
                                        showMemberCancel == 1, showMemberBuyAgain == 1, showShipSearch == 1,
                                        showEvaluation == 1, showMemberReceive == 1, orderSkuItemList, giftItemList);

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
                        if (!hasMore && payItemList.size() > 0) {
                            // 如果全部加載完畢，添加加載完畢的提示
                            payItemList.add(new PayItem(Constant.ITEM_TYPE_LOAD_END_HINT));
                        }

                        payItemListAdapter.setNewData(payItemList);
                        payItemListAdapter.loadMoreComplete();

                        currPage++;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSelect(TwTabButton tabButton) {
        int id = tabButton.getId();
        if (id == selTabId) {
            SLog.info("重复点击");
            return;
        }

        handleOrderStatusSwitch(id);
        currPage = 0;
        payItemList.clear();
        payItemListAdapter.notifyDataSetChanged();
        payItemListAdapter.setEnableLoadMore(true);
        loadOrderData(orderStatus, currPage + 1);

        selTabId = id;
    }

    private void reloadData() {
        loadOrderCountData();
        SLog.info("onSupportVisible::orderStatus[%d]", orderStatus);
        currPage = 0;
        loadOrderData(orderStatus, currPage + 1);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("needRefresh[%s]", needRefresh);
        if (needRefresh) {
            rvOrderList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SLog.info("reloadData___________________________________");
                    reloadData();
                    needRefresh = false;
                }
            }, 1500);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    public void orderOperation(OrderOperation operationType, int ordersId) {
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
                            rvOrderList.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    reloadData();
                                }
                            }, 1500);
                        } else if(operationType == OrderOperation.ORDER_OPERATION_TYPE_DELETE) {
                            ToastUtil.success(_mActivity, "刪除訂單成功");
                        } else if (operationType == OrderOperation.ORDER_OPERATION_TYPE_BUY_AGAIN) {
                            ToastUtil.success(_mActivity, "訂單已添加到購物袋");
                            start(CartFragment.newInstance(true));
                        }
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
    public void confirmReceive(int ordersId) {
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
                    rvOrderList.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reloadData();
                        }
                    }, 1500);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            payItemListAdapter.setEnableLoadMore(false);
            return;
        }
        loadOrderData(orderStatus, currPage + 1);
    }
}



