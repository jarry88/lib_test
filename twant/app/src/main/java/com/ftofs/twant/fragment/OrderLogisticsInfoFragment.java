package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單物流詳情
 * @author zwm
 */
public class OrderLogisticsInfoFragment extends BaseFragment implements View.OnClickListener {
    int ordersId;

    ImageView goodsImage;
    TextView tvGoodsName;

    TextView tvShipName;

    TextView tvOrdersStateName;
    TextView tvLogisticsNumber;  // 快遞客戶單號
    TextView tvCustomerOrderNumber;  // 受理單號
    TextView tvSendTime;  // 發貨時間

    public static OrderLogisticsInfoFragment newInstance(int ordersId) {
        Bundle args = new Bundle();

        args.putInt("ordersId", ordersId);
        OrderLogisticsInfoFragment fragment = new OrderLogisticsInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_logistics_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        ordersId = args.getInt("ordersId");

        SLog.info("ordersId[%d]", ordersId);

        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);

        tvShipName = view.findViewById(R.id.tv_ship_name);

        tvOrdersStateName = view.findViewById(R.id.tv_orders_state_name);
        tvLogisticsNumber = view.findViewById(R.id.tv_logistics_number);
        tvCustomerOrderNumber = view.findViewById(R.id.tv_customer_order_number);
        tvSendTime = view.findViewById(R.id.tv_send_time);

        Util.setOnClickListener(view, R.id.btn_back, this);

        loadLogisticsDetail();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    /**
     * 加載物流詳情
     */
    private void loadLogisticsDetail() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "ordersId", ordersId);


        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_LOGISTICS_DETAIL, params, new UICallback() {
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

                    String goodsImageUrl = responseObj.getString("datas.ordersVo.ordersGoodsVoList[0].goodsImage");
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);

                    tvGoodsName.setText(responseObj.getString("datas.ordersVo.ordersGoodsVoList[0].goodsName"));

                    tvShipName.setText(responseObj.getString("datas.shipCompany.shipName"));

                    tvOrdersStateName.setText(responseObj.getString("datas.ordersVo.ordersStateName"));
                    tvLogisticsNumber.setText(responseObj.getString("datas.logisticsNumber"));
                    tvCustomerOrderNumber.setText(responseObj.getString("datas.customerOrderNumber"));
                    tvSendTime.setText(responseObj.getString("datas.ordersVo.sendTime"));

                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }
}
