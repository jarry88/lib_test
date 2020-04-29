package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerOrderItem;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家訂單詳情頁面
 * @author zwm
 */
public class SellerOrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int orderId;
    String orderSn;
    String buyerNickname;
    String buyerMemberName;

    public static SellerOrderDetailFragment newInstance(int orderId) {
        Bundle args = new Bundle();

        SellerOrderDetailFragment fragment = new SellerOrderDetailFragment();
        fragment.setArguments(args);
        fragment.setParams(orderId);

        return fragment;
    }

    public void setParams(int orderId) {
        this.orderId = orderId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadData();
    }


    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_ORDER_DETAIL + "/" + orderId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
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

                    View itemView = getView();
                    if (itemView == null) {
                        return;
                    }

                    EasyJSONObject ordersVo = responseObj.getSafeObject("datas.ordersVo");

                    ((TextView) itemView.findViewById(R.id.tv_order_status_desc)).setText(ordersVo.getSafeString("ordersStateName"));

                    String paymentInfoStr = String.format("買家已使用「%s」方式成功對訂單進行支付，支付單號 「%s」。",
                            responseObj.getSafeString("paymentName"), ordersVo.getSafeString("paySnStr"));
                    ((TextView) itemView.findViewById(R.id.tv_payment_info)).setText(paymentInfoStr);

                    orderSn = ordersVo.getSafeString("ordersSnStr");
                    ((TextView) itemView.findViewById(R.id.tv_order_sn)).setText(orderSn);


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
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }
}

