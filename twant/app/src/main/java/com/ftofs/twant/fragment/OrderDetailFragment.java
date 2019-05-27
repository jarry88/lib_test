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

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AreaPopupAdapter;
import com.ftofs.twant.adapter.OrderDetailGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.Receipt;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    TextView tvReceiverName;
    TextView tvMobile;
    TextView tvAddress;
    TextView tvStoreName;
    TextView tvOrdersStateName;
    TextView tvFreightAmount;
    TextView tvOrdersAmount;
    TextView tvShipTime;
    TextView tvOrdersSn;
    TextView tvCreateTime;

    public static OrderDetailFragment newInstance(int ordersId) {
        Bundle args = new Bundle();

        args.putInt("ordersId", ordersId);

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

        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvAddress = view.findViewById(R.id.tv_address);
        tvStoreName = view.findViewById(R.id.tv_store_name);
        tvOrdersStateName = view.findViewById(R.id.tv_orders_state_name);
        tvFreightAmount = view.findViewById(R.id.tv_freight_amount);
        tvOrdersAmount = view.findViewById(R.id.tv_orders_amount);
        tvShipTime = view.findViewById(R.id.tv_ship_time);
        tvOrdersSn = view.findViewById(R.id.tv_orders_sn);
        tvCreateTime = view.findViewById(R.id.tv_create_time);

        RecyclerView rvOrderDetailGoodsList = view.findViewById(R.id.rv_order_detail_goods_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOrderDetailGoodsList.setLayoutManager(layoutManager);
        adapter = new OrderDetailGoodsAdapter(_mActivity, R.layout.order_detail_goods_item, orderDetailGoodsItemList);
        rvOrderDetailGoodsList.setAdapter(adapter);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadOrderDetail();
    }


        @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            default:
                break;
        }
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
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

                    tvReceiverName.setText(getString(R.string.text_receiver) + ":  " + receiverName);
                    tvMobile.setText(mobile);
                    tvAddress.setText(address);
                    tvStoreName.setText(storeName);
                    tvOrdersStateName.setText(ordersStateName);
                    tvFreightAmount.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1));
                    tvOrdersAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1));
                    tvShipTime.setText(shipTime);
                    tvOrdersSn.setText(String.valueOf(ordersSn));
                    tvCreateTime.setText(createTime);

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
