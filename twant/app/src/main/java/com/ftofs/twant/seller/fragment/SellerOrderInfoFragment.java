package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.adapter.SellerOrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
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

public class SellerOrderInfoFragment extends BaseFragment implements View.OnClickListener {

    private int refundId;
    private LinearLayout llOrderListContainer;
    private int tariffBuy;
    private int storeId;
    private OrderListAdapter goodsAdapter;
    private List<OrderItem> goodsList = new ArrayList<>();
    LinearLayout rlTaxContainer;
    TextView tvTaxAmount;
    private double ordersAmount;
    private double freightAmount;
    TextView tvOrderAmount;
    TextView tvOrderSn;
    TextView tvPayment;
    TextView tvRefundFreight;
    TextView tvRefundSendSn;
    TextView tvReceiverMobile;
    TextView tvReceiverAddress;
    TextView tvRefundPayTime;
    TextView tvReceiverName;
    private boolean isReturn = false;
    private List<Goods> orderGoodsList = new ArrayList<>();

    RecyclerView rvRefundRelativeGoodsList;

    public static SellerOrderInfoFragment newInstance(int refundId, boolean isReturn) {
        SellerOrderInfoFragment fragment = new SellerOrderInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.refundId = refundId;
        fragment.isReturn = isReturn;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_refund_goods_info, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRefundRelativeGoodsList = view.findViewById(R.id.rv_refund_relative_goods_list);
        tvReceiverName = view.findViewById(R.id.tv_receiver_name);
        tvReceiverAddress = view.findViewById(R.id.tv_receiver_address);
        tvRefundPayTime = view.findViewById(R.id.tv_refund_pay_time);
        tvReceiverMobile = view.findViewById(R.id.tv_receiver_mobile);
        tvRefundSendSn = view.findViewById(R.id.tv_refund_send_sn);
        tvRefundFreight = view.findViewById(R.id.tv_refund_freight);
        rlTaxContainer = view.findViewById(R.id.rl_tax_container);
        tvPayment = view.findViewById(R.id.tv_refund_payment);
        tvOrderSn = view.findViewById(R.id.tv_order_sn);
        tvTaxAmount = view.findViewById(R.id.tv_tax_amount);
        tvOrderAmount = view.findViewById(R.id.tv_orders_amount);
        Util.setOnClickListener(view, R.id.btn_back, this);
        loadData();
    }

    private void loadData() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
        SLog.info("params[%s]", params);
        String path = Api.PATH_SELLER_REFUND_ORDERS_INFO;
        if (isReturn) {
            path = Api.PATH_SELLER_RETURN_ORDERS_INFO;
        }
        Api.getUI(path + "/" + refundId, params, new UICallback() {
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
                    EasyJSONObject ordersVo = responseObj.getObject("datas.ordersVo");
                    updateView(ordersVo);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });

    }

    private void updateView(EasyJSONObject ordersVo) throws Exception {
        String receiverName = ordersVo.getSafeString("receiverName");
        String mobile = ordersVo.getSafeString("receiverPhone");
        String address = ordersVo.getSafeString("receiverAreaInfo") + "\n" + ordersVo.getSafeString("receiverAddress");
        if (ordersVo.exists("tariffBuy")) {
            tariffBuy = ordersVo.getInt("tariffBuy");
            double tariffAmount = 0;
            if (tariffBuy == Constant.TRUE_INT) {
                tariffAmount = ordersVo.getDouble("taxAmount");
                rlTaxContainer.setVisibility(View.VISIBLE);
                tvTaxAmount.setText(StringUtil.formatPrice(_mActivity, tariffAmount, 1));
            }
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
        if (ordersGoodsVoList != null) {
            for (Object object : ordersGoodsVoList) {
                EasyJSONObject orderGood = (EasyJSONObject) object;
                orderGoodsList.add(Goods.parse(orderGood));
                orderItemList.add(OrderItem.parse(orderGood));
            }
        }
        SellerOrderListAdapter adapter = new SellerOrderListAdapter(R.layout.common_sku_item, orderGoodsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvRefundRelativeGoodsList.setLayoutManager(linearLayoutManager);
        rvRefundRelativeGoodsList.setAdapter(adapter);
        storeId = ordersVo.getInt("storeId");
        ordersAmount = ordersVo.getDouble("ordersAmount");
        freightAmount = ordersVo.getDouble("freightAmount");
        tvOrderAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1));
        tvRefundFreight.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1));
        tvRefundSendSn.setText(ordersVo.getSafeString("shipSn"));
        tvOrderSn.setText(ordersVo.getSafeString("ordersSnStr"));
        tvPayment.setText(ordersVo.getSafeString("paymentName"));
        tvRefundPayTime.setText(ordersVo.getSafeString("createTime"));
        tvReceiverName.setText(ordersVo.getSafeString("receiverName"));
        tvReceiverMobile.setText(ordersVo.getSafeString("receiverPhone"));
        tvReceiverAddress.setText(address);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }
}
