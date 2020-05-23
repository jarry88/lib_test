package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.OrderItemListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.widget.ScaledButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.ISupportFragment;
import okhttp3.Call;

public class SellerOrderInfoFragment extends BaseFragment {

    private int refundId;
    private LinearLayout llOrderListContainer;
    private int tariffBuy;
    private int storeId;

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @BindView(R.id.rv_refund_relative_goods_list)
    RecyclerView rv_refund_relative_goods_list;
    public static SellerOrderInfoFragment newInstance(int refundId) {
        SellerOrderInfoFragment fragment = new SellerOrderInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.refundId = refundId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_refund_goods_info, container, false);
        unbinder=ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private Unbinder unbinder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llOrderListContainer = view.findViewById(R.id.ll_order_list_container);
        loadData();
    }

    private void loadData() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SELLER_ORDERS_INFO+"/"+refundId, params, new UICallback() {
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
                         EasyJSONObject ordersVo =responseObj.getObject("datas.ordersVo");
                         updateView(ordersVo);
                     } catch (Exception e) {
                         SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                     }
                 }
             });

    }

    private void updateView(EasyJSONObject ordersVo) throws Exception{
        String receiverName = ordersVo.getSafeString("receiverName");
        String mobile = ordersVo.getSafeString("receiverPhone");
        String address = ordersVo.getSafeString("receiverAreaInfo") + ordersVo.getSafeString("receiverAddress");
        tariffBuy =ordersVo.getInt("tariffBuy");
        double tariffAmount =0;
        if (tariffBuy == Constant.TRUE_INT) {
            tariffAmount = ordersVo.getDouble("taxAmount");
//            rlTaxContainer.setVisibility(View.VISIBLE);

        }
        List<OrderItem> orderItemList=new ArrayList<>();

        OrderItemListAdapter adapter = new OrderItemListAdapter(_mActivity, llOrderListContainer, R.layout.order_item, false);
        EasyJSONArray ordersGoodsVoList = ordersVo.getArray("ordersGoodsVoList");
        if (ordersGoodsVoList != null) {
            for (Object object : ordersGoodsVoList) {
                EasyJSONObject orderGood = (EasyJSONObject) object;
                orderItemList.add(OrderItem.parse(orderGood));
            }
        }
        adapter.setData(orderItemList);
        storeId = ordersVo.getInt("storeId");

    }
}
