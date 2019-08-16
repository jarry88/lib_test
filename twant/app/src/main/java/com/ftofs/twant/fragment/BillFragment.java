package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
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
 * 訂單頁面
 * @author zwm
 */
public class BillFragment extends BaseFragment implements View.OnClickListener, TwTabButton.TtbOnSelectListener {
    int billStatus;

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderListAdapter adapter;
    TwTabButton[] tvOrderStatusArr = new TwTabButton[5];
    int[] orderStatusIds = new int[] {R.id.btn_bill_all, R.id.btn_bill_to_be_paid, R.id.btn_bill_to_be_shipped,
           R.id.btn_bill_to_be_received, R.id.btn_bill_to_be_commented};

    int twRed;
    int twBlack;
    boolean needRefresh;

    public static BillFragment newInstance(int billStatus) {
        Bundle args = new Bundle();

        args.putInt("billStatus", billStatus);
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
        billStatus = args.getInt("billStatus", Constant.ORDER_STATUS_ALL);
        SLog.info("billStatus[%d]", billStatus);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);

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

        RecyclerView rvOrderList = view.findViewById(R.id.rv_order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(layoutManager);
        adapter = new OrderListAdapter(_mActivity, R.layout.order_item, orderItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SLog.info("onItemClick");

                OrderItem orderItem = orderItemList.get(position);

                Util.startFragment(OrderDetailFragment.newInstance(orderItem.orderId));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_pay_order) {
                    int payId = (int) view.getTag();
                    SLog.info("payId[%d]", payId);
                    Util.startFragment(ICBCFragment.newInstance(String.valueOf(payId)));
                }
            }
        });
        rvOrderList.setAdapter(adapter);

        tvOrderStatusArr[billStatus].setStatus(Constant.STATUS_SELECTED);
        handleOrderStatusSwitch(orderStatusIds[billStatus]);
        loadBillData(billStatus);
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
            SLog.info("重新加載訂單列表數據, billStatus[%d]", billStatus);
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

        billStatus = index;
    }

    private void loadBillData(int billStatus) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("token", token);

            String ordersState = getOrdersState(billStatus);
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
                        orderItemList.clear();
                        for (Object object : ordersPayVoList) { // PayObject
                            EasyJSONObject ordersPayVo = (EasyJSONObject) object;

                            int payId = ordersPayVo.getInt("payId");
                            EasyJSONArray ordersVoList = ordersPayVo.getArray("ordersVoList");
                            int len = ordersVoList.length();
                            int index = 0;
                            for (Object object2 : ordersVoList) { // OrderVo
                                EasyJSONObject ordersVo = (EasyJSONObject) object2;

                                int ordersId = ordersVo.getInt("ordersId");
                                String ordersStateName = ordersVo.getString("ordersStateName");
                                String storeName = ordersVo.getString("storeName");
                                float freightAmount = (float) ordersVo.getDouble("freightAmount");
                                float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

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

                                OrderItem orderItem = new OrderItem(ordersId, storeName, ordersStateName, freightAmount, ordersAmount, orderSkuItemList);
                                // 最后一個顯示【支付訂單】按鈕
                                if (index == len -1) {
                                    orderItem.setShowPayButton(true);
                                    orderItem.setPayId(payId);
                                }

                                orderItemList.add(orderItem);
                                ++index;
                            } // END OF Order Object
                        } // END OF Pay Object
                        SLog.info("orderItemList:count[%d]", orderItemList.size());
                        adapter.setNewData(orderItemList);
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadBillData failed");
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
        loadBillData(billStatus);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (needRefresh) {
            SLog.info("onSupportVisible::billStatus[%d]", billStatus);
            loadBillData(billStatus);
            needRefresh = false;
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
