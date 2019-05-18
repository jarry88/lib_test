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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
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
    private static final int BILL_STATUS_ALL = 1;
    private static final int BILL_STATUS_TO_BE_PAID = 2;
    private static final int BILL_STATUS_TO_BE_SHIPPED = 3;
    private static final int BILL_STATUS_TO_BE_RECEIVED = 4;
    private static final int BILL_STATUS_TO_BE_COMMENTED = 5;

    int billStatus = BILL_STATUS_ALL;

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderListAdapter adapter;

    public static BillFragment newInstance() {
        Bundle args = new Bundle();

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

        Util.setOnClickListener(view, R.id.btn_back, this);

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
                }
            }
        });
        rvOrderList.setAdapter(adapter);

        loadBillData(billStatus);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadBillData(int billStatus) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);
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
                    for (Object object : ordersPayVoList) {
                        EasyJSONObject ordersPayVo = (EasyJSONObject) object;

                        int payId = ordersPayVo.getInt("payId");
                        EasyJSONArray ordersVoList = ordersPayVo.getArray("ordersVoList");
                        int len = ordersPayVoList.length();
                        int index = 0;
                        for (Object object2 : ordersVoList) {
                            EasyJSONObject ordersVo = (EasyJSONObject) object2;

                            String ordersStateName = ordersVo.getString("ordersStateName");
                            String storeName = ordersVo.getString("storeName");
                            float freightAmount = (float) ordersVo.getDouble("freightAmount");
                            float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

                            List<OrderSkuItem> orderSkuItemList = new ArrayList<>();

                            // 獲取Sku列表
                            EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
                            for (Object object3 : ordersGoodsVoList) {
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

                        SLog.info("orderItemList:count[%d]", orderItemList.size());
                        adapter.setNewData(orderItemList);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
