package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 訂單頁面
 * @author zwm
 */
public class BillFragment extends BaseFragment implements View.OnClickListener {
    int billStatus;

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderListAdapter adapter;
    TextView[] tvOrderStatusArr = new TextView[5];
    int[] orderStatusIds = new int[] {R.id.btn_bill_all, R.id.btn_bill_to_be_paid, R.id.btn_bill_to_be_shipped,
           R.id.btn_bill_to_be_received, R.id.btn_bill_to_be_commented};

    int twRed;
    int twBlack;

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

        Bundle args = getArguments();
        billStatus = args.getInt("billStatus", Constant.ORDER_STATUS_ALL);
        SLog.info("billStatus[%d]", billStatus);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);

        Util.setOnClickListener(view, R.id.btn_back, this);
        int index = 0;
        for (int id : orderStatusIds) {
            TextView tvOrderStatus = view.findViewById(id);
            tvOrderStatusArr[index] = tvOrderStatus;
            tvOrderStatus.setOnClickListener(this);
            ++index;
        }

        RecyclerView rvOrderList = view.findViewById(R.id.rv_order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderList.setLayoutManager(layoutManager);
        adapter = new OrderListAdapter(_mActivity, R.layout.order_item, orderItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);
                if (id == R.id.btn_pay_order) {
                    int payId = (int) view.getTag();
                    SLog.info("payId[%d]", payId);
                    MainFragment mainFragment = MainFragment.getInstance();
                    mainFragment.start(ICBCFragment.newInstance(String.valueOf(payId)));
                }
            }
        });
        rvOrderList.setAdapter(adapter);

        handleOrderStatusSwitch(orderStatusIds[billStatus]);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }

        if (handleOrderStatusSwitch(id)) {
            loadBillData(billStatus);
            return;
        }
    }

    /**
     * 處理狀態切換按鈕
     * @param id
     * @return 事件是否被處理
     */
    private boolean handleOrderStatusSwitch(int id) {
        boolean consumed = false;
        int index = 0;
        for (int i = 0; i < orderStatusIds.length; i++) {
            if (orderStatusIds[i] == id) {
                index = i;
                consumed = true;
                break;
            }
        }

        if (!consumed) {
            return false;
        }

        billStatus = index;
        for (int i = 0; i < orderStatusIds.length; i++) {
            int textColor = twBlack;
            if (index == i) {
                textColor = twRed;
            }

            tvOrderStatusArr[i].setTextColor(textColor);
        }

        loadBillData(billStatus);
        return true;
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

            Api.postUI(Api.PATH_ORDER_LIST, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
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
                            int len = ordersPayVoList.length();
                            int index = 0;
                            for (Object object2 : ordersVoList) { // OrderVo
                                EasyJSONObject ordersVo = (EasyJSONObject) object2;

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
                                }

                                OrderItem orderItem = new OrderItem(storeName, ordersStateName, freightAmount, ordersAmount, orderSkuItemList);
                                // 最后一個顯示【支付訂單】按鈕
                                if (index == len -1) {
                                    orderItem.setShowPayButton(true);
                                    orderItem.setPayId(payId);
                                }

                                orderItemList.add(orderItem);
                                ++index;
                            }
                        }
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
}
